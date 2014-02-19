package com.uminho.uce15.cityroots;

import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.provider.ContactsContract;

import com.facebook.AccessToken;
import com.uminho.uce15.cityroots.data.Attraction;
import com.uminho.uce15.cityroots.data.Event;
import com.uminho.uce15.cityroots.data.Poi;
import com.uminho.uce15.cityroots.data.Route;
import com.uminho.uce15.cityroots.data.RouteAttraction;
import com.uminho.uce15.cityroots.data.RouteEvent;
import com.uminho.uce15.cityroots.data.RouteService;
import com.uminho.uce15.cityroots.data.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.uminho.uce15.cityroots.data.CityRootsWebInterfaceImpl;

public class DataProvider {
    private String uriBase;
    private CityRootsWebInterfaceImpl cityRootsWebInterface;

    public DataProvider(Context context) {
        cityRootsWebInterface = new CityRootsWebInterfaceImpl(context);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public List<Attraction> getAttractions(){
        List<Attraction> res = new ArrayList<Attraction>();
        try {
            res = cityRootsWebInterface.getAttractions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }

    public List<Attraction> getAttractions(String tipo){

        List<Attraction> tmp = new ArrayList<Attraction>();
        try {
            tmp = cityRootsWebInterface.getAttractions();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }

        List<Attraction> res = new ArrayList<Attraction>();
        for(Attraction attraction : tmp){
            if( attraction.getType().contains(tipo) )
                res.add(attraction);
        }

        return res;
    }

    public List<Event> getEvents(){

        List<Event> res = null;
        try {
            res = cityRootsWebInterface.getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }

    public List<Event> getEvents(String tipo){

        List<Event> tmp = new ArrayList<Event>();
        try {
            tmp = cityRootsWebInterface.getEvents();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }

        List<Event> res = new ArrayList<Event>();
        for(Event event : tmp){
            if( event.getType().contains(tipo) )
                res.add(event);
        }

        return res;
    }

    public List<Route> getRoutes(){
        List<Route> res = new ArrayList<Route>();
        try {
            res = cityRootsWebInterface.getRoutes();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }

    public ArrayList<Poi> getPontosRoute(int id){
        Route res = null;
        try {
            res = cityRootsWebInterface.getRouteWithId(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }

        List<RouteAttraction> list_attractions = new ArrayList();
        List<RouteEvent> list_events = new ArrayList();
        List<RouteService> list_services = new ArrayList();

        list_attractions =(ArrayList<RouteAttraction>) res.getAttractions();
        list_events =(ArrayList<RouteEvent>) res.getEvents();
        list_services =(ArrayList<RouteService>) res.getServices();

        ArrayList<Poi> list_result = new ArrayList<Poi>();


        for(RouteAttraction rattraction : list_attractions)
            list_result.add(rattraction.getAttraction());
        for(RouteEvent revent : list_events)
            list_result.add(revent.getEvent());
        for(RouteService rservice : list_services)
            list_result.add(rservice.getService());
       
        return list_result;
    }

    public List<Service> getServices(){
        List<Service> res = new ArrayList<Service>();
        try {
            res = cityRootsWebInterface.getServices();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }

    public List<Service> getServices(String tipo){

        List<Service> tmp = new ArrayList<Service>();
        try {
            tmp = cityRootsWebInterface.getServices();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }

        List<Service> res = new ArrayList<Service>();
        for(Service service : tmp){
            if( service.getType().contains(tipo) )
                res.add(service);
        }

        return res;
    }

    public Attraction getAttraction (int id){
        Attraction res = null;
        try {
            res = cityRootsWebInterface.getAttractionWithId(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }


    public Event getEvent (int id){
        Event res = null;
        try {

            res = cityRootsWebInterface.getEventWithId(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }

    public Service getService (int id){
        Service res = null;
        try {
            res = cityRootsWebInterface.getServiceWithId(id);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;

    }

    public List<Event> getAds(){
        List<Event> res = null;
        try {
            res = cityRootsWebInterface.getAds();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
            noInternetConnectionError.printStackTrace();
        }
        return res;
    }

    public void sendComment(String poi_id, String user_id, String comment, int rating,int poi_type) throws CityRootsWebInterfaceImpl.NoInternetConnectionError {
        switch (poi_type){
            case 0:
                cityRootsWebInterface.commentAttraction(poi_id,user_id,comment,rating);
            break;
            case 1:
                cityRootsWebInterface.commentEvent(poi_id,user_id,comment,rating);
            break;
            case 2:
                cityRootsWebInterface.commentService(poi_id, user_id, comment, rating);
            break;
        }
    }

    public int signup(String email,String service_id,String firstname,String surname) throws CityRootsWebInterfaceImpl.NoInternetConnectionError {

        return cityRootsWebInterface.signup(email,service_id,firstname,surname);

    }

    public void getOfflineMap() {
        cityRootsWebInterface.getOfflineMap();
    }

}