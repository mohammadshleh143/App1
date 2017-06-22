package com.mahkota_company.android.merchandise;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.R;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.supervisor.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.StaffTemp;
import com.mahkota_company.android.database.Tracking;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.InventoryActivity;
import com.mahkota_company.android.inventory.RequestActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

@SuppressWarnings("deprecation")
public class LocatorStaffActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ArrayList<Customer> customer_list = new ArrayList<Customer>();
	private ArrayList<Tracking> tracking_list = new ArrayList<Tracking>();
	private Typeface typefaceSmall;
	private TextView tvNamaWilayah;
	private Spinner spinnerWilayah;
	public ArrayList<Wilayah> wilayahList;
	public ArrayList<String> wilayahStringList;
	public HashMap<Integer, Customer> mapWilayahList = new HashMap<Integer, Customer>();
	private static final String LOG_TAG = LocatorStaffActivity.class.getSimpleName();
	private GoogleMap googleMap;
	private double lat;// = location.getLatitude();
	private double lng;// = location.getLongitude();
	private LinearLayout layoutWilayah;
	private LinearLayout layoutStaff;
	private LinearLayout layoutFilter;
	private RadioGroup radioChoiceGroup;
	private RadioButton radioChoiceButton;
	private RadioButton radioChoiceCustomer;
	private RadioButton radioChoiceStaff;
	private TextView tvNamaStaff;
	private TextView tvFilterTanggal;
	private ProgressDialog progressDialog;
	private String message;
	private String response_data;
	private Handler handler = new Handler();
	private Spinner spinnerStaffTemp;
	public ArrayList<StaffTemp> staffList;
	public ArrayList<String> staffStringList;
	private String kodeStaff;
	private Button mButtonSearchStaff;
	private Button mButtonLocatorSearchByDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_locator);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvNamaWilayah = (TextView) findViewById(R.id.activity_tv_wilayah);
		tvNamaWilayah.setTypeface(typefaceSmall);

		act = this;
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_locator_processing_staff));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(11);
		spinnerWilayah = (Spinner) findViewById(R.id.activity_locator_wilayah);
		spinnerStaffTemp = (Spinner) findViewById(R.id.activity_locator_staff);
		layoutWilayah = (LinearLayout) findViewById(R.id.activity_wilayah);
		layoutStaff = (LinearLayout) findViewById(R.id.activity_staff);
		layoutFilter = (LinearLayout) findViewById(R.id.activity_date_filter);

		mButtonSearchStaff = (Button) findViewById(R.id.activity_locator_search);

		radioChoiceGroup = (RadioGroup) findViewById(R.id.radioChoice);
		radioChoiceCustomer = (RadioButton) findViewById(R.id.activity_locator_customer);
		radioChoiceStaff = (RadioButton) findViewById(R.id.activity_locator_kode_staff);
		radioChoiceCustomer.setTypeface(typefaceSmall);
		radioChoiceStaff.setTypeface(typefaceSmall);

		tvNamaStaff = (TextView) findViewById(R.id.activity_tv_staff);
		tvNamaStaff.setTypeface(typefaceSmall);

		tvFilterTanggal = (TextView) findViewById(R.id.activity_tv_date_filter);
		tvFilterTanggal.setTypeface(typefaceSmall);
		mButtonLocatorSearchByDate = (Button) findViewById(R.id.activity_button_filter_date);
		initilizeMap();
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_id_wilayah = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH, null);
		String main_app_staff_level = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_LEVEL, null);
		int levelStaff = Integer.parseInt(main_app_staff_level);
		radioChoiceGroup.setVisibility(View.GONE);
		layoutWilayah.setVisibility(View.GONE);
		layoutStaff.setVisibility(View.GONE);
		layoutFilter.setVisibility(View.GONE);
		if (levelStaff == 4) {
			if (GlobalApp.checkInternetConnection(act)) {
				showCustomerLocator(Integer.parseInt(main_app_staff_id_wilayah));
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
		} else if (levelStaff == 3) {
			updateSpinnerWilayahCustomer();
		} else {
			radioChoiceGroup.setVisibility(View.VISIBLE);
			updateSpinnerWilayahCustomer();
			int countStaffTemp = databaseHandler.getCountStaffTemp();
			if (countStaffTemp == 0) {
				if (GlobalApp.checkInternetConnection(act)) {
					new DownloadDataStaff().execute();
				} else {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_locator_processing_staff_empty);
					showCustomDialog(message);
				}
			} else {
				updateContentRefreshStaff();
			}
		}

		radioChoiceGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						radioChoiceButton = (RadioButton) findViewById(checkedId);
						if (radioChoiceButton
								.getText()
								.toString()
								.equalsIgnoreCase(
										getApplicationContext()
												.getResources()
												.getString(
														R.string.app_locator_radio_lokasi_staff))) {
							layoutWilayah.setVisibility(View.GONE);
							layoutStaff.setVisibility(View.VISIBLE);
							layoutFilter.setVisibility(View.VISIBLE);
							int checkStaffData = databaseHandler
									.getCountStaffTemp();
							if (checkStaffData == 0) {
								String msg = getApplicationContext()
										.getResources()
										.getString(
												R.string.app_locator_failed_no_staff_data);
								showCustomDialog(msg);
							}
						} else {
							layoutWilayah.setVisibility(View.GONE);
							layoutStaff.setVisibility(View.VISIBLE);
							layoutFilter.setVisibility(View.GONE);
						}
					}
				});
		mButtonSearchStaff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (kodeStaff != null) {
					new DownloadDataTracking().execute();
				}
			}
		});
		mButtonLocatorSearchByDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (tracking_list.size() > 0) {
					showDatePicker();
				} else {
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_locator_processing_staff_failed_empty_download_data_tracking_staff);
					showCustomDialog(msg);
				}
			}
		});

	}

	public void showDatePicker() {
		Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(LocatorStaffActivity.this,
				new mDateSetListener(), mYear, mMonth, mDay);
		dialog.show();
	}

	class mDateSetListener implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			String tempMont;
			if (monthOfYear + 1 < 10) {
				tempMont = "0" + String.valueOf(monthOfYear + 1);
			} else {
				tempMont = String.valueOf(monthOfYear + 1);
			}
			String tempDay;
			if (dayOfMonth < 10) {
				tempDay = "0" + String.valueOf(dayOfMonth);
			} else {
				tempDay = String.valueOf(dayOfMonth);
			}
			String dateSchedule = String.valueOf(year) + "-" + tempMont + "-"
					+ tempDay;
			// showCustomDialog(dateSchedule);
			showStaffLocatorByDate(dateSchedule);

		}
	}

	public void showStaffLocatorByDate(String dateSchedule) {

		googleMap.clear();
		// MarkerOptions options = new MarkerOptions();
		tracking_list.clear();
		tracking_list = databaseHandler.getAllTracking();

		if (tracking_list.size() == 0) {
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			String msg = getApplicationContext().getResources().getString(
					R.string.app_locator_no_staff_on_tracking)
					+ " " + kodeStaff;
			showCustomDialog(msg);
		} else {

			for (int i = 0; i < tracking_list.size(); i++) {
				if (tracking_list.get(i).getDate()
						.equalsIgnoreCase(dateSchedule)) {
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(Double.parseDouble(tracking_list
									.get(i).getLats()), Double
									.parseDouble(tracking_list.get(i)
											.getLongs()))).zoom(13).build();
					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				}

			}

			addMarkersStaffAll(dateSchedule);
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

	private class DownloadDataTracking extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_locator_processing_tracking));
			progressDialog.show();
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
			String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_TRACKING;
			download_data_url = download_data_url + "?kode_staff=" + kodeStaff;
			Log.d(LOG_TAG, "url=" + download_data_url);
			HttpResponse response = getDownloadData(download_data_url);
			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_data = EntityUtils.toString(response.getEntity());

					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_table_data = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_TABLE_TRACKING, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataTrackingSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableTracking();
							saveAppDataTrackingSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableTracking();
						saveAppDataTrackingSameData(act.getApplicationContext()
								.getResources()
								.getString(R.string.app_value_false));
					}
				} catch (ParseException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} catch (IOException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				if (response_data.equalsIgnoreCase("error")) {
					message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_locator_processing_staff_tracking_failed_no_data);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} else {
					saveAppDataTracking(response_data);
					extractDataTracking();
					message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_locator_processing_staff_tracking_success);
					showCustomDialogDownloadTrackingSuccess(message);
				}
			} else {
				message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_locator_processing_staff_tracking_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}

	}

	private class DownloadDataStaff extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_locator_processing_staff));
			progressDialog.show();
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
			String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_STAFF;
			HttpResponse response = getDownloadData(download_data_url);
			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_URL_NOT_FOUND);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_data = EntityUtils.toString(response.getEntity());

					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_table_data = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_TABLE_STAFF, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataStaffSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableStaffTemp();
							saveAppDataStaffSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableStaffTemp();
						saveAppDataStaffSameData(act.getApplicationContext()
								.getResources()
								.getString(R.string.app_value_false));
					}
				} catch (ParseException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} catch (IOException e) {
					message = e.toString();
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataStaff(response_data);
				extractDataStaffTemp();
				message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_locator_processing_staff_success);
				showCustomDialogDownloadSuccess(message);
			} else {
				message = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.app_locator_processing_staff_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}

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
								updateContentRefreshStaff();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void showCustomDialogDownloadTrackingSuccess(String msg) {
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
								showStaffLocator();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void updateContentRefreshStaff() {
		staffList = new ArrayList<StaffTemp>();
		staffStringList = new ArrayList<String>();
		ArrayList<StaffTemp> staff_from_db = databaseHandler.getAllStaffTemp();
		if (staff_from_db.size() > 0) {
			for (int i = 0; i < staff_from_db.size(); i++) {
				int id_staff = staff_from_db.get(i).getId_staff();
				String nama_lengkap = staff_from_db.get(i).getNama_lengkap();
				String username = staff_from_db.get(i).getUsername();
				String notelp = staff_from_db.get(i).getNotelp();
				String password = staff_from_db.get(i).getPassword();
				int level = staff_from_db.get(i).getLevel();
				String id_branch = staff_from_db.get(i).getId_branch();
				String id_type_customer = staff_from_db.get(i)
						.getId_type_customer();
				int id_depo = staff_from_db.get(i).getId_depo();

				StaffTemp staffTemp = new StaffTemp();
				staffTemp.setId_staff(id_staff);
				staffTemp.setNama_lengkap(nama_lengkap);
				staffTemp.setUsername(username);
				staffTemp.setNotelp(notelp);
				staffTemp.setPassword(password);
				staffTemp.setLevel(level);
				staffTemp.setId_branch(id_branch);
				staffTemp.setId_type_customer(id_type_customer);
				staffTemp.setId_depo(id_depo);
				if (staffTemp.getLevel() > 2)
					staffList.add(staffTemp);
			}

			spinnerStaffTemp.setBackgroundColor(getResources().getColor(
					R.color.black));

			for (StaffTemp staffTemp : staffList)
				staffStringList.add(staffTemp.getUsername());
			kodeStaff = staffList.get(0).getUsername();

			ArrayAdapter<String> adapterLocator = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item, staffStringList);

			adapterLocator
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinnerStaffTemp.setAdapter(adapterLocator);
			spinnerStaffTemp
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							kodeStaff = staffList.get(position).getUsername();

						}

						public void onNothingSelected(AdapterView<?> parent) {
						}
					});

		} else {
			String msg = getApplicationContext().getResources().getString(
					R.string.app_locator_processing_staff_failed_empty);
			showCustomDialog(msg);
		}

	}

	public void extractDataStaffTemp() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_STAFF_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_STAFF, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("staff");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_staff = oResponsealue.isNull("id_staff") ? null
							: oResponsealue.getString("id_staff");
					String nama_lengkap = oResponsealue.isNull("nama_lengkap") ? null
							: oResponsealue.getString("nama_lengkap");
					String username = oResponsealue.isNull("username") ? null
							: oResponsealue.getString("username");
					String notelp = oResponsealue.isNull("no_telp") ? null
							: oResponsealue.getString("no_telp");
					String password = oResponsealue.isNull("password") ? null
							: oResponsealue.getString("password");
					String level = oResponsealue.isNull("level") ? null
							: oResponsealue.getString("level");
					String id_branch = oResponsealue.isNull("id_branch") ? null
							: oResponsealue.getString("id_branch");
					String id_type_customer = oResponsealue
							.isNull("id_type_customer") ? null : oResponsealue
							.getString("id_type_customer");
					String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
							: oResponsealue.getString("id_wilayah");

					Log.d(LOG_TAG, "id_staff:" + id_staff);
					Log.d(LOG_TAG, "nama_lengkap:" + nama_lengkap);
					Log.d(LOG_TAG, "username:" + username);
					Log.d(LOG_TAG, "notelp:" + notelp);
					Log.d(LOG_TAG, "password:" + password);
					Log.d(LOG_TAG, "level:" + level);
					Log.d(LOG_TAG, "id_branch:" + id_branch);
					Log.d(LOG_TAG, "id_wilayah:" + id_wilayah);
					Log.d(LOG_TAG, "id_type_customer:" + id_type_customer);
					databaseHandler.add_Staff_Temp(new StaffTemp(Integer
							.parseInt(id_staff), nama_lengkap, username,
							notelp, password, Integer.parseInt(level),
							id_branch, id_type_customer, Integer
									.parseInt(id_wilayah)));
				}
			} catch (JSONException e) {
				final String message = e.toString();
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});

			}
		}
	}

	public void extractDataTracking() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TRACKING_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TRACKING, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("tracking");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_locator = oResponsealue.isNull("id_locator") ? null
							: oResponsealue.getString("id_locator");
					String username = oResponsealue.isNull("username") ? null
							: oResponsealue.getString("username");
					String nama_lengkap = oResponsealue.isNull("nama_lengkap") ? null
							: oResponsealue.getString("nama_lengkap");
					String level = oResponsealue.isNull("level") ? null
							: oResponsealue.getString("level");
					String lats = oResponsealue.isNull("lats") ? null
							: oResponsealue.getString("lats");
					String longs = oResponsealue.isNull("longs") ? null
							: oResponsealue.getString("longs");
					String address = oResponsealue.isNull("address") ? null
							: oResponsealue.getString("address");
					String imei = oResponsealue.isNull("imei") ? null
							: oResponsealue.getString("imei");
					String mcc = oResponsealue.isNull("mcc") ? null
							: oResponsealue.getString("mcc");
					String mnc = oResponsealue.isNull("mnc") ? null
							: oResponsealue.getString("mnc");
					String date = oResponsealue.isNull("date") ? null
							: oResponsealue.getString("date");
					String time = oResponsealue.isNull("mcc") ? null
							: oResponsealue.getString("time");
					Log.d(LOG_TAG, "id_locator:" + id_locator);
					Log.d(LOG_TAG, "nama_lengkap:" + nama_lengkap);
					Log.d(LOG_TAG, "username:" + username);
					Log.d(LOG_TAG, "lats:" + lats);
					Log.d(LOG_TAG, "longs:" + longs);
					Log.d(LOG_TAG, "level:" + level);
					Log.d(LOG_TAG, "address:" + address);
					Log.d(LOG_TAG, "imei:" + imei);
					Log.d(LOG_TAG, "mcc:" + mcc);
					Log.d(LOG_TAG, "mnc:" + mnc);
					Log.d(LOG_TAG, "date:" + date);
					Log.d(LOG_TAG, "time:" + time);
					databaseHandler.add_Tracking(new Tracking(Integer
							.parseInt(id_locator), username, nama_lengkap,
							Integer.parseInt(level), lats, longs, address,
							imei, mcc, mnc, date, time));
				}
			} catch (JSONException e) {
				final String message = e.toString();
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});

			}
		}
	}

	public void saveAppDataStaff(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_STAFF, responsedata);
		editor.commit();
	}

	public void saveAppDataStaffSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_STAFF_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTracking(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_TRACKING, responsedata);
		editor.commit();
	}

	public void saveAppDataTrackingSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_TRACKING_SAME_DATA,
				responsedata);
		editor.commit();
	}

	private void updateSpinnerWilayahCustomer() {
		layoutWilayah.setVisibility(View.VISIBLE);
		spinnerWilayah.setBackgroundColor(getResources()
				.getColor(R.color.black));
		wilayahList = new ArrayList<Wilayah>();
		wilayahStringList = new ArrayList<String>();
		customer_list = databaseHandler.getAllCustomerActive();
		for (Customer customer : customer_list) {
			mapWilayahList.put(customer.getId_wilayah(), customer);
		}

		for (Customer customer : mapWilayahList.values()) {
			Wilayah wilayah = databaseHandler.getWilayah(customer
					.getId_wilayah());
			wilayahList.add(wilayah);
			wilayahStringList.add(wilayah.getNama_wilayah());
		}

		ArrayAdapter<String> adapterLocator = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, wilayahStringList);

		adapterLocator
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerWilayah.setAdapter(adapterLocator);
		spinnerWilayah
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (GlobalApp.checkInternetConnection(act)) {
							showCustomerLocator(wilayahList.get(position)
									.getId_wilayah());
						} else {
							String message = act
									.getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
							showCustomDialog(message);
						}

					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		if (GlobalApp.checkInternetConnection(act)) {
			showCustomerLocator(wilayahList.get(0).getId_wilayah());
		} else {
			String message = act
					.getApplicationContext()
					.getResources()
					.getString(R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
			showCustomDialog(message);
		}
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showCustomerLocator(int id_wilayah) {

		googleMap.clear();
		// MarkerOptions options = new MarkerOptions();
		customer_list.clear();
		customer_list = databaseHandler.getAllCustomerActive(id_wilayah);

		if (customer_list.size() == 0) {
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			String msg = getApplicationContext().getResources().getString(
					R.string.app_locator_no_customer_on_wilayah);
			showCustomDialog(msg);
		} else {

			for (int i = 0; i < customer_list.size(); i++) {

				// options.position(
				// new LatLng(Double.parseDouble(customer_list.get(i)
				// .getLats()), Double.parseDouble(customer_list
				// .get(i).getLongs()))).title(
				// customer_list.get(i).getKode_customer() + " "
				// + customer_list.get(i).getNama_lengkap());
				//
				// googleMap.addMarker(options);
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(Double.parseDouble(customer_list
								.get(i).getLats()), Double
								.parseDouble(customer_list.get(i).getLongs())))
						.zoom(13).build();
				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

			}

			addMarkersCustomerAll();
		}

	}

	public void showStaffLocator() {

		googleMap.clear();
		// MarkerOptions options = new MarkerOptions();
		tracking_list.clear();
		tracking_list = databaseHandler.getAllTracking();

		if (tracking_list.size() == 0) {
			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(new LatLng(lat, lng)).zoom(15).build();

			googleMap.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));
			String msg = getApplicationContext().getResources().getString(
					R.string.app_locator_no_staff_on_tracking)
					+ " " + kodeStaff;
			showCustomDialog(msg);
		} else {

			for (int i = 0; i < tracking_list.size(); i++) {

				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(Double.parseDouble(tracking_list
								.get(i).getLats()), Double
								.parseDouble(tracking_list.get(i).getLongs())))
						.zoom(13).build();
				googleMap.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));

			}

			addMarkersStaffAll();
		}

	}

	private void addMarkersCustomerAll() {
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_map);
		if (googleMap != null) {
			for (int i = 0; i < customer_list.size(); i++) {

				googleMap.addMarker(new MarkerOptions()
						.position(
								new LatLng(Double.parseDouble(customer_list
										.get(i).getLats()), Double
										.parseDouble(customer_list.get(i)
												.getLongs())))
						.icon(icon)
						.title(customer_list.get(i).getNama_lengkap())
						.snippet(
								"Kode Customer :"
										+ customer_list.get(i)
												.getKode_customer() + "\n"
										+ "Alamat :"
										+ customer_list.get(i).getAlamat()
										+ "\n" + "Email :"
										+ customer_list.get(i).getEmail()
										+ "\n" + "Telepon :"
										+ customer_list.get(i).getNo_telp()
										+ "\n"));
				googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

							@Override
							public View getInfoWindow(Marker arg0) {
								return null;
							}

							@Override
							public View getInfoContents(Marker marker) {

								LinearLayout info = new LinearLayout(act);
								info.setOrientation(LinearLayout.VERTICAL);

								TextView title = new TextView(act);
								title.setTextColor(Color.BLACK);
								title.setGravity(Gravity.CENTER);
								title.setTypeface(typefaceSmall);
								title.setText(marker.getTitle());

								TextView snippet = new TextView(act);
								snippet.setTextColor(Color.GRAY);
								snippet.setText(marker.getSnippet());

								info.addView(title);
								info.addView(snippet);

								return info;
							}
						});
			}
		}

	}

	private String getOperator(int kodeOperator) {
		String msg = getApplicationContext().getResources().getString(
				R.string.app_locator_provider_1);
		switch (kodeOperator) {
		case 0:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_1);
			break;
		case 1:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_2);
			break;
		case 3:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_3);
			break;
		case 7:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_4);
			break;
		case 8:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_5);
			break;
		case 9:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_6);
			break;
		case 10:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_7);
			break;
		case 11:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_8);
			break;
		case 20:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_9);
			break;
		case 21:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_10);
			break;
		case 27:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_11);
			break;
		case 28:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_12);
			break;
		case 89:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_13);
			break;
		case 99:
			msg = getApplicationContext().getResources().getString(
					R.string.app_locator_provider_14);
			break;
		}
		return msg;

	}

	private void addMarkersStaffAll() {
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_map);
		if (googleMap != null) {
			for (int i = 0; i < tracking_list.size(); i++) {
				String number = tracking_list.get(i).getMnc();
				if (number.startsWith("0"))
					number = number.substring(1);
				number = number.replace(" ", "");
				String operator = getOperator(Integer.parseInt(number));
				googleMap
						.addMarker(new MarkerOptions()
								.position(
										new LatLng(Double
												.parseDouble(tracking_list.get(
														i).getLats()), Double
												.parseDouble(tracking_list.get(
														i).getLongs())))
								.icon(icon)
								.title(tracking_list.get(i).getNama_lengkap())
								.snippet(
										"Kode Customer :"
												+ tracking_list.get(i)
														.getUsername()
												+ "\n"
												+ "Address :"
												+ tracking_list.get(i)
														.getAddress()
												+ "\n"
												+ "Operator :"
												+ operator
												+ "\n"
												+ "IMEI :"
												+ tracking_list.get(i)
														.getImei()
												+ "\n"
												+ "Time :"
												+ tracking_list.get(i)
														.getDate()
												+ " "
												+ tracking_list.get(i)
														.getTime() + "\n"));
				googleMap
						.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

							@Override
							public View getInfoWindow(Marker arg0) {
								return null;
							}

							@Override
							public View getInfoContents(Marker marker) {

								LinearLayout info = new LinearLayout(act);
								info.setOrientation(LinearLayout.VERTICAL);

								TextView title = new TextView(act);
								title.setTextColor(Color.BLACK);
								title.setGravity(Gravity.CENTER);
								title.setTypeface(typefaceSmall);
								title.setText(marker.getTitle());

								TextView snippet = new TextView(act);
								snippet.setTextColor(Color.GRAY);
								snippet.setText(marker.getSnippet());

								info.addView(title);
								info.addView(snippet);

								return info;
							}
						});
			}
		}

	}

	private void addMarkersStaffAll(String date) {
		BitmapDescriptor icon = BitmapDescriptorFactory
				.fromResource(R.drawable.ic_map);
		if (googleMap != null) {
			for (int i = 0; i < tracking_list.size(); i++) {
				if (tracking_list.get(i).getDate().equalsIgnoreCase(date)) {
					String number = tracking_list.get(i).getMnc();
					if (number.startsWith("0"))
						number = number.substring(1);
					number = number.replace(" ", "");
					String operator = getOperator(Integer.parseInt(number));
					googleMap.addMarker(new MarkerOptions()
							.position(
									new LatLng(Double.parseDouble(tracking_list
											.get(i).getLats()), Double
											.parseDouble(tracking_list.get(i)
													.getLongs())))
							.icon(icon)
							.title(tracking_list.get(i).getNama_lengkap())
							.snippet(
									"Kode Customer :"
											+ tracking_list.get(i)
													.getUsername() + "\n"
											+ "Address :"
											+ tracking_list.get(i).getAddress()
											+ "\n" + "Operator :" + operator
											+ "\n" + "IMEI :"
											+ tracking_list.get(i).getImei()
											+ "\n" + "Time :"
											+ tracking_list.get(i).getDate()
											+ " "
											+ tracking_list.get(i).getTime()
											+ "\n"));
					googleMap
							.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

								@Override
								public View getInfoWindow(Marker arg0) {
									return null;
								}

								@Override
								public View getInfoContents(Marker marker) {

									LinearLayout info = new LinearLayout(act);
									info.setOrientation(LinearLayout.VERTICAL);

									TextView title = new TextView(act);
									title.setTextColor(Color.BLACK);
									title.setGravity(Gravity.CENTER);
									title.setTypeface(typefaceSmall);
									title.setText(marker.getTitle());

									TextView snippet = new TextView(act);
									snippet.setTextColor(Color.GRAY);
									snippet.setText(marker.getSnippet());

									info.addView(title);
									info.addView(snippet);

									return info;
								}
							});
				}
			}
		}

	}

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Failed to create maps", Toast.LENGTH_SHORT).show();
			}
		}

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		Location location = locationManager.getLastKnownLocation(provider);
		if (location != null) {
			Log.d(LOG_TAG, "Provider " + provider + " has been selected.");
			// double latitude = -8.7813933;
			// double longitude = 115.1757012;
			lat = location.getLatitude();
			lng = location.getLongitude();
			// onLocationChanged(location);
			// start = new LatLng(lat, lng);
		}

		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

		// Showing / hiding your current location
		googleMap.setMyLocationEnabled(true);

		// Enable / Disable zooming controls
		googleMap.getUiSettings().setZoomControlsEnabled(false);

		// Enable / Disable my location button
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);

		// Enable / Disable Compass icon
		googleMap.getUiSettings().setCompassEnabled(true);

		// Enable / Disable Rotate gesture
		googleMap.getUiSettings().setRotateGesturesEnabled(true);

		// Enable / Disable zooming functionality
		googleMap.getUiSettings().setZoomGesturesEnabled(true);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		MenuItem item = menu.findItem(R.id.menu_upload);
		if (item != null) {
			item.setVisible(false);
		}

		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_level = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_LEVEL, null);
		int levelStaff = Integer.parseInt(main_app_staff_level);
		if (levelStaff > 2) {
			item = menu.findItem(R.id.menu_refresh);
			if (item != null) {
				item.setVisible(false);
			}
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataStaff().execute();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_locator_processing_staff_empty);
				showCustomDialog(message);
			}
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 11) {
				if (position == 0) {
					Intent intentActivity = new Intent(this,
							CustomerActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 1) {
					Intent intentActivity = new Intent(this,
							JadwalActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 2) {
					Intent intentActivity = new Intent(this,
							ProductActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 3) {
					Intent intentActivity = new Intent(this,
							CustomerProspectActivity.class);
					startActivity(intentActivity);
					finish();
				}  else if (position == 4) {
					Intent intentActivity = new Intent(this,
							LocatorActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
					Intent intentActivity = new Intent(this,
							SalesOrderActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 6) {
					Intent intentActivity = new Intent(this,
							StockOnHandActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 7) {
					Intent intentActivity = new Intent(this,
							DisplayProductActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 8) {
					Intent intentActivity = new Intent(this,
							SuperVisor.class);
					startActivity(intentActivity);
					finish();
				}
				else if (position == 9) {
					Intent intentActivity = new Intent(this,
							InventoryActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 10) {
					Intent intentActivity = new Intent(this,
							ReturActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 11) {
					Intent intentActivity = new Intent(this,
							CustomerMerchandiseActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 12) {
					Intent intentActivity = new Intent(this,
							CheckCustomer.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 13) {
					Intent intentActivity = new Intent(this,
							CheckCustomerProspectActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 14) {
					Intent intentActivity = new Intent(this,
							RequestActivity.class);
					startActivity(intentActivity);
					finish();
				}
			}
		}

		// Toast.makeText(this, "Menu item selected -> " + position,
		// Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onBackPressed() {
		if (mNavigationDrawerFragment.isDrawerOpen())
			mNavigationDrawerFragment.closeDrawer();
		else
			super.onBackPressed();
	}

	public void showCustomDialogExit() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_EXIT_DIALOG);
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
								android.os.Process
										.killProcess(android.os.Process.myPid());

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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			showCustomDialogExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
