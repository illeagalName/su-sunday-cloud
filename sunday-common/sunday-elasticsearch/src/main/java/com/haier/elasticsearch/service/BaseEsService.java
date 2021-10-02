package com.haier.elasticsearch.service;

import cn.hutool.core.bean.BeanUtil;
import com.haier.core.util.JsonUtils;
import com.haier.elasticsearch.config.EsProperties;
import com.haier.elasticsearch.exception.EsException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.HttpAsyncResponseConsumerFactory;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/1 18:36
 */
@Slf4j
public abstract class BaseEsService {
    @Resource
    RestHighLevelClient highLevelClient;

    @Resource
    EsProperties esProperties;


    static final RequestOptions OPTIONS;

    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();
        // 设置缓冲为30M
        builder.setHttpAsyncResponseConsumerFactory(new HttpAsyncResponseConsumerFactory.HeapBufferedResponseConsumerFactory(30 * 1024 * 1024));
        OPTIONS = builder.build();
    }

    protected void createIndexRequest(String index, XContentBuilder builder) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(index);
            request.settings(Settings.builder().put("index.number_of_shards", esProperties.getIndex().getNumberOfShards()).put("index.number_of_replicas", esProperties.getIndex().getNumberOfReplicas()).build());

            if (Objects.nonNull(builder)) {
                request.mapping(builder);
            }

            CreateIndexResponse response = highLevelClient.indices().create(request, OPTIONS);
            log.info(" whether all of the nodes have acknowledged the request : {}", response.isAcknowledged());
            log.info(" Indicates whether the requisite number of shard copies were started for each shard in the index before timing out :{}", response.isShardsAcknowledged());
        } catch (IOException e) {
            throw new EsException("创建索引 [" + index + "] 失败", e);
        }
    }

    protected void deleteIndexRequest(String index) {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        try {
            highLevelClient.indices().delete(request, OPTIONS);
        } catch (IOException e) {
            throw new EsException("删除索引 [" + index + "] 失败", e);
        }
    }

    protected boolean isExistIndex(String index) {
        try {
            GetIndexRequest request = new GetIndexRequest(index);
            return highLevelClient.indices().exists(request, OPTIONS);
        } catch (IOException e) {
            throw new EsException("查询索引 [" + index + "] 是否存在失败", e);
        }
    }

    /**
     * @param index  Index
     * @param id     Document id
     * @param object
     */
    protected void updateRequest(String index, String id, Object object) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, id).doc(BeanUtil.beanToMap(object), XContentType.JSON);
            highLevelClient.update(updateRequest, OPTIONS);
        } catch (IOException e) {
            throw new EsException("更新索引 [" + index + "] 数据 [" + JsonUtils.toString(object) + "] 失败", e);
        }
    }

    protected void deleteRequest(String index, String id) {
        try {
            DeleteRequest deleteRequest = new DeleteRequest(index, id);
            highLevelClient.delete(deleteRequest, OPTIONS);
        } catch (IOException e) {
            throw new EsException("删除索引 [" + index + "] 数据id [" + id + "] 失败", e);
        }
    }

    /**
     * @param index  Index
     * @param id     Document id for the request
     * @param object Document source provided as a String
     * @return
     */
    protected static IndexRequest buildRequest(String index, String id, Object object) {
        return new IndexRequest(index).id(id).source(BeanUtil.beanToMap(object), XContentType.JSON);
    }

    /**
     * 获取某条记录
     *
     * @param index
     * @param id
     * @param includes
     * @param excludes
     * @param clazz
     * @param <T>
     * @return
     */
    protected <T> T get(String index, String id, String[] includes, String[] excludes, Class<T> clazz) {
        Map<String, Object> stringObjectMap = get(index, id, includes, excludes);
        return BeanUtil.toBean(stringObjectMap, clazz);
    }

    protected Map<String, Object> get(String index, String id, String[] includes, String[] excludes) {
        try {
            GetRequest request = new GetRequest(index, id);
            FetchSourceContext fetchSourceContext;
            if (ArrayUtils.isNotEmpty(includes) || ArrayUtils.isNotEmpty(excludes)) {
                fetchSourceContext = new FetchSourceContext(true, includes, excludes);
            } else {
                fetchSourceContext = FetchSourceContext.DO_NOT_FETCH_SOURCE;
            }
            request.fetchSourceContext(fetchSourceContext);
            GetResponse response = highLevelClient.get(request, OPTIONS);
            if (response.isExists()) {
                return response.getSourceAsMap();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 简单的分页全部查询
     * 具体查询：QueryBuilders.termQuery，MatchQueryBuilder，HighlightBuilder，TermsAggregationBuilder，SuggestionBuilder
     *
     * @param index
     * @param from
     * @param size
     * @return
     */
    protected SearchResponse searchAll(String index, int from, int size) {
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.from(from);
        searchSourceBuilder.size(size);
        SearchResponse searchResponse = null;
        try {
            searchResponse = highLevelClient.search(searchRequest, OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }
}
