package com.haier.user.vo.response;

import lombok.Data;

import java.util.List;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.vo.response
 * @ClassName: PersonalInfoVO
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/19 16:02
 * @Version: 1.0
 */
@Data
public class PersonalInfoVO {
    private String userName;
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

    private List<String> roles;
}
