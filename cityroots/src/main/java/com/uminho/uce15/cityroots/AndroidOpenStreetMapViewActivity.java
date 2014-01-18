package com.uminho.uce15.cityroots;

/**
 * Created by Alexis on 04-01-2014.
 */


import java.util.ArrayList;

import org.osmdroid.ResourceProxy;
import org.osmdroid.util.GeoPoint;

import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;


import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;


import android.app.Activity;

import android.os.Bundle;
import android.widget.Toast;

public class AndroidOpenStreetMapViewActivity extends Activity{
    double lat=41.54694103404733;
    double lng=-8.425848484039307;

    private MapView myOpenMapView;
    private MapController myMapController;

    ArrayList<OverlayItem> anotherOverlayItemArray;
    MyLocationOverlay myLocationOverlay = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adosmap);
        GeoPoint geo = new GeoPoint(lat, lng);


        myOpenMapView = (MapView)findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myOpenMapView.setMultiTouchControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setZoom(15);

        myMapController.setCenter(geo);



        //Add MyLocationOverlay
        myLocationOverlay = new MyLocationOverlay(this, myOpenMapView);
        myOpenMapView.getOverlays().add(myLocationOverlay);



        /*PathOverlay myPath = new PathOverlay(Color.RED, this);

        myPath.addPoint(new GeoPoint(lat, lng));
        myPath.addPoint(new GeoPoint(41.552400928837926, -8.412652015686035));


        myOpenMapView.getOverlays().add(myPath);
        myOpenMapView.postInvalidate();
        */


        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myOpenMapView.getController().animateTo(myLocationOverlay.getMyLocation());
            }
        });



    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        myLocationOverlay.enableMyLocation();
        myLocationOverlay.enableCompass();
        myLocationOverlay.enableFollowLocation();


    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        myLocationOverlay.disableMyLocation();
        myLocationOverlay.disableCompass();
        myLocationOverlay.disableFollowLocation();
    }


}