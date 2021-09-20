package com.sunday.user.api.domain;

import lombok.Data;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 20:38
 */
@Data
public class UserVO {
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
     * 加盐
     */
    private String salt;


    // 额外信息

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;
}
