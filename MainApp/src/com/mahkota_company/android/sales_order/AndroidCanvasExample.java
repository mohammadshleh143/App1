package com.mahkota_company.android.sales_order;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import com.mahkota_company.android.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class AndroidCanvasExample extends Activity {

	private CanvasView customCanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.canvas_ttd_main);

		customCanvas = (CanvasView) findViewById(R.id.signature_canvas);
	}

	public void clearCanvas(View v) {
		customCanvas.clearCanvas();
	}

	public void onClick(View v)
	{


		File folder = new File(Environment.getExternalStorageDirectory().toString());
		boolean success = false;
		if (!folder.exists())
		{
			success = folder.mkdirs();
		}

		System.out.println(success+"folder");

		File file = new File(Environment.getExternalStorageDirectory().toString() + "/sample.png");

		if ( file.exists() )
		{
			try {
				success = file.createNewFile();
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
			View targetView = customCanvas;

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
			Bitmap well = customCanvas.getBitmap();
			Bitmap save = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
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
		finish();
	}


}