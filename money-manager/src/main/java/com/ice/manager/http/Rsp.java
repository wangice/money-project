package com.ice.manager.http;

public class Rsp
{
	public enum RspErr
	{
		/** 成功. */
		ERR_NONE(0x0000),
		/** 失败. */
		ERR_FAIL(0x0001),
		/** 异常. */
		ERR_SYSTEM_EXCEPTION(0x0003),
		/** 无法验证的签名. */
		ERR_SIGN(0x0004),
		/** 盐值错误, 未按规则使用. */
		ERR_SALT(0x0005),
		/** 格式错误. */
		ERR_FORMAT_ERROR(0x0006),
		/** 缺少必要的参数. */
		ERR_MISSING_PARAMETER(0x0007),
		/** 拒绝. */
		ERR_FORBIDDEN(0x0008),
		/** 不支持的操作. */
		ERR_UNSUPPORTED(0x0009),
		/** 未鉴权. */
		ERR_NO_AUTH(0x000A),
		/** 无权限. */
		ERR_NO_PERMISSION(0x000B),
		/** 无记录. */
		ERR_NO_RECORD(0x000C),
		/** 超出限制. */
		ERR_OVER_LIMIT(0x000D),
		/** 重复的操作. */
		ERR_DUPLICATE_OPER(0x000E),
		/** 找不到要调用的API. */
		ERR_NOT_FOUND_API(0x0100);

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
