package com.agentlink.agentlink.controllers;


import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class HousesController {

    private final HouseRepository housesDao;
    private final UserRepository usersDao;
    private final String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};

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
        model.addAttribute("states", states);
        return "/houses/create";
    }

    @PostMapping("/houses/create")
    public String createHouse(@Valid @ModelAttribute House house, BindingResult result, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        house.setUser(currentUser);
        model.addAttribute("states", states);
        if (result.hasErrors()) {
            return "houses/create";
        }
//        emailSvc.prepareAndSend(post.getUser().getEmail(), "title", "body");
        housesDao.save(house);
        return "redirect:/houses";
    }

    @GetMapping("/houses/edit/{id}")
    public String editHousesForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        House house = housesDao.getById((id));
        model.addAttribute("states", states);
        if (currentUser.getId() == house.getUser().getId()) {
            model.addAttribute("house", house);
            return "/houses/edit";
        } else {
            return "redirect:/houses";
        }
    }

    @PostMapping("/houses/edit/{id}")
    public String saveEditedHouse(@PathVariable long id, @Valid @ModelAttribute House house, BindingResult result, Model model){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        House houseFromDb = housesDao.getById(id);
        model.addAttribute("states", states);
        if (result.hasErrors()) {
            return "houses/edit";
        }
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            house.setUser(currentUser);
            housesDao.save(house);
        }
        return "redirect:/houses/" + id;
    }

    @GetMapping("/houses/delete/{id}")
    public String deleteHouse(@PathVariable("id") long id, Model model) {
        House house = housesDao.getById(id);
        housesDao.delete(house);
        return "redirect:/houses";
    }





}
