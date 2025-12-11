package com.service.impl;


import com.entity.User;
import com.repository.UserRepository;
import com.service.UserService;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User createUser(User user) {
        return repository.save(user);
    }


    @Override
    public User getByEmail(String email) {
        return repository.findByEmail(email);

    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByEmail;
    }

}
