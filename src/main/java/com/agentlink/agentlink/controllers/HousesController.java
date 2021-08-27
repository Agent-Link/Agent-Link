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

    @GetMapping("/houses")
    public String getAllHouses(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        List<House> houses = housesDao.findAll();
        model.addAttribute("houses", houses);
        model.addAttribute("MAPBOX_ACCESS_TOKEN", MAPBOX_ACCESS_TOKEN);
        return "houses/index";
    }

    @GetMapping("/houses/{id}")
    public String singleHouse(@PathVariable long id, Model model){
        boolean isHouseOwner = false;
        House house = housesDao.getById(id);
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
            isHouseOwner = currentUser.getId() == house.getUser().getId();
        }
        model.addAttribute("house", house);
        model.addAttribute("isHouseOwner", isHouseOwner);
        model.addAttribute("MAPBOX_ACCESS_TOKEN",MAPBOX_ACCESS_TOKEN);
        return "houses/show";
    }

    @GetMapping("/houses/create")
    public String createHouseForm(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        // Verifies the current user is a listing agent
        if (usersDao.getById(currentUser.getId()).isListingAgent()) {
            model.addAttribute("house", new House());
            model.addAttribute("states", states);
            model.addAttribute("FILESTACK_TOKEN",FILESTACK_TOKEN);
            return "houses/create";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("/houses/create")
    public String createHouse(@Valid @ModelAttribute House house, BindingResult result, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        house.setUser(currentUser);
        model.addAttribute("states", states);
        if (result.hasErrors()) {
            return "houses/create";
        }
        // Verifies the current user is a listing agent
        if (usersDao.getById(currentUser.getId()).isListingAgent()) {
            housesDao.save(house);
//            emailSvc.prepareAndSend(post.getUser().getEmail(), "title", "body");
        }
        return "redirect:/profile";
    }

    @GetMapping("/houses/edit/{id}")
    public String editHousesForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        House house = housesDao.getById((id));
        model.addAttribute("states", states);
        // Verifies the current user is the house creator
        if (currentUser.getId() == house.getUser().getId()) {
            model.addAttribute("house", house);
            model.addAttribute("FILESTACK_TOKEN",FILESTACK_TOKEN);
            return "houses/edit";
        } else {
            return "redirect:/profile";
        }
    }

    @PostMapping("/houses/edit/{id}")
    public String saveEditedHouse(@PathVariable Long id, @Valid @ModelAttribute House house, BindingResult result, Model model){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
            model.addAttribute("FILESTACK_TOKEN",FILESTACK_TOKEN);
        }
        House houseFromDb = housesDao.getById(id);
        model.addAttribute("states", states);
        if (result.hasErrors()) {
            return "houses/edit";
        }
        // Verifies the current user is the house creator
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            house.setUser(currentUser);
            housesDao.save(house);
        }
        return "redirect:/houses/" + id;
    }

    @PostMapping("/houses/deactivate/{id}")
    public String deactivateHouse(@PathVariable Long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        House houseFromDb = housesDao.getById(id);
        // Verifies the current user is the house creator
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            houseFromDb.setListingActive(false);
            housesDao.save(houseFromDb);
        }
        return "redirect:/profile";
    }

    // Do we need to remove this entirely? We are not allowing house deletion as it will cause a cascade of data loss if connected to events/reviews etc..
//    @PostMapping("/houses/delete/{id}")
//    public String deleteHouse(@PathVariable Long id, Model model) {
//        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
//            User user = usersDao.getById(currentUser.getId());
//            model.addAttribute("user", user);
//        }
//        House houseFromDb = housesDao.getById(id);
//        // Verifies the current user is the house creator
//        if (currentUser.getId() == houseFromDb.getUser().getId()) {
//            housesDao.deleteById(id);
//        }
//        return "redirect:/houses";
//    }

    @PostMapping("/houses/delete/{id}/image")
    public String deleteHouseImage(@PathVariable Long id, Model model){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        House houseFromDb = housesDao.getById(id);
        model.addAttribute("house", houseFromDb);
        model.addAttribute("states", states);
        if (currentUser.getId() == houseFromDb.getUser().getId()) {
            houseFromDb.setImage_url(null);
        }
        return "houses/edit";
    }
}
