package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.dto.ApiRespDto;
import com.hadyan.teleport.pkgtracknumber.utils.ResponseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResponseUtilTest {
    @Test
    void testOkWithData() {
        String data = "test";
        ResponseEntity<ApiRespDto<?>> resp = ResponseUtil.okWithData(data);
        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertEquals("Request successful", resp.getBody().message());
        assertEquals(data, resp.getBody().data());
    }

    @Test
    void testFailWithMessage() {
        String msg = "error";
        ResponseEntity<ApiRespDto<?>> resp = ResponseUtil.failWithMessage(HttpStatus.BAD_REQUEST, msg);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals(msg, resp.getBody().message());
        assertNull(resp.getBody().data());
    }

    @Test
    void testFailWithErrors() {
        Errors errors = mock(Errors.class);
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(errors.getFieldErrors()).thenReturn(List.of(fieldError));
        ResponseEntity<ApiRespDto<?>> resp = ResponseUtil.failWithErrors(HttpStatus.BAD_REQUEST, "Validation failed",
                errors);
        assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
        assertEquals("Validation failed", resp.getBody().message());
        assertNotNull(resp.getBody().errors());
        assertTrue(resp.getBody().errors().containsKey("field"));
        assertEquals(List.of("error message"), resp.getBody().errors().get("field"));
    }
}