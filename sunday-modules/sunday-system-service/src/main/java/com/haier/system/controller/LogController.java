package com.haier.system.controller;

import com.haier.core.domain.R;
import com.haier.system.es.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.system.controller
 * @ClassName: LogController
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/21 13:59
 * @Version: 1.0
 */
@RestController
@RequestMapping("log")
public class LogController {

    @Autowired
    LogService logService;

    @GetMapping("all")
    public R<?> log() {
        Map<String, Object> maps = logService.get();
        return R.success(maps);
    }
}
