package com.uminho.uce15.cityroots;

/**
 * Created by John on 18-12-2013.
 */
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

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class DetalhesEventos extends ActionBarActivity {

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
        setContentView(R.layout.activity_detalhes_eventos);

        /*if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/

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
        getMenuInflater().inflate(R.menu.detalhes_poi, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_detalhes_poi, container, false);
            return rootView;
        }
    }


    class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DetalhesEventos.this);
            pDialog.setMessage("Loading profile ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting Profile JSON
         * */
        protected String doInBackground(String... args) {
            String reference = args[0];

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
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
                        name = ponto.getString("name");
                        description = ponto.getString("description");
                        schedule = ponto.getString("schedule");
                        address = ponto.getString("address");
                        organization = ponto.getString("organization");
                        price = ponto.getString("price");
                        program = ponto.getString("program");
                        latitude = ponto.getString("latitude");
                        longitude = ponto.getString("longitude");
                    }
                    catch(JSONException jse){
                        Log.v("PLACES", "missing value");
                        jse.printStackTrace();
                    }

                    /*String name = placeDetails.result.name;
                    String address = placeDetails.result.formatted_address;
                    String phone = placeDetails.result.formatted_phone_number;
                    String latitude = Double.toString(placeDetails.result.geometry.location.lat);
                    String longitude = Double.toString(placeDetails.result.geometry.location.lng)*/

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

                    /** Button click event for shown on map */
                    btnShowPlaceOnMap.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View arg0) {

                            // Intent i = new Intent(getApplicationContext(),SimplePlaceMapActivity.class);

                            // Sending user current geo location
                            //i.putExtra("place_latitude", lat1);
                            //i.putExtra("place_longitude", lng1);

                            // passing near places to map activity
                            //  i.putExtra("near_places", nem);
                            // staring activity

                            //startActivity(i);
                        }
                    });

                }
            });

        }

    }

}