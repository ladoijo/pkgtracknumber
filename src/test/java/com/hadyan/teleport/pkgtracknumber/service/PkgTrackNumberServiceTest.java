package com.hadyan.teleport.pkgtracknumber.service;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.exception.DuplicatePackageTrackNumberException;
import com.hadyan.teleport.pkgtracknumber.repository.PkgTrackNumberRepo;
import com.mongodb.DuplicateKeyException;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcernResult;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PkgTrackNumberServiceTest {

    @InjectMocks
    private PkgTrackNumberService service;

    @Mock
    private PkgTrackNumberRepo repo;

    private PkgTrackNumber testPkgTrackNumber;
    private PkgTrackNumberReqDto testPkgTrackNumberReqDto;

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
        var packageTimeGenerated = OffsetDateTime.parse(testPkgTrackNumberReqDto.packageCreatedAt()).toEpochSecond();
        testPkgTrackNumber = new PkgTrackNumber(
                TRACK_NUMBER,
                System.currentTimeMillis(),
                testPkgTrackNumberReqDto.originCountryId(),
                testPkgTrackNumberReqDto.destinationCountryId(),
                testPkgTrackNumberReqDto.weight(),
                packageTimeGenerated,
                testPkgTrackNumberReqDto.customerId()
        );
    }

    @Test
    void getTrackNumber_whenPkgTrackNumberNotExists_thenReturnEmpty() {
        when(repo.findBy(
                testPkgTrackNumberReqDto.originCountryId(),
                testPkgTrackNumberReqDto.destinationCountryId(),
                testPkgTrackNumberReqDto.weight(),
                testPkgTrackNumberReqDto.getPackageCreatedAtOffset().toInstant().toEpochMilli(),
                testPkgTrackNumberReqDto.customerId()
        )).thenReturn(Optional.empty());
        var result = service.getTrackNumber(testPkgTrackNumberReqDto);
        assertEquals(Optional.empty(), result);
    }

    @Test
    void getTrackNumber_whenPkgTrackNumberExists_thenReturnPkgTrackNumber() {
        when(repo.findBy(
                testPkgTrackNumberReqDto.originCountryId(),
                testPkgTrackNumberReqDto.destinationCountryId(),
                testPkgTrackNumberReqDto.weight(),
                testPkgTrackNumberReqDto.getPackageCreatedAtOffset().toInstant().toEpochMilli(),
                testPkgTrackNumberReqDto.customerId()
        )).thenReturn(Optional.of(testPkgTrackNumber));
        var result = service.getTrackNumber(testPkgTrackNumberReqDto);
        assertTrue(result.isPresent());
        assertEquals(testPkgTrackNumber, result.get());
    }

    @Test
    void insertTrackNumber_whenValidRequest_thenReturnNewPkgTrackNumber() {
        when(repo.insert(any(PkgTrackNumber.class))).thenAnswer(invocation -> {
            PkgTrackNumber arg = invocation.getArgument(0);
            arg.setId("generated-id");
            return arg;
        });

        var result = service.insertTrackNumber(testPkgTrackNumberReqDto);

        assertNotNull(result.getId());
        assertEquals(testPkgTrackNumberReqDto.originCountryId(), result.getOriginCountryId());
        assertEquals(testPkgTrackNumberReqDto.destinationCountryId(), result.getDestinationCountryId());
        assertEquals(testPkgTrackNumberReqDto.weight(), result.getWeight());
        assertEquals(testPkgTrackNumberReqDto.customerId(), result.getCustomerId());
        assertTrue(result.getPackageCreatedAt() > 0);
        assertNotNull(result.getTrackNumber());
        assertEquals(16, result.getTrackNumber().length());

        verify(repo, times(1)).insert(any(PkgTrackNumber.class));
    }

    @Test
    void insertTrackNumber_whenDuplicateTrackNumber_thenThrowDuplicatePackageTrackNumberException() {
        BsonDocument keyValue = new BsonDocument("_id", new BsonString("duplicate-key"));
        BsonDocument response = new BsonDocument("keyValue", keyValue);
        DuplicateKeyException dke = new DuplicateKeyException(
                response,
                new ServerAddress(),
                WriteConcernResult.unacknowledged()
        );

        when(repo.insert(any(PkgTrackNumber.class))).thenThrow(dke);

        var exception = assertThrows(DuplicatePackageTrackNumberException.class, () -> service.insertTrackNumber(testPkgTrackNumberReqDto));

        assertEquals("Duplicate tracking number", exception.getMessage());
        verify(repo, times(1)).insert(any(PkgTrackNumber.class));
    }
}
