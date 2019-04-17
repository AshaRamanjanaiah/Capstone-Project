package com.example.android.hindutemple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.hindutemple.model.Events;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    private List<Events> eventsList = new ArrayList<>();

    final private ItemClickListener itemClickListener;

    public EventsAdapter(List<Events> listOfEvents, ItemClickListener itemClickListener){
        eventsList.addAll(listOfEvents);
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.events_list_items, viewGroup, false);

        return new EventsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder eventsViewHolder, int i) {
        eventsViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        if (null == eventsList) return 0;
        return eventsList.size();
    }


    public interface ItemClickListener{
        void onEdit(int clickedItemIndex);
        void onDelete(int clickedItemIndex);
    }

    class EventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewEventName;
        TextView textViewEventDateAndTime;
        ImageView imageViewEdit;
        ImageView imageViewDelete;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewEventName = itemView.findViewById(R.id.textview_event_name);
            textViewEventDateAndTime = itemView.findViewById(R.id.textview_event_date_and_time);
            imageViewEdit = itemView.findViewById(R.id.imageView_edit_event);
            imageViewDelete = itemView.findViewById(R.id.imageView_delete_event);

            imageViewEdit.setOnClickListener(this);
            imageViewDelete.setOnClickListener(this);
        }


        public void bind(int position){
            String name = eventsList.get(position).getEventName();
            textViewEventName.setText(name);

            String eventdate = eventsList.get(position).getEventDate();
            String eventTime = eventsList.get(position).getEventTime();

            textViewEventDateAndTime.setText("On" +eventdate+" from "+eventTime);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            int id = view.getId();
            switch (id){
                case R.id.imageView_edit_event:
                    itemClickListener.onEdit(clickedPosition);
                    break;
                case R.id.imageView_delete_event:
                    itemClickListener.onDelete(clickedPosition);
                    break;
                default:
                    break;

            }
        }
    }
}
