package com.controller;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.dto.TokenRequest;
import com.dto.RegistRequest;
import com.entity.User;
import com.service.UserService;
import com.service.JwtService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    private final Map<String, String> refreshStorage = new HashMap<>();

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();


    @PostMapping("/register")
    public ResponseEntity<?> postUser(@RequestBody RegistRequest request){

        String hashedPassword = encoder.encode(request.getPassword());
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(hashedPassword);
        user.setRole(User.Role.USER);
        userService.createUser(user);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest userS) {
        User user = (User) userService.userDetailsService().loadUserByUsername(userS.getEmail());
        System.out.println(user);
        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        refreshStorage.put(user.getEmail(), refreshToken);
        return new ResponseEntity<>(new AuthResponse(accessToken, refreshToken), HttpStatus.OK);
    }


    @PostMapping("/token")
    public ResponseEntity<AuthResponse> getAccessToken(@RequestBody TokenRequest request) {

        String refreshToken = request.getRefreshToken();
        if (!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null));
        }

        final Claims claims = jwtService.getRefreshClaims(refreshToken);
        final String login = claims.getSubject();

        final User user = userService.getByEmail(login);
        final String newAccessToken = jwtService.generateAccessToken(user);
        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody TokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtService.validateRefreshToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null));
        }

        final Claims claims = jwtService.getRefreshClaims(refreshToken);

        final String login = claims.getSubject();
        final String savedRefreshToken = refreshStorage.get(login);

        if (savedRefreshToken == null || !savedRefreshToken.equals(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, null));
        }

        final User user = userService.getByEmail(login);
        final String accessToken = jwtService.generateAccessToken(user);
        final String newRefreshToken = jwtService.generateRefreshToken(user);

        refreshStorage.put(user.getUsername(), newRefreshToken);
        return ResponseEntity.ok(new AuthResponse(accessToken, newRefreshToken));
    }


    @PostMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestBody TokenRequest request) {
        boolean isValid = jwtService.validateToken(request.getRefreshToken());
        if (isValid) {
            return ResponseEntity.ok("Token is valid.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is invalid or expired.");
        }
    }


}
