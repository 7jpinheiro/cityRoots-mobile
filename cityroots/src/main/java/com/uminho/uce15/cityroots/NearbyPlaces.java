package com.uminho.uce15.cityroots;

import android.content.SharedPreferences;
import android.provider.CalendarContract;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.uminho.uce15.cityroots.objects.Attraction;
import com.uminho.uce15.cityroots.objects.Event;
import com.uminho.uce15.cityroots.objects.Poi;

import org.osmdroid.api.Marker;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;

public class NearbyPlaces extends ActionBarActivity {
    private MapView myOpenMapView;
    private MapController myMapController;

    ArrayList<OverlayItem> anotherOverlayItemArray;
    MyLocationOverlay myLocationOverlay = null;
    ArrayList<Poi> lis = new ArrayList<Poi>();;
    double lat=41.54694103404733;
    double lng=-8.425848484039307;

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


    private Marker userMarker;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 20;
    private int distNearBy=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        SeekBar seekbar = (SeekBar) findViewById(R.id.seekBar);

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                    // change progress text label with current seekbar value
                    distNearBy=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                updatePlaces();
            }
        });

        if(myOpenMapView==null){
            //map not instantiated yet
            GeoPoint geo = new GeoPoint(lat, lng);

            myOpenMapView = (MapView)findViewById(R.id.openmapview1);
            myOpenMapView.setBuiltInZoomControls(false);
            myOpenMapView.setMultiTouchControls(true);
            myMapController = (MapController) myOpenMapView.getController();
            myMapController.setZoom(15);

           // myMapController.setCenter(geo);

            //Add MyLocationOverlay
            myLocationOverlay = new MyLocationOverlay(this, myOpenMapView);
            myOpenMapView.getOverlays().add(myLocationOverlay);

        }

        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myOpenMapView.getController().animateTo(myLocationOverlay.getMyLocation());
            }
        });





       //updatePlaces();
    }

    private void updatePlaces(){
        int i=0;
        OverlayItem o;
        while (i< lis.size()){

            anotherOverlayItemArray = new ArrayList<OverlayItem>();

            o =  new OverlayItem(
                    ((Poi)lis.get(i)).getName() , ((Poi)lis.get(i)).getDescription(), new GeoPoint(((Poi)lis.get(i)).getLatitude(), ((Poi)lis.get(i)).getLongitude()));


            anotherOverlayItemArray.add(o);

            ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay
                    = new ItemizedIconOverlay<OverlayItem>(
                    this, anotherOverlayItemArray, null);
            myOpenMapView.getOverlays().add(anotherItemizedIconOverlay);

            i++;
        }

    }


    public static float distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        int meterConversion = 1609;

        return (float) (dist * meterConversion);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nearby_places, menu);
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

    @Override
    public void onStop() {

        super.onStop();
    }
    /*
     * Called when the Activity is going into the background.
     * Parts of the UI may be visible, but the Activity is inactive.
     */
    @Override
    public void onPause() {
        super.onPause();
        myLocationOverlay.disableMyLocation();
        myLocationOverlay.disableCompass();
        myLocationOverlay.disableFollowLocation();

    }

    /*
     * Called when the Activity is restarted, even before it becomes visible.
     */
    @Override
    public void onStart() {

        super.onStart();

    }
    /*
     * Called when the system detects that this Activity is now visible.
     */
    @Override
    public void onResume() {
        super.onResume();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableCompass();
        myLocationOverlay.enableFollowLocation();

        GeoPoint myLocation = myLocationOverlay.getMyLocation();

        double myLocLat=0.0;
        double myLocLng=0.0;
        try{
            myLocLat = myLocation.getLatitude();
            myLocLng = myLocation.getLongitude();
        }
        catch(Exception e){

        }
        DataProvider dp = new DataProvider();
        ArrayList pois = new ArrayList() ;
        pois = (ArrayList<Event>) dp.getEvents();
        Poi poi;

        for(int i = 0; i< pois.size();i++){
            poi =(Poi) pois.get(i);
            System.out.println("Distancia:" + distFrom(myLocLat, myLocLng, poi.getLatitude(), poi.getLongitude()));
            if(distFrom(myLocLat, myLocLng,  poi.getLatitude(), poi.getLongitude()) < distNearBy)
                lis.add(poi);

            System.out.println("Buahahahahahahhsdada");
        }

        updatePlaces();

        //System.out.println("Long:" + myLocLng + " Lat:" + myLocLat);
        //System.out.println("Distancia:" + distFrom(myLocLat,myLocLng,p,lng1));
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
}

