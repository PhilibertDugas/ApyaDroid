package com.carko.carko;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by fabrice on 2017-06-11.
 */

public class EventViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    public TextView label;
    public ImageView image;
    public CardView cardView;

    private Event event;

    public final static String EXTRA_EVENT = "com.carko.carko.extra_event";

    EventViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        label = (TextView) itemView.findViewById(R.id.eventLabel);
        image = (ImageView) itemView.findViewById(R.id.eventImage);
        cardView = (CardView) itemView.findViewById(R.id.eventCellContainer);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, EventMapActivity.class);
        intent.putExtra(EventViewHolder.EXTRA_EVENT, this.event);
        context.startActivity(intent);
    }

    public void setEvent(Event event){
        this.event = event;
    }
}
