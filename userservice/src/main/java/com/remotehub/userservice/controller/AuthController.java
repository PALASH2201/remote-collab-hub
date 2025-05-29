package com.remotehub.userservice.controller;

import com.remotehub.userservice.dto.request.LoginRequest;
import com.remotehub.userservice.dto.request.RegisterRequest;
import com.remotehub.userservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerNewUser(@RequestBody RegisterRequest request){
        authService.addNewUser(request);
        return ResponseEntity.status(201).body("Success");
    }

    @PostMapping("/login")
    public ResponseEntity<String> signIn(@RequestBody LoginRequest loginRequest){
        String token = authService.getLoginToken(loginRequest);
        return ResponseEntity.status(200).body(token);
    }
}
