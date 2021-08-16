package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {
    private UserRepository users;
    private PasswordEncoder passwordEncoder;
    private HouseRepository housesDao;
    private OpenHouseEventRepository eventsDao;

    public UserController(UserRepository users, PasswordEncoder passwordEncoder, HouseRepository housesDao, OpenHouseEventRepository eventsDao) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.housesDao = housesDao;
        this.eventsDao = eventsDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam(defaultValue = "false") boolean isListingAgent) {
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
        if (isListingAgent) {
            user.setListingAgent(true);
        }
        user.setPassword(hash);
        users.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<OpenHouseEvent> openHouseEvents = eventsDao.findAllByUser(user);
        model.addAttribute("houses", housesDao.findAllByUser(user)); //This produces all user houses on their profile
        model.addAttribute("openHouseEvents", openHouseEvents); //This code produces all user events on their profile
        return "users/profile";
    }

    //This code allows users to edit their profile.
    @GetMapping("/profile/edit")
    public String editUserProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", users.getOne(user.getId()));
        return "users/editProfile";
    }

    @PostMapping("/profile/edit")
    public String updateUser(@ModelAttribute("user") User user, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updatedUser = users.getOne(currentUser.getId());

        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setTeam(user.getTeam());
        updatedUser.setListingAgent(user.isListingAgent());//Still need to work on this line. Not saving to database.
        users.save(updatedUser);
        return "redirect:/profile";
    }

    //EDIT PASSWORD
    @GetMapping("/users/editPassword/{id}")
    public String showEditPassword(@PathVariable long id){
        return "users/editPassword";
    }

    @PostMapping("/users/editPassword/{id}")
    public String editPassword(@ModelAttribute("user") @PathVariable long id, @RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirm){
//        if(!confirm.equals(newPassword)){
//            return "users/editPassword";
//        }
        User user = users.getById(id);
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt()); //Generates a hash from plaintext password

        //This checks that given plaintext password matches a known hash
        if(BCrypt.checkpw(oldPassword, user.getPassword())){
            user.setPassword(hashedPassword);
            users.save(user);
        }
        return "users/passwordChangeSuccess";
    }
}
