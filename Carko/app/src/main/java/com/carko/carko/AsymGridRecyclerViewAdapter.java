package com.carko.carko;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.List;

/**
 * Created by fabrice on 2017-06-11.
 */

public class AsymGridRecyclerViewAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private List<Event> itemList;
    private Context context;

    public AsymGridRecyclerViewAdapter(Context context, List<Event> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);
        return new EventViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(EventViewHolder viewHolder, int position) {
        viewHolder.label.setText(itemList.get(position).getLabel());
        viewHolder.image.setImageResource(itemList.get(position).getDrawable());
        StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) viewHolder.cardView.getLayoutParams();
        if (position == 0 || position == 1) {
            // Active event and next event are full width
            layoutParams.setFullSpan(true);
            layoutParams.height = 600;
        } else{
            layoutParams.height = 400;
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
