package com.carko.carko;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by fabrice on 2017-06-11.
 */

public class EventViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    public TextView label;
    public ImageView image;
    public CardView cardView;

    private Event event;

    EventViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        label = (TextView) itemView.findViewById(R.id.eventLabel);
        image = (ImageView) itemView.findViewById(R.id.eventImage);
        cardView = (CardView) itemView.findViewById(R.id.eventCellContainer);
    }

    @Override
    public void onClick(View view) {
//        int pos = this.getAdapterPosition();
        Context context = view.getContext();
        Intent intent = new Intent(context, EventMapActivity.class);
        context.startActivity(intent);
    }

    public void setEvent(Event event){
        this.event = event;
    }
}
