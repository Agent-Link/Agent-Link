package com.agentlink.agentlink.controllers;


import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.HouseImage;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.HouseImageRepository;
import com.agentlink.agentlink.repositories.HouseRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HouseImagesController {
    private HouseImageRepository houseImageDao;
    private HouseRepository houseDao;

    public HouseImagesController(HouseImageRepository houseImageDao, HouseRepository houseDao) {
        this.houseImageDao = houseImageDao;
        this.houseDao = houseDao;
    }


    @GetMapping("/houses/{id}/images")
    public String showHouseImages(Model model, @PathVariable long id) {
        House house = houseDao.getById(id);
        List<HouseImage> images = house.getImages();
        model.addAttribute("house", house);
        model.addAttribute("images", images);
        return "showHouseImages";
    }

    @GetMapping("/houses/{houseId}/images/{imageId}/delete")
    public String deleteHouseImage(@PathVariable Long houseId, @PathVariable Long imageId, Model model){
        HouseImage image = houseImageDao.getById(imageId);
        houseImageDao.delete(image);
        return"redirect:/houses/" + houseId + "/images";
    }

    @GetMapping("/houses/{id}/images/create")
    public String showCreateImageForm(Model model, @PathVariable long id){
        model.addAttribute("house", houseDao.getById(id));
        return "addHouseImage";
    }

    @PostMapping("/houses/{id}/images/create")
    public String createHouseImage(@PathVariable long id, @RequestParam String image_url){
        HouseImage image = new HouseImage(houseDao.getById(id), image_url);
        houseImageDao.save(image);
        return "redirect:/houses/" + id + "/images";
    }
}