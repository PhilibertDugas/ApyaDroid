package com.carko.carko;

import android.content.Context;
import android.media.Image;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by fabrice on 2017-06-10.
 */

public class EventAdapter extends ArrayAdapter<Event> {
    private Context mContext;

    private static class ViewHolder {
        ImageView image;
        TextView text;
    }

    EventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Event event = getItem(position);
        Log.d("EventAdapter.getView()", "event: " + event.toString());
        ViewHolder viewHolder;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            Log.d("EventAdapter.getView()", "initializing view");
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.event_cell, parent, false);
            viewHolder = new ViewHolder();
            ImageView imageView = (ImageView) convertView.findViewById(R.id.eventImage);
            imageView.setLayoutParams(new RelativeLayout.LayoutParams(400,400));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            TextView textView = (TextView) convertView.findViewById(R.id.eventLabel);
            textView.setPadding(8, 8, 8, 8);
            viewHolder.image = imageView;
            viewHolder.text = textView;
            convertView.setTag(viewHolder);
        } else {
            Log.d("EventAdapter.getView()", "recycling view");
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.image.setImageResource(event.getDrawable());
        viewHolder.text.setText(event.getLabel());
        Log.d("EventAdapter.getView()", "viewHolder: " + viewHolder.image.getDrawable() + ", " + viewHolder.text.getText());
        return convertView;
    }
}
