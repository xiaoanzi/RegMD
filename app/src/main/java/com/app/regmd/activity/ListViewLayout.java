package com.app.regmd.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.app.regmd.R;
import com.app.regmd.db.MyDatabaseHelper;
import com.app.regmd.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王海 on 2015/4/8.
 */
public class ListViewLayout extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyDatabaseHelper myDatabaseHelper;
    private User user = null;
    private MyAdapter myAdapter;
    private List<String> mdataes = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listviewlayout);
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("luser");
        myDatabaseHelper = new MyDatabaseHelper(this, "UserLog.db", null, 1);
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        Cursor cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query("logRecord", null, "userId = ?", new String[]{user.getId()+""},null,null,"id");
            if (cursor.getCount() > 0){
                while (cursor.moveToNext()){
                    mdataes.add(cursor.getString(cursor.getColumnIndex("dayDate")));
                }
                myAdapter = new MyAdapter(mdataes);
                mRecyclerView.setAdapter(myAdapter);
            }else{
                Toast.makeText(this, "暂时没有签到记录", Toast.LENGTH_SHORT).show();
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            db.endTransaction();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
