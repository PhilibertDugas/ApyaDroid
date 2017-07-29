package com.carko.carko

import android.app.Activity
import android.os.Bundle

import kotlinx.android.synthetic.main.reservation_dialog.*


class ReservationDialog : Activity() {
    companion object {
        val EVENT_PRICE_KEY = "com.carko.carko.EVENT_PRICE_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_dialog)

        val event_price = intent.getFloatExtra(EVENT_PRICE_KEY, 0.0f)
        price.text = event_price.toString()

        confirm_button.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        cancel_button.setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
