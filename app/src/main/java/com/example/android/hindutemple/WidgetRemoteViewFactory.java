package com.example.android.hindutemple;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.hindutemple.model.Events;
import com.example.android.hindutemple.utils.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import networkutils.FirebaseDatabaseUtils;

class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = WidgetRemoteViewFactory.class.getSimpleName();

    private Context mContext;
    private ArrayList<Events> eventsArrayList =  new ArrayList<>();
    private String templeId;

    public WidgetRemoteViewFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        if(intent.hasExtra(Constants.TEMPLE_ID)){
            templeId = intent.getStringExtra(Constants.TEMPLE_ID);
        }
    }

    @Override
    public void onCreate() {
        DatabaseReference mDatabaseEvents = FirebaseDatabaseUtils.getDatabase()
                .getReference("events").child(templeId);

        mDatabaseEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventsArrayList.clear();
                for (DataSnapshot eventsSnapshot: dataSnapshot.getChildren()){
                    Events events = eventsSnapshot.getValue(Events.class);
                    eventsArrayList.add(events);
                }
                Log.d(TAG, "Got events Data");
                AppWidgetManager mgr = AppWidgetManager.getInstance(mContext);
                int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(mContext,TempleWidgetProvider.class));
                mgr.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Something went wrong, could not get events Data");
            }
        });

    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(eventsArrayList != null && !eventsArrayList.isEmpty()){
            return eventsArrayList.size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        }

        CharSequence eventName = eventsArrayList.get(position).getEventName();
        CharSequence eventDate = eventsArrayList.get(position).getEventDate();
        CharSequence eventTime = eventsArrayList.get(position).getEventTime();

        CharSequence eventDateAndTime = "On " + eventDate + " at " +eventTime;

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_listview_item);
        rv.setTextViewText(R.id.textview_eventName, eventName);
        rv.setTextViewText(R.id.textview_event_dateandtime, eventDateAndTime);

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Constants.EVENT_ID, eventsArrayList.get(position).getEventId());
        rv.setOnClickFillInIntent(R.id.widgetItemContainer, fillInIntent);

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}