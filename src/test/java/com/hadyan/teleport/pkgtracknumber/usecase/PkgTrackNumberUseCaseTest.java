package com.hadyan.teleport.pkgtracknumber.usecase;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberRespDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.service.CachePkgTrackNumberService;
import com.hadyan.teleport.pkgtracknumber.service.PkgTrackNumberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PkgTrackNumberUseCaseTest {

    @InjectMocks
    private PkgTrackNumberUseCase useCase;

    @Mock
    private PkgTrackNumberService service;

    @Mock
    private CachePkgTrackNumberService cacheService;

    private PkgTrackNumber testPkgTrackNumber;
    private PkgTrackNumberReqDto testPkgTrackNumberReqDto;
    private CachePkgTrackNumber testCachePkgTrackNumber;
    private PkgTrackNumberRespDto testPkgTrackNumberRespDto;

    @BeforeEach
    void setUp() {
        testPkgTrackNumberReqDto = new PkgTrackNumberReqDto(
                "ID",
                "MY",
                new BigDecimal("5.5"),
                "2025-06-24T15:30:45+07:00",
                "de619854-b59b-425e-9db4-943979e1bd42",
                "RedBox Logistics",
                "redbox-logistics"
        );

        var TRACK_NUMBER = "IIMSUYKV6AY32YHY";
        var packageCreatedAt = OffsetDateTime.parse(testPkgTrackNumberReqDto.packageCreatedAt()).toEpochSecond();
        var trackNumberCreatedAt = Instant.now().toEpochMilli();
        testPkgTrackNumber = new PkgTrackNumber(
                TRACK_NUMBER,
                trackNumberCreatedAt,
                testPkgTrackNumberReqDto.originCountryId(),
                testPkgTrackNumberReqDto.destinationCountryId(),
                testPkgTrackNumberReqDto.weight(),
                packageCreatedAt,
                testPkgTrackNumberReqDto.customerId()
        );

        testCachePkgTrackNumber = new CachePkgTrackNumber("id", TRACK_NUMBER, trackNumberCreatedAt);
        testPkgTrackNumberRespDto = PkgTrackNumberRespDto.from(testCachePkgTrackNumber);
    }

    @Test
    void generatePkgTrackNumber_whenPkgTrackNumberCached_thenReturnPkgTrackNumberCached() {
        when(cacheService.getPkgTrackNumberKey(testPkgTrackNumberReqDto)).thenReturn("key");
        when(cacheService.findPkgTrackNumberByKey("key")).thenReturn(testCachePkgTrackNumber);
        var result = useCase.genNextTrackingNumber(testPkgTrackNumberReqDto);
        assertEquals(testPkgTrackNumberRespDto, result);

        verify(service, never()).getTrackNumber(testPkgTrackNumberReqDto);
        verify(cacheService, never()).savePkgTrackNumber("key", testPkgTrackNumber);
        verify(service, never()).insertTrackNumber(testPkgTrackNumberReqDto);
    }

    @Test
    void generatePkgTrackNumber_whenPkgTrackNumberRowExists_thenReturnPkgTrackNumberFromDb() {
        when(cacheService.getPkgTrackNumberKey(testPkgTrackNumberReqDto)).thenReturn("key");
        when(cacheService.findPkgTrackNumberByKey("key")).thenReturn(null);
        when(service.getTrackNumber(testPkgTrackNumberReqDto)).thenReturn(Optional.of(testPkgTrackNumber));
        var result = useCase.genNextTrackingNumber(testPkgTrackNumberReqDto);
        assertEquals(testPkgTrackNumberRespDto, result);
        verify(cacheService, times(1)).savePkgTrackNumber("key", testPkgTrackNumber);
        verify(service, never()).insertTrackNumber(testPkgTrackNumberReqDto);
    }

    @Test
    void generatePkgTrackNumber_whenPkgTrackNumberRowNotExists_thenInsertPkgTrackNumberToDb() {
        when(cacheService.getPkgTrackNumberKey(testPkgTrackNumberReqDto)).thenReturn("key");
        when(cacheService.findPkgTrackNumberByKey("key")).thenReturn(null);
        when(service.getTrackNumber(testPkgTrackNumberReqDto)).thenReturn(Optional.empty());
        when(service.insertTrackNumber(testPkgTrackNumberReqDto)).thenReturn(testPkgTrackNumber);
        var result = useCase.genNextTrackingNumber(testPkgTrackNumberReqDto);
        assertEquals(testPkgTrackNumberRespDto, result);
        verify(cacheService, times(1)).savePkgTrackNumber("key", testPkgTrackNumber);
    }
}
