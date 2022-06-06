package al.hamdu.lil.allah.service

import al.hamdu.lil.allah.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat

class ForegroundNotification {
    private val channelId = "channelId"

    fun buildNotification(context: Context, title: String, content: String) : Notification{
        createNotificationChannel(context)
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_noor)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }
     private fun createNotificationChannel(context: Context){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = context.resources.getString(R.string.channel_name)
                val descriptionText = context.resources.getString(R.string.channel_description)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(channelId, name, importance).apply {
                    description = descriptionText
                }
                channel.setSound(null, null)
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }
    }
}