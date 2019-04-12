package com.example.android.hindutemple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.hindutemple.model.Timings;

import java.util.ArrayList;
import java.util.List;

public class TimingsAdapter extends RecyclerView.Adapter<TimingsAdapter.TimingsViewHolder> {

    private List<Timings> timingsList = new ArrayList<>();

    final private ItemClickListener itemClickListener;

    public TimingsAdapter(List<Timings> listOfTimings, ItemClickListener itemClickListener){
        timingsList.addAll(listOfTimings);
        this.itemClickListener = itemClickListener;
    }


    public interface ItemClickListener{
        void onEdit(int clickedItemIndex);
        void onDelete(int clickedItemIndex);
    }

    @NonNull
    @Override
    public TimingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.timings_list_item, viewGroup, false);

        return new TimingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimingsViewHolder timingsViewHolder, int i) {
        timingsViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        if (null == timingsList) return 0;
        return timingsList.size();
    }

    class TimingsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView textViewDay;
        TextView textViewOpenandCloseTime;
        ImageView imageViewEdit;
        ImageView imageViewDelete;

        public TimingsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDay = itemView.findViewById(R.id.textview_day);
            textViewOpenandCloseTime = itemView.findViewById(R.id.textview_open_close_time);
            imageViewEdit = itemView.findViewById(R.id.imageView_edit);
            imageViewDelete = itemView.findViewById(R.id.imageView_delete);

            imageViewEdit.setOnClickListener(this);
            imageViewDelete.setOnClickListener(this);

        }

        public void bind(int position){
            String day = timingsList.get(position).getTimingsDay();
            textViewDay.setText(day);

            String openTime = timingsList.get(position).getTimingsOpen();
            String closeTime = timingsList.get(position).getTimingsClose();

            textViewOpenandCloseTime.setText(openTime+" - "+closeTime);

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            int id = view.getId();
            switch (id){
                case R.id.imageView_edit:
                    itemClickListener.onEdit(clickedPosition);
                    break;
                case R.id.imageView_delete:
                    itemClickListener.onDelete(clickedPosition);
                    break;

            }
        }
    }



}
