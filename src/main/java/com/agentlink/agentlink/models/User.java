package com.agentlink.agentlink.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 25, unique = true)
    private String userName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 20)
    private String firstName;

    @Column(nullable = false, length = 30)
    private String lastName;

    @Column(nullable = false, length = 10)
    private String phone;

    @Column(nullable = false, length = 40)
    private String team;

    @Column(nullable = false)
    private boolean isListingAgent;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Home> homes;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<OpenHouseEvent> openHouseEvents;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Application> applications;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Review> reviews;

    public User() {
    }

    public User(long id, String userName, String email, String password, String firstName, String lastName, String phone, String team, boolean isListingAgent) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.team = team;
        this.isListingAgent = isListingAgent;
    }

    public User(long id, String userName, String email, String password, String firstName, String lastName, String phone, String team, boolean isListingAgent, List<Home> homes, List<OpenHouseEvent> openHouseEvents, List<Application> applications, List<Review> reviews) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.team = team;
        this.isListingAgent = isListingAgent;
        this.homes = homes;
        this.openHouseEvents = openHouseEvents;
        this.applications = applications;
        this.reviews = reviews;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public boolean isListingAgent() {
        return isListingAgent;
    }

    public void setListingAgent(boolean listingAgent) {
        isListingAgent = listingAgent;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public void setHomes(List<Home> homes) {
        this.homes = homes;
    }

    public List<OpenHouseEvent> getOpenHouseEvents() {
        return openHouseEvents;
    }

    public void setOpenHouseEvents(List<OpenHouseEvent> openHouseEvents) {
        this.openHouseEvents = openHouseEvents;
    }

    public List<Application> getApplications() {
        return applications;
    }

    public void setApplications(List<Application> applications) {
        this.applications = applications;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
