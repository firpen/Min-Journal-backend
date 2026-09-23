package com.minjournal.min_journal_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.minjournal.min_journal_backend.dtos.NoteDto;
import com.minjournal.min_journal_backend.dtos.NoteResponseDto;
import com.minjournal.min_journal_backend.models.Status;
import com.minjournal.min_journal_backend.services.NoteService;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping()
    public ResponseEntity<Void> createNote(@Valid @RequestBody NoteDto note, Authentication authentication) {
        String username = authentication.getName();
        noteService.createNote(note.getNote(), note.getStatus(), username);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable int id, Authentication authentication) {
        String username = authentication.getName();
        noteService.deleteNote(id, username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateNote(@PathVariable int id, @Valid @RequestBody NoteDto note,
            Authentication authentication) {
        String username = authentication.getName();
        noteService.updateNote(note.getNote(), note.getStatus(), id, username);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping()
    public ResponseEntity<List<NoteResponseDto>> getNotes(Authentication authentication) {
        String username = authentication.getName();
        List<NoteResponseDto> notes = noteService.getNotes(username);
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/notesbetweendates")
    public ResponseEntity<List<NoteResponseDto>> getNotesBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            Authentication authentication) {
        String username = authentication.getName();
        List<NoteResponseDto> notes = noteService.getNotesBetweenDates(username, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(notes);
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<Status, Double>> getStats(Authentication authentication,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        String username = authentication.getName();
        Map<Status, Double> percentages = noteService.getStatistics(username, start, end);
        return ResponseEntity.status(HttpStatus.OK).body(percentages);
    }

}
