package com.service.impl;


import com.entity.User;
import com.exception.AlreadyExistsException;
import com.repository.UserRepository;
import com.service.UserService;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new AlreadyExistsException("User with such 'username' is already exists");
        }

        if (repository.existsByEmail(user.getEmail())) {
            throw new AlreadyExistsException("User with such 'email' is already exists");
        }

        return repository.save(user);
    }


    @Override
    public User getByUsername(String username) {
        return repository.findByUsername(username);

    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }


}
