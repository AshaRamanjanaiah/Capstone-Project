package com.example.android.hindutemple;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.model.Timings;
import com.example.android.hindutemple.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import networkutils.FirebaseDatabaseUtils;

public class UpdateTimingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TimingsAdapter.ItemClickListener {

    private static final String TAG = UpdateTimingsActivity.class.getSimpleName();

    private static final String DAY = "day";
    private static final String OPENING_TIME = "opening_time";
    private static final String CLOSING_TIME = "closing_time";

    private TextInputLayout mTextInputLayoutDay;
    private TextInputLayout mTextInputLayoutOpenTime;
    private TextInputLayout mTextInputLayoutCloseTime;
    private RecyclerView mRecyclerviewTimingsList;

    private DatabaseReference databaseTimings;
    private String mTempleId;

    private List<Temples> templeList = new ArrayList<>();
    private ArrayAdapter<Temples> adapter;
    private List<Timings> timingsList = new ArrayList<>();
    private TimingsAdapter timingsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_timings);

        Spinner mSpinnerDisplayTemples = findViewById(R.id.spinner_templelist);
        mTextInputLayoutDay = findViewById(R.id.editText_day);
        mTextInputLayoutOpenTime = findViewById(R.id.editText_open_time);
        mTextInputLayoutCloseTime = findViewById(R.id.editText_close_time);
        Toolbar mToolbar = findViewById(R.id.admin_toolbar);
        setSupportActionBar(mToolbar);

        mRecyclerviewTimingsList = findViewById(R.id.recyclerview_timings);
        mRecyclerviewTimingsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerviewTimingsList.setLayoutManager(layoutManager);
        mRecyclerviewTimingsList.setNestedScrollingEnabled(false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if(mUser == null || mUser.getUid().isEmpty()){
            return;
        }

        DatabaseReference databaseTemples = FirebaseDatabaseUtils.getDatabase().getReference("temples").child(mUser.getUid());

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
                Log.e(TAG , "Network error, can not load temple data");
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.event_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){
            case R.id.add_events:
                Intent intent = new Intent(this, AddAndUpdateEvents.class);
                startActivity(intent);
                return true;
                default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Temples temple = templeList.get(i);
        mTempleId = temple.getTempleId();
        databaseTimings = FirebaseDatabaseUtils.getDatabase().getReference("timings").child(mTempleId);

        databaseTimings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                timingsList.clear();
                for (DataSnapshot timingsSnapshot: dataSnapshot.getChildren()){
                    Timings timings = timingsSnapshot.getValue(Timings.class);
                    timingsList.add(timings);
                }

                timingsListAdapter = new TimingsAdapter(timingsList, UpdateTimingsActivity.this);
                mRecyclerviewTimingsList.setAdapter(timingsListAdapter);
                timingsListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG , "Network error, can not load timings data");
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void saveTimings(View view){

        if(mTextInputLayoutDay.getEditText() == null || mTextInputLayoutDay.getEditText().getText() == null
                || mTextInputLayoutOpenTime.getEditText() == null || mTextInputLayoutOpenTime.getEditText().getText() == null
                || mTextInputLayoutCloseTime.getEditText()  == null || mTextInputLayoutCloseTime.getEditText().getText() == null ){
            return;
        }

        String day = mTextInputLayoutDay.getEditText().getText().toString().trim();
        String openingTime = mTextInputLayoutOpenTime.getEditText().getText().toString().trim();
        String closingTime = mTextInputLayoutCloseTime.getEditText().getText().toString().trim();

        if(!Constants.validateField(DAY, mTextInputLayoutDay, day ) |
                !Constants.validateField(OPENING_TIME, mTextInputLayoutOpenTime, openingTime)
        | !Constants.validateField(CLOSING_TIME , mTextInputLayoutCloseTime, closingTime)){
            return;
        }

        String id = databaseTimings.push().getKey();

        if(id != null){

            Timings timings = new Timings(id, day, openingTime, closingTime);

            databaseTimings.child(id).setValue(timings);

        }

        mTextInputLayoutDay.getEditText().setText("");
        mTextInputLayoutOpenTime.getEditText().setText("");
        mTextInputLayoutCloseTime.getEditText().setText("");
    }

    private void showUpdateDialog(final String id, final String day, final String openTime, final String closeTime){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_day_and_time);

        LayoutInflater inflater =getLayoutInflater();

        View dailogView = inflater.inflate(R.layout.update_time_dailog, null);

        final EditText editTextDay = dailogView.findViewById(R.id.dailog_update_day);
        final EditText editTextOpneTime = dailogView.findViewById(R.id.dailog_update_opntime);
        final EditText editTextCloseTime = dailogView.findViewById(R.id.dailog_update_clstime);

        Button buttonUpdate = dailogView.findViewById(R.id.dailog_update_button);
        Button buttonCancel = dailogView.findViewById(R.id.dailog_update_cancel);

        editTextDay.setText(day);
        editTextOpneTime.setText(openTime);
        editTextCloseTime.setText(closeTime);

        builder.setView(dailogView);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String weekDay = editTextDay.getText().toString().trim();
                String timeOpen = editTextOpneTime.getText().toString().trim();
                String timeClose = editTextCloseTime.getText().toString().trim();

                updateTimingsInfo(id, weekDay, timeOpen, timeClose);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void onEdit(int itemIndex) {
        String day = timingsList.get(itemIndex).getTimingsDay();
        String openTime = timingsList.get(itemIndex).getTimingsOpen();
        String closeTime = timingsList.get(itemIndex).getTimingsClose();
        String id = timingsList.get(itemIndex).getTimingsId();
        showUpdateDialog(id, day, openTime, closeTime);
    }

    @Override
    public void onDelete(int clickedItemIndex) {
        String id = timingsList.get(clickedItemIndex).getTimingsId();
        
        showDeleteDailog(id);
    }

    private void showDeleteDailog(final String timeId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_message);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                databaseTimings.child(timeId).removeValue();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateTimingsInfo(String id, String day, String openTime, String closetime){

        Timings temple = new Timings(id, day, openTime, closetime);

        databaseTimings.child(id).setValue(temple);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(mTextInputLayoutDay.getEditText() == null || mTextInputLayoutDay.getEditText().getText() == null
                || mTextInputLayoutOpenTime.getEditText() == null || mTextInputLayoutOpenTime.getEditText().getText() == null
                || mTextInputLayoutCloseTime.getEditText()  == null || mTextInputLayoutCloseTime.getEditText().getText() == null ){
            return;
        }

        String day = mTextInputLayoutDay.getEditText().getText().toString().trim();
        String openingTime = mTextInputLayoutOpenTime.getEditText().getText().toString().trim();
        String closingTime = mTextInputLayoutCloseTime.getEditText().getText().toString().trim();

        outState.putString(DAY, day);
        outState.putString(OPENING_TIME, openingTime);
        outState.putString(CLOSING_TIME, closingTime);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if(mTextInputLayoutDay.getEditText() == null || mTextInputLayoutOpenTime.getEditText() == null
                ||  mTextInputLayoutCloseTime.getEditText()  == null ){
            return;
        }
        mTextInputLayoutDay.getEditText().setText(savedInstanceState.getString(DAY));
        mTextInputLayoutOpenTime.getEditText().setText(savedInstanceState.getString(OPENING_TIME));
        mTextInputLayoutCloseTime.getEditText().setText(savedInstanceState.getString(CLOSING_TIME));
    }
}
