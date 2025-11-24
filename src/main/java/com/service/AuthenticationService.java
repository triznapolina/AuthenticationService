package com.service;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.dto.RegistRequest;
import com.dto.UserInfoResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    void register(RegistRequest request);

    AuthResponse login(AuthRequest request);

    ResponseEntity<AuthResponse> refreshToken(HttpServletRequest request,
                                                     HttpServletResponse response);

    boolean isTokenValid(String token);



   UserInfoResponse getUserInfo(String token);


}
