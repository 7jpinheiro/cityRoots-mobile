package com.uminho.uce15.cityroots;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SlidingPaneLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.uminho.uce15.cityroots.objects.Attraction;
import com.uminho.uce15.cityroots.objects.Event;
import com.uminho.uce15.cityroots.objects.Route;
import com.uminho.uce15.cityroots.objects.Service;

import org.json.JSONObject;

import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ListarPoi extends ActionBarActivity implements LocationListener,GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener{

    private GPSTracker gps;
    private LocationManager service;
    private String provider;
    private Location posicao;
    double lati=41.561653;
    double longi=-8.397139;
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

    // Handles to UI widgets
    private TextView mLatLng;
    private TextView mAddress;
    private ProgressBar mActivityIndicator;
    private TextView mConnectionState;
    private TextView mConnectionStatus;

    // Handle to SharedPreferences for this app
    SharedPreferences mPrefs;

    // Handle to a SharedPreferences editor
    SharedPreferences.Editor mEditor;

    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     *
     */
    boolean mUpdatesRequested = true;

    private LocationManager locMan;
    private Marker userMarker;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;
    private MarkerOptions[] places;

    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
    private GoogleMap theMap=null;

    private ListView list;
    private ListAdapter adapter;

    private Context old;
    MySlidingPaneLayout slidingPaneLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_listar_poi);

        ArrayList lista = null;
        slidingPaneLayout = (MySlidingPaneLayout)findViewById(R.id.pane);

        DataProvider provider = new DataProvider() ;

        Bundle b = getIntent().getExtras();
        int value = b.getInt("id_category");

        switch (value){
            case R.id.poi:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.routes:
                lista = (ArrayList<Route>) provider.getRoutes();
                break;
            case R.id.events:
                lista =(ArrayList<Event>) provider.getEvents();
                break;
            case R.id.tpa:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.gastronomy:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.activities:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.outdoor:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.nightlife:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.hotels:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.transport:
                lista = (ArrayList<Service>) provider.getServices();
                break;
            case R.id.afd:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                break;
            case R.id.contacts:
                lista = (ArrayList<Event>) provider.getEvents();
                break;
        }


        list=(ListView)findViewById(R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));


        if(lista!=null){
            adapter = new ListAdapter(ListarPoi.this,lista);
            list.setAdapter(adapter);
        }

        assert lista != null;


        /*// Get handles to the UI view objects
        mLatLng = (TextView) findViewById(R.id.lat_lng);
        mAddress = (TextView) findViewById(R.id.address);
        mActivityIndicator = (ProgressBar) findViewById(R.id.address_progress);
        mConnectionState = (TextView) findViewById(R.id.text_connection_state);
        mConnectionStatus = (TextView) findViewById(R.id.text_connection_status);*/


        mLocationClient = new LocationClient(this, this, this);
        // Create a new global location parameters object
        mLocationRequest = LocationRequest.create();

        /*
         * Set the update interval
         */
        mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        mUpdatesRequested = false;

        // Open Shared Preferences
        mPrefs = getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);

        // Get an editor
        mEditor = mPrefs.edit();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        userIcon = R.drawable.marcador;
        otherIcon = R.drawable.events;

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

        //GPS
        /*service = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(enabled)
        {System.out.println("GPS Ligado");
            Criteria criteria = new Criteria();
            provider = service.getBestProvider(criteria, false);
            posicao = service.getLastKnownLocation(provider);
        }
        else System.out.println("GPS Desligado");*/
        gps = new GPSTracker(this);
        //GPS----------------------------------------

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
              //Starting new intent
              //Intent intent = new Intent(ListarPoi.this, DetalhesPois.class);
              //intent.putExtra("id",lista[position].);
                //intent.putExtra("id",pos.toString());
              //  startActivity(intent);
            }
        });
    }

    public void goToList(View view){
        slidingPaneLayout.openPane();
    }

    private void updatePlaces(){
        if(userMarker!=null) userMarker.remove();
        //update location
        //locMan = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //Location lastLoc = locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //double lat = lastLoc.getLatitude();
        //double lng = lastLoc.getLongitude();


        double lat=lati;
        double lng=longi;


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

        //String placesSearchStr = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=41.561653,-8.397139&radius=1000&sensor=true&types=bar&key=AIzaSyCSbIjUOgbOhQ9JJ4njs3hyPkdkyhXBxnQ";

        //new GetPlaces().execute(placesSearchStr);
        //locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 30000, 100, this);
    }

   /* private class GetPlaces extends AsyncTask<String, Void, String>
        //process data retrieved from doInBackground
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
                pontos=new JSONObject[placesArray.length()];
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
                        JSONArray et=placeObject.getJSONArray("event_translations");
                        JSONObject pt=et.getJSONObject(0);
                        vicinity =pt.getString("description");
                        //name
                        placeName = pt.getString("name");
                        web[p]=pt.getString("name");
                        pontos[p]=placeObject;
                    }
                    catch(JSONException jse){
                        Log.v("JSON", "missing value");
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

            if(web!=null){adapter = new ListAdapter(ListarPoi.this,web,imageId);
            list.setAdapter(adapter);}
        }
    }
*/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.listar_eventos, menu);
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



    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_listar_eventos, container, false);
            return rootView;
        }
    }




    /*
     * Called when the Activity is no longer visible at all.
     * Stop updates and disconnect.
     */
    @Override
    public void onStop() {

        // If the client is connected
        if (mLocationClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        mLocationClient.disconnect();

        super.onStop();
    }
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {

        // Save the current setting for updates
        mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        mEditor.commit();
        //service.removeUpdates(this);
        super.onPause();
    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

        /*
         * Connect the client. Don't re-start any requests here;
         * instead, wait for onResume()
         */
        mLocationClient.connect();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        //service.requestLocationUpdates(provider, 400, 1,this);
        // If the app already has a setting for getting location updates, get it
        if (mPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = mPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);

            // Otherwise, turn off location updates until requested
        } else {
            mEditor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            mEditor.commit();
        }
    }

    /*
     * Handle results returned to this Activity by other Activities started with
     * startActivityForResult(). In particular, the method onConnectionFailed() in
     * LocationUpdateRemover and LocationUpdateRequester may call startResolutionForResult() to
     * start an Activity that handles Google Play services problems. The result of this
     * call returns here, to onActivityResult.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        //Log.d(LocationUtils.APPTAG, getString(R.string.resolved));
                        Log.d(LocationUtils.APPTAG,"Connected");
                        // Display the result
                        //mConnectionState.setText(R.string.connected);
                        //mConnectionStatus.setText(R.string.resolved);
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        //Log.d(LocationUtils.APPTAG, getString(R.string.no_resolution));
                        Log.d(LocationUtils.APPTAG, "no resolution");
                        // Display the result
                        //mConnectionState.setText(R.string.disconnected);
                        //mConnectionStatus.setText(R.string.no_resolution);

                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                //Log.d(LocationUtils.APPTAG,getString(R.string.unknown_activity_request_code, requestCode));
                Log.d(LocationUtils.APPTAG,"unknown_activity_request_code"+requestCode);

                break;
        }
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            //Log.d(LocationUtils.APPTAG, getString(R.string.play_services_available));
            Log.d(LocationUtils.APPTAG, "play_services_available");
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);
                errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
            }
            return false;
        }
    }

    /**
     * Invoked by the "Get Address" button.
     * Get the address of the current location, using reverse geocoding. This only works if
     * a geocoding service is available.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    // For Eclipse with ADT, suppress warnings about Geocoder.isPresent()
    @SuppressLint("NewApi")
    public void getAddress(View v) {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            //Toast.makeText(this, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            Toast.makeText(this,"no_geocoder_available", Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = mLocationClient.getLastLocation();

            // Turn the indefinite activity indicator on
            mActivityIndicator.setVisibility(View.VISIBLE);

            // Start the background task
            (new GetAddressTask(this)).execute(currentLocation);
        }
    }

    /**
     * Invoked by the "Start Updates" button
     * Sends a request to start location updates
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void startUpdates(View v) {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    /**
     * Invoked by the "Stop Updates" button
     * Sends a request to remove location updates
     * request them.
     *
     * @param v The view object associated with this method, in this case a Button.
     */
    public void stopUpdates(View v) {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        //mConnectionStatus.setText(R.string.connected);
        System.out.println("Ligado: Listener");
        if (servicesConnected()) {
            System.out.println("Ligado: Services");
            // Get the current location
            Location currentLocation=null;
            while(currentLocation==null){
            System.out.println("NOP");
            currentLocation = mLocationClient.getLastLocation();
            //currentLocation =service.getLastKnownLocation(provider);
            }
            if(currentLocation!=null){
            System.out.println("YESSSS");
            lati=(double)currentLocation.getLatitude();
            longi=(double)currentLocation.getLongitude();
            //LatLng lastLatLng2 = new LatLng(lati, longi);
            System.out.println("Lat:"+currentLocation.getLatitude()+" Long:"+currentLocation.getLongitude());
            }
            // Display the current location in the UI
            //mLatLng.setText(LocationUtils.getLatLng(this, currentLocation));
        }else System.out.println("DesLigado: Services");
        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }

        if(gps.canGetLocation()){
            System.out.println("GPSLat:"+gps.getLatitude()+" GPSLong:"+gps.getLongitude());
        }else System.out.println("Erro de GPS");

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
        //mConnectionStatus.setText(R.string.disconnected);
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this,
                        LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /**
     * Report location updates to the UI.
     *
     * @param location The updated location.
     */
    @Override
    public void onLocationChanged(Location location) {

        // Report to the UI that the location was updated
        //mConnectionStatus.setText(R.string.location_updated);

        // In the UI, set the latitude and longitude to the value received
        mLatLng.setText(LocationUtils.getLatLng(this, location));
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
        //mConnectionState.setText(R.string.location_requested);
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
        //mConnectionState.setText(R.string.location_updates_stopped);
    }

    /**
     * An AsyncTask that calls getFromLocation() in the background.
     * The class uses the following generic types:
     * Location - A {@link android.location.Location} object containing the current location,
     *            passed as the input parameter to doInBackground()
     * Void     - indicates that progress units are not used by this subclass
     * String   - An address passed to onPostExecute()
     */
    protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        Context localContext;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            localContext = context;
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(localContext, Locale.getDefault());

            // Get the current location from the input parameter list
            Location location = params[0];

            // Create a list to contain the result address
            List <Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(),
                        location.getLongitude(), 1
                );

                // Catch network or other I/O problems.
            } catch (IOException exception1) {

                // Log an error and return an error message
                //Log.e(LocationUtils.APPTAG, getString(R.string.IO_Exception_getFromLocation));
                Log.e(LocationUtils.APPTAG, "IO_Exception_getFromLocation");

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                //return (getString(R.string.IO_Exception_getFromLocation));
                return "IO_Exception_getFromLocation";

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2) {

                // Construct a message containing the invalid arguments
               /* String errorString = getString(
                        R.string.illegal_argument_exception,
                        location.getLatitude(),
                        location.getLongitude()
                );*/
                String errorString="erro illegal argument exception";
                // Log the error and print the stack trace
                Log.e(LocationUtils.APPTAG, errorString);
                exception2.printStackTrace();

                //
                return errorString;
            }
            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                /*String addressText = getString(R.string.address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ?
                                address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );*/

                // Return the text
                //return addressText;
                return "endereco";
                // If there aren't any addresses, post a message
            } else {
                return "no_address found";
                //return getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address) {

            // Turn off the progress bar
            mActivityIndicator.setVisibility(View.GONE);

            // Set the address in the UI
            mAddress.setText(address);
        }
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                this,
                LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(getSupportFragmentManager(), LocationUtils.APPTAG);
        }
    }

    /**
     * Define a DialogFragment to display the error dialog generated in
     * showErrorDialog.
     */
    public static class ErrorDialogFragment extends DialogFragment {

        // Global field to contain the error dialog
        private Dialog mDialog;

        /**
         * Default constructor. Sets the dialog field to null
         */
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        /**
         * Set the dialog to display
         *
         * @param dialog An error dialog
         */
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        /*
         * This method must return a Dialog to the DialogFragment.
         */
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }


}
