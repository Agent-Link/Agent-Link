package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.Review;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.ReviewRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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

    @GetMapping("/reviews/{eventId}/create")
    public String createReviewForm(Model model, @PathVariable long eventId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEvent = openhouseDao.getById(eventId);
        // This verifies that the current user should have permission to leave a review
        if (openHouseEvent.getHouse().getUser().getId() == currentUser.getId() && currentUser.getId() != openHouseEvent.getUser().getId() && new Date().after(openHouseEvent.getDateEnd()) && openHouseEvent.getReview() == null) {
            model.addAttribute("openHouseEvent", openHouseEvent);
            model.addAttribute("review", new Review());
            return "reviews/create";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/reviews/{eventId}/create")
    public String submitReview(@Valid @ModelAttribute Review review, BindingResult result, @PathVariable long eventId, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEvent = openhouseDao.getById(eventId);
        if (result.hasErrors()) {
            model.addAttribute("openHouseEvent", openHouseEvent);
            model.addAttribute("review", review);
            return "reviews/create";
        }
        // This verifies that the current user should have permission to leave a review on the host of the event and that they cannot review themselves if they were host
        if(openHouseEvent.getHouse().getUser().getId() == currentUser.getId() && currentUser.getId() != openHouseEvent.getUser().getId() && new Date().after(openHouseEvent.getDateEnd()) && openHouseEvent.getReview() == null) {
            User buyingAgent = openhouseDao.getById(eventId).getUser();
            openHouseEvent.setReview(review);
            review.setOpenHouseEvent(openHouseEvent);
            review.setListingUser(currentUser);
            review.setBuyingUser(buyingAgent);
            reviewsDao.save(review);
        } else {
            return "redirect:/";
        }
        return "redirect:/profile";
    }
}
