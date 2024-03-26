package com.example.sampleandroidapp.activity

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.app.RemoteInput
import com.example.sampleandroidapp.services.MyFirebaseMessagingService

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == MyFirebaseMessagingService.REPLY_ACTION) {
            val remoteInput = RemoteInput.getResultsFromIntent(intent)
            val replyText = remoteInput?.getCharSequence(MyFirebaseMessagingService.KEY_TEXT_REPLY)?.toString()

            if (replyText != null) {
                Toast.makeText(context,"Message Received",Toast.LENGTH_SHORT).show()

            }

            val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(MyFirebaseMessagingService.NOTIFICATION_ID)
        }
    }
}
