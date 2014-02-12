package com.uminho.uce15.cityroots;


import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;


import com.uminho.uce15.cityroots.data.Attraction;
import com.uminho.uce15.cityroots.data.Event;
import com.uminho.uce15.cityroots.data.Poi;
import com.uminho.uce15.cityroots.data.Route;
import com.uminho.uce15.cityroots.data.Service;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.MyLocationOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import android.widget.ListView;

import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ListarPoi extends ActionBarActivity{

    private MapView myOpenMapView;
    private MapController myMapController;

    ArrayList<MyOwnItemizedOverlay> anotherOverlayItemArray;
    MyLocationOverlay myLocationOverlay = null;

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



    private final int MAX_PLACES = 20;



    private ArrayList lista;

    private ListView list;
    private ListAdapter adapter;
    private int value;
    private Context old;
    MySlidingPaneLayout slidingPaneLayout;

    private int type_poi = -1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();



        setContentView(R.layout.activity_listar_poi);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(findViewById(R.id.editText_lispoi).getWindowToken(), 0);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        lista = null;
        slidingPaneLayout = (MySlidingPaneLayout)findViewById(R.id.pane);

        DataProvider provider = new DataProvider(getApplicationContext()) ;

        Bundle b = getIntent().getExtras();
        value = b.getInt("id_category");

        switch (value){
            case R.id.poi:
                lista = new ArrayList<Attraction>();
                lista =(ArrayList<Attraction>) provider.getAttractions();

                break;
            case R.id.routes:
                lista = new ArrayList<Route>();
                lista = (ArrayList<Route>) provider.getRoutes();
                break;
            case R.id.events:
                lista =(ArrayList<Event>) provider.getEvents();
                break;
            case R.id.tpa:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>) provider.getAttractions("Tradicional"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Tradicional"));
                lista.addAll((ArrayList<Service>)provider.getServices("Tradicional"));
                break;
            case R.id.gastronomy:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Gastronomia"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Gastronomia"));
                lista.addAll((ArrayList<Service>)provider.getServices("Gastronomia"));
                break;
            case R.id.activities:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Divertimento"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Divertimento"));
                lista.addAll((ArrayList<Service>)provider.getServices("Divertimento"));
                break;
            case R.id.outdoor:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Ar livre"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Ar livre"));
                lista.addAll((ArrayList<Service>)provider.getServices("Ar livre"));
                break;
            case R.id.nightlife:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Nightlife"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Nightlife"));
                lista.addAll((ArrayList<Service>)provider.getServices("Nightlife"));
                break;
            case R.id.hotels:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Hotel"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Hotel"));
                lista.addAll((ArrayList<Service>)provider.getServices("Hotel"));
                break;
            case R.id.transport1:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Transporte"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Transporte"));
                lista.addAll((ArrayList<Service>)provider.getServices("Transporte"));
                break;
            case R.id.afd:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Acessibilidade"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Acessibilidade"));
                lista.addAll((ArrayList<Service>)provider.getServices("Acessibilidade")); 
                break;
            case R.id.contacts:
                lista = new ArrayList<Poi>();
                lista.addAll((ArrayList<Attraction>)provider.getAttractions("Contacto Util"));
                lista.addAll((ArrayList<Event>)provider.getEvents("Contacto Util"));
                lista.addAll((ArrayList<Service>)provider.getServices("Contacto Util"));
                break;
        }


        list=(ListView)findViewById(R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));

        EditText filterText = (EditText) findViewById(R.id.editText_lispoi);
        filterText.addTextChangedListener(filterTextWatcher);

        if(lista!=null){
            adapter = new ListAdapter(ListarPoi.this,lista);
            list.setAdapter(adapter);
        }

        assert lista != null;

        if(myOpenMapView==null){
            //map not instantiated yet
            GeoPoint geo = new GeoPoint(lat, lng);

            myOpenMapView = (MapView)findViewById(R.id.openmapview);
            myOpenMapView.setBuiltInZoomControls(false);
            myOpenMapView.setMultiTouchControls(true);

            double north = 41.5742;
            double east  = -8.3769;
            double south = 41.5178;
            double west  =  -8.4484;
            BoundingBoxE6 bBox = new BoundingBoxE6(north, east, south, west);

            myOpenMapView.setScrollableAreaLimit(bBox);

            myMapController = (MapController) myOpenMapView.getController();
            myMapController.setZoom(15);

            myMapController.setCenter(geo);
            myOpenMapView.setMaxZoomLevel(17);
            myOpenMapView.setMinZoomLevel(14);
            //Add MyLocationOverlay
            myLocationOverlay = new MyLocationOverlay(this, myOpenMapView);
            myOpenMapView.getOverlays().add(myLocationOverlay);


        }

        myLocationOverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myOpenMapView.getController().animateTo(myLocationOverlay.getMyLocation());
            }
        });

        updatePlaces();

    }

    public void goToList(View view){
        slidingPaneLayout.openPane();
    }

    private void updatePlaces(){
        int i=0;
        OverlayItem o;

        Drawable marker = this.getResources().getDrawable(R.drawable.marcador);

        while (i< lista.size()){

        //anotherOverlayItemArray = new ArrayList<MyOwnItemizedOverlay>();

          o =  new OverlayItem(
                  ((Poi)lista.get(i)).getName() , ((Poi)lista.get(i)).getDescription(), new GeoPoint(((Poi)lista.get(i)).getLatitude(), ((Poi)lista.get(i)).getLongitude()));
            o.setMarker(marker);


            MyOwnItemizedOverlay mon= new MyOwnItemizedOverlay(marker,this);

            mon.addItem(o);

            myOpenMapView.getOverlays().add(mon.getOverlay());
        //anotherOverlayItemArray.add(o);

       /* ItemizedIconOverlay<OverlayItem> anotherItemizedIconOverlay
                = new ItemizedIconOverlay<MyOwnItemizedOverlay>(
                this, anotherOverlayItemArray, null);
        myOpenMapView.getOverlays().add(anotherItemizedIconOverlay);*/

        i++;
        }

    }


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
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */

    public class MyOwnItemizedOverlay {
        protected ItemizedIconOverlay<OverlayItem> mOverlay;
        protected Context mContext;
        protected Drawable mMarker;

        public MyOwnItemizedOverlay(Drawable marker, Context context) {
            mContext = context;
            ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
            ResourceProxy resourceProxy = (ResourceProxy) new DefaultResourceProxyImpl(mContext);
            mMarker = marker;

            mOverlay = new ItemizedIconOverlay<OverlayItem>(
                    items, mMarker,
                    new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                        @Override public boolean onItemSingleTapUp(final int index, final OverlayItem item) {

                            return true;
                        }

                        @Override public boolean onItemLongPress(final int index, final OverlayItem item) {
                            return onSingleTapUpHelper(index, item);
                        }
                    }, resourceProxy);

        }

        public boolean onSingleTapUpHelper(int i, OverlayItem item) {
            //Toast.makeText(mContext, "Item " + i + " has been tapped!", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
            dialog.setTitle(item.getTitle());
            dialog.setIcon(mMarker);
            dialog.setMessage(item.getSnippet());
            dialog.show();
            return true;
        }

        public void addItem(OverlayItem item){
            mOverlay.addItem(item);
        }

        public ItemizedIconOverlay<OverlayItem> getOverlay(){
            return mOverlay;
        }
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            adapter.getFilter().filter(s.toString().toLowerCase());
            adapter.notifyDataSetChanged();

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
