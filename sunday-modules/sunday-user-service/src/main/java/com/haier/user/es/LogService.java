package com.haier.user.es;

import com.haier.core.util.DateUtils;
import com.haier.elasticsearch.service.BaseEsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/2 18:04
 */
@Service
@Slf4j
public class LogService extends BaseEsService {

    public List<Map<String, Object>> get() {
        List<Map<String, Object>> result = new ArrayList<>();
        String format = DateUtils.toString(LocalDate.now(), "yyyy.MM.dd");
        SearchResponse response = searchAll("sunday-log-" + format, 1, 10);
        SearchHit[] hits = response.getHits().getHits();
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            result.add(map);
        }
        return result;
    }
}
