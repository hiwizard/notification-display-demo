package syu.test.notificationtest

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        NotificationManagerCompat.from(applicationContext).createNotificationChannel(
            NotificationChannel(
                CHANNEL,
                getString(R.string.app_name),
                NotificationManager.IMPORTANCE_HIGH
            )
        )

        findViewById<Button>(R.id.button).setOnClickListener {
            sendNotifications()
        }
    }

    private fun sendNotifications() = lifecycleScope.launch {
        delay(1000L)

        val context = this@MainActivity
        val notificationManagerCompat = NotificationManagerCompat.from(context.applicationContext)

        // send a single notification
        val firstTimeMillis = System.currentTimeMillis()
        notificationManagerCompat.notify(
            firstTimeMillis.toString(),
            firstTimeMillis.toInt(),
            createNotification(context, "1st notification", firstTimeMillis)
        )

        // send multi notifications
        val otherTimeMillisList = mutableListOf<Long>()
        repeat(5) {
            delay(20_000L)
            val otherTimeMillis = System.currentTimeMillis()
            otherTimeMillisList.add(otherTimeMillis)
            notificationManagerCompat.notify(
                otherTimeMillis.toString(),
                otherTimeMillis.toInt(),
                createNotification(context, "other notification", otherTimeMillis)
            )
        }

        // update all notification with text and large icon except 1st one.
        val sampleIcon = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(sampleIcon)
        canvas.drawColor(Color.RED)
        otherTimeMillisList.forEach { otherTimeMillis ->
            notificationManagerCompat.notify(
                otherTimeMillis.toString(),
                otherTimeMillis.toInt(),
                createNotification(
                    context,
                    "fixed other notification",
                    otherTimeMillis,
                    sampleIcon
                )
            )
        }
    }

    private fun createNotification(
        context: Context,
        body: String,
        timeMillis: Long,
        largeIcon: Bitmap? = null
    ): Notification =
        NotificationCompat.Builder(context, CHANNEL).apply {
            setWhen(timeMillis)
            setSmallIcon(R.drawable.ic_launcher_foreground)
            setLargeIcon(largeIcon)
            color = Color.GREEN
            setContentTitle("Test notification")
            setContentText(body)
            setStyle(NotificationCompat.BigTextStyle().bigText(body))
            setCategory(Notification.CATEGORY_MESSAGE)
            setDefaults(NotificationCompat.DEFAULT_ALL)
            priority = NotificationCompat.PRIORITY_HIGH
            setContentIntent(
                PendingIntent.getActivity(
                    context,
                    timeMillis.toInt(),
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
            setAutoCancel(true)
            setOnlyAlertOnce(true)
        }.build()

    companion object {
        private const val CHANNEL = "channel"
    }
}