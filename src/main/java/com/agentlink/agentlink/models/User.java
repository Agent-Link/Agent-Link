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
    private String username;

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
    private List<House> houses;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<OpenHouseEvent> openHouseEvents;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Application> applications;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "listingUser")
    private List<Review> reviewsMade;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "buyingUser")
    private List<Review> reviewsReceived;

    public User() {
    }

    public User(long id, String username, String email, String password, String firstName, String lastName, String phone, String team, boolean isListingAgent) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.team = team;
        this.isListingAgent = isListingAgent;
    }

    public User(long id, String username, String email, String password, String firstName, String lastName, String phone, String team, boolean isListingAgent, List<House> houses, List<OpenHouseEvent> openHouseEvents, List<Application> applications, List<Review> reviewsMade, List<Review> reviewsReceived) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.team = team;
        this.isListingAgent = isListingAgent;
        this.houses = houses;
        this.openHouseEvents = openHouseEvents;
        this.applications = applications;
        this.reviewsMade = reviewsMade;
        this.reviewsReceived = reviewsReceived;
    }

    public User(User copy) {
        id = copy.id;
        email = copy.email;
        username = copy.username;
        password = copy.password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public List<House> getHouses() {
        return houses;
    }

    public void setHouses(List<House> houses) {
        this.houses = houses;
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

    public List<Review> getReviewsMade() {
        return reviewsMade;
    }

    public void setReviewsMade(List<Review> reviewsMade) {
        this.reviewsMade = reviewsMade;
    }

    public List<Review> getReviewsReceived() {
        return reviewsReceived;
    }

    public void setReviewsReceived(List<Review> reviewsReceived) {
        this.reviewsReceived = reviewsReceived;
    }
}
