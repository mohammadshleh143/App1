package com.mahkota_company.android.Perbaikan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.R;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.TypeCustomer;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.FileUtils;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.RowItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class PerbaikanDetail extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private ProgressDialog progressDialog;
	private ArrayList<Customer> customer_list = new ArrayList<Customer>();
	private Customer customer;
	private String newImageName;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private EditText tvNamaCustomer;
	private EditText tvEmailCustomer;
	private EditText tvNoKtp;

    private EditText tvTanggalLahir;
    private EditText tvNamabank;
    private EditText tvNorek;
    private EditText tvAtasnama;
    private EditText tvNpwp;
	private EditText tvNamapasar;
	private EditText tvCluster;
	private EditText tvTelp;
	private EditText tvNohp;
	private EditText tvFax;
    private EditText tvOmset;
    private EditText tvCarapembayaran;
    private EditText tvPlafonkredit;
    private EditText tvTermkredit;
    private EditText tvNamaistri;
    private EditText tvNamaAnak1;
    private EditText tvNamaAnak2;
    private EditText tvNamaAnak3;


	private EditText tvAlamatCustomer;
	private EditText tvWilayahCustomer;
	private TextView tvTypeCustomer;
	private TextView tvGpsCustomer;
	private TextView tvImageCustomer;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderEmailCustomer;
	private TextView tvHeaderAlamatCustomer;
	private TextView tvHeaderWilayahCustomer;
	private TextView tvHeaderTypeCustomer;
	private TextView tvHeaderGpsCustomer;
	private TextView tvHeaderImageCustomer;
	private TextView tvHeaderTelpCustomer;
	private Button mButtonCustomerDetailMaps;
	private Button mButtonCustomerDetailImage;
	private Button mButtonCustomerDetailUpdateImage;
	private Button mButtonCustomerDetailSave;
	private LocationManager locationManager;
	private double tempCheckInLatitude;
	private double tempCheckInLongitude;
	private static final String LOG_TAG = com.mahkota_company.android.Perbaikan.PerbaikanDetail.class
			.getSimpleName();
	boolean isContainImage = true;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri; // file url to store image/video
	private double latitude; // latitude
	private double longitude; // longitude
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Location location; // location

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_customer);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoCustomer();
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
		tvKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_kode_customer);
		tvNamaCustomer = (EditText) findViewById(R.id.activity_customer_detail_value_nama_customer);
		tvEmailCustomer = (EditText) findViewById(R.id.activity_customer_detail_value_email_customer);
		tvNoKtp = (EditText) findViewById(R.id.activity_customer_detail_value_noktp_customer);

        tvTanggalLahir = (EditText) findViewById(R.id.activity_customer_detail_value_tanggal_lahir_customer);
        tvNamabank  = (EditText) findViewById(R.id.activity_customer_detail_value_nama_bank_customer);
        tvNorek= (EditText) findViewById(R.id.activity_customer_detail_value_no_rekening_customer);
        tvAtasnama = (EditText) findViewById(R.id.activity_customer_detail_value_atas_nama_customer);
        tvNpwp = (EditText) findViewById(R.id.activity_customer_detail_value_npwp_customer);
		tvNamapasar = (EditText) findViewById(R.id.activity_customer_detail_value_nama_pasar_customer);
		tvCluster = (EditText) findViewById(R.id.activity_customer_detail_value_cluster_customer);
		tvTelp = (EditText) findViewById(R.id.activity_customer_detail_value_telp_customer);
		tvNohp = (EditText) findViewById(R.id.activity_customer_detail_value_nope_customer);
		tvFax = (EditText) findViewById(R.id.activity_customer_detail_value_fax_customer);

        tvOmset = (EditText) findViewById(R.id.activity_customer_detail_value_omset_customer);
        tvCarapembayaran = (EditText) findViewById(R.id.activity_customer_detail_value_carpembayaran_customer);
        tvPlafonkredit = (EditText) findViewById(R.id.activity_customer_detail_value_plafon_kredit_customer);
        tvTermkredit = (EditText) findViewById(R.id.activity_customer_detail_value_term_kredit_customer);
        tvNamaistri = (EditText) findViewById(R.id.activity_customer_detail_value_nama_istri_customer);
        tvNamaAnak1 = (EditText) findViewById(R.id.activity_customer_detail_value_nama_anak1_customer);
        tvNamaAnak2 = (EditText) findViewById(R.id.activity_customer_detail_value_nama_anak2_customer);
        tvNamaAnak3 = (EditText) findViewById(R.id.activity_customer_detail_value_nama_anak3_customer);


		tvAlamatCustomer = (EditText) findViewById(R.id.activity_customer_detail_value_alamat_customer);
		tvWilayahCustomer = (EditText) findViewById(R.id.activity_customer_detail_value_wilayah_customer);
		tvTypeCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_type_customer);
		tvGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_gps_location);
		tvImageCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_image);

		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_kode_customer);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_nama_customer);
		tvHeaderEmailCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_email_customer);
		tvHeaderAlamatCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_alamat_customer);
		tvHeaderWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_wilayah_customer);
		tvHeaderTypeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_type_customer);
		tvHeaderGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_gps_location);
		tvHeaderImageCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_image);
		tvHeaderTelpCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_telp_customer);
		mButtonCustomerDetailMaps = (Button) findViewById(R.id.activity_customer_detail_btn_map);
		mButtonCustomerDetailImage = (Button) findViewById(R.id.activity_customer_detail_btn_image);
		mButtonCustomerDetailUpdateImage = (Button) findViewById(R.id.activity_customer_detail_btn_take_image);
		mButtonCustomerDetailSave = (Button) findViewById(R.id.activity_customer_detail_btn_save);

		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);
		tvEmailCustomer.setTypeface(typefaceSmall);
		tvAlamatCustomer.setTypeface(typefaceSmall);
		tvWilayahCustomer.setTypeface(typefaceSmall);
		tvTypeCustomer.setTypeface(typefaceSmall);
		tvGpsCustomer.setTypeface(typefaceSmall);
		tvImageCustomer.setTypeface(typefaceSmall);


		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderEmailCustomer.setTypeface(typefaceSmall);
		tvHeaderAlamatCustomer.setTypeface(typefaceSmall);
		tvHeaderWilayahCustomer.setTypeface(typefaceSmall);
		tvHeaderTypeCustomer.setTypeface(typefaceSmall);
		tvHeaderGpsCustomer.setTypeface(typefaceSmall);
		tvHeaderImageCustomer.setTypeface(typefaceSmall);
		tvHeaderTelpCustomer.setTypeface(typefaceSmall);
		mButtonCustomerDetailMaps
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailImage
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailSave
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailUpdateImage
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailUpdateImage.setVisibility(View.INVISIBLE);
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
		if (main_app_table_id != null) {
			saveAppDataCustomerIdCustomer(main_app_table_id);
			showCustomerFromDB(main_app_table_id);
		} else {
			gotoCustomer();
		}
		checkGPS();
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
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
			} else {
				Intent intentGps = new Intent(
						"android.location.GPS_ENABLED_CHANGE");
				intentGps.putExtra("enabled", true);
				act.sendBroadcast(intentGps);
			}

		}
	}

	public void showCustomDialogSaveSuccess(String msg) {
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
								gotoCustomer();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private double distanceNew(double lat1, double lon1, double lat2,
			double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
				+ Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
				* Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		// if (unit == 'K') {
		dist = dist * 1.609344;
		// } else if (unit == 'N') {
		// dist = dist * 0.8684;
		// }
		return (dist) * 2;
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}

	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	private double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}

	private final OnClickListener mDetailCustomerButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_customer_detail_btn_take_image:
				gotoCaptureImage();
				break;
			case R.id.activity_customer_detail_btn_image:
				if (!isContainImage) {
					showCustomDialog(getApplicationContext().getResources()
							.getString(R.string.app_customer_warning));
				} else {
					previewImageDialog();
				}
				break;
			case R.id.activity_customer_detail_btn_save:
				if (customer != null) {
					String curLatitude = String
							.valueOf((int) tempCheckInLatitude);
					String curLongitude = String
							.valueOf((int) tempCheckInLongitude);
					if (curLatitude.equalsIgnoreCase("0")
							|| curLongitude.equalsIgnoreCase("0")) {
						showConfirmationUpdateGps();
					} else {
						double differentKm = distanceNew(tempCheckInLatitude,
								tempCheckInLongitude, latitude, longitude);
						int getMeter = (int) differentKm * 1000;
						String message = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_customer_detail_far_away_location);
						if (getMeter > 50) {
							showCustomDialog(message);
						} else {
							if (newImageName != null) {
								if (customer.getFoto_2().length() == 0) {
									Customer newCustomer = new Customer();
									newCustomer.setId_customer(customer
											.getId_customer());
									newCustomer.setAlamat(customer.getAlamat());
									newCustomer.setBlokir(customer.getBlokir());
									newCustomer.setDate(customer.getDate());
									newCustomer.setEmail(customer.getEmail());
									newCustomer.setFoto_1(customer.getFoto_1());
									newCustomer.setFoto_2(newImageName);
									newCustomer.setFoto_3(customer.getFoto_3());
									newCustomer.setId_type_customer(customer
											.getId_type_customer());
									newCustomer.setId_wilayah(customer
											.getId_wilayah());
									newCustomer.setKode_customer(customer
											.getKode_customer());
									newCustomer.setLats(String
											.valueOf(latitude));
									newCustomer.setLongs(String
											.valueOf(longitude));
									newCustomer.setNama_lengkap(customer
											.getNama_lengkap());
									newCustomer.setNo_telp(customer
											.getNo_telp());
									newCustomer.setStatus_update("2");
									newCustomer.setId_staff(customer
											.getId_staff());
									databaseHandler.updateCustomer(
											customer.getId_customer(),
											newCustomer);
								} else {
									Customer newCustomer = new Customer();
									newCustomer.setId_customer(customer
											.getId_customer());
									newCustomer.setAlamat(customer.getAlamat());
									newCustomer.setBlokir(customer.getBlokir());
									newCustomer.setDate(customer.getDate());
									newCustomer.setEmail(customer.getEmail());
									newCustomer.setFoto_1(customer.getFoto_1());
									newCustomer.setFoto_2(customer.getFoto_2());
									newCustomer.setFoto_3(newImageName);
									newCustomer.setId_type_customer(customer
											.getId_type_customer());
									newCustomer.setId_wilayah(customer
											.getId_wilayah());
									newCustomer.setKode_customer(customer
											.getKode_customer());
									newCustomer.setLats(String
											.valueOf(latitude));
									newCustomer.setLongs(String
											.valueOf(longitude));
									newCustomer.setNama_lengkap(customer
											.getNama_lengkap());
									newCustomer.setNo_telp(customer
											.getNo_telp());
									newCustomer.setStatus_update("2");
									newCustomer.setId_staff(customer
											.getId_staff());
									databaseHandler.updateCustomer(
											customer.getId_customer(),
											newCustomer);
								}
							} else {
								Customer newCustomer = new Customer();
								newCustomer.setId_customer(customer
										.getId_customer());
								newCustomer.setAlamat(customer.getAlamat());
								newCustomer.setBlokir(customer.getBlokir());
								newCustomer.setDate(customer.getDate());
								newCustomer.setEmail(customer.getEmail());
								newCustomer.setFoto_1(customer.getFoto_1());
								newCustomer.setFoto_2(customer.getFoto_2());
								newCustomer.setFoto_3(customer.getFoto_3());
								newCustomer.setId_type_customer(customer
										.getId_type_customer());
								newCustomer.setId_wilayah(customer
										.getId_wilayah());
								newCustomer.setKode_customer(customer
										.getKode_customer());
								newCustomer.setLats(String.valueOf(latitude));
								newCustomer.setLongs(String.valueOf(longitude));
								newCustomer.setNama_lengkap(customer
										.getNama_lengkap());
								newCustomer.setNo_telp(customer.getNo_telp());
								newCustomer.setStatus_update("2");
								newCustomer.setId_staff(customer.getId_staff());
								databaseHandler.updateCustomer(
										customer.getId_customer(), newCustomer);
							}
							String msg = getApplicationContext().getResources()
									.getString(
											R.string.app_customer_save_success);
							showCustomDialogSaveSuccess(msg);
						}
					}
				}
				break;
			default:
				break;
			}
		}

	};

	private void previewImageDialog() {
		if (customer != null) {
			List<File> mListImages = new ArrayList<File>();
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer.getFoto_1());
			if (dir.isFile()) {
				mListImages.add(dir);
			}
			File dir2 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer.getFoto_2());
			if (dir2.isFile()) {
				mListImages.add(dir2);
			}
			File dir3 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer.getFoto_3());
			if (dir3.isFile()) {
				mListImages.add(dir3);
			}
			final Dialog imagesDialog = new Dialog(act);
			imagesDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			imagesDialog.setContentView(com.mahkota_company.android.Perbaikan.PerbaikanDetail.this
					.getLayoutInflater().inflate(R.layout.activity_popup_image,
							null));

			Gallery galleryImages = (Gallery) imagesDialog
					.findViewById(R.id.gallery1);
			galleryImages.setAdapter(new ImageAdapter(this, mListImages));

			imagesDialog.show();
		}

	}

	protected void showConfirmationUpdateGps() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_UPDATE_GPS_DIALOG);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_YES),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								updateGpsLocation();
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

	protected void updateGpsLocation() {
		Customer newCustomer = new Customer();
		newCustomer.setId_customer(customer.getId_customer());
		newCustomer.setAlamat(customer.getAlamat());
		newCustomer.setBlokir(customer.getBlokir());
		newCustomer.setDate(customer.getDate());
		newCustomer.setEmail(customer.getEmail());
        newCustomer.setNo_ktp(customer.getNo_ktp());
        newCustomer.setAlamat(customer.getAlamat());
		newCustomer.setFoto_1(customer.getFoto_1());
		newCustomer.setFoto_2(customer.getFoto_2());
		newCustomer.setFoto_3(customer.getFoto_3());
		newCustomer.setId_type_customer(customer.getId_type_customer());
		newCustomer.setId_wilayah(customer.getId_wilayah());
		newCustomer.setKode_customer(customer.getKode_customer());
		newCustomer.setLats(String.valueOf(latitude));
		newCustomer.setLongs(String.valueOf(longitude));
		newCustomer.setNama_lengkap(customer.getNama_lengkap());
		newCustomer.setNo_telp(customer.getNo_telp());
		newCustomer.setStatus_update("2");
		newCustomer.setId_staff(customer.getId_staff());
		databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
		String msg = getApplicationContext().getResources().getString(
				R.string.app_customer_save_success);
		showCustomDialogSaveSuccess(msg);
	}

	public void gotoCaptureImage() {
		if (customer != null) {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

			// start the image capture Intent
			startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
		}
	}

	/*
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		int id_image = 2;
		if (customer.getFoto_2().length() > 0)
			id_image = 3;
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator
					+ customer.getKode_customer() + "_" + "IMG_" + timeStamp
					+ "_" + id_image + ".png");
			newImageName = customer.getKode_customer() + "_" + "IMG_"
					+ timeStamp + "_" + id_image + ".png";
		} else {
			return null;
		}
		return mediaFile;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		checkGPS();
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
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

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		private List<File> listFile;

		public ImageAdapter(Context context, List<File> listFile) {
			this.context = context;
			this.listFile = listFile;
			TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
			itemBackground = a.getResourceId(
					R.styleable.MyGallery_android_galleryItemBackground, 0);
			a.recycle();
		}

		public int getCount() {
			return listFile.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			if (listFile == null) {
				Bitmap icon = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.ic_logo);
				imageView.setImageBitmap(icon);
			} else {
				if (listFile.get(position).exists()) {
					imageView.setImageBitmap(BitmapFactory.decodeFile(listFile
							.get(position).getAbsolutePath()));
				} else {
					Bitmap icon = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.ic_logo);
					imageView.setImageBitmap(icon);
				}
			}
			// imageView.setLayoutParams(new Gallery.LayoutParams(260, 260));
			imageView.setBackgroundResource(itemBackground);
			return imageView;
		}
	}

	public void saveAppDataCustomerIdCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER,
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

	public void showCustomerFromDB(String id) {
		customer_list.clear();
		customer = null;

		Customer customer_from_db = databaseHandler.getCustomer(Integer
				.parseInt(id));
		if (customer_from_db != null) {
			customer = customer_from_db;

			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer_from_db.getFoto_1());

			File dir2 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer_from_db.getFoto_2());

			File dir3 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer_from_db.getFoto_3());
			if (!dir.exists() || !dir2.exists() || !dir3.exists()) {
				customer_list.add(customer_from_db);
			}

			if (customer_list.size() > 0) {
				if (GlobalApp.checkInternetConnection(act)) {
					processDownloadContentCustomer();
				} else {
					String message = act.getApplicationContext().getResources()
							.getString(R.string.app_customer_processing_empty);
					showCustomDialog(message);
					tvKodeCustomer.setVisibility(View.GONE);
					tvNamaCustomer.setVisibility(View.GONE);
					tvEmailCustomer.setVisibility(View.GONE);
					tvAlamatCustomer.setVisibility(View.GONE);
					tvWilayahCustomer.setVisibility(View.GONE);
					tvTypeCustomer.setVisibility(View.GONE);
					tvGpsCustomer.setVisibility(View.GONE);
					tvImageCustomer.setVisibility(View.GONE);

				}
			} else {
				showDataCustomer();
			}
		} else {
			tvKodeCustomer.setVisibility(View.GONE);
			tvNamaCustomer.setVisibility(View.GONE);
			tvEmailCustomer.setVisibility(View.GONE);
			tvAlamatCustomer.setVisibility(View.GONE);
			tvWilayahCustomer.setVisibility(View.GONE);
			tvTypeCustomer.setVisibility(View.GONE);
			tvGpsCustomer.setVisibility(View.GONE);
			tvImageCustomer.setVisibility(View.GONE);

		}

	}

	public void processDownloadContentCustomer() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_customer_processing_download_content));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);
		progressDialog.show();
		DownloadContentCustomer task = new DownloadContentCustomer();
		List<String> stringImg = new ArrayList<String>();
		for (int i = 0; i < customer_list.size(); i++) {
			String img1 = customer_list.get(i).getFoto_1()
					.replaceAll(" ", "%20");
			String download_image = CONFIG.CONFIG_APP_URL_DIR_IMG_CUSTOMER
					+ img1 + "#" + customer_list.get(i).getFoto_1();
			if (customer_list.get(i).getFoto_1().length() > 0) {
				File dir = new File(CONFIG.getFolderPath() + "/"
						+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
						+ customer_list.get(i).getFoto_1());
				if (!dir.exists())
					stringImg.add(download_image);
			}

			String img2 = customer_list.get(i).getFoto_2()
					.replaceAll(" ", "%20");
			String download_image2 = CONFIG.CONFIG_APP_URL_DIR_IMG_CUSTOMER
					+ img2 + "#" + customer_list.get(i).getFoto_2();
			if (customer_list.get(i).getFoto_2().length() > 0) {
				File dir2 = new File(CONFIG.getFolderPath() + "/"
						+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
						+ customer_list.get(i).getFoto_2());
				if (!dir2.exists())
					stringImg.add(download_image2);

			}

			String img3 = customer_list.get(i).getFoto_3()
					.replaceAll(" ", "%20");
			String download_image3 = CONFIG.CONFIG_APP_URL_DIR_IMG_CUSTOMER
					+ img3 + "#" + customer_list.get(i).getFoto_3();
			if (customer_list.get(i).getFoto_3().length() > 0) {
				File dir3 = new File(CONFIG.getFolderPath() + "/"
						+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
						+ customer_list.get(i).getFoto_3());
				if (!dir3.exists())
					stringImg.add(download_image3);
			}
		}
		String[] imgArr = new String[stringImg.size()];
		imgArr = stringImg.toArray(imgArr);
		task.execute(imgArr);

	}

	private class DownloadContentCustomer extends
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
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER);
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
			showDataCustomer();
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

	public void showDataCustomer() {
		if (customer != null) {
			tvKodeCustomer.setVisibility(View.VISIBLE);
			tvNamaCustomer.setVisibility(View.VISIBLE);
			tvEmailCustomer.setVisibility(View.VISIBLE);
			tvAlamatCustomer.setVisibility(View.VISIBLE);
			tvWilayahCustomer.setVisibility(View.VISIBLE);
			tvTypeCustomer.setVisibility(View.VISIBLE);
			tvGpsCustomer.setVisibility(View.VISIBLE);
			tvImageCustomer.setVisibility(View.INVISIBLE);


			tvKodeCustomer.setText(customer.getKode_customer());
			tvNamaCustomer.setText(customer.getNama_lengkap());
			tvEmailCustomer.setText(customer.getEmail());
            tvNoKtp.setText(customer.getNo_ktp());
            tvTanggalLahir.setText(customer.getTanggal_lahir());
            tvNamabank.setText(customer.getNama_bank());
            tvNorek.setText(customer.getNo_rekening());
            tvAtasnama.setText(customer.getAtas_nama());
            tvNpwp.setText(customer.getNpwp());
			tvNamapasar.setText(customer.getNama_pasar());
			tvTelp.setText(customer.getTelp());
			tvNohp.setText(customer.getNo_telp());
			tvFax.setText(customer.getFax());
            tvOmset.setText(customer.getOmset());
            tvCarapembayaran.setText(customer.getCara_pembayaran());
            tvPlafonkredit.setText(customer.getPlafon_kredit());
            tvTermkredit.setText(customer.getTerm_kredit());
            tvNamaistri.setText(customer.getNama_istri());
            tvNamaAnak1.setText(customer.getNama_anak1());
            tvNamaAnak2.setText(customer.getNama_anak2());
            tvNamaAnak3.setText(customer.getNama_anak3());

			tvAlamatCustomer.setText(customer.getAlamat());
			Wilayah wilayah = databaseHandler.getWilayah(customer
					.getId_wilayah());
			if (wilayah != null)
				tvWilayahCustomer.setText(wilayah.getNama_wilayah());
			TypeCustomer typeCustomer = databaseHandler
					.getTypeCustomer(customer.getId_type_customer());
			if (typeCustomer != null)
				tvTypeCustomer.setText(typeCustomer.getDeskripsi());
			tvGpsCustomer.setText(customer.getLats() + " / "
					+ customer.getLongs());


			boolean isGps = true;
			if (customer.getLats().equalsIgnoreCase("null")
					|| customer.getLongs().equalsIgnoreCase("null")) {
				isGps = false;
			} else if (customer.getLats().equalsIgnoreCase("")
					|| customer.getLongs().equalsIgnoreCase("")) {
				isGps = false;
			} else if (customer.getLats().equalsIgnoreCase("0")
					|| customer.getLongs().equalsIgnoreCase("0")) {
				isGps = false;
			} else if (customer.getLats().equalsIgnoreCase("0.0")
					|| customer.getLongs().equalsIgnoreCase("0.0")) {
				isGps = false;
			} else {
				isGps = true;
			}
			mButtonCustomerDetailMaps.setEnabled(isGps);

			tempCheckInLatitude = Double.parseDouble(customer.getLats());
			tempCheckInLongitude = Double.parseDouble(customer.getLongs());

			File dir1 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer.getFoto_1());

			File dir2 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer.getFoto_2());

			File dir3 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
					+ customer.getFoto_3());
			if (!dir1.exists() && !dir2.exists() && !dir3.exists()) {
				isContainImage = false;
			} else {
				isContainImage = true;
			}

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

	public void gotoCustomer() {
		Intent i = new Intent(this, PerbaikanActivity.class);
		startActivity(i);
		finish();
	}
}
