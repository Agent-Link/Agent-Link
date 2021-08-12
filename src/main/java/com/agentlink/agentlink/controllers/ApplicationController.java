package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.Application;
import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class ApplicationController {
    private final HouseRepository housesDao;
    private final UserRepository usersDao;
    private final OpenHouseEventRepository eventsDao;

    public ApplicationController(HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository eventsDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.eventsDao = eventsDao;
    }

    @GetMapping("/events/apply/{id}")
    public String applicationForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("application", new Application());
        return "applications/create";
    }

    @PostMapping("/events/apply/{id}")
    public String submitApplication(@PathVariable long id, @Valid @ModelAttribute Application application, BindingResult result){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (result.hasErrors()) {
            return "applications/create";
        }

        // will redirect to applicants profile or an applied to page
        return "redirect:/";
    }
}
