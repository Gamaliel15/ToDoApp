package com.gamaliel.todoapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.getSystemService
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.gamaliel.todoapp.R
import com.gamaliel.todoapp.model.TodoDatabase
import com.gamaliel.todoapp.view.MainActivity

val DB_NAME = "newtododb"

fun buildDb(context: Context): TodoDatabase{
    val db = TodoDatabase.buildDatabase(context)
    return db
}

val MIGRATION_1_2 = object:Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN priority INTEGER DEFAULT 3 not null")
    }

}

val MIGRATION_2_3 = object: Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN is_done INTEGER DEFAULT 0 NOT NULL")
    }
}

val MIGRATION_3_4 = object: Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE todo ADD COLUMN todo_date INTEGER DEFAULT 0 NOT NULL")
    }
}

class NotificationHelper(val context: Context) {
    private val CHANNEL_ID = "todo_channel"
    private val NOTIFICATION_ID = 1

    companion object {
        val REQUEST_NOTIF = 100
    }

    private fun createNotificationChannel(){
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT)
        channel.description = "channel"
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    fun createNotification( title:String, message:String) {
        createNotificationChannel()
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context,0, intent,
            PendingIntent.FLAG_IMMUTABLE)

        val icon = BitmapFactory.decodeResource(context.resources, R.drawable.todochar)

        val notif = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.checklist)
            .setLargeIcon(icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                    .bigPicture(icon)
                    .bigLargeIcon(null)
            )
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        try {
            NotificationManagerCompat.from(context)
                .notify(NOTIFICATION_ID, notif)
        } catch (e:SecurityException) {
            Log.e("error notif", e.message.toString())
        }


    }

    class TodoWorker(context: Context, params: WorkerParameters): Worker(context,params) {
        override fun doWork(): Result {
            NotificationHelper(applicationContext).createNotification(
                inputData.getString("title").toString(),
                inputData.getString("message").toString())
            return Result.success()
        }
    }

}
