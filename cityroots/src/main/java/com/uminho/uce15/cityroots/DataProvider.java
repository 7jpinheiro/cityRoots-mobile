package com.uminho.uce15.cityroots;

import android.os.AsyncTask;
import android.util.Log;

import com.uminho.uce15.cityroots.objects.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private String uriBase;

    public DataProvider() {
        uriBase = "http://193.136.19.202:8080/";

    }


    public Poi createPoiFromJson(JSONObject jObj) throws JSONException {

        int trpt_index = 0;

        String name        = jObj.getJSONArray("attraction_translations").getJSONObject(trpt_index).getString("name");
        String schedule    = jObj.getJSONArray("attraction_translations").getJSONObject(trpt_index).getString("schedule");
        String language    = jObj.getJSONArray("attraction_translations").getJSONObject(trpt_index).getString("schedule");
        String description = jObj.getJSONArray("attraction_translations").getJSONObject(trpt_index).getString("description");
        String transport   = jObj.getJSONArray("attraction_translations").getJSONObject(trpt_index).getString("transport");
        String price       = jObj.getJSONArray("attraction_translations").getJSONObject(trpt_index).getString("price");
        String site        = jObj.getString("site");
        String email       = jObj.getString("email");
        String address     = jObj.getString("address");
        double latitude    = jObj.getDouble("latitude");
        double longitude   = jObj.getDouble("latitude");
        boolean is_active  = jObj.getBoolean("active");
        int timestamp      = jObj.getInt("timestamp");
        boolean has_accessibility = jObj.getBoolean("accessibility");
        String type        = "notype";


        ArrayList<Photo> photos = new ArrayList<Photo>();
        JSONArray listPhotos = jObj.getJSONArray("photo_attractions");

        for( int i=0; i<listPhotos.length(); i++){
            JSONObject joObj = listPhotos.getJSONObject(i);
            String photoName = joObj.getString("name");
            String photoFile = joObj.getString("photo_file_name");

            photos.add( new Photo( photoName, photoFile ) );
        }


        ArrayList<Comment> comments = new ArrayList<Comment>();
        JSONArray listComments = jObj.getJSONArray("comment_attractions");

        for( int i=0; i<listComments.length(); i++){
            JSONObject joObj = listComments.getJSONObject(i);
            String comment     = joObj.getString("comment");
            String userName    = joObj.getJSONObject("mobile_user").getString("firstname");
            String userSurname = joObj.getJSONObject("mobile_user").getString("surname");

            comments.add( null );
        }



        double rating      = jObj.getDouble("rating");


        return new Poi(name, schedule, language, description, transport, price, site, email, address,
                latitude, longitude, is_active, timestamp, has_accessibility, type, photos, rating);
    }

    public List<Attraction> getAttractions(){
        //get JSON from webserver
        List<Attraction> res = new ArrayList<Attraction>();
        String uri = uriBase + "attractions.json";
        JSONArray jsonAttractions = getJSONFromUrl( uri );

        for( int i=0; i<jsonAttractions.length(); i++){
            try {
                JSONObject jObj = jsonAttractions.getJSONObject(i);

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //res.add(tmp);
        }
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

    public JSONArray getJSONFromUrl(String url) {
        InputStream is = null;
        JSONArray jObj = null;
        String json = "";
        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }
        // try parse the string to a JSON object
        try {
            jObj = new JSONArray(json);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        // return JSON String
        return jObj;
    }

}