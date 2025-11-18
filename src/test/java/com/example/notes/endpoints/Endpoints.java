package com.example.notes.endpoints;

public class Endpoints {

    public static final String NOTES = "/notes";
    public static final String NOTES_BY_ID = "/notes/{id}";

    public static String noteById(String id) {
        return NOTES_BY_ID.replace("{id}", id);
    }
}
