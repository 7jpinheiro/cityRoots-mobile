package com.uminho.uce15.cityroots;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RootActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(RootActivity.this);
        String user = prefs.getString("username", "");
        Intent intent;

        if( user.equals("") ){
            intent = new Intent(this, Login.class);
        }else{
            intent = new Intent(this, Home.class);
        }

        startActivity(intent);
        finish();
    }
}
