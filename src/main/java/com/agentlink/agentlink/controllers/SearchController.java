package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private final OpenHouseEventRepository openHouseDao;

    public SearchController(OpenHouseEventRepository openHouseDao){
        this.openHouseDao = openHouseDao;
    }

    @GetMapping("/search")
    public String searchOpenHouses(@RequestParam String search, Model model) {
        model.addAttribute("openHouseEvents", openHouseDao.findAllQuery(search));
        return "/results";
    }
}


