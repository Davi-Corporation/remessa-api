package com.api.remessa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bcb.ptax")
public record BcbProperties(String baseUrl) {

}
