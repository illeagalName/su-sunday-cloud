package com.haier.api.user.domain;

import lombok.Data;

import java.util.List;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.api.user.domain
 * @ClassName: RoleVO
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/23 15:57
 * @Version: 1.0
 */
@Data
public class RoleVO {
    private Long roleId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 角色标识
     */
    private String symbol;
    /**
     * 数据范围
     * 1 全部数据
     * 2 自定义数据
     * 3 本部门数据
     * 4 本部门及以下部门
     * 5 本人数据
     */
    private Integer dataScope;

    /**
     * 父id
     */
    private Long parentId;
    /**
     * 祖父级关系，FIND_IN_SET
     */
    private String ancestors;

    private List<String> menus;
}
