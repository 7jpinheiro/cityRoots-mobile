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
    List<Attraction> getAttractions(@Query("lang") String language);

    @GET("/attractions/{id}.json")
    Attraction getAttractionWithId(@Path("id") int id);

    @GET("/events.json")
    List<Event> getPagedEvents(@Query("page") int page);

    @GET("/apis.json?evt=1")
    List<Event> getEvents(@Query("lang") String language);

    @GET("/events/{id}.json")
    Event getEventWithId(@Path("id") int id);

    @GET("/services.json")
    List<Service> getPagedServices(@Query("page") int page);

    @GET("/apis.json?ser=1")
    List<Service> getServices(@Query("lang") String language);

    @GET("/services/{id}.json")
    Service getServiceWithId(@Path("id") int id);

    @GET("itineraries.json")
    List<Route> getPagedRoutes(@Query("page") int page);

    @GET("/apis.json?iti=1")
    List<Route> getRoutes(@Query("lang") String language);

    @GET("/itineraries/{id}.json")
    Route getRouteWithId(@Path("id") int id);

    @GET("/apis.json?registar=1")
    Identification signup(@Query("email") String email,
               @Query("username") String username,
               @Query("firstname") String firstname,
               @Query("surname") String surname);

    @GET("/apis.json?anun=1")
    List<Event> getAds(@Query("lang") String language);

    @GET("/apis.json?co=1&attr=1")
    CommentResponse commentAttraction(@Query("user") String user,
                           @Query("id") String id,
                           @Query("comentario") String comment,
                           @Query("rating") int rating);

    @GET("/apis.json?co=1&even=1")
    CommentResponse commentEvent(@Query("user") String user,
                                 @Query("id") String id,
                                 @Query("comentario") String comment,
                                 @Query("rating") int rating);

    @GET("/apis.json?co=1&serv=1")
    CommentResponse commentService(@Query("user") String user,
                                   @Query("id") String id,
                                   @Query("comentario") String comment,
                                   @Query("rating") int rating);

    @GET("/apis.json?co=1&itin=1")
    CommentResponse commentRoute(@Query("user") String user,
                                 @Query("id") String id,
                                 @Query("comentario") String comment,
                                 @Query("rating") int rating);

}
