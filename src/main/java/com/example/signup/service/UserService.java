package com.example.signup.service;

import com.example.signup.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> users = new ArrayList<>();

    public void saveUser(User user) {
        users.add(user);
    }

    public List<User> getAllUsers() {
        return users;
    }
}
