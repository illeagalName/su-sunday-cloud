package com.haier.elasticsearch.config;

import com.haier.core.util.AssertUtils;
import com.haier.core.util.JsonUtils;
import com.haier.core.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.*;
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
        log.info("初始化elasticsearch配置开始 {}", JsonUtils.toString(esProperties));
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

                .setFailureListener(new RestClient.FailureListener() {
                    @Override
                    public void onFailure(Node node) {
                        super.onFailure(node);
                        log.info("{} ==节点失败了", node.getName());
                    }
                })
                /*配置节点选择器，客户端以循环方式将每个请求发送到每一个配置的节点上，发送请求的节点，用于过滤客户端，将请求发送到这些客户端节点，默认向每个配置节点发送，这个配置通常是用户在启用嗅探时向专用主节点发送请求（即只有专用的主节点应该被HTTP请求命中）*/
                .setNodeSelector(NodeSelector.SKIP_DEDICATED_MASTERS)
                .setDefaultHeaders(defaultHeaders)
                /*配置连接超时和套接字超时 配置请求超时，将连接超时（默认为1秒）和套接字超时（默认为30秒）增加，这里配置完应该相应地调整最大重试超时（默认为30秒*/
                .setRequestConfigCallback(requestConfigBuilder -> {
                    requestConfigBuilder.setConnectTimeout(esProperties.getConnectTimeout());
                    requestConfigBuilder.setSocketTimeout(esProperties.getSocketTimeout());
                    requestConfigBuilder.setConnectionRequestTimeout(esProperties.getConnectionRequestTimeout());
                    return requestConfigBuilder;
                })
                /*配置异步请求的线程数量，Apache Http Async Client默认启动一个调度程序线程，以及由连接管理器使用的许多工作线程（与本地检测到的处理器数量一样多，取决于Runtime.getRuntime().availableProcessors()返回的数量）。线程数可以修改如下,这里是修改为1个线程，即默认情况*/
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.setMaxConnTotal(esProperties.getMaxConnectTotal());
                    httpClientBuilder.setMaxConnPerRoute(esProperties.getMaxConnectPerRoute());
                    httpClientBuilder.setDefaultIOReactorConfig(IOReactorConfig.custom().setIoThreadCount(1).build());
                    EsProperties.Account account = esProperties.getAccount();
                    if (!StringUtils.isAnyBlank(account.getUsername(), account.getPassword())) {
                        httpClientBuilder.disableAuthCaching();
                        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(account.getUsername(), account.getPassword()));
                        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                    }
                    return httpClientBuilder;
                });
        return new RestHighLevelClient(builder);
    }
}
