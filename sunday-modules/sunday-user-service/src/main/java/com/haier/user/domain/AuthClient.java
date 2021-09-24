package com.haier.user.domain;

import lombok.Data;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.domain
 * @ClassName: AuthClient
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/24 15:44
 * @Version: 1.0
 */
@Data
public class AuthClient {
    private String clientId;
    private Long accessTokenValidity;
    private String remark;
    private Integer status;
}
