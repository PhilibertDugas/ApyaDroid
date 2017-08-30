package com.carko.carko.context

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.icu.util.Calendar
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import kotlinx.android.synthetic.main.app_bar.*
import android.widget.DatePicker
import com.carko.carko.R
import kotlinx.android.synthetic.main.activity_bank_account.*


class BankAccountActivity : AppCompatActivity() {

    val TAG = "APYA - BankAccountActivity"
    val BUNDLE_KEY = "bank_acount_info"

    val validityChecks = HashMap<String, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_account)

        toolbar.title = getString(R.string.title_activity_bank_account)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        bank_account_continue_button.setOnClickListener { continueOnClick() }
        bank_account_birth.setOnClickListener { showDatePicker() }
        bank_account_birth.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                showDatePicker()
            }
        }

        // Initialize validity checks
        validityChecks.put("address", false)
        validityChecks.put("city", false)
        validityChecks.put("province", false)
        validityChecks.put("postal", false)
        validityChecks.put("country", false)
        validityChecks.put("birth", false)

        bank_account_address.addTextChangedListener(NotBlankWatcher("address"))
        bank_account_city.addTextChangedListener(NotBlankWatcher("city"))
        bank_account_province.addTextChangedListener(NotBlankWatcher("province"))
        bank_account_postal.addTextChangedListener(NotBlankWatcher("postal"))
        bank_account_country.addTextChangedListener(NotBlankWatcher("country"))
        bank_account_birth.addTextChangedListener(NotBlankWatcher("birth"))
    }

    private fun validate() {
        for (validityCheck in validityChecks) {
            if (!validityCheck.value) {
                bank_account_continue_button.isEnabled = false
                return
            }
        }

        bank_account_continue_button.isEnabled = true
    }

    private fun continueOnClick() {
        val intent = Intent(this, BankInformationActivity::class.java)
        val data = Bundle()
        data.putString("address", bank_account_address.text.toString())
        data.putString("city", bank_account_city.toString())
        data.putString("province", bank_account_province.toString())
        data.putString("postal", bank_account_postal.toString())
        data.putString("country", bank_account_country.toString())
        data.putString("birth", bank_account_birth.toString())
        intent.putExtra(this.BUNDLE_KEY, data)
        startActivity(intent)
    }

    private fun showDatePicker() {
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

    inner class NotBlankWatcher(val field: String): TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            Log.i(TAG, "onTextChanged() - ${s.isBlank()}")
            validityChecks[field] = ! s.isBlank()
            validate()
        }
    }
}
