package com.carko.carko.context

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.carko.carko.R
import kotlinx.android.synthetic.main.activity_bank_information.*
import android.content.Intent
import android.util.Log
import com.carko.carko.AuthenticationHelper
import com.carko.carko.MapActivity
import com.carko.carko.models.BankAccount


class BankInformationActivity : AppCompatActivity() {

    val TAG = "APYA-BankInfoActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bank_information)

        bank_info_save_button.setOnClickListener {
            save()

            val intent = Intent(this, MapActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun save() {
        // values from previous activity
        val address = intent.getStringExtra("address")
        val city = intent.getStringExtra("city")
        val province = intent.getStringExtra("province")
        val postal = intent.getStringExtra("postal")
        val country = intent.getStringExtra("country")
        val birth = intent.getStringExtra("birth")

        val day = birth.split('-')[2]
        val month = birth.split('-')[1]
        val year = birth.split('-')[0]

        // current values
        val routing = bank_info_routing_number.text.toString()
        val accountNum = bank_info_account_number.text.toString()

        val customer = AuthenticationHelper.getCustomer()
        if (customer != null) {
            val accountAddress = BankAccount.AccountAddress(city, address, postal, province)
            val dob = BankAccount.AccountDateOfBirth(day, month, year)
            val account = BankAccount(customer.firstName, customer.lastName, accountAddress, dob)
            // TODO get stripe api key
            // TODO push account to backend
            // TODO create token
//            account.persist { error ->
//                if (error != null) {
//                    Stripe(this, )
//                }
//            }
        } else {
            Log.e(TAG, "customer not available")
        }
    }
}
