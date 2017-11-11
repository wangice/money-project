package com.ice.manager.core;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import com.ice.manager.cfg.MsgCfg;
import com.ice.manager.cfg.MsgCfg.CallBackStub;
import com.ice.manager.http.HttpTrans;
import com.ice.manager.http.Rsp.RspErr;

import misc.Misc;

@Controller
public class HttpMsgDispatcher
{
	Logger log = LoggerFactory.getLogger("HttpMsgDispatcher");

	/** 服务器入口. */
	@ResponseBody
	@RequestMapping(value = "/money", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
	public DeferredResult<String> dispatcher(HttpServletRequest request, String sign, String salt, String action) throws Exception
	{
		DeferredResult<String> def = new DeferredResult<>();
		HttpTrans trans = new HttpTrans(request, def);
		//
		String st = Misc.trim(salt);/* 盐值. */
		String s = Misc.trim(sign);/* 签名. */
		String a = Misc.trim(action);/* 请求的功能. */
		if (st == null && s == null && a == null)
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

		return def;
	}

	private void noAuth(CallBackStub cbs, HttpTrans trans, String sign, String salt, String action)
	{
		
	}

	/** 鉴权通过后可访问的功能. */
	private void passedAuth(CallBackStub cbs, HttpTrans trans, String sign, String salt, String token, String action) throws Exception
	{
	}

}
