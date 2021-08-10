package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Controller
public class UserController {
    private UserRepository users;
    private PasswordEncoder passwordEncoder;

    public UserController(UserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam(defaultValue = "false") boolean isListingAgent){
        String hash = passwordEncoder.encode(user.getPassword());
        if (users.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "user.email", "This email already exists");
        }
        if (users.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "user.username", "This username already exists");
        }
        if (result.hasErrors()) {
            return "users/sign-up";
        }
        if(isListingAgent) {
            user.setListingAgent(true);
        }
        user.setPassword(hash);
        users.save(user);
        return "redirect:/login";
    }
}