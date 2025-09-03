package dev.pm.authservice.controller;


import dev.pm.authservice.dto.LoginRequestDTO;
import dev.pm.authservice.dto.LoginResponseDTO;
import dev.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
//Operation used by Api docs for giving title verb
    @Operation(summary="Generate token on user login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO)
    {
        Optional<String> tokenOptional=authService.authenticate(loginRequestDTO);

        if(tokenOptional.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(new LoginResponseDTO(tokenOptional.get()));
    }


    @Operation(summary="validate token")
    @GetMapping("/validate")
    public ResponseEntity<Void> validateToken(@RequestHeader("Authorization") String authHeader)
    {
        if(authHeader ==null || !authHeader.startsWith("Bearer "))
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.validateToken(authHeader.substring(7))
                ?ResponseEntity.ok().build():ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
}
