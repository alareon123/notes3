package com.example.notes.endpoints;

import com.example.notes.core.Specs;
import com.example.notes.models.NoteCreateRequest;
import com.example.notes.models.NoteDto;
import com.example.notes.models.NoteUpdateRequest;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class NotesClient {

    public static Response createNoteResponse(NoteCreateRequest request) {
        return given()
                .spec(Specs.requestSpec)
                .body(request)
                .when()
                .post(Endpoints.NOTES);
    }

    public static NoteDto createNote(NoteCreateRequest request) {
        return createNoteResponse(request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", NoteDto.class);
    }

    public static Response getNoteResponse(String id) {
        return given()
                .spec(Specs.requestSpec)
                .pathParam("id", id)
                .when()
                .get(Endpoints.NOTES_BY_ID);
    }

    public static NoteDto getNote(String id) {
        return getNoteResponse(id)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", NoteDto.class);
    }

    public static Response updateNoteResponse(String id, NoteUpdateRequest request) {
        return given()
                .spec(Specs.requestSpec)
                .pathParam("id", id)
                .body(request)
                .when()
                .put(Endpoints.NOTES_BY_ID);
    }

    public static NoteDto updateNote(String id, NoteUpdateRequest request) {
        return updateNoteResponse(id, request)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getObject("data", NoteDto.class);
    }

    public static Response deleteNoteResponse(String id) {
        return given()
                .spec(Specs.requestSpec)
                .pathParam("id", id)
                .when()
                .delete(Endpoints.NOTES_BY_ID);
    }

    public static void deleteNote(String id) {
        deleteNoteResponse(id)
                .then()
                .statusCode(200);
    }

    public static Response getAllNotesResponse() {
        return given()
                .spec(Specs.requestSpec)
                .when()
                .get(Endpoints.NOTES);
    }
}
