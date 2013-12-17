package com.uminho.uce15.cityroots;

/**
 * Created by lgomes on 15-12-2013.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class Lista extends ArrayAdapter<String> {

        private final Activity context;
        //private final JSONArray jsonArray;
        private Integer[] imageId;
        private String[] web;

        public Lista(Activity context,String[] web,Integer[] imageId) {

            super(context, R.layout.elemento_lista, web);
            this.context = context;
            //this.jsonArray = jsonArray;
            this.imageId = imageId;
            this.web = web;
        }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.elemento_lista, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        /*try {
            txtTitle.setText(jsonArray.getJSONObject(position).getString("Name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        txtTitle.setText(web[position]);
        //imageView.setImageResource(imageId[position]);
        imageView.setImageResource(R.drawable.images);
        return rowView;
    }
}
