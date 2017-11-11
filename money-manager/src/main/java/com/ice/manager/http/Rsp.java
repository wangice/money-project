package com.ice.manager.http;

public class Rsp
{
	public enum RspErr
	{
		/** 成功. */
		ERR_NONE(0x0000),
		/** 失败. */
		ERR_FAIL(0x0001);

		private int number = 0;

		private RspErr(int number)
		{
			this.number = number;
		}

		public int getNumber()
		{
			return number;
		}
	}

	/** 响应码. */
	public int err;
	/** 错误码的描述. */
	public String desc = null;
	/** 响应正文. */
	public Object dat = null;
}
