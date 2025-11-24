package com.controller;

import com.dto.AuthResponse;
import com.dto.AuthRequest;
import com.dto.RegistRequest;
import com.dto.UserInfoResponse;
import com.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegistRequest request) {
        authenticationService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid AuthRequest request) {
        AuthResponse tokenAnswer = authenticationService.login(request);
        return ResponseEntity.ok(tokenAnswer);
    }



    @PostMapping("refresh")
    public ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request,
                                                     HttpServletResponse response) {
        return authenticationService.refreshToken(request, response);
    }


    @GetMapping("/validate-access-token")
    public ResponseEntity<Boolean> validateToken(@RequestParam String token) {
        boolean isValid = authenticationService.isTokenValid(token);
        return ResponseEntity.ok(isValid);
    }



    @GetMapping("/user-info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@RequestParam String token) {
        UserInfoResponse userInfo = authenticationService.getUserInfo(token);
        return ResponseEntity.ok(userInfo);
    }







}
