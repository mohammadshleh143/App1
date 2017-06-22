package com.mahkota_company.android.jadwal;

import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.TypeCustomer;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.SalesKanvasActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
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

@SuppressWarnings("deprecation")
public class DetailJadwalCustomerActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private ProgressDialog progressDialog;
	private ArrayList<Customer> customer_list = new ArrayList<Customer>();
	private Customer customer;
	private Jadwal jadwal;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvAlamatCustomer;
	private TextView tvWilayahCustomer;
	private TextView tvGpsCustomer;
	private TextView tvImageCustomer;
	private TextView tvTelpCustomer;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderAlamatCustomer;
	private TextView tvHeaderWilayahCustomer;
	private TextView tvHeaderGpsCustomer;
	private TextView tvHeaderImageCustomer;
	private TextView tvHeaderTelpCustomer;
	private Button mButtonCustomerDetailMaps;
	private Button mButtonCustomerDetailImage;
	private Button mButtonCustomerDetailCheckIn;
	private Button mButtonCustomerDetailCheckOut;
	private Button mButtonCustomerDetailSalesOrder;
	private Button mButtonCustomerDetailStockOnHand;
	private Button mButtonCustomerDetailRetur;
	private Button mButtonCustomerDetailDisplay;
	private EditText keterangan;
	private LocationManager locationManager;
	private double tempCheckInLatitude;
	private double tempCheckInLongitude;
	private static final String LOG_TAG = DetailJadwalCustomerActivity.class
			.getSimpleName();
	boolean isContainImage = true;
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
		setContentView(R.layout.activity_detail_jadwal_customer);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoJadwalCustomer();
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
		tvNamaCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_nama_customer);
		tvAlamatCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_alamat_customer);
		tvWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_wilayah_customer);
		tvGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_gps_location);
		tvImageCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_image);
		tvTelpCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_telp_customer);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_kode_customer);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_nama_customer);
		tvHeaderAlamatCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_alamat_customer);
		tvHeaderWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_wilayah_customer);
		tvHeaderGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_gps_location);
		tvHeaderImageCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_image);
		tvHeaderTelpCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_telp_customer);
		mButtonCustomerDetailMaps = (Button) findViewById(R.id.activity_customer_detail_btn_map);
		mButtonCustomerDetailImage = (Button) findViewById(R.id.activity_customer_detail_btn_image);
		mButtonCustomerDetailCheckIn = (Button) findViewById(R.id.activity_customer_detail_btn_check_in);
		mButtonCustomerDetailCheckOut = (Button) findViewById(R.id.activity_customer_detail_btn_check_out);
		mButtonCustomerDetailSalesOrder = (Button) findViewById(R.id.activity_customer_detail_btn_sales_order);
		mButtonCustomerDetailStockOnHand = (Button) findViewById(R.id.activity_customer_detail_btn_stock_on_hand);
		mButtonCustomerDetailRetur = (Button) findViewById(R.id.activity_customer_detail_btn_sales_kanvas);
		mButtonCustomerDetailDisplay = (Button) findViewById(R.id.activity_customer_detail_btn_sales_display);
		keterangan = (EditText) findViewById(R.id.activity_customer_detail_value_ket_customer);

		mButtonCustomerDetailSalesOrder
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						gotoSalesOrder();
					}
				});

		mButtonCustomerDetailStockOnHand
				.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						gotoStockOnHand();
					}
				});

		mButtonCustomerDetailRetur.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						gotoSalesRewtur();
					}
				});

		mButtonCustomerDetailDisplay.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						gotoSalesDisplay();
					}
				});

        //mButtonCustomerDetailMaps.setVisibility(View.INVISIBLE);

		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);
		tvAlamatCustomer.setTypeface(typefaceSmall);
		tvWilayahCustomer.setTypeface(typefaceSmall);
		tvGpsCustomer.setTypeface(typefaceSmall);
		tvImageCustomer.setTypeface(typefaceSmall);
		tvTelpCustomer.setTypeface(typefaceSmall);

		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderAlamatCustomer.setTypeface(typefaceSmall);
		tvHeaderWilayahCustomer.setTypeface(typefaceSmall);
		tvHeaderGpsCustomer.setTypeface(typefaceSmall);
		tvHeaderImageCustomer.setTypeface(typefaceSmall);
		tvHeaderTelpCustomer.setTypeface(typefaceSmall);
		mButtonCustomerDetailMaps
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailImage
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailCheckOut
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailCheckIn
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_kode_customer = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_KODE_CUSTOMER, null);
		String main_app_id_jadwal = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_ID_JADWAL, null);
		if (main_app_kode_customer != null && main_app_id_jadwal != null) {
			saveAppDataCustomerIdCustomer(main_app_kode_customer);
			showCustomerFromDB(main_app_kode_customer, main_app_id_jadwal);
		} else {
			gotoJadwalCustomer();
		}
		checkGPS();
	}

	public void gotoSalesOrder() {
		Intent i = new Intent(this, SalesOrderActivity.class);
		startActivity(i);
		finish();
	}

	public void gotoStockOnHand() {
		Intent i = new Intent(this, StockOnHandActivity.class);
		startActivity(i);
		finish();
	}

	public void gotoSalesRewtur() {
		Intent i = new Intent(this, ReturActivity.class);
		startActivity(i);
		finish();
	}

	public void gotoSalesDisplay() {
		Intent i = new Intent(this, DisplayProductActivity.class);
		startActivity(i);
		finish();
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
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private double distanceNew(double lat1, double lon1, double lat2, double lon2) {
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

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	private final OnClickListener mDetailCustomerButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_customer_detail_btn_map:
				gotoCustomerLocator();
				break;
			case R.id.activity_customer_detail_btn_image:
				if (!isContainImage) {
					showCustomDialog(getApplicationContext().getResources()
							.getString(R.string.app_customer_warning));
				} else {
					previewImageDialog();
				}
				break;
			case R.id.activity_customer_detail_btn_check_in:
				if (jadwal != null) {
					String curLatitude = String
							.valueOf((int) tempCheckInLatitude);
					String curLongitude = String
							.valueOf((int) tempCheckInLongitude);
					if (curLatitude.equalsIgnoreCase("0")
							|| curLongitude.equalsIgnoreCase("0")) {
						String msg = getApplicationContext().getResources()
								.getString(R.string.app_jadwal_no_gps_location);
						showCustomDialog(msg);
					} else {
						if (Integer.parseInt(jadwal.getStatus_update()) == 1) {
							double differentKm = distanceNew(
									tempCheckInLatitude, tempCheckInLongitude,
									latitude, longitude);
							int getMeter = (int) differentKm * 1000;
							String message = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_jadwal_detail_far_away_location_check_in);
							if (getMeter > 50) {
								showCustomDialog(message);
							} else {
                                final String date = "yyyy-MM-dd";
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										date);
								final String dateOutput = dateFormat
										.format(calendar.getTime());
								Calendar now = Calendar.getInstance();
								int hrs = now.get(Calendar.HOUR_OF_DAY);
								int min = now.get(Calendar.MINUTE);
								int sec = now.get(Calendar.SECOND);
								final String time = zero(hrs) + ":" + zero(min)
										+ ":" + zero(sec);
								String datetime = dateOutput + " " + time;
								Jadwal newJadwal = new Jadwal();
                                newJadwal.setId_jadwal(jadwal.getId_jadwal());
								newJadwal.setKode_jadwal(jadwal.getKode_jadwal());
								newJadwal.setAlamat(jadwal.getAlamat());
								newJadwal.setCheckin(datetime);
								newJadwal.setCheckout(jadwal.getCheckout());
								newJadwal.setDate(jadwal.getDate());
								newJadwal.setId_wilayah(jadwal.getId_wilayah());
								newJadwal.setKode_customer(jadwal
										.getKode_customer());
								newJadwal.setNama_toko(jadwal
										.getNama_toko());
								newJadwal.setStatus(jadwal.getStatus());
								newJadwal.setStatus_update("2");
								newJadwal.setUsername(jadwal.getUsername());
								databaseHandler.updateJadwal(
										jadwal.getId_jadwal(), newJadwal);
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.app_jadwal_success_check_in);
								showCustomDialogSaveSuccess(msg);

							}
						} else {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_jadwal_failed_check_in);
							showCustomDialog(msg);
						}
					}
				}
				break;
			case R.id.activity_customer_detail_btn_check_out:
				if (jadwal != null) {
					String curLatitude = String
							.valueOf((int) tempCheckInLatitude);
					String curLongitude = String
							.valueOf((int) tempCheckInLongitude);
					if (curLatitude.equalsIgnoreCase("0")
							|| curLongitude.equalsIgnoreCase("0")) {
						String msg = getApplicationContext().getResources()
								.getString(R.string.app_jadwal_no_gps_location);
						showCustomDialog(msg);
					} else {
						if (Integer.parseInt(jadwal.getStatus_update()) == 2) {
							if(keterangan.getText().toString().equals("") ||
									keterangan.getText().toString().equals(" ")){
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.app_jadwal_failed_check_out_need_keterangan);
								showCustomDialog(msg);
							}else{
								final String date = "yyyy-MM-dd";
								Calendar calendar = Calendar.getInstance();
								SimpleDateFormat dateFormat = new SimpleDateFormat(
										date);
								final String dateOutput = dateFormat
										.format(calendar.getTime());
								Calendar now = Calendar.getInstance();
								int hrs = now.get(Calendar.HOUR_OF_DAY);
								int min = now.get(Calendar.MINUTE);
								int sec = now.get(Calendar.SECOND);
								final String time = zero(hrs) + ":" + zero(min)
										+ ":" + zero(sec);
								String datetime = dateOutput + " " + time;
								Jadwal newJadwal = new Jadwal();
								newJadwal.setId_jadwal(jadwal.getId_jadwal());
								newJadwal.setKode_jadwal(jadwal.getKode_jadwal());
								newJadwal.setAlamat(jadwal.getAlamat());
								newJadwal.setCheckin(jadwal.getCheckin());
								newJadwal.setCheckout(datetime);
								newJadwal.setDate(jadwal.getDate());
								newJadwal.setId_wilayah(jadwal.getId_wilayah());
								newJadwal.setKode_customer(jadwal
										.getKode_customer());
								newJadwal.setNama_toko(jadwal.getNama_toko());
								newJadwal.setStatus(jadwal.getStatus());
								newJadwal.setStatus_update("3");
								newJadwal.setUsername(jadwal.getUsername());
								newJadwal.setKeterangan(keterangan.getText().toString());
								databaseHandler.updateJadwal(jadwal.getId_jadwal(),
										newJadwal);
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.app_jadwal_success_check_out);
								showCustomDialogSaveSuccess(msg);
							}

						} else {
							if (Integer.parseInt(jadwal.getStatus_update()) == 1) {
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.app_jadwal_failed_check_out_need_check_in);
								showCustomDialog(msg);
							} else {
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.app_jadwal_failed_check_out);
								showCustomDialog(msg);
							}

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
			imagesDialog.setContentView(DetailJadwalCustomerActivity.this
					.getLayoutInflater().inflate(R.layout.activity_popup_image,
							null));

			Gallery galleryImages = (Gallery) imagesDialog
					.findViewById(R.id.gallery1);
			galleryImages.setAdapter(new ImageAdapter(this, mListImages));

			imagesDialog.show();
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

	public void gotoCustomerLocator() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
		if (main_app_table_id != null) {
			saveAppDataCustomerIdCustomer(main_app_table_id);
			Intent intentActivity = new Intent(
					DetailJadwalCustomerActivity.this,
					DetailCustomerJadwalLocator.class);
			startActivity(intentActivity);
			//finish();
		} else {
			gotoJadwalCustomer();
		}

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

	public void showCustomerFromDB(
			String kode_customer_from_shared_preferenced,
			String main_app_id_jadwal) {
		customer_list.clear();
		customer = null;

		Customer customer_from_db = databaseHandler
				.getCustomer(kode_customer_from_shared_preferenced);
		jadwal = databaseHandler.getJadwal(Integer.parseInt(main_app_id_jadwal));
		if (jadwal != null) {
			if (customer_from_db != null) {
				customer = customer_from_db;

				customer_list.add(customer_from_db);

				//File dir = new File(CONFIG.getFolderPath() + "/"
				//		+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
				//		+ customer_from_db.getFoto_1());

				//File dir2 = new File(CONFIG.getFolderPath() + "/"
				//		+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
				//		+ customer_from_db.getFoto_2());

				//File dir3 = new File(CONFIG.getFolderPath() + "/"
				//		+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
				//		+ customer_from_db.getFoto_3());
				//if (!dir.exists() || !dir2.exists() || !dir3.exists()) {
				//	customer_list.add(customer_from_db);
				//}

				if (customer_list.size() > 0) {
					if (GlobalApp.checkInternetConnection(act)) {

						showDataCustomer();
						//processDownloadContentCustomer();
					} else {
						showDataCustomer();
						/*
						String message = act
								.getApplicationContext()
								.getResources()
								.getString(
										R.string.app_customer_processing_empty);
						showCustomDialog(message);
						tvKodeCustomer.setVisibility(View.GONE);
						tvNamaCustomer.setVisibility(View.GONE);
						tvAlamatCustomer.setVisibility(View.GONE);
						tvWilayahCustomer.setVisibility(View.GONE);
						tvTypeCustomer.setVisibility(View.GONE);
						tvGpsCustomer.setVisibility(View.GONE);
						tvImageCustomer.setVisibility(View.GONE);
						tvTelpCustomer.setVisibility(View.GONE);
						*/
					}
				} else {
					showDataCustomer();
				}
			} else {
				tvKodeCustomer.setVisibility(View.GONE);
				tvNamaCustomer.setVisibility(View.GONE);
				tvAlamatCustomer.setVisibility(View.GONE);
				tvWilayahCustomer.setVisibility(View.GONE);
				tvGpsCustomer.setVisibility(View.GONE);
				tvImageCustomer.setVisibility(View.GONE);
				tvTelpCustomer.setVisibility(View.GONE);
			}
		} else {
			gotoJadwalCustomer();
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
				map = null;//downloadImage(url.split("#")[0], url.split("#")[1]);
				rowItems.add(new RowItem(map));
			}
			return rowItems;
		}

		/*
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
		*/

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
		if (jadwal != null) {
			tvKodeCustomer.setVisibility(View.VISIBLE);
			tvNamaCustomer.setVisibility(View.VISIBLE);
			tvAlamatCustomer.setVisibility(View.VISIBLE);
			tvWilayahCustomer.setVisibility(View.VISIBLE);
			tvGpsCustomer.setVisibility(View.VISIBLE);
			tvImageCustomer.setVisibility(View.INVISIBLE);
			tvTelpCustomer.setVisibility(View.VISIBLE);

			tvKodeCustomer.setText(jadwal.getKode_customer());
			tvNamaCustomer.setText(jadwal.getNama_toko());
			tvAlamatCustomer.setText(customer.getAlamat());
			Wilayah wilayah = databaseHandler.getWilayah(customer
					.getId_wilayah());
			if (wilayah != null)
				tvWilayahCustomer.setText(wilayah.getNama_wilayah());
			tvGpsCustomer.setText(customer.getLats() + " / "
					+ customer.getLongs());
			tvTelpCustomer.setText(customer.getNo_telp());

			//boolean isGps = true;
			//if (customer.getLats().equalsIgnoreCase("null")
			//		|| customer.getLongs().equalsIgnoreCase("null")) {
			//	isGps = false;
			//} else if (customer.getLats().equalsIgnoreCase("")
			//		|| customer.getLongs().equalsIgnoreCase("")) {
			//	isGps = false;
			//} else if (customer.getLats().equalsIgnoreCase("0")
			//		|| customer.getLongs().equalsIgnoreCase("0")) {
			//	isGps = false;
			//} else if (customer.getLats().equalsIgnoreCase("0.0")
			//		|| customer.getLongs().equalsIgnoreCase("0.0")) {
			//	isGps = false;
			//} else {
			//	isGps = true;
			//}
			//mButtonCustomerDetailMaps.setEnabled(isGps);

			tempCheckInLatitude = Double.parseDouble(customer.getLats());
			tempCheckInLongitude = Double.parseDouble(customer.getLongs());

			//File dir1 = new File(CONFIG.getFolderPath() + "/"
			//		+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
			//		+ customer.getFoto_1());

			//File dir2 = new File(CONFIG.getFolderPath() + "/"
			//		+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
			//		+ customer.getFoto_2());

			//File dir3 = new File(CONFIG.getFolderPath() + "/"
			//		+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
			//		+ customer.getFoto_3());
			//if (!dir1.exists() && !dir2.exists() && !dir3.exists()) {
			//	isContainImage = false;
			//} else {
			//	isContainImage = true;
			//}

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

	public void gotoJadwalCustomer() {
		Intent i = new Intent(this, JadwalCustomerActivity.class);
		startActivity(i);
		finish();
	}
}
