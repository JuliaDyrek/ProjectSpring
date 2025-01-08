package com.projektRestauracja.Projekt.controller;

import com.projektRestauracja.Projekt.model.User;
import com.projektRestauracja.Projekt.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/api/reservations";
        }
        return "security/login";
    }

    @PostMapping("/login")
    public String loginPost(@RequestParam String username, 
                        @RequestParam String password, 
                        Model model, 
                        HttpSession session) {
        User user = userRepository.findByUsername(username);

        if (user != null && password.equals(user.getPassword())) {
            session.setAttribute("loggedUser", user.getUsername());
            return "redirect:/api/reservations";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "security/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String register(HttpSession session) {
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/api/reservations";
        }
        return "security/register";
    }

    @PostMapping("/register")
    public String registerPost(@RequestParam String username, 
                           @RequestParam String password, 
                           @RequestParam String confirmPassword, 
                           Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "security/register";
        }

        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists");
            return "security/register";
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        userRepository.save(user);
    
        return "redirect:/login";
    }
}
