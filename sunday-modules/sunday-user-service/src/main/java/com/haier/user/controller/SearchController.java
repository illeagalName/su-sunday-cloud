package com.haier.user.controller;

import com.haier.core.domain.R;
import com.haier.user.es.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/5 14:51
 */
@RestController
@RequestMapping("log")
public class SearchController {

    @Autowired
    LogService logService;

    @GetMapping("all")
    public R<?> log() {
        List<Map<String, Object>> maps = logService.get();
        return R.success(maps);
    }
}
