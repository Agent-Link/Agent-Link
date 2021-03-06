package com.agentlink.agentlink.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
@Table(name = "reviews")
public class Review{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "listing_agent_id")
    private User listingUser;

    @ManyToOne
    @JoinColumn(name = "buying_agent_id")
    private User buyingUser;

    @OneToOne(mappedBy = "review")
    private OpenHouseEvent openHouseEvent;

    @Column(nullable = false)
    @CreationTimestamp
    private Date date;

    @Column(nullable = false, length = 70)
    @NotBlank
    @Size(max = 70, message = "must not be over 70 characters")
    private String title;

    @Column(nullable = false, length = 500)
    @NotBlank
    @Size(max = 500, message = "must not be over 500 characters")
    private String description;

    @Column(nullable = false)
    @Min(value = 1, message = "must select a rating")
    @Max(value = 5)
    private int rating;

    public Review() {
    }

    public Review(long id, User listingUser, User buyingUser, OpenHouseEvent openHouseEvent, Date date, String title, String description, int rating) {
        this.id = id;
        this.listingUser = listingUser;
        this.buyingUser = buyingUser;
        this.openHouseEvent = openHouseEvent;
        this.date = date;
        this.title = title;
        this.description = description;
        this.rating = rating;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getListingUser() {
        return listingUser;
    }

    public void setListingUser(User listingUser) {
        this.listingUser = listingUser;
    }

    public User getBuyingUser() {
        return buyingUser;
    }

    public void setBuyingUser(User buyingUser) {
        this.buyingUser = buyingUser;
    }

    public OpenHouseEvent getOpenHouseEvent() {
        return openHouseEvent;
    }

    public void setOpenHouseEvent(OpenHouseEvent openHouseEvent) {
        this.openHouseEvent = openHouseEvent;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
