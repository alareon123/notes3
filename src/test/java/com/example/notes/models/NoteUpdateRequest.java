package com.example.notes.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NoteUpdateRequest {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    @JsonProperty("completed")
    private Boolean completed;

    public NoteUpdateRequest() {
    }

    public NoteUpdateRequest(String title, String description, String category, Boolean completed) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
