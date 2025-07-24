package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberRespDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

class PkgTrackNumberRespDtoTest {
    @Test
    void testFromCachePkgTrackNumber() {
        CachePkgTrackNumber cache = new CachePkgTrackNumber("id1", "TRACK123", 123456789L);
        PkgTrackNumberRespDto resp = PkgTrackNumberRespDto.from(cache);
        assertEquals("TRACK123", resp.trackNumber());
        OffsetDateTime expected = Instant.ofEpochMilli(123456789L).atOffset(ZoneOffset.UTC);
        assertEquals(expected, resp.createdAt());
    }

    @Test
    void testFromPkgTrackNumber() {
        PkgTrackNumber pkg = new PkgTrackNumber();
        pkg.setTrackNumber("TRACK456");
        pkg.setCreatedAt(987654321L);
        PkgTrackNumberRespDto resp = PkgTrackNumberRespDto.from(pkg);
        assertEquals("TRACK456", resp.trackNumber());
        OffsetDateTime expected = Instant.ofEpochMilli(987654321L).atOffset(ZoneOffset.UTC);
        assertEquals(expected, resp.createdAt());
    }
}