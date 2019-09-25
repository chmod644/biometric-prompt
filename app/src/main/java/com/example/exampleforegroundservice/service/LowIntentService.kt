package com.example.exampleforegroundservice.service

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.exampleforegroundservice.R

class LowIntentService: IntentService("LowIntentService") {
    val notificationId = 101
    var notificationBuilder: NotificationCompat.Builder? = null

    override fun onHandleIntent(intent: Intent?) {
        try {
            Thread.sleep(5000)
        } catch (e: InterruptedException) {
            // Restore interrupt status.
            Thread.currentThread().interrupt()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Toast.makeText(this, getString(R.string.message_start_service), Toast.LENGTH_SHORT).show()

        val id =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val id = "channel_low_upload"
                val name = "Low Notification"
                val notifyDescription = getString(R.string.notify_description)
                createNotificationChannel(id, name, notifyDescription)
            } else {
                ""
            }

        notificationBuilder = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.ic_file_upload_black_24dp)
            .setContentTitle(getText(R.string.app_name))
            .setContentText(getText(R.string.notification_message))

        val notification: Notification = notificationBuilder!!.build()

        startForeground(notificationId, notification)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Toast.makeText(this, getString(R.string.message_finish_service), Toast.LENGTH_SHORT).show()
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(id: String, name: String, desc: String): String {
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (manager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_LOW)
            mChannel.apply {
                description = desc
            }
            manager.createNotificationChannel(mChannel)
        }
        return id
    }
}

