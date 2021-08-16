package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.Application;
import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
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
    private final OpenHouseEventRepository eventsDao;
    private final ApplicationRepository applicationDao;

    public EventsController(HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository eventsDao, ApplicationRepository applicationDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.eventsDao = eventsDao;
        this.applicationDao = applicationDao;
    }

    @RequestMapping(path = "/events", method = RequestMethod.GET)
    public String getAllEvents(Model model) {
        List<OpenHouseEvent> openHouseEvents = eventsDao.findAll();
        model.addAttribute("openHouseEvents", openHouseEvents);
        return "openHouseEvents/index";
    }

    @GetMapping("/events/{id}")
    public String singleEvent(@PathVariable long id, Model model){
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);

        // Create boolean to check if user has already applied to event, then use to show/not show apply button
        boolean isEventCreator = false;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isEventCreator = currentUser.getId() == openHouseEvent.getHouse().getUser().getId();
        }
        List<Application> applications = applicationDao.findApplicationsByOpenHouseEventId(id);
        model.addAttribute("applications", applications);
        model.addAttribute("openHouseEvent", openHouseEvent);
        model.addAttribute("isEventCreator", isEventCreator);
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
        eventsDao.save(openHouseEvent);
        return "redirect:/events";
    }

    @GetMapping("/events/edit/{id}")
    public String editEventsForm(@PathVariable long id, Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);
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
        OpenHouseEvent eventFromDb = eventsDao.getById(openHouseEvent.getId());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Date startDateFormatted = simpleDateFormat.parse(startDate);
        Date endDateFormatted = simpleDateFormat.parse(endDate);

        openHouseEvent.setDateStart(startDateFormatted);
        openHouseEvent.setDateEnd(endDateFormatted);
        openHouseEvent.setHouse(eventFromDb.getHouse());

        if (currentUser.getId() == eventFromDb.getHouse().getUser().getId()) {
            openHouseEvent.setUser(eventFromDb.getUser());
            eventsDao.save(openHouseEvent);
        }
        return "redirect:/events";
    }

    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable("id") long id, Model model) {
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);
        eventsDao.delete(openHouseEvent);
        return "redirect:/events";
    }


}
