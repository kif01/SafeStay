package com.home.khalil.opendata;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;


import com.claudiodegio.msv.MaterialSearchView;
import com.claudiodegio.msv.OnSearchViewListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.lang.reflect.Modifier;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifTextView;

public class MainActivity extends AppCompatActivity implements OnSearchViewListener {


    private RecyclerView placeRCV;
    private  RecyclerViewPlaceAdapter mAdapter;
    private FirebaseFirestore mFirestore;
    private MaterialSearchView mSearchView;
    private boolean isClosed=true;
    private ProgressBar spinner;
    private ArrayList<Accommodation> accommodationList;
    private FloatingActionButton fabWorld;
private ImageButton likeBtn;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    GifTextView gif;
    public static final ArrayList<Accommodation> favorite = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //img=(ImageView) findViewById(R.id.image);
        //Picasso.get().load("https://a1.muscache.com/im/pictures/7907241/d977e016_original.jpg?aki_policy=x_large").into(img);
        //mDatabase = FirebaseDatabase.getInstance().getReference();
        //mDatabase.keepSynced(true);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        mSearchView = (MaterialSearchView) findViewById(R.id.sv);
        mSearchView.setOnSearchViewListener(this);
        spinner = (ProgressBar)findViewById(R.id.progressBar);

        gif= (GifTextView) findViewById(R.id.gif);

        //likeBtn= (ImageButton) findViewById(R.id.like);
        mFirestore=FirebaseFirestore.getInstance();
        accommodationList =new ArrayList<>();
        mAdapter= new RecyclerViewPlaceAdapter(accommodationList);
        placeRCV= (RecyclerView) findViewById(R.id.recycler_view);
        placeRCV.setHasFixedSize(true);
        placeRCV.setLayoutManager(new LinearLayoutManager(this));
        placeRCV.setAdapter(mAdapter);

        fabWorld=(FloatingActionButton) findViewById(R.id.allhomes);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        mFirestore.setFirestoreSettings(settings);




       fabWorld.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               final int size = accommodationList.size();
               accommodationList.clear();
               mAdapter.notifyItemRangeRemoved(0, size);
                fabWorld.hide();
               //spinner.setVisibility(View.VISIBLE);
               gif.setVisibility(View.VISIBLE);

               mFirestore.collection("home").
                       addSnapshotListener(new EventListener<QuerySnapshot>() {
                           @Override
                           public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                               if (e != null) {

                                   Log.d("Error", "Error:" + e.getMessage());
                               }
                               //  ArrayList<Accommodation> list = new ArrayList<>();
                               accommodationList=new ArrayList<>();
                               for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                   if (doc.getType() == DocumentChange.Type.ADDED) {
                                       String id = doc.getDocument().getId();
                                       Accommodation p = doc.getDocument().toObject(Accommodation.class);

                                       //Log.d("herex",emp.getId());
                                       accommodationList.add(p);
                                       mAdapter.notifyDataSetChanged();

                                   }
                               }
                               //spinner.setVisibility(View.GONE);
                               gif.setVisibility(View.GONE);
                               RecyclerViewPlaceAdapter adapter = new RecyclerViewPlaceAdapter(accommodationList);
                               placeRCV.setHasFixedSize(true);
                               placeRCV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                               placeRCV.setAdapter(adapter);
                           }
                       });

           }
       });

       // spinner.setVisibility(View.VISIBLE);
        gif.setVisibility(View.VISIBLE);
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
                                mAdapter.notifyDataSetChanged();

                            }
                        }
                       // spinner.setVisibility(View.GONE);
                        gif.setVisibility(View.GONE);
                    }
                });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        mSearchView.setMenuItem(item);
        return true;
    }


    @Override
    public void onSearchViewShown() {
        isClosed=false;
    }

    @Override
    public void onSearchViewClosed() {
        isClosed=true;

    }

    @Override
    public boolean onQueryTextSubmit(String str) {
        if(str!=null && !str.isEmpty()) {
            String s=str;
            final int size = accommodationList.size();
            accommodationList.clear();
            mAdapter.notifyItemRangeRemoved(0, size);
            s=Character.toUpperCase(str.charAt(0))+str.substring(1);
           // spinner.setVisibility(View.VISIBLE);
            gif.setVisibility(View.VISIBLE);

            mFirestore.collection("home").whereEqualTo("city",s).
                    addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {

                            if (e != null) {

                                Log.d("Error", "Error:" + e.getMessage());
                            }
                          //  ArrayList<Accommodation> list = new ArrayList<>();
                            accommodationList=new ArrayList<>();
                            for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String id = doc.getDocument().getId();
                                    Accommodation p = doc.getDocument().toObject(Accommodation.class);

                                    //Log.d("herex",emp.getId());
                                   accommodationList.add(p);
                                    mAdapter.notifyDataSetChanged();

                                }
                            }
                           // spinner.setVisibility(View.GONE);
                            gif.setVisibility(View.GONE);
                            RecyclerViewPlaceAdapter adapter = new RecyclerViewPlaceAdapter(accommodationList);
                            placeRCV.setHasFixedSize(true);
                            placeRCV.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                            placeRCV.setAdapter(adapter);
                            fabWorld.show();
                        }
                    });
        }
        return false;
    }

    @Override
    public void onQueryTextChange(String s) {



        }

    @Override
    public void onBackPressed() {
        if(isClosed==false) {
            Log.d("CDA", "onBackPressed Called");
            mSearchView.closeSearch();
        }else{
            finish();
        }
    }



}

