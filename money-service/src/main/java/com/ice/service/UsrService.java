package com.ice.service;

import com.ice.dao.bo.UsrBo;

public interface UsrService
{
	/** 查询用户信息. */
	UsrBo queryUsr(Integer id);
	
}
