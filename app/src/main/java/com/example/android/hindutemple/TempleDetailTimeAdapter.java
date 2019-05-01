package com.example.android.hindutemple;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hindutemple.model.Timings;

import java.util.ArrayList;
import java.util.List;

public class TempleDetailTimeAdapter  extends RecyclerView.Adapter<TempleDetailTimeAdapter.TempleDetailTimingsViewHolder> {

    private List<Timings> timingsList = new ArrayList<>();


    public TempleDetailTimeAdapter(List<Timings> listOfTimings){
        timingsList.addAll(listOfTimings);
    }


    @NonNull
    @Override
    public TempleDetailTimeAdapter.TempleDetailTimingsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.templedetail_timings_list, viewGroup, false);

        return new TempleDetailTimeAdapter.TempleDetailTimingsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TempleDetailTimeAdapter.TempleDetailTimingsViewHolder timingsViewHolder, int i) {
        timingsViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        if (null == timingsList) return 0;
        return timingsList.size();
    }

    class TempleDetailTimingsViewHolder extends RecyclerView.ViewHolder{

        TextView textViewDay;
        TextView textViewOpenandCloseTime;

        public TempleDetailTimingsViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewDay = itemView.findViewById(R.id.textview_detail_day);
            textViewOpenandCloseTime = itemView.findViewById(R.id.textview_detail_open_close_time);
        }

        public void bind(int position){
            String day = timingsList.get(position).getTimingsDay();
            textViewDay.setText(day);

            String openTime = timingsList.get(position).getTimingsOpen();
            String closeTime = timingsList.get(position).getTimingsClose();

            textViewOpenandCloseTime.setText(openTime+" - "+closeTime);

        }

        }


}
