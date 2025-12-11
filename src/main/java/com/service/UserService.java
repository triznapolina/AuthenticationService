package com.service;

import com.dto.AuthRequest;
import com.dto.AuthResponse;
import com.dto.RegistRequest;
import com.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {


    User createUser(User user);

    User getByEmail(String email);

    UserDetailsService userDetailsService();
}
