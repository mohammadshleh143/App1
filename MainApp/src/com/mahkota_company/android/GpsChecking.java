package com.mahkota_company.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class GpsChecking extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main1);
        
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
        	final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("GPS Belum Aktif, Silahkan Aktifkan GPS Terlebih dahulu..")
                   .setCancelable(false)
                   .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                       public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                           startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                           finish();
                       }
                   })
                   .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                       public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            GpsChecking.this.finish();
                       }
                   });
            final AlertDialog alert = builder.create();
            alert.show();
        }
        else
        {
        	Toast.makeText(getApplicationContext(), "GPS Enabled", Toast.LENGTH_LONG).show();
            startActivity(new Intent(getApplicationContext(),
                    SplashScreenActivity.class));
            finish();
        }
    }
}