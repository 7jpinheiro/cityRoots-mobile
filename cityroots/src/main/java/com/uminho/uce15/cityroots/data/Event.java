package com.uminho.uce15.cityroots.data;

import java.io.Serializable;
import java.util.List;

public class Event extends Poi implements Serializable {

    private int id;
    private String start;
    private String end;
    private String organization;
    private String program;
    private String price;

    public Event(String name, String schedule, String description, String transport, String site, String email, String address, double latitude, double longitude, boolean isActive, int timestamp, String phone, boolean hasAccessibility, List<String> type, List<String> photos, double rating, List<Comment> comments, int id, String start, String end, String organization, String program, String price) {
        super(name, schedule, description, transport, site, email, address, latitude, longitude, isActive, timestamp, phone, hasAccessibility, type, photos, rating, comments);
        this.id = id;
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.program = program;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
