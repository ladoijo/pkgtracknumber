package com.hadyan.teleport.pkgtracknumber.service;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CachePkgTrackNumberServiceTest {

    @Mock
    private RedisTemplate<String, CachePkgTrackNumber> redisTemplate;

    @Mock
    private ValueOperations<String, CachePkgTrackNumber> valueOperations;

    @InjectMocks
    private CachePkgTrackNumberService cacheService;

    private PkgTrackNumberReqDto testRequest;
    private PkgTrackNumber testPkgTrackNumber;
    private final String testKey = "testKey123";

    @BeforeEach
    void setUp() {
        testRequest = new PkgTrackNumberReqDto(
                "ID",
                "MY",
                new BigDecimal("5.5"),
                "2025-06-24T15:30:45+07:00",
                "de619854-b59b-425e-9db4-943979e1bd42",
                "RedBox Logistics",
                "redbox-logistics"
        );

        testPkgTrackNumber = new PkgTrackNumber(
                "id",
                "TRACK123",
                "ID",
                "MY",
                new BigDecimal("5.5"),
                System.currentTimeMillis(),
                "de619854-b59b-425e-9db4-943979e1bd42"
        );
    }

    @Test
    void getPkgTrackNumberKey_shouldReturnConsistentHash() {
        var key1 = cacheService.getPkgTrackNumberKey(testRequest);
        var key2 = cacheService.getPkgTrackNumberKey(testRequest);

        assertNotNull(key1);
        assertEquals(64, key1.length());
        assertEquals(key1, key2);
    }

    @Test
    void findPkgTrackNumberByKey_shouldReturnCachedValue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        var cached = new CachePkgTrackNumber("1", "TRACK123", System.currentTimeMillis());
        when(valueOperations.get("pkg:" + testKey)).thenReturn(cached);

        var result = cacheService.findPkgTrackNumberByKey(testKey);

        assertNotNull(result);
        assertEquals(cached, result);
        verify(redisTemplate.opsForValue()).get("pkg:" + testKey);
    }

    @Test
    void findPkgTrackNumberByKey_whenKeyNotExists_shouldReturnNull() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        when(valueOperations.get("pkg:" + testKey)).thenReturn(null);
        var result = cacheService.findPkgTrackNumberByKey(testKey);
        assertNull(result);
        verify(redisTemplate.opsForValue()).get("pkg:" + testKey);
    }

    @Test
    void savePkgTrackNumber_shouldSaveWithCorrectKeyAndValue() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        doNothing().when(valueOperations).set(eq("pkg:" + testKey), any(CachePkgTrackNumber.class));
        cacheService.savePkgTrackNumber(testKey, testPkgTrackNumber);
        verify(redisTemplate.opsForValue()).set(
                eq("pkg:" + testKey),
                argThat(cache ->
                        cache.getId().equals(testPkgTrackNumber.getId()) &&
                                cache.getTrackNumber().equals(testPkgTrackNumber.getTrackNumber()) &&
                                cache.getCreatedAt() == testPkgTrackNumber.getCreatedAt()
                )
        );
    }

    @Test
    void savePkgTrackNumber_withNullValues_shouldHandleGracefully() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        var nullTrackNumber = new PkgTrackNumber(
                null, null, null, null, 0L, null
        );
        doNothing().when(valueOperations).set(anyString(), any());
        assertDoesNotThrow(() ->
                cacheService.savePkgTrackNumber(testKey, nullTrackNumber)
        );
    }
}