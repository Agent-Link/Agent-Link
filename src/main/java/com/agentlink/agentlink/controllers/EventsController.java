package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.*;
import com.agentlink.agentlink.repositories.ApplicationRepository;
import com.agentlink.agentlink.repositories.HouseRepository;
import com.agentlink.agentlink.repositories.OpenHouseEventRepository;
import com.agentlink.agentlink.repositories.UserRepository;
import jdk.jfr.Event;
import org.springframework.beans.factory.annotation.Value;
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
    private final OpenHouseEventRepository eventsDao;
    private final ApplicationRepository applicationsDao;

    @Value("${MAPBOX_ACCESS_TOKEN}")
    private String MAPBOX_ACCESS_TOKEN;

    public EventsController(HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository eventsDao, ApplicationRepository applicationsDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.eventsDao = eventsDao;
        this.applicationsDao = applicationsDao;
    }

    @GetMapping("/events")
    public String getAllEvents(Model model) {
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        List<OpenHouseEvent> openHouseEvents = eventsDao.findAllWithoutHostWhereDateStartAfter(new Date());
        model.addAttribute("house", housesDao);
        model.addAttribute("openHouseEvents", openHouseEvents);
        model.addAttribute("MAPBOX_ACCESS_TOKEN", MAPBOX_ACCESS_TOKEN);
        return "openHouseEvents/index";
    }

    @GetMapping("/events/{id}")
    public String singleEvent(@PathVariable long id, Model model) throws ParseException {
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);
        model.addAttribute("MAPBOX_ACCESS_TOKEN", MAPBOX_ACCESS_TOKEN);
        model.addAttribute("openHouseEvent", openHouseEvent);
        boolean isEventCreator;
        boolean hasNotApplied = true;
        boolean hasNoEventScheduled = false;
        User currentUser;

        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isEventCreator = currentUser.getId() == openHouseEvent.getHouse().getUser().getId();
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        } else {
            return "openHouseEvents/show";
        }

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy-MM-dd");

        OpenHouseEvent userHasEventScheduled = eventsDao.findByUserIdAndDateStartLike(currentUser.getId(), sdfYMD.format(openHouseEvent.getDateStart()));
        if (userHasEventScheduled == null) {
            hasNoEventScheduled = true;
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
        model.addAttribute("hasNoEventScheduled", hasNoEventScheduled);
        model.addAttribute("currentDateTime", new Date());
        return "openHouseEvents/show";
    }

    @GetMapping("/events/create")
    public String createEventForm(Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = usersDao.getById(currentUser.getId());
        model.addAttribute("user", user);
        List<House> houses = housesDao.findAllByUserAndListingActive(currentUser, true);
        if (user.isListingAgent()) {
            model.addAttribute("houses", houses);
            model.addAttribute("openHouseEvent", new OpenHouseEvent());
            return "openHouseEvents/create";
        } else
            return "redirect:/";
    }

    @PostMapping("/events/create")
    public String createEvent(@ModelAttribute OpenHouseEvent openHouseEvent, @RequestParam String startDate, @RequestParam String endDate, @RequestParam Long house, Model model) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        openHouseEvent.setUser(currentUser);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");

        House selectedHouse = housesDao.getById(house);

        List<OpenHouseEvent> houseEventList = selectedHouse.getOpenHouseEvents();

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);

        boolean startTimeNotBeforeEndTime = startDateFormatted.after(endDateFormatted);
        boolean doesNotStartAfterCurrentDateAndTime = startDateFormatted.before(new Date());
        boolean doesNotStartAndEndOnSameDay = !sdfYMD.format(startDateFormatted).equals(sdfYMD.format(endDateFormatted));
        boolean isCreatorOfHouse = currentUser.getId() == selectedHouse.getUser().getId();

        List<House> houses = housesDao.findAllByUserAndListingActive(currentUser, true);

        // Checks for existing events on a house for the input start date.
        if (houseEventList != null) {
            for (OpenHouseEvent event : houseEventList) {
                if (sdfYMD.format(event.getDateStart()).equals(sdfYMD.format(startDateFormatted))) {
                    model.addAttribute("eventOnSelectedDateAlreadyExists", true);
                    model.addAttribute("houses", houses);
                    return "openHouseEvents/create";
                }
            }
        }

        // Verifies start/end date are on the same day and that the time is set in the future and that the start time is before the end time.
        if (startTimeNotBeforeEndTime || doesNotStartAfterCurrentDateAndTime || doesNotStartAndEndOnSameDay) {
            model.addAttribute("houses", houses);
            if (startTimeNotBeforeEndTime) {
                model.addAttribute("startTimeNotBeforeEndTime", true);
            }
            if (doesNotStartAfterCurrentDateAndTime) {
                model.addAttribute("doesNotStartAfterCurrentDateAndTime", true);
            }
            if (doesNotStartAndEndOnSameDay) {
                model.addAttribute("doesNotStartAndEndOnSameDay", true);
            }
            return "openHouseEvents/create";
        }
        // Verifies if the user trying to create the event is actually the listing agent/creator of the house they are trying to create an event for.
        if (isCreatorOfHouse) {
            openHouseEvent.setDateStart(startDateFormatted);
            openHouseEvent.setDateEnd(endDateFormatted);
            eventsDao.save(openHouseEvent);
        } else {
            // If verification is not met redirect; we should eventually add an error message/page here
            return "redirect:/";
        }
        return "redirect:/events";
    }

    @GetMapping("/events/edit/{id}")
    public String editEventsForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);
        if (currentUser.getId() == openHouseEvent.getHouse().getUser().getId() && openHouseEvent.getDateStart().after(new Date())) {
            model.addAttribute("openHouseEvent", openHouseEvent);
            return "openHouseEvents/edit";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/events/edit/{id}")
    public String saveEditedEvent(@ModelAttribute OpenHouseEvent openHouseEvent, @RequestParam String startDate, @RequestParam String endDate, Model model) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEventFromDb = eventsDao.getById(openHouseEvent.getId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyyMMdd");

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);

        List<OpenHouseEvent> houseEventList = openHouseEventFromDb.getHouse().getOpenHouseEvents();

        boolean startTimeNotBeforeEndTime = startDateFormatted.after(endDateFormatted);
        boolean doesNotStartAfterCurrentDateAndTime = startDateFormatted.before(new Date());
        boolean doesNotStartAndEndOnSameDay = !sdfYMD.format(startDateFormatted).equals(sdfYMD.format(endDateFormatted));
        boolean isCreatorOfHouse = currentUser.getId() == openHouseEventFromDb.getHouse().getUser().getId();

        // Checks for existing events on a house for the input start date.
        for (OpenHouseEvent event : houseEventList) {
            if (sdfYMD.format(event.getDateStart()).equals(sdfYMD.format(startDateFormatted)) && event.getId() != openHouseEventFromDb.getId()) {
                model.addAttribute("eventOnSelectedDateAlreadyExists", true);
                model.addAttribute("openHouseEvent", openHouseEventFromDb);
                return "openHouseEvents/edit";
            }
        }

        // Verifies start/end date are on the same day and that the time is set in the future and that the start time is before the end time.
        if (startTimeNotBeforeEndTime || doesNotStartAfterCurrentDateAndTime || doesNotStartAndEndOnSameDay) {
            model.addAttribute("openHouseEvent", openHouseEventFromDb);
            if (startTimeNotBeforeEndTime) {
                model.addAttribute("startTimeNotBeforeEndTime", true);
            }
            if (doesNotStartAfterCurrentDateAndTime) {
                model.addAttribute("doesNotStartAfterCurrentDateAndTime", true);
            }
            if (doesNotStartAndEndOnSameDay) {
                model.addAttribute("doesNotStartAndEndOnSameDay", true);
            }
            return "openHouseEvents/edit";
        }

        /// need to disallow editing event once it has passed

//      Checks if the user editing the event is actually the listing agent/creator of the event house
        if (isCreatorOfHouse && openHouseEventFromDb.getDateStart().after(new Date())) {
            openHouseEvent.setDateStart(startDateFormatted);
            openHouseEvent.setDateEnd(endDateFormatted);
            openHouseEvent.setHouse(openHouseEventFromDb.getHouse());
            openHouseEvent.setUser(openHouseEventFromDb.getUser());
            eventsDao.save(openHouseEvent);
        } else {
            // If verification is not met redirect; we should eventually add an error message/page here
            return "redirect:/";
        }
        return "redirect:/events";
    }

    @PostMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);
        // Verifies the current user is the event creator and that the event has not started/passed.
        if (currentUser.getId() == openHouseEvent.getHouse().getUser().getId() && openHouseEvent.getDateStart().after(new Date())) {
            eventsDao.delete(openHouseEvent);
        }
        return "redirect:/profile";
    }


    @GetMapping("/event/feedback/{eventId}")
    public String submitFeedbackForm(Model model, @PathVariable long eventId){
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEventFromDb = eventsDao.getById(eventId);
        // This verifies that the current user should have permission to leave feedback on the event and cannot leave feedback on themself if they host the event
        if(openHouseEventFromDb.getUser().getId() == currentUser.getId() && currentUser.getId() != openHouseEventFromDb.getHouse().getUser().getId() && new Date().after(openHouseEventFromDb.getDateEnd()) && openHouseEventFromDb.getFeedback() == null) {

            model.addAttribute("openHouseEvent", eventsDao.getById(eventId));
            return "openHouseEvents/createfeedback";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/event/feedback/{eventId}")
    public String submitFeedback(@PathVariable long eventId, @RequestParam String feedback, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User user = usersDao.getById(currentUser.getId());
            model.addAttribute("user", user);
        }
        OpenHouseEvent openHouseEventFromDb = eventsDao.getById(eventId);
        model.addAttribute("openHouseEvent", openHouseEventFromDb);
        if (feedback.trim().isEmpty()) {
            model.addAttribute("isBlank", true);
            return "openHouseEvents/createfeedback";
        }
        if (feedback.length() > 500) {
            model.addAttribute("isOver500", true);
            return "openHouseEvents/createfeedback";
        }
        // This verifies that the current user should have permission to leave feedback on the event and cannot leave feedback on themself if they host the event
        if(openHouseEventFromDb.getUser().getId() == currentUser.getId() && currentUser.getId() != openHouseEventFromDb.getHouse().getUser().getId() && new Date().after(openHouseEventFromDb.getDateEnd()) && openHouseEventFromDb.getFeedback() == null) {
            openHouseEventFromDb.setFeedback(feedback);
            eventsDao.save(openHouseEventFromDb);
        } else {
            return "redirect:/";
        }
        return "redirect:/profile";
    }

}
