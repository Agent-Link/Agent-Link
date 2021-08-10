package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private HouseRepository housesDao;

    public UserController(UserRepository users, PasswordEncoder passwordEncoder, HouseRepository housesDao) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.housesDao = housesDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model){
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam(defaultValue = "false") boolean isListingAgent){
        String hash = passwordEncoder.encode(user.getPassword());
        if (result.hasErrors()) {
            return "users/sign-up";
        }
        if(isListingAgent) {
            user.setListingAgent(true);
        }
        user.setPassword(hash);
        users.save(user);
        // working on validation
//        if (!users.existsByEmail(user.getEmail()) && Verify.userNameNotExist(users, user.getUsername())) {
//            user.setPassword(hash);
//            users.save(user);
//            return "redirect:/login";
//        } else {
//            return "users/sign-up";
//        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("houses",housesDao.findAllByUser(user));
        return "users/profile";
    }

}