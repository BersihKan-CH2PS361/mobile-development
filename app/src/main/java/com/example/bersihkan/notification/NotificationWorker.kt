package com.example.bersihkan.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.bersihkan.R
import com.example.bersihkan.helper.findOrderStatus
import com.example.bersihkan.utils.UserRole

class NotificationWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val status = inputData.getString(EXTRA_STATUS)
        val user = inputData.getString(EXTRA_USER)
        when(user){
            UserRole.USER.role -> {
                val title = findOrderStatus(status.toString()).notifTitle
                val text = findOrderStatus(status.toString()).notifText
                showNotification(title, text)
            }
            UserRole.COLLECTOR.role -> {
                val title = "You got an order!"
                val text = "An order is waiting for you. Pick up your waste order!"
                showNotification(title, text)
            }
        }
        return Result.success()
    }

    private fun showNotification(title: String, description: String?) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notification: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_logo_circle)
            .setContentTitle(title)
            .setContentText(description)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notification.setChannelId(CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    companion object {
        private val TAG = NotificationWorker::class.java.simpleName
        const val EXTRA_STATUS = "status"
        const val EXTRA_USER = "user"
        const val NOTIFICATION_ID = 1
        const val CHANNEL_ID = "channel_01"
        const val CHANNEL_NAME = "channel_name"
    }

}