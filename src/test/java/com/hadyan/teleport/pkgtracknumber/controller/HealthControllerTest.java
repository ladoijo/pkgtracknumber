package com.hadyan.teleport.pkgtracknumber.controller;

import com.hadyan.teleport.pkgtracknumber.dto.HealthDetailRespDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.mongodb.client.MongoDatabase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HealthControllerTest {

    @InjectMocks
    private HealthController controller;

    @Mock
    private MongoTemplate mongoTemplate;

    @Mock
    private MongoDatabase mongoDb;

    @Mock
    private RedisTemplate<String, CachePkgTrackNumber> redisTemplate;

    @Mock
    private RedisConnectionFactory redisConnectionFactory;

    @Mock
    private RedisConnection redisConnection;

    @Test
    void getHealthDetail_whenFailedConnectToMongo_thenReturnDown() {
        // Mock MongoDB failure
        when(mongoTemplate.getDb()).thenThrow(new RuntimeException("Failed to connect to MongoDB"));
        
        // Mock Redis success
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenReturn("PONG");

        // Execute
        var response = controller.detailHealth();
        var data = (HealthDetailRespDto) response.getBody().data();
        
        // Verify
        assertEquals("DOWN", data.mongoDb());
        assertEquals("UP", data.redis());
        assertEquals("UP", data.application());
        
        // Verify interactions
        verify(mongoTemplate).getDb();
        verify(redisTemplate).getConnectionFactory();
        verify(redisConnectionFactory).getConnection();
        verify(redisConnection).ping();
    }

    @Test
    void getHealthDetail_whenFailedConnectToRedis_thenReturnDown() {
        // Mock MongoDB success
        when(mongoTemplate.getDb()).thenReturn(mongoDb);
        when(mongoDb.getName()).thenReturn("MongoDB");

        // Mock Redis failure
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenThrow(new RuntimeException("Failed to connect to Redis"));

        // Execute
        var response = controller.detailHealth();
        var data = (HealthDetailRespDto) response.getBody().data();

        // Verify
        assertEquals("UP", data.mongoDb());
        assertEquals("DOWN", data.redis());
        assertEquals("UP", data.application());

        // Verify interactions
        verify(mongoTemplate).getDb();
        verify(redisTemplate).getConnectionFactory();
        verify(redisConnectionFactory).getConnection();
        verify(redisConnection).ping();
    }

    @Test
    void getHealthDetail_whenAllServicesUp_thenReturnUp() {
        // Mock MongoDB success
        when(mongoTemplate.getDb()).thenReturn(mongoDb);
        when(mongoDb.getName()).thenReturn("MongoDB");

        /// Mock Redis success
        when(redisTemplate.getConnectionFactory()).thenReturn(redisConnectionFactory);
        when(redisConnectionFactory.getConnection()).thenReturn(redisConnection);
        when(redisConnection.ping()).thenReturn("PONG");

        // Execute
        var response = controller.detailHealth();
        var data = (HealthDetailRespDto) response.getBody().data();

        // Verify
        assertEquals("UP", data.mongoDb());
        assertEquals("UP", data.redis());
        assertEquals("UP", data.application());

        // Verify interactions
        verify(mongoTemplate).getDb();
        verify(redisTemplate).getConnectionFactory();
        verify(redisConnectionFactory).getConnection();
        verify(redisConnection).ping();
    }
}
