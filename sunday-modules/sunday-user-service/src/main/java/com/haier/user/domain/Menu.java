package com.haier.user.domain;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:15
 */
@Data
public class Menu {
    private Long menu_id;
    /**
     * 菜单名称
     */
    private String menu_name;
    /**
     * 父id
     */
    private Long parent_id;
    /**
     * 顺序
     */
    private Integer menu_sort;
    /**
     * 路由地址
     */
    private String path;
    /**
     * 组件路径
     */
    private String component;
    /**
     * 是否外链
     * 0 否
     * 1 是
     */
    private Integer is_frame;
    /**
     * 菜单类型
     * M 菜单menu
     * B 按钮button
     * C 目录catalog
     */
    private String menu_type;
    /**
     * 显示状态
     * 1 显示
     * 0 隐藏
     */
    private Integer visible;
    /**
     * 菜单状态
     * 0 正常
     * 1 停用
     */
    private Integer status;
    /**
     * 权限标识
     */
    private String symbol;
    /**
     * 菜单图标
     */
    private String icon;
}
