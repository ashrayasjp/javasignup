package com.example.signup.controller;

import com.example.signup.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    // Simple in-memory user storage (for demo only)
    private List<User> users = new ArrayList<>();

    // Show signup form
    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup";  // src/main/resources/templates/signup.html
    }

    // Process signup form submission
    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user) {
        users.add(user);  // save user
        return "redirect:/users/login";  // redirect to login after signup
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // src/main/resources/templates/login.html
    }

    // Process login form submission
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        // Check if user exists and password matches
        User validUser = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (validUser != null) {
            // Store user in session
            session.setAttribute("loggedInUser", validUser);

            // Add all users to model for dashboard view
            model.addAttribute("users", users);
            return "users";  // dashboard page
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Handle logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    // Show dashboard (only if logged in)
    @GetMapping("/dash")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            // Not logged in, redirect to login page
            return "redirect:/users/login";
        }

        model.addAttribute("users", users);
        return "users";  // show dashboard
    }
}
