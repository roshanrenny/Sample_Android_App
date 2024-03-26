package com.example.sampleandroidapp.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.sampleandroidapp.R
import com.example.sampleandroidapp.activity.NotificationReceiver
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        println("From: ${remoteMessage.from}")
        remoteMessage.notification?.let {
            println("Message Notification Body: ${it.body}")
            sendNotification(it.body)
        }

        remoteMessage.data[REPLY_ACTION]?.let { replyText ->
            showToast("Reply: $replyText")
        }
    }

    private fun sendNotification(messageBody: String?) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, messageBody, Toast.LENGTH_SHORT).show()
        }

        val replyIntent = Intent(this, NotificationReceiver::class.java)
        replyIntent.action = REPLY_ACTION

        val context = applicationContext
        val replyPendingIntent = if (context != null) {
            PendingIntent.getBroadcast(context, 0, replyIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            null
        }

        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("Reply").build()

        val replyAction = NotificationCompat.Action.Builder(
            R.drawable.ic_launcher_foreground, "Reply", replyPendingIntent).addRemoteInput(remoteInput)
            .build()

        val channelId = "My Channel ID"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("My new notification")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .addAction(replyAction)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notificationId = NOTIFICATION_ID
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    private fun showToast(message: String) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        const val REPLY_ACTION = "REPLY_ACTION"
        const val KEY_TEXT_REPLY = "key_text_reply"
        const val NOTIFICATION_ID= 123
    }
}
