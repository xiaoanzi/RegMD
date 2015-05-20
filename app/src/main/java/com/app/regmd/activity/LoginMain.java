package com.app.regmd.activity;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.regmd.R;
import com.app.regmd.db.MyDatabaseHelper;
import com.app.regmd.model.User;
import com.app.regmd.reciver.UpdateBroadcastRecvier;
import com.app.regmd.service.UpdateService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 王海 on 2015/4/8.
 */
public class LoginMain extends Activity implements View.OnClickListener{
    private static final int Update_message = 1;
    private TextView e_username;
    private TextView t_message;
    private Button b_reg;
    private Button b_look;
    private MyDatabaseHelper myDatabaseHelper;
    private int number = 0;
    private User user = null;
    private MyBroadcast myBroadcast;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case Update_message:{
                    number = msg.arg1;
                    t_message.setText(number+"");
                }
            }
        }
    };

    @Override
    protected void onStart() {
        myBroadcast = new MyBroadcast();
        registerReceiver(myBroadcast,new IntentFilter("MYBROADS"));
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginamain);
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");
        e_username = (TextView) findViewById(R.id.textView);
        t_message = (TextView) findViewById(R.id.t_message);
        b_reg = (Button) findViewById(R.id.b_registration);
        b_look = (Button) findViewById(R.id.b_look);
        if (user.getFlag().equals("1")){
            b_reg.setVisibility(View.VISIBLE);
        }
        e_username.setText("欢迎："+user.getName()+user.getFlag());
        b_reg.setOnClickListener(this);
        b_look.setOnClickListener(this);
        updateMessage();
        sharefSave();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.b_registration:{
                myDatabaseHelper = new MyDatabaseHelper(LoginMain.this,"UserLog.db", null, 1);
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                db.beginTransaction();
                try{
                    ContentValues values = new ContentValues();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                    String str = formatter.format(curDate);
                    values.put("dayNumber", number+1);
                    values.put("dayDate", str);
                    values.put("userId",user.getId());
                    db.insert("logRecord", null, values);
                    values.clear();
                    values.put("flag", "0");
                    db.update("user",values,"id = ?", new String[]{user.getId()+""});
                    db.setTransactionSuccessful();
                    updateMessage();
                    b_reg.setVisibility(View.INVISIBLE);
                    Intent intent1 = new Intent(LoginMain.this, UpdateBroadcastRecvier.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginMain.this, 1, intent1, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 50 * 1000, pendingIntent);
                }catch(Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }

                break;
            }
            case R.id.b_look:{
                Intent intent = new Intent(LoginMain.this, ListViewLayout.class);
                intent.putExtra("luser", user);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    public void updateMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                myDatabaseHelper = new MyDatabaseHelper(LoginMain.this,"UserLog.db", null, 1);
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                db.beginTransaction();
                Cursor cursor = null;
                try{
//                    cursor = db.query("logrec", null, "userId = ?", new String[]{(String)user.getId()},null,null,null);
                    cursor = db.rawQuery("select count(*) from logRecord where userId = ?",new String[]{Integer.toString(user.getId())});
                    cursor.moveToFirst();
                    int daynumber = 0;
                    daynumber =(int)cursor.getLong(0);
                    Message message = new Message();
                    message.what = Update_message;
                    message.arg1 = daynumber;
                    handler.sendMessage(message);
                    db.setTransactionSuccessful();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    cursor.close();
                    db.endTransaction();
                }

            }
        }).start();
    }

    public void sharefSave(){
        SharedPreferences.Editor editor = getSharedPreferences("user", Context.MODE_PRIVATE).edit();
        editor.putInt("id", user.getId());
        editor.commit();
    }

    public class MyBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            b_reg.setVisibility(View.VISIBLE);
        }
    }
    @Override
    protected void onDestroy() {
        stopService(new Intent(LoginMain.this, UpdateService.class));
        unregisterReceiver(myBroadcast);
        super.onDestroy();
    }
}
