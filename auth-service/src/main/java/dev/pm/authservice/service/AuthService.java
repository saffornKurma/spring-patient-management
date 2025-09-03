package dev.pm.authservice.service;


import dev.pm.authservice.dto.LoginRequestDTO;
import dev.pm.authservice.model.User;
import dev.pm.authservice.util.JwtUtil;
import io.jsonwebtoken.JwtException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.jar.JarException;

@Service
public class AuthService {

    private final UserService userService;
    PasswordEncoder passwordEncoder;
    JwtUtil jwtUtil;

    public AuthService(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public Optional<String> authenticate(LoginRequestDTO loginRequestDTO) {

        Optional<String> token = userService.findByEmail(loginRequestDTO.getEmail())
                .filter(u->passwordEncoder.matches(loginRequestDTO.getPassword(),u.getPassword()))
                .map(u->jwtUtil.generateToken(u.getEmail(),u.getRole()));

        return token;
    }

    public boolean validateToken(String token) {
        try{
            jwtUtil.validateToken(token);
            return true;
        }catch (JwtException e)
        {
            return false;
        }
    }
}
