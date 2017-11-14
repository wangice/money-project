package com.ice.manager.actor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import misc.Misc;

public class ActorBlocking extends Actor
{
	Logger log = LoggerFactory.getLogger("ActorBlocking");

	/** 等待处理的Consumer */
	private ConcurrentLinkedQueue<Consumer<Void>> cs = new ConcurrentLinkedQueue<>();
	/** cs的个数. */
	private final AtomicInteger size = new AtomicInteger();
	/** 线程个数. */
	private int ts = 1;
	/** 线程忙? */
	private volatile boolean busy = false;

	public ActorBlocking(int ts)
	{
		super(ActorType.BLOCKING);
		this.ts = ts < 1 ? 1 : ts;
		this.start();
	}

	public void push(Consumer<Void> c)
	{
		this.cs.add(c);
		this.size.incrementAndGet();
		synchronized (this)
		{
			this.notify();/* 唤醒其他线程. */
		}
	}

	private void start()
	{
		ActorBlocking ab = this;
		ExecutorService service = Executors.newFixedThreadPool(ts);
		for (int i = 0; i < ts; i++)
		{
			service.execute(() ->
			{
				log.info("Actor-Blocking worker thread started successfully, name: {}, tid: {}", ab.name, Thread.currentThread().getId());
				while (true)
					ab.run();
			});
		}
	}

	/** 线程忙? */
	public boolean isBusy()
	{
		return this.busy;
	}

	/** 队列尺寸. */
	public int size()
	{
		return this.size.get();
	}

	private void run()
	{
		Consumer<Void> c = this.cs.poll();
		if (c == null)
		{
			synchronized (this)
			{
				try
				{
					this.wait();
				} catch (InterruptedException e)
				{
				}
			}
			c = this.cs.poll();
		}
		if (c != null)/* 抢占式. */
		{
			this.size.decrementAndGet();
			this.busy = true;
			Misc.exeConsumer(c, null);
			this.busy = false;
		}
	}

}
