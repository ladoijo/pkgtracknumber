package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class PkgTrackNumberReqDtoTest {
    @Test
    void testGetCreatedAtOffset() {
        String dateStr = "2024-07-01T12:34:56+07:00";
        PkgTrackNumberReqDto req = new PkgTrackNumberReqDto(
                "ID", "SG", BigDecimal.valueOf(1.5), dateStr, "123e4567-e89b-12d3-a456-426614174000", "John Doe",
                "john-doe");
        OffsetDateTime odt = req.getCreatedAtOffset();
        assertEquals(OffsetDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")), odt);
    }
}