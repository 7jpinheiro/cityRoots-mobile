package com.uminho.uce15.cityroots;

import android.content.Intent;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.view.GestureDetector.SimpleOnGestureListener;


/*
public class Category extends ActionBarActivity {

    private ListView list;
    private ListAdapter adapter;
    private SlidingPaneLayout slidingPaneLayout;
    private boolean slidingEnabled;
    String[] web= {"1","2","3","4"};
    Integer[] imageId = {
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo,
            R.drawable.abc_ab_bottom_solid_dark_holo
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);


        slidingPaneLayout = (SlidingPaneLayout)findViewById(R.id.pane);
        list=(ListView)findViewById(R.id.list);
        list.setEmptyView(findViewById(android.R.id.empty));

        adapter = new ListAdapter(this,web,imageId);

        list.setAdapter(adapter);
        list.invalidate();
        slidingPaneLayout.openPane();
        slidingEnabled=true;

        slidingPaneLayout.setOnTouchListener(
                new SlidingPaneLayout.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent m) {
                        if (!slidingEnabled) {
                            return true;
                        }
                        return false;
                    }

                }
        );

        slidingPaneLayout.setPanelSlideListener(new SlidingPaneLayout.PanelSlideListener() {


            @Override
            public void onPanelSlide(View view, float v) {
            }

            @Override
            public void onPanelOpened(View view) {
                slidingEnabled = true;
            }

            @Override
            public void onPanelClosed(View view) {
                slidingEnabled = false;
            }
        });

        Bundle b = getIntent().getExtras();
        int value = b.getInt("id_category");

        switch (value){
            case R.id.poi:
                break;
            case R.id.routes:
                break;
            case R.id.events:
                Intent intent = new Intent(this, ListarPoi.class);
                startActivity(intent);
                break;
            case R.id.tpa:
                break;
            case R.id.gastronomy:
                break;
            case R.id.activities:
                break;
            case R.id.outdoor:
                break;
            case R.id.nightlife:
                break;
            case R.id.hotels:
                break;
            case R.id.transport:
                break;
            case R.id.afd:
                break;
            case R.id.contacts:
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.category_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.menu){
            slidingPaneLayout.openPane();
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
            View rootView = inflater.inflate(R.layout.fragment_category, container, false);
            return rootView;
        }
    }

}

*/
