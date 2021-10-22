package com.haier.job.config.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.job.config.props
 * @ClassName: XxlJobUser
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/22 14:12
 * @Version: 1.0
 */
@Data
@ConfigurationProperties(prefix = "xxl.job.user")
public class XxlJobUser {
    private String username;		// 账号
    private String password;		// 密码
    private int role;				// 角色：0-普通用户、1-管理员
}
