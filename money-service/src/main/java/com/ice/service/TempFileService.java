package com.ice.service;

import com.ice.dao.bo.TempFileBo;

public interface TempFileService
{
	/** 保存文件. */
	int save(TempFileBo file);

	/** 更新文件. */
	int updateTempFileBo(TempFileBo file);

	/** 修改文件是否写入完成. */
	public int updateTempFile(boolean w);
}
