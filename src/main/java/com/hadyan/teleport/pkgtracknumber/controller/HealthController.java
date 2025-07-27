package com.hadyan.teleport.pkgtracknumber.controller;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import com.hadyan.teleport.pkgtracknumber.dto.HealthDetailRespDto;
import com.hadyan.teleport.pkgtracknumber.dto.HealthRespDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthController {
    private final MongoTemplate mongoTemplate;
    private final RedisTemplate<String, CachePkgTrackNumber> redisTemplate;

    @GetMapping("/v1/health")
    public ResponseEntity<ApiRespDto<?>> home() {
        var data = new HealthRespDto(
                "Package Track Number Service is running",
                "UP"
        );
        return ResponseUtil.okWithData(data);
    }

    @GetMapping("/v1/health/detail")
    public ResponseEntity<ApiRespDto<?>> detailHealth() {
        var mongoDb = "DOWN";
        var redis = "DOWN";
        var application = "UP";

        // Check MongoDB
        try {
            mongoTemplate.getDb().getName();
            mongoDb = "UP";
        } catch (Exception e) {
            mongoDb = "DOWN";
        }

        // Check Redis
        try {
            redisTemplate.getConnectionFactory().getConnection().ping();
            redis = "UP";
        } catch (Exception e) {
            redis = "DOWN";
        }

        var data = new HealthDetailRespDto(mongoDb, redis, application);
        return ResponseUtil.okWithData(data);
    }
}
