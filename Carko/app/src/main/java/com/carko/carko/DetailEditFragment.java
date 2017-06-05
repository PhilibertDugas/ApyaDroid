package com.carko.carko;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.mapbox.mapboxsdk.geometry.LatLng;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailEditFragment extends Fragment {

    Button saveButton;

    public DetailEditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentLayout = inflater.inflate(R.layout.fragment_detail_edit, container, false);

        Intent incomingIntent = getActivity().getIntent();
        Bundle data = incomingIntent.getBundleExtra("bundle");
        final LatLng currPos = data.getParcelable("pos");

        saveButton = (Button) fragmentLayout.findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Intent outcomingIntent = new Intent(getActivity(), ParkingDetailActivity.class);
                Bundle data = new Bundle();
                data.putParcelable("pos", currPos);
                outcomingIntent.putExtra("bundle", data);
                getActivity().setResult(Activity.RESULT_OK, outcomingIntent);
                getActivity().finish();
            }
        });

        return fragmentLayout;
    }

}
