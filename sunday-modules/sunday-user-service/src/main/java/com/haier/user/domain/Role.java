package com.haier.user.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 角色表
 * @TableName t_role
 */
@Data
public class Role implements Serializable {
    /**
     * 
     */
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
1 全部数据
2 自定义数据
3 本部门数据
4 本部门及以下部门
5 本人数据
     */
    private Integer dataScope;

    /**
     * 角色状态
0 正常
1 停用
     */
    private Integer status;

    /**
     * 删除状态
0 未删除
1 删除
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
        Role other = (Role) that;
        return (this.getRoleId() == null ? other.getRoleId() == null : this.getRoleId().equals(other.getRoleId()))
            && (this.getRoleName() == null ? other.getRoleName() == null : this.getRoleName().equals(other.getRoleName()))
            && (this.getSymbol() == null ? other.getSymbol() == null : this.getSymbol().equals(other.getSymbol()))
            && (this.getRoleSort() == null ? other.getRoleSort() == null : this.getRoleSort().equals(other.getRoleSort()))
            && (this.getDataScope() == null ? other.getDataScope() == null : this.getDataScope().equals(other.getDataScope()))
            && (this.getStatus() == null ? other.getStatus() == null : this.getStatus().equals(other.getStatus()))
            && (this.getIsDelete() == null ? other.getIsDelete() == null : this.getIsDelete().equals(other.getIsDelete()))
            && (this.getParentId() == null ? other.getParentId() == null : this.getParentId().equals(other.getParentId()))
            && (this.getAncestors() == null ? other.getAncestors() == null : this.getAncestors().equals(other.getAncestors()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRoleId() == null) ? 0 : getRoleId().hashCode());
        result = prime * result + ((getRoleName() == null) ? 0 : getRoleName().hashCode());
        result = prime * result + ((getSymbol() == null) ? 0 : getSymbol().hashCode());
        result = prime * result + ((getRoleSort() == null) ? 0 : getRoleSort().hashCode());
        result = prime * result + ((getDataScope() == null) ? 0 : getDataScope().hashCode());
        result = prime * result + ((getStatus() == null) ? 0 : getStatus().hashCode());
        result = prime * result + ((getIsDelete() == null) ? 0 : getIsDelete().hashCode());
        result = prime * result + ((getParentId() == null) ? 0 : getParentId().hashCode());
        result = prime * result + ((getAncestors() == null) ? 0 : getAncestors().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", roleId=").append(roleId);
        sb.append(", roleName=").append(roleName);
        sb.append(", symbol=").append(symbol);
        sb.append(", roleSort=").append(roleSort);
        sb.append(", dataScope=").append(dataScope);
        sb.append(", status=").append(status);
        sb.append(", isDelete=").append(isDelete);
        sb.append(", parentId=").append(parentId);
        sb.append(", ancestors=").append(ancestors);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}