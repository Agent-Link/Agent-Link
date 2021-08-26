package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {
    private final UserRepository usersDao;

    public HomeController(UserRepository usersDao) {
        this.usersDao = usersDao;
    }

    @GetMapping("/")
    public String home(Model model){
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        return "home";
    }

    @GetMapping("/AboutUs")
    public String AboutUs(Model model){
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        return "aboutUs";
    }
}