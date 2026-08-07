package com.api.remessa.config;

import com.api.remessa.model.ExchangeRate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import tools.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, ExchangeRate> redisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper) {

        RedisTemplate<String, ExchangeRate> template = new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        JacksonJsonRedisSerializer<ExchangeRate> serializer = new JacksonJsonRedisSerializer<>(objectMapper,ExchangeRate.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
}
