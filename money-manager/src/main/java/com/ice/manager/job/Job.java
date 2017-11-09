package com.ice.manager.job;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ice.manager.cfg.Cfg;

@Service
public class Job
{
	@Resource
	Job4UploadFileMove uploadFileMove;

	/** 开启定时任务. */
	public boolean init()
	{
		if (Cfg.cfg_quartz_job)
		{
			new Thread(() -> uploadFileMove.init()).start();
		}
		return true;
	}
}
