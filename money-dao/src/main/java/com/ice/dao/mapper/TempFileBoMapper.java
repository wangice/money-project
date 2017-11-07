package com.ice.dao.mapper;

import com.ice.dao.bo.TempFileBo;

public interface TempFileBoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TempFileBo record);

    int insertSelective(TempFileBo record);

    TempFileBo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TempFileBo record);

    int updateByPrimaryKeyWithBLOBs(TempFileBo record);

    int updateByPrimaryKey(TempFileBo record);
}