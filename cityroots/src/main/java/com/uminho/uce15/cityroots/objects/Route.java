package com.uminho.uce15.cityroots.objects;

import java.util.ArrayList;

public class Route {
    private int id;
    private String name;
    private String description;
    private ArrayList<Poi> pois;

    public Route(int id,String name, String description, ArrayList<Poi> pois) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pois = pois;
    }

    public int getId() {
        return id;
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
