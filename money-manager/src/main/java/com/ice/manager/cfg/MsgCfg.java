package com.ice.manager.cfg;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import misc.Misc;

@Service("msgCfg")
public class MsgCfg
{
	Logger logger = LoggerFactory.getLogger("MsgCfg");

	/** action在登录后方可操作. */
	public static final int ACTION_OPTION_LOGIN = 1 << 0;
	/** action操作记入用户日志. */
	public static final int ACTION_OPTION_LOG = 1 << 1;

	public static final HashMap<String, CallBackStub> cbsws = new HashMap<>();

	/** 初始化配置文件. */
	public boolean init()
	{
		File[] files = new File(Cfg.pwd + "msg/").listFiles();
		for (int i = 0; i < files.length; i++)
		{
			List<CallBackStub> arr = this.loadCbs(files[i].getPath());
			if (arr == null)
				return false;
			for (CallBackStub cb : arr)
			{
				if (!this.addCbs(cb))
					return false;
			}
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<CallBackStub> loadCbs(String path)
	{
		try
		{
			List<CallBackStub> arr = new ArrayList<>();
			Document root = new SAXReader().read(path);
			Element e = root.getRootElement();
			List<Element> elements = e.elements("msgs");
			for (int i = 0; i < elements.size(); i++)
			{
				Element tmp = elements.get(i);
				Class<?> cls = Class.forName(tmp.attributeValue("class"));
				List<Element> msgs = tmp.elements("msg");
				for (Element msg : msgs)
				{
					Method mth = Misc.findMethodByName(cls, msg.attributeValue("method"));
					boolean login = !"false".equals(msg.attributeValue("login"));
					int bitset = 0x00000000;
					if (login)
						bitset |= MsgCfg.ACTION_OPTION_LOGIN;
					arr.add(new CallBackStub(mth, cls, bitset));
				}
			}
			return arr;
		} catch (Exception e)
		{
			if (logger.isErrorEnabled())
				logger.error("xml parse error");
			return null;
		}
	}

	/** 尝试的添加一个新的消息回调. */
	public boolean addCbs(CallBackStub cb)
	{
		if (MsgCfg.cbsws.containsKey(cb.mth.getName()))
		{
			if (logger.isErrorEnabled())
				logger.error("Duplicate CallBackStub %s", cb);
			return false;
		}
		if (logger.isTraceEnabled())
			logger.trace("register a action,cbs: {}", Misc.obj2json(cb));
		MsgCfg.cbsws.put(cb.mth.getName(), cb);
		return true;
	}

	public static class CallBackStub
	{
		/** 回调函数. */
		public Method mth;
		/** 类名. */
		public Class<?> cls;
		/** 可选项. */
		public int option;

		public CallBackStub(Method mth, Class<?> cls, int option)
		{
			this.mth = mth;
			this.cls = cls;
			this.option = option;
		}
	}
}
