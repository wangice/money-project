package com.ice.manager.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ice.manager.job.Job;

import misc.Misc;

public class Itcp extends HandlerInterceptorAdapter
{
	Logger logger = LoggerFactory.getLogger("Itcp");

	@Autowired
	public Job job;

	@Autowired
	public CoreManager coreManager;

	/** 容器初始化后的首个调用函数, 应用入口被放置在这里. */
	public void init() throws Exception
	{
		logger.info("系统初始化开始.");
		if (!job.init())
		{
			logger.error("job start failed!");
			Misc.lazySystemExit();
		}
		if (!coreManager.init())
		{
			logger.error("coreManager start failed!");
			Misc.lazySystemExit();
		}
	}
}
