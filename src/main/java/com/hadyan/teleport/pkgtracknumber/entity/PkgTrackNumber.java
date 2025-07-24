package com.hadyan.teleport.pkgtracknumber.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "package_track_number")
public class PkgTrackNumber {
    public PkgTrackNumber(
            String trackNumber,
            String originCountryId,
            String destinationCountryId,
            BigDecimal weight,
            long createdAt,
            String customerId) {
        this.trackNumber = trackNumber;
        this.originCountryId = originCountryId;
        this.destinationCountryId = destinationCountryId;
        this.weight = weight;
        this.createdAt = createdAt;
        this.customerId = customerId;
    }

    @Id
    private String id;

    @Indexed(unique = true)
    private String trackNumber;
    private String originCountryId;
    private String destinationCountryId;
    private BigDecimal weight;
    private long createdAt;
    private String customerId;
}
