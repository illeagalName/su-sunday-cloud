package com.haier.user.dao;

import com.haier.user.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:56
 */
@Mapper
public interface UserMapper {
    User selectUserByUserName(@Param("userName") String userName);
}
