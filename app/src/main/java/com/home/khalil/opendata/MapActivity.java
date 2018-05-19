package com.home.khalil.opendata;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import qiu.niorgai.StatusBarCompat;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {


    private TileProvider mProvider;
    private TileOverlay mOverlay;
    private FirebaseFirestore mFirestore;
    private ArrayList<Accommodation> accommodationList;
    private ClusterManager<Accommodation> mClusterManager;
    SupportMapFragment mapFragment;
    private FloatingActionButton fabSearch;
    private FloatingActionButton reset;
    private FloatingActionButton fabList;
    private GoogleMap mMap;
    private ProgressBar spinner;

    private boolean filtered;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFirestore=FirebaseFirestore.getInstance();
        filtered=false;
        spinner = (ProgressBar)findViewById(R.id.progressBar2);
        accommodationList =new ArrayList<>();
        fabSearch=(FloatingActionButton) findViewById(R.id.fab_search);
        reset=(FloatingActionButton)findViewById(R.id.fab_world);
        fabList=(FloatingActionButton) findViewById(R.id.fab_list);

        StatusBarCompat.translucentStatusBar(this);

        View mapView = mapFragment.getView();


        if (mapView != null &&
                mapView.findViewById(Integer.parseInt("1")) != null) {
            // Get the button view
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            // and next place it, on bottom right (as Google Maps app)
            View mapCompass = ((View) mapView.findViewById(Integer.parseInt("4")).getParent());

            mapCompass.setPadding(20,70,0,0);




            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                    locationButton.getLayoutParams();
            // position on right bottom

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            layoutParams.setMargins(00, 130, 0, 0);
        }

        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MapActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);



        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                                    .build(MapActivity.this);
                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    // Log.e(TAG, e.getStackTrace().toString());
                }
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMap.clear();
                addHeatMap();
                spinner.setVisibility(View.VISIBLE);
                filtered=false;

                setCluster();
                addItems();
                reset.hide();

            }
        });






    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //spinner.setVisibility(View.VISIBLE);
        mMap = googleMap;

        LatLng loc= new LatLng(51.509865,-0.118092);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc)      // Sets the center of the map to Mountain View
                .zoom(6)                   // Sets the zoom
                // Sets the orientation of the camera to east
                .tilt(90)                   // Sets the tilt of the camera to 90 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        ;
        addHeatMap();
       // mMap.animateCamera(CameraUpdateFactory.zoomIn(),2000,null);
        addItems();




        setCluster();




    }

    private void addItems() {

        mFirestore.collection("home").
                addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if(e!= null){

                            Log.d("Error", "Error:"+ e.getMessage());
                        }

                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String id=doc.getDocument().getId();
                                Accommodation p= doc.getDocument().toObject(Accommodation.class);

                                //Log.d("herex",emp.getId());
                                accommodationList.add(p);
                                mClusterManager.addItem(p);
                                //mAdapter.notifyDataSetChanged();

                            }
                        }

                        Log.d("here", accommodationList.get(0).getPosition()+"");
                        spinner.setVisibility(View.GONE);
                        LatLng loc= new LatLng(51.509865,-0.118092);
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(loc)      // Sets the center of the map to Mountain View
                                .zoom(6)                   // Sets the zoom
                                // Sets the orientation of the camera to east
                                .tilt(90)                   // Sets the tilt of the camera to 90 degrees
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        mClusterManager.cluster();

                    }
                });






    }

    private void addHeatMap() {

        int[] colors = {
              //Color.rgb(244,227,0),
               Color.rgb( 249, 198, 0),
                  Color.rgb( 255, 180, 0),
                  Color.rgb( 255, 170, 0),
                  Color.rgb( 255, 160, 0),
                  Color.rgb( 255, 140, 0),
                  Color.rgb( 255, 120, 0),
                  Color.rgb( 255, 100, 0),
                  Color.rgb(255, 75, 0),
                  Color.rgb(255, 57, 0),
                  Color.rgb( 255, 0, 0)

//               Color.rgb( 255, 0, 0),
//                Color.rgb(255, 57, 0),
//                Color.rgb(255, 75, 0),
//                Color.rgb( 255, 100, 0),
//                Color.rgb( 255, 120, 0),
//                Color.rgb( 255, 140, 0),
//                Color.rgb( 255, 160, 0),
//                Color.rgb( 255, 170, 0),
//                Color.rgb( 255, 180, 0),
//                Color.rgb( 249, 198, 0)



        };

        float[] startPoints = {
            //   0.2f, 1f,2f,3f,4f,5f,6f,7f,8f,9f,10f
                0.1f,0.2f,0.3f,0.4f,0.5f,0.6f,0.7f,0.8f,0.9f,1f
//                0f,1f,1f,1f,1f,1f,1f,1f,1f,1f,1f
               // 0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);

       /* int[] colors = {
                Color.rgb(102, 225, 0), // green
                Color.rgb(255, 0, 0)    // red
        };

        float[] startPoints = {
                0.2f, 1f
        };

        Gradient gradient = new Gradient(colors, startPoints);*/

        ArrayList<LatLng> list = null;

        // Get the data: latitude/longitude positions of police stations.
        try {
            list = readItems(R.raw.crime_rates);
           // Log.d("list", list.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list).radius(30).gradient(gradient)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getResources().openRawResource(resource);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("Lat");
            double lng = object.getDouble("Lon");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ArrayList<Accommodation> list=new ArrayList<>();
        if(requestCode == 1){
            if (resultCode == RESULT_OK) {
                spinner.setVisibility(View.VISIBLE);
                final Place place = PlaceAutocomplete.getPlace(this, data);
                filtered=true;
                mMap.clear();
                addHeatMap();

                 setCluster();
                // Add cluster items (markers) to the cluster manager.
                Log.d("here2", "onActivityResult: "+place.getName());

                mFirestore.collection("home").whereEqualTo("city",place.getName()).
                        addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                                if(e!= null){

                                    Log.d("Error", "Error:"+ e.getMessage());
                                }

                                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                    if (doc.getType() == DocumentChange.Type.ADDED) {
                                        String id=doc.getDocument().getId();
                                        Accommodation p= doc.getDocument().toObject(Accommodation.class);

                                        //Log.d("herex",emp.getId());
                                        list.add(p);
                                        mClusterManager.addItem(p);
                                        //mAdapter.notifyDataSetChanged();

                                    }
                                }
                                spinner.setVisibility(View.INVISIBLE);

                                CameraPosition cameraPosition = new CameraPosition.Builder()
                                        .target(place.getLatLng())      // Sets the center of the map to Mountain View
                                        .zoom(6)                   // Sets the zoom
                                        // Sets the orientation of the camera to east
                                        .tilt(90)                   // Sets the tilt of the camera to 90 degrees
                                        .build();                   // Creates a CameraPosition from the builder
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                mClusterManager.cluster();
                                reset.show();
                               // Log.d("here", list.get(0).getCity());


                            }
                        });

            }
        }
    }

    private void setCluster(){
        mClusterManager = new ClusterManager<Accommodation>(this, mMap);
        final  CustomClusterRenderer renderer = new CustomClusterRenderer(this, mMap, mClusterManager);

        mClusterManager.setRenderer(renderer);

        mClusterManager.getMarkerCollection()
                .setOnInfoWindowAdapter(new CustomInfoViewAdapter(LayoutInflater.from(this)));

        mMap.setInfoWindowAdapter(mClusterManager.getMarkerManager());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);





        mClusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<Accommodation>() {
            @Override
            public boolean onClusterItemClick(Accommodation accommodation) {
                String img=accommodation.getImageUrl();

                //renderer.getMarker(accommodation).showInfoWindow();
               // Log.d("click","image: "+ img);




                return false;
            }
        });



        mClusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<Accommodation>() {
            @Override
            public void onClusterItemInfoWindowClick(Accommodation accommodation) {
                Log.d("here","clicked");
                Bundle bundle = new Bundle();
                String[] parts =renderer.getMarker(accommodation).getSnippet().split("!@#%");
                String price =parts[0];
                String imageUrl=parts[1];
                String city=parts[2];
                String neighbourhood=parts[3];
                String bathrooms=parts[4];
                String bedrooms=parts[5];
                double rating=Double.parseDouble(parts[6]);
                String name=parts[7];
                String guestNum=parts[8];
                String listingUrl=parts[9];
                String type = parts[10];
                String street= parts[11];
                String s= parts[12];
                String summary=Character.toUpperCase(s.charAt(0))+s.substring(1);

                bundle.putString("price",price);
                bundle.putString("imageUrl",imageUrl);
                bundle.putString("city",city);
                bundle.putString("neighbourhood",neighbourhood);
                bundle.putString("bathrooms",bathrooms);
                bundle.putString("bedrooms",bedrooms);
                bundle.putDouble("rating",rating);
                bundle.putString("name",name);
                bundle.putString("guestNum",guestNum);
                bundle.putString("listingUrl",listingUrl);
                bundle.putString("type",type);
                bundle.putString("street",street);
                bundle.putString("summary",summary);



                Intent i = new Intent(MapActivity.this, AccommodationDetails.class);

                i.putExtras(bundle);
                startActivity(i);

            }
        });

        mMap.setOnInfoWindowClickListener(mClusterManager);

    }





}
