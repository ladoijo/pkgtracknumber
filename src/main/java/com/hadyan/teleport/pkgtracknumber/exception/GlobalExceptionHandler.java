package com.hadyan.teleport.pkgtracknumber.exception;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import com.hadyan.teleport.pkgtracknumber.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<ApiRespDto<?>> handleValidationErrors(BindException e) {
        return ResponseUtil.failWithErrors(HttpStatus.BAD_REQUEST, "Validation failed", e.getBindingResult());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseUtil.failWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleResourceNotFound(NoResourceFoundException ex) {
        return ResponseUtil.failWithMessage(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(DuplicatePackageTrackNumberException.class)
    public ResponseEntity<?> handleDuplicate(DuplicatePackageTrackNumberException ex) {
        return ResponseUtil.failWithMessage(HttpStatus.CONFLICT, ex.getMessage());
    }
}
