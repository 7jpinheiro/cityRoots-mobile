package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Attraction extends Poi{
    private boolean is_reference_point;

    public Attraction(String name, String schedule, String language, String description, String transport, String price, String site, String email, String address, double latitude, double longitude, boolean is_active, int timestamp, boolean has_accessibility, ArrayList<String> type, ArrayList<Photo> photos, double rating, ArrayList<Comment> comments, boolean is_reference_point) {
        super(name, schedule, language, description, transport, price, site, email, address, latitude, longitude, is_active, timestamp, has_accessibility, type, photos, rating, comments);
        this.is_reference_point = is_reference_point;
    }

    public Attraction(Poi p, boolean is_reference_point) {
        super(p);
        this.is_reference_point = is_reference_point;
    }


    public boolean isIs_reference_point() {
        return is_reference_point;
    }

    public void setIs_reference_point(boolean is_reference_point) {
        this.is_reference_point = is_reference_point;
    }
}
