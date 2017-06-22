package com.mahkota_company.android.jadwal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.mahkota_company.android.R;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.Log2File;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class RequestJadwal extends FragmentActivity {
	private Context act;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = RequestJadwal.class
			.getSimpleName();
	private Typeface typefaceSmall;
	private ImageView menuBackButton;
	private Button btnUpload, refresh;
	private Button btnFoto1;
	private Button btnFoto2;
	private Button btnFoto3;
	private Button btnFoto4;
	private double latitude; // latitude
	private double longitude; // longitude
	private Location location; // location
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Uri fileUri; // file url to store image/video
	private final int MEDIA_TYPE_IMAGE = 1;
	private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private String IMAGE_DIRECTORY_NAME = "Supplier";
	private String id_Image = "";
	private String newImageName;
	private ImageView imageView1;
	private ImageView imageView2;
	private ImageView imageView3;
	private ImageView imageView4;
	private EditText etwilayah, gps, namaToko;
	private String keterangan;
	private String streetName = "";
	private int counterFoto = 0;
	private LocationManager locationManager;
	private static Context _context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_req_jadwal_tambahan);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		act = this;
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				gotoInventory();
			}
		});
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog
				.setMessage(getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_processing_download_stock_summary));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);

		etwilayah = (EditText) findViewById(R.id.supplier_et_keterangan);
		namaToko = (EditText) findViewById(R.id.etnamaToko);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		btnUpload = (Button) findViewById(R.id.supplier_btn_upload);

		SharedPreferences spPreferences = getSharedPrefereces();

		btnUpload.setTypeface(typefaceSmall);

		menuBackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {


				gotoInventory();
			}
		});
		checkGPS();
	}


	protected String uploadImage(final String url,  final String id_staff,
								 final String foto_1, final String foto_2,
								 final String foto_3, final String foto_4,
								 final String keterangan) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		String responseString = null;
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}

			MultipartEntity entity = new MultipartEntity();

			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("keterangan", new StringBody(keterangan));

			httppost.setEntity(entity);

			// Making server call
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// Server response
				responseString = EntityUtils.toString(r_entity);
			} else {
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (ClientProtocolException e) {
			responseString = e.toString();
		} catch (IOException e) {
			responseString = e.toString();
		}

		return responseString;

	}


	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
			progressDialog
					.setOnCancelListener(new DialogInterface.OnCancelListener() {
						@Override
						public void onCancel(DialogInterface dialog) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_CANCEL);
							showCustomDialog(msg);
						}
					});
		}

		@Override
		protected String doInBackground(String... params) {
			String url = CONFIG.CONFIG_APP_URL_PUBLIC;
			String uploadSupplier = CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_SUPPLIER;
			String upload_image_supplier_url = url
					+ uploadSupplier;
			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_id_staff = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);
			String main_app_id_foto1 = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_1, null);
			String main_app_id_foto2 = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_2, null);
			String main_app_id_foto3 = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_3, null);
			String main_app_id_foto4 = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_4, null);
			/***********************
			 * Upload Image Supplier
			 */
				response_data = uploadImage(
						upload_image_supplier_url,
						main_app_id_staff,
						main_app_id_foto1,
						main_app_id_foto2,
						main_app_id_foto3,
						main_app_id_foto4,
						keterangan);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.d(LOG_TAG, "response:" + response_data);
			if (response_data != null && response_data.length() > 0) {
				if (response_data.startsWith("Error occurred")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing_failed);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(msg);
						}
					});
				} else {
					handler.post(new Runnable() {
						public void run() {
							extractUpload();
						}
					});
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}


	protected List<File> getListFiles(File parentDir) {
		ArrayList<File> inFiles = new ArrayList<File>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			inFiles.add(file);
			if (file.isDirectory())
				inFiles.addAll(getListFiles(file));
		}
		return inFiles;
	}


	public void extractUpload() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_supplier_processing_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("False")) {
					saveAppSupplierFoto1("");
					saveAppSupplierFoto2("");
					saveAppSupplierFoto3("");
					saveAppSupplierFoto4("");

					File dir = new File(CONFIG.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_NAME);

					List<File> fileFoto = getListFiles(dir);
					for (File tempFile : fileFoto) {
						tempFile.delete();
					}

					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing_sukses);
					showCustomDialogDownloadSuccess(msg);

				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing_failed);
					showCustomDialog(msg);
				}

			}

		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

		}
	}


	/**
	 * Validasi Empty Box
	 *
	 * @return
	 */
	public boolean passValidationForUpload() {
		if (GlobalApp.isBlank(etwilayah)) {
			GlobalApp.takeDefaultAction(
					etwilayah,
					RequestJadwal.this,
					getApplicationContext().getResources().getString(
							R.string.app_supplier_failed_no_keterangan));
			return false;
		}
		return true;
	}

	public void showCustomDialogReplaceOldFoto(String msg) {
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
				// .setTitle(title)
				.setCancelable(false)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_YES),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								gotoCaptureImage();

							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NO),
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

	protected void refreshStatus() {
		Intent intent = getIntent();
		overridePendingTransition(0, 0);
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		overridePendingTransition(0, 0);
		startActivity(intent);
	}


	public void gotoCaptureImage() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/*
 * Creating file uri to store image/video
 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ IMAGE_DIRECTORY_NAME);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator + timeStamp + "_" + id_Image + ".jpg");
			newImageName = timeStamp + "_"
					+ id_Image + ".jpg";
		} else {
			return null;
		}
		return mediaFile;
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			try {
				String strStatus = "";
				switch (status) {
					case GpsStatus.GPS_EVENT_FIRST_FIX:
						strStatus = "GPS_EVENT_FIRST_FIX";
						break;
					case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
						strStatus = "GPS_EVENT_SATELLITE_STATUS";
						break;
					case GpsStatus.GPS_EVENT_STARTED:
						strStatus = "GPS_EVENT_STARTED";
						break;
					case GpsStatus.GPS_EVENT_STOPPED:
						strStatus = "GPS_EVENT_STOPPED";
						break;
					default:
						strStatus = String.valueOf(status);
						break;
				}
				Log.i(LOG_TAG, "locationListener " + strStatus);
			} catch (Exception e) {
				Log.d(LOG_TAG, "locationListener Error");
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			try {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Log.d(LOG_TAG, "latitude " + latitude);
				Log.d(LOG_TAG, "longitude " + longitude);
			} catch (Exception e) {
				Log.i(LOG_TAG, "onLocationChanged " + e.toString());
			}
		}
	};

	public String getAddress(double lat, double lng) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
			if (addresses != null) {
				Address returnedAddress = addresses.get(0);
				StringBuilder strReturnedAddress = new StringBuilder("");

				for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
					strReturnedAddress
							.append(returnedAddress.getAddressLine(i)).append(
							"\n");
				}
				strAdd = strReturnedAddress.toString();
				Log2File.log(LOG_TAG,
						"Location address " + strReturnedAddress.toString());
			} else {
				Log.w(LOG_TAG,
						"My Current loction address No Address returned!");
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, "My Current loction address Canont get Address!");
		}
		return strAdd;
	}

	private void checkGPS() {
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, locationListener);
		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					0);
		} else {
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
					Log.d(LOG_TAG, "GPS Enabled");

				}
			} else {
				Intent intentGps = new Intent(
						"android.location.GPS_ENABLED_CHANGE");
				intentGps.putExtra("enabled", true);
				act.sendBroadcast(intentGps);
			}

		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void gotoInventory() {
		Intent i = new Intent(this, JadwalActivity.class);
		startActivity(i);
		finish();
	}


	public void showCustomDialogDownloadSuccess(String msg) {
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

								etwilayah.setText("");

								refreshStatus();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void saveAppSupplierFoto1(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_1,
				responsedata);
		editor.commit();
	}

	public void saveAppSupplierFoto2(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_2,
				responsedata);
		editor.commit();
	}

	public void saveAppSupplierFoto3(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_3,
				responsedata);
		editor.commit();
	}

	public void saveAppSupplierFoto4(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_SUPPLIER_FOTO_4,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStockVan(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN,
				responsedata);
		editor.commit();
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

}