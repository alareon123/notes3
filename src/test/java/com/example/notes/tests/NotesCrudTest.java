package com.example.notes.tests;

import com.example.notes.core.BaseApiTest;
import com.example.notes.data.TestData;
import com.example.notes.endpoints.NotesClient;
import com.example.notes.models.NoteCreateRequest;
import com.example.notes.models.NoteDto;
import com.example.notes.models.NoteUpdateRequest;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Notes CRUD Tests")
public class NotesCrudTest extends BaseApiTest {

    @Test
    @DisplayName("Full CRUD cycle: Create, Read, Update, Delete note")
    public void fullCrudCycle() {
        // 1. Create note
        NoteCreateRequest createRequest = TestData.simpleNote();
        NoteDto createdNote = NotesClient.createNote(createRequest);

        assertThat("Note ID should not be null", createdNote.getId(), notNullValue());
        assertThat("Title should match", createdNote.getTitle(), equalTo(createRequest.getTitle()));
        assertThat("Description should match", createdNote.getDescription(), equalTo(createRequest.getDescription()));
        assertThat("Category should match", createdNote.getCategory(), equalTo(createRequest.getCategory()));
        assertThat("Created at timestamp should not be null", createdNote.getCreatedAt(), notNullValue());

        String noteId = createdNote.getId();

        // 2. Get note by ID
        NoteDto fetchedNote = NotesClient.getNote(noteId);

        assertThat("Fetched note ID should match", fetchedNote.getId(), equalTo(noteId));
        assertThat("Fetched title should match", fetchedNote.getTitle(), equalTo(createRequest.getTitle()));
        assertThat("Fetched description should match", fetchedNote.getDescription(), equalTo(createRequest.getDescription()));

        // 3. Update note
        NoteUpdateRequest updateRequest = TestData.updateNoteData();
        NoteDto updatedNote = NotesClient.updateNote(noteId, updateRequest);

        assertThat("Updated note ID should remain the same", updatedNote.getId(), equalTo(noteId));
        assertThat("Updated title should match", updatedNote.getTitle(), equalTo(updateRequest.getTitle()));
        assertThat("Updated description should match", updatedNote.getDescription(), equalTo(updateRequest.getDescription()));
        assertThat("Updated category should match", updatedNote.getCategory(), equalTo(updateRequest.getCategory()));
        assertThat("Completed status should match", updatedNote.getCompleted(), equalTo(updateRequest.getCompleted()));

        // 4. Delete note
        NotesClient.deleteNote(noteId);

        // 5. Verify note is deleted
        Response response = NotesClient.getNoteResponse(noteId);
        response.then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Create note and verify response fields")
    public void createNoteAndVerifyFields() {
        NoteCreateRequest request = TestData.simpleNote();
        NoteDto note = NotesClient.createNote(request);

        assertThat("Note should have non-null ID", note.getId(), notNullValue());
        assertThat("Note should have non-empty ID", note.getId(), not(emptyString()));
        assertThat("Title should match request", note.getTitle(), equalTo(request.getTitle()));
        assertThat("Description should match request", note.getDescription(), equalTo(request.getDescription()));
        assertThat("Category should match request", note.getCategory(), equalTo(request.getCategory()));
        assertThat("Created at should not be null", note.getCreatedAt(), notNullValue());

        // Cleanup
        NotesClient.deleteNote(note.getId());
    }
}
