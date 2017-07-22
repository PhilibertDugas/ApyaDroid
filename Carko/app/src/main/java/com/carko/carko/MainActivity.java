package com.carko.carko;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String TAG = "APYA - " + MainActivity.class.getSimpleName();
    private ArrayList<Event> eventList;

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Cache events onPause and use them instead of calling API again
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Events view
        recyclerView = (RecyclerView) findViewById(R.id.eventsRecyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(layoutManager);

        EventOffsetDecoration eventDecoration = new EventOffsetDecoration(this, R.dimen.event_offset);
        recyclerView.addItemDecoration(eventDecoration);

        eventList = new ArrayList<>();
        EventAdapter adapter = new EventAdapter(MainActivity.this, eventList);
        recyclerView.setAdapter(adapter);

        Event.getAllEvents(new EventClient.Complete<ArrayList<Event>>() {
            @Override
            public void onComplete(ArrayList<Event> events, String e) {
                Log.i(TAG, events.toString());
                if (e == null) {
                    if (events.isEmpty()) {
                        // TODO: Put something for when it's empty
                        Log.e(TAG, "No events found.");
                    } else {
                        eventList.addAll(events);
                        recyclerView.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    // TODO: Put something to show error
                    Log.e(TAG, e);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
            // open stripe view
            // philibert
        } else if (id == R.id.nav_vehicule) {
            // open custom activity
            // mario
        } else if (id == R.id.nav_history) {
            // open custom activity
            //
        } else if (id == R.id.nav_help) {
            // open mail
            //
        } else if (id == R.id.nav_rent) {
            // open custom activity
            // rough
        } else if (id == R.id.nav_payout) {
            // open custom activity
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
