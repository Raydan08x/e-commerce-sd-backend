package com.sierra_dorada.controller;
import com.sierra_dorada.dto.*;
import com.sierra_dorada.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }
    @PostMapping({"/api/auth/login", "/login"})
    public AuthResponse login(@Valid @RequestBody LoginRequest request) { return service.login(request); }
    @PostMapping({"/api/auth/registro", "/registro"})
    public ResponseEntity<AuthResponse> registro(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registrar(request));
    }
}
