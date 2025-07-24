package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberRespDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.service.CachePkgTrackNumberService;
import com.hadyan.teleport.pkgtracknumber.service.PkgTrackNumberService;
import com.hadyan.teleport.pkgtracknumber.usecase.PkgTrackNumberUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PkgTrackNumberUseCaseTest {
    @Mock
    private PkgTrackNumberService pkgTrackNumberService;
    @Mock
    private CachePkgTrackNumberService cachePkgTrackNumberService;
    @InjectMocks
    private PkgTrackNumberUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenNextTrackingNumber_cacheHit() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        String key = "cachekey";
        CachePkgTrackNumber cached = new CachePkgTrackNumber();
        when(cachePkgTrackNumberService.getPkgTrackNumberKey(req)).thenReturn(key);
        when(cachePkgTrackNumberService.findPkgTrackNumberByKey(key)).thenReturn(cached);
        PkgTrackNumberRespDto resp = useCase.genNextTrackingNumber(req);
        assertNotNull(resp);
        assertEquals(cached.getTrackNumber(), resp.trackNumber());
    }

    @Test
    void testGenNextTrackingNumber_dbHit() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        String key = "dbkey";
        when(cachePkgTrackNumberService.getPkgTrackNumberKey(req)).thenReturn(key);
        when(cachePkgTrackNumberService.findPkgTrackNumberByKey(key)).thenReturn(null);
        PkgTrackNumber pkg = new PkgTrackNumber();
        when(pkgTrackNumberService.getTrackNumber(req)).thenReturn(Optional.of(pkg));
        PkgTrackNumberRespDto resp = useCase.genNextTrackingNumber(req);
        assertNotNull(resp);
        assertEquals(pkg.getTrackNumber(), resp.trackNumber());
        verify(cachePkgTrackNumberService).savePkgTrackNumber(key, pkg);
    }

    @Test
    void testGenNextTrackingNumber_newNumber() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        String key = "newkey";
        when(cachePkgTrackNumberService.getPkgTrackNumberKey(req)).thenReturn(key);
        when(cachePkgTrackNumberService.findPkgTrackNumberByKey(key)).thenReturn(null);
        when(pkgTrackNumberService.getTrackNumber(req)).thenReturn(Optional.empty());
        PkgTrackNumber newPkg = new PkgTrackNumber();
        when(pkgTrackNumberService.insertTrackNumber(req)).thenReturn(newPkg);
        PkgTrackNumberRespDto resp = useCase.genNextTrackingNumber(req);
        assertNotNull(resp);
        assertEquals(newPkg.getTrackNumber(), resp.trackNumber());
        verify(cachePkgTrackNumberService).savePkgTrackNumber(key, newPkg);
    }
}