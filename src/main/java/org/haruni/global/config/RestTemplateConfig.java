package org.haruni.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Value("${model-server.url}")
    private String modelServerUrl;

    @Bean
    @Qualifier("oauthTemplate")
    public RestTemplate oauthTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }

    @Bean
    @Qualifier("modelServerTemplate")
    public RestTemplate modelServerTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder
                .rootUri(modelServerUrl)
                .build();
    }
}
