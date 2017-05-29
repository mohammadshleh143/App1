package com.mahkota_company.android.chat;

/**
 * Created by Matrix Center on 12/05/2016.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.mahkota_company.android.R;

public class Splashscreen extends Activity {

    //Set waktu lama splashscreen
    private static int splashInterval = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashscreen);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent i = new Intent(Splashscreen.this, CheckLoginDulu.class);
                startActivity(i);


                //jeda selesai Splashscreen
                Splashscreen.this.finish();
            }

        }, splashInterval);

    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Toast.makeText(Splashscreen.this, "Tunggu", Toast.LENGTH_SHORT);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
