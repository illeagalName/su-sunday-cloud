package com.haier.user.dao;

import com.haier.user.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.dao
 * @ClassName: RoleMapper
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/23 14:34
 * @Version: 1.0
 */
@Mapper
public interface RoleMapper {
    List<Role> listRolesByUserId(@Param("userId") Long userId);

    List<Role> listRoles();
}
