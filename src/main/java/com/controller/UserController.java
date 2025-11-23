package com.controller;

import com.entity.User;
import com.service.impl.UserServiceImpl;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @GetMapping("/me")
    public ResponseEntity<User> getMyInfo(Authentication auth) {
        String login = auth.getName();
        User user = userServiceImpl.getByUsername(login);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/me/cards")
    public ResponseEntity<String> getMyCards(Authentication auth) {
        return ResponseEntity.ok("Cards for " + auth.getName());
    }

    @GetMapping("/me/orders")
    public ResponseEntity<String> getMyOrders(Authentication auth) {
        return ResponseEntity.ok("Orders for " + auth.getName());
    }



}
