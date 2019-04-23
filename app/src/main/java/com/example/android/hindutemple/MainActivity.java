package com.example.android.hindutemple;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.hindutemple.model.Temples;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import networkutils.FirebaseDatabaseUtils;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    private Toolbar toolbar;
    private RecyclerView mTempleListRecyclerView;
    private TemplesListAdapter templesListAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DatabaseReference databaseTemples;
    private DatabaseReference databaseTemplesList;

    private List<Temples> mTemplesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.app_toolbar);

        setSupportActionBar(toolbar);

        mTempleListRecyclerView = (RecyclerView) findViewById(R.id.rv_temples_list);

        // using this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mTempleListRecyclerView.setHasFixedSize(true);

        // using a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mTempleListRecyclerView.setLayoutManager(mLayoutManager);

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

                    databaseTemplesList = FirebaseDatabaseUtils.getDatabase().getReference("temples").child(key);

                    setTempleList();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                templesListAdapter = new TemplesListAdapter(mTemplesList);
                mTempleListRecyclerView.setAdapter(templesListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
