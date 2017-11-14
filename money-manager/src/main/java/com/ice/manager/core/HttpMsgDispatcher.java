package com.ice.manager.core;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.ice.manager.cfg.MsgCfg;
import com.ice.manager.cfg.MsgCfg.CallBackStub;
import com.ice.manager.http.HttpTrans;
import com.ice.manager.http.Rsp.RspErr;

import misc.Log;
import misc.Misc;

@Controller
public class HttpMsgDispatcher
{
	Logger log = LoggerFactory.getLogger("HttpMsgDispatcher");

	@Autowired
	private SpringContextUtil springContextUtil;

	@Resource
	public CoreManager manager;

	/** 服务器入口. */
	@ResponseBody
	@RequestMapping(value = "/money", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public DeferredResult<String> dispatcherGet(HttpServletRequest request, String sign, String salt, String action) throws Exception
	{
		DeferredResult<String> def = new DeferredResult<>();
		HttpTrans trans = new HttpTrans(request, def);
		//
		String st = Misc.trim(salt);/* 盐值. */
		String s = Misc.trim(sign);/* 签名. */
		String a = Misc.trim(action);/* 请求的功能. */
		if (st == null || s == null || a == null)
		{
			if (log.isDebugEnabled())
				log.debug("missing parameter, req: {}", trans.req.getAction());
			return trans.end(RspErr.ERR_MISSING_PARAMETER);
		}
		CallBackStub cbs = MsgCfg.cbsws.get(a);
		if (cbs == null)
		{
			if (log.isDebugEnabled())
				log.debug("can not found action: {},req: {}", a, trans.req.getAction());
			return trans.end(RspErr.ERR_NOT_FOUND_API);
		}
		/** -------------------------------- */
		/**                                  */
		/** 鉴权通过前的操作. */
		/**                                  */
		/** -------------------------------- */
		if ((cbs.option & MsgCfg.ACTION_OPTION_LOGIN) == 0)
		{
			manager.mab.future(v -> noAuth(cbs, trans, sign, salt, action));
			return def;
		}
		/** -------------------------------- */
		/**                                  */
		/** 鉴权通过后的操作. */
		/**                                  */
		/** -------------------------------- */
		String token = trans.getParStr("token");
		if (token == null)
		{
			if (log.isDebugEnabled())
				log.debug("missing parameter, req: {}", request.getQueryString());
			return trans.endNoLog(RspErr.ERR_MISSING_PARAMETER);
		}
		manager.mab.future(v -> passedAuth(cbs, request, trans, request.getQueryString(), sign, salt, token, action));/* 鉴权之后. */
		return def;
	}

	/** 鉴权通过前可访问的功能. */
	private void noAuth(CallBackStub cbs, HttpTrans trans, String sign, String salt, String action)
	{
		try
		{
			cbs.mth.invoke(springContextUtil.getBean(cbs.cls), trans, sign, salt, action);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			if (log.isErrorEnabled())
				log.error("query: {},exception: {}", trans.req.getAction(), Log.trace(e));
			trans.endNoLog(RspErr.ERR_SYSTEM_EXCEPTION);
		}
	}

	/** 鉴权通过后可访问的功能. */
	private void passedAuth(CallBackStub cbs, HttpServletRequest request, HttpTrans trans, String query, String sign, String salt, String token, String action)
	{
		// if (!Crypto.sha1StrLowerCase((salt + token + action).getBytes()).equals(sign))
		// {
		// if (log.isDebugEnabled())
		// Log.debug("sign error, req: %s", query);
		// trans.end(RspErr.ERR_SIGN);
		// }
		try
		{
			cbs.mth.invoke(springContextUtil.getBean(cbs.cls), request, trans);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			if (log.isErrorEnabled())
				log.error("query: {},exception: {}", trans.req.getAction(), Log.trace(e));
			trans.endNoLog(RspErr.ERR_SYSTEM_EXCEPTION);
		}
	}

	/** 服务器入口. */
	@ResponseBody
	@RequestMapping(value = "/money", method = RequestMethod.POST, headers = "Content-Type=multipart/form-data")
	public DeferredResult<String> dispatcherPost(HttpServletRequest request, String sign, String salt, String action) throws Exception
	{
		DeferredResult<String> def = new DeferredResult<>();
		HttpTrans trans = new HttpTrans(request, def);
		//
		String st = Misc.trim(salt);/* 盐值. */
		String s = Misc.trim(sign);/* 签名. */
		String a = Misc.trim(action);/* 请求的功能. */
		if (st == null || s == null || a == null)
		{
			if (log.isDebugEnabled())
				log.debug("missing parameter, req: {}", trans.req.getAction());
			return trans.end(RspErr.ERR_MISSING_PARAMETER);
		}
		CallBackStub cbs = MsgCfg.cbsws.get(a);
		if (cbs == null)
		{
			if (log.isDebugEnabled())
				log.debug("can not found action: {},req: {}", a, trans.req.getAction());
			return trans.end(RspErr.ERR_NOT_FOUND_API);
		}
		// String token = trans.getParStr("token");
		// if (!Crypto.sha1StrLowerCase((salt + token + action).getBytes()).equals(sign))
		// {
		// if (log.isDebugEnabled())
		// Log.debug("sign error, req: %s", request.getQueryString());
		// trans.end(RspErr.ERR_SIGN);
		// }
		try
		{
			cbs.mth.invoke(springContextUtil.getBean(cbs.cls), request, trans);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
		{
			if (log.isErrorEnabled())
				log.error("query: {},exception: {}", trans.req.getAction(), Log.trace(e));
			trans.endNoLog(RspErr.ERR_SYSTEM_EXCEPTION);
		}
		return def;
	}

}
