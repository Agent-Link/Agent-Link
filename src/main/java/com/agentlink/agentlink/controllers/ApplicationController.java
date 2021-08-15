package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.Application;
import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.ApplicationRepository;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class ApplicationController {
    private final HouseRepository housesDao;
    private final UserRepository usersDao;
    private final OpenHouseEventRepository eventsDao;
    private final ApplicationRepository applicationDao;

    public ApplicationController(ApplicationRepository applicationDao, HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository eventsDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.eventsDao = eventsDao;
        this.applicationDao = applicationDao;
    }

    @GetMapping("/events/apply/{id}")
    public String applicationForm(@PathVariable Long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("application", new Application());
        return "applications/create";
    }

    @PostMapping("/events/apply/{id}")
    public String submitApplication(@PathVariable Long id, @Valid @ModelAttribute Application application, BindingResult result, Model model){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        application.setUser(currentUser);
        application.setOpenHouseEvent(eventsDao.getById(id));
        application.setDate(new Date());
        if (result.hasErrors()) {
            return "applications/create";
        }
        // Applying currently overwrites last the applicants application so we only have one applicant per event with this bug
        applicationDao.save(application);
        // will redirect to applicants profile or an applied to page
        return "redirect:/";
    }
}
