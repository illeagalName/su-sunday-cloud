package com.haier.system.service;

import com.alibaba.fastjson.JSONObject;
import com.haier.core.util.HttpUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.system.service
 * @ClassName: CommonService
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/21 14:06
 * @Version: 1.0
 */
@Service
public class CommonService {

    @Value("${remote.service}")
    String remoteServiceUrl;

    public Object todayElectricity() {
        String url = remoteServiceUrl + "/system/use/groupByPeriod";
        Map<String, Object> params = new HashMap<>();
        params.put("buildingId", 1);
        params.put("period", 1);
        params.put("energyType", 1);
        String s = HttpUtils.doGet(url, params);
        JSONObject jsonObject = JSONObject.parseObject(s);
        return jsonObject.getJSONArray("data");
    }

    @Data
    public static class UsedVO {
        private String subscript;
        private BigDecimal used;
    }
}
