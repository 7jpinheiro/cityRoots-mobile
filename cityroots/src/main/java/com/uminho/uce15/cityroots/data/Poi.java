package com.uminho.uce15.cityroots.data;

import java.io.Serializable;
import java.util.List;

public class Poi implements Serializable {

    private String name;
    private String schedule;
    private String description;
    private String transport;
    private String site;
    private String email;
    private String address;
    private double latitude;
    private double longitude;
    private boolean isActive;
    private int timestamp;
    private String phone;
    private boolean hasAccessibility;
    private List<String> type;
    private List<String> photos;
    private double rating;
    private List<Comment> comments;

    public Poi(String name, String schedule, String description, String transport, String site, String email, String address, double latitude, double longitude, boolean isActive, int timestamp, String phone, boolean hasAccessibility, List<String> type, List<String> photos, double rating, List<Comment> comments) {
        this.name = name;
        this.schedule = schedule;
        this.description = description;
        this.transport = transport;
        this.site = site;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = isActive;
        this.timestamp = timestamp;
        this.phone = phone;
        this.hasAccessibility = hasAccessibility;
        this.type = type;
        this.photos = photos;
        this.rating = rating;
        this.comments = comments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean hasAccessibility() {
        return hasAccessibility;
    }

    public void setHasAccessibility(boolean hasAccessibility) {
        this.hasAccessibility = hasAccessibility;
    }

    public List<String> getType() {
        return type;
    }

    public void setType(List<String> type) {
        this.type = type;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void addComment(Comment comment) {
        this.comments.add(comment);
    }
}
