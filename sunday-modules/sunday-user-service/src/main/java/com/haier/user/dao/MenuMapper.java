package com.haier.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.haier.user.domain.Menu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.dao
 * @ClassName: MenuMapper
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/23 15:31
 * @Version: 1.0
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {
    List<Menu> listMenusByRoleId(@Param("roleId") Long roleId);
}
