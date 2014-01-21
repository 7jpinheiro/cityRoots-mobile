package com.uminho.uce15.cityroots;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;

/**
 * Created by lgomes on 21-01-2014.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView bmImage;
    ProgressBar loadBar;

    public DownloadImageTask(ImageView bmImage, ProgressBar loadBar) {
        this.bmImage = bmImage;
        this.loadBar = loadBar;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null ;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            if(in!=null) mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            //Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        loadBar.setVisibility(View.GONE);
    }
}