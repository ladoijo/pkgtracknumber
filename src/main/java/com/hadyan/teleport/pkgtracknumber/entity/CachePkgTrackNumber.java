package com.hadyan.teleport.pkgtracknumber.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@RedisHash("package_track_number")
@AllArgsConstructor
@NoArgsConstructor
public class CachePkgTrackNumber implements Serializable {
    @Id
    private String id;
    private String trackNumber;
    private long trackNumberCreatedAt;
}
