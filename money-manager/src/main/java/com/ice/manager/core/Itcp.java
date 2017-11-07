package com.ice.manager.core;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Itcp extends HandlerInterceptorAdapter
{
	/** 容器初始化后的首个调用函数, 应用入口被放置在这里. */
	public void init() throws Exception
	{
		System.out.println("初始化");
	}
}
