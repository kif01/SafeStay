package com.home.khalil.opendata;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

/**
 * Created by khalil on 4/28/18.
 */

public class CustomInfoViewAdapter implements GoogleMap.InfoWindowAdapter {

    private LayoutInflater mInflater;

    public CustomInfoViewAdapter(LayoutInflater inflater) {
        this.mInflater = inflater;
    }





    @Override
    public View getInfoWindow(Marker marker) {
        final View popup = mInflater.inflate(R.layout.info_window_layout, null);
        String[] parts = marker.getSnippet().split("!@#%");
        ((TextView) popup.findViewById(R.id.info_price)).setText(parts[0]);

        ImageView imageView= (ImageView) popup.findViewById(R.id.info_image);
        Picasso.get().load(parts[1]).placeholder(R.drawable.progress_image).into(imageView);





        return popup;

    }

    @Override
     public View getInfoContents(Marker marker) {
       final View popup = mInflater.inflate(R.layout.info_window_layout, null);
        String[] parts = marker.getSnippet().split("!@#%");



        ((TextView) popup.findViewById(R.id.info_price)).setText(parts[0]);

        ImageView imageView= (ImageView) popup.findViewById(R.id.info_image);
        Picasso.get().load(parts[1]).placeholder(R.drawable.progress_image).into(imageView);




        return popup;

    }
}
