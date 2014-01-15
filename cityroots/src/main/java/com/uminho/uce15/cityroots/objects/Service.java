package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Service extends Poi {
    private boolean is_reference_point;
    private int capacity;
    private String details;

    public Service(String name, String schedule, String language, String description, String transport, String price, String site, String email, String address, float latitude, float longitude, boolean is_active, int timestamp, boolean has_accessibility, String type, ArrayList<Photo> photos, float rating, boolean is_reference_point, int capacity, String details) {
        super(name, schedule, language, description, transport, price, site, email, address, latitude, longitude, is_active, timestamp, has_accessibility, type, photos, rating);
        this.is_reference_point = is_reference_point;
        this.capacity = capacity;
        this.details = details;
    }

    public boolean isIs_reference_point() {
        return is_reference_point;
    }

    public void setIs_reference_point(boolean is_reference_point) {
        this.is_reference_point = is_reference_point;
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
