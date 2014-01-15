package com.uminho.uce15.cityroots;

import android.os.AsyncTask;

import com.uminho.uce15.cityroots.objects.*;

import org.apache.http.client.HttpClient;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private String uriBase;

    public DataProvider() {
        uriBase = "http://193.136.19.202:8080/";

    }

    public List<Attraction> getAttractions(){
        //get JSON from webserver
        String uri = uriBase + "attractions.json";


        //parse


        //create list and return

        List<Attraction> res = new ArrayList<Attraction>();


        return res;
    }

    public List<Comment> getComments(int poi_id){

        //get JSON from webserver

        //parse


        //create list and return

        List<Comment> res = new ArrayList<Comment>();


        return res;
    }

    public List<Event> getEvents(){

        //get JSON from webserver

        //parse


        //create list and return

        List<Event> res = new ArrayList<Event>();


        return res;
    }

    public List<Photo> getPhotos(int poi_id){

        //get JSON from webserver

        //parse


        //create list and return

        List<Photo> res = new ArrayList<Photo>();


        return res;
    }

    public List<Poi> getPois(){

        //get JSON from webserver

        //parse


        //create list and return

        List<Poi> res = new ArrayList<Poi>();


        return res;
    }

    public List<Route> getRoutes(){

        //get JSON from webserver

        //parse


        //create list and return

        List<Route> res = new ArrayList<Route>();


        return res;
    }

    public List<Service> getServices(){

        //get JSON from webserver

        //parse


        //create list and return

        List<Service> res = new ArrayList<Service>();


        return res;
    }

    public List<User> getUsers(){

        //get JSON from webserver

        //parse


        //create list and return

        List<User> res = new ArrayList<User>();


        return res;
    }

    private class GetData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            StringBuilder dataBuilder = new StringBuilder();

            for( String dataSearchURL : params){
                //HttpClient dataClient
            }


            return null;
        }
    }

}