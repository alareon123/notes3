package com.example.notes.tests;

import com.example.notes.core.BaseApiTest;
import com.example.notes.data.TestData;
import com.example.notes.endpoints.NotesClient;
import com.example.notes.models.NoteCreateRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.notNullValue;

@DisplayName("Notes Negative Tests")
public class NotesNegativeTest extends BaseApiTest {

    @Test
    @DisplayName("Create note with empty title should return 400")
    public void createNoteWithEmptyTitleShouldFail() {
        NoteCreateRequest request = TestData.noteWithEmptyTitle();
        Response response = NotesClient.createNoteResponse(request);

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Create note with empty description should return 400")
    public void createNoteWithEmptyDescriptionShouldFail() {
        NoteCreateRequest request = TestData.noteWithEmptyDescription();
        Response response = NotesClient.createNoteResponse(request);

        response.then()
                .statusCode(400)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Get note with invalid ID should return 404")
    public void getNoteWithInvalidIdShouldReturn404() {
        String invalidId = "invalid-note-id-12345";
        Response response = NotesClient.getNoteResponse(invalidId);

        response.then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Delete non-existent note should return 404")
    public void deleteNonExistentNoteShouldReturn404() {
        String nonExistentId = "non-existent-note-id-99999";
        Response response = NotesClient.deleteNoteResponse(nonExistentId);

        response.then()
                .statusCode(404)
                .body("message", notNullValue());
    }

    @Test
    @DisplayName("Update non-existent note should return 404")
    public void updateNonExistentNoteShouldReturn404() {
        String nonExistentId = "non-existent-note-id-88888";
        Response response = NotesClient.updateNoteResponse(nonExistentId, TestData.updateNoteData());

        response.then()
                .statusCode(404)
                .body("message", notNullValue());
    }
}
