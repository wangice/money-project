package com.ice.manager.core;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
public class HttpMsgDispatcher
{
	/** 服务器入口. */
	@ResponseBody
	@RequestMapping()
	public DeferredResult<String> dispatcher() throws Exception
	{
		DeferredResult<String> def = new DeferredResult<>();

		return def;
	}

}
