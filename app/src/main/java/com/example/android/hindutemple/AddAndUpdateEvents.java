package com.example.android.hindutemple;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.android.hindutemple.model.Events;
import com.example.android.hindutemple.model.Temples;
import com.example.android.hindutemple.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import networkutils.FirebaseDatabaseUtils;

public class AddAndUpdateEvents extends AppCompatActivity implements AdapterView.OnItemSelectedListener, EventsAdapter.ItemClickListener {

    private static final String TAG = AddAndUpdateEvents.class.getSimpleName();
    private static final String EVENT_NAME = "event_name";
    private static final String EVENT_DATE = "event_date";
    private static final String EVENT_TIME = "event_time";

    private Calendar calendar;

    private TextInputLayout mInputStartDate;
    private TextInputLayout mInputTime;
    private TextInputLayout mTextInputLayoutEventName;
    private RecyclerView mRecyclerviewEventsList;

    private DatabaseReference databaseEvents;
    private DatabaseReference databaseTemples;
    private String mTempleId;

    private List<Temples> templeList = new ArrayList<>();
    private ArrayAdapter<Temples> adapter;
    private List<Events> eventsList = new ArrayList<>();
    private EventsAdapter eventsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_and_update_events);

        calendar = Calendar.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();

        if(mUser == null || mUser.getUid().isEmpty()){
            return;
        }

        databaseTemples = FirebaseDatabaseUtils.getDatabase().getReference("temples").child(mUser.getUid());

        Toolbar mToolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(mToolbar);

        mInputStartDate = findViewById(R.id.editText_event_date);
        mInputTime = findViewById(R.id.editText_event_time);
        Spinner mSpinnerDisplayTempleEvents = findViewById(R.id.spinner_templelist_events);
        mTextInputLayoutEventName = findViewById(R.id.editText_event_name);
        adapter =  new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, templeList);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mSpinnerDisplayTempleEvents.setAdapter(adapter);
        mSpinnerDisplayTempleEvents.setSelection(0);
        mSpinnerDisplayTempleEvents.setOnItemSelectedListener(this);
        mRecyclerviewEventsList = findViewById(R.id.recyclerview_events);
        mRecyclerviewEventsList.setHasFixedSize(true);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerviewEventsList.setLayoutManager(layoutManager);
        mRecyclerviewEventsList.setNestedScrollingEnabled(false);
        
        getTempleList();

    }

    private void getTempleList() {

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

    public void setDate(View view){
        int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        showDate(year, month, day);

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void setTime(View view){
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        if(hourOfDay == 0 || minute == 0){
                            return;
                        }

                        String format =  ((hourOfDay > 12) ? hourOfDay % 12 : hourOfDay) + ":" + (minute < 10 ? ("0" + minute) : minute) + " " + ((hourOfDay >= 12) ? "PM" : "AM");

                        if(mInputTime.getEditText() != null){
                            mInputTime.getEditText().setText(format);
                        }

                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void showDate(int year, int month, int day) {
        if(mInputStartDate.getEditText() != null){
            mInputStartDate.getEditText().setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
    }

    public void saveEvents(View view) {

        if( mTextInputLayoutEventName.getEditText() == null ||
                mTextInputLayoutEventName.getEditText().getText() == null ||
                mInputStartDate.getEditText() == null || mInputStartDate.getEditText().getText() == null
                || mInputTime.getEditText() == null || mInputTime.getEditText().getText() == null ){
            return;
        }

        String eventName = mTextInputLayoutEventName.getEditText().getText().toString().trim();
        String eventDate = mInputStartDate.getEditText().getText().toString().trim();
        String eventTime = mInputTime.getEditText().getText().toString().trim();

        if(!Constants.validateField(EVENT_NAME, mTextInputLayoutEventName, eventName ) |
                !Constants.validateField(EVENT_DATE, mInputStartDate, eventDate)
                | !Constants.validateField(EVENT_TIME , mInputTime, eventTime)){
            return;
        }

        String id = databaseEvents.push().getKey();

        if(id != null){

            Events events = new Events(id, eventName, eventDate, eventTime);

            databaseEvents.child(id).setValue(events);
        }

        mTextInputLayoutEventName.getEditText().setText("");
        mInputStartDate.getEditText().setText("");
        mInputTime.getEditText().setText("");

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Temples temple = templeList.get(i);
        mTempleId = temple.getTempleId();
        databaseEvents = FirebaseDatabaseUtils.getDatabase().getReference("events").child(mTempleId);

        databaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsList.clear();
                for (DataSnapshot eventsSnapshot: dataSnapshot.getChildren()){
                    Events events = eventsSnapshot.getValue(Events.class);
                    eventsList.add(events);
                }

                eventsListAdapter = new EventsAdapter(eventsList, AddAndUpdateEvents.this);
                mRecyclerviewEventsList.setAdapter(eventsListAdapter);
                eventsListAdapter.notifyDataSetChanged();
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

    @Override
    public void onEdit(int clickedItemIndex) {
        String eventName = eventsList.get(clickedItemIndex).getEventName();
        String eventDate = eventsList.get(clickedItemIndex).getEventDate();
        String eventTime = eventsList.get(clickedItemIndex).getEventTime();
        String id = eventsList.get(clickedItemIndex).getEventId();
        showUpdateDialog(id, eventName, eventDate, eventTime);
    }

    private void showUpdateDialog(final String id, String eventName, String eventDate, String eventTime) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.update_event_date_time);

        LayoutInflater inflater =getLayoutInflater();

        View dailogView = inflater.inflate(R.layout.update_event_dailog, null);

        final EditText editTextName = dailogView.findViewById(R.id.update_event_name);
        final EditText editTextDate = dailogView.findViewById(R.id.update_event_date);
        final EditText editTextTime = dailogView.findViewById(R.id.update_event_time);

        Button buttonUpdate = dailogView.findViewById(R.id.button_update);
        Button buttonCancel = dailogView.findViewById(R.id.button_cancel);

        editTextName.setText(eventName);
        editTextDate.setText(eventDate);
        editTextTime.setText(eventTime);

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

                String eventName = editTextName.getText().toString().trim();
                String eventDate = editTextDate.getText().toString().trim();
                String eventTime = editTextTime.getText().toString().trim();

                updateEventInfo(id, eventName, eventDate, eventTime);
                alertDialog.dismiss();
            }
        });
    }

    private void showDeleteDailog(final String timeId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_message);

        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                databaseEvents.child(timeId).removeValue();
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

    private void updateEventInfo(String id, String name, String date, String time){

        Events events = new Events(id, name, date, time);

        databaseEvents.child(id).setValue(events);
    }

    @Override
    public void onDelete(int clickedItemIndex) {
        String id = eventsList.get(clickedItemIndex).getEventId();

        showDeleteDailog(id);
    }
}
