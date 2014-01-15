package com.uminho.uce15.cityroots;

import android.util.Log;

import com.uminho.uce15.cityroots.objects.*;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    private String uriBase;

    public DataProvider() {
        uriBase = "http://193.136.19.202:8080/";

    }


    public Poi createPoiFromJson(JSONObject jObj, String type) throws JSONException {

        int trpt_index = 0;

        String name        = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("name");
        String schedule    = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("schedule");
        String language    = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("schedule");
        String description = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("description");
        String transport   = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("transport");
        String price       = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("price");
        String site        = jObj.getString("site");
        String email       = jObj.getString("email");
        String address     = jObj.getString("address");
        double latitude    = jObj.getDouble("latitude");
        double longitude   = jObj.getDouble("latitude");
        boolean is_active  = jObj.getBoolean("active");
        int timestamp      = jObj.getInt("timestamp");
        boolean has_accessibility = jObj.getBoolean("accessibility");


        ArrayList<String> types = new ArrayList<String>();
        JSONArray listTypes = jObj.getJSONArray("types");
        for( int i=0; i<listTypes.length(); i++){
            types.add( listTypes.getString(i) );
        }


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

            String userName    = joObj.getJSONObject("mobile_user").getString("firstname");
            String userSurname = joObj.getJSONObject("mobile_user").getString("surname");
            User u = new User(userName, userSurname);

            String comment     = joObj.getString("comment");

            comments.add( new Comment(comment, u ));
        }



        double rating      = jObj.getDouble("rating");


        return new Poi(name, schedule, language, description, transport, price, site, email, address,
                latitude, longitude, is_active, timestamp, has_accessibility, types, photos, rating, comments);
    }

    public List<Attraction> getAttractions(){
        //get JSON from webserver
        String poiType = "attraction";

        List<Attraction> res = new ArrayList<Attraction>();
        String uri = uriBase + poiType + "s.json";
        JSONArray jsonArray = null;
        try {
            jsonArray = getJSON(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for( int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Poi p = createPoiFromJson(jObj, poiType);

                boolean b = jObj.getBoolean("reference_point");

                Attraction a = new Attraction(p, b);
                res.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public List<Event> getEvents(){
        String poiType = "event";

        List<Event> res = new ArrayList<Event>();
        String uri = uriBase + poiType + "s.json";
        JSONArray jsonArray = null;
        try {
            jsonArray = getJSON(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for( int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Poi p = createPoiFromJson(jObj, poiType);

                String start        = jObj.getString("startdate");
                String end          = jObj.getString("enddate");
                String organization = jObj.getString("organization");
                String program      = jObj.getString("program");

                Event e = new Event(p, start, end, organization, program);
                res.add(e);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
        String poiType = "service";

        List<Service> res = new ArrayList<Service>();
        String uri = uriBase + poiType + "s.json";
        JSONArray jsonArray = null;
        try {
            jsonArray = getJSON(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for( int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Poi p = createPoiFromJson(jObj, poiType);

                // boolean is_reference_point, int capacity, String details ) {
                boolean is_reference_point = jObj.getBoolean("reference_point");
                int capacity = jObj.getInt("capacity");
                String details = jObj.getString("details");

                Service s = new Service(p, is_reference_point, capacity, details );
                res.add(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public JSONArray getJSON(String url) throws IOException {
        JSONArray jArr = new JSONArray( );
        String responseString;
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(url));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            responseString = out.toString();
        } else{
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }

        try {
            jArr = new JSONArray(responseString);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return jArr;
    }

}