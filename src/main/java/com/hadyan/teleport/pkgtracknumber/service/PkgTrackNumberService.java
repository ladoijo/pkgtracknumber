package com.hadyan.teleport.pkgtracknumber.service;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.exception.DuplicatePackageTrackNumberException;
import com.hadyan.teleport.pkgtracknumber.repository.PkgTrackNumberRepo;
import com.hadyan.teleport.pkgtracknumber.utils.KeyGeneratorUtil;
import com.mongodb.DuplicateKeyException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PkgTrackNumberService {

    private final PkgTrackNumberRepo repo;

    public Optional<PkgTrackNumber> getTrackNumber(PkgTrackNumberReqDto packageTrackNumberReqDto) {
        return repo.findBy(
                packageTrackNumberReqDto.originCountryId(),
                packageTrackNumberReqDto.destinationCountryId(),
                packageTrackNumberReqDto.weight(),
                packageTrackNumberReqDto.getPackageCreatedAtOffset().toInstant().toEpochMilli(),
                packageTrackNumberReqDto.customerId()
        );
    }

    public PkgTrackNumber insertTrackNumber(PkgTrackNumberReqDto packageTrackNumberReqDto) {
        var trackingNumber = KeyGeneratorUtil.generatePackageTrackNumber();
        var newTrackNumber = new PkgTrackNumber(
                trackingNumber,
                Instant.now().toEpochMilli(),
                packageTrackNumberReqDto.originCountryId(),
                packageTrackNumberReqDto.destinationCountryId(),
                packageTrackNumberReqDto.weight(),
                packageTrackNumberReqDto.getPackageCreatedAtOffset().toInstant().toEpochMilli(),
                packageTrackNumberReqDto.customerId()
        );

        try {
            return repo.insert(newTrackNumber);
        } catch (DuplicateKeyException | org.springframework.dao.DuplicateKeyException ex) {
            var message = "Duplicate tracking number";
            log.error("{}: {}", message,  trackingNumber);
            throw new DuplicatePackageTrackNumberException(message);
        }
    }
}
