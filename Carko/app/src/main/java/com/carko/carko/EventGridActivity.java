package com.carko.carko;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.carko.carko.controllers.EventClient;
import com.carko.carko.models.Customer;
import com.carko.carko.models.Event;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class EventGridActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "APYA - " + EventGridActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 3235;

    private ArrayList<Event> eventList;

    private Function1<Customer, Unit> mAuthListener;

    private RecyclerView recyclerView;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Cache events onPause and use them instead of calling API again
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_grid);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Drawer
        DrawerLayout drawer = findViewById(R.id.event_grid_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        // Navigation view
        // TODO create custom layout for
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        // Events view
        recyclerView = findViewById(R.id.eventsRecyclerView);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(layoutManager);

        EventOffsetDecoration eventDecoration = new EventOffsetDecoration(this, R.dimen.event_offset);
        recyclerView.addItemDecoration(eventDecoration);

        eventList = new ArrayList<>();
        EventAdapter adapter = new EventAdapter(EventGridActivity.this, eventList);
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

        mAuthListener = new Function1<Customer, Unit>() {
            @Override
            public Unit invoke(Customer customer) {
                Log.i(TAG, "listener - Listener notified in EventGridActivity!");
                if (customer != null) {
                    Log.i(TAG, "mAuthListener - Customer logged in from EventGridActivity!");
                    View header = navigationView.getHeaderView(0);
                    TextView displayName = header.findViewById(R.id.display_name);
                    displayName.setText(customer.getDisplayName());
                } else {
                    Log.i(TAG, "mAuthListener - Customer logged out from EventGridActivity!");
                    View header = navigationView.getHeaderView(0);
                    TextView displayName = header.findViewById(R.id.display_name);
                    displayName.setText("Android Studio");
                }
                return null;
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        AuthenticationHelper.INSTANCE.removeAuthStateListener(mAuthListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        AuthenticationHelper.INSTANCE.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.event_grid_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (AuthenticationHelper.INSTANCE.customerAvailable()) {
            Toast.makeText(this, "User already logged in!", Toast.LENGTH_SHORT).show();
            Customer customer = AuthenticationHelper.INSTANCE.getCustomer();
            Log.i(TAG, customer.toJson().toString());

            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.i(TAG, "FirebaseUser: " + user.getDisplayName());
            }
        } else {
            AuthenticationHelper.INSTANCE.login(this);
            return true;
        }

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.item_payment) {
            // open stripe view
            // philibert
        } else if (id == R.id.item_vehicule) {
            // open custom activity
            // mario
        } else if (id == R.id.item_history) {
            // open custom activity
            //
        } else if (id == R.id.item_help) {
            // open mail
            //
        } else if (id == R.id.item_rent) {
            // open custom activity
            // rough
        } else if (id == R.id.item_payout) {
            // open custom activity
        }

        DrawerLayout drawer = findViewById(R.id.event_grid_drawer_layout);
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
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                AuthenticationHelper.INSTANCE.ensureCustomerInBackend(user);
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
