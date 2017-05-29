package com.mahkota_company.android.product;

import com.mahkota_company.android.customer.DetailEditCustomer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Kemasan;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.FileUtils;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.RowItem;
import com.mahkota_company.android.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class DetailProductActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private TextView tvTitle,tvid;
	private TextView tvHarga;
	private TextView tvStock,tvimg;
	private TextView tvDeskripsi;
	private Button tambahpic,savepic;
	private DatabaseHandler databaseHandler;
	private ProgressDialog progressDialog;
	private ImageView galleryImages;
	private ArrayList<Product> product_list = new ArrayList<Product>();
	private Product product;
	private Typeface typefaceSmall;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private String newImageName1;
	private static final String LOG_TAG = DetailEditCustomer.class
			.getSimpleName();
	private int counterFoto = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_product);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoProduct();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_promosi_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		tvid = (TextView) findViewById(R.id.detail_id);
		tvTitle = (TextView) findViewById(R.id.detail_title);
		tvHarga = (TextView) findViewById(R.id.detail_harga);
		tvStock = (TextView) findViewById(R.id.detail_stock);
		tvimg = (TextView) findViewById(R.id.detail_img);
		tvDeskripsi = (TextView) findViewById(R.id.detail_deskripsi);
		tambahpic=(Button) findViewById(R.id.activity_product_btn_add_pic);
		savepic=(Button) findViewById(R.id.activity_product_btn_save_pic);
		tvTitle.setTypeface(typefaceSmall);
		tvimg.setTypeface(typefaceSmall);
		tvid.setTypeface(typefaceSmall);
		tvStock.setTypeface(typefaceSmall);
		tvHarga.setTypeface(typefaceSmall);
		tvDeskripsi.setTypeface(typefaceSmall);
		galleryImages = (ImageView) findViewById(R.id.gallery1);
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_ID_PRODUCT, null);
		if (main_app_table_id != null) {
			showProductFromDB(main_app_table_id);
		} else {
			gotoProduct();
		}

		tambahpic.setOnClickListener(mDetailCustomerButtonOnClickListener);
		savepic.setOnClickListener(mDetailCustomerButtonOnClickListener);

		savepic.setVisibility(View.INVISIBLE);

		SharedPreferences sspPreferences = getSharedPrefereces();
		final String main_app_foto_1 = sspPreferences.getString(
				CONFIG.SHARED_PREFERENCES_Product_FOTO_1, null);
		if (main_app_foto_1 != null && main_app_foto_1.length() > 0) {
			counterFoto +=1;
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT);
			String imgResource = dir.getPath() + "/" + main_app_foto_1;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			final Bitmap bitmap = BitmapFactory.decodeFile(imgResource, options);

			/*
			galleryImages.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					File dir = new File(CONFIG.getFolderPath() + "/"
							+ CONFIG.CONFIG_APP_FOLDER_PRODUCT);
					final Dialog settingsDialog = new Dialog(act);
					settingsDialog.getWindow().requestFeature(
							Window.FEATURE_NO_TITLE);
					settingsDialog.setContentView(getLayoutInflater().inflate(
							R.layout.activity_popup_image_product, null));
					ImageView imgPreview = (ImageView) settingsDialog
							.findViewById(R.id.mygallery);
					//imgPreview.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath()));
					//imgPreview.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath()));
					imgPreview.setImageBitmap(bitmap);
					imgPreview.setImageBitmap(bitmap);
					Button button = (Button) settingsDialog
							.findViewById(R.id.btn);
					button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							settingsDialog.dismiss();
						}
					});
					settingsDialog.show();
				}
			});
			*/


		}
		else {
			galleryImages.setVisibility(View.INVISIBLE);
		}

	}

	private final OnClickListener mDetailCustomerButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
				case R.id.activity_product_btn_add_pic:
					gotoCaptureImage();
					break;
				default:
					break;
			}
		}

	};

	public void gotoCaptureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	public Uri getOutputMediaFileUri1(int type) {
		return Uri.fromFile(getOutputMediaFile1(type));
	}

	private File getOutputMediaFile1(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_PRODUCT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Create a media file name
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator
					+ tvid.getText().toString()+"_"
					+ "IMG"+ ".png");
			newImageName1 = tvid.getText().toString()+"_"
					+ "IMG"+ ".png";
		} else {
			return null;
		}
		return mediaFile;
	}


	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/"
					+ product.getFoto());
			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_table_id = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_ID_PRODUCT, null);
			if (resultCode == RESULT_OK) {
				Log.d(LOG_TAG, "take image success");
				if (newImageName1 != null)
					tvimg.setText(newImageName1);

				Product newproduct = new Product();
				newproduct.setId_product(Integer.parseInt(main_app_table_id));
				newproduct.setFoto(newImageName1);
				newproduct.setNama_product(product.getNama_product());
				newproduct.setKode_product(product.getKode_product());
				newproduct.setHarga_jual(product.getHarga_jual());
				newproduct.setStock(product.getStock());
				newproduct.setId_kemasan(product.getId_kemasan());
				newproduct.setDeskripsi(product.getDeskripsi());
				newproduct.setUomqtyl1(product.getUomqtyl1());
				newproduct.setUomqtyl2(product.getUomqtyl2());
				newproduct.setUomqtyl3(product.getUomqtyl3());
				newproduct.setUomqtyl4(product.getUomqtyl4());
				newproduct.setStatus(product.getStatus());

				databaseHandler.updateProduct(Integer.parseInt(main_app_table_id), newproduct);
				saveAppProductFoto1(newImageName1);
				showProductFromDB(main_app_table_id);
				Toast.makeText(getApplicationContext(),
						"Gambar berhasil diperbaharui!", Toast.LENGTH_SHORT)
						.show();
			} else if (resultCode == RESULT_CANCELED) {
				Toast.makeText(getApplicationContext(),
						"Batal mengambil foto terbaru!", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Failed to capture image", Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void saveAppProductFoto1(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_1,
				responsedata);
		editor.commit();
	}

	public HttpResponse getDownloadData(String url) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		HttpResponse response;
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			response = client.execute(get);
		} catch (UnsupportedEncodingException e1) {
			response = null;
		} catch (Exception e) {
			e.printStackTrace();
			response = null;
		}

		return response;
	}

	public void showProductFromDB(String id) {
		product_list.clear();
		product = null;
		Product product_from_db = databaseHandler.getProduct(Integer
				.parseInt(id));
		if (product_from_db != null) {
			product = product_from_db;
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/"
					+ product_from_db.getFoto());
			if (!dir.exists()) {
				product_list.add(product_from_db);
			}

			if (product_list.size() > 0) {
				if (GlobalApp.checkInternetConnection(act)) {
					processDownloadContentProduct();
				} else {
					String message = act.getApplicationContext().getResources()
							.getString(R.string.app_product_processing_empty);
					showCustomDialog(message);
					tvTitle.setVisibility(View.GONE);
					tvid.setVisibility(View.GONE);
					tvHarga.setVisibility(View.GONE);
					tvDeskripsi.setVisibility(View.GONE);
					galleryImages.setVisibility(View.GONE);
				}
			} else {
				showDataProduct();
			}
		} else {
			tvTitle.setVisibility(View.GONE);
			tvid.setVisibility(View.GONE);
			tvHarga.setVisibility(View.GONE);
			tvDeskripsi.setVisibility(View.GONE);
			galleryImages.setVisibility(View.GONE);
		}
	}

	public void processDownloadContentProduct() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_product_processing_download_content));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);
		progressDialog.show();
		DownloadContentProduct task = new DownloadContentProduct();
		List<String> stringImg = new ArrayList<String>();
		for (int i = 0; i < product_list.size(); i++) {
			String img1 = product_list.get(i).getFoto().replaceAll(" ", "%20");
			String download_image = CONFIG.CONFIG_APP_URL_DIR_IMG_PRODUCT
					+ img1 + "#" + product_list.get(i).getFoto();
			stringImg.add(download_image);
		}
		String[] imgArr = new String[stringImg.size()];
		imgArr = stringImg.toArray(imgArr);
		task.execute(imgArr);
	}

	private class DownloadContentProduct extends
			AsyncTask<String, Integer, List<RowItem>> {
		List<RowItem> rowItems;
		int noOfURLs;

		protected List<RowItem> doInBackground(String... urls) {
			noOfURLs = urls.length;
			rowItems = new ArrayList<RowItem>();
			Bitmap map = null;
			for (String url : urls) {
				map = downloadImage(url.split("#")[0], url.split("#")[1]);
				rowItems.add(new RowItem(map));
			}
			return rowItems;
		}

		private Bitmap downloadImage(String urlString, String fileName) {
			int count = 0;
			Bitmap bitmap = null;
			URL url;
			InputStream inputStream = null;
			BufferedOutputStream outputStream = null;
			OutputStream output = null;

			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			try {
				url = new URL(urlString);
				output = new FileOutputStream(dir + "/" + fileName);
				URLConnection connection = url.openConnection();
				int lenghtOfFile = connection.getContentLength();
				inputStream = new BufferedInputStream(url.openStream());
				ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
				outputStream = new BufferedOutputStream(dataStream);
				byte data[] = new byte[512];
				long total = 0;
				while ((count = inputStream.read(data)) != -1) {
					total += count;
					publishProgress((int) ((total * 100) / lenghtOfFile));
					outputStream.write(data, 0, count);
					output.write(data, 0, count);
				}
				outputStream.flush();
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				byte[] bytes = dataStream.toByteArray();
				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						bmOptions);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				FileUtils.close(output);
				FileUtils.close(inputStream);
				FileUtils.close(outputStream);
			}
			return bitmap;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
			if (rowItems != null) {
				progressDialog.setMessage("Loading " + (rowItems.size() + 1)
						+ "/" + noOfURLs);
			}
		}

		protected void onPostExecute(List<RowItem> rowItems) {
			progressDialog.dismiss();
			showDataProduct();
		}
	}

	public void showCustomDialog(String msg) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
						act.getApplicationContext().getResources()
								.getString(R.string.MSG_DLG_LABEL_OK),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();

							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void showDataProduct() {
		if (product != null) {
			tvTitle.setVisibility(View.VISIBLE);
			tvid.setVisibility(View.VISIBLE);
			tvHarga.setVisibility(View.VISIBLE);
			tvStock.setVisibility(View.VISIBLE);
			tvDeskripsi.setVisibility(View.VISIBLE);
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/"
					+ product.getFoto());
			if (dir.exists()) {
				galleryImages.setImageBitmap(BitmapFactory.decodeFile(dir
						.getAbsolutePath()));
			} else {
				Bitmap icon = BitmapFactory.decodeResource(
						getApplicationContext().getResources(),
						R.drawable.ic_logo);
				galleryImages.setImageBitmap(icon);
			}

			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_table_id = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_ID_PRODUCT, null);
			tvTitle.setText(product.getNama_product());
			tvid.setText(main_app_table_id);
			Kemasan kemasan = databaseHandler.getKemasan(Integer
					.parseInt(product.getId_kemasan()));
			if (kemasan != null)
				tvStock.setText(getApplicationContext().getResources()
						.getString(R.string.app_product_stock)
						+ " "
						+ product.getStock()
						+ " "
						+ kemasan.getNama_id_kemasan());
			Float priceIDR = Float.valueOf(product.getHarga_jual());

			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);

			tvHarga.setText(getApplicationContext().getResources().getString(
					R.string.app_product_harga)
					+ " " + "Rp. " + df.format(priceIDR));

			tvDeskripsi.setText(product.getDeskripsi());

		}

	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void gotoProduct() {
		Intent i = new Intent(this, ProductActivity.class);
		startActivity(i);
		finish();
	}
}
