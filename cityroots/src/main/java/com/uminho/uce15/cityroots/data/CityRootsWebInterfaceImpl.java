package com.uminho.uce15.cityroots.data;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.List;
import java.util.Locale;
import org.apache.commons.io.FileUtils;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class CityRootsWebInterfaceImpl {

    private static final String MAP_NAME = "Mapnikpeq.zip";
    private static final String SERVER_NAME = "http://www.cityrootsapp.com:80";

    private CityRootsWebService service;
    private Context context;
    private String lang;

    public CityRootsWebInterfaceImpl(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer(SERVER_NAME)
                .build();

        this.context = context;
        lang = Locale.getDefault().getDisplayLanguage();


        //if(lang.equals("português")) lang = "PT";
        //else lang = "EN";
        lang = "PT";
        this.service = restAdapter.create(CityRootsWebService.class);
    }

    public class NoInternetConnectionError extends Throwable{};

    private boolean isCached(String filename) throws IOException {
        File outputDir = this.context.getCacheDir();
        return (new File (outputDir, filename)).exists();
    }

    private void cache(String filename, Object object) throws IOException {
        File outputDir = this.context.getCacheDir();
        File file = new File(outputDir, filename);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
        oos.flush();
        oos.close();
    }

    private Object getCache(String filename) throws IOException, ClassNotFoundException {
        File outputDir = this.context.getCacheDir();
        File file = new File(outputDir, filename);
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    public List<Route> getRoutes() throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("routes")){
            return (List<Route>)getCache("routes");
        }
        else {
            try {
                List<Route> routes = service.getRoutes("PT");
                cache("routes",routes);
                return routes;
            } catch(RetrofitError e){
                if (e.isNetworkError())
                    throw new NoInternetConnectionError();
                else
                    throw e;
            }
        }

    }

    public List<Route> getPagedRoutes(int page) throws NoInternetConnectionError {
        try {
            return service.getPagedRoutes(page);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public Route getRouteWithId(int id) throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("routes")){
            List<Route> routes = (List<Route>)getCache("routes");
            for (Route route : routes) {
                if(route.getId() == id)
                    return route;
            }
        }
        try{
            return service.getRouteWithId(id);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }

    }

    public List<Attraction> getAttractions() throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("attractions")){
            return (List<Attraction>)getCache("attractions");
        }
        else {
            try {
                List<Attraction> attractions = service.getAttractions(lang);
                cache("attractions",attractions);
                return attractions;
            } catch(RetrofitError e){
                if (e.isNetworkError())
                    throw new NoInternetConnectionError();
                else
                    throw e;
            }
        }
    }

    public List<Attraction> getPagedAttractions(int page) throws NoInternetConnectionError {
        try {
            return service.getPagedAttractions(page);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public Attraction getAttractionWithId(int id) throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("attractions")){
            List<Attraction> attractions = (List<Attraction>)getCache("attractions");
            for (Attraction attraction : attractions) {
                if(attraction.getId() == id)
                    return attraction;
            }
        }
        try {
            return service.getAttractionWithId(id);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public List<Service> getServices() throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("services")){
            return (List<Service>)getCache("services");
        }
        else {
            try {
                List<Service> services = service.getServices(lang);
                cache("services", services);
                return services;
            } catch(RetrofitError e){
                if (e.isNetworkError())
                    throw new NoInternetConnectionError();
                else
                    throw e;
            }
        }

    }

    public List<Service> getPagedServices(int page) throws NoInternetConnectionError {
        try {
            return service.getPagedServices(page);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public Service getServiceWithId(int id) throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("services")){
            List<Service> services = (List<Service>)getCache("services");
            for (Service service : services) {
                if(service.getId() == id)
                    return service;
            }
        }
        try{
            return service.getServiceWithId(id);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public List<Event> getEvents() throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("events")){
            return (List<Event>)getCache("events");
        }
        else {
            try {
                List<Event> events = service.getEvents(lang);
                cache("events", events);
                return events;
            } catch(RetrofitError e){
                if (e.isNetworkError())
                    throw new NoInternetConnectionError();
                else
                    throw e;
            }
        }
    }

    public List<Event> getPagedEvents(int page) throws NoInternetConnectionError {
        try {
            return service.getPagedEvents(page);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public Event getEventWithId(int id) throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("events")){
            List<Event> events = (List<Event>)getCache("events");
            for (Event event : events) {
                if(event.getId() == id)
                    return event;
            }
        }
        try {
            return service.getEventWithId(id);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public int signup(String email,
                      String service_id,
                      String firstname,
                      String surname) throws NoInternetConnectionError {
        try {
            Identification result =  service.signup(email, service_id, firstname, surname);
            return result.getId();
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public List<Event> getAds() throws IOException, ClassNotFoundException, NoInternetConnectionError {
        if(isCached("ads")){
            return (List<Event>)getCache("ads");
        }
        try {
            List<Event> events = service.getAds(lang);
            cache("ads", events);
            return events;
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public void commentAttraction(String attraction_id, String user_id,String userName, String comment, int rating) throws NoInternetConnectionError, IOException, ClassNotFoundException {
        try {
            service.commentAttraction(user_id, attraction_id, comment, rating);
            if(isCached("attractions")){
                List<Attraction> attractions = (List<Attraction>)getCache("attractions");
                for (Attraction attraction : attractions) {
                    if(attraction.getId() == Integer.parseInt(attraction_id))
                        attraction.addComment(new Comment(comment,userName,"-"));
                }
                cache("attractions",attractions);
            }
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public void commentEvent(String event_id, String user_id,String userName, String comment, int rating) throws NoInternetConnectionError, IOException, ClassNotFoundException {
        try {
            service.commentEvent(user_id, event_id, comment, rating);
            if(isCached("events")){
                List<Event> events = (List<Event>)getCache("events");
                for (Event event : events) {
                    if(event.getId() == Integer.parseInt(event_id))
                        event.addComment(new Comment(comment,userName,"-"));
                }
                cache("events",events);
            }
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public void commentService(String service_id, String user_id,String userName, String comment, int rating) throws NoInternetConnectionError, IOException, ClassNotFoundException {
        try {
            service.commentService(user_id, service_id, comment, rating);
            if(isCached("services")){
                List<Service> services = (List<Service>)getCache("services");
                for (Service service : services) {
                    if(service.getId() == Integer.parseInt(service_id))
                        service.addComment(new Comment(comment,userName,"-"));
                }
                cache("services",services);
            }
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public void commentRoute(String route_id, String user_id, String comment, int rating) throws NoInternetConnectionError {
        try {
            service.commentRoute(user_id, route_id, comment, rating);
        } catch(RetrofitError e){
            if (e.isNetworkError())
                throw new NoInternetConnectionError();
            else
                throw e;
        }
    }

    public void getOfflineMap() {
        try {
            String path = Environment.getExternalStorageDirectory() + File.separator + "OSMDroid";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(path, "maps.zip");
            FileUtils.copyURLToFile(new URL(CityRootsWebInterfaceImpl.SERVER_NAME + "/uploads/" + MAP_NAME), file);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void invalidateCache(){
        File cacheDir = context.getCacheDir();

        File file = new File(cacheDir, "routes");
        file.delete();

        file = new File(cacheDir, "attractions");
        file.delete();

        file = new File(cacheDir, "events");
        file.delete();

        file = new File(cacheDir, "services");
        file.delete();

        file = new File(cacheDir, "ads");
        file.delete();
    }
}
