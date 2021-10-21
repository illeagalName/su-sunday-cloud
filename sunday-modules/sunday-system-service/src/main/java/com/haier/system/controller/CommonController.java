package com.haier.system.controller;

import com.haier.core.domain.R;
import com.haier.system.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.system.controller
 * @ClassName: CommonController
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/21 14:06
 * @Version: 1.0
 */
@RestController
@RequestMapping("common")
public class CommonController {

    @Autowired
    CommonService commonService;

    @GetMapping("electricity")
    public R<?> todayElectricity() {
        return R.success(commonService.todayElectricity());
    }
}
