package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import com.hadyan.teleport.pkgtracknumber.exception.DuplicatePackageTrackNumberException;
import com.hadyan.teleport.pkgtracknumber.exception.GlobalExceptionHandler;
import com.hadyan.teleport.pkgtracknumber.utils.ResponseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationErrors() {
        BindException ex = mock(BindException.class);
        ResponseEntity<ApiRespDto<?>> resp = handler.handleValidationErrors(ex);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Validation failed", resp.getBody().message());
    }

    @Test
    void testHandleGeneric() {
        Exception ex = new Exception("Something went wrong");
        ResponseEntity<?> resp = handler.handleGeneric(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, ((ApiRespDto<?>) resp.getBody()).code());
        assertEquals("Something went wrong", ((ApiRespDto<?>) resp.getBody()).message());
    }

    @Test
    void testHandleResourceNotFound() {
        NoResourceFoundException ex = new NoResourceFoundException(HttpMethod.GET, "/notfound");
        ResponseEntity<?> resp = handler.handleResourceNotFound(ex);
        assertEquals(HttpStatus.NOT_FOUND, ((ApiRespDto<?>) resp.getBody()).code());
    }

    @Test
    void testHandleDuplicate() {
        DuplicatePackageTrackNumberException ex = new DuplicatePackageTrackNumberException("Duplicate!");
        ResponseEntity<?> resp = handler.handleDuplicate(ex);
        assertEquals(HttpStatus.CONFLICT, ((ApiRespDto<?>) resp.getBody()).code());
        assertEquals("Duplicate!", ((ApiRespDto<?>) resp.getBody()).message());
    }
}