package com.haier.user.vo.request;

import lombok.Data;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.vo.request
 * @ClassName: RegisterUserVO
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/12 13:36
 * @Version: 1.0
 */
@Data
public class RegisterUserVO {
    private String nickName;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private Integer sex;
}
