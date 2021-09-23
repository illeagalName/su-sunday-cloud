package com.haier.api.user.domain;

import lombok.Data;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.api.user.domain
 * @ClassName: MenuVO
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/23 16:01
 * @Version: 1.0
 */
@Data
public class MenuVO {
    private Long menuId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * 父id
     */
    private Long parentId;
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
    private Integer isFrame;
    /**
     * 菜单类型
     * M 菜单menu
     * B 按钮button
     * C 目录catalog
     */
    private String menuType;
    /**
     * 显示状态
     * 1 显示
     * 0 隐藏
     */
    private Integer visible;
    /**
     * 权限标识
     */
    private String symbol;

}
