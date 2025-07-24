package com.hadyan.teleport.pkgtracknumber.usecase;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberRespDto;
import com.hadyan.teleport.pkgtracknumber.service.CachePkgTrackNumberService;
import com.hadyan.teleport.pkgtracknumber.service.PkgTrackNumberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PkgTrackNumberUseCase {
    private final PkgTrackNumberService service;
    private final CachePkgTrackNumberService cacheService;

    @Transactional
    public PkgTrackNumberRespDto genNextTrackingNumber(PkgTrackNumberReqDto reqDto) {
        var key = cacheService.getPkgTrackNumberKey(reqDto);
        var pkgTrackNumberCached = cacheService.findPkgTrackNumberByKey(key);
        if (pkgTrackNumberCached != null) return PkgTrackNumberRespDto.from(pkgTrackNumberCached);

        var pkgTrackNumberDb = service.getTrackNumber(reqDto);
        if (pkgTrackNumberDb.isPresent()) {
            cacheService.savePkgTrackNumber(key, pkgTrackNumberDb.get());
            return PkgTrackNumberRespDto.from(pkgTrackNumberDb.get());
        }

        var newPkgTrackNumber = service.insertTrackNumber(reqDto);
        cacheService.savePkgTrackNumber(key, newPkgTrackNumber);
        return PkgTrackNumberRespDto.from(newPkgTrackNumber);
    }
}
