package com.haier.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haier.core.config.JavaTimeConfig;
import org.redisson.codec.JsonJacksonCodec;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.redis.config
 * @ClassName: ExtendedJsonJacksonMapCodec
 * @Author: Ami
 * @Description: org.redisson.codec.JsonJacksonCodec 的扩展类，支持LocalDateTime等格式，不然保存进去的很难看
 * @Date: 2021/9/24 13:25
 * @Version: 1.0
 */
public class ExtendedJsonJacksonMapCodec extends JsonJacksonCodec {
    @Override
    protected void init(ObjectMapper objectMapper) {
        objectMapper.registerModule(new JavaTimeConfig());
        super.init(objectMapper);
    }

}
