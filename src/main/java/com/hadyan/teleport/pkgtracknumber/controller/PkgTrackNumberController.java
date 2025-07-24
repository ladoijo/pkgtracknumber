package com.hadyan.teleport.pkgtracknumber.controller;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import com.hadyan.teleport.pkgtracknumber.dto.PkgTrackNumberReqDto;
import com.hadyan.teleport.pkgtracknumber.usecase.PkgTrackNumberUseCase;
import com.hadyan.teleport.pkgtracknumber.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PkgTrackNumberController {
    private final PkgTrackNumberUseCase useCase;

    @GetMapping("/v1/next-tracking-number")
    public ResponseEntity<ApiRespDto<?>> genNextTrackingNumber(@Valid PkgTrackNumberReqDto reqDto) {
        var data = useCase.genNextTrackingNumber(reqDto);
        return ResponseUtil.okWithData(data);
    }
}
