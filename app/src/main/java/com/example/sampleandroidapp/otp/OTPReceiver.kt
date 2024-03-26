package com.example.sampleandroidapp.otp

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.widget.EditText


class OTPReceiver : BroadcastReceiver() {
    fun setEditText_otp(editText: EditText?) {
        editText_otp = editText
    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        val smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (smsMessage in smsMessages) {
            val message_body = smsMessage.messageBody
            val getOTP = message_body.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1]
            editText_otp!!.setText(getOTP)
        }
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var editText_otp: EditText? = null
    }
}