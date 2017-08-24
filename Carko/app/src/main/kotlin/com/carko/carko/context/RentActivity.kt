package com.carko.carko.context

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.carko.carko.R
import kotlinx.android.synthetic.main.activity_rent.*
import kotlinx.android.synthetic.main.content_rent.*


class RentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rent)

        // Toolbar
        toolbar.title = getString(R.string.rent_header)
        setSupportActionBar(toolbar)

        // Enable up button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        get_started_button.setOnClickListener {
            val intent = Intent(this, AddParkingMapActivity::class.java)
            startActivity(intent)
        }
    }
}
