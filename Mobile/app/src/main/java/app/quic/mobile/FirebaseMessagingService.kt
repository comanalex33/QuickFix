package app.quic.mobile

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import app.quic.mobile.activities.MainActivity
import app.quic.mobile.dialogs.InfoDialog
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : com.google.firebase.messaging.FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        if (message.data.isNotEmpty()) {
            Toast.makeText(
                applicationContext,
                "Message data payload: ${message.data}",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Check if message contains a notification payload.
        message.notification?.let {
            var title = it.title
            var body = it.body

            println("Message Notification Title: $title")
            print("Message Notification Body: $body")

            sendNotification(title!!, body!!)
        }
    }

    private fun sendNotification(title: String, body: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE)
        if (notificationManager is NotificationManager) {
            notificationManager.notify(0, notificationBuilder.build())
        }
    }
}
