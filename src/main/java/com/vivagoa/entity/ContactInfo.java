package com.vivagoa.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "contact_info")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String restaurantName;

    private String tagline;

    private String phone1;

    private String phone2;

    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String mapEmbedUrl;

    @Column(columnDefinition = "TEXT")
    private String openingHours;

    private String instagramUrl;

    private String facebookUrl;

    private String zomatoUrl;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMapEmbedUrl() {
        return mapEmbedUrl;
    }

    public void setMapEmbedUrl(String mapEmbedUrl) {
        this.mapEmbedUrl = mapEmbedUrl;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getInstagramUrl() {
        return instagramUrl;
    }

    public void setInstagramUrl(String instagramUrl) {
        this.instagramUrl = instagramUrl;
    }

    public String getFacebookUrl() {
        return facebookUrl;
    }

    public void setFacebookUrl(String facebookUrl) {
        this.facebookUrl = facebookUrl;
    }

    public String getZomatoUrl() {
        return zomatoUrl;
    }

    public void setZomatoUrl(String zomatoUrl) {
        this.zomatoUrl = zomatoUrl;
    }
}
