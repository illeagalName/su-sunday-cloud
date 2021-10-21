package com.haier.system.es;

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
 * @ProjectName: su-sunday-cloud
 * @Package: com.haier.system.es
 * @ClassName: LogService
 * @Author: yangwendong
 * @Description:
 * @Date: 2021/10/21 14:00
 * @Version: 1.0
 */
@Service
@Slf4j
public class LogService extends BaseEsService {

    public Map<String, Object> get() {
        List<Map<String, Object>> items = new ArrayList<>();
        String format = DateUtils.toString(LocalDate.now(), "yyyy.MM.dd");
        SearchResponse response = searchAll("sunday-log-" + format, 1, 30);
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
