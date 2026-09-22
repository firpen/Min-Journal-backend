package com.minjournal.min_journal_backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minjournal.min_journal_backend.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    
}
