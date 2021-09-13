package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    private final OpenHouseEventRepository eventsDao;
    private final UserRepository userDao;
    private final HouseRepository houseDao;

    public SearchController(OpenHouseEventRepository eventsDao, UserRepository userDao, HouseRepository houseDao){
        this.eventsDao = eventsDao;
        this.userDao = userDao;
        this.houseDao = houseDao;
    }

    @GetMapping("/search")
    public String searchOpenHouses(@RequestParam String search, Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = userDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        model.addAttribute("openHouseEventsSearch", eventsDao.findAllQuery(search));
        model.addAttribute("usersSearch", userDao.findAllByFirstNameLike(search));
        model.addAttribute("houseSearch", houseDao.findAllHouseQuery(search));
        return "results";
    }
}


