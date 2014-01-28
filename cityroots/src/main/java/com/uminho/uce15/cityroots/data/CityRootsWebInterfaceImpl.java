package com.uminho.uce15.cityroots.data;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CityRootsWebInterfaceImpl {

    public interface Callback<T> {

        void success(T t);

        void failure();
    }

    private CityRootsWebService service;
    private Context context;
    //private static final String TOKEN = "";

    public CityRootsWebInterfaceImpl(Context context) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setServer("http://193.136.19.202:8080")
                /* to add when authorization on the server is working

                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestInterceptor.RequestFacade request) {
                        request.addQueryParam("Authorization", TOKEN);
                    }
                })*/
                .build();

        this.context = context;

        this.service = restAdapter.create(CityRootsWebService.class);
    }

    private boolean isCached(String filename) throws IOException {
        File outputDir = this.context.getCacheDir();
        return (new File (outputDir, filename)).exists();
    }

    private void cache(String filename, Object object) throws IOException {
        File outputDir = this.context.getCacheDir();
        File file = new File(outputDir, filename);
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        oos.writeObject(object);
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

    public void getRoutes(final Callback<List<Route>> callback) throws IOException, ClassNotFoundException {
        if(isCached("routes")){
            callback.success((List<Route>)getCache("routes"));
        }
        else {
            service.getRoutes("PT", new retrofit.Callback<List<Route>>() {
                @Override
                public void success(List<Route> routes, Response response) {
                    try {
                        cache("routes",routes);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.success(routes);
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        }

    }

    public List<Route> getPagedRoutes(int page) {
        return service.getPagedRoutes(page);
    }

    public void getRouteWithId(int id, final Callback<Route> callback) throws IOException, ClassNotFoundException {
        if(isCached("routes")){
            List<Route> routes = (List<Route>)getCache("routes");
            for (Route route : routes) {
                if(route.getId() == id)
                    callback.success(route);
            }
        }
        service.getRouteWithId(id, new retrofit.Callback<Route>() {
            @Override
            public void success(Route route, Response response) {
                callback.success(route);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void getAttractions(final Callback<List<Attraction>> callback) throws IOException, ClassNotFoundException {
        if(isCached("attractions")){
            callback.success((List<Attraction>) getCache("attractions"));
        }
        else {
            service.getAttractions("PT", new retrofit.Callback<List<Attraction>>() {
                @Override
                public void success(List<Attraction> attractions, Response response) {
                    try {
                        cache("attractions",attractions);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.success(attractions);
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        }
    }

    public List<Attraction> getPagedAttractions(int page) {
        return service.getPagedAttractions(page);
    }

    public void getAttractionWithId(final int id, final Callback<Attraction> callback) throws IOException, ClassNotFoundException {
        if(isCached("attraction")){
            List<Attraction> attractions = (List<Attraction>)getCache("attractions");
            for (Attraction attraction : attractions) {
                if(attraction.getId() == id)
                    callback.success(attraction);
            }
        }

        service.getAttractionWithId(id, new retrofit.Callback<Attraction>() {
            @Override
            public void success(Attraction attraction, Response response) {
                callback.success(attraction);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void getServices(final Callback<List<Service>> callback) throws IOException, ClassNotFoundException {
        if(isCached("services")){
            callback.success((List<Service>)getCache("services"));
        }
        else {
            service.getServices("PT", new retrofit.Callback<List<Service>>() {
                @Override
                public void success(List<Service> services, Response response) {
                    try {
                        cache("services", services);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.success(services);
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        }

    }

    public List<Service> getPagedServices(int page){
        return service.getPagedServices(page);
    }

    public void getServiceWithId(int id, final Callback<Service> callback) throws IOException, ClassNotFoundException {
        if(isCached("services")){
            List<Service> services = (List<Service>)getCache("services");
            for (Service service : services) {
                if(service.getId() == id)
                    callback.success(service);
            }
        }
        
        service.getServiceWithId(id, new retrofit.Callback<Service>() {
            @Override
            public void success(Service service, Response response) {
                callback.success(service);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void getEvents(final Callback<List<Event>> callback) throws IOException, ClassNotFoundException {
        if(isCached("events")){
            callback.success((List<Event>) getCache("events"));
        }
        else {
            service.getEvents("PT", new retrofit.Callback<List<Event>>() {
                @Override
                public void success(List<Event> events, Response response) {
                    try {
                        cache("events", events);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    callback.success(events);
                }

                @Override
                public void failure(RetrofitError retrofitError) {

                }
            });
        }
    }

    public List<Event> getPagedEvents(int page){
        return service.getPagedEvents(page);
    }

    public void getEventWithId(int id, final Callback<Event> callback) throws IOException, ClassNotFoundException {
        if(isCached("events")){
            List<Event> events = (List<Event>)getCache("events");
            for (Event event : events) {
                if(event.getId() == id)
                    callback.success(event);
            }
        }
        service.getEventWithId(id, new retrofit.Callback<Event>() {
            @Override
            public void success(Event event, Response response) {
                callback.success(event);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void signup(String email,
                       String username,
                       String password,
                       String firstname,
                       String surname,
                       char gender,
                       String dateOfBirth,
                       final Callback<Boolean> callback){
        service.signup(email, username, password, firstname, surname, gender, dateOfBirth, new retrofit.Callback() {
            @Override
            public void success(Object o, Response response) {
                callback.success(true);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    public void getAds(final Callback<List<Event>> callback) throws IOException, ClassNotFoundException {
        if(isCached("ads")){
             callback.success((List<Event>)getCache("ads"));
        }
        service.getAds("PT", new retrofit.Callback<List<Event>>() {
            @Override
            public void success(List<Event> events, Response response) {
                try {
                    cache("ads", events);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                callback.success(events);
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
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
