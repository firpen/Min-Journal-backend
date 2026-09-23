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
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    private final SecurityContextLogoutHandler securityContextLogoutHandler;

    public AuthController(AuthService authService, SecurityContextRepository securityContextRepository,
            SecurityContextLogoutHandler securityContextLogoutHandler) {
        this.authService = authService;
        this.securityContextRepository = securityContextRepository;
        this.securityContextLogoutHandler = securityContextLogoutHandler;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterDto register) {
        authService.register(register.getUsername(), register.getPassword());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginDto login, HttpServletRequest request,
            HttpServletResponse response) {
        User user = authService.login(login.getUsername(), login.getPassword());
        // Skapar ett Authentication-objekt i minnet som innehåller username, null och
        // en tom lista.
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null, List.of());
        // Skapar en tom behållare av typen SecurityContext
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        // Lägger Authentication-objektet i SecurityContext-behållaren
        context.setAuthentication(authentication);
        /*
         * Skapar en session på servern om det inte redan fanns en och lägger
         * SecurityContext-behållaren
         * i sessionen på servern, slumpar ett ID och lägger Set-Cookie: JSESSIONID=...
         * i responsen.
         */
        securityContextRepository.saveContext(context, request, response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        this.securityContextLogoutHandler.logout(request, response, authentication);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
