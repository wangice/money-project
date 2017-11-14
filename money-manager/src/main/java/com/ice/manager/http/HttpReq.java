package com.ice.manager.http;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import misc.Misc;

public class HttpReq
{
	public HttpServletRequest request;

	public HashMap<String, String> pars = new HashMap<>();

	public boolean jsonp = false;

	public String callback;

	public HttpReq(HttpServletRequest request)
	{
		this.request = request;
		this.parsePars();
		jsonp = !Misc.isNull(this.getParStr("callback"));
		callback = Misc.trim(this.getParStr("callback"));
	}

	public void parsePars()
	{
		try
		{
			String[] pars = this.request.getQueryString().split("&");
			if (pars == null || pars.length == 1)
				return;
			for (int i = 0; i < pars.length; i++)
			{
				String[] kv = pars[i].split("=");
				if (kv == null || kv.length < 1)
					return;
				this.pars.put(kv[0], kv.length == 1 ? "" : kv[1]);
			}
		} catch (Exception e)
		{
			return;
		}
	}

	public HttpServletRequest getRequest()
	{
		return this.request;
	}

	/** 获得HTTP-URI中的字符串参数. */
	public final String getParStr(String par)
	{
		return Misc.trim(this.pars.get(par));
	}

	/** 获得HTTP-URI中的整形参数(异常时返回零, 否则值总是 >=0). */
	public final int getParInt0(String par)
	{
		return Misc.forceInt0(this.pars.get(par));
	}

	/** 获得HTTP-URI中的整形参数(异常时返回零, 否则值总是 >=1). */
	public final int getParInt1(String par)
	{
		return Misc.forceInt1(this.pars.get(par));
	}

	/** 获得HTTP-URI中的整形参数(异常时返回零, 否则值总是 >=0). */
	public final long getParLong0(String par)
	{
		return Misc.forceLongO(this.pars.get(par));
	}

	/** 获取&action及其身后的字段. */
	public String getAction()
	{
		String query = this.request.getQueryString();
		String action = Misc.trim(query.substring(query.indexOf("&action"), query.length()));
		return (!this.jsonp) ? action : action.substring(0, action.lastIndexOf("&callback="));
	}

}
