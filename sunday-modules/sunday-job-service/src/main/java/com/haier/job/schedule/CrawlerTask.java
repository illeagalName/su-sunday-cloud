package com.haier.job.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.haier.core.util.DataUtils;
import com.haier.core.util.HttpUtils;
import com.haier.core.util.StringUtils;
import com.haier.job.domain.Hitokoto;
import com.haier.job.mapper.HitokotoMapper;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

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


    @Autowired
    HitokotoMapper hitokotoMapper;

    /**
     * hitokoto(一言)、en(中英文)、social(社会语录)、soup(毒鸡汤)、fart(彩虹屁)、zha(渣男语录)
     */
    final List<String> HITOKOTO_TYPES = Lists.newArrayList("hitokoto", "en", "social", "soup", "fart", "zha");

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
            // {
            //"code": 200,
            //"type": "一言",
            //"content": "有些时候，当你说你赢了的时候，你就已经输了。"
            //}
            JSONObject result = JSON.parseObject(s);
            if (Objects.nonNull(result) && Objects.equals(result.getInteger("code"), 200)) {
                String content = result.getString("content");
                Hitokoto hitokoto = new Hitokoto();
                hitokoto.setType(type);
                hitokoto.setContent(content);
                hitokotos.add(hitokoto);
            }
            XxlJobHelper.log("type : {} 异常", type);
        });
        if (DataUtils.isNotEmpty(hitokotos)) {
            hitokotoMapper.batchInsertHitokotos(hitokotos);
        }
        XxlJobHelper.handleSuccess("执行完毕");
    }

}
