package com.softarum.svsa.util;

import org.apache.commons.lang3.RandomStringUtils;

public class GenerateValidation {

    public static String keyValidation() {

        String shortID = RandomStringUtils.randomAlphanumeric(8);
        return shortID;
    }
}