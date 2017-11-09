package com.ice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ice.dao.bo.TempFileBo;
import com.ice.dao.mapper.TempFileBoMapper;
import com.ice.dao.mapper.ext.TempFileBoExtMapper;
import com.ice.service.TempFileService;

@Service("tempFileService")
public class TempFileServiceImpl implements TempFileService
{
	@Autowired
	public TempFileBoMapper tempMapper;

	@Autowired
	TempFileBoExtMapper tempExtMapper;

	@Override
	public int save(TempFileBo fileBo)
	{
		return tempMapper.insertSelective(fileBo);
	}

	@Override
	public int updateTempFileBo(TempFileBo file)
	{
		return tempMapper.updateByPrimaryKeySelective(file);
	}

	@Override
	public int updateTempFileByPath(String path, boolean w)
	{
		TempFileBo file = new TempFileBo();
		file.setPath(path);
		file.setW(w);
		return tempExtMapper.updateByPath(file);
	}

	@Override
	public List<TempFileBo> queryTempFilesByW(boolean w)
	{
		TempFileBo file = new TempFileBo();
		file.setW(w);
		List<TempFileBo> temps = tempExtMapper.selectTempFileByW(file);
		return temps.isEmpty() ? null : temps;
	}

	@Override
	public int delteTempFile(TempFileBo file)
	{
		return tempMapper.deleteByPrimaryKey(file.getId());
	}

}
