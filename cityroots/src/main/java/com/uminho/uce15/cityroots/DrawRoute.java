package com.uminho.uce15.cityroots;
/**
 * Created by John on 21-01-2014.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.uminho.uce15.cityroots.data.Attraction;
import com.uminho.uce15.cityroots.data.Poi;

import org.w3c.dom.Document;

import java.util.ArrayList;

public class DrawRoute extends ActionBarActivity implements LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,GooglePlayServicesClient.OnConnectionFailedListener{

    //-----------------
    GMapV2Direction md;
    //---------------------

    private GPSTracker gps;
    private LocationManager service;
    private String provider;
    private Location posicao;
    double lati=0.0;
    double longi=0.0;
    // A request to connect to Location Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private LocationClient mLocationClient;

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

    private Marker userMarker;
    private Marker[] placeMarkers;
    private MarkerOptions[] places;

    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
    private GoogleMap theMap=null;
    private ArrayList lista;

    private ListView list;
    private ListAdapter adapter;

    private Context old;
    MySlidingPaneLayout slidingPaneLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_desenhar_rota);

        lista = null;
        slidingPaneLayout = (MySlidingPaneLayout)findViewById(R.id.pane);

        DataProvider provider = new DataProvider(getApplicationContext()) ;


        Intent intent = getIntent();
        Integer id= Integer.valueOf(intent.getStringExtra("id"));

        lista= provider.getPontosRoute((int)id);

        list=(ListView)findViewById(R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));


        if(lista!=null){
            adapter = new ListAdapter(DrawRoute.this,lista);
            list.setAdapter(adapter);
        }

        assert lista != null;


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
        otherIcon = R.drawable.user_pos;

        if(theMap==null){
            //map not instantiated yet
            SupportMapFragment mapFrag=
                    (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mapa);
            theMap=mapFrag.getMap();

        }
        if(theMap != null){
            //ok - proceed
            theMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            if(checkNetworkState(DrawRoute.this)){
                updatePlaces();
            }
            else Toast.makeText(DrawRoute.this,"No Connection",Toast.LENGTH_LONG).show();
        }

        gps = new GPSTracker(this);

       list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //Starting new intent
                //Intent intent = new Intent(ListarPoi.this, DetalhesPois.class);
                //Intent id1 = intent.putExtra("id", ((Poi) lista.get(position)).getId());
                //startActivity(intent);
            }
        });
    }

    public void goToList(View view){
        slidingPaneLayout.openPane();
    }

    private void updatePlaces(){

        /*
        if(userMarker!=null) userMarker.remove();
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
        */


        LatLng lastpos = null;
        LatLng newpos = null;

        int i=0;
        theMap.addMarker(new MarkerOptions()
                .position(lastpos=(new LatLng(((Poi)lista.get(0)).getLatitude(),((Poi)lista.get(0)).getLongitude())))
                .title(((Poi)lista.get(0)).getName())
                .snippet(((Poi) lista.get(0)).getDescription()));
        i++;
        while(i<lista.size()){
            md = new GMapV2Direction(); 
            theMap.addMarker(new MarkerOptions()
                    .position(newpos = (new LatLng(((Poi) lista.get(i)).getLatitude(), ((Poi) lista.get(i)).getLongitude())))
                    .title(((Poi) lista.get(i)).getName())
                    .snippet(((Poi) lista.get(i)).getDescription()));
            i++;

            Document doc = md.getDocument(lastpos, newpos,
                    GMapV2Direction.MODE_WALKING);

            ArrayList<LatLng> directionPoint = md.getDirection(doc);
            PolylineOptions rectLine = new PolylineOptions().width(3).color(
                    Color.RED);

            for (int j = 0; j < directionPoint.size(); j++) {
                rectLine.add(directionPoint.get(j));
            }
            Polyline polylin = theMap.addPolyline(rectLine);
        }
}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.listar_eventos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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
    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Location currentLocation=null;
        //mConnectionStatus.setText(R.string.connected);
        System.out.println("Ligado: Listener");
        if (servicesConnected()) {
            System.out.println("Ligado: Services");
            // Get the current location
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
        //if (mUpdatesRequested) {
            startPeriodicUpdates();
        //}

        if(gps.canGetLocation()){
            System.out.println("GPSLat:"+gps.getLatitude()+" GPSLong:"+gps.getLongitude());
        }else System.out.println("Erro de GPS");


        if(currentLocation!=null){
                if(userMarker!=null) userMarker.remove();
                LatLng pos=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

                userMarker = theMap.addMarker(new MarkerOptions()
                        .position(pos)
                        .title("Está aqui")
                        .icon(BitmapDescriptorFactory.fromResource(userIcon))
                        .snippet("Sua última localização"));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(pos)
                        .zoom(12)                   // Sets the zoom
                        .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder

                theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

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
        System.out.println("Location changed");

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

        if(currentLocation!=null){
            if(userMarker!=null) userMarker.remove();
            LatLng pos=new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

            userMarker = theMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .title("Está aqui")
                    .icon(BitmapDescriptorFactory.fromResource(userIcon))
                    .snippet("Sua última localização"));

            /*CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(pos)
                    .zoom(12)                   // Sets the zoom
                    .tilt(45)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder

            theMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
        }
    }

    private void startPeriodicUpdates() {

        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    private void stopPeriodicUpdates() {
        mLocationClient.removeLocationUpdates(this);
    }

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

    public static boolean checkNetworkState(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo infos[] = conMgr.getAllNetworkInfo();
        for (NetworkInfo info : infos) {
            if (info.getState() == NetworkInfo.State.CONNECTED)
                return true;
        }
        return false;
    }
}