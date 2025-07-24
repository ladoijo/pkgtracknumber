package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.exception.DuplicatePackageTrackNumberException;
import com.hadyan.teleport.pkgtracknumber.repository.PkgTrackNumberRepo;
import com.hadyan.teleport.pkgtracknumber.service.PkgTrackNumberService;
import com.hadyan.teleport.pkgtracknumber.utils.KeyGeneratorUtil;
import com.mongodb.DuplicateKeyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PkgTrackNumberServiceTest {
    @Mock
    private PkgTrackNumberRepo repo;

    @InjectMocks
    private PkgTrackNumberService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetTrackNumber_found() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        PkgTrackNumber pkg = new PkgTrackNumber();
        when(req.originCountryId()).thenReturn("ID");
        when(req.destinationCountryId()).thenReturn("SG");
        when(req.weight()).thenReturn(BigDecimal.valueOf(1.5));
        when(req.getCreatedAtOffset()).thenReturn(OffsetDateTime.now());
        when(req.customerId()).thenReturn("customer-1");
        when(repo.findBy(anyString(), anyString(), any(), anyLong(), anyString())).thenReturn(Optional.of(pkg));
        Optional<PkgTrackNumber> result = service.getTrackNumber(req);
        assertTrue(result.isPresent());
        assertEquals(pkg, result.get());
    }

    @Test
    void testGetTrackNumber_notFound() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        when(req.originCountryId()).thenReturn("ID");
        when(req.destinationCountryId()).thenReturn("SG");
        when(req.weight()).thenReturn(BigDecimal.valueOf(1.5));
        when(req.getCreatedAtOffset()).thenReturn(OffsetDateTime.now());
        when(req.customerId()).thenReturn("customer-1");
        when(repo.findBy(anyString(), anyString(), any(), anyLong(), anyString())).thenReturn(Optional.empty());
        Optional<PkgTrackNumber> result = service.getTrackNumber(req);
        assertFalse(result.isPresent());
    }

    @Test
    void testInsertTrackNumber_success() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        when(req.originCountryId()).thenReturn("ID");
        when(req.destinationCountryId()).thenReturn("SG");
        when(req.weight()).thenReturn(BigDecimal.valueOf(1.5));
        when(req.customerId()).thenReturn("customer-1");
        when(repo.insert(any())).thenAnswer(invocation -> invocation.getArgument(0));
        PkgTrackNumber result = service.insertTrackNumber(req);
        assertNotNull(result);
        assertNotNull(result.getTrackNumber());
        assertEquals("ID", result.getOriginCountryId());
        assertEquals("SG", result.getDestinationCountryId());
        assertEquals(BigDecimal.valueOf(1.5), result.getWeight());
        assertEquals("customer-1", result.getCustomerId());
    }

    @Test
    void testInsertTrackNumber_duplicate() {
        PkgTrackNumberReqDto req = mock(PkgTrackNumberReqDto.class);
        when(req.originCountryId()).thenReturn("ID");
        when(req.destinationCountryId()).thenReturn("SG");
        when(req.weight()).thenReturn(BigDecimal.valueOf(1.5));
        when(req.customerId()).thenReturn("customer-1");
        when(repo.insert(any())).thenThrow(new DuplicateKeyException("Duplicate"));
        assertThrows(DuplicatePackageTrackNumberException.class, () -> service.insertTrackNumber(req));
    }
}