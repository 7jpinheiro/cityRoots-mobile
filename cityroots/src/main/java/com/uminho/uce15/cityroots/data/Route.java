package com.uminho.uce15.cityroots.data;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {

    private int id;
    private String name;
    private String description;
    private List<Attraction> attractions;
    private List<Event> events;
    private List<Service> services;

    public Route(int id, String name, String description, List<Attraction> attractions, List<Event> events, List<Service> services) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.attractions = attractions;
        this.events = events;
        this.services = services;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Attraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<Attraction> attractions) {
        this.attractions = attractions;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(List<Service> services) {
        this.services = services;
    }
}
