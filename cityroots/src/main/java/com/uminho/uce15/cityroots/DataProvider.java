package com.uminho.uce15.cityroots;

import android.os.AsyncTask;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DataProvider {
    private String uriBase;

    private List<Route> rotas = new ArrayList<Route>();

    public DataProvider() {
        uriBase = "http://193.136.19.202:8080/";

        ArrayList pontos=(ArrayList<Event>)this.getEvents();
        ArrayList pontos2=(ArrayList<Attraction>)this.getAttractions("Tradicional");
        Route a= new Route(1,"Rota1","RotaExemplo1",pontos);
        Route b= new Route(2,"Rota2","RotaExemplo2",pontos2);
        Route c= new Route(3,"Rota3","RotaExemplo3",pontos);

        rotas.add(a);
        rotas.add(b);
        rotas.add(c);
    }


    public Poi createPoiFromJson(JSONObject jObj, String type) throws JSONException {

        int trpt_index = 0;

        int id             = jObj.getInt("id");
        String name        = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("name");
        String schedule    = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("schedule");
        String language    = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("schedule");
        String description = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("description");
        String transport   = jObj.getJSONArray(type + "_translations").getJSONObject(trpt_index).getString("transport");

        String site        = jObj.getString("site");
        String email       = jObj.getString("email");
        String address     = jObj.getString("address");
        double latitude;
        try{
            latitude    = jObj.getDouble("latitude");
        }
        catch (Exception e){
            latitude = 0.0;
        }
        double longitude;
        try{
            longitude    = jObj.getDouble("longitude");
        }
        catch (Exception e){
            longitude = 0.0;
        }
        
        boolean is_active  = jObj.getBoolean("active");
        int timestamp      = jObj.getInt("timestamp");
        boolean has_accessibility = jObj.getBoolean("accessibility");


        ArrayList<String> types = new ArrayList<String>();
        JSONArray listTypes = jObj.getJSONArray("types");
        for( int i=0; i<listTypes.length(); i++){
            types.add( listTypes.getJSONObject(i).getString("name") );
        }


        ArrayList<Photo> photos = new ArrayList<Photo>();

        JSONArray listPhotos = jObj.getJSONArray("photo_" + type + "s");

        for( int i=0; i<listPhotos.length(); i++){
            JSONObject joObj = listPhotos.getJSONObject(i);
            String photoName = joObj.getString("name");
            String photoFile = joObj.getString("photo_file_name");

            photos.add( new Photo( photoName, photoFile ) );
        }


        ArrayList<Comment> comments = new ArrayList<Comment>();
        /*JSONArray listComments = jObj.getJSONArray("comment_" + type + "s");


        for( int i=0; i<listComments.length(); i++){
            JSONObject joObj = listComments.getJSONObject(i);

            String userName    = joObj.getJSONObject("mobile_user").getString("firstname");
            String userSurname = joObj.getJSONObject("mobile_user").getString("surname");
            User u = new User(userName, userSurname);

            String comment     = joObj.getString("comment");

            comments.add( new Comment(comment, u ));
        }*/



        double rating      = jObj.getDouble("rating");


        return new Poi(id,name, schedule, language, description, transport, site, email, address,
                latitude, longitude, is_active, timestamp, has_accessibility, types, photos, rating, comments);
    }

    public List<Attraction> getAttractions(){
        //get JSON from webserver
        String poiType = "attraction";

        List<Attraction> res = new ArrayList<Attraction>();
        String uri = uriBase + poiType + "s.json";
        JSONArray jsonArray = null;
        try {

            jsonArray = (new GetContentTask()).execute(uri).get();

        for( int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Poi p = createPoiFromJson(jObj, poiType);
                String price = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("price");
                boolean b = jObj.getBoolean("reference_point");

                Attraction a = new Attraction(p,b,price);
                res.add(a);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public List<Attraction> getAttractions(String tipo){
        //get JSON from webserver
        String poiType = "attraction";

        List<Attraction> res = new ArrayList<Attraction>();
        String uri = uriBase + poiType + "s.json";
        JSONArray jsonArray = null;
        try {

            jsonArray = (new GetContentTask()).execute(uri).get();

            for( int i=0; i<jsonArray.length(); i++){
                try {
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Poi p = createPoiFromJson(jObj, poiType);
                    String price = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("price");
                    boolean b = jObj.getBoolean("reference_point");

                    Attraction a = new Attraction(p,b,price);

                    if(a.getType().contains(tipo))
                    res.add(a);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public List<Event> getEvents(){
        String poiType = "event";

        List<Event> res = new ArrayList<Event>();
        String uri = uriBase + poiType + "s.json";

        JSONArray jsonArray = null;
        try {
            jsonArray = (new GetContentTask()).execute(uri).get();


        for( int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Poi p = createPoiFromJson(jObj, poiType);

                String start        = jObj.getString("startdate");
                String end          = jObj.getString("enddate");
                String organization = jObj.getString("organization");
                String program      = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("program");
                String price = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("price");

                Event e = new Event(p, start, end, organization, program,price);
                res.add(e);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public List<Route> getRoutes(){

        //get JSON from webserver

        //parse


        //create list and return

        //List<Route> res = new ArrayList<Route>();
        return rotas;
    }

    public List<Poi> getPontosRoute(int id){

        ArrayList<Poi> res = new ArrayList<Poi>();
        //create list and return
        System.out.println("Tamanho Rotas:"+rotas.size());
        System.out.println("id:"+id);
        for(int i=0;i<rotas.size();i++){
            System.out.println("pos:"+i+"id:"+id);
            if(rotas.get(i).getId()==id){
                System.out.println("Tamanho Pontos:"+rotas.get(i).getPois().size());
                return rotas.get(i).getPois();}
        }
        return res;
    }

    public List<Service> getServices(){
        String poiType = "service";

        List<Service> res = new ArrayList<Service>();
        String uri = uriBase + poiType + "s.json";
        JSONArray jsonArray = null;

        try {
            jsonArray = (new GetContentTask()).execute(uri).get();


        for( int i=0; i<jsonArray.length(); i++){
            try {
                JSONObject jObj = jsonArray.getJSONObject(i);
                Poi p = createPoiFromJson(jObj, poiType);

                boolean is_reference_point = jObj.getBoolean("reference_point");
                int capacity = 0;
                try{
                    capacity=jObj.getInt("capacity");
                }
                catch ( Exception e )
                {
                    capacity=0;
                }

                String details = jObj.getString("details");

                Service s = new Service(p, is_reference_point, capacity, details );
                res.add(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return res;
    }

    public Attraction getAttraction (int id){
        Poi poi;
        Attraction attraction=null;
        System.out.println("ID:"+id);
        String poiType = "attraction";

        String uri = uriBase + poiType+ "s" + "/" + id + ".json";
        System.out.println(uri);
        JSONObject jObj = null;
        try {

            jObj = (new getObjJson()).execute(uri).get();

            try {

                 poi = createPoiFromJson(jObj, poiType);
                 String price = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("price");
                 boolean b = jObj.getBoolean("reference_point");

                 attraction = new Attraction(poi,b,price);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return attraction;
    }

    public Event getEvent (int id){
        Poi poi;
        Event event=null;

        String poiType = "event";

        List<Event> res = new ArrayList<Event>();
        String uri = uriBase + poiType + "s" + "/" + id + ".json";

        JSONObject jObj = null;
        try {
            jObj = (new getObjJson()).execute(uri).get();
            try {
                poi = createPoiFromJson(jObj, poiType);

                String start        = jObj.getString("startdate");
                String end          = jObj.getString("enddate");
                String organization = jObj.getString("organization");
                String program      = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("program");
                String price = jObj.getJSONArray(poiType + "_translations").getJSONObject(0).getString("price");

                event = new Event(poi, start, end, organization, program,price);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println(event.getName());
        System.out.println(event.getPrice());

        return event;
    }

    public Service getService (int id){
        Poi poi;
        Service service=null;

        String poiType = "service";

        String uri = uriBase + poiType + "s" + "/" + id + ".json";
        JSONObject jObj = null;

        try {
            jObj = (new getObjJson()).execute(uri).get();
                try {

                    Poi p = createPoiFromJson(jObj, poiType);

                    boolean is_reference_point = jObj.getBoolean("reference_point");
                    int capacity = 0;
                    try{
                        capacity=jObj.getInt("capacity");
                    }
                    catch ( Exception e )
                    {
                        capacity=0;
                    }

                    String details = jObj.getString("details");

                    service = new Service(p, is_reference_point, capacity, details );

                } catch (JSONException e) {
                    e.printStackTrace();
                }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return service;

    }

    public ArrayList<Poi> getAds(){
        ArrayList<Poi> pois = new ArrayList<Poi>();

        return pois;
    }


    private class GetContentTask extends AsyncTask<String, Void, JSONArray> {
        //@Override
        protected JSONArray  doInBackground(String ... urls){
            JSONArray jArr = new JSONArray( );

            String responseString=null;
            for (String url : urls) {
                HttpClient httpclient = new DefaultHttpClient();
                try {
                    HttpResponse response = null;
                    System.out.println(url);
                    response = httpclient.execute(new HttpGet(url));
                    System.out.println(response);

                    StatusLine statusLine = response.getStatusLine();
                    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                        ByteArrayOutputStream out = new ByteArrayOutputStream();

                        response.getEntity().writeTo(out);
                        out.close();


                        responseString = out.toString();
                        System.out.println("resp string::"+responseString);

                    } else{

                        response.getEntity().getContent().close();
                        throw new IOException(statusLine.getReasonPhrase());

                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            try {
                jArr = new JSONArray(responseString);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return jArr;
        }
    }

    private class getObjJson extends AsyncTask<String, Void, JSONObject> {
        //@Override
        protected JSONObject  doInBackground(String ... urls){
            JSONObject jobj = new JSONObject( );

            String responseString=null;
            for (String url : urls) {
                HttpClient httpclient = new DefaultHttpClient();
                try {
                    HttpResponse response = null;
                    response = httpclient.execute(new HttpGet(url));

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
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            try {
                jobj = new JSONObject(responseString);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            catch(Exception e){
                e.printStackTrace();
            }
            return jobj;
        }
    }

}