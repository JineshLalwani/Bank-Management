package banking.management.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticSearchConfig {

    @Bean
    public ElasticsearchClient client() {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200, "http")
        ).build();

        return new ElasticsearchClient(new RestClientTransport(
                restClient, new JacksonJsonpMapper()));
    }
}


//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import co.elastic.clients.util.ContentType;
//import org.apache.http.HttpHeaders;
//import org.apache.http.HttpHost;
//import org.apache.http.HttpResponseInterceptor;
//import org.apache.http.message.BasicHeader;
//import org.elasticsearch.client.RestClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.List;
//
//@Configuration
//public class ElasticSearchConfig {
//    @Bean
//    public RestClient getRestClient() {
//        return RestClient.builder(new HttpHost("localhost", 9200, "https"))
//                .setHttpClientConfigCallback(httpClientBuilder -> {
//                    httpClientBuilder.disableAuthCaching();
//                    httpClientBuilder.setDefaultHeaders(List.of(
//                            new BasicHeader(
//                                    HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)));
//                    httpClientBuilder.addInterceptorLast((HttpResponseInterceptor) (response, context) ->
//                            response.addHeader("X-Elastic-Product", "Elasticsearch"));
//
//                    // Add AWS signing interceptor (custom implementation required)
//                    return httpClientBuilder;
//                }).build();
//    }
//
//
//    @Bean
//    public ElasticsearchTransport getElasticsearchTransport() {
//        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
//    }
//
//    @Bean
//    public ElasticsearchClient getElasticsearchClient() {
//        return new ElasticsearchClient(getElasticsearchTransport());
//    }
//
//}