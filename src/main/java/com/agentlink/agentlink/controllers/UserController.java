package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.Review;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.ReviewRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Controller
public class UserController {
    private UserRepository usersDao;
    private PasswordEncoder passwordEncoder;
    private HouseRepository housesDao;
    private OpenHouseEventRepository eventsDao;
    private ReviewRepository reviewsDao;

    public UserController(UserRepository usersDao, PasswordEncoder passwordEncoder, HouseRepository housesDao, OpenHouseEventRepository eventsDao, ReviewRepository reviewsDao) {
        this.usersDao = usersDao;
        this.passwordEncoder = passwordEncoder;
        this.housesDao = housesDao;
        this.eventsDao = eventsDao;
        this.reviewsDao = reviewsDao;
    }

    @GetMapping("/sign-up")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "users/sign-up";
    }

    @PostMapping("/sign-up")
    public String saveUser(@Valid @ModelAttribute User user, BindingResult result, @RequestParam(defaultValue = "false") boolean isListingAgent) {
        String hash = passwordEncoder.encode(user.getPassword());
        if (usersDao.existsByEmail(user.getEmail())) {
            result.rejectValue("email", "user.email", "This email already exists");
        }
        if (usersDao.existsByUsername(user.getUsername())) {
            result.rejectValue("username", "user.username", "This username already exists");
        }
        if (result.hasErrors()) {
            return "users/sign-up";
        }
        if (isListingAgent) {
            user.setListingAgent(true);
        }
        user.setPassword(hash);
        usersDao.save(user);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String userProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("houses", housesDao.findAllByUser(user)); //This produces all user houses on their profile
        model.addAttribute("openHouseEvents", eventsDao.findAll()); //This code produces all user events on their profile
        model.addAttribute("userId", user.getId());
        model.addAttribute("currentDateTime", new Date());
        return "users/profile";
    }

    @GetMapping("/profile/{id}")
    public String userProfileInfo(@PathVariable long id, Model model){
        User user = usersDao.getById(id);
        List<Review> reviewRatings = reviewsDao.findAllByUser(user);
        model.addAttribute("user", user);
        return "users/agentInfo";
    }

    //This code allows users to edit their profile.
    @GetMapping("/profile/edit")
    public String editUserProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", usersDao.getById(user.getId()));
        return "users/editProfile";
    }

    // Need to add validation to user edit
    @PostMapping("/profile/edit")
    public String updateUser(@ModelAttribute("user") User user, Model model,  @RequestParam(defaultValue = "false") boolean isListingAgent) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User updatedUser = usersDao.getById(currentUser.getId());

        updatedUser.setEmail(user.getEmail());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setPhone(user.getPhone());
        updatedUser.setTeam(user.getTeam());
        if (isListingAgent) {
            updatedUser.setListingAgent(true);
        }
        usersDao.save(updatedUser);
        return "redirect:/profile";
    }

    //EDIT PASSWORD
    @GetMapping("/users/editPassword")
    public String showEditPassword(){
        return "users/editPassword";
    }

    @PostMapping("/users/editPassword")
    public String editPassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String confirm){
        if(!confirm.equals(newPassword)){
            return "users/editPassword";
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = usersDao.getById(currentUser.getId());

        //FOLLOWING 2 LINES DID NOT WORK
        String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(10)); //Generates a hash from plaintext password
        //String hashedPassword = passwordEncoder.encode(newPassword);

//        This checks that given plaintext password matches a known hash
        if(BCrypt.checkpw(oldPassword, user.getPassword())){
            user.setPassword(hashedPassword);
            usersDao.save(user);
        }
        return "users/passwordChangeSuccess";
    }
}
