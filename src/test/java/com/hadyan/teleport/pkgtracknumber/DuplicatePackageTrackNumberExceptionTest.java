package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.exception.DuplicatePackageTrackNumberException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DuplicatePackageTrackNumberExceptionTest {
    @Test
    void testExceptionMessage() {
        String msg = "Duplicate!";
        DuplicatePackageTrackNumberException ex = new DuplicatePackageTrackNumberException(msg);
        assertEquals(msg, ex.getMessage());
    }
}