package com.uminho.uce15.cityroots;

/**
 * Created by root on 04-01-2014.
 */
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.uminho.uce15.cityroots.data.Event;
import com.uminho.uce15.cityroots.data.Poi;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter implements Filterable {

    private final Activity context;
    private List lista;
    private List listaOrig;

    public ListAdapter(Activity context,List lista) {


        this.context = context;

        this.lista = lista;
    }
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return lista.size();
    }

    @Override
    public Object getItem(int i) {
        return lista.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.category_listelem, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.listelem_txt);
        TextView txtDesc = (TextView) rowView.findViewById(R.id.listelem_desc);
        RatingBar rtbar = (RatingBar) rowView.findViewById(R.id.listelem_ratingBar);
        ProgressBar loadBar = (ProgressBar) rowView.findViewById(R.id.loadingImg);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listelem_img);


        txtTitle.setText(((Poi) lista.get(position)).getName());

        try{
        String photo_path = ((Poi) lista.get(position)).getPhotos().get(0);
        new DownloadImageTask((ImageView) imageView, (ProgressBar) loadBar ).execute(photo_path);
        }
        catch(Exception e){
            imageView.setImageResource(R.drawable.abc_ab_bottom_solid_dark_holo);
        }


        if(lista.get(position).getClass() == Event.class){
            txtDesc.setText(((Event) lista.get(position)).getStart());
            rtbar.setVisibility(View.GONE);
        }
        else rtbar.setRating((float)((Poi) lista.get(position)).getRating());



        return rowView;
    }

    @Override
    public Filter getFilter() {

        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {

                lista = (ArrayList<Poi>) results.values;
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();
                ArrayList<Poi> FilteredPois = new ArrayList<Poi>();

                if (listaOrig == null) {
                    listaOrig  = new ArrayList<Poi>(lista);
                }
                if (constraint == null || constraint.length() == 0) {
                    results.count = listaOrig.size();
                    results.values = listaOrig;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < listaOrig.size(); i++) {
                        Poi poi = (Poi) listaOrig.get(i);
                        if (poi.getName().toLowerCase()
                                .contains(constraint.toString())) {
                            FilteredPois.add(poi);
                        }
                    }

                    results.count = FilteredPois.size();
                    // System.out.println(results.count);

                    results.values = FilteredPois;
                    // Log.e("VALUES", results.values.toString());
                }

                return results;
            }
        };

        return filter;
    }

}
