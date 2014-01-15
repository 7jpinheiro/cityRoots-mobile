package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Poi {
    private String name;
    private String schedule;
    private String language;
    private String description;
    private String transport;
    private String price;
    private String site;
    private String email;
    private String address;
    private float latitude;
    private float longitude;
    private boolean is_active;
    private int timestamp;
    private boolean has_accessibility;
    private String type;
    private ArrayList<Photo> photos;
    private float rating;

    public Poi(String name, String schedule, String language, String description, String transport, String price, String site, String email, String address, float latitude, float longitude, boolean is_active, int timestamp, boolean has_accessibility, String type, ArrayList<Photo> photos, float rating) {
        this.name = name;
        this.schedule = schedule;
        this.language = language;
        this.description = description;
        this.transport = transport;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        this.photos = photos;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }
}
