package com.haier.api.user.domain;

import lombok.Data;

/**
 * @Description: header头中隐藏的信息
 * @Author Ami
 * @Date 2021/9/24 22:44
 */
@Data
public class TokenVO {
    private String clientId;
    private Long uniqueId;
    private Long exp;
}
