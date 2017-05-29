package com.mahkota_company.android.prospect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.R;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.utils.CONFIG;


public class Useless_AndroidCanvas1 extends Activity{
    private DatabaseHandler databaseHandler;
    public static final String HASIL_TTD = "HASILTTD";
    private TextView kode_customer;
    private TextView hasilttd;
    private String hasil;

	private CanvasView canvasView;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		// myDrawView = new MyDrawView(this, null);
		setContentView(R.layout.canvas_ttd_main_prospect);
		canvasView = (CanvasView) findViewById(R.id.draw);

        Intent intent = getIntent();
        String Id_customer = intent.getStringExtra(AddCustomerProspectActivity.KODE_CUSTOMER);
        kode_customer = (TextView) findViewById(R.id.id_customer);
        hasilttd=(TextView) findViewById(R.id.hasilttd);
        kode_customer.setText(Id_customer);
	}
    public void onClick(View v){
        File folder = new File(CONFIG.getFolderPath() + "/"
                + CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
                //new File(Environment.getExternalStorageDirectory().toString());
        boolean success = false;

        if (!folder.exists()) {
            folder.mkdirs();
        }
        /*if (!folder.exists()){
            success = folder.mkdirs();
        }
        */

        System.out.println(success+"folder");
        File file = new File(folder.getPath() + File.separator +"TTD1_IMG."
                +kode_customer.getText().toString()+ ".png");
        //new File(Environment.getExternalStorageDirectory().toString() + "/sample.png");
        hasil = "TTD1_IMG." +kode_customer.getText().toString()+ ".png";


        if ( file.exists() ){
            try {
                success = file.createNewFile();
                //hasilttd.setText(hasil);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        System.out.println(success+"file");
        FileOutputStream ostream = null;
        try
        {
            ostream = new FileOutputStream(file);

            System.out.println(ostream);
            View targetView = canvasView;

            // myDrawView.setDrawingCacheEnabled(true);
            //   Bitmap save = Bitmap.createBitmap(myDrawView.getDrawingCache());
            //   myDrawView.setDrawingCacheEnabled(false);
            // copy this bitmap otherwise distroying the cache will destroy
            // the bitmap for the referencing drawable and you'll not
            // get the captured view
            //   Bitmap save = b1.copy(Bitmap.Config.ARGB_8888, false);
            //BitmapDrawable d = new BitmapDrawable(b);
            //canvasView.setBackgroundDrawable(d);
            //   myDrawView.destroyDrawingCache();
            // Bitmap save = myDrawView.getBitmapFromMemCache("0");
            // myDrawView.setDrawingCacheEnabled(true);
            //Bitmap save = myDrawView.getDrawingCache(false);
            Bitmap well = canvasView.getBitmap();
            Bitmap save = Bitmap.createBitmap(320, 480, Config.ARGB_8888);
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            Canvas now = new Canvas(save);
            now.drawRect(new Rect(0,0,320,480), paint);
            now.drawBitmap(well, new Rect(0,0,well.getWidth(),well.getHeight()), new Rect(0,0,320,480), null);

            // Canvas now = new Canvas(save);
            //myDrawView.layout(0, 0, 100, 100);
            //myDrawView.draw(now);
            if(save == null) {
                System.out.println("NULL bitmap save\n");
            }
            save.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            //bitmap.compress(Bitmap.CompressFormat.PNG, 100, ostream);
            //ostream.flush();
            //ostream.close();
        }catch (NullPointerException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Null error", Toast.LENGTH_SHORT).show();
        }

        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "File error", Toast.LENGTH_SHORT).show();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "IO error", Toast.LENGTH_SHORT).show();
        }
        //selesai();
        finish();
    }

    private void selesai() {
        String hasil_ttd;
        hasil_ttd = hasilttd.getText().toString();
        Intent intentActivity = new Intent(Useless_AndroidCanvas1.this,
                AddCustomerProspectActivity.class);
        intentActivity.putExtra(HASIL_TTD, hasil_ttd);
        startActivity(intentActivity);
    }

    public void clearCanvas(View v) {
        canvasView.clearCanvas();
    }
}
