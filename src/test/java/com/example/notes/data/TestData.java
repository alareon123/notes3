package com.example.notes.data;

import com.example.notes.models.NoteCreateRequest;
import com.example.notes.models.NoteUpdateRequest;

public class TestData {

    public static NoteCreateRequest simpleNote() {
        return new NoteCreateRequest(
                "Test Note Title",
                "This is a test note description",
                "Home"
        );
    }

    public static NoteCreateRequest noteWithEmptyTitle() {
        return new NoteCreateRequest(
                "",
                "Note with empty title",
                "Work"
        );
    }

    public static NoteCreateRequest noteWithEmptyDescription() {
        return new NoteCreateRequest(
                "Note without description",
                "",
                "Personal"
        );
    }

    public static NoteUpdateRequest updateNoteData() {
        return new NoteUpdateRequest(
                "Updated Note Title",
                "Updated description content",
                "Work",
                true
        );
    }
}
