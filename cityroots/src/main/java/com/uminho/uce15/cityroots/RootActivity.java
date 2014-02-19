package com.uminho.uce15.cityroots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.uminho.uce15.cityroots.data.CityRootsWebInterfaceImpl;

import java.io.InputStream;

public class RootActivity extends ActionBarActivity {
    LoadData ldata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadingscreen);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user = prefs.getString("userid", "");

        ldata = new LoadData(this,user);
        ldata.execute();

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

    public class LoadData extends AsyncTask<String, Void, String> {

        String user;
        Context context;

        public LoadData(Context context,String user) {

            this.user = user;
            this.context = context;
            runOnUiThread(new Runnable() {
                public void run() {
                    ((TextView)findViewById(R.id.textView)).setText("Internet Connection Detected!");
                }
            });


        }

        protected String doInBackground(String... urls) {
            if(checkNetworkState(getApplicationContext())){
                runOnUiThread(new Runnable() {
                    public void run() {
                        ((TextView)findViewById(R.id.textView2)).setText("Loading Data ...");
                    }
                });

                DataProvider dp = new DataProvider(getApplicationContext());
                CityRootsWebInterfaceImpl cityRootsWebInterface = new CityRootsWebInterfaceImpl(getApplicationContext());
                cityRootsWebInterface.invalidateCache();
                dp.getAttractions();
                dp.getServices();
                dp.getEvents();
                dp.getRoutes();
                dp.getAds();
                dp.getOfflineMap();
            }

            return null;
        }

        protected void onPostExecute(String result) {
            Intent intent;
            runOnUiThread(new Runnable() {
                public void run() {
                    ((TextView)findViewById(R.id.textView3)).setText("Done");
                }
            });

            if( user.equals("") ){
                intent = new Intent(context, Login.class);
            }else{
                intent = new Intent(context, Home.class);
            }



            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStop() {

        super.onStop();
        ldata.cancel(true);
        finish();
    }
}
