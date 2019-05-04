package com.example.android.hindutemple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hindutemple.model.Events;

import java.util.ArrayList;
import java.util.List;

public class TempleDetailEventAdapter extends RecyclerView.Adapter<TempleDetailEventAdapter.TempleDetailEventsViewHolder> {

    private List<Events> eventsList = new ArrayList<>();


    public TempleDetailEventAdapter(List<Events> listOfEvents){
        eventsList.addAll(listOfEvents);
    }


    @NonNull
    @Override
    public TempleDetailEventAdapter.TempleDetailEventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.templedetail_events_list, viewGroup, false);

        return new TempleDetailEventAdapter.TempleDetailEventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempleDetailEventAdapter.TempleDetailEventsViewHolder eventsViewHolder, int i) {
        eventsViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        if (null == eventsList) return 0;
        return eventsList.size();
    }

    class TempleDetailEventsViewHolder extends RecyclerView.ViewHolder{

        TextView textViewEventName;
        TextView textViewDateAndTime;

        TempleDetailEventsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.textview_detail_event_name);
            textViewDateAndTime = itemView.findViewById(R.id.textview_detail_date_and_time);
        }

        void bind(int position){
            String eventName = eventsList.get(position).getEventName();
            textViewEventName.setText(eventName);

            String eventDate = eventsList.get(position).getEventDate();
            String eventTime = eventsList.get(position).getEventTime();
            String eventDateAndTime = eventDate+" on "+eventTime;

            textViewDateAndTime.setText(eventDateAndTime);

        }

    }
}
