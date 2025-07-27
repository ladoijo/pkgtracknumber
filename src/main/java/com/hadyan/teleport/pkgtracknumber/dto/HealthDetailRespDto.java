package com.hadyan.teleport.pkgtracknumber.dto;

public record HealthDetailRespDto(
        String mongoDb,
        String redis,
        String application
) {}
