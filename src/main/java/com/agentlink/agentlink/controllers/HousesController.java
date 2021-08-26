package com.agentlink.agentlink.controllers;


import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.dom4j.rule.Mode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class HousesController {

    private final HouseRepository housesDao;
    private final UserRepository usersDao;
    private final String[] states = {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin", "Wyoming"};

    //access (took out final)
    @Value("${MAPBOX_ACCESS_TOKEN}")
    private String MAPBOX_ACCESS_TOKEN;

    @Value("${FILESTACK_TOKEN}")
    private String FILESTACK_TOKEN;

    public HousesController(HouseRepository housesDao, UserRepository usersDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
    }

@RequestMapping(path = "/houses", method = RequestMethod.GET)
    public String getAllHouses(Model model) {
        List<House> houses = housesDao.findAll();
        model.addAttribute("houses", houses);
        model.addAttribute("MAPBOX_ACCESS_TOKEN", MAPBOX_ACCESS_TOKEN);
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
        model.addAttribute("MAPBOX_ACCESS_TOKEN",MAPBOX_ACCESS_TOKEN);
        return "houses/show";
    }

    @GetMapping("/houses/create")
    public String createHouseForm(Model model) {
        model.addAttribute("house", new House());
        model.addAttribute("states", states);
        model.addAttribute("FILESTACK_TOKEN",FILESTACK_TOKEN);
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
        return "redirect:/profile";
    }

    @GetMapping("/houses/edit/{id}")
    public String editHousesForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        House house = housesDao.getById((id));
        model.addAttribute("states", states);
        if (currentUser.getId() == house.getUser().getId()) {
            model.addAttribute("house", house);
            model.addAttribute("FILESTACK_TOKEN",FILESTACK_TOKEN);
            return "/houses/edit";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("/houses/edit/{id}")
    public String saveEditedHouse(@PathVariable Long id, @Valid @ModelAttribute House house, BindingResult result, Model model){
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

    @PostMapping("/houses/deactivate/{id}")
    public String deactivateHouse(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        House houseFromDb = housesDao.getById(id);
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            houseFromDb.setListingActive(false);
            housesDao.save(houseFromDb);
        }
        return "redirect:/profile";
    }


    @PostMapping("/houses/delete/{id}")
    public String deleteHouse(@PathVariable Long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        House houseFromDb = housesDao.getById(id);
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            housesDao.deleteById(id);
        }
        return "redirect:/houses";
    }

    @PostMapping("/houses/delete/{id}/image")
    public String deleteHouseImage(@PathVariable Long id, Model model){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        House houseFromDb = housesDao.getById(id);
        model.addAttribute("house", houseFromDb);
        model.addAttribute("states", states);
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            houseFromDb.setImage_url(null);
        }
        return "/houses/edit";
    }




}
