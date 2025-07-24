package com.hadyan.teleport.pkgtracknumber.dto;

import com.hadyan.teleport.pkgtracknumber.constant.PatternConst;
import jakarta.validation.constraints.*;
import org.springframework.web.bind.annotation.BindParam;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import static com.hadyan.teleport.pkgtracknumber.constant.PatternConst.STRING_DATE_PATTERN;

public record PkgTrackNumberReqDto(
        @BindParam("origin_country_id")
        @NotBlank(message = "origin_country_id is required")
        @Pattern(
                regexp = PatternConst.REGEX_ISO_3166_1_AL_2_PATTERN,
                message = "origin_country_id must be in valid ISO 3166-1 alpha-2 format"
        )
        String originCountryId,

        @BindParam("destination_country_id")
        @NotBlank(message = "destination_country_id is required")
        @Pattern(
                regexp = PatternConst.REGEX_ISO_3166_1_AL_2_PATTERN,
                message = "destination_country_id must be in valid ISO 3166-1 alpha-2 format"
        )
        String destinationCountryId,

        @BindParam("weight")
        @DecimalMin(value = "0.001", message = "Value must be greater than 0")
        @Digits(integer = 10, fraction = 3, message = "Value must have at most 3 decimal places")
        BigDecimal weight,

        @BindParam("created_at")
        @NotNull(message = "created_at is required")
        @Pattern(
                regexp = PatternConst.REGEX_RFC_3339_PATTERN,
                message = "created_at must be in valid RFC 3339 format"
        )
        String createdAt,

        @BindParam("customer_id")
        @NotNull(message = "customer_id is required")
        @Pattern(
                regexp = PatternConst.REGEX_UUID_PATTERN,
                message = "customer_id must be in valid UUID format"
        )
        String customerId,

        @BindParam("customer_name")
        @NotBlank(message = "customer_name is required")
        String customerName,

        @BindParam("customer_slug")
        @NotBlank(message = "customer_slug is required")
        @Pattern(
                regexp = PatternConst.REGEX_SLUG_PATTERN,
                message = "customer_slug must be in valid slug format"
        )
        String customerSlug
) {
        public OffsetDateTime getCreatedAtOffset() {
                var formatter = DateTimeFormatter.ofPattern(STRING_DATE_PATTERN);
                return OffsetDateTime.parse(this.createdAt, formatter);
        }
}
