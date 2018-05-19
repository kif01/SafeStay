package com.home.khalil.opendata;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by khalil on 4/26/18.
 */

public class RecyclerViewPlaceAdapter extends RecyclerView.Adapter<RecyclerViewPlaceAdapter.ViewHolder> {
    public ArrayList<Accommodation> accommodationList;
    private DecimalFormat df1 = new DecimalFormat(".#");


    public RecyclerViewPlaceAdapter(ArrayList<Accommodation> accommodationList){
        this.accommodationList = accommodationList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_row,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name= accommodationList.get(position).getName();
        String locationText= accommodationList.get(position).getCity()+", "+ accommodationList.get(position).getNeighbourhood();
        int guestNum= accommodationList.get(position).getGuestNumber();
        int bedrooms= accommodationList.get(position).getBedrooms();
        int bathrooms= accommodationList.get(position).getBathrooms();
        int price= accommodationList.get(position).getPrice();
        String type= accommodationList.get(position).getType();
        String imageUri= accommodationList.get(position).getImageUrl();
        int rate= accommodationList.get(position).getRating();
        String neighbourhood = accommodationList.get(position).getNeighbourhood();
        String city=accommodationList.get(position).getCity();
        String listingUrl = accommodationList.get(position).getListingUrl();
        String street = accommodationList.get(position).getStreet();
        String summary= accommodationList.get(position).getSummary();
        double modifiedRate=rate*0.05;


        holder.nameText.setText(name);
        holder.locationText.setText(locationText);
        holder.guestNumTest.setText(guestNum+"");
        holder.bedroomText.setText(bedrooms+"");
        holder.bathroomText.setText(bathrooms+"");
        holder.priceText.setText(price+"");
        holder.typeText.setText(type);
        holder.rateText.setText(df1.format(modifiedRate)+"");
        Picasso.get().load(imageUri).placeholder(R.drawable.progress_image).into(holder.image);

        final Bundle bundle = new Bundle();


        bundle.putString("price",price+"");
        bundle.putString("imageUrl",imageUri);
        bundle.putString("city",city);
        bundle.putString("neighbourhood",neighbourhood);
        bundle.putString("bathrooms",bathrooms+"");
        bundle.putString("bedrooms",bedrooms+"");
        bundle.putDouble("rating",rate);
        bundle.putString("name",name);
        bundle.putString("guestNum",guestNum+"");
        bundle.putString("listingUrl",listingUrl);
        bundle.putString("type",type);
        bundle.putString("street",street);
        bundle.putString("summary",summary);

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AccommodationDetails.class);
                i.putExtras(bundle);
                view.getContext().startActivity(i);
            }
        });



    }

    @Override
    public int getItemCount() {
        return accommodationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public TextView nameText;
        public TextView locationText;
        public TextView guestNumTest;
        public TextView bedroomText;
        public TextView bathroomText;
        public TextView priceText;
        public TextView typeText;
        public TextView rateText;
        public ImageView image;
        public CardView card;

        public ViewHolder(View itemView){
            super(itemView);
            mView=itemView;
            nameText= (TextView) mView.findViewById(R.id.name);
            locationText=(TextView) mView.findViewById(R.id.location);
            guestNumTest=(TextView) mView.findViewById(R.id.guestNum);
            bedroomText=(TextView) mView.findViewById(R.id.bedroom);
            bathroomText=(TextView) mView.findViewById(R.id.bathroom);
            priceText=(TextView) mView.findViewById(R.id.price);
            typeText=(TextView) mView.findViewById(R.id.accomodation_type);
            image=(ImageView) mView.findViewById(R.id.accomodation_image);
            rateText=(TextView) mView.findViewById(R.id.rate);
            card= (CardView) mView.findViewById(R.id.card_view);

        }



    }
}
