package com.ice.manager.http;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.async.DeferredResult;

import com.ice.manager.cfg.Cfg;
import com.ice.manager.http.Rsp.RspErr;

import misc.Misc;

public class HttpTrans extends MoTrans
{
	public HttpReq req;

	public DeferredResult<String> def;

	/** 返回信息. */
	public Rsp rsp;

	/** 事务产生时间. */
	public long sts;
	/** 事务结束时间. */
	public long ets;

	public HttpTrans(HttpServletRequest request, DeferredResult<String> def)
	{
		this.sts = Cfg.clock;
		this.req = new HttpReq(request);
		this.def = def;
	}

	@Override
	public String getParStr(String par)
	{
		return this.req.getParStr(par);
	}

	@Override
	public int getParInt0(String par)
	{
		return this.req.getParInt0(par);
	}

	public DeferredResult<String> end(Object o)
	{
		return this.end(RspErr.ERR_NONE, o);
	}

	public DeferredResult<String> end(RspErr err)
	{
		return this.end(err, null);
	}

	public DeferredResult<String> end(RspErr err, Object obj)
	{
		Rsp rsp = new Rsp();
		rsp.err = err.getNumber();
		rsp.desc = err.toString();
		rsp.dat = obj;
		this.rsp = rsp;
		return this.flush();
	}

	/** 不记录日志返回值. */
	public DeferredResult<String> endNoLog(RspErr err)
	{

		Rsp rsp = new Rsp();
		rsp.err = err.getNumber();
		rsp.desc = err.toString();
		rsp.dat = null;
		String r = Misc.obj2json(rsp);
		if (this.req.jsonp)
		{
			StringBuilder strb = new StringBuilder(this.req.callback);
			strb.append("(").append(r).append(");");
			this.def.setResult(strb.toString());
		} else
			this.def.setResult(r);
		return this.def;
	}

	/** 立即返回信息. */
	public DeferredResult<String> flush()
	{
		String r = Misc.obj2json(rsp);
		if (this.req.jsonp)
		{
			StringBuilder strb = new StringBuilder(this.req.callback);
			strb.append("(").append(r).append(");");
			this.def.setResult(strb.toString());
		} else
			this.def.setResult(r);

		/** 事务结束时间. */
		this.ets = Cfg.clock;
		// 以后用户计算调用接口的时间
		return this.def;
	}
}
