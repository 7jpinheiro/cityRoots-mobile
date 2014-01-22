package com.uminho.uce15.cityroots.data;

import java.io.Serializable;
import java.util.List;

public class Service extends Poi implements Serializable {

    private int id;
    private boolean isReferencePoint;
    private int capacity;
    private String details;

    public Service(String name, String schedule, String description, String transport, String site, String email, String address, double latitude, double longitude, boolean isActive, int timestamp, String phone, boolean hasAccessibility, List<String> type, List<String> photos, double rating, List<Comment> comments, int id, boolean isReferencePoint, int capacity, String details) {
        super(name, schedule, description, transport, site, email, address, latitude, longitude, isActive, timestamp, phone, hasAccessibility, type, photos, rating, comments);
        this.id = id;
        this.isReferencePoint = isReferencePoint;
        this.capacity = capacity;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReferencePoint() {
        return isReferencePoint;
    }

    public void setReferencePoint(boolean isReferencePoint) {
        this.isReferencePoint = isReferencePoint;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
