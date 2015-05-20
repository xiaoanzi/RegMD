package com.app.regmd.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.regmd.R;
import com.app.regmd.db.MyDatabaseHelper;

/**
 * Created by 王海 on 2015/4/8.
 */
public class RegistrationLayout extends Activity implements View.OnClickListener{
    private EditText e_userName;
    private EditText e_password;
    private EditText e_passwordSure;
    private EditText e_email;
    private Button b_registration;
    private Button b_back;
    private String userName = null;
    private String password = null;
    private String passwordSure = null;
    private String email = null;
    private MyDatabaseHelper myDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        e_userName = (EditText) findViewById(R.id.edit_userName);
        e_password = (EditText) findViewById(R.id.edit_password);
        e_passwordSure = (EditText) findViewById(R.id.edit_passwordSure);
        e_email = (EditText) findViewById(R.id.edit_email);
        b_registration = (Button) findViewById(R.id.button_registration);
        b_back = (Button) findViewById(R.id.button_back);
        b_registration.setOnClickListener(this);
        b_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_registration:{
                userName = e_userName.getText().toString();
                password  = e_password.getText().toString();
                passwordSure = e_passwordSure.getText().toString();
                email = e_email.getText().toString();
                if (!userName.isEmpty() || !password.isEmpty() || !passwordSure.isEmpty() || !email.isEmpty()){
                    if (password.equals(passwordSure)){
                        myDatabaseHelper = new MyDatabaseHelper(this,"UserLog.db",null,1);
                        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                        ContentValues values = new ContentValues();
                        values.put("name",userName);
                        values.put("password", password);
                        values.put("email", email);
                        values.put("flag", "1");
                        db.beginTransaction();
                        try {
                            db.insert("user", null, values);
                            db.setTransactionSuccessful();
                            Toast.makeText(this, "注册成功！请登录吧~~~", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistrationLayout.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            e.printStackTrace();
                        }finally {
                            db.endTransaction();
                        }
                    }else{
                        Toast.makeText(this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "请确保数据的完整性", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_back:{
                finish();
                break;
            }
            default:break;
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
