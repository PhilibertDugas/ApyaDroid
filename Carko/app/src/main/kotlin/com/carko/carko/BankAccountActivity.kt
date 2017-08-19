package com.carko.carko

import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.SupportActivity
import android.text.Editable
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.app_bar.*
import android.widget.DatePicker
import kotlinx.android.synthetic.main.activity_bank_account.*


class BankAccountActivity : AppCompatActivity() {

    val TAG = "APYA - BankAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)

        toolbar.title = getString(R.string.title_activity_bank_account)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bank_account_birth.setOnClickListener {
            showDatePicker()
        }
        bank_account_birth.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }
    }

    fun showDatePicker() {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

        val TAG = "APYA - DatePicker"

        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            // Use the current date as the default date in the picker
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(activity, this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            // Do something with the date chosen by the user
            Log.i(TAG, "Date set: $year/$month/$day")
            val paddedMonth = if (month < 10) "0$month" else "$month"
            val paddedDay = if (day < 10) "0$day" else "$day"
            activity.bank_account_birth.text = Editable.Factory()
                    .newEditable("$year-$paddedMonth-$paddedDay")
        }
    }
}
