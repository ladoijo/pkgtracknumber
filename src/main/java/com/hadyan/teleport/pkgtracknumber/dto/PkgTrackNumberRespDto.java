package com.hadyan.teleport.pkgtracknumber.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hadyan.teleport.pkgtracknumber.constant.PatternConst;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public record PkgTrackNumberRespDto(
        String trackNumber,

        @JsonProperty("createdAt")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = PatternConst.STRING_DATE_PATTERN)
        OffsetDateTime trackNumberCreatedAt
) {
        public static PkgTrackNumberRespDto from(CachePkgTrackNumber trackNumber) {
                var offset = ZoneOffset.UTC;
                var offsetDateTime = Instant.ofEpochMilli(trackNumber.getTrackNumberCreatedAt()).atOffset(offset);
                return new PkgTrackNumberRespDto(trackNumber.getTrackNumber(), offsetDateTime);
        }

        public static PkgTrackNumberRespDto from(PkgTrackNumber trackNumber) {
                var offset = ZoneOffset.UTC;
                var offsetDateTime = Instant.ofEpochMilli(trackNumber.getTrackNumberCreatedAt()).atOffset(offset);
                return new PkgTrackNumberRespDto(trackNumber.getTrackNumber(), offsetDateTime);
        }
}
