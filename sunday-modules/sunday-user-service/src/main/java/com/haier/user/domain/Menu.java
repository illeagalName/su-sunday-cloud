package com.haier.user.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @TableName t_menu
 */
@Data
public class Menu implements Serializable {
    /**
     *
     */
    private Long menuId;

    /**
     * 侧边栏和面包屑的展示名称
     */
    private String menuTitle;

    /**
     * 菜单keep-live的name
     */
    private String menuName;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 顺序
     */
    private Integer menuSort;

    /**
     * 路由地址
     */
    private String path;

    /**
     * 组件路径
     */
    private String component;

    /**
     *
     */
    private String redirect;

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
    private Boolean visible;

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

    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Menu other = (Menu) that;
        return (this.getMenuId() == null ? other.getMenuId() == null : this.getMenuId().equals(other.getMenuId()))
                && (this.getMenuTitle() == null ? other.getMenuTitle() == null : this.getMenuTitle().equals(other.getMenuTitle()))
                && (this.getMenuName() == null ? other.getMenuName() == null : this.getMenuName().equals(other.getMenuName()))
                && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
                && (this.getMenuSort() == null ? other.getMenuSort() == null : this.getMenuSort().equals(other.getMenuSort()))
                && (this.getPath() == null ? other.getPath() == null : this.getPath().equals(other.getPath()))
                && (this.getComponent() == null ? other.getComponent() == null : this.getComponent().equals(other.getComponent()))
                && (this.getRedirect() == null ? other.getRedirect() == null : this.getRedirect().equals(other.getRedirect()))
                && (this.getIsFrame() == null ? other.getIsFrame() == null : this.getIsFrame().equals(other.getIsFrame()))
                && (this.getMenuType() == null ? other.getMenuType() == null : this.getMenuType().equals(other.getMenuType()))
                && (this.getVisible() == null ? other.getVisible() == null : this.getVisible().equals(other.getVisible()))
                && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
                && (this.getSymbol() == null ? other.getSymbol() == null : this.getSymbol().equals(other.getSymbol()))
                && (this.getIcon() == null ? other.getIcon() == null : this.getIcon().equals(other.getIcon()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getMenuId() == null) ? 0 : getMenuId().hashCode());
        result = prime * result + ((getMenuTitle() == null) ? 0 : getMenuTitle().hashCode());
        result = prime * result + ((getMenuName() == null) ? 0 : getMenuName().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getMenuSort() == null) ? 0 : getMenuSort().hashCode());
        result = prime * result + ((getPath() == null) ? 0 : getPath().hashCode());
        result = prime * result + ((getComponent() == null) ? 0 : getComponent().hashCode());
        result = prime * result + ((getRedirect() == null) ? 0 : getRedirect().hashCode());
        result = prime * result + ((getIsFrame() == null) ? 0 : getIsFrame().hashCode());
        result = prime * result + ((getMenuType() == null) ? 0 : getMenuType().hashCode());
        result = prime * result + ((getVisible() == null) ? 0 : getVisible().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getSymbol() == null) ? 0 : getSymbol().hashCode());
        result = prime * result + ((getIcon() == null) ? 0 : getIcon().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", menuId=").append(menuId);
        sb.append(", menuTitle=").append(menuTitle);
        sb.append(", menuName=").append(menuName);
        sb.append(", parentId=").append(parentId);
        sb.append(", menuSort=").append(menuSort);
        sb.append(", path=").append(path);
        sb.append(", component=").append(component);
        sb.append(", redirect=").append(redirect);
        sb.append(", isFrame=").append(isFrame);
        sb.append(", menuType=").append(menuType);
        sb.append(", visible=").append(visible);
        sb.append(", status=").append(status);
        sb.append(", symbol=").append(symbol);
        sb.append(", icon=").append(icon);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}