package com.mahkota_company.android.jadwal;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.R;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class DetailCustomerLocatorActivity extends Activity implements
		LocationListener {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;

	private GoogleMap googleMap;
	double lat;// = location.getLatitude();
	double lng;// = location.getLongitude();
	private String logLatitude;
	private String logLongitude;
	private String logNamaCustomer;
	ArrayList<Customer> customer_data = new ArrayList<Customer>();
	// Location location; // location
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10
	// meters
	// // The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1
	private Location location;
	private static final String LOG_TAG = DetailCustomerLocatorActivity.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_customer_locator);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoDetailCustomer();
			}
		});
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
		if (main_app_table_id != null) {
			saveAppDataCustomerIdCustomer(main_app_table_id);

			Customer customer_from_db = databaseHandler.getCustomer(Integer
					.parseInt(main_app_table_id));

			if (customer_from_db != null) {
				logLatitude = customer_from_db.getLats();
				logLongitude = customer_from_db.getLongs();
				logNamaCustomer = customer_from_db.getNama_lengkap();
				try {
					// Loading map
					initilizeMap();

					// Changing map type
					googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
					// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
					// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
					// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
					// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

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

					double latitude = Double.parseDouble(logLatitude); // 17.385044;
					double longitude = Double.parseDouble(logLongitude); // 78.486671;

					BitmapDescriptor icon = BitmapDescriptorFactory
							.fromResource(R.drawable.ic_map);

					MarkerOptions marker = new MarkerOptions().position(
							new LatLng(latitude, longitude)).title(
							logNamaCustomer);

					marker.icon(icon);

					// Move the camera to last position with a zoom level
					CameraPosition cameraPosition = new CameraPosition.Builder()
							.target(new LatLng(latitude, longitude)).zoom(17)
							.build();
					googleMap.addMarker(marker);
					googleMap.animateCamera(CameraUpdateFactory
							.newCameraPosition(cameraPosition));
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				gotoDetailCustomer();
			}
		} else {
			gotoCustomer();
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

	public void gotoDetailCustomer() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
		if (main_app_table_id != null) {
			saveAppDataCustomerIdCustomer(main_app_table_id);
			Intent i = new Intent(DetailCustomerLocatorActivity.this,
					DetailJadwalCustomerActivity.class);
			startActivity(i);
			finish();
		} else {
			gotoCustomer();
		}

	}

	public void gotoCustomer() {
		Intent i = new Intent(DetailCustomerLocatorActivity.this,
				JadwalCustomerActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();

	}

	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				showCustomDialog("Sorry! unable to create maps");
			}
		}

		// LocationManager locationManager = (LocationManager)
		// getSystemService(Context.LOCATION_SERVICE);
		// Criteria criteria = new Criteria();
		// String provider = locationManager.getBestProvider(criteria, false);
		// Location location = locationManager.getLastKnownLocation(provider);
		// if (location != null) {
		// Log.d("", "Provider " + provider + " has been selected.");
		// lat = location.getLatitude();
		// lng = location.getLongitude();
		// // onLocationChanged(location);
		// }

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean isNetworkEnabled = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					0);
		} else {
			if (isNetworkEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
				Log.d("Network", "Network");
				if (locationManager != null) {
					location = locationManager
							.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					if (location != null) {
						lat = location.getLatitude();
						lng = location.getLongitude();
					}
				}
			} else {
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								lat = location.getLatitude();
								lng = location.getLongitude();
							}
						}
					}
				}
			}

		}

	}

	@Override
	public void onLocationChanged(Location location) {

		lat = location.getLatitude();
		lng = location.getLongitude();

		Log.d(LOG_TAG, "onLocationChanged " + String.valueOf(lat) + " "
				+ String.valueOf(lng));
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
