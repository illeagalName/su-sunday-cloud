package com.haier.job.controller;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.haier.job.config.props.XxlJobProps;
import com.haier.job.config.props.XxlJobUser;
import com.haier.job.util.XxlJobCookieUtils;
import com.xxl.job.core.enums.ExecutorBlockStrategyEnum;
import com.xxl.job.core.glue.GlueTypeEnum;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Cookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.job.controller
 * @ClassName: JobController
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/22 11:07
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/xxl-job")
public class JobController {

    @Resource
    XxlJobProps props;

    @Resource
    XxlJobUser user;

    private final static String JOB_INFO_URI = "/jobinfo";
    private final static String JOB_GROUP_URI = "/jobgroup";

    /**
     * 任务组列表，xxl-job叫做触发器列表
     */
    @GetMapping("/group")
    public String xxlJobGroup(@RequestParam(required = false, defaultValue = "0") int start,
                              @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("start", start);
        jobInfo.put("length", length);

        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_GROUP_URI + "/pageList", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }

    /**
     * 分页任务列表
     *
     * @param start  当前页，第一页 -> 0
     * @param length 每页条数，默认10
     * @return 分页任务列表
     */
    @GetMapping("/list")
    public String xxlJobList(@RequestParam(required = false, defaultValue = "0") int start,
                             @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("start", start);
        jobInfo.put("length", length);
        jobInfo.put("jobGroup", 2);
        jobInfo.put("triggerStatus", -1);

        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_INFO_URI + "/pageList", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }

    /**
     * 测试手动保存任务
     */
    @GetMapping("/add")
    public String xxlJobAdd() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("jobGroup", 2);
        jobInfo.put("jobCron", "0 0/1 * * * ? *");
        jobInfo.put("jobDesc", "手动添加的任务");
        jobInfo.put("author", "admin");
        jobInfo.put("executorRouteStrategy", "ROUND");
        jobInfo.put("executorHandler", "demoTask");
        jobInfo.put("executorParam", "手动添加的任务的参数");
        jobInfo.put("executorBlockStrategy", ExecutorBlockStrategyEnum.SERIAL_EXECUTION);
        jobInfo.put("glueType", GlueTypeEnum.BEAN);
        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_INFO_URI + "/add", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }

    /**
     * 测试手动触发一次任务
     */
    @GetMapping("/trigger")
    public String xxlJobTrigger() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);
        jobInfo.put("executorParam", JSONUtil.toJsonStr(jobInfo));

        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_INFO_URI + "/trigger", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }

    /**
     * 测试手动删除任务
     */
    @GetMapping("/remove")
    public String xxlJobRemove() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);

        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_INFO_URI + "/remove", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }

    /**
     * 测试手动停止任务
     */
    @GetMapping("/stop")
    public String xxlJobStop() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);

        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_INFO_URI + "/stop", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }

    /**
     * 测试手动启动任务
     */
    @GetMapping("/start")
    public String xxlJobStart() {
        Map<String, Object> jobInfo = Maps.newHashMap();
        jobInfo.put("id", 4);

        List<Cookie> cookie = XxlJobCookieUtils.getCookie(props.getAdmin().getAddress(), props.getLoginIdentityKey(), user);
        String execute = XxlJobCookieUtils.doGet(props.getAdmin().getAddress() + JOB_INFO_URI + "/start", jobInfo, cookie);
        log.info("【execute】= {}", execute);
        return execute;
    }


}
