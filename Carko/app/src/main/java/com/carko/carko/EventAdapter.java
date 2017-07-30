package com.carko.carko;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.carko.carko.models.Event;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

/**
 * Created by fabrice on 2017-06-11.
 */

public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private static final String TAG = EventAdapter.class.getSimpleName();

    private List<Event> itemList;
    private Context context;

    public EventAdapter(Context context, List<Event> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_cell, parent, false);
        return new EventViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(final EventViewHolder viewHolder, int position) {
        Event event = itemList.get(position);
        viewHolder.setEvent(event);
        viewHolder.label.setText(event.getLabel());

        // Load image from Firebase
        StorageReference ref = FirebaseStorage.getInstance()
                .getReferenceFromUrl(event.getPhotoURL());
        Glide.with(this.context)
                .using(new FirebaseImageLoader())
                .load(ref)
                .into(viewHolder.image);

        // Set layout parameters
        StaggeredGridLayoutManager.LayoutParams layoutParams =
                (StaggeredGridLayoutManager.LayoutParams) viewHolder.cardView.getLayoutParams();
        if (position == 0) {
            // Active event is full width
            layoutParams.setFullSpan(true);
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
}
