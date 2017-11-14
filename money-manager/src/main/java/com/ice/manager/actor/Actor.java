package com.ice.manager.actor;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import misc.Misc;

public class Actor
{
	Logger log = LoggerFactory.getLogger("Actor");

	public enum ActorType
	{
		/** 进程内Actor, 附着在某个Tworker上. */
		ITC,
		/** 拥有自己独立线程或线程池的actor, 主要用于IO阻塞操作, 如数据库查询. */
		BLOCKING,
		/** 即network to host, 网络到主机的连接. */
		N2H,
		/** 即host to network, 主机到网络的连接. */
		H2N
	}

	/** 工作线程索引, -1时无效. */
	public int wk = -1;
	/** Actor类型. */
	public ActorType type;
	/** Actor名称. */
	public String name;

	public Actor(ActorType type)
	{
		this.type = type;
		this.name = this.getClass().getSimpleName();
	}

	public void future(Consumer<Void> c)
	{
		if (this.type.ordinal() == ActorType.BLOCKING.ordinal())/* 直接入队. */
		{
			((ActorBlocking) this).push(c);
			return;
		}
		if (wk == -1)
		{
			log.error("it's a buf t: %s, %s", this.name, Misc.getStackInfo());
			return;
		}
		Misc.exeConsumer(c, null);/* 立即消费. */
	}
}
