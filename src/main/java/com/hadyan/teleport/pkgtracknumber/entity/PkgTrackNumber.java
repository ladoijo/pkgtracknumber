package com.hadyan.teleport.pkgtracknumber.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "package_track_number")
@CompoundIndexes({
        @CompoundIndex(name = "unique_tracking_constraint", def = "{'customerId': 1, 'originCountryId': 1, 'destinationCountryId': 1, 'weight': 1, 'packageCreatedAt': 1}", unique = true)
})
public class PkgTrackNumber {
    public PkgTrackNumber(
            String trackNumber,
            long trackNumberCreatedAt,
            String originCountryId,
            String destinationCountryId,
            BigDecimal weight,
            long packageCreatedAt,
            String customerId) {
        this.trackNumber = trackNumber;
        this.trackNumberCreatedAt = trackNumberCreatedAt;
        this.originCountryId = originCountryId;
        this.destinationCountryId = destinationCountryId;
        this.weight = weight;
        this.packageCreatedAt = packageCreatedAt;
        this.customerId = customerId;
    }

    @Id
    private String id;

    @Indexed(unique = true)
    private String trackNumber;

    private long trackNumberCreatedAt;
    private String originCountryId;
    private String destinationCountryId;
    private BigDecimal weight;
    private long packageCreatedAt;
    private String customerId;
}
