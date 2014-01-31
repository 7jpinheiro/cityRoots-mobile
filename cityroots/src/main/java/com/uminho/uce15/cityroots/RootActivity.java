package com.uminho.uce15.cityroots;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.uminho.uce15.cityroots.data.CityRootsWebInterfaceImpl;

public class RootActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String user = prefs.getString("userid", "");
        Intent intent;
        if(checkNetworkState(getApplicationContext())){
            DataProvider dp = new DataProvider(getApplicationContext());
            CityRootsWebInterfaceImpl cityRootsWebInterface = new CityRootsWebInterfaceImpl(getApplicationContext());
            cityRootsWebInterface.invalidateCache();
            dp.getAttractions();
            dp.getServices();
            dp.getEvents();
            dp.getRoutes();
            dp.getAds();
        }
        if( user.equals("") ){
            intent = new Intent(this, Login.class);
        }else{
            intent = new Intent(this, Home.class);
        }

        startActivity(intent);
        finish();
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
