package com.ice.manager.http;

public abstract class MoTrans
{
	/** 鉴权成功后的token值. */
	public String token;
	/** 用户存根. */
	public UsrStub stub = null;

	/** 响应. */
	public Rsp ret = null;
}
