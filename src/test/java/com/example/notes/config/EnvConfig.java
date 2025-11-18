package com.example.notes.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/local.properties")
public interface EnvConfig extends Config {

    @Key("baseUrl")
    @DefaultValue("https://practice.expandtesting.com/notes/api")
    String baseUrl();

    @Key("log.level")
    @DefaultValue("BASIC")
    String logLevel();
}
