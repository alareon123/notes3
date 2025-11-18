package com.example.notes.config;

import org.aeonbits.owner.ConfigFactory;

public class TestEnv {

    private static final EnvConfig CONFIG = ConfigFactory.create(EnvConfig.class);

    public static String getBaseUrl() {
        return CONFIG.baseUrl();
    }

    public static String getLogLevel() {
        return CONFIG.logLevel();
    }
}
