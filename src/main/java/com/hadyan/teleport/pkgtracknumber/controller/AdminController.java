package com.hadyan.teleport.pkgtracknumber.controller;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import com.hadyan.teleport.pkgtracknumber.service.CachePkgTrackNumberService;
import com.hadyan.teleport.pkgtracknumber.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {
    private final CachePkgTrackNumberService service;

    @DeleteMapping("/v1/admin/cache/flush-db")
    public ResponseEntity<ApiRespDto<?>> flushDbCache() {
        service.flushDb();
        return ResponseUtil.okWithData(null);
    }

    @DeleteMapping("/v1/admin/cache/flush-all")
    public ResponseEntity<ApiRespDto<?>> flushAllCache() {
        service.flushAll();
        return ResponseUtil.okWithData(null);
    }
}
