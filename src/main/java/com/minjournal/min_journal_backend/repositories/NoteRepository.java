package com.minjournal.min_journal_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minjournal.min_journal_backend.models.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    
}
