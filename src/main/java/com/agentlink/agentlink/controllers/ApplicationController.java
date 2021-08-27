package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.Application;
import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
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
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@Controller
public class ApplicationController {
    private final HouseRepository housesDao;
    private final UserRepository usersDao;
    private final OpenHouseEventRepository eventsDao;
    private final ApplicationRepository applicationsDao;

    public ApplicationController(ApplicationRepository applicationsDao, HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository eventsDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.eventsDao = eventsDao;
        this.applicationsDao = applicationsDao;
    }

    @GetMapping("/events/apply/{openHouseId}")
    public String applicationForm(@PathVariable Long openHouseId, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        model.addAttribute("openHouseEvent", eventsDao.getById(openHouseId));
        model.addAttribute("app", new Application());
        return "applications/create";
    }

    @PostMapping("/events/apply/{openHouseId}")
    public String submitApplication(@PathVariable Long openHouseId, @Valid @ModelAttribute Application app, BindingResult result, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        // had to write custom errors because error results is not passing to html correctly.
        if (result.hasErrors()) {
            if (app.getInquiry().trim().isEmpty()) {
                boolean isBlank = true;
                model.addAttribute("isBlank", isBlank);
            }
            if (app.getInquiry().length() > 500) {
                boolean isOver500 = true;
                model.addAttribute("isOver500", isOver500);
            }
            model.addAttribute("openHouseEvent", eventsDao.getById(openHouseId));
            model.addAttribute("app", app);
            return "applications/create";
        }
        OpenHouseEvent openHouseEvent = eventsDao.getById(openHouseId);
        app.setUser(currentUser);
        app.setOpenHouseEvent(openHouseEvent);
        app.setDate(new Date());

        List<Application> appsForEvent = applicationsDao.findApplicationsByOpenHouseEventId(openHouseId);
        boolean hasNotApplied = true;
        for (Application application : appsForEvent) {
            if (application.getUser().getId() == currentUser.getId()) {
                hasNotApplied = false;
                break;
            }
        }
        // Verifies that the person trying to apply is actually an user that has not applied and is also not the event creator. And that the event has not started yet.
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser" && hasNotApplied && currentUser.getId() != openHouseEvent.getUser().getId() && openHouseEvent.getDateStart().after(new Date())) {
            applicationsDao.save(app);
        }
        // will redirect to applicants profile or an applied to page
        return "redirect:/profile";
    }

    @PostMapping("/events/apply/host/{openHouseId}/{userId}")
    public String setApplicantToEventHost(@PathVariable Long openHouseId, @PathVariable Long userId) {
        User applicant = usersDao.getById(userId);
        OpenHouseEvent openHouseEvent = eventsDao.getById(openHouseId);
        openHouseEvent.setUser(applicant);
        eventsDao.save(openHouseEvent);
        return "redirect:/profile";
    }
}
