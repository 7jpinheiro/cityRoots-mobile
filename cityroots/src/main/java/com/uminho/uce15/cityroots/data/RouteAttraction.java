package com.uminho.uce15.cityroots.data;

public class RouteAttraction {

    private int order;
    private Attraction attraction;

    public RouteAttraction(int order, Attraction attraction) {
        this.order = order;
        this.attraction = attraction;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }
}
