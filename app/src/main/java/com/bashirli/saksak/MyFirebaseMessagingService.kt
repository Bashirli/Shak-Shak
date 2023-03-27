package com.bashirli.saksak

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.RemoteViews.RemoteView
import androidx.core.app.NotificationCompat
import com.bashirli.saksak.ui.activity.ScreenActivity
import com.bashirli.saksak.ui.activity.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


const val channelId="notification_channel"
const val channelName="com.bashirli.saksak"


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
            if(message.notification!=null){
                generateNotification(message.notification!!.title!!,message.notification!!.body!!)
            }
    }

    fun generateNotification(header:String,message:String){

        val intent= Intent(this,SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        var builder=NotificationCompat.Builder(this, channelId)
        builder.setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder=builder.setContent(getRemoteView(header,message))

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
        val notificationChannel=NotificationChannel(channelId, channelName,NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(0,builder.build())
    }

    fun getRemoteView(header: String,message: String):RemoteViews{
        val remoteView=RemoteViews("com.bashirli.saksak",R.layout.notification)
        remoteView.setTextViewText(R.id.headerNotification,header)
        remoteView.setTextViewText(R.id.messageNotification,message)
        return remoteView
    }

}