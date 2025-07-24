package com.hadyan.teleport.pkgtracknumber.configuration;

import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, CachePkgTrackNumber> redisPkgTrackNumberTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, CachePkgTrackNumber> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        Jackson2JsonRedisSerializer<CachePkgTrackNumber> serializer = new Jackson2JsonRedisSerializer<>(CachePkgTrackNumber.class);
        template.setValueSerializer(serializer);
        return template;
    }
}
