package com.example.notes.tests;

import com.example.notes.core.BaseApiTest;
import com.example.notes.endpoints.NotesClient;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;

@DisplayName("Smoke Tests")
public class SmokeTest extends BaseApiTest {

    @Test
    @DisplayName("API should be available and respond with 200 OK")
    public void apiShouldBeAvailable() {
        Response response = NotesClient.getAllNotesResponse();

        response.then()
                .statusCode(200)
                .body("success", equalTo(true));
    }
}
