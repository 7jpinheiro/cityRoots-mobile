package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Event extends Poi{
    private int start;
    private int end;
    private String organization;
    private String program;

    public Event(String name, String schedule, String language, String description, String transport, String price, String site, String email, String address, double latitude, double longitude, boolean is_active, int timestamp, boolean has_accessibility, ArrayList<String> type, ArrayList<Photo> photos, double rating, ArrayList<Comment> comments, int start, int end, String organization, String program) {
        super(name, schedule, language, description, transport, price, site, email, address, latitude, longitude, is_active, timestamp, has_accessibility, type, photos, rating, comments);
        this.start = start;
        this.end = end;
        this.organization = organization;
        this.program = program;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
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
}
