package com.uminho.uce15.cityroots.data;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import retrofit.RestAdapter;

public class CityRootsWebInterfaceImpl {

    private CityRootsWebService service;
    private Context context;

    public CityRootsWebInterfaceImpl(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer("http://193.136.19.202:8080")
                .build();

        this.context = context;

        this.service = restAdapter.create(CityRootsWebService.class);
    }

    private boolean isCached(String filename) throws IOException {
        File outputDir = this.context.getCacheDir();
        return File.createTempFile(filename,"obj",outputDir).exists();
    }

    private void cache(String filename, Object object) throws IOException {
        File outputDir = this.context.getCacheDir();
        File file = File.createTempFile(filename, "obj", outputDir);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
    }

    private Object getCache(String filename) throws IOException, ClassNotFoundException {
        File outputDir = this.context.getCacheDir();
        File file = File.createTempFile(filename, "obj", outputDir);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        return ois.readObject();
    }

    public List<Route> getRoutes() throws IOException, ClassNotFoundException {
        if(isCached("routes")){
            return (List<Route>)getCache("routes");
        }
        else {
            List<Route> routes = service.getRoutes("PT");
            cache("routes",routes);
            return routes;
        }

    }

    public List<Route> getPagedRoutes(int page) {
        return service.getPagedRoutes(page);
    }

    public Route getRouteWithId(int id) throws IOException, ClassNotFoundException {
        if(isCached("routes")){
            List<Route> routes = (List<Route>)getCache("routes");
            for (Route route : routes) {
                if(route.getId() == id)
                    return route;
            }
        }
        return service.getRouteWithId(id);
    }

    public List<Attraction> getAttractions() throws IOException, ClassNotFoundException {
        if(isCached("attractions")){
            return (List<Attraction>)getCache("attractions");
        }
        else {
            List<Attraction> attractions = service.getAttractions("PT");
            cache("attractions",attractions);
            return attractions;
        }
    }

    public List<Attraction> getPagedAttractions(int page) {
        return service.getPagedAttractions(page);
    }

    public Attraction getAttractionWithId(int id) throws IOException, ClassNotFoundException {
        if(isCached("attraction")){
            List<Attraction> attractions = (List<Attraction>)getCache("attractions");
            for (Attraction attraction : attractions) {
                if(attraction.getId() == id)
                    return attraction;
            }
        }
        return service.getAttractionWithId(id);
    }

    public List<Service> getServices() throws IOException, ClassNotFoundException {
        if(isCached("services")){
            return (List<Service>)getCache("services");
        }
        else {
            List<Service> services = service.getServices("PT");
            cache("services", services);
            return services;
        }

    }

    public List<Service> getPagedServices(int page){
        return service.getPagedServices(page);
    }

    public Service getServiceWithId(int id) throws IOException, ClassNotFoundException {
        if(isCached("services")){
            List<Service> services = (List<Service>)getCache("services");
            for (Service service : services) {
                if(service.getId() == id)
                    return service;
            }
        }
        return service.getServiceWithId(id);
    }

    public List<Event> getEvents() throws IOException, ClassNotFoundException {
        if(isCached("events")){
            return (List<Event>)getCache("events");
        }
        else {
            List<Event> events = service.getEvents("PT");
            cache("events", events);
            return events;
        }
    }

    public List<Event> getPagedEvents(int page){
        return service.getPagedEvents(page);
    }

    public Event getEventWithId(int id) throws IOException, ClassNotFoundException {
        if(isCached("events")){
            List<Event> events = (List<Event>)getCache("events");
            for (Event event : events) {
                if(event.getId() == id)
                    return event;
            }
        }
        return service.getEventWithId(id);
    }
}
