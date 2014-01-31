package com.uminho.uce15.cityroots;

import android.app.Activity;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.uminho.uce15.cityroots.data.Comment;
import com.uminho.uce15.cityroots.data.Route;

import java.util.ArrayList;
import java.util.List;

public class ListRoteiros extends ActionBarActivity {
    private ArrayList <Route> routes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_roteiros);

        DataProvider dp = new DataProvider(getApplicationContext());

        routes = (ArrayList<Route>) dp.getRoutes();

        ListView list = (ListView) findViewById(R.id.listView_roteiros);
        ListAdapter adapter = new ListAdapter(ListRoteiros.this, routes);



        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                //Starting new intent
                Intent intent = new Intent(ListRoteiros.this, DrawRoute.class);
                intent.putExtra("id", ""+(routes.get(position)).getId());
                startActivity(intent);
            }
        });

        list.setAdapter(adapter);
    }

    public class ListAdapter extends ArrayAdapter<String> {

        private final Activity context;
        private List lista;


        public ListAdapter(Activity context, List lista) {

            super(context, R.layout.list_roteiros_elem, lista);
            this.context = context;

            this.lista = lista;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            LayoutInflater inflater = context.getLayoutInflater();
            View rowView= inflater.inflate(R.layout.list_roteiros_elem, null, true);

            TextView route_name = (TextView) rowView.findViewById(R.id.listelem_name);
            route_name.setText(((Route) lista.get(position)).getName());


            return rowView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list_roteiros, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_list_roteiros, container, false);
            return rootView;
        }
    }

}
