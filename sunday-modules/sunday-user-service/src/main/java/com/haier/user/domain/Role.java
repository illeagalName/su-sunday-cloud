package com.haier.user.domain;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:07
 */
@Data
public class Role {
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
     * 显示顺序
     */
    private Integer roleSort;
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
     * 角色状态
     * 0 正常
     * 1 停用
     */
    private Integer status;
    /**
     * 删除状态
     * 0 未删除
     * 1 删除
     */
    private Integer isDelete;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 祖父级关系，FIND_IN_SET
     */
    private String ancestors;
}
