package com.uminho.uce15.cityroots;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Home extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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


    public void onClickCities(View view){
        Intent intent = new Intent(this, CidadesActivity.class);
        startActivity(intent);
    }

    public void onClickRoutes(View view){
        Intent intent = new Intent(this, ListarRotas.class);
        startActivity(intent);
    }

    public void onClickPoI(View view){
        Intent intent = new Intent(this, ListarPOIs.class);
        startActivity(intent);
    }

    public void onClickEvents(View view){
        Intent intent = new Intent(this, ListarEventos.class);
        startActivity(intent);
    }

    public void onClickToVisit(View view){
        Intent intent = new Intent(this, ToVisitActivity.class);
        startActivity(intent);
    }


}
