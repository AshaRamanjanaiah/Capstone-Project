package com.example.android.hindutemple;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.model.Timings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class UpdateTimingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimingsAdapter.ItemClickListener {

    private static final String TAG = UpdateTimingsActivity.class.getSimpleName();

    private Spinner mSpinnerDisplayTemples;
    private TextInputLayout mTextInputLayoutDay;
    private TextInputLayout mTextInputLayoutOpenTime;
    private TextInputLayout mTextInputLayoutCloseTime;
    private Button mButtonUpdateTime;
    private RecyclerView mRecyclerviewTimingsList;

    DatabaseReference databaseTimings;
    DatabaseReference databaseTemples;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String mTempleId;

    List<Temples> templeList = new ArrayList<>();
    ArrayAdapter<Temples> adapter;
    List<Timings> timingsList = new ArrayList<>();
    TimingsAdapter timingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_timings);

        mSpinnerDisplayTemples = (Spinner) findViewById(R.id.spinner_templelist);
        mTextInputLayoutDay = (TextInputLayout) findViewById(R.id.editText_day);
        mTextInputLayoutOpenTime = (TextInputLayout) findViewById(R.id.editText_open_time);
        mTextInputLayoutCloseTime = (TextInputLayout) findViewById(R.id.editText_close_time);
        mButtonUpdateTime = (Button) findViewById(R.id.button_add_time);

        mRecyclerviewTimingsList = (RecyclerView) findViewById(R.id.recyclerview_timings);
        mRecyclerviewTimingsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerviewTimingsList.setLayoutManager(layoutManager);
        mRecyclerviewTimingsList.setNestedScrollingEnabled(false);
        timingsListAdapter = new TimingsAdapter(timingsList, this);
        mRecyclerviewTimingsList.setAdapter(timingsListAdapter);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        databaseTemples = FirebaseDatabase.getInstance().getReference("temples").child(mUser.getUid());

        adapter =  new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, templeList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerDisplayTemples.setAdapter(adapter);
        mSpinnerDisplayTemples.setSelection(0);
        mSpinnerDisplayTemples.setOnItemSelectedListener(this);

        databaseTemples.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                templeList.clear();
                for(DataSnapshot templesSnapshot: dataSnapshot.getChildren()){
                    Temples temple = templesSnapshot.getValue(Temples.class);
                    templeList.add(temple);
                }
                mTempleId = templeList.get(0).getTempleId();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG , "Network error, can not load data");
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Temples temple = templeList.get(i);
        mTempleId = temple.getTempleId();
        databaseTimings = FirebaseDatabase.getInstance().getReference("timings").child(mTempleId);

        databaseTimings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timingsList.clear();
                for (DataSnapshot timingsSnapshot: dataSnapshot.getChildren()){
                    Timings timings = timingsSnapshot.getValue(Timings.class);
                    timingsList.add(timings);
                }

                mRecyclerviewTimingsList.setAdapter(new TimingsAdapter(timingsList, UpdateTimingsActivity.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void saveTimings(View view){

        String day = mTextInputLayoutDay.getEditText().getText().toString().trim();
        String openingTime = mTextInputLayoutOpenTime.getEditText().getText().toString().trim();
        String closingTime = mTextInputLayoutCloseTime.getEditText().getText().toString().trim();

        String id = databaseTimings.push().getKey();

        Timings timings = new Timings(id, day, openingTime, closingTime);

        databaseTimings.child(id).setValue(timings);

        Toast.makeText(this, "Timings saved Successfully", Toast.LENGTH_LONG).show();
    }

    private void showUpdateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_day_and_time);
        builder.setPositiveButton(R.string.update_temple_time, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onEdit(int clickedItemIndex) {
        showUpdateDialog();
        Toast.makeText(this, "Edit clicked", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDelete(int clickedItemIndex) {
        Toast.makeText(this, "delete clicked", Toast.LENGTH_LONG).show();
    }
}
