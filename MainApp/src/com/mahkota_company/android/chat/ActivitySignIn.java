package com.mahkota_company.android.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.R;

public class ActivitySignIn extends Activity{
    public static final String NAMA = "NAMA";
    String nma;
    Button btnSignIn;
    EditText  editTextUserName, editTextPassword;
    LoginDataBaseAdapter loginDataBaseAdapter;
    TextView createacount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();

        btnSignIn=(Button)findViewById(R.id.buttonSignIn);
        editTextUserName=(EditText)findViewById(R.id.editTextUserNameToLogin);
        editTextPassword=(EditText)findViewById(R.id.editTextPasswordToLogin);
        createacount=(TextView)findViewById(R.id.create);

        createacount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                editTextUserName.setText("");
                editTextPassword.setText("");
                Intent intentSignUP=new Intent(getApplicationContext(),SignUPActivity.class);
                startActivity(intentSignUP);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get The User name and Password
                String userName=editTextUserName.getText().toString();
                String password=editTextPassword.getText().toString();

                // fetch the Password form database for respective user name
                String storedPassword=loginDataBaseAdapter.getSinlgeEntry(userName);

                // check if the Stored password matches with  Password entered by user
                if(password.equals(storedPassword))
                {
                    Toast.makeText(ActivitySignIn.this, "Wellcome " + userName, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(v.getContext(), LayananActivity.class);
                    myIntent.putExtra(NAMA, userName);
                    startActivity(myIntent);
                    ActivitySignIn.this.finish();
                }
                else
                {
                    Toast.makeText(ActivitySignIn.this, "User Name or Password does not match", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
