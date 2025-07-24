package com.hadyan.teleport.pkgtracknumber.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("package_track_number")
@NoArgsConstructor
public class CachePkgTrackNumber implements Serializable {
    public CachePkgTrackNumber(
            String id,
            String trackNumber,
            long createdAt) {
        this.id = id;
        this.trackNumber = trackNumber;
        this.createdAt = createdAt;
    }

    @Id
    private String id;
    private String trackNumber;
    private long createdAt;
}
