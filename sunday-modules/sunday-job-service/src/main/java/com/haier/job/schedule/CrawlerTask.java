package com.haier.job.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.haier.api.bot.RemoteBotService;
import com.haier.core.util.DataUtils;
import com.haier.core.util.HttpUtils;
import com.haier.core.util.StringUtils;
import com.haier.job.domain.Hitokoto;
import com.haier.job.mapper.HitokotoMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/28 17:16
 */
@Component
@Slf4j
@RefreshScope
public class CrawlerTask {

    @Value("${hitokoto.url}")
    String hitokotoUrl;

    @Resource
    HitokotoMapper hitokotoMapper;

    @Resource
    RemoteBotService remoteBotService;


    /**
     * hitokoto(一言)、en(中英文)、social(社会语录)、soup(毒鸡汤)、fart(彩虹屁)、zha(渣男语录)
     */
    final List<Pair<String, String>> HITOKOTO_TYPES = Lists.newArrayList(Pair.of("一言", "hitokoto"), Pair.of("中英文", "en"), Pair.of("社会语录", "social"), Pair.of("毒鸡汤", "soup"), Pair.of("彩虹屁", "fart"), Pair.of("渣男语录", "zha"));


    @XxlJob("getHitokotoInfo")
    public void getHitokotoInfo() {
        log.info("执行器进入逻辑处理getHitokotoInfo");
        XxlJobHelper.log("执行器进入逻辑处理getHitokotoInfo");
        // XxlJobHelper.handleFail(""),XxlJobHelper.handleSuccess("")
        //获取页面传递的参数
        String param = XxlJobHelper.getJobParam();
        List<Hitokoto> hitokotos = new ArrayList<>();
        HITOKOTO_TYPES.forEach(type -> {
            String s = HttpUtils.doGet(StringUtils.stringFormat(hitokotoUrl, type));
            JSONObject result = JSON.parseObject(s);
            if (Objects.nonNull(result) && Objects.equals(result.getInteger("code"), 200)) {
                String content = result.getString("content");
                Hitokoto hitokoto = new Hitokoto();
                hitokoto.setType(type.getRight());
                hitokoto.setContent(content);
                hitokotos.add(hitokoto);
                if (type.getLeft().equals("毒鸡汤")) {
                    remoteBotService.sendMessage(type.getLeft().concat(" - ").concat(content));
                }
            }
            XxlJobHelper.log("type : {} 异常", type);
        });
        if (DataUtils.isNotEmpty(hitokotos)) {
            hitokotoMapper.batchInsertHitokotos(hitokotos);
        }
        XxlJobHelper.handleSuccess("执行完毕");
    }

    @XxlJob("privateDomain")
    public void privateDomain() {
        log.info("执行器进入逻辑处理privateDomain");
        remoteBotService.sendMessage2("加油");
        XxlJobHelper.handleSuccess("执行完毕");
    }

}
