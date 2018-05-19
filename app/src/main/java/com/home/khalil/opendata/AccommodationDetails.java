package com.home.khalil.opendata;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class AccommodationDetails extends AppCompatActivity {

    private TextView nameText;
    private TextView locationText;
    private TextView guestNumText;
    private TextView bedroomText;
    private TextView bathroomText;
    private TextView priceText;
    private TextView typeText;
    private TextView rateText;
    private TextView summaryText;
    private ImageView image;
    private RatingBar ratingBar;
    private FloatingActionButton link;
    private DecimalFormat df1 = new DecimalFormat(".#");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation__details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameText= (TextView) findViewById(R.id.detail_name);
        locationText=(TextView) findViewById(R.id.detail_location);
        guestNumText=(TextView) findViewById(R.id.detail_guestNum);
        bedroomText=(TextView) findViewById(R.id.detail_bedroom);
        bathroomText=(TextView) findViewById(R.id.detail_bathroom);
        priceText=(TextView) findViewById(R.id.detail_price);
        typeText=(TextView) findViewById(R.id.detail_type);
        image=(ImageView) findViewById(R.id.detail_image);
        rateText=(TextView) findViewById(R.id.detail_rating_value);
        summaryText=(TextView) findViewById(R.id.quick_summary);
        ratingBar=(RatingBar) findViewById(R.id.detail_rating_bar);
        link= (FloatingActionButton)findViewById(R.id.fab_link);

        Bundle bundle = getIntent().getExtras();
        String price =bundle.getString("price");
        String imageUrl=bundle.getString("imageUrl");
        String city=bundle.getString("city");
        String neighbourhood=bundle.getString("neighbourhood");
        String bathrooms=bundle.getString("bathrooms");
        String bedrooms=bundle.getString("bedrooms");
        double rating=bundle.getDouble("rating");
        double modifiedRate=rating*0.05;
        String name=bundle.getString("name");
        String guestNum=bundle.getString("guestNum");
        final String listingUrl=bundle.getString("listingUrl");
        String type = bundle.getString("type");
        String street= bundle.getString("street");
        String summary= bundle.getString("summary");


        Picasso.get().load(imageUrl).placeholder(R.drawable.progress_image).into(image);
        nameText.setText(name);
        locationText.setText(city+", "+neighbourhood);
        guestNumText.setText(guestNum);
        bedroomText.setText(bedrooms);
        bathroomText.setText(bathrooms);
        priceText.setText(price);
        typeText.setText(type);
        rateText.setText(df1.format(modifiedRate)+"");
        summaryText.setText(summary);
        ratingBar.setRating((float) modifiedRate);

        Log.d("dets",listingUrl);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_link);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(listingUrl));
                startActivity(i);
            }
        });
    }
}
