package com.ice.manager.cfg;

import misc.Misc;

/**
 * 基本配置文件
 * 
 * create on: 2017年11月8日 下午3:58:51
 * 
 * @author: ice
 *
 */
public class Cfg
{
	/** 系统工作目录. */
	public static final String pwd = Misc.getPwd(Cfg.class);

	/** 是否开启定时任务. */
	public static final boolean cfg_quartz_job = false;

	/** 系统时间戳, 每Cfg.libtsc_quartz频率更新, (主要用于避免重复调用System.currentTimeMillis()). */
	public static long clock = System.currentTimeMillis();
}
