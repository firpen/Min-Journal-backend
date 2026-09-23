package com.minjournal.min_journal_backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minjournal.min_journal_backend.dtos.LoginDto;
import com.minjournal.min_journal_backend.dtos.RegisterDto;
import com.minjournal.min_journal_backend.models.User;
import com.minjournal.min_journal_backend.services.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthService authService, SecurityContextRepository securityContextRepository) {
        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto registerDto) {
        authService.register(registerDto.getUsername(), registerDto.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto loginDto, HttpServletRequest request,
            HttpServletResponse response) {
        User user = authService.login(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, List.of());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        securityContextRepository.saveContext(context, request, response);
        SecurityContextHolder.setContext(context);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
