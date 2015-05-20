package com.app.regmd.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.regmd.R;
import com.app.regmd.db.MyDatabaseHelper;
import com.app.regmd.model.User;


public class MainActivity extends Activity implements View.OnClickListener{
    private EditText e_loginName;
    private EditText e_loginPassword;
    private Button b_login;
    private Button b_registration;
    private String loginName = null;
    private String loginPassword = null;
    private MyDatabaseHelper myDatabaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e_loginName = (EditText) findViewById(R.id.edit_loginName);
        e_loginPassword = (EditText) findViewById(R.id.edit_loginPassword);
        b_login = (Button)findViewById(R.id.button_login);
        b_registration = (Button) findViewById(R.id.button_registration);
        b_login.setOnClickListener(this);
        b_registration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:{
                loginName = e_loginName.getText().toString();
                loginPassword = e_loginPassword.getText().toString();
                if (!loginName.isEmpty() || !loginPassword.isEmpty()){
                    ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setTitle("正在登录");
                    progressDialog.setMessage("登录中...");
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    myDatabaseHelper = new MyDatabaseHelper(this, "UserLog.db", null, 1);
                    SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                    Cursor cursor = null;
                    db.beginTransaction();
                    try {
                        cursor = db.query("user", null, "name = ? and password = ?", new String[]{loginName, loginPassword},null,null,null);
                        int userId = 0;
                        String flag = "";
                        if (cursor.getCount() > 0){
                            while (cursor.moveToNext()){
                                userId = cursor.getInt(cursor.getColumnIndex("id"));
                                flag = cursor.getString(cursor.getColumnIndex("flag"));
                            }
                            Intent intent = new Intent(MainActivity.this, LoginMain.class);
                            User user = new User();
                            user.setId(userId);
                            user.setName(loginName);
                            user.setPassword(loginPassword);
                            user.setFlag(flag);
                            intent.putExtra("user", user);
                            progressDialog.dismiss();
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_SHORT).show();
                        }
                        db.setTransactionSuccessful();
                    }catch (Exception e){
                        e.printStackTrace();
                    }finally {
                        cursor.close();
                        db.endTransaction();
                    }
                }else {
                    Toast.makeText(this, "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.button_registration:{
                Intent intent = new Intent(MainActivity.this, RegistrationLayout.class);
                startActivity(intent);
                break;
            }
            default:break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
