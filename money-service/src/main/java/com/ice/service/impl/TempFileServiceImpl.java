package com.ice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.dao.bo.TempFileBo;
import com.ice.dao.mapper.TempFileBoMapper;
import com.ice.service.TempFileService;

@Service("tempFileService")
public class TempFileServiceImpl implements TempFileService
{
	@Autowired
	public TempFileBoMapper mapper;

	@Override
	public int save(TempFileBo fileBo)
	{
		return mapper.insertSelective(fileBo);
	}

	@Override
	public int updateTempFileBo(TempFileBo file)
	{
		return mapper.updateByPrimaryKeySelective(file);
	}

	@Override
	public int updateTempFile(boolean w)
	{
		TempFileBo file = new TempFileBo();
		file.setW(w);
		return updateTempFileBo(file);
	}

}
