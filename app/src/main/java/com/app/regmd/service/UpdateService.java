package com.app.regmd.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.app.regmd.db.MyDatabaseHelper;
import com.app.regmd.reciver.UpdateBroadcastRecvier;

/**
 * Created by 王海 on 2015/4/8.
 */
public class UpdateService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("xxxxxxx", "12312312313");
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        int userId = sharedPreferences.getInt("id", 0);
        MyDatabaseHelper myDatabaseHelper = new MyDatabaseHelper(this,"UserLog.db", null, 1);
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        try{
            ContentValues values = new ContentValues();
            values.put("flag", "1");
            db.update("user",values,"id = ?", new String[]{userId+""});
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            db.endTransaction();
        }

        Intent intent3 = new Intent();
        intent3.setAction("MYBROADS");
        sendBroadcast(intent3);
//        stopSelf();
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int time = 50 * 1000;
        long times = SystemClock.elapsedRealtime() + time;
        Intent intent1 = new Intent(this, UpdateBroadcastRecvier.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent1, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, times, pendingIntent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
