package com.haier.core.constant;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/9/20 21:55
 */
public class CacheConstants {
    /**
     * 令牌自定义标识
     */
    public static final String HEADER = "Authorization";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /************user_id,username,client_id 是header里必须带的**************/
    /**
     * 用户ID字段
     */
    public static final String DETAILS_USER_ID = "user_id";

    /**
     * 用户名字段
     */
    public static final String DETAILS_USERNAME = "username";

    /**
     * auth分组标识
     */
    public static final String DETAILS_CLIENT_ID = "client_id";

    /**
     * 授权信息字段
     */
    public static final String AUTHORIZATION_HEADER = "authorization";


    /**
     * 用户角色
     */
    public static final String AUTHORIZATION_USER_ROLE = "sunday:user:role:";
    /**
     * 用户菜单
     */
    public static final String AUTHORIZATION_USER_MENU = "sunday:user:menu:";
    /**
     * 用户token
     */
    public static final String AUTHORIZATION_USER_TOKEN = "sunday:user:token:";
}
