package com.home.khalil.opendata;

import android.content.Context;
import android.graphics.Color;
import android.os.Debug;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by khalil on 4/28/18.
 */

public class CustomClusterRenderer extends DefaultClusterRenderer<Accommodation> {
    private final Context mContext;



    public CustomClusterRenderer(Context context, GoogleMap map, ClusterManager<Accommodation> clusterManager) {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override protected void onBeforeClusterItemRendered(Accommodation item, MarkerOptions markerOptions) {
        final BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE);

        markerOptions.icon(markerDescriptor).snippet(item.getPrice()+"!@#%"+item.getImageUrl()+"!@#%"+item.getCity()+"!@#%"+item.getNeighbourhood()+
        "!@#%"+item.getBathrooms()+"!@#%"+item.getBedrooms()+"!@#%"+item.getRating()+"!@#%"+item.getName()+"!@#%"+item.getGuestNumber()+"!@#%"+item.getListingUrl()+"!@#%"+item.getType()
        +"!@#%"+item.getStreet()+"!@#%"+item.getSummary());
    }

    @Override
    protected int getColor(int clusterSize) {
        String color = "#80DEEA";
        switch(clusterSize) {
            case 10:
                color = "#4DD0E1";
                break;
            case 20:
                color = "#26C6DA";
                break;
            case 50:
                color = "#00BCD4";
                break;
            case 100:
                color = "#00ACC1";
                break;
            case 200:
                color = "#0097A7";
                break;
            case 500:
                color = "#00838F";
                break;
            case 1000:
                color = "#006064";
                break;
        }
        return Color.parseColor(color);
    }





}
