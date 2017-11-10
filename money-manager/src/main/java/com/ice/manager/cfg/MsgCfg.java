package com.ice.manager.cfg;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import misc.Misc;

public class MsgCfg
{

	/** 初始化配置文件. */
	public boolean init()
	{
		File[] files = new File(Cfg.pwd + "msg/").listFiles();
		for (int i = 0; i < files.length; i++)
		{
			List<CallBackStub> arr = this.loadCbs(files[i].getPath());
			if (arr == null)
				return false;
			
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
					arr.add(new CallBackStub(mth, cls));
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static class CallBackStub
	{
		/** 回调函数. */
		public Method mth;
		/** 类名. */
		public Class<?> cls;

		public CallBackStub(Method mth, Class<?> cls)
		{
			this.mth = mth;
			this.cls = cls;
		}
	}
}
