package al.hamdu.lil.allah

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.TypedArrayUtils.getText

class MyNotification {
    private val channelId = "channelId"

    @RequiresApi(Build.VERSION_CODES.O)
    fun buildNotification(context: Context, title: String, content: String) : Notification{
        createNotificationChannel(context)
        return NotificationCompat.Builder(context, channelId)
            .setSmallIcon(androidx.activity.R.drawable.notification_bg)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
    }

//     fun showNotification(context: Context, notificationId: Int , notification: Notification){
//        createNotificationChannel(context)
//
//         with(NotificationManagerCompat.from(context)) {
//             notify(notificationId, notification)
//         }
//    }
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