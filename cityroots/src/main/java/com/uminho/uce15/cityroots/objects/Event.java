package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Event extends Poi{
    private String start;
    private String end;
    private String organization;
    private String program;
    private String price;

    public Event(String name, String schedule, String language, String description, String transport, String price, String site, String email, String address, double latitude, double longitude, boolean is_active, int timestamp, boolean has_accessibility, ArrayList<String> type, ArrayList<Photo> photos, double rating, ArrayList<Comment> comments, String start, String end, String organization, String program) {
        super(name, schedule, language, description, transport, site, email, address, latitude, longitude, is_active, timestamp, has_accessibility, type, photos, rating, comments);
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.program = program;
        this.price = price;
    }

    public Event(Poi p, String start, String end, String organization, String program, String price) {
        super(p);
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.program = program;
        this.price = price;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getPrice() {return price;}

    public void setPrice(String price) {this.price = price;}
}
