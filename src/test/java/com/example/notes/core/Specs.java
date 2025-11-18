package com.example.notes.core;

import com.example.notes.config.TestEnv;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class Specs {

    public static RequestSpecification requestSpec;

    public static void setupRequestSpec() {
        requestSpec = new RequestSpecBuilder()
                .setBaseUri(TestEnv.getBaseUrl())
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
    }
}
