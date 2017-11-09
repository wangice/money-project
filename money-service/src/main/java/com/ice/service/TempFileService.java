package com.ice.service;

import java.util.List;

import com.ice.dao.bo.TempFileBo;

public interface TempFileService
{
	/** 保存文件. */
	int save(TempFileBo file);

	/** 更新文件. */
	int updateTempFileBo(TempFileBo file);

	/** 修改文件是否写入完成. */
	public int updateTempFileByPath(String path, boolean w);

	/** 查询所有写入成功集合. */
	public List<TempFileBo> queryTempFilesByW(boolean w);

	public int delteTempFile(TempFileBo file);
}
