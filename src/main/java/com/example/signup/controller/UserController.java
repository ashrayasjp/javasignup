package com.example.signup.controller;

import com.example.signup.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        return "signup"; // signup.html
    }

    // Process signup form with password validation
    @PostMapping("/signup")
    public String processSignup(@ModelAttribute User user, Model model) {
        String password = user.getPassword();

        // Validate password
        if (!isValidPassword(password)) {
            model.addAttribute("error", "Password must contain at least one uppercase letter and one special character.");
            return "signup";
        }

        // Check for duplicate username
        boolean userExists = users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()));
        if (userExists) {
            model.addAttribute("error", "Username already taken.");
            return "signup";
        }

        users.add(user);  // Save user
        return "redirect:/users/login";
    }

    // Helper method to validate password
    private boolean isValidPassword(String password) {
        boolean hasUppercase = password.matches(".*[A-Z].*");
        boolean hasSpecial = password.matches(".*[!@#$%^&*()_+\\-={}:;\"'<>?,./\\\\].*");
        return hasUppercase && hasSpecial;
    }

    // Show login form
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // login.html
    }

    // Process login
    @PostMapping("/login")
    public String processLogin(@RequestParam String username,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {

        User validUser = users.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);

        if (validUser != null) {
            session.setAttribute("loggedInUser", validUser);
            model.addAttribute("users", users);
            return "users"; // dashboard
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    // Dashboard (GET)
    @GetMapping("/dash")
    public String showDashboard(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/users/login";
        }

        model.addAttribute("users", users);
        return "users"; // dashboard view
    }
}
