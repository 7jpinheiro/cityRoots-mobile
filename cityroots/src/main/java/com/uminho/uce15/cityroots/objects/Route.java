package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Route {
    private String name;
    private String description;
    private ArrayList<Poi> pois;

    public Route(String name, String description, ArrayList<Poi> pois) {
        this.name = name;
        this.description = description;
        this.pois = pois;
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

    public ArrayList<Poi> getPois() {
        return pois;
    }

    public void setPois(ArrayList<Poi> pois) {
        this.pois = pois;
    }
}
