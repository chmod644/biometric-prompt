package com.example.exampleforegroundservice.service

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.exampleforegroundservice.MainActivity
import com.example.exampleforegroundservice.R

class SimpleIntentService: IntentService("SimpleIntentService") {
    override fun onHandleIntent(intent: Intent?) {
        try {
            Toast.makeText(this, "Sending", Toast.LENGTH_SHORT).show()
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
                val id = "channle_simple_upload"
                val name = getString(R.string.notify_name)
                val notifyDescription = getString(R.string.notify_description)
                createNotificationChannel(id, name, notifyDescription)
            } else {
                ""
            }

        val notification: Notification = NotificationCompat.Builder(this, id)
            .setSmallIcon(R.drawable.ic_file_upload_black_24dp)
            .setContentTitle(getText(R.string.app_name))
            .setContentText(getText(R.string.notification_message))
            .setProgress(100, 100, true)
            .build()

        startForeground(1, notification)
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

