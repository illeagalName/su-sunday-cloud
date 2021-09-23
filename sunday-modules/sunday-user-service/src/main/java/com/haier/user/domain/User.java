package com.haier.user.domain;

import lombok.Data;

import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 19:53
 */
@Data
public class User {
    private Long userId;
    /**
     * 登录名称
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名称
     */
    private String nickName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 性别
     * 0 女
     * 1 男
     * 2 保密
     */
    private Integer sex;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 账号状态，
     * 0 正常
     * 1 停用
     */
    private Integer status;
    /**
     * 删除状态
     * 0 未删除
     * 1  删除
     */
    private Integer isDelete;
}
