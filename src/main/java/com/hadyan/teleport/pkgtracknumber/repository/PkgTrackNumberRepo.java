package com.hadyan.teleport.pkgtracknumber.repository;

import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface PkgTrackNumberRepo extends MongoRepository<PkgTrackNumber, String> {
    @Query("{ 'originCountryId': ?0, 'destinationCountryId': ?1, 'weight': ?2, 'packageCreatedAt': ?3, 'customerId': ?4 }")
    Optional<PkgTrackNumber> findBy(
            String originCountryId,
            String destinationCountryId,
            BigDecimal weight,
            long packageCreatedAt,
            String customerId
    );
}
