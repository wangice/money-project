package com.ice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.dao.bo.UsrBo;
import com.ice.dao.mapper.UsrBoMapper;
import com.ice.service.UsrService;

@Service("usrService")
public class UsrServiceImpl implements UsrService
{

	@Autowired
	public UsrBoMapper usrBoMapper;

	@Override
	public UsrBo queryUsr(Integer id)
	{
		UsrBo usr = usrBoMapper.selectByPrimaryKey(id);
		return usr;
	}

}
