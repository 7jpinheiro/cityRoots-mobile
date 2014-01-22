package com.uminho.uce15.cityroots;


import android.app.AlertDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.Marker;
import com.uminho.uce15.cityroots.data.Attraction;
import com.uminho.uce15.cityroots.data.Event;
import com.uminho.uce15.cityroots.data.Poi;
import com.uminho.uce15.cityroots.data.Route;
import com.uminho.uce15.cityroots.data.Service;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
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


    private Marker userMarker;
    private Marker[] placeMarkers;
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

        lista = null;
        slidingPaneLayout = (MySlidingPaneLayout)findViewById(R.id.pane);

        DataProvider provider = new DataProvider(getApplicationContext()) ;

        Bundle b = getIntent().getExtras();
        value = b.getInt("id_category");

        switch (value){
            case R.id.poi:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                type_poi = 0;
                break;
            case R.id.routes:
                lista = (ArrayList<Route>) provider.getRoutes();
                type_poi = -1;
                break;
            case R.id.events:
                lista =(ArrayList<Event>) provider.getEvents();
                type_poi = 1;
                break;
            case R.id.tpa:
                lista = (ArrayList<Attraction>) provider.getAttractions("Tradicional");
                type_poi = 0;
                break;
            case R.id.gastronomy:
                lista = (ArrayList<Attraction>) provider.getAttractions("Gastronomia");
                type_poi = 0;
                break;
            case R.id.activities:
                lista = (ArrayList<Attraction>) provider.getAttractions();
                type_poi = 0;
                break;
            case R.id.outdoor:
                lista = (ArrayList<Attraction>) provider.getAttractions("Ar livre");
                type_poi = 0;
                break;
            case R.id.nightlife:
                lista = (ArrayList<Attraction>) provider.getAttractions("Nightlife");
                type_poi = 0;
                break;
            case R.id.hotels:
                lista = (ArrayList<Attraction>) provider.getAttractions("Hotel");
                type_poi = 0;
                break;
            case R.id.transport:
                lista = (ArrayList<Service>) provider.getServices();
                type_poi = 2;
                break;
            case R.id.afd:
                lista = (ArrayList<Attraction>) provider.getAttractions("Divertimento");
                type_poi = 0;
                break;
            case R.id.contacts:
                lista = (ArrayList<Attraction>) provider.getAttractions("Contacto");
                type_poi = 1;
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
            myMapController = (MapController) myOpenMapView.getController();
            myMapController.setZoom(15);

            myMapController.setCenter(geo);

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

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
              //Starting new intent
              Intent intent = new Intent(ListarPoi.this, DetalhesPois.class);
              //intent.putExtra("id", ((Poi) lista.get(position)).get);
              intent.putExtra("type", type_poi);
              startActivity(intent);

            }
        });
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


    public void search(View view){
        TextView textView = (TextView)findViewById(R.id.editText);

        //Log.d("Search", "Text:" + textView.getText());
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
