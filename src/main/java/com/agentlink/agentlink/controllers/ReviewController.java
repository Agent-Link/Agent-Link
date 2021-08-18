package com.agentlink.agentlink.controllers;


import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.Review;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.ReviewRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class ReviewController {

    private ReviewRepository reviewsDao;
    private UserRepository usersDao;
    private OpenHouseEventRepository openhouseDao;

    public ReviewController(ReviewRepository reviewsDao, UserRepository usersDao, OpenHouseEventRepository openhouseDao) {
        this.reviewsDao = reviewsDao;
        this.usersDao = usersDao;
        this.openhouseDao = openhouseDao;
    }

    @GetMapping(path = "/reviews")
    public String getAllReviews(Model model) {
        List<Review> reviews = reviewsDao.findAll();
        model.addAttribute("reviews", reviews);
        return "reviews/index";
    }

    //STILL NEED TO CREATE HTML PAGE.
    @GetMapping("/reviews/{id}")
    public String singleHouse(@PathVariable long id, Model model){
        model.addAttribute("review", reviewsDao.getById(id));
        return"reviews/show";
    }

    @GetMapping("/reviews/{eventId}/create")
    public String createReviewForm(Model model, @PathVariable long eventId){
        model.addAttribute("openHouseEvent", openhouseDao.getById(eventId));
        model.addAttribute("review", new Review());
        return "reviews/create";
    }

    @PostMapping("/reviews/{eventId}/create")
    public String submitReview(@ModelAttribute Review review, @PathVariable long eventId, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEvent = openhouseDao.getById(eventId);

        // This verifies that the current user should have permission to leave a review on the host of the event
        if(openHouseEvent.getHouse().getUser().getId() == currentUser.getId() && new Date().after(openHouseEvent.getDateEnd()) && reviewsDao.findReviewWhere(eventId) == null) {
            User buyingAgent = openhouseDao.getById(eventId).getUser();
            review.setOpenHouseEvent(openHouseEvent);
            review.setListingUser(currentUser);
            review.setBuyingUser(buyingAgent);
            reviewsDao.save(review);
        } else {
            return "redirect:/";
        }
        return "redirect:/reviews";
    }
}
