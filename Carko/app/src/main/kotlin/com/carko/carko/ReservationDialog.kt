package com.carko.carko

import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.widget.Toast


class ReservationDialog : DialogFragment() {

    interface ReservationDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    private var mListener: ReservationDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the ReservationDialogListener so we can send events to the host
            mListener = context as ReservationDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(context.toString() + " must implement ReservationDialogListener")
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val builder = android.app.AlertDialog.Builder(activity)
        builder.setTitle(R.string.reservation_dialog_title)
        builder.setMessage("20$")
        builder.setPositiveButton(R.string.reservation_dialog_confirm, {dialog, id ->
            mListener?.onDialogPositiveClick(this)
        })
        builder.setNegativeButton(R.string.reservation_dialog_cancel, {dialog, id ->
            mListener?.onDialogNegativeClick(this)
        })
        val dialog = builder.create()
        return dialog
    }
}
