package com.agentlink.agentlink.controllers;


import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.Review;
import com.agentlink.agentlink.models.User;
import com.agentlink.agentlink.repositories.ReviewRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ReviewController {

    private ReviewRepository reviewsDao;
    private UserRepository usersDao;

    public ReviewController(ReviewRepository reviewsDao, UserRepository usersDao) {
        this.reviewsDao = reviewsDao;
        this.usersDao = usersDao;
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
        model.addAttribute("post", reviewsDao.getById(id));
        return"reviews/show";
    }

    @GetMapping("/reviews/create")
    public String createReview(Model model){
        model.addAttribute("createReview", new Review());
        return "reviews/create";
    }

    @PostMapping("/reviews/create")
    public String createHouse(@ModelAttribute Review review) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        review.setListingUser(currentUser);
        reviewsDao.save(review);
        return "redirect:/houses";
    }
}
