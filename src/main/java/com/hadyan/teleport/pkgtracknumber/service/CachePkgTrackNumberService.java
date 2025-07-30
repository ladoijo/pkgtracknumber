package com.hadyan.teleport.pkgtracknumber.service;

import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.entity.CachePkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.entity.PkgTrackNumber;
import com.hadyan.teleport.pkgtracknumber.utils.KeyGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CachePkgTrackNumberService {
    private final RedisTemplate<String, CachePkgTrackNumber> redisTemplate;

    public String getPkgTrackNumberKey(PkgTrackNumberReqDto packageTrackNumberReqDto) {
        var millisCreatedAt = packageTrackNumberReqDto.getPackageCreatedAtOffset().toInstant().toEpochMilli();
        var redisKey = String.format("%s:%s:%s:%d:%3f",
                packageTrackNumberReqDto.customerId(),
                packageTrackNumberReqDto.originCountryId(),
                packageTrackNumberReqDto.destinationCountryId(),
                millisCreatedAt,
                packageTrackNumberReqDto.weight()
        );
        return KeyGeneratorUtil.sha256(redisKey);
    }

    public CachePkgTrackNumber findPkgTrackNumberByKey(String key) {
        return redisTemplate.opsForValue().get("pkg:" + key);
    }

    public void savePkgTrackNumber(String key, PkgTrackNumber pkgTrackNumber) {
        var cachePkgTrackNumber = new CachePkgTrackNumber(
                pkgTrackNumber.getId(),
                pkgTrackNumber.getTrackNumber(),
                pkgTrackNumber.getTrackNumberCreatedAt()
        );
        redisTemplate.opsForValue().set("pkg:" + key, cachePkgTrackNumber);
    }
}
