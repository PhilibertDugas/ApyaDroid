package com.carko.carko.models

import com.carko.carko.controllers.BankAccountClient
import org.json.JSONObject

/**
 * Created by fabrice on 2017-07-30.
 */
class BankAccount(
        var firstName: String,
        var lastName: String,
        var address: AccountAddress,
        var dob: AccountDateOfBirth): Model {

    val type = "individual"
    // FIXME: Handle this server side
    // https://stackoverflow.com/questions/35497495/what-identification-document-does-the-field-legal-entity-personal-id-number-desi
    val personalIdNumber = "123456789"

    override fun toJson(): JSONObject {
        val json = JSONObject()
                .put("first_name", firstName)
                .put("last_name", lastName)
                .put("address", address.toJson())
                .put("dob", dob.toJson())
                .put("type", type)
                .put("personal_id_number", personalIdNumber)
        return json
    }

    fun persist(complete: (Error?) -> Unit) {
        BankAccountClient.postAccount(this, complete)
    }

    companion object {
        fun associateExternalAccount(token: String, complete: (Error?) -> Unit) {
            BankAccountClient.postExternalAccount(token, complete)
        }
    }

    class AccountAddress(
            var city: String,
            var line1: String,
            var postalCode: String,
            var state: String): Model {

        override fun toJson(): JSONObject {
            val json = JSONObject()
                    .put("city", city)
                    .put("line1", line1)
                    .put("postal_code", postalCode)
                    .put("state", state)
            return json
        }
    }

    class AccountDateOfBirth(
            var day: String,
            var month: String,
            var year: String): Model {

        override fun toJson(): JSONObject {
            val json = JSONObject()
                    .put("day", day)
                    .put("month", month)
                    .put("year", year)
            return json
        }
    }
}