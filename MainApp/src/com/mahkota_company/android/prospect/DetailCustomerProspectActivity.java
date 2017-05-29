package com.mahkota_company.android.prospect;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import android.app.Dialog;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView;

import com.mahkota_company.android.database.Cluster;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.TypeCustomer;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.SpinnerAdapter;
import com.mahkota_company.android.R;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class DetailCustomerProspectActivity extends FragmentActivity {
	int hour, minute, mYear,mMonth, mDay;
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	private EditText etTanggal_lahir;
	private String[] arrMonth = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};


	private Spinner spinnerCluster;
	private ArrayList<Cluster> clusterList;
	private ArrayList<String> clusterStringList;
	private int idCluster = 0;

	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private ProgressDialog progressDialog;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private EditText etNamaCustomer;
	private EditText etEmailCustomer;
	private EditText etAlamatCustomer;
	private TextView tvWilayahCustomer;
	private Spinner spinnerTypeCustomer;
	private ArrayList<TypeCustomer> typeCustomerList;
	private ArrayList<String> typeCustomerStringList;
	private int idTypeCustomer = 0;
	private TextView tvGpsCustomer;
	private TextView tvImage1Customer;
	private TextView tvImage2Customer;
	private TextView tvImage3Customer;
	private TextView tvttd1;
	private TextView tvttd2;
	private EditText etTelpCustomer;
	private EditText etno_ktp;
	private EditText etNama_bank;
	private EditText etNo_rekenig;
	private EditText etAtas_nama;
	private EditText etNpwp;
    private EditText etNama_pasar;
    private EditText etCluster;
    private EditText etTelp;
    private EditText etFax;
    private EditText etOmset;
    private EditText etCara_pembayaran;
    private EditText etPlafon_kredit;
    private EditText etTerm_kredit;

	private EditText etNama_istri;
	private EditText etNama_anak1;
	private EditText etNama_anak2;
	private EditText etNama_anak3;
	private EditText etKode_pos;
	private EditText etNama_toko;


	private Customer customer;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderEmailCustomer;
	private TextView tvHeaderAlamatCustomer;
	private TextView tvHeaderWilayahCustomer;
	private TextView tvHeaderTypeCustomer;
	private TextView tvHeaderGpsCustomer;
	private TextView tvHeaderImage1Customer;
	private TextView tvHeaderImage2Customer;
	private TextView tvHeaderImage3Customer;
	private TextView tvHeaderTelpCustomer;
	private Button mButtonCustomerDetailMap;
	private Button mButtonCustomerDetailImage1;
	private Button mButtonCustomerDetailImage2;
	private Button mButtonCustomerDetailImage3;
	private Button mButtonCustomerDetailPreview;
	private Button mButtonCustomerDetailSave;
	private LocationManager locationManager;
	private static final String LOG_TAG = DetailCustomerProspectActivity.class
			.getSimpleName();
	private double latitude; // latitude
	private double longitude; // longitude
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Location location; // location
	private String tempLatitude;
	private String tempLongitude;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_customer_prospect);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoCustomerProspect();
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
		etNamaCustomer = (EditText) findViewById(R.id.activity_customer_prospect_nama_customer_value);
		etEmailCustomer = (EditText) findViewById(R.id.activity_customer_prospect_email_customer_value);
		etAlamatCustomer = (EditText) findViewById(R.id.activity_customer_prospect_alamat_customer_value);
		tvWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_wilayah_customer);
		spinnerTypeCustomer = (Spinner) findViewById(R.id.activity_customer_prospect_type_customer_value);
		tvGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_gps_location);
		tvImage1Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image);
		tvImage2Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_2);
		tvImage3Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_3);
		etTelpCustomer = (EditText) findViewById(R.id.activity_customer_prospect_telp_customer_value);
		etno_ktp = (EditText) findViewById(R.id.activity_customer_prospect_no_ktp_value);
		etTanggal_lahir = (EditText) findViewById(R.id.activity_customer_prospect_tanggal_lahir_value);
		etNama_bank = (EditText) findViewById(R.id.activity_customer_prospect_nama_bank_value);
		etNo_rekenig = (EditText) findViewById(R.id.activity_customer_prospect_no_rekening_value);
		etAtas_nama = (EditText) findViewById(R.id.activity_customer_prospect_atas_nama_value);
		etNpwp = (EditText) findViewById(R.id.activity_customer_prospect_npwp_value);
        etNama_pasar = (EditText) findViewById(R.id.activity_customer_prospect_nama_pasar_value);
        //etCluster = (EditText) findViewById(R.id.activity_customer_prospect_cluster_value);
		spinnerCluster = (Spinner) findViewById(R.id.activity_customer_prospect_cluster);
        etTelp = (EditText) findViewById(R.id.activity_customer_prospect_telp_value);
        etFax = (EditText) findViewById(R.id.activity_customer_prospect_fax_value);
        etOmset = (EditText) findViewById(R.id.activity_customer_prospect_omset_value);
        etCara_pembayaran = (EditText) findViewById(R.id.activity_customer_prospect_cara_pembayaran_value);
        etPlafon_kredit = (EditText) findViewById(R.id.activity_customer_prospect_plafon_value);
        etTerm_kredit = (EditText) findViewById(R.id.activity_customer_prospect_term_value);
		tvttd1 = (TextView) findViewById(R.id.activity_customer_detail_ttd1);
		tvttd2 = (TextView) findViewById(R.id.activity_customer_detail_ttd2);

		etNama_istri = (EditText) findViewById(R.id.activity_customer_prospect_nama_istri_value);
		etNama_anak1 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak1_value);
		etNama_anak2 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak2_value);
		etNama_anak3 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak3_value);
		etKode_pos = (EditText) findViewById(R.id.activity_customer_prospect_kode_pos_value);
		etNama_toko = (EditText) findViewById(R.id.activity_customer_prospect_nama_toko_value);

		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_kode_customer);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_nama_customer);
		tvHeaderEmailCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_email_customer);
		tvHeaderAlamatCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_alamat_customer);
		tvHeaderWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_wilayah_customer);
		tvHeaderTypeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_type_customer);
		tvHeaderGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_gps_location);
		tvHeaderImage1Customer = (TextView) findViewById(R.id.activity_customer_detail_title_image);
		tvHeaderImage2Customer = (TextView) findViewById(R.id.activity_customer_detail_title_image_2);
		tvHeaderImage3Customer = (TextView) findViewById(R.id.activity_customer_detail_title_image_3);
		tvHeaderTelpCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_telp_customer);

		mButtonCustomerDetailMap = (Button) findViewById(R.id.activity_customer_detail_btn_map);
		mButtonCustomerDetailImage1 = (Button) findViewById(R.id.activity_customer_detail_btn_image);
		mButtonCustomerDetailImage2 = (Button) findViewById(R.id.activity_customer_detail_btn_image_2);
		mButtonCustomerDetailImage3 = (Button) findViewById(R.id.activity_customer_detail_btn_image_3);
		mButtonCustomerDetailPreview = (Button) findViewById(R.id.activity_customer_detail_btn_preview);
		mButtonCustomerDetailSave = (Button) findViewById(R.id.activity_customer_detail_btn_save);

		mButtonCustomerDetailImage1.setVisibility(View.INVISIBLE);
		mButtonCustomerDetailImage2.setVisibility(View.INVISIBLE);
		mButtonCustomerDetailImage3.setVisibility(View.INVISIBLE);

		//set list cluster
		clusterList = new ArrayList<Cluster>();
		clusterStringList = new ArrayList<String>();
		List<Cluster> dataCluster = databaseHandler
				.getAllCluster();
		for (Cluster cluster : dataCluster) {
			clusterList.add(cluster);
			clusterStringList.add(cluster.getNama_cluster());
		}

		SpinnerAdapter adapterCluster = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				clusterStringList);
		adapterCluster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCluster.setAdapter(adapterCluster);
		spinnerCluster
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
											   View view, int position, long id) {
						idCluster = clusterList.get(position)
								.getId_cluster();
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		idCluster = clusterList.get(0).getId_cluster();

		typeCustomerList = new ArrayList<TypeCustomer>();
		typeCustomerStringList = new ArrayList<String>();
		List<TypeCustomer> dataTypeCustomer = databaseHandler
				.getAllTypeCustomer();
		for (TypeCustomer typeCustomer : dataTypeCustomer) {
			typeCustomerList.add(typeCustomer);
			typeCustomerStringList.add(typeCustomer.getType_customer());
		}

		SpinnerAdapter adapterCustomer = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				typeCustomerStringList);
		adapterCustomer
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTypeCustomer.setAdapter(adapterCustomer);
		spinnerTypeCustomer
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						idTypeCustomer = typeCustomerList.get(position)
								.getId_type_customer();
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		idTypeCustomer = typeCustomerList.get(0).getId_type_customer();

		tvKodeCustomer.setTypeface(typefaceSmall);
		tvWilayahCustomer.setTypeface(typefaceSmall);
		tvGpsCustomer.setTypeface(typefaceSmall);
		tvImage1Customer.setTypeface(typefaceSmall);
		tvImage1Customer.setTypeface(typefaceSmall);
		tvImage2Customer.setTypeface(typefaceSmall);
		tvImage3Customer.setTypeface(typefaceSmall);
		tvttd1.setTypeface(typefaceSmall);
		tvttd2.setTypeface(typefaceSmall);

		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderEmailCustomer.setTypeface(typefaceSmall);
		tvHeaderAlamatCustomer.setTypeface(typefaceSmall);
		tvHeaderWilayahCustomer.setTypeface(typefaceSmall);
		tvHeaderTypeCustomer.setTypeface(typefaceSmall);
		tvHeaderGpsCustomer.setTypeface(typefaceSmall);
		tvHeaderImage1Customer.setTypeface(typefaceSmall);
		tvHeaderImage2Customer.setTypeface(typefaceSmall);
		tvHeaderImage3Customer.setTypeface(typefaceSmall);
		tvHeaderTelpCustomer.setTypeface(typefaceSmall);
		mButtonCustomerDetailImage1.setVisibility(View.INVISIBLE);
		mButtonCustomerDetailImage2.setVisibility(View.INVISIBLE);
		mButtonCustomerDetailImage3.setVisibility(View.INVISIBLE);
		mButtonCustomerDetailImage1
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailImage2
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailImage3
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailSave
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailPreview
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailMap
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
		if (main_app_id != null) {
			saveAppDataCustomerIdCustomer(main_app_id);
			showKodeCustomerFromDB(main_app_id);
		} else {
			gotoCustomerProspect();
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
							tvGpsCustomer.setText(String.valueOf(latitude)
									+ "/" + String.valueOf(longitude));

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
								gotoCustomerProspect();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private final OnClickListener mDetailCustomerButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_customer_detail_btn_map:
				gotoCustomerLocator();
				break;
			case R.id.activity_customer_detail_btn_preview:
				previewImageDialog();
				break;
			case R.id.activity_customer_detail_btn_save:
				if (customer != null) {
					if (etAlamatCustomer.getText().length() > 0
							&& etEmailCustomer.getText().length() > 0
							&& etno_ktp.getText().length() > 0
							&& etTanggal_lahir.getText().length() > 0
							&& etNama_bank.getText().length() > 0
							&& etNo_rekenig.getText().length() > 0
							&& etAtas_nama.getText().length() > 0
							&& etNpwp.getText().length() > 0
                            && idCluster != 0
                            && etTelp.getText().length() > 0
                            && etFax.getText().length() > 0
                            && etNama_pasar.getText().length() > 0
                            && etOmset.getText().length() > 0
                            && etCara_pembayaran.getText().length() > 0
                            && etPlafon_kredit.getText().length() > 0
                            && etTerm_kredit.getText().length() > 0
                            && etNama_istri.getText().length() > 0
                            && etNama_anak1.getText().length() > 0
                            && etNama_anak2.getText().length() > 0
                            && etNama_anak3.getText().length() > 0
							&& etKode_pos.getText().length() > 0

							&& etNamaCustomer.getText().length() > 0
							&& etTelpCustomer.getText().length() > 0) {
						String curLatitude = String.valueOf((int) latitude);
						String curLongitude = String.valueOf((int) longitude);
						if (curLatitude.equalsIgnoreCase("0")
								|| curLongitude.equalsIgnoreCase("0")) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_FAILED_GPS_DIALOG);
							showCustomDialog(msg);
						} else {
							saveGPSandProfile();
							//showConfirmationUpdateCustomerProspect();
						}
					} else {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_customer_prospect_save_failed_empty_box);
						showCustomDialog(msg);
					}
				} else {
					gotoCustomerProspect();
				}

				break;
			default:
				break;
			}
		}

	};

	public void gotoCustomerLocator() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
		if (main_app_table_id != null) {
			saveAppDataCustomerIdCustomer(main_app_table_id);
			Intent intentActivity = new Intent(
					DetailCustomerProspectActivity.this,
					CustomerProspectLocatorActivity.class);
			startActivity(intentActivity);
			finish();
		} else {
			gotoCustomerProspect();
		}

	}

	protected void showConfirmationUpdateCustomerProspect() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_UPDATE_GPS_PROSPECT_DIALOG);
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
								saveGPSandProfile();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NO_ONLY_DATA),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								saveOnlyProfile();

							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	protected void saveGPSandProfile() {
		final String date = "yyyy-MM-dd";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				date);
		final String checkDate = dateFormat.format(calendar
				.getTime());

		Customer newCustomer = new Customer();
		newCustomer.setId_customer(customer.getId_customer());
		newCustomer.setAlamat(etAlamatCustomer.getText().toString());
		newCustomer.setBlokir(customer.getBlokir());
		newCustomer.setDate(checkDate);
		newCustomer.setEmail(etEmailCustomer.getText().toString());
		newCustomer.setFoto_1(customer.getFoto_1());
		newCustomer.setFoto_2(customer.getFoto_2());
		newCustomer.setFoto_3(customer.getFoto_3());
		newCustomer.setId_type_customer(idTypeCustomer);
		newCustomer.setId_wilayah(customer.getId_wilayah());
		newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
		newCustomer.setLats(customer.getLats());
		newCustomer.setLongs(customer.getLongs());
		newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
		newCustomer.setNo_telp(etTelpCustomer.getText().toString());
		newCustomer.setStatus_update(customer.getStatus_update());
		newCustomer.setId_staff(customer.getId_staff());
		newCustomer.setNo_ktp(etno_ktp.getText().toString());
		newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
		newCustomer.setNama_bank(etNama_bank.getText().toString());
		newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
		newCustomer.setAtas_nama(etAtas_nama.getText().toString());
		newCustomer.setNpwp(etNpwp.getText().toString());
        newCustomer.setNama_pasar(etNama_pasar.getText().toString());
        newCustomer.setId_cluster(idCluster);
        newCustomer.setTelp(etTelp.getText().toString());
        newCustomer.setFax(etFax.getText().toString());
        newCustomer.setOmset(etOmset.getText().toString());
        newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
        newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
        newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
		newCustomer.setNama_istri(etNama_istri.getText().toString());
		newCustomer.setNama_anak1(etNama_anak1.getText().toString());
		newCustomer.setNama_anak2(etNama_anak2.getText().toString());
		newCustomer.setNama_anak3(etNama_anak3.getText().toString());
		newCustomer.setKode_pos(etKode_pos.getText().toString());
		newCustomer.setId_depo(customer.getId_depo());
		newCustomer.setIsactive(customer.getIsactive());
		newCustomer.setDescription(customer.getDescription());
		newCustomer.setNama_toko(customer.getNama_toko());
		newCustomer.setTtd1(customer.getTtd1());
		newCustomer.setTtd2(customer.getTtd2());

		databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
		String msg = getApplicationContext().getResources().getString(
				R.string.app_customer_prospect_update_success);
		showCustomDialogSaveSuccess(msg);
	}

	protected void saveOnlyProfile() {
		Customer newCustomer = new Customer();
		newCustomer.setId_customer(customer.getId_customer());
		newCustomer.setAlamat(etAlamatCustomer.getText().toString());
		newCustomer.setBlokir(customer.getBlokir());
		newCustomer.setDate(customer.getDate());
		newCustomer.setEmail(etEmailCustomer.getText().toString());
		newCustomer.setFoto_1(customer.getFoto_1());
		newCustomer.setFoto_2(customer.getFoto_2());
		newCustomer.setFoto_3(customer.getFoto_3());
		newCustomer.setId_type_customer(idTypeCustomer);
		newCustomer.setId_wilayah(customer.getId_wilayah());
		newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
		newCustomer.setLats(customer.getLats());
		newCustomer.setLongs(customer.getLongs());
		newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
		newCustomer.setNo_telp(etTelpCustomer.getText().toString());
		newCustomer.setStatus_update(customer.getStatus_update());
		newCustomer.setId_staff(customer.getId_staff());
		newCustomer.setNo_ktp(customer.getNo_ktp());
		newCustomer.setTanggal_lahir(customer.getTanggal_lahir());
		newCustomer.setNama_bank(customer.getNama_bank());
		newCustomer.setNo_rekening(customer.getNo_rekening());
		newCustomer.setAtas_nama(customer.getAtas_nama());
		newCustomer.setNpwp(customer.getNpwp());
        newCustomer.setNama_pasar(etNama_pasar.getText().toString());
        newCustomer.setId_cluster(idCluster);
        newCustomer.setTelp(customer.getTelp());
        newCustomer.setFax(customer.getFax());
        newCustomer.setOmset(customer.getOmset());
        newCustomer.setCara_pembayaran(customer.getCara_pembayaran());
        newCustomer.setPlafon_kredit(customer.getPlafon_kredit());
        newCustomer.setTerm_kredit(customer.getTerm_kredit());
		newCustomer.setNama_istri(customer.getNama_istri());
		newCustomer.setNama_anak1(customer.getNama_anak1());
		newCustomer.setNama_anak2(customer.getNama_anak2());
		newCustomer.setNama_anak3(customer.getNama_anak3());
		newCustomer.setKode_pos(customer.getKode_pos());
		newCustomer.setId_depo(customer.getId_depo());
		newCustomer.setIsactive(customer.getIsactive());
		newCustomer.setDescription(customer.getDescription());
		newCustomer.setNama_toko(customer.getNama_toko());
		newCustomer.setTtd1(customer.getTtd1());
		newCustomer.setTtd2(customer.getTtd2());

		databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
		String msg = getApplicationContext().getResources().getString(
				R.string.app_customer_prospect_update_success);
		showCustomDialogSaveSuccess(msg);
	}

	private void previewImageDialog() {
		if (customer != null) {
			List<File> mListImages = new ArrayList<File>();
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/"
					+ customer.getFoto_1());
			if (dir.isFile()) {
				mListImages.add(dir);
			}
			File dir2 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/"
					+ customer.getFoto_2());
			if (dir2.isFile()) {
				mListImages.add(dir2);
			}
			File dir3 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/"
					+ customer.getFoto_3());
			if (dir3.isFile()) {
				mListImages.add(dir3);
			}
			final Dialog imagesDialog = new Dialog(act);
			imagesDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
			imagesDialog.setContentView(DetailCustomerProspectActivity.this
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
		// private int itemBackground;
		private List<File> listFile;

		public ImageAdapter(Context context, List<File> listFile) {
			this.context = context;
			this.listFile = listFile;
			// TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
			// itemBackground = a.getResourceId(
			// R.styleable.MyGallery_android_galleryItemBackground, 0);
			// a.recycle();
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
			// imageView.setBackgroundResource(itemBackground);
			return imageView;
		}
	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	public void showKodeCustomerFromDB(String customerId) {
		customer = databaseHandler.getCustomer(Integer.parseInt(customerId));
		if (customer != null) {
			tvKodeCustomer.setText(customer.getKode_customer());
			Wilayah wilayah = databaseHandler.getWilayah(customer
					.getId_wilayah());
			tvWilayahCustomer.setText(wilayah.getNama_wilayah());
			etNamaCustomer.setText(customer.getNama_lengkap());
			etEmailCustomer.setText(customer.getEmail());
			etAlamatCustomer.setText(customer.getAlamat());
			tvImage1Customer.setText(customer.getFoto_1());
			tvImage2Customer.setText(customer.getFoto_2() != null ? String.valueOf(customer.getFoto_2()) : "");
			tvImage3Customer.setText(customer.getFoto_3() != null ? String.valueOf(customer.getFoto_3()) : "");
			etTelpCustomer.setText(customer.getNo_telp());
			etno_ktp.setText(customer.getNo_ktp());
			etTanggal_lahir.setText(customer.getTanggal_lahir());
			etNama_bank.setText(customer.getNama_bank());
            etNo_rekenig.setText(customer.getNo_rekening());
            etAtas_nama.setText(customer.getAtas_nama());
            etNpwp.setText(customer.getNpwp());
            etNama_pasar.setText(customer.getNama_pasar());
            //etCluster.setText(customer.getId_cluster());
            etTelp.setText(customer.getTelp());
            etFax.setText(customer.getFax());
            etOmset.setText(customer.getOmset());
            etCara_pembayaran.setText(customer.getCara_pembayaran());
            etPlafon_kredit.setText(customer.getPlafon_kredit());
            etTerm_kredit.setText(customer.getTerm_kredit());
			etNama_istri.setText(customer.getNama_istri());
			etNama_anak1.setText(customer.getNama_anak1());
			etNama_anak2.setText(customer.getNama_anak2());
			etNama_anak3.setText(customer.getNama_anak3());
			etKode_pos.setText(customer.getKode_pos());
			etNama_toko.setText(customer.getNama_toko());
			tvttd1.setText(customer.getTtd1() != null ? String.valueOf(customer.getTtd1()) : "");
			tvttd2.setText(customer.getTtd2() != null ? String.valueOf(customer.getTtd2()) : "");

			tempLatitude = customer.getLats();
			tempLongitude = customer.getLongs();

			List<TypeCustomer> dataTypeCustomer = databaseHandler
					.getAllTypeCustomer();
			int index = 0;
			for (TypeCustomer typeCustomer : dataTypeCustomer) {
				if (typeCustomer.getId_type_customer() == customer
						.getId_type_customer())
					break;
				index += 1;
			}
			spinnerTypeCustomer.setSelection(index);
			idTypeCustomer = typeCustomerList.get(index).getId_type_customer();

			List<Cluster> dataCluster = databaseHandler
					.getAllCluster();
			int index1 = 0;
			for (Cluster cluster : dataCluster) {
				if (cluster.getId_cluster() == customer
						.getId_cluster())
					break;
				index1 += 1;
			}
			spinnerCluster.setSelection(index1);
			idCluster = clusterList.get(index1).getId_cluster();
		} else {
			gotoCustomerProspect();
		}

	}

	public void saveAppDataCustomerIdCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER,
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

	public void gotoCustomerProspect() {
		Intent i = new Intent(this, CustomerProspectActivity.class);
		startActivity(i);
		finish();
	}
}
