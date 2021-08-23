package com.agentlink.agentlink.models;


import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "openHouseEvent")
    private List<Application> applications;

    @OneToOne(cascade = CascadeType.ALL)
    private Review review;

    @Column(nullable = false)
    private Date dateStart;

    @Column(nullable = false)
    private Date dateEnd;

    @Column(length = 500)
    private String feedback;

    public OpenHouseEvent() {
    }

    public OpenHouseEvent(long id, House house, User user, List<Application> applications, Review review, Date dateStart, Date dateEnd, String feedback) {
        this.id = id;
        this.house = house;
        this.user = user;
        this.applications = applications;
        this.review = review;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
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

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

}
