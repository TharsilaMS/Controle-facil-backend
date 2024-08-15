package com.controlefacil.controlefacil.controllers;
import com.controlefacil.controlefacil.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password) {
        try {
            return authService.authenticate(email, password);
        } catch (Exception e) {
            return "Invalid credentials";
        }
    }
}
