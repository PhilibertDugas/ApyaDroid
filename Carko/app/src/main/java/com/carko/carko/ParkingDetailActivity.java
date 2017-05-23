package com.carko.carko;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

public class ParkingDetailActivity extends AppCompatActivity {

    private static String TAG = "ParkingDetailActivity";

    private ImageButton image;
    ArrayList<Parking> parkings;
    ParkingAdapter parkingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate");

        if (savedInstanceState == null) createAndAddFragment();
    }

    private void createAndAddFragment(){
        Log.d(TAG, "create and add fragment");
        Intent intent = getIntent();
        ParkingTabActivity.FragmentToLaunch ftl = (ParkingTabActivity.FragmentToLaunch)
                intent.getSerializableExtra(ParkingTabActivity.FRAGMENT_TO_LAUNCH_EXTRA);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (ftl){
            case VIEW:
                DetailViewFragment viewFragment = new DetailViewFragment();
                setTitle(R.string.detail_view_fragment_title);
                fragmentTransaction.add(R.id.content_parking_detail, viewFragment, "DETAIL_VIEW_FRAGMENT");
                break;

            case EDIT:
                DetailEditFragment editFragment = new DetailEditFragment();
                setTitle(R.string.detail_edit_fragment_title);
                fragmentTransaction.add(R.id.content_parking_detail, editFragment, "DETAIL_EDIT_FRAGMENT");
                break;
        }

        fragmentTransaction.commit();
    }

}
