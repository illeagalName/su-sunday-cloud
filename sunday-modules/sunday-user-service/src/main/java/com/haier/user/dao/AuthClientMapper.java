package com.haier.user.dao;

import com.haier.user.domain.AuthClient;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.user.dao
 * @ClassName: AuthClientMapper
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/9/24 15:45
 * @Version: 1.0
 */
@Mapper
public interface AuthClientMapper {
    List<AuthClient> listAuthClients();
}
