package com.proj.expensetracker.mapper;

import com.proj.expensetracker.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper {

    boolean getUserInfo(@Param("userInfo") UserInfo userInfo);

}
