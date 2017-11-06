package com.ice.dao.mapper;

import com.ice.dao.bo.UsrBo;

public interface UsrBoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UsrBo record);

    int insertSelective(UsrBo record);

    UsrBo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UsrBo record);

    int updateByPrimaryKey(UsrBo record);
}