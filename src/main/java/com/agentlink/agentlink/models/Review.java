package com.agentlink.agentlink.models;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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

    @Column(nullable = false)
    @CreationTimestamp
    private Date date;

    @Column(nullable = false, length = 70)
    private String title;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private int rating;

    public Review() {
    }

    //INITIAL VALUE TO REVIEW INCLUDING GETTERS AND SETTERS
//    int reviewDefault = 5;
//
//    public Review(int reviewDefault) {
//        this.reviewDefault = reviewDefault;
//    }
//
//    public int getReviewDefault() {
//        return reviewDefault;
//    }
//
//    public void setReviewDefault(int reviewDefault) {
//        this.reviewDefault = reviewDefault;
//    }

    public Review(long id, User listingUser, User buyingUser, Date date, String title, String description, int rating) {
        this.id = id;
        this.listingUser = listingUser;
        this.buyingUser = buyingUser;
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
