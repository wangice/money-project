package com.ice.dao.mapper.ext;

import java.util.List;

import com.ice.dao.bo.TempFileBo;

public interface TempFileBoExtMapper
{
	/**
	 * 根据path更新
	 * 
	 * @param tempFile
	 * @return
	 */
	int updateByPath(TempFileBo tempFile);

	/**
	 * 根据w写入完成查询文件.
	 * 
	 * @param tempFile
	 * @return
	 */
	List<TempFileBo> selectTempFileByW(TempFileBo tempFile);
}
