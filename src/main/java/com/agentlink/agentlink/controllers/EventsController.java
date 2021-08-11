package com.agentlink.agentlink.controllers;

import com.agentlink.agentlink.models.House;
import com.agentlink.agentlink.models.OpenHouseEvent;
import com.agentlink.agentlink.models.User;
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

    public EventsController(HouseRepository housesDao, UserRepository usersDao, OpenHouseEventRepository eventsDao) {
        this.housesDao = housesDao;
        this.usersDao = usersDao;
        this.eventsDao = eventsDao;
    }

    @RequestMapping(path = "/events", method = RequestMethod.GET)
    public String getAllEvents(Model model) {
        List<OpenHouseEvent> openHouseEvents = eventsDao.findAll();
        model.addAttribute("openHouseEvents", openHouseEvents);
        return "events/index";
    }

    @GetMapping("/events/{id}")
    public String singleEvent(@PathVariable long id, Model model){
        OpenHouseEvent openHouseEvent = eventsDao.getById(id);
        boolean isEventCreator = false;
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() != "anonymousUser") {
            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            isEventCreator = currentUser.getId() == openHouseEvent.getUser().getId();
        }
        model.addAttribute("openHouseEvent", openHouseEvent);
        model.addAttribute("isEventCreator", isEventCreator);
        return "events/show";
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
    public String createEvent(@ModelAttribute OpenHouseEvent openHouseEvent, @RequestParam String eventDate) throws ParseException {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        openHouseEvent.setUser(currentUser);
//        String sDate1="eventDate";
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

        Date date = simpleDateFormat.parse(eventDate);
        System.out.println(date);

        openHouseEvent.setDate(date);
        eventsDao.save(openHouseEvent);
        return "redirect:/houses";
    }
}
