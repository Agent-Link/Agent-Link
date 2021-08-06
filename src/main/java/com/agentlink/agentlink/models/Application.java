package com.agentlink.agentlink.models;

import javax.persistence.*;


@Entity
@Table(name = "applications")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "buying_agent_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "open_house_event_id")
    private OpenHouseEvent openHouseEvent;

    @Column(nullable = false, length = 500)
    private String inquiry;

    public Application() {
    }

    public Application(long id, User user, OpenHouseEvent openHouseEvent, String inquiry) {
        this.id = id;
        this.user = user;
        this.openHouseEvent = openHouseEvent;
        this.inquiry = inquiry;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OpenHouseEvent getOpenHouseEvent() {
        return openHouseEvent;
    }

    public void setOpenHouseEvent(OpenHouseEvent openHouseEvent) {
        this.openHouseEvent = openHouseEvent;
    }

    public String getInquiry() {
        return inquiry;
    }

    public void setInquiry(String inquiry) {
        this.inquiry = inquiry;
    }
}
