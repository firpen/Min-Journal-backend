package com.minjournal.min_journal_backend.exceptions;

public class UserNoteDoesntExistException extends RuntimeException {
    public UserNoteDoesntExistException(String message) {
        super(message);
    }
}
