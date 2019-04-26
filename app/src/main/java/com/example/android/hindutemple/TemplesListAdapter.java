package com.example.android.hindutemple;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.hindutemple.model.Temples;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TemplesListAdapter extends RecyclerView.Adapter<TemplesListAdapter.TemplesListViewHolder> {

    public static final String TAG = TemplesListAdapter.class.getSimpleName();
    private CardviewClickListener cardviewClickListener;

    private List<Temples> templesList = new ArrayList<>();

    public TemplesListAdapter(CardviewClickListener cardviewClickListener, List<Temples> listOfTemples){
        this.templesList.addAll(listOfTemples);
        this.cardviewClickListener = cardviewClickListener;
    }

    public interface CardviewClickListener{
        void onCardClicked(int position);
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

    public class TemplesListViewHolder extends RecyclerView.ViewHolder {

            TextView textViewTemple;
            ImageView templeImageView;
            TextView textViewTempleLocation;
            View parentView;

        public TemplesListViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTemple = (TextView) itemView.findViewById(R.id.tv_temple_name);
            templeImageView = (ImageView) itemView.findViewById(R.id.imageview_temple);
            textViewTempleLocation = (TextView) itemView.findViewById(R.id.tv_temple_location);
            parentView = itemView;
        }

        public void bind(final int i) {
            textViewTemple.setText(templesList.get(i).getTempleName());
            textViewTempleLocation.setText(templesList.get(i).getTempleLocation());
            try {
                Picasso.with(itemView.getContext())
                        .load(templesList.get(i).getTempleImageUri())
                        .error(R.drawable.temple_image)
                        .placeholder(R.drawable.temple_image)
                        .into(templeImageView);
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, e.getMessage());
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cardviewClickListener.onCardClicked(i);
                }
            });
        }
    }
}
