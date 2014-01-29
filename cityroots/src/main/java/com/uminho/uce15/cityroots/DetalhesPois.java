

package com.uminho.uce15.cityroots;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.uminho.uce15.cityroots.data.Attraction;
import com.uminho.uce15.cityroots.data.Comment;
import com.uminho.uce15.cityroots.data.Event;
import com.uminho.uce15.cityroots.data.Poi;
import com.uminho.uce15.cityroots.data.Service;


import java.util.List;

public class DetalhesPois extends ActionBarActivity {
    private boolean commentsVisible;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_poi);

        Intent i = getIntent();

        int id = i.getIntExtra("id",0);
        int poi_type = i.getIntExtra("type", 0);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(findViewById(R.id.myComment).getWindowToken(), 0);

        LoadDetails loadD = new LoadDetails(DetalhesPois.this,poi_type);

        loadD.execute(id);

        commentsVisible = false;


    }

    public void toggleComments(View view){
        if (commentsVisible){
            commentsVisible = false;

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(findViewById(R.id.myComment).getWindowToken(), 0);

            ((Button)findViewById(R.id.viewComments)).setText("View Comments");
            ((ScrollView)findViewById(R.id.myScrollView)).setVisibility(View.VISIBLE);
            ((LinearLayout)findViewById(R.id.commentLayout)).setVisibility(View.GONE);
            ((ListView)findViewById(R.id.commentList)).setVisibility(View.GONE);
        }
        else {
            commentsVisible = true;
            ((Button)findViewById(R.id.viewComments)).setText("Hide Comments");
            ((ScrollView)findViewById(R.id.myScrollView)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.commentLayout)).setVisibility(View.VISIBLE);
            ((ListView)findViewById(R.id.commentList)).setVisibility(View.VISIBLE);
        }
    }

    public void doComment(View view){
        String comment = ((TextView) findViewById(R.id.myComment)).getText().toString();
        if( !comment.equals("") ){

        }
    }

    class LoadDetails extends AsyncTask<Integer, Integer, Integer> {

        Activity activity;
        int poi_type;

        protected LoadDetails(Activity activity,int poi_type){
            this.activity = activity;
            this.poi_type = poi_type;
        }

        @Override
        protected void onPreExecute(){
            //start loading
        }
        @Override
        protected Integer doInBackground(Integer... id) {
            return id[0];
        }
        @Override
        protected void onPostExecute(Integer id){

            RatingBar ratBar = (RatingBar) findViewById(R.id.ratingBar1);

            // Poi Labels
            TextView lbl_name = (TextView) findViewById(R.id.name);
            TextView lbl_description = (TextView) findViewById(R.id.description);
            TextView lbl_schedule = (TextView) findViewById(R.id.schedule);
            TextView lbl_address = (TextView) findViewById(R.id.address);
            TextView lbl_price = (TextView) findViewById(R.id.price);
            TextView lbl_site = (TextView) findViewById(R.id.website);
            TextView lbl_email = (TextView) findViewById(R.id.email);
            TextView lbl_transport = (TextView) findViewById(R.id.transport);
            //Event Labels
            TextView lbl_start = (TextView) findViewById(R.id.event_start);
            TextView lbl_end = (TextView) findViewById(R.id.event_end);
            TextView lbl_organization = (TextView) findViewById(R.id.org);
            TextView lbl_program = (TextView) findViewById(R.id.event_program);


            //Service Label
            TextView lbl_capacity = (TextView) findViewById(R.id.serv_capacity);
            TextView lbl_details = (TextView) findViewById(R.id.serv_details);

            LinearLayout lin = (LinearLayout) findViewById(R.id.labels_event);
            LinearLayout lin1 = (LinearLayout) findViewById(R.id.labels_service);

            //finish loading
            DataProvider dp = new DataProvider(getApplicationContext());
            Poi poi = null;

            switch (poi_type){
                case 0:
                    poi = dp.getAttraction(id);
                    String price = "Não tem";
                    if( poi != null ){
                        price = ((Attraction) poi).getPrice();
                    }
                    lbl_price.setText(price);
                    lin.setVisibility(View.GONE);
                    lin1.setVisibility(View.GONE);
                    break;
                case 1:
                    poi = dp.getEvent(id);
                    String priceA = "Não tem";
                    if( poi != null ){
                        priceA = ((Event) poi).getPrice();
                    }
                    lbl_price.setText(priceA);
                    lbl_start.setText(((Event)poi).getStart());
                    lbl_end.setText(((Event)poi).getEnd());
                    lbl_organization.setText(((Event)poi).getOrganization());
                    lbl_program.setText(((Event)poi).getProgram());

                    ratBar.setVisibility(View.GONE);
                    lin1.setVisibility(View.GONE);
                    break;
                case 2:
                    poi = dp.getService(id);
                    lbl_capacity.setText(Integer.toString(((Service)poi).getCapacity()));
                    lbl_details.setText(Integer.toString(((Service)poi).getCapacity()));

                    lbl_price.setVisibility(View.GONE);
                    lin.setVisibility(View.GONE);
                    break;
            }

            String photoPath = poi.getPhotos().get(0);
            new DownloadImageTask((ImageView)findViewById(R.id.listelem_img), (ProgressBar)findViewById(R.id.loadingImg)).execute(photoPath);

            lbl_name.setText(poi.getName());
            lbl_description.setText(poi.getDescription());
            lbl_schedule.setText(poi.getSchedule());
            lbl_address.setText(poi.getAddress());
            lbl_site.setText(poi.getSite());
            lbl_email.setText(poi.getEmail());
            lbl_transport.setText(poi.getTransport());



            CommentAdapter ca = new CommentAdapter(activity,poi.getComments());
            ListView list = (ListView) findViewById(R.id.commentList);
            list.setAdapter(ca);
        }

    }


