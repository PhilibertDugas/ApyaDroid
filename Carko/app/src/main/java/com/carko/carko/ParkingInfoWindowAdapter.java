package com.carko.carko;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by fabrice on 2016-11-12.
 */


public class ParkingInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final View mWindow;
    private final View mContents;

    ParkingInfoWindowAdapter(Context context){
        mWindow = null;
        mContents = ((AppCompatActivity) context).getLayoutInflater().inflate(
                R.layout.content_marker_info, null);

    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        if (mWindow == null) {
            // No custom layout
            return null;
        }
        render(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {
        if (mContents == null) {
            // Use default content
            return null;
        }
        render(marker, mContents);
        return mContents;
    }

    private void render(Marker marker, View view){
        // TODO: Replace placeholders with real information
        //Parking parking = (Parking) marker.getTag();

        ((ImageView) view.findViewById(R.id.marker_info_icon)).setImageResource(R.drawable.placeholder);
        String title = marker.getTitle();

        TextView titleUi = view.findViewById(R.id.marker_info_address);
        if (title != null) {
            // Spannable string allows us to edit the formatting of the text.
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0, titleText.length(), 0);
            titleUi.setText(titleText);
        } else {
            titleUi.setText("");
        }

    }
}
