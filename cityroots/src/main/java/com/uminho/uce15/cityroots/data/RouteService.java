package com.uminho.uce15.cityroots.data;

public class RouteService {

    private int order;
    private Service service;

    public RouteService(int order, Service service) {
        this.order = order;
        this.service = service;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
