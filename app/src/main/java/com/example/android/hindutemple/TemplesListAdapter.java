package com.example.android.hindutemple;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.hindutemple.model.Temples;

import java.util.ArrayList;
import java.util.List;

public class TemplesListAdapter extends RecyclerView.Adapter<TemplesListAdapter.TemplesListViewHolder> {

    private List<Temples> templesList = new ArrayList<>();

    public TemplesListAdapter(List<Temples> listOfTemples){
        this.templesList.addAll(listOfTemples);
    }

    @NonNull
    @Override
    public TemplesListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.temples_list_item, viewGroup, false);

        return new TemplesListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplesListViewHolder templesListViewHolder, int i) {
        templesListViewHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return templesList.size();
    }

    public class TemplesListViewHolder extends RecyclerView.ViewHolder{

            TextView textView;

        public TemplesListViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.temple_name);
        }

        public void bind(int i) {
            textView.setText("Position" +templesList.get(i).getTempleName());
        }
    }
}
