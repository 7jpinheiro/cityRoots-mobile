package com.uminho.uce15.cityroots.data;

import java.io.Serializable;
import java.util.List;

public class Attraction extends Poi implements Serializable{

    private int id;
    private boolean isReferencePoint;
    private String price;

    public Attraction(String name, String schedule, String description, String transport, String site, String email, String address, double latitude, double longitude, boolean isActive, int timestamp, String phone, boolean hasAccessibility, List<String> type, List<String> photos, double rating, List<Comment> comments, int id, boolean isReferencePoint, String price) {
        super(name, schedule, description, transport, site, email, address, latitude, longitude, isActive, timestamp, phone, hasAccessibility, type, photos, rating, comments);
        this.id = id;
        this.isReferencePoint = isReferencePoint;
        this.price = price;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
