package com.agentlink.agentlink.controllers;


import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HousesController {

    private final HouseRepository housesDao;
    private final UserRepository usersDao;

    public HousesController(HouseRepository housesDao, UserRepository usersDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
    }

@RequestMapping(path = "/houses", method = RequestMethod.GET)
    public String getAllHouses(Model model) {
        List<House> houses = housesDao.findAll();
        model.addAttribute("houses", houses);
        return "houses/index";
    }

    @GetMapping("/houses/{id}")
    public String singleHouse(@PathVariable long id, Model model){
        House house = housesDao.getById(id);
        boolean isHouseOwner = false;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isHouseOwner = currentUser.getId() == house.getUser().getId();
        }
        model.addAttribute("house", house);
        model.addAttribute("isHouseOwner", isHouseOwner);
        return "houses/show";
    }

    @GetMapping("/houses/create")
    public String createHouseForm(Model model) {
        model.addAttribute("house", new House());
        return "/houses/create";
    }

    @PostMapping("/houses/create")
    public String createHouse(@ModelAttribute House house) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        house.setUser(currentUser);
//        emailSvc.prepareAndSend(post.getUser().getEmail(), "title", "body");
        housesDao.save(house);
        return "redirect:/houses";
    }

    @GetMapping("/houses/edit/{id}")
    public String updateHousesForm(@PathVariable("id") long id, Model model) {
        House house = housesDao.getById((id));
        model.addAttribute("houses", house);
        return "/houses/edit";
    }




}
