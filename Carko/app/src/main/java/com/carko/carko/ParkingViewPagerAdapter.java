package com.carko.carko;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by fabrice on 2016-11-26.
 */

public class ParkingViewPagerAdapter extends FragmentStatePagerAdapter {

    Fragment one, two, three;

    public ParkingViewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position){
        Fragment fragment;
        switch(position){
            case 0:
                if (one == null)
                    one = new ParkingMapFragment();
                fragment = one;
                break;
            case 1:
                if (two == null)
                    two = new ParkingListFragment();
                fragment = two;
                break;
            case 2:
                if (three == null)
                    three = new MainTabFragment();
                fragment = three;
                break;
            default:
                fragment = new MainTabFragment();
        }

        return fragment;
    }

    @Override
    public int getCount(){
        return 3;
    }

}
