package com.example.android.hindutemple;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.utils.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import networkutils.FirebaseDatabaseUtils;

public class MainActivity extends AppCompatActivity implements TemplesListAdapter.CardviewClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mTempleListRecyclerView;
    private TemplesListAdapter templesListAdapter;

    private DatabaseReference databaseTemples;
    private DatabaseReference databaseTemplesList;

    private ArrayList<Temples> mTemplesList = new ArrayList<>();

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.app_toolbar);

        setSupportActionBar(toolbar);

        mTempleListRecyclerView = findViewById(R.id.rv_temples_list);

        // using this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mTempleListRecyclerView.setHasFixedSize(true);

        // using a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mTempleListRecyclerView.setLayoutManager(mLayoutManager);

        MobileAds.initialize(this, "ca-app-pub-3601817193499741~3953624780");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        if(savedInstanceState != null){
            mTemplesList = savedInstanceState.getParcelableArrayList(Constants.TEMPLES_INFO);
        }

        databaseTemples = FirebaseDatabaseUtils.getDatabase().getReference("temples");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
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
    protected void onStart() {
        super.onStart();

        databaseTemples.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mTemplesList.clear();
                for(DataSnapshot templesSnapshot: dataSnapshot.getChildren()){

                    String key = templesSnapshot.getKey();

                    if(key != null) {

                        databaseTemplesList = FirebaseDatabaseUtils.getDatabase().getReference("temples").child(key);

                        setTempleList();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to load Temples data" );
            }
        });

    }

    private void setTempleList() {
        databaseTemplesList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot templesListSnapshot: dataSnapshot.getChildren()){
                    Temples temple = templesListSnapshot.getValue(Temples.class);
                    mTemplesList.add(temple);
                }

                templesListAdapter = new TemplesListAdapter(MainActivity.this, mTemplesList);
                mTempleListRecyclerView.setAdapter(templesListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Failed to load Temples List" );
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(Constants.TEMPLES_INFO, mTemplesList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCardClicked(int position) {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            Log.d(TAG, "The interstitial wasn't loaded yet.");
        }
        Bundle bundle = new Bundle();
        Temples templeInfo = mTemplesList.get(position);
        bundle.putParcelable(Constants.TEMPLES_INFO, templeInfo);
        Intent intent = new Intent(this, TempleDetailActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
