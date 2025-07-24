package com.hadyan.teleport.pkgtracknumber;

import com.hadyan.teleport.pkgtracknumber.utils.KeyGeneratorUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyGeneratorUtilTest {
    @Test
    void testGeneratePackageTrackNumber() {
        String trackNumber = KeyGeneratorUtil.generatePackageTrackNumber();
        assertNotNull(trackNumber);
        assertEquals(16, trackNumber.length());
        assertTrue(trackNumber.matches("[A-Z0-9]{16}"));
    }

    @Test
    void testSha256() {
        String input = "testinput";
        String hash = KeyGeneratorUtil.sha256(input);
        assertNotNull(hash);
        assertEquals(64, hash.length());
        assertTrue(hash.matches("[a-f0-9]{64}"));
        // Check deterministic
        assertEquals(KeyGeneratorUtil.sha256(input), hash);
    }
}