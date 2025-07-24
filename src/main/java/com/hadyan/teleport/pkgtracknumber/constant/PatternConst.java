package com.hadyan.teleport.pkgtracknumber.constant;

public class PatternConst {
    public static final String REGEX_UUID_PATTERN = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";
    public static final String REGEX_RFC_3339_PATTERN = "^\\d{4}-[01]\\d-[0-3]\\d[T ]([01]\\d|2[0-3]):[0-5]\\d(:[0-5]\\d)?([.,]\\d+)?([+-][01]\\d:[0-5]\\d|Z)$";
    public static final String REGEX_SLUG_PATTERN = "^[a-z0-9]+(-[a-z0-9]+)*$";
    public static final String REGEX_ISO_3166_1_AL_2_PATTERN = "[A-Z]{2}";
    public static final String STRING_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ssXXX";
}
