

package com.uminho.uce15.cityroots;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.preference.PreferenceManager;
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

import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.uminho.uce15.cityroots.data.Attraction;
import com.uminho.uce15.cityroots.data.CityRootsWebInterfaceImpl;
import com.uminho.uce15.cityroots.data.Comment;
import com.uminho.uce15.cityroots.data.Event;
import com.uminho.uce15.cityroots.data.Poi;
import com.uminho.uce15.cityroots.data.Service;


import java.util.ArrayList;
import java.util.List;

public class DetalhesPois extends ActionBarActivity {
    private boolean commentsVisible;
    int poi_id = -1;
    int poi_type =-1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_poi);

        Intent i = getIntent();

        int id = i.getIntExtra("id",0);
        poi_id = id;
        poi_type = i.getIntExtra("type", 0);

        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(findViewById(R.id.myComment).getWindowToken(), 0);


        ((RatingBar)findViewById(R.id.comment_ratingBar)).setVisibility(View.GONE);
        commentsVisible = false;

        RatingBar ratBar = (RatingBar) findViewById(R.id.ratingBar1);

        // Poi Labels

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
                String priceA= "-";
                if( poi != null){
                    priceA = ((Event) poi).getPrice();
                    if(priceA == null || priceA.equals(""))
                        priceA ="-";
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



        //String photoPath = poi.getPhotos().get(0);
        //new DownloadImageTask((ImageView)findViewById(R.id.listelem_img), (ProgressBar)findViewById(R.id.loadingImg)).execute(photoPath);

        //lbl_name.setText(poi.getName());
        setTitle(poi.getName());
        lbl_description.setText(poi.getDescription());
        lbl_schedule.setText(poi.getSchedule());
        lbl_address.setText(poi.getAddress());
        lbl_site.setText(poi.getSite());
        lbl_email.setText(poi.getEmail());
        lbl_transport.setText(poi.getTransport());
        ratBar.setRating((float)poi.getRating());


        CommentAdapter ca = new CommentAdapter(DetalhesPois.this,poi.getComments());
        ListView list = (ListView) findViewById(R.id.commentList);
        list.setAdapter(ca);

        ImageView imageView = (ImageView)findViewById(R.id.listelem_img);
        ProgressBar loadBar = (ProgressBar)findViewById(R.id.loadingImg);

        try{
            String photo_path = poi.getPhotos().get(0);
            new DownloadImageTask(imageView, loadBar).execute(photo_path);
        }
        catch(Exception e){
            imageView.setImageResource(R.drawable.abc_ab_bottom_solid_dark_holo);
        }
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
            ((RatingBar)findViewById(R.id.comment_ratingBar)).setVisibility(View.GONE);
        }
        else {
            commentsVisible = true;
            ((Button)findViewById(R.id.viewComments)).setText("Hide Comments");
            ((ScrollView)findViewById(R.id.myScrollView)).setVisibility(View.GONE);
            ((LinearLayout)findViewById(R.id.commentLayout)).setVisibility(View.VISIBLE);
            ((ListView)findViewById(R.id.commentList)).setVisibility(View.VISIBLE);
            ((RatingBar)findViewById(R.id.comment_ratingBar)).setVisibility(View.VISIBLE);
        }


    }

    public void doComment(View view){
        String comment = ((TextView) findViewById(R.id.myComment)).getText().toString();
        if( !comment.equals("") ){
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            String userid = prefs.getString("userid", "jprophet");
            Log.d("comentar", "userid:" + userid);
            //String service = prefs.getString("service", "google");
            int rating = Math.round(((RatingBar)findViewById(R.id.comment_ratingBar)).getRating());

            //Log.d("Comment", "PoI " + poi_id + " - "+userid + "@"+service+": " + comment);
            DataProvider dp = new DataProvider(getApplicationContext());
            try {
                dp.sendComment(""+poi_id,userid,comment,rating,poi_type);
            } catch (CityRootsWebInterfaceImpl.NoInternetConnectionError noInternetConnectionError) {
               // noInternetConnectionError.printStackTrace();
                dialogAlert(DetalhesPois.this);
            }
            ((TextView) findViewById(R.id.myComment)).setText("");
            Toast.makeText(getApplicationContext(),"Comment Sent!",Toast.LENGTH_LONG).show();
        }
    }

    public void dialogAlert(Context context){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("No Internet Connection");

        // set dialog message
        alertDialogBuilder
                .setMessage("To comment you need to have an Internet Connection!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
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
            avatar.setImageResource(R.drawable.avatar);

            return rowView;
        }
    }
}



