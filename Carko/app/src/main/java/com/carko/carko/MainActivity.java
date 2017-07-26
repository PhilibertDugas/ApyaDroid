package com.carko.carko;

import android.content.Intent;
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
import android.widget.Toast;

import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.firebase.ui.auth.AuthUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "APYA - " + MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 3235;

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
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Toast.makeText(this, "User already logged in!", Toast.LENGTH_SHORT).show();
        } else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                    new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build()
            );
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
            return true;
        }


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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == ResultCodes.OK) {
                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    Toast.makeText(this, "Sign in cancelled!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "No network!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "Unknown error!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(this, "Unknown response!", Toast.LENGTH_SHORT).show();
        }
    }

}
