package com.agentlink.agentlink.models;

import javax.persistence.*;

@Entity
 public class HouseImage {
     @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;

     @ManyToOne
     @JoinColumn(name = "vendor_id", referencedColumnName = "id")
     private House house;

     @Column(name = "image_url")
     private String imageUrl;

    public HouseImage(long id, House house, String imageUrl) {
        this.id = id;
        this.house = house;
        this.imageUrl = imageUrl;
    }

    public HouseImage() {

    }

    public HouseImage(House house, String imageUrl) {
        this.house = house;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

