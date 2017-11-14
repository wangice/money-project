package com.ice.manager.http;

public abstract class MoTrans
{
	/** 鉴权成功后的token值. */
	public String token;
	/** 用户存根. */
	public UsrStub stub = null;
	/** 响应. */
	public Rsp ret = null;

	/** 设置用户存根. */
	public void setStub(UsrStub stub)
	{
		this.stub = stub;
	}

	/** 获取字符串参数. */
	public abstract String getParStr(String par);

	/** 获取整形参数. */
	public abstract int getParInt0(String par);

}
