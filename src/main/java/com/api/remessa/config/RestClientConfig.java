package com.api.remessa.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final BcbProperties properties;

    @Bean
    RestClient restClient() {

        return RestClient.builder()
                .baseUrl(properties.baseUrl())
                .build();
    }
}
