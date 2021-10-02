package com.haier.elasticsearch.config;

import com.haier.core.util.AssertUtils;
import com.haier.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author Ami
 * @Date 2021/10/1 17:43
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(EsProperties.class)
public class EsAutoConfiguration {

    @Resource
    EsProperties esProperties;

    private final List<HttpHost> HTTP_HOSTS = new ArrayList<>();


    @Bean
    @ConditionalOnMissingBean
    public RestHighLevelClient restHighLevelClient() {
        log.info("=======================================");
        log.info("初始化elasticsearch配置开始");
        List<String> clusterNodes = esProperties.getClusterNodes();
        clusterNodes.forEach(node -> {
            String[] ipPort = StringUtils.split(node, ":");
            AssertUtils.notEmpty(ipPort, "es ip and port must defined");
            AssertUtils.isTrue(ipPort.length == 2, "es must be defined as 'host:port'");
            HTTP_HOSTS.add(new HttpHost(ipPort[0], Integer.parseInt(ipPort[1]), esProperties.getScheme()));
        });
        RestClientBuilder builder = RestClient.builder(HTTP_HOSTS.toArray(new HttpHost[0]));
        RestHighLevelClient restHighLevelClient = getRestHighLevelClient(builder, esProperties);
        log.info("初始化elasticsearch配置结束");
        return restHighLevelClient;
    }

    private static RestHighLevelClient getRestHighLevelClient(RestClientBuilder builder, EsProperties esProperties) {

        Header[] defaultHeaders = {new BasicHeader("content-type", "application/json")};
        builder
                .setDefaultHeaders(defaultHeaders)
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(esProperties.getConnectTimeout());
                    requestConfigBuilder.setSocketTimeout(esProperties.getSocketTimeout());
                    requestConfigBuilder.setConnectionRequestTimeout(esProperties.getConnectionRequestTimeout());
                    return requestConfigBuilder;
                })
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setMaxConnTotal(esProperties.getMaxConnectTotal());
                    httpClientBuilder.setMaxConnPerRoute(esProperties.getMaxConnectPerRoute());
                    EsProperties.Account account = esProperties.getAccount();
                    if (!StringUtils.isAnyBlank(account.getUsername(), account.getPassword())) {
                        // httpClientBuilder.disableAuthCaching();
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(account.getUsername(), account.getPassword()));
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                    return httpClientBuilder;
                });
        return new RestHighLevelClient(builder);
    }
}
