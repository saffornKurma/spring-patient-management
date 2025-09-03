package dev.pm.authservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotBlank(message="Email is mandatory")
    @Email(message="Email should be a valid format")
    private String email;

    @NotBlank(message="password is required")
    @Size(min=8,message="Password must be atleasst 8 chars long")
    private String password;
}
