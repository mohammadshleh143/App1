package com.mahkota_company.android.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.mahkota_company.android.R;

public class CheckLoginDulu extends Activity{
    public static final String NAMA = "NAMA";
    LoginDataBaseAdapter loginDataBaseAdapter;
    EditText username, password;
    private final String SELECT_SQL = "SELECT * FROM LOGIN";
    private SQLiteDatabase db;
    private Cursor b,c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.obalogin);

        openDatabase();
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pass);
        c = db.rawQuery(SELECT_SQL, null);
        c.moveToLast();

        if(c.getCount()!=0){
            showRecords();
            Pilihan();
        }else {
            LoginManual();
        }
    }

    private void Pilihan() {
        if(username.getText().toString().equals("")){
            LoginManual();
        }else if(password.getText().toString().equals("")){
            LoginManual();
        }else{
            AutoLogin();
        }
    }

    protected void openDatabase() {
        db = openOrCreateDatabase("login.db", Context.MODE_PRIVATE, null);
    }

    protected void showRecords() {
        String name = c.getString(1);
        String pass = c.getString(2);
        username.setText(name);
        password.setText(pass);
    }

    private void LoginManual() {
        Intent intentSignUP=new Intent(CheckLoginDulu.this, ActivitySignIn.class);
        startActivity(intentSignUP);
        CheckLoginDulu.this.finish();
    }

    private void AutoLogin() {
        // get The User name and Password
        String userName=username.getText().toString();
        String Password=password.getText().toString();

        // fetch the Password form database for respective user name
        String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);

        // check if the Stored password matches with  Password entered by user
        if(Password.equals(storedPassword))
        {
            Toast.makeText(CheckLoginDulu.this, "Wellcome " + userName, Toast.LENGTH_SHORT).show();
            Intent myIntent = new Intent(CheckLoginDulu.this, LayananActivity.class);
            myIntent.putExtra(NAMA, userName);
            startActivity(myIntent);
            CheckLoginDulu.this.finish();
        }
        else
        {
            Toast.makeText(CheckLoginDulu.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
        }
    }
}