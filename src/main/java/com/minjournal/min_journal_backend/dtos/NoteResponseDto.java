package com.minjournal.min_journal_backend.dtos;

import java.time.LocalDateTime;

import com.minjournal.min_journal_backend.models.Status;

public class NoteResponseDto {

    String noteText;
    Status status;
    LocalDateTime date;
    int id;
    
      public NoteResponseDto(String noteText, Status status, LocalDateTime date, int id) {
        this.noteText = noteText;
        this.status = status;
        this.date = date;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
