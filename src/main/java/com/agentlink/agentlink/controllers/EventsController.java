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
        model.addAttribute("openHouseEvent", openHouseEvent);
        boolean isEventCreator;
        boolean hasNotApplied = true;
        User currentUser;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isEventCreator = currentUser.getId() == openHouseEvent.getHouse().getUser().getId();
        } else {
            return "openHouseEvents/show";
        }

        List<Application> applications = applicationsDao.findApplicationsByOpenHouseEventId(id);
        for (Application application : applications) {
            if (application.getUser().getId() == currentUser.getId()) {
                hasNotApplied = false;
                break;
            }
        }
        model.addAttribute("applications", applications);
        model.addAttribute("isEventCreator", isEventCreator);
        model.addAttribute("hasNotApplied", hasNotApplied);
        model.addAttribute("currentDateTime", new Date());
        return "openHouseEvents/show";
    }

    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<House> houses = housesDao.findAllByUserAndListingActive(currentUser, true);

        model.addAttribute("houses", houses);
        model.addAttribute("openHouseEvent", new OpenHouseEvent());
        return "/openHouseEvents/create";
    }

    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute OpenHouseEvent openHouseEvent, @RequestParam String startDate, @RequestParam String endDate, @RequestParam Long house) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        openHouseEvent.setUser(currentUser);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");

        House selectedHouse = housesDao.getById(house);

        List<OpenHouseEvent> houseEventList = selectedHouse.getOpenHouseEvents();

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);
        // Checks for existing events on a house for the input start date, if an event already exists you will be redirected.
        if (houseEventList != null) {
            for (OpenHouseEvent event : houseEventList) {
                if (sdfYMD.format(event.getDateStart()).equals(sdfYMD.format(startDateFormatted))) {
                    return "redirect:/";
                }
            }
        }

        // Verifies start/end date are on the same day and that the time is set in the future and that the start time is before the end time along
        // with checking if the user trying to create the event is actually the listing agent of the house they are trying to create an event for.
        if (sdfYMD.format(startDateFormatted).equals(sdfYMD.format(endDateFormatted)) && startDateFormatted.after(new Date()) && startDateFormatted.before(endDateFormatted) && currentUser.getId() == openHouseEvent.getHouse().getUser().getId()) {
            openHouseEvent.setDateStart(startDateFormatted);
            openHouseEvent.setDateEnd(endDateFormatted);
            openHouseEventsDao.save(openHouseEvent);
        } else {
            // If verification is not met redirect; we should eventually add an error message/page here
            return "redirect:/";
        }
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

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);

//        Need to test and probably refactor this code to be properly implemented on edit. This will probably currently have an issue with the current date selected if you're just trying to change the time
        // Checks for existing events on a house for the input start date, if an event already exists you will be redirected.
//        House selectedHouse = housesDao.getById(openHouseEventFromDb.getHouse().getId());
//
//        List<OpenHouseEvent> houseEventList = selectedHouse.getOpenHouseEvents();
//
//        if (houseEventList != null) {
//            for (OpenHouseEvent event : houseEventList) {
//                if (sdfYMD.format(event.getDateStart()).equals(sdfYMD.format(startDateFormatted))) {
//                    return "redirect:/";
//                }
//            }
//        }

        /// need to disallow editing event once it has passed

        // Verifies the event has not started/passed, verifies the new event start/end day are on the same day and the time is set in the future and that
        // the start time is before the end time along with checking if the user editing the event is actually the listing agent of the event house
        if (sdfYMD.format(startDateFormatted).equals(sdfYMD.format(endDateFormatted)) && openHouseEventFromDb.getDateStart().after(new Date()) && startDateFormatted.after(new Date()) && startDateFormatted.before(endDateFormatted) && currentUser.getId() == openHouseEventFromDb.getHouse().getUser().getId()) {
            openHouseEvent.setDateStart(startDateFormatted);
            openHouseEvent.setDateEnd(endDateFormatted);
            openHouseEvent.setHouse(openHouseEventFromDb.getHouse());
            openHouseEvent.setUser(openHouseEventFromDb.getUser());
            openHouseEventsDao.save(openHouseEvent);
        } else {
            // If verification is not met redirect; we should eventually add an error message/page here
            return "redirect:/";
        }
        return "redirect:/events";
    }

    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable long id) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEvent = openHouseEventsDao.getById(id);
        // Verifies the current user is the event creator and that the event has not started/passed.
        if (currentUser.getId() == openHouseEvent.getHouse().getUser().getId() && openHouseEvent.getDateStart().after(new Date())) {
            openHouseEventsDao.delete(openHouseEvent);
        }
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
    public String submitFeedback(@PathVariable long eventId, @RequestParam String feedback) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEventFromDb = openHouseEventsDao.getById(eventId);

        // This verifies that the current user should have permission to leave feedback on the event and cannot leave feedback on themself if they host the event
        if(openHouseEventFromDb.getUser().getId() == currentUser.getId() && currentUser.getId() != openHouseEventFromDb.getHouse().getUser().getId() && new Date().after(openHouseEventFromDb.getDateEnd()) && openHouseEventFromDb.getFeedback() == null) {
            openHouseEventFromDb.setFeedback(feedback);
            openHouseEventsDao.save(openHouseEventFromDb);
        } else {
            return "redirect:/";
        }
        return "redirect:/profile";
    }
////////////////////

}
