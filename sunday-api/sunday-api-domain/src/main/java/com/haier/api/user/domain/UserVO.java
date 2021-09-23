package com.haier.api.user.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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


    // 额外信息

    /**
     * 用户唯一标识
     */
    private String token;

    private String clientId;

    private String secret;

    /**
     * 登录时间
     */
    private LocalDateTime loginTime;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 用户身上的角色id集合
     */
    private List<Long> roleIds;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 账号状态
     * 0 正常
     * 1 停用
     */
    private Integer status;
}
