package com.uminho.uce15.cityroots;

/**
 * Created by John on 18-12-2013.
 */
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;

import android.util.Log;

public class SimplePlaceMapActivity  extends FragmentActivity implements LocationListener{

    private LocationManager locMan;
    private Marker userMarker;
    private Marker[] placeMarkers;
    private final int MAX_PLACES = 2;
    private MarkerOptions[] places;

    private int userIcon, foodIcon, drinkIcon, shopIcon, otherIcon;
    private GoogleMap theMap=null;

    double loclat=41.561653;
    double loclng=-8.397139;

    double placelat = 41.557183;
    double placelng = -8.39657;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_map);

        userIcon = R.drawable.mark_blue;
        foodIcon = R.drawable.mark_red;
        drinkIcon = R.drawable.mark_red;
        shopIcon = R.drawable.mark_red;
        otherIcon = R.drawable.mark_red;
        System.out.println("1");
        if(theMap==null){
            //map not instantiated yet
            SupportMapFragment mapFrag=
                    (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.the_map);
            theMap=mapFrag.getMap();

        }
        if(theMap != null){
            //ok - proceed
            theMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            placeMarkers = new Marker[MAX_PLACES];

            Intent intent = getIntent();


            placelat = Double.valueOf(intent.getStringExtra("place_latitude"));

            placelng = Double.valueOf(intent.getStringExtra("place_longitude"));

            updatePlaces(loclat,loclng,placelat,placelng);
        }
    }

    private void updatePlaces(double loclatitude, double loclongitude,double placelat,double placelng){
        if(userMarker!=null) userMarker.remove();
        System.out.println("3");
        LatLng lastLatLng = new LatLng(loclat, loclng);

        userMarker = theMap.addMarker(new MarkerOptions()
                .position(lastLatLng)
                .title("You are here")
                .icon(BitmapDescriptorFactory.fromResource(userIcon))
                .snippet("Your last recorded location"));

        theMap.animateCamera(CameraUpdateFactory.newLatLng(lastLatLng), 3000, null);

        theMap.addMarker(new MarkerOptions()
                .position(new LatLng(placelat, placelng))
                .title("Local Escolhido"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }




    @Override
    public void onLocationChanged(Location location) {
        Log.v("MainActivity", "location changed");
        //  updatePlaces(location.getLatitude(),location.getLatitude());
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

}