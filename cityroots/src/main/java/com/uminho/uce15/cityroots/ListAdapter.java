package com.uminho.uce15.cityroots;

/**
 * Created by root on 04-01-2014.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private Integer[] imageId;
    private String[] web;

    public ListAdapter(Activity context,String[] web,Integer[] imageId) {

        super(context, R.layout.category_listelem, web);
        this.context = context;

        this.imageId = imageId;
        this.web = web;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.category_listelem, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.listelem_txt);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listelem_img);


        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);

        return rowView;
    }
}
