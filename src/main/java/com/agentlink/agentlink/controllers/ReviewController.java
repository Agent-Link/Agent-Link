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

    @GetMapping("/reviews/{id}/create")
    public String createReview(Model model, @PathVariable long id){
        OpenHouseEvent event = openhouseDao.getById(id);
        model.addAttribute("openHouseEvent", event);
        model.addAttribute("review", new Review());
        return "reviews/create";
    }

    @PostMapping("/profile/reviews")
    public String getAllUserReviews(@ModelAttribute Review review, @RequestParam String eventDate, @RequestParam long eventId) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setListingUser(currentUser);
        User buyingAgent = openhouseDao.getById(eventId).getUser();
        review.setBuyingUser(buyingAgent);

        //DATE INFO-Need current date not date of event
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date = simpleDateFormat.parse(eventDate);
        review.setDate(date);

        reviewsDao.save(review);
        return "redirect:/reviews";
    }
}
