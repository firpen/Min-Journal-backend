package com.minjournal.min_journal_backend.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minjournal.min_journal_backend.models.Note;

public interface NoteRepository extends JpaRepository<Note, Integer> {
    Optional<Note> findByIdAndUserUsername(int id, String username);
    List<Note> findByUserUsername(String username);
    List<Note> findByUserUsernameAndDateBetween(String username, LocalDateTime start, LocalDateTime end);
}
