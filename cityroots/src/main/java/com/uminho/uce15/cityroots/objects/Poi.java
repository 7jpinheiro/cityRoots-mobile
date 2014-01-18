package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Poi {
    private String name;
    private String schedule;
    private String language;
    private String description;
    private String transport;
    private String site;
    private String email;
    private String address;
    private double latitude;
    private double longitude;
    private boolean is_active;
    private int timestamp;
    private boolean has_accessibility;
    private ArrayList<String> type;
    private ArrayList<Photo> photos;
    private double rating;
    private ArrayList<Comment> comments;

    public Poi(String name, String schedule, String language, String description, String transport, String site, String email, String address, double latitude, double longitude, boolean is_active, int timestamp, boolean has_accessibility, ArrayList<String> type, ArrayList<Photo> photos, double rating, ArrayList<Comment> comments) {
        this.name = name;
        this.schedule = schedule;
        this.language = language;
        this.description = description;
        this.transport = transport;
        this.site = site;
        this.email = email;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.is_active = is_active;
        this.timestamp = timestamp;
        this.has_accessibility = has_accessibility;
        this.type = type;
        this.photos = photos;
        this.rating = rating;
        this.comments = comments;
    }

    public Poi( Poi p){
        this.name = p.getName();
        this.schedule = p.getSchedule();
        this.language = p.getLanguage();
        this.description = p.getDescription();
        this.transport = p.getTransport();
        this.site = p.getSite();
        this.email = p.getEmail();
        this.address = p.getAddress();
        this.latitude = p.getLatitude();
        this.longitude = p.getLongitude();
        this.is_active = p.isIs_active();
        this.timestamp = p.getTimestamp();
        this.has_accessibility = p.isHas_accessibility();
        this.type = p.getType();
        this.photos = p.getPhotos();
        this.rating = p.getRating();
        this.comments = p.getComments();
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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
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

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHas_accessibility() {
        return has_accessibility;
    }

    public void setHas_accessibility(boolean has_accessibility) {
        this.has_accessibility = has_accessibility;
    }

    public ArrayList<String> getType() {
        return type;
    }

    public void setType(ArrayList<String> type) {
        this.type = type;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
