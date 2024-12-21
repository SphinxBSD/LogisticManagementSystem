package com.ltms.backend.auth;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/registrar")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registrar(
            @RequestBody @Valid RegistrationRequest request
    ) throws MessagingException {
        service.registrar(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/autenticar")
    public ResponseEntity<AuthenticationResponse> autenticar(
            @RequestBody @Valid AuthenticationRequest request
    ){
        return ResponseEntity.ok(service.autenticar(request));
    }

}
