package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.*;
import com.agentlink.agentlink.repositories.ApplicationRepository;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import jdk.jfr.Event;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
public class EventsController {

    private final HouseRepository housesDao;
    private final UserRepository usersDao;
    private final OpenHouseEventRepository openHouseEventsDao;
    private final ApplicationRepository applicationsDao;

    public EventsController(HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository openHouseEventsDao, ApplicationRepository applicationsDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.openHouseEventsDao = openHouseEventsDao;
        this.applicationsDao = applicationsDao;
    }

    @RequestMapping(path = "/events", method = RequestMethod.GET)
    public String getAllEvents(Model model) {
        List<OpenHouseEvent> openHouseEvents = openHouseEventsDao.findAll();
        model.addAttribute("openHouseEvents", openHouseEvents);
        return "openHouseEvents/index";
    }

    @GetMapping("/events/{id}")
    public String singleEvent(@PathVariable long id, Model model){
        OpenHouseEvent openHouseEvent = openHouseEventsDao.getById(id);
        // Create boolean to check if user has already applied to event, then use to show/not show apply button
        boolean isEventCreator = false;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isEventCreator = currentUser.getId() == openHouseEvent.getHouse().getUser().getId();
        }
        List<Application> applications = applicationsDao.findApplicationsByOpenHouseEventId(id);
        model.addAttribute("applications", applications);
        model.addAttribute("openHouseEvent", openHouseEvent);
        model.addAttribute("isEventCreator", isEventCreator);
        model.addAttribute("currentDateTime", new Date());
        return "openHouseEvents/show";
    }

    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<House> houses = housesDao.findByUser(currentUser);

        model.addAttribute("houses", houses);
        model.addAttribute("openHouseEvent", new OpenHouseEvent());
        return "/openHouseEvents/create";
    }

    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute OpenHouseEvent openHouseEvent, @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        openHouseEvent.setUser(currentUser);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);


        openHouseEvent.setDateStart(startDateFormatted);
        openHouseEvent.setDateEnd(endDateFormatted);
        openHouseEventsDao.save(openHouseEvent);
        return "redirect:/events";
    }

    @GetMapping("/events/edit/{id}")
    public String editEventsForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEvent = openHouseEventsDao.getById(id);
        if (currentUser.getId() == openHouseEvent.getHouse().getUser().getId()) {
            model.addAttribute("openHouseEvent", openHouseEvent);
            return "/openHouseEvents/edit";
        } else {
            return "redirect:/events";
        }
    }

    @PostMapping("/events/edit/{id}")
    public String saveEditedEvent(@ModelAttribute OpenHouseEvent openHouseEvent, @RequestParam String startDate, @RequestParam String endDate) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEventFromDb = openHouseEventsDao.getById(openHouseEvent.getId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);

        openHouseEvent.setDateStart(startDateFormatted);
        openHouseEvent.setDateEnd(endDateFormatted);
        openHouseEvent.setHouse(openHouseEventFromDb.getHouse());
        /// need to disallow editing event once it has passed
        if (currentUser.getId() == openHouseEventFromDb.getHouse().getUser().getId()) {
            openHouseEvent.setUser(openHouseEventFromDb.getUser());
            openHouseEventsDao.save(openHouseEvent);
        }
        return "redirect:/events";
    }

    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable("id") long id, Model model) {
        OpenHouseEvent openHouseEvent = openHouseEventsDao.getById(id);
        openHouseEventsDao.delete(openHouseEvent);
        return "redirect:/events";
    }



/////////////////////

    /// Need to clean up unused code here

    @GetMapping("/event/feedback/{eventId}")
    public String submitFeedbackForm(Model model, @PathVariable long eventId){
        model.addAttribute("openHouseEvent", openHouseEventsDao.getById(eventId));
        return "/openHouseEvents/createfeedback";
    }

    @PostMapping("/event/feedback/{eventId}")
    public String submitFeedback(@ModelAttribute OpenHouseEvent openHouseEvent, @PathVariable long eventId, @RequestParam String feedback, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEventFromDb = openHouseEventsDao.getById(eventId);

        // This verifies that the current user should have permission to leave feedback on the event
        if(openHouseEventFromDb.getUser().getId() == currentUser.getId() && new Date().after(openHouseEventFromDb.getDateEnd()) && openHouseEventFromDb.getFeedback() == null) {
            openHouseEventFromDb.setFeedback(feedback);
            openHouseEventsDao.save(openHouseEventFromDb);
        } else {
            return "redirect:/";
        }
        return "redirect:/profile";
    }
////////////////////

}
