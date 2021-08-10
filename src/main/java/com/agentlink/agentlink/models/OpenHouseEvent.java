package com.agentlink.agentlink.models;


import javax.persistence.*;
import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "open_house_events")
public class OpenHouseEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "house_id")
    private House house;

    @ManyToOne
    @JoinColumn(name = "host_agent_id")
    private User user;

    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Time time;

    @Column(length = 500)
    private String feedback;

    public OpenHouseEvent() {
    }

    public OpenHouseEvent(long id, House house, User user, Date date, Time time, String feedback) {
        this.id = id;
        this.house = house;
        this.user = user;
        this.date = date;
        this.time = time;
        this.feedback = feedback;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public House getHouse() {
        return house;
    }

    public void setHouse(House house) {
        this.house = house;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