//Fill Comment List

    private class CommentAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private List lista;


        public CommentAdapter(Activity context, List lista) {

            super(context, R.layout.list_comment, lista);
            this.context = context;

            this.lista = lista;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.list_comment, null, true);

            ImageView avatar = (ImageView) rowView.findViewById(R.id.avatar);
            TextView user_name = (TextView) rowView.findViewById(R.id.user_name);
            TextView comment = (TextView) rowView.findViewById(R.id.comment);


            user_name.setText(((Comment) lista.get(position)).getUsername());
            comment.setText(((Comment)lista.get(position)).getComment());
            avatar.setImageResource(R.drawable.logo);

            return rowView;
        }
    }
}



/*
    // flag for Internet connection status
    Boolean isInternetPresent = false;
    // Alert Dialog Manager
    AlertDialogManager alert = new AlertDialogManager();
    // Progress dialog
    ProgressDialog pDialog;
    // KEY Strings
    public static String Ref= null; // id of the place

    // Button
    Button btnShowPlaceOnMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_poi);

        */
/*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*//*


        Intent i = getIntent();
        // Place reference id
        String reference = i.getStringExtra("id");
        // Calling a Async Background thread
        Ref=reference;
        new LoadSinglePlaceDetails().execute(reference);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.detalhes_poi, menu);
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

    */
/**
 * A placeholder fragment containing a simple view.
 *//*

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_detalhes_events, container, false);
            return rootView;
        }
    }


    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

        */
/**
 * Before starting background thread Show Progress Dialog
 * *//*

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetalhesPois.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        */
/**
 * getting Profile JSON
 * *//*

        protected String doInBackground(String... args) {
            String reference = args[0];

            return null;
        }

        */
/**
 * After completing background task Dismiss the progress dialog
 * **//*

        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all products
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {

                    String name = "";
                    String description = "";
                    String schedule = "";
                    String address = "";
                    String organization = "";
                    String price = "";
                    String program = "";
                    String latitude = "";
                    String longitude = "";

                    try{
                        JSONObject ponto= new JSONObject(Ref);
                        JSONArray et=ponto.getJSONArray("event_translations");
                        JSONObject pt=et.getJSONObject(0);
                        name = pt.getString("name");
                        description = pt.getString("description");
                        schedule = pt.getString("schedule");
                        address = ponto.getString("address");
                        organization = ponto.getString("organization");
                        price = ponto.getString("price");
                        program = ponto.getString("program");
                        latitude = ponto.getString("latitude");
                        longitude = ponto.getString("longitude");
                    }
                    catch(JSONException jse){
                        Log.v("DetalhesPois", "missing value");
                        jse.printStackTrace();
                    }

                    */
/*String name = placeDetails.result.name;
                    String address = placeDetails.result.formatted_address;
                    String phone = placeDetails.result.formatted_phone_number;
                    String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                    String longitude = Double.toString(placeDetails.result.geometry.location.lng)*//*


                    //Log.d("Place ", name + address + phone + latitude + longitude);

                    // Displaying all the details in the view
                    // single_place.xml
                    TextView lbl_name = (TextView) findViewById(R.id.name);
                    TextView lbl_description = (TextView) findViewById(R.id.description);
                    TextView lbl_schedule = (TextView) findViewById(R.id.schedule);
                    TextView lbl_address = (TextView) findViewById(R.id.address);
                    TextView lbl_organization = (TextView) findViewById(R.id.organization);
                    TextView lbl_price = (TextView) findViewById(R.id.price);
                    TextView lbl_program = (TextView) findViewById(R.id.program);
                    TextView lbl_location = (TextView) findViewById(R.id.location);

                    // Check for null data from google
                    // Sometimes place details might missing
                    name = name == null ? "Not present" : name; // if name is null display as "Not present"
                    description = description == null ? "Not present" : description;
                    schedule = schedule == null ? "Not present" : schedule;
                    organization = organization == null ? "Not present" : organization;
                    price = price == null ? "Not present" : price;
                    program = program == null ? "Not present" : program; // if name is null display as "Not present"
                    address = address == null ? "Not present" : address;
                    latitude = latitude == null ? "Not present" : latitude;
                    longitude = longitude == null ? "Not present" : longitude;

                    final String lat1 = latitude;
                    final String lng1 = longitude;

                    lbl_name.setText(name);
                    lbl_address.setText(address);
                    lbl_description.setText(description);
                    lbl_schedule.setText(schedule);
                    lbl_organization.setText(organization);
                    lbl_price.setText(price);
                    lbl_program.setText(program);
                    lbl_location.setText(Html.fromHtml("<b>Latitude:</b> " + latitude + ", <b>Longitude:</b> " + longitude));

                    btnShowPlaceOnMap = (Button) findViewById(R.id.placebutton);


                }
            });

        }

    }

}*/
