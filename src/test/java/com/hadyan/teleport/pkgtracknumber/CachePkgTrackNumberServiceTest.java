package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.service.CachePkgTrackNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CachePkgTrackNumberServiceTest {
    @Mock
    private RedisTemplate<String, CachePkgTrackNumber> redisTemplate;
    @Mock
    private ValueOperations<String, CachePkgTrackNumber> valueOperations;
    @InjectMocks
    private CachePkgTrackNumberService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testGetPkgTrackNumberKey() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        when(req.customerId()).thenReturn("customer-1");
        when(req.originCountryId()).thenReturn("ID");
        when(req.destinationCountryId()).thenReturn("SG");
        when(req.getCreatedAtOffset()).thenReturn(OffsetDateTime.now());
        when(req.weight()).thenReturn(BigDecimal.valueOf(1.5));
        String key = service.getPkgTrackNumberKey(req);
        assertNotNull(key);
        assertEquals(64, key.length()); // SHA-256 hex length
    }

    @Test
    void testFindPkgTrackNumberByKey() {
        CachePkgTrackNumber cache = new CachePkgTrackNumber();
        when(valueOperations.get("pkg:somekey")).thenReturn(cache);
        CachePkgTrackNumber result = service.findPkgTrackNumberByKey("somekey");
        assertEquals(cache, result);
    }

    @Test
    void testSavePkgTrackNumber() {
        PkgTrackNumber pkg = new PkgTrackNumber();
        pkg.setId("id1");
        pkg.setTrackNumber("TRACK123");
        pkg.setCreatedAt(123456789L);
        service.savePkgTrackNumber("somekey", pkg);
        verify(valueOperations).set(eq("pkg:somekey"), any(CachePkgTrackNumber.class));
    }
}