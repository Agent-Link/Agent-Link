package com.agentlink.agentlink.models;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @ManyToOne
    @JoinColumn(name = "listing_agent_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "house")
    private List<OpenHouseEvent> openHouseEvents;

    public House() {
    }

    public House(long id, String address, String city, String state, String zipcode, String description, User user) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
        this.description = description;
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
