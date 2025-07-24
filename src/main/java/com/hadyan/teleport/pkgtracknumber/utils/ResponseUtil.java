package com.hadyan.teleport.pkgtracknumber.utils;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ResponseUtil {
    public static <T> ResponseEntity<ApiRespDto<?>> okWithData(T data) {
        var resp = new ApiRespDto<>(
                HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "Request successful",
                data,
                null
        );
        return ResponseEntity.status(HttpStatus.OK.value()).body(resp);
    }

    public static ResponseEntity<ApiRespDto<?>> failWithMessage(HttpStatus httpStatus, String errMsg) {
        var resp = new ApiRespDto<>(
                httpStatus.value(),
                httpStatus.name(),
                errMsg,
                null,
                null
        );
        return ResponseEntity.status(httpStatus.value()).body(resp);
    }

    public static ResponseEntity<ApiRespDto<?>> failWithErrors(HttpStatus httpStatus, String errMsg, Errors errors) {
        var objErrors = errors.getFieldErrors().stream()
                .collect(
                        () -> new HashMap<String, List<String>>(),
                        (map, err) -> map
                                .computeIfAbsent(err.getField(), k -> new ArrayList<>())
//                                .add(hasText(err.getDefaultMessage()) ? err.getDefaultMessage() : LanguageUtil.getMessage(err.getCode())),
                                .add(err.getDefaultMessage()),
                        HashMap::putAll
                );

        var resp = new ApiRespDto<>(
                httpStatus.value(),
                httpStatus.name(),
                errMsg,
                null,
                objErrors
        );
        return ResponseEntity.status(httpStatus.value()).body(resp);
    }
}
