package com.uminho.uce15.cityroots.data;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

public interface CityRootsWebService {

    @GET("/attractions.json")
    List<Attraction> getPagedAttractions(@Query("page") int page);

    @GET("/apis.json?att=1")
    void getAttractions(@Query("lang") String language, Callback<List<Attraction>> callback);

    @GET("/attractions/{id}.json")
    void getAttractionWithId(@Path("id") int id, Callback<Attraction> callback);

    @GET("/events.json")
    List<Event> getPagedEvents(@Query("page") int page);

    @GET("/apis.json?evt=1")
    void getEvents(@Query("lang") String language, Callback<List<Event>> callback);

    @GET("/events/{id}.json")
    void getEventWithId(@Path("id") int id, Callback<Event> callback);

    @GET("/services.json")
    List<Service> getPagedServices(@Query("page") int page);

    @GET("/apis.json?ser=1")
    void getServices(@Query("lang") String language, Callback<List<Service>> callback);

    @GET("/services/{id}.json")
    void getServiceWithId(@Path("id") int id, Callback<Service> callback);

    @GET("itineraries.json")
    List<Route> getPagedRoutes(@Query("page") int page);

    @GET("/apis.json?iti=1")
    void getRoutes(@Query("lang") String language, Callback<List<Route>> callback);

    @GET("/itineraries/{id}.json")
    void getRouteWithId(@Path("id") int id, Callback<Route> callback);

    @GET("/apis.json?registar=1")
    void signup(@Query("email") String email,
                @Query("username") String username,
                @Query("password") String password,
                @Query("firstname") String firstname,
                @Query("surname") String surname,
                @Query("gender") char gender,
                @Query("dateofbirth") String dateOfBirth,
                Callback callback);

    @GET("/apis.json?anun=1")
    void getAds(@Query("lang") String language, Callback<List<Event>> callback);

}
