package com.haier.api.user.domain;

import lombok.Data;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.api.user.domain
 * @ClassName: ClientVO
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/24 16:20
 * @Version: 1.0
 */
@Data
public class ClientVO {
    private String clientId;
    private Long time;
    private String remark;
}
