package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {
    private final OpenHouseEventRepository openHouseDao;
    private final UserRepository userDao;

    public SearchController(OpenHouseEventRepository openHouseDao, UserRepository userDao){
        this.openHouseDao = openHouseDao;
        this.userDao = userDao;
    }

    @GetMapping("/search")
    public String searchOpenHouses(@RequestParam String search, Model model) {
        model.addAttribute("openHouseEventsSearch", openHouseDao.findAllQuery(search));
        model.addAttribute("usersSearch", userDao.findAllByFirstNameLike(search));
        return "/results";
    }
}


