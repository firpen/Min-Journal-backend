package com.minjournal.min_journal_backend.dtos;

import com.minjournal.min_journal_backend.models.Status;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class NoteDto {

    @NotBlank
    @Size(max = 1000)
    String note;

    @NotNull
    Status status;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
