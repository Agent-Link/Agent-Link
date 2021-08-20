package com.agentlink.agentlink.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "houses")
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 50)
    @NotBlank
    @Size(max = 50, message = "Max size 50 chars")
    private String address;

    @Column(nullable = false, length = 30)
    @NotBlank
    @Size(max = 30, message = "Max size 30 chars")
    private String city;

    @Column(nullable = false, length = 30)
    @NotBlank
    private String state;

    @Column(nullable = false, length = 10)
    @NotBlank
    @Size(max = 10, message = "Max size 10 chars")
    private String zipcode;

    @Column(nullable = false, length = 500)
    @NotBlank
    @Size(max = 500, message = "Max size 500 chars")
    private String description;

    @Column
    private boolean isListingActive = true;

    @Column(length = 255)
    private String image_url;

    @OneToMany(mappedBy = "house")
    private List<HouseImage> images = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "listing_agent_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "house")
    private List<OpenHouseEvent> openHouseEvents;

    public House() {
    }

//    public House(long id, String address, String city, String state, String zipcode, String description, boolean isListingActive, User user) {
//        this.id = id;
//        this.address = address;
//        this.city = city;
//        this.state = state;
//        this.zipcode = zipcode;
//        this.description = description;
//        this.isListingActive = isListingActive;
//        this.user = user;
//    }

    public House(long id, String address, String city, String state, String zipcode, String description, boolean isListingActive, String image_url, List<HouseImage> images, User user, List<OpenHouseEvent> openHouseEvents) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.description = description;
        this.isListingActive = isListingActive;
        this.image_url = image_url;
        this.images = images;
        this.user = user;
        this.openHouseEvents = openHouseEvents;
    }

    public House(String address, String city, String state, String zipcode, String description, boolean isListingActive, String image_url, List<HouseImage> images, User user, List<OpenHouseEvent> openHouseEvents) {
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.description = description;
        this.isListingActive = isListingActive;
        this.image_url = image_url;
        this.images = images;
        this.user = user;
        this.openHouseEvents = openHouseEvents;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isListingActive() {
        return isListingActive;
    }

    public void setListingActive(boolean listingActive) {
        isListingActive = listingActive;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OpenHouseEvent> getOpenHouseEvents() {
        return openHouseEvents;
    }

    public void setOpenHouseEvents(List<OpenHouseEvent> openHouseEvents) {
        this.openHouseEvents = openHouseEvents;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public List<HouseImage> getImages() {
        return images;
    }

    public void setImages(List<HouseImage> images) {
        this.images = images;
    }
}
