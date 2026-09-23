package com.minjournal.min_journal_backend.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.minjournal.min_journal_backend.dtos.NoteResponseDto;
import com.minjournal.min_journal_backend.exceptions.UserNoteDoesntExistException;
import com.minjournal.min_journal_backend.models.Note;
import com.minjournal.min_journal_backend.models.Status;
import com.minjournal.min_journal_backend.models.User;
import com.minjournal.min_journal_backend.repositories.NoteRepository;
import com.minjournal.min_journal_backend.repositories.UserRepository;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteService(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    public void createNote(String noteText, Status status, String username) {
        User user = userRepository.findByUsername(username).orElseThrow();
        Note newNote = new Note(noteText, status, LocalDateTime.now(), user);
        noteRepository.save(newNote);
    }

    public void deleteNote(int id, String username) {
        Optional<Note> noteToDelete = noteRepository.findByIdAndUserUsername(id, username);
        if (noteToDelete.isEmpty()) {
            throw new UserNoteDoesntExistException("That note doesnt exist.");
        }
        noteRepository.delete(noteToDelete.get());
    }

    public void updateNote(String noteText, Status status, int id, String username) {
        Optional<Note> noteToUpdate = noteRepository.findByIdAndUserUsername(id, username);
        if (noteToUpdate.isEmpty()) {
            throw new UserNoteDoesntExistException("That note doesnt exist");
        }
        Note note = noteToUpdate.get();
        note.setNote(noteText);
        note.setStatus(status);
        noteRepository.save(note);
    }

    public List<NoteResponseDto> getNotes(String username) {
        List<Note> notes = noteRepository.findByUserUsername(username);
        List<NoteResponseDto> noteResponseDtos = new ArrayList<>();
        for (Note note : notes) {
            NoteResponseDto noteDto = new NoteResponseDto(note.getNote(), note.getStatus(), note.getDate(), note.getId());
            noteResponseDtos.add(noteDto);
        }   
        return noteResponseDtos;
    }

    public List<NoteResponseDto> getNotesBetweenDates(String username, LocalDateTime start, LocalDateTime end) {
        List<Note> notes = noteRepository.findByUserUsernameAndDateBetween(username, start, end);
        List<NoteResponseDto> noteResponseDtos = new ArrayList<>();
        for (Note note: notes) {
            NoteResponseDto noteDto = new NoteResponseDto(note.getNote(), note.getStatus(), note.getDate(), note.getId());
            noteResponseDtos.add(noteDto);
        }
        return noteResponseDtos;
    }

    public Map<Status, Double> getStatistics(String username, LocalDateTime start, LocalDateTime end) {
        List<Note> notes = noteRepository.findByUserUsernameAndDateBetween(username, start, end);
        Map<Status, Integer> counts = new HashMap<>();
        // counts.get() används för att hämta ett värde med hjälp av nyckeln
        for (Note note : notes) {
            counts.put(note.getStatus(), counts.getOrDefault(note.getStatus(), 0) + 1 );
        }
        Map<Status, Double> percentages = new HashMap<>();
        for (Status s : Status.values()) {
            double percent = notes.isEmpty() ? 0 : (counts.getOrDefault(s, 0) * 100.0) / notes.size();
            percentages.put(s, percent);
        }
        return percentages;
    }

}
