package com.ice.manager.core;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ice.manager.cfg.MsgCfg;

@Service("coreManager")
public class CoreManager
{
	public MoneyActorBlocking mab = null;

	@Resource
	public MsgCfg msgCfg;

	public boolean init()
	{
		/** -------------------------------- */
		/**                                  */
		/** 异步future. */
		/**                                  */
		/** -------------------------------- */
		mab = new MoneyActorBlocking();

		/** -------------------------------- */
		/**                                  */
		/** API消息总线. */
		/**                                  */
		/** -------------------------------- */
		if (!msgCfg.init())
			return false;
		return true;
	}
}
