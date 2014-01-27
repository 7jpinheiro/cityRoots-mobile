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
        }
        return res;
    }

    public List<Poi> getPontosRoute(int id){
        return new ArrayList<Poi>();
    }

    public List<Service> getServices(){
        List<Service> res = new ArrayList<Service>();
        try {
            res = cityRootsWebInterface.getServices();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        }
        return res;

    }

    public ArrayList<Event> getAds(){
        ArrayList<Event> res = new ArrayList<Event>();
        return res;
    }

}