package com.haier.user.domain;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:21
 */
@Data
public class Dept {
    private Long deptId;
    /**
     * 部门名称
     */
    private Long deptName;
    /**
     * 父id
     */
    private Long parentId;
    /**
     * 祖父级
     */
    private Long ancestors;
    /**
     * 显示顺序
     */
    private Long deptSort;
    /**
     * 部门状态
     * 0 正常
     * 1 停用
     */
    private Long status;
    /**
     * 删除状态
     * 0 未删除
     * 1 删除
     */
    private Long isDelete;
}
