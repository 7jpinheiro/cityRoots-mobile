package com.uminho.uce15.cityroots.data;

import java.io.Serializable;
import java.util.List;

public class Route implements Serializable {

    private int id;
    private String name;
    private String description;
    private List<RouteAttraction> attractions;
    private List<RouteEvent> events;
    private List<RouteService> services;

    public Route(int id,
                 String name,
                 String description,
                 List<RouteAttraction> attractions,
                 List<RouteEvent> events,
                 List<RouteService> services) {
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

    public List<RouteAttraction> getAttractions() {
        return attractions;
    }

    public void setAttractions(List<RouteAttraction> attractions) {
        this.attractions = attractions;
    }

    public List<RouteEvent> getEvents() {
        return events;
    }

    public void setEvents(List<RouteEvent> events) {
        this.events = events;
    }

    public List<RouteService> getServices() {
        return services;
    }

    public void setServices(List<RouteService> services) {
        this.services = services;
    }
}
