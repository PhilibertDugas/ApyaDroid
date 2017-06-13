package com.carko.carko;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by fabrice on 2017-06-11.
 */

public class EventViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    public TextView label;
    public ImageView image;
    public CardView cardView;

    EventViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        label = (TextView) itemView.findViewById(R.id.eventLabel);
        image = (ImageView) itemView.findViewById(R.id.eventImage);
        cardView = (CardView) itemView.findViewById(R.id.eventCellContainer);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + this.getAdapterPosition(), Toast.LENGTH_SHORT).show();
    }
}
