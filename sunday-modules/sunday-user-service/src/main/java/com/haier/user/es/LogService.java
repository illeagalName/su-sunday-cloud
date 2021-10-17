package com.haier.user.es;

import com.haier.core.util.DateUtils;
import com.haier.elasticsearch.service.BaseEsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/2 18:04
 */
@Service
@Slf4j
public class LogService extends BaseEsService {

    public Map<String, Object> get() {
        List<Map<String, Object>> items = new ArrayList<>();
        String format = DateUtils.toString(LocalDate.now(), "yyyy.MM.dd");
//        SearchResponse response = searchAll("sunday-log-" + format, 1, 30);
        SearchResponse response = null;
        SearchHit[] hits = Optional.ofNullable(response).map(SearchResponse::getHits).map(SearchHits::getHits).orElse(new SearchHit[0]);
        for (SearchHit hit : hits) {
            Map<String, Object> map = hit.getSourceAsMap();
            Map<String, Object> r = new HashMap<>();
            map.forEach((key, value) -> {
                r.put(key.replaceFirst("@", ""), value);
            });
            items.add(r);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", items.size());
        return result;
    }
}
