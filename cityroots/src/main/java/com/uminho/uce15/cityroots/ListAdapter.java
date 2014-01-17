package com.uminho.uce15.cityroots;

/**
 * Created by root on 04-01-2014.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.uminho.uce15.cityroots.objects.Attraction;
import com.uminho.uce15.cityroots.objects.Event;
import com.uminho.uce15.cityroots.objects.Poi;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private List lista;


    public ListAdapter(Activity context,List lista) {

        super(context, R.layout.category_listelem, lista);
        this.context = context;

        this.lista = lista;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.category_listelem, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listelem_txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listelem_img);

        txtTitle.setText(((Poi) lista.get(position)).getName());
        //rtbar.setRating((float)((Poi) lista.get(position)).getRating());



        return rowView;
    }

}
