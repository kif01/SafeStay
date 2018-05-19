package com.home.khalil.opendata;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by khalil on 4/21/18.
 */

public class Accommodation implements ClusterItem {
    public int bathrooms;
    public int bedrooms;
    public String city;
    public int guestNumber;
    public String imageUrl;
    public float latitude;
    public String listingUrl;
    public float longitude;
    public String name;
    public String neighbourhood;
    public int price;
    public int rating;
    public String street;
    public String summary;
    public String type;
   // public String id ="0";
   private LatLng mPosition;

    public Accommodation(){

    }


    public Accommodation(int bathrooms, int bedrooms, String city, int guestNumber, String imageUrl, float latitude, String listingUrl, float longitude, String name, String neighbourhood,
                         int price, int rating, String street, String summary, String type){
        this.bathrooms=bathrooms;
        this.bedrooms=bedrooms;
        this.city=city;
        this.guestNumber=guestNumber;
        this.imageUrl=imageUrl;
        this.latitude=latitude;
        this.listingUrl=listingUrl;
        this.longitude=longitude;
        this.name=name;
        this.neighbourhood=neighbourhood;
        this.price=price;
        this.rating=rating;
        this.street=street;
        this.summary=summary;
        this.type=type;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public String getCity() {
        return city;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getListingUrl() {
        return listingUrl;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public int getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    public String getStreet() {
        return street;
    }

    public String getSummary() {
        return summary;
    }

    public String getType() {
        return type;
    }

    @Override
    public LatLng getPosition() {
        mPosition= new LatLng(latitude,longitude);
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

   /* public void setID(String id){
        this.id=id;
    }

    //public String getID(){
        return id;
    }
    public void print(){
        Log.d("niceps",bathrooms+", "+bedrooms+", "+city+", "+guestNumber+", "+imageUrl+", "+latitude+", "+listingUrl+", "+longitude+", "+name+", "+neighbourhood+", "+price+", "+rating+", "+
        street+", "+summary+", "+type+" "+id);
    }*/







}
