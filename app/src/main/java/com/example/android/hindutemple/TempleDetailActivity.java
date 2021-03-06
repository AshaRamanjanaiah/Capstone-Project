package com.example.android.hindutemple;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.android.hindutemple.model.Events;
import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.model.Timings;
import com.example.android.hindutemple.utils.Constants;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import networkutils.FirebaseDatabaseUtils;

public class TempleDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = TempleDetailActivity.class.getSimpleName();

    private Temples templeInfo;
    private RecyclerView mTimingsRecyclerView;
    private RecyclerView mEventsRecyclerView;

    private List<Timings> timingsList = new ArrayList<>();
    private List<Events> eventsList = new ArrayList<>();

    private TempleDetailTimeAdapter templeDetailTimeAdapter;
    private TempleDetailEventAdapter mTempleDetailEventAdapter;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ImageView mBackgroundImageView;

    private String mTempleId = null;
    private String mTempleName = null;
    private String mTempleImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temple_detail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportPostponeEnterTransition();
        }

        setToolbar();

        mCollapsingToolbarLayout = findViewById(R.id.main_collapsing_layout);
        mBackgroundImageView = findViewById(R.id.iv_main_backdrop);
        mTimingsRecyclerView = findViewById(R.id.rv_templedetail_timings);
        mEventsRecyclerView = findViewById(R.id.rv_templedetail_events);

        mTimingsRecyclerView.setHasFixedSize(false);
        mTimingsRecyclerView.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mTimingsRecyclerView.setNestedScrollingEnabled(false);

        mEventsRecyclerView.setHasFixedSize(false);
        mEventsRecyclerView.setLayoutManager(new
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mEventsRecyclerView.setNestedScrollingEnabled(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if(mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        if(getIntent().getExtras() == null){
            return;
        }

        if(getIntent().hasExtra(Constants.TEMPLES_INFO)){
            templeInfo = getIntent().getParcelableExtra(Constants.TEMPLES_INFO);
            mTempleId = templeInfo.getTempleId();
            mTempleName = templeInfo.getTempleName();
            mTempleImage = templeInfo.getTempleImageUri();
        }else if(getIntent().getExtras().containsKey(Constants.TEMPLE_ID)
                && getIntent().getExtras().containsKey(Constants.TEMPLE_NAME) &&
                getIntent().getExtras().containsKey(Constants.TEMPLE_IMAGE_URL)){
            mTempleId = getIntent().getStringExtra(Constants.TEMPLE_ID);
            mTempleName = getIntent().getExtras().getString(Constants.TEMPLE_NAME);
            mTempleImage = getIntent().getExtras().getString(Constants.TEMPLE_IMAGE_URL);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBackgroundImageView.setTransitionName(getResources().getString(R.string.image_transition)+mTempleId);
        }

        if(mTempleId == null){
                return;
            }
            populateData();
    }

    private void setToolbar() {
        Toolbar mTopToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mTopToolbar);

        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void populateData() {

        mCollapsingToolbarLayout.setTitle(mTempleName);
        Picasso.with(this)
                .load(mTempleImage)
                .placeholder(R.drawable.temple_image)
                .error(R.drawable.temple_image)
                .into(mBackgroundImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        supportStartPostponedEnterTransition();
                    }

                    @Override
                    public void onError() {
                        supportStartPostponedEnterTransition();
                    }
                });

        DatabaseReference mDatabaseTimings = FirebaseDatabaseUtils.getDatabase().getReference("timings").child(mTempleId);
        DatabaseReference mDatabaseEvents = FirebaseDatabaseUtils.getDatabase().getReference("events").child(mTempleId);


        mDatabaseTimings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timingsList.clear();
                for (DataSnapshot timingsSnapshot: dataSnapshot.getChildren()){
                    Timings timings = timingsSnapshot.getValue(Timings.class);
                    timingsList.add(timings);
                }

                templeDetailTimeAdapter = new TempleDetailTimeAdapter(timingsList);
                mTimingsRecyclerView.setAdapter(templeDetailTimeAdapter);
                templeDetailTimeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Network error, Could not load timings");
            }
        });


        mDatabaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsList.clear();
                for (DataSnapshot eventsSnapshot: dataSnapshot.getChildren()){
                    Events events = eventsSnapshot.getValue(Events.class);
                    eventsList.add(events);
                }

                mTempleDetailEventAdapter = new TempleDetailEventAdapter(eventsList);
                mEventsRecyclerView.setAdapter(mTempleDetailEventAdapter);
                mTempleDetailEventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Network error, Could not load events");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.admin_login:
                Intent intent = new Intent(this, AdminLoginActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Add a marker in Bangalore, India, and move the camera.
        LatLng banglore = new LatLng(13.0293077, 77.576516);
        googleMap.addMarker(new MarkerOptions().position(banglore).title("Marker in Bangalore"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(banglore));
    }

    public void shareTempleDetails(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        if(templeInfo != null){
            sendIntent.putExtra(Intent.EXTRA_TEXT, templeInfo.getTempleName()+"\n"+templeInfo.getTempleLocation());
        }else {
            sendIntent.putExtra(Intent.EXTRA_TEXT, mTempleName);
        }

        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.app_chooser)));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
