package com.mahkota_company.android.common;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

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


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.TrackingLogs;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

@SuppressWarnings("deprecation")
@SuppressLint("SimpleDateFormat")
public class TrackingService extends Service {
	public long NOTIFY_INTERVAL = 15 * 1000; // 10 seconds
	private Handler mHandler = new Handler();
	private Timer mTimer = new Timer();
	private Context _context;
	private String LOG_TAG = TrackingService.class.getSimpleName();
	// flag for network status
	private boolean isGPSEnabled = false;

	private DatabaseHandler databaseHandler;

	private Location location; // location
	private double latitude; // latitude
	private double longitude; // longitude
	protected String streetName = "";

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 15 * 1; // 15 seconds
	// Declaring a Location Manager
	protected LocationManager locationManager;
	private String provider = LocationManager.GPS_PROVIDER;
	final Handler handler = new Handler();
	// public static long tm = 1;
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
				// Toast.makeText(_context, "Status: " + strStatus,
				// Toast.LENGTH_SHORT).show();
			} catch (Exception e) {
				e.printStackTrace();
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
				// Toast.makeText(_context, "***new location***" +
				// location.getLatitude() + " " + location.getLongitude(),
				// Toast.LENGTH_SHORT).show();

				// Geocoder gcd = new Geocoder(_context, Locale.getDefault());
				try {
					// List<Address> addresses = gcd.getFromLocation(
					// location.getLatitude(), location.getLongitude(),
					// 100);
					// if (addresses.size() > 0) {
					// StreetName = addresses.get(0).getThoroughfare();
					// if (StreetName == null) {
					// StreetName = addresses.get(0).getFeatureName();
					// }
					// }
					streetName = getAddress(latitude, longitude);
				} catch (Exception e) {
					Log.i(LOG_TAG, "onLocationChanged e " + e.toString());
				}
			} catch (Exception e) {
				Log.i(LOG_TAG, "onLocationChanged " + e.toString());
			}
		}
	};

	public String getAddress(double lat, double lng) {
		String strAdd = "";
		Geocoder geocoder = new Geocoder(_context, Locale.getDefault());
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
			} else {
				Log.w(LOG_TAG,
						"My Current loction address No Address returned!");
			}
		} catch (Exception e) {
			Log.w(LOG_TAG, "My Current loction address Canont get Address!");
		}
		return strAdd;
	}


	public void initDatabase() {
		databaseHandler = new DatabaseHandler(_context);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Service#onCreate() Called by the system when the service
	 * is first created. Do not call this method directly.
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		_context = getApplicationContext();
		// NOTIFY_INTERVAL = NOTIFY_INTERVAL * 20;

		try {
			initDatabase();
		} catch (Exception e) {
			Log.d(LOG_TAG, "Initialisasi Database Error");
		}

		try {
			locationManager = (LocationManager) _context
					.getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(provider, 1000L, 1.0f,
					locationListener);
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
					MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
			Log.i(LOG_TAG, "onReceive isGPSEnabled " + isGPSEnabled);
			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			// Intent intentGps = new Intent(
			// "android.location.NETWORK_ENABLED_CHANGE");
			// intentGps.putExtra("enabled", true);
			// _context.sendBroadcast(intentGps);

			if (isGPSEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
						MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
				Log.d(LOG_TAG, "GPS Enabled");
				if (locationManager != null) {
					if (location != null) {
						latitude = location.getLatitude();
						longitude = location.getLongitude();
						try {
							streetName = getAddress(latitude, longitude);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} else {
				Intent intentGps = new Intent(
						"android.location.GPS_ENABLED_CHANGE");
				intentGps.putExtra("enabled", true);
				_context.sendBroadcast(intentGps);
			}

		} catch (Exception e) {
			Log.d(LOG_TAG, "exception = " + e.getMessage());
		}

	}

	protected SharedPreferences getSharedPrefereces() {
		return _context.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	class TimeDisplayTimerTask extends TimerTask {

		@Override
		public void run() {
			// run on another thread
			mHandler.post(new Runnable() {

				@Override
				public void run() {

					try {
						SharedPreferences spPreferences = getSharedPrefereces();

						String app_kode_user = spPreferences.getString(
								CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
						String app_nama_user = spPreferences.getString(
								CONFIG.SHARED_PREFERENCES_STAFF_NAMA_LENGKAP, null);
						String app_level = spPreferences.getString(
								CONFIG.SHARED_PREFERENCES_STAFF_LEVEL, null);

						// final Calendar c = Calendar.getInstance();
						// int hour = c.get(Calendar.HOUR_OF_DAY);
						// int minute = c.get(Calendar.MINUTE);
						// String stringTimeHour = String.valueOf(hour);
						// if (stringTimeHour.length() < 2) {
						// stringTimeHour = "0" + stringTimeHour;
						// }
						// String stringTimeMinute = String.valueOf(minute);
						// if (stringTimeMinute.length() < 2) {
						// stringTimeMinute = "0" + stringTimeMinute;
						// }
						// String stringTime = stringTimeHour +
						// stringTimeMinute;
						// int timeNow = Integer.parseInt(stringTime);
						//
						// int beforeTime = 800;
						// int afterTime = 1930;
						// Log.d(LOG_TAG, "timeNow " + timeNow + "beforeTime "
						// + beforeTime + "afterTime " + afterTime);
						int tempLevel = Integer.parseInt(app_level);
						ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
						NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
						if (GlobalApp.checkInternetConnection(_context) && tempLevel > 2) {
							// if (timeNow == beforeTime && timeNow ==
							// afterTime) {
							// // Toast.makeText(getApplicationContext(),
							// // "Set Time Salah",
							// // Toast.LENGTH_SHORT).show();
							// Log.d(LOG_TAG, "ignore the tracking");
							// } else {
							// if (timeNow > beforeTime && timeNow < afterTime)
							// {

							if (streetName.length() == 0) {
								streetName = CONFIG.CONFIG_APP_ERROR_MESSAGE_ADDRESS_FAILED;
							}
							// Toast.makeText(_context,
							// msg_sms,
							// Toast.LENGTH_SHORT).show();

							if(mWifi.isConnected()){
								String urlLocator = "http://186.132.0.58:8081/"
										+ CONFIG.CONFIG_APP_URL_UPLOAD_LOCATOR;
								int countTrackingLogs = databaseHandler.getCountTrackingLogs();
								//ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
								//NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
								if (countTrackingLogs>1) {
									ArrayList<TrackingLogs> trackingLogs_list = databaseHandler
											.getAllTrackingLogs();
									for(TrackingLogs trackingLogs : trackingLogs_list) {
										uploadTracking(
												urlLocator,
												trackingLogs.getUsername(),
												trackingLogs.getNama_lengkap(),
												String.valueOf(trackingLogs.getLevel()),
												trackingLogs.getLats(),
												trackingLogs.getLongs(),
												trackingLogs.getAddress(), trackingLogs.getImei(),
												trackingLogs.getMcc(),
												trackingLogs.getMnc(),
												trackingLogs.getDate(), trackingLogs.getTime());
									}
									databaseHandler.deleteTableTrackingLogs();

								}else if (!getNameOfDay().equalsIgnoreCase("Sunday")) { //200
									try {
										int tempLat = (int) latitude;
										int tempLong = (int) longitude;

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
										final String timeOutput = zero(hrs) + ":"
												+ zero(min) + ":" + zero(sec);
										TelephonyManager telephonyManager = (TelephonyManager) _context
												.getSystemService(Context.TELEPHONY_SERVICE);
										String imei = telephonyManager
												.getDeviceId();
										String networkOperator = telephonyManager
												.getNetworkOperator();
										Log.d(LOG_TAG, "networkOperator="
												+ networkOperator);
										if (TextUtils.isEmpty(networkOperator) == false) {
											int mcc = Integer
													.parseInt(networkOperator
															.substring(0, 3));
											int mnc = Integer
													.parseInt(networkOperator
															.substring(3));
											if (tempLat != 0 || tempLong != 0) {
												Log.d(LOG_TAG, "uploadTracking = "
														+ urlLocator);

												databaseHandler.add_TrackingLogs(new TrackingLogs(countTrackingLogs+1, app_kode_user,
														app_nama_user,
														Integer.parseInt(app_level),
														String.valueOf(latitude),
														String.valueOf(longitude),
														streetName, imei,
														String.valueOf(mcc),
														String.valueOf(mnc),
														dateOutput, timeOutput));
											}
										} else {
											Log.d(LOG_TAG,
													"disableds networkOperator="
															+ networkOperator);
										}

									} catch (Exception e) {
										Log.d(LOG_TAG, e.getMessage());
									}
								}
							}else{
								String urlLocator = CONFIG.CONFIG_APP_URL_PUBLIC
										+ CONFIG.CONFIG_APP_URL_UPLOAD_LOCATOR;
								int countTrackingLogs = databaseHandler.getCountTrackingLogs();
								//ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
								//NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
								if (countTrackingLogs>1) {
									ArrayList<TrackingLogs> trackingLogs_list = databaseHandler
											.getAllTrackingLogs();
									for(TrackingLogs trackingLogs : trackingLogs_list) {
										uploadTracking(
												urlLocator,
												trackingLogs.getUsername(),
												trackingLogs.getNama_lengkap(),
												String.valueOf(trackingLogs.getLevel()),
												trackingLogs.getLats(),
												trackingLogs.getLongs(),
												trackingLogs.getAddress(), trackingLogs.getImei(),
												trackingLogs.getMcc(),
												trackingLogs.getMnc(),
												trackingLogs.getDate(), trackingLogs.getTime());
									}
									databaseHandler.deleteTableTrackingLogs();

								}else if (!getNameOfDay().equalsIgnoreCase("Sunday")) { //200
									try {
										int tempLat = (int) latitude;
										int tempLong = (int) longitude;

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
										final String timeOutput = zero(hrs) + ":"
												+ zero(min) + ":" + zero(sec);
										TelephonyManager telephonyManager = (TelephonyManager) _context
												.getSystemService(Context.TELEPHONY_SERVICE);
										String imei = telephonyManager
												.getDeviceId();
										String networkOperator = telephonyManager
												.getNetworkOperator();
										Log.d(LOG_TAG, "networkOperator="
												+ networkOperator);
										if (TextUtils.isEmpty(networkOperator) == false) {
											int mcc = Integer
													.parseInt(networkOperator
															.substring(0, 3));
											int mnc = Integer
													.parseInt(networkOperator
															.substring(3));
											if (tempLat != 0 || tempLong != 0) {
												Log.d(LOG_TAG, "uploadTracking = "
														+ urlLocator);

												databaseHandler.add_TrackingLogs(new TrackingLogs(countTrackingLogs+1, app_kode_user,
														app_nama_user,
														Integer.parseInt(app_level),
														String.valueOf(latitude),
														String.valueOf(longitude),
														streetName, imei,
														String.valueOf(mcc),
														String.valueOf(mnc),
														dateOutput, timeOutput));
											}
										} else {
											Log.d(LOG_TAG,
													"disableds networkOperator="
															+ networkOperator);
										}

									} catch (Exception e) {
										Log.d(LOG_TAG, e.getMessage());
									}
								}
							}

						} else{
							Log.d(LOG_TAG, "Koneksi internet mati silahkan hidupakan internet kembali");
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

					// display toast
					// Toast.makeText(getApplicationContext(), getDateTime(),
					// Toast.LENGTH_SHORT).show();
				}

			});
		}

		protected String getDateTime() {
			// get date time in custom format
			SimpleDateFormat sdf = new SimpleDateFormat(
					"[yyyy/MM/dd - HH:mm:ss]");
			return sdf.format(new Date());
		}

	}

	public String uploadTracking(final String url, final String username,
								 final String nama_lengkap, final String level, final String lats,
								 final String longs, final String address, final String imei,
								 final String mcc, final String mnc, final String date,
								 final String time) {
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

			// Adding file data to http body
			entity.addPart("username", new StringBody(username));
			entity.addPart("nama_lengkap", new StringBody(nama_lengkap));
			entity.addPart("level", new StringBody(level));
			entity.addPart("lats", new StringBody(lats));
			entity.addPart("longs", new StringBody(longs));
			entity.addPart("address", new StringBody(address));
			entity.addPart("imei", new StringBody(imei));
			entity.addPart("mcc", new StringBody(mcc));
			entity.addPart("mnc", new StringBody(mnc));
			entity.addPart("date", new StringBody(date));
			entity.addPart("time", new StringBody(time));

			httppost.setEntity(entity);

			// Making server call
			HttpResponse response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();

			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 12) { //200
				// Server response
				responseString = EntityUtils.toString(r_entity);
				Log.d(LOG_TAG, "responseString=" + responseString);
			} else {
				Log.d(LOG_TAG, "responseString=" + responseString);
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (ClientProtocolException e) {
			responseString = e.toString();
			Log.d(LOG_TAG, "responseString=" + responseString);
		} catch (IOException e) {
			responseString = e.toString();
			Log.d(LOG_TAG, "responseString=" + responseString);
		}

		return responseString;

	}

	protected void startService() {
		stopService(new Intent(getBaseContext(), TrackingService.class));
	}

	protected void stopService() {
		stopService(new Intent(getBaseContext(), TrackingService.class));
	}

	public String getNameOfDay() {
		String nameOfDay = "";
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case 1:
			nameOfDay = "Sunday";
			break;
		case 2:
			nameOfDay = "Monday";

			break;
		case 3:
			nameOfDay = "Tuesday";

			break;
		case 4:
			nameOfDay = "Wednesday";

			break;
		case 5:
			nameOfDay = "Thursday";

			break;
		case 6:
			nameOfDay = "Friday";

			break;
		case 7:
			nameOfDay = "Saturday";

			break;
		}
		return nameOfDay;
	}

	public String getCurrentTime() {
		try {
			Calendar calendar = Calendar.getInstance();
			String timeOutput = zero(calendar.get(Calendar.HOUR_OF_DAY)) + ":"
					+ zero(calendar.get(Calendar.MINUTE)) + ":"
					+ zero(calendar.get(Calendar.SECOND));
			return timeOutput;
		} catch (Exception e) {
			return null;
		}

	}

	public String getCurrentDate() {
		try {
			final String date = "MM/dd/yyyy";
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat dateFormat = new SimpleDateFormat(date);
			final String dateOutput = dateFormat.format(calendar.getTime());
			return dateOutput;
		} catch (Exception e) {
			return null;
		}
	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		// Display the Toast Message
		// Toast.makeText(this, "Start Service", Toast.LENGTH_SHORT).show();

		// schedule task
		mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0,
				NOTIFY_INTERVAL);

		return super.onStartCommand(intent, flags, startId);
	}

	//
	/*
	 * (non-Javadoc)
	 *
	 * @see android.app.Service#onDestroy() Service is destroyed when user stop
	 * the service
	 */
	@Override
	public void onDestroy() {
		// Display the Toast Message
		// Toast.makeText(this, "Stop Service", Toast.LENGTH_SHORT).show();
		if (mTimer != null) {
			mTimer.cancel();
		}
		super.onDestroy();
	}

}
