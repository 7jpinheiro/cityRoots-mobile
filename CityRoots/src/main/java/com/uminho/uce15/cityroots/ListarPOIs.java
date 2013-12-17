package com.uminho.uce15.cityroots;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import android.location.Location;
import android.location.LocationManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class ListarPOIs extends ActionBarActivity {

    private LocationManager locMan;
    private Marker userMarker;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;
    private MarkerOptions[] places;

    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
    private GoogleMap theMap=null;

    private ListView list;
    private Lista adapter;

    String[] web =null;
    Integer[] imageId = {
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images,
            R.drawable.images
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_listar_pois);

        userIcon = R.drawable.mark_blue;
        //foodIcon = R.drawable.red_point;
        //drinkIcon = R.drawable.blue_point;
        //shopIcon = R.drawable.green_point;
        otherIcon = R.drawable.green_point;

        if(theMap==null){
            //map not instantiated yet
            SupportMapFragment mapFrag=
                    (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapa);
            theMap=mapFrag.getMap();

        }
        if(theMap != null){
            //ok - proceed
            theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            placeMarkers = new Marker[MAX_PLACES];
            updatePlaces();
        }

        list=(ListView)findViewById(R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));

        /*String readBD = readBD();

        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(readBD);
        } catch (JSONException e) {
            e.printStackTrace();
        }
*/
        //adapter = new Lista(this,web,imageId);
        //list.setAdapter(adapter);


    }

    private void updatePlaces(){
        if(userMarker!=null) userMarker.remove();
        //update location
        //locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //double lat = lastLoc.getLatitude();
        //double lng = lastLoc.getLongitude();
        double lat=41.561653;
        double lng=-8.397139;

        LatLng lastLatLng = new LatLng(lat, lng);

        userMarker = theMap.addMarker(new MarkerOptions()
                .position(lastLatLng)
                .title("Está aqui")
                .icon(BitmapDescriptorFactory.fromResource(userIcon))
                .snippet("Sua última localização"));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(lastLatLng)
                .zoom(12)                   // Sets the zoom
                .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder

        theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));




        //theMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);

        String types = "food|bar|store|museum|art_gallery";
        try {
            types = URLEncoder.encode(types, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
		/*String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/" +
		"json?location="+lat+","+lng+
		"&radius=1000&sensor=true" +
		"&types=" + types +
		"&key=AIzaSyCSbIjUOgbOhQ9JJ4njs3hyPkdkyhXBxnQ";*/

        String placesSearchStr = "http://193.136.19.202:8080/attractions.json";

        //String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=41.561653,-8.397139&radius=1000&sensor=true&types=bar&key=AIzaSyCSbIjUOgbOhQ9JJ4njs3hyPkdkyhXBxnQ";

        new GetPlaces().execute(placesSearchStr);
        //locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
    }

    private class GetPlaces extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... placesURL) {
            //fetch places

            //build result as string
            StringBuilder placesBuilder = new StringBuilder();
            //process search parameter string(s)
            for (String placeSearchURL : placesURL) {
                HttpClient placesClient = new DefaultHttpClient();
                try {
                    //try to fetch the data

                    //HTTP Get receives URL string
                    HttpGet placesGet = new HttpGet(placeSearchURL);
                    //execute GET with Client - return response
                    HttpResponse placesResponse = placesClient.execute(placesGet);
                    //check response status
                    StatusLine placeSearchStatus = placesResponse.getStatusLine();
                    //only carry on if response is OK
                    if (placeSearchStatus.getStatusCode() == 200) {
                        //get response entity
                        HttpEntity placesEntity = placesResponse.getEntity();
                        //get input stream setup
                        InputStream placesContent = placesEntity.getContent();
                        //create reader
                        InputStreamReader placesInput = new InputStreamReader(placesContent);
                        //use buffered reader to process
                        BufferedReader placesReader = new BufferedReader(placesInput);
                        //read a line at a time, append to string builder
                        String lineIn;
                        while ((lineIn = placesReader.readLine()) != null) {
                            placesBuilder.append(lineIn);
                        }
                    }
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }
            return placesBuilder.toString();
        }
        //process data retrieved from doInBackground
		/*protected void onPostExecute(String result) {
			//parse place data returned from Google Places
			//remove existing markers
			if(placeMarkers!=null){
				for(int pm=0; pm<placeMarkers.length; pm++){
					if(placeMarkers[pm]!=null)
						placeMarkers[pm].remove();
				}
			}
			try {
				//parse JSON

				//create JSONObject, pass stinrg returned from doInBackground
				JSONObject resultObject = new JSONObject(result);
				//get "results" array
				JSONArray placesArray = resultObject.getJSONArray("results");
				//marker options for each place returned
				places = new MarkerOptions[placesArray.length()];
				//loop through places
				for (int p=0; p<placesArray.length(); p++) {
					//parse each place
					//if any values are missing we won't show the marker
					boolean missingValue=false;
					LatLng placeLL=null;
					String placeName="";
					String vicinity="";
					int currIcon = otherIcon;
					try{
						//attempt to retrieve place data values
						missingValue=false;
						//get place at this index
						JSONObject placeObject = placesArray.getJSONObject(p);
						//get location section
						JSONObject loc = placeObject.getJSONObject("geometry")
								.getJSONObject("location");
						//read lat lng
						placeLL = new LatLng(Double.valueOf(loc.getString("lat")),
								Double.valueOf(loc.getString("lng")));
						//get types
						JSONArray types = placeObject.getJSONArray("types");
						//loop through types
						for(int t=0; t<types.length(); t++){
							//what type is it
							String thisType=types.get(t).toString();
							//check for particular types - set icons
							if(thisType.contains("food")){
								currIcon = foodIcon;
								break;
							}
							else if(thisType.contains("bar")){
								currIcon = drinkIcon;
								break;
							}
							else if(thisType.contains("store")){
								currIcon = shopIcon;
								break;
							}
						}
						//vicinity
						vicinity = placeObject.getString("vicinity");
						//name
						placeName = placeObject.getString("name");
					}
					catch(JSONException jse){
						Log.v("PLACES", "missing value");
						missingValue=true;
						jse.printStackTrace();
					}
					//if values missing we don't display
					if(missingValue)	places[p]=null;
					else
						places[p]=new MarkerOptions()
					.position(placeLL)
					.title(placeName)
					.icon(BitmapDescriptorFactory.fromResource(currIcon))
					.snippet(vicinity);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			if(places!=null && placeMarkers!=null){
				for(int p=0; p<places.length && p<placeMarkers.length; p++){
					//will be null if a value was missing
					if(places[p]!=null)
						placeMarkers[p]=theMap.addMarker(places[p]);
				}
			}

		}*/

        protected void onPostExecute(String result) {
            //parse place data returned from Google Places
            //remove existing markers
            if(placeMarkers!=null){
                for(int pm=0; pm<placeMarkers.length; pm++){
                    if(placeMarkers[pm]!=null)
                        placeMarkers[pm].remove();
                }
            }
            try {
                //parse JSON

                //create JSONObject, pass stinrg returned from doInBackground
                JSONArray placesArray = new JSONArray(result);
                //get "results" array
                //marker options for each place returned
                web=new String[placesArray.length()];
                places = new MarkerOptions[placesArray.length()];
                //loop through places
                for (int p=0; p<placesArray.length(); p++) {
                    //parse each place
                    //if any values are missing we won't show the marker
                    boolean missingValue=false;
                    LatLng placeLL=null;
                    String placeName="";
                    String vicinity="";
                    int currIcon = otherIcon;
                    try{
                        //attempt to retrieve place data values
                        missingValue=false;
                        //get place at this index
                        JSONObject placeObject = placesArray.getJSONObject(p);
                        //get location section
                        //read lat lng
                        placeLL = new LatLng(Double.valueOf(placeObject.getString("latitude")),
                                Double.valueOf(placeObject.getString("longitude")));

                        vicinity =placeObject.getString("address");
                        //name
                        placeName = placeObject.getString("name");
                        web[p]=placeObject.getString("name");
                    }
                    catch(JSONException jse){
                        Log.v("PLACES", "missing value");
                        missingValue=true;
                        jse.printStackTrace();
                    }
                    //if values missing we don't display
                    if(missingValue)	places[p]=null;
                    else
                        places[p]=new MarkerOptions()
                                .position(placeLL)
                                .title(placeName)
                                .icon(BitmapDescriptorFactory.fromResource(currIcon))
                                .snippet(vicinity);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            if(places!=null && placeMarkers!=null){
                for(int p=0; p<places.length && p<placeMarkers.length; p++){
                    //will be null if a value was missing
                    if(places[p]!=null)
                        placeMarkers[p]=theMap.addMarker(places[p]);
                }
            }

            adapter = new Lista(ListarPOIs.this,web,imageId);
            list.setAdapter(adapter);
        }
    }

    public String readBD() {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet("http://193.136.19.202:8080/attractions.json");
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                Log.e(ListarPOIs.class.toString(), "Failed to download file");
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.listar_pois, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClickPOI(View view){
        Intent intent = new Intent(this, DetalhesPOI.class);
        startActivity(intent);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_listar_pois, container, false);
            return rootView;
        }
    }

    public void onLocationChanged(Location location) {
        Log.v("MainActivity", "location changed");
        updatePlaces();
    }
    public void onProviderDisabled(String provider){
        Log.v("MainActivity", "provider disabled");
    }
    public void onProviderEnabled(String provider) {
        Log.v("MainActivity", "provider enabled");
    }
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.v("MainActivity", "status changed");
    }


	/*@Override
	protected void onResume() {
		super.onResume();
		if(theMap!=null){
			locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(theMap!=null){
			locMan.removeUpdates(this);
		}
	}*/

}
