package com.commuting.commutingapp.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtils {

    private static final int CODE_LENGTH = 4;

    public static String generateCode() {
        return RandomStringUtils.randomNumeric(CODE_LENGTH);
    }
}
