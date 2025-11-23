package com.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/cards")
    public ResponseEntity<String> getAllCards(Authentication auth) {
        return ResponseEntity.ok("All cards for: " + auth.getName());
    }

    @GetMapping("/orders")
    public ResponseEntity<String> getAllOrders(Authentication auth) {
        return ResponseEntity.ok("All orders for: " + auth.getName());
    }

}
