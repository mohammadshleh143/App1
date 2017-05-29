package com.mahkota_company.android;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.mahkota_company.android.common.AlarmReceiver;
import com.mahkota_company.android.common.TrackingService;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.Branch;
import com.mahkota_company.android.database.Cluster;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Kemasan;
import com.mahkota_company.android.database.Staff;
import com.mahkota_company.android.database.TypeCustomer;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class LoginActivity extends Activity {
	private static final String LOG_TAG = LoginActivity.class.getSimpleName();
	private Button btnLogin;
	private TextView txtHeading;
	private EditText edtLoginUsername, edtLoginPassword;
	private ProgressDialog progressDialog;
	private Context act;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private String userNameStaff;
	private String passwordStaff;
	private String message;
	private Handler handler = new Handler();
	private String response_server;
	private TextView txtDownloadData;
	private String response_data;
	private long tm = 1;

	private Branch branch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		act = this;
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_login_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		databaseHandler = new DatabaseHandler(this);
		txtHeading = (TextView) findViewById(R.id.textView1);
		txtHeading.setTypeface(typefaceSmall);
		txtDownloadData = (TextView) findViewById(R.id.txtview_download_data);
		edtLoginUsername = (EditText) findViewById(R.id.edt_login_username);
		edtLoginPassword = (EditText) findViewById(R.id.edt_login_password);

		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogin.setTypeface(typefaceSmall);
		btnLogin.setOnClickListener(loginClickListener);

		txtDownloadData.setOnClickListener(downloadDataClickListener);
		// if (databaseHandler.getCountBranch() == 0
		// || databaseHandler.getCountKemasan() == 0
		// || databaseHandler.getCountTypeCustomer() == 0
		// || databaseHandler.getCountWilayah() == 0) {
		if (databaseHandler.getCountKemasan() == 0){
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataKemasan().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
		}else if (databaseHandler.getCountBranch() == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataBranch().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
		}else if (databaseHandler.getCountTypeCustomer() == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataTypeCustomer().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
		}else if (databaseHandler.getCountCluster() == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDatacluster().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
		}else if (databaseHandler.getCountWilayah() == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataWilayah().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
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

	/////////http respon sebelum download data dari db////
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
////end of http respon///////

	///////download data dari db lalu dihandle/////
	private class DownloadDataKemasan extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
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
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWifi.isConnected()){
				String download_data_url = "http://186.132.0.58:8081/"
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_KEMASAN;
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
								CONFIG.SHARED_PREFERENCES_TABLE_KEMASAN, null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataKemasanSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableKemasan();
								saveAppDataKemasanSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableKemasan();
							saveAppDataKemasanSameData(act.getApplicationContext()
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
			}else{
				String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_KEMASAN;
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
								CONFIG.SHARED_PREFERENCES_TABLE_KEMASAN, null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataKemasanSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableKemasan();
								saveAppDataKemasanSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableKemasan();
							saveAppDataKemasanSameData(act.getApplicationContext()
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

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataKemasan(response_data);
				extractDataKemasan();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				new DownloadDataBranch().execute();

			} else {
				handler.post(new Runnable() {
					public void run() {
						message = act
								.getApplicationContext()
								.getResources()
								.getString(
										R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
						showCustomDialog(message);
					}
				});
			}
		}

	}

	private class DownloadDataBranch extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
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
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWifi.isConnected()){
				String download_data_url = "http://186.132.0.58:8081/"
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_BRANCH;
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
								CONFIG.SHARED_PREFERENCES_TABLE_BRANCH, null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataBranchSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableBranch();
								saveAppDataBranchSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableBranch();
							saveAppDataBranchSameData(act.getApplicationContext()
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
			}else{
				String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_BRANCH;
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
								CONFIG.SHARED_PREFERENCES_TABLE_BRANCH, null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataBranchSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableBranch();
								saveAppDataBranchSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableBranch();
							saveAppDataBranchSameData(act.getApplicationContext()
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

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataBranch(response_data);
				extractDataBranch();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				new DownloadDataTypeCustomer().execute();
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}

	}

	private class DownloadDataTypeCustomer extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
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
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWifi.isConnected()){
				String download_data_url = "http://186.132.0.58:8081/"
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_TYPE_CUSTOMER;
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
						String main_app_table_data = spPreferences
								.getString(
										CONFIG.SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER,
										null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataTypeCustomerSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableTypeCustomer();
								saveAppDataTypeCustomerSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableTypeCustomer();
							saveAppDataTypeCustomerSameData(act
									.getApplicationContext().getResources()
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
			}else{
				String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_TYPE_CUSTOMER;
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
						String main_app_table_data = spPreferences
								.getString(
										CONFIG.SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER,
										null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataTypeCustomerSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableTypeCustomer();
								saveAppDataTypeCustomerSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableTypeCustomer();
							saveAppDataTypeCustomerSameData(act
									.getApplicationContext().getResources()
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

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataTypeCustomer(response_data);
				extractDataTypeCustomer();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				new DownloadDatacluster().execute();
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}

	}

	//////////////////////////////////
	private class DownloadDatacluster extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
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
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWifi.isConnected()){
				String download_data_url = "http://186.132.0.58:8081/"
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_CLUSTER;
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
						String main_app_table_data = spPreferences
								.getString(
										CONFIG.SHARED_PREFERENCES_TABLE_CLUSTER,
										null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataClusterSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableCluster();
								saveAppDataClusterSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableCluster();
							saveAppDataClusterSameData(act
									.getApplicationContext().getResources()
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
			}else{
				String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_CLUSTER;
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
						String main_app_table_data = spPreferences
								.getString(
										CONFIG.SHARED_PREFERENCES_TABLE_CLUSTER,
										null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataClusterSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableCluster();
								saveAppDataClusterSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableCluster();
							saveAppDataClusterSameData(act
									.getApplicationContext().getResources()
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

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataCluster(response_data);
				extractDataCluster();
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				new DownloadDataWilayah().execute();
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}

	}

	///////////////////////////////////

	private class DownloadDataWilayah extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.MSG_DLG_LABEL_SYNRONISASI_DATA));
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
			ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if(mWifi.isConnected()){
				String download_data_url = "http://186.132.0.58:8081/"
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_WILAYAH;
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
								CONFIG.SHARED_PREFERENCES_TABLE_WILAYAH, null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataWilayahSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableWilayah();
								saveAppDataWilayahSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableWilayah();
							saveAppDataWilayahSameData(act.getApplicationContext()
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
			}else{
				String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
						+ CONFIG.CONFIG_APP_URL_DOWNLOAD_WILAYAH;
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
								CONFIG.SHARED_PREFERENCES_TABLE_WILAYAH, null);
						if (main_app_table_data != null) {
							if (main_app_table_data.equalsIgnoreCase(response_data)) {
								saveAppDataWilayahSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_true));
							} else {
								databaseHandler.deleteTableWilayah();
								saveAppDataWilayahSameData(act
										.getApplicationContext().getResources()
										.getString(R.string.app_value_false));
							}
						} else {
							databaseHandler.deleteTableWilayah();
							saveAppDataWilayahSameData(act.getApplicationContext()
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

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataWilayah(response_data);
				extractDataWilayah();
				message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.MSG_DLG_LABEL_SYNRONISASI_DATA_SUCCESS);
				showCustomDialogDownloadSuccess(message);
			} else {
				message = act.getApplicationContext().getResources()
						.getString(R.string.MSG_DLG_LABEL_DOWNLOAD_FAILED);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			}
		}

	}
////////end of download//////////


	/////////tampil dialog download data sukses//////
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
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}
////////end of tampil dialog//


	/////////simpan data yang telah didownload keadalam tabel yg di shared preference//
	public void saveAppDataKemasan(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_KEMASAN, responsedata);
		editor.commit();
	}

	public void saveAppDataKemasanSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_KEMASAN_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataBranch(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_BRANCH, responsedata);
		editor.commit();
	}

	public void saveAppDataBranchSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_BRANCH_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTypeCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTypeCustomerSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataCluster(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_CLUSTER,
				responsedata);
		editor.commit();
	}

	public void saveAppDataClusterSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_CLUSTER_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataWilayah(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_WILAYAH, responsedata);
		editor.commit();
	}

	public void saveAppDataWilayahSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_WILAYAH_SAME_DATA,
				responsedata);
		editor.commit();
	}
/////////end of	save data////

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void extractDataKemasan() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_KEMASAN_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_KEMASAN, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("kemasan");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_kemasan = oResponsealue.isNull("id_kemasan") ? null
							: oResponsealue.getString("id_kemasan");
					String nama_id_kemasan = oResponsealue
							.isNull("nama_id_kemasan") ? null : oResponsealue
							.getString("nama_id_kemasan");
					Log.d(LOG_TAG, "id_kemasan:" + id_kemasan);
					Log.d(LOG_TAG, "nama_id_kemasan:" + nama_id_kemasan);
					databaseHandler.add_Kemasan(new Kemasan(Integer
							.parseInt(id_kemasan), nama_id_kemasan));
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

	public void extractDataBranch() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_BRANCH_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_BRANCH, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("branch");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_branch = oResponsealue.isNull("id_branch") ? null
							: oResponsealue.getString("id_branch");
					String kode_branch = oResponsealue.isNull("kode_branch") ? null
							: oResponsealue.getString("kode_branch");
					String deskripsi = oResponsealue.isNull("deskripsi") ? null
							: oResponsealue.getString("deskripsi");
					Log.d(LOG_TAG, "id_branch:" + id_branch);
					Log.d(LOG_TAG, "kode_branch:" + kode_branch);
					Log.d(LOG_TAG, "deskripsi:" + deskripsi);
					databaseHandler.add_Branch(new Branch(Integer
							.parseInt(id_branch), kode_branch, deskripsi));
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

	public void extractDataTypeCustomer() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("type_customer");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_type_customer = oResponsealue
							.isNull("id_type_customer") ? null : oResponsealue
							.getString("id_type_customer");
					String type_customer = oResponsealue
							.isNull("type_customer") ? null : oResponsealue
							.getString("type_customer");
					String deskripsi = oResponsealue.isNull("deskripsi") ? null
							: oResponsealue.getString("deskripsi");
					Log.d(LOG_TAG, "id_type_customer:" + id_type_customer);
					Log.d(LOG_TAG, "type_customer:" + type_customer);
					Log.d(LOG_TAG, "deskripsi:" + deskripsi);
					databaseHandler.add_TypeCustomer(new TypeCustomer(Integer
							.parseInt(id_type_customer), type_customer,
							deskripsi));
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

	public void extractDataCluster() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CLUSTER_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_CLUSTER, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("cluster");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_cluster = oResponsealue
							.isNull("id_cluster") ? null : oResponsealue
							.getString("id_cluster");
					String nama_cluster = oResponsealue
							.isNull("nama_cluster") ? null : oResponsealue
							.getString("nama_cluster");
					String description = oResponsealue.isNull("description") ? null
							: oResponsealue.getString("description");
					Log.d(LOG_TAG, "id_cluster:" + id_cluster);
					Log.d(LOG_TAG, "nama_cluster:" + nama_cluster);
					Log.d(LOG_TAG, "description:" + description);
					databaseHandler.add_Cluster(new Cluster(Integer
							.parseInt(id_cluster), nama_cluster,
							description));
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

	public void extractDataWilayah() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_WILAYAH_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_WILAYAH, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("wilayah");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
							: oResponsealue.getString("id_wilayah");
					String nama_wilayah = oResponsealue.isNull("nama_wilayah") ? null
							: oResponsealue.getString("nama_wilayah");
					String deskripsi = oResponsealue.isNull("deskripsi") ? null
							: oResponsealue.getString("deskripsi");
					String lats = oResponsealue.isNull("lats") ? null
							: oResponsealue.getString("lats");
					String longs = oResponsealue.isNull("longs") ? null
							: oResponsealue.getString("longs");
					Log.d(LOG_TAG, "id_wilayah:" + id_wilayah);
					Log.d(LOG_TAG, "nama_wilayah:" + nama_wilayah);
					Log.d(LOG_TAG, "deskripsi:" + deskripsi);
					Log.d(LOG_TAG, "lats:" + lats);
					Log.d(LOG_TAG, "longs:" + longs);
					databaseHandler.add_Wilayah(new Wilayah(Integer
							.parseInt(id_wilayah), nama_wilayah, deskripsi,
							lats, longs));
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

	protected void gotoMenuUtama() {
		startMonitoring();
		Intent intentActivity = new Intent(this, CustomerActivity.class);
		startActivity(intentActivity);
		finish();
	}

	/**
	 * Button Login Listener
	 */
	public OnClickListener loginClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (databaseHandler.getCountBranch() == 0
					|| databaseHandler.getCountKemasan() == 0
					|| databaseHandler.getCountTypeCustomer() == 0
					|| databaseHandler.getCountWilayah() == 0) {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_login_text_view_download_data_warning);
				showCustomDialog(message);
			} else {
				if (passValidationForLogin()) {
					userNameStaff = edtLoginUsername.getText().toString();
					passwordStaff = edtLoginPassword.getText().toString();
					if (GlobalApp.checkInternetConnection(act)) {
						new LoginToServer().execute();
					} else {
						String message = act
								.getApplicationContext()
								.getResources()
								.getString(
										R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
						showCustomDialog(message);
					}
				}
			}
		}
	};

	public OnClickListener downloadDataClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataKemasan().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
				showCustomDialog(message);
			}
		}
	};

	/**
	 * Validasi Empty Box
	 *
	 * @return
	 */
	public boolean passValidationForLogin() {
		if (GlobalApp.isBlank(edtLoginUsername)) {
			GlobalApp.takeDefaultAction(
					edtLoginUsername,
					LoginActivity.this,
					getApplicationContext().getResources().getString(
							R.string.app_login_error_no_email));
			return false;
		} else if (GlobalApp.isBlank(edtLoginPassword)) {
			GlobalApp.takeDefaultAction(
					edtLoginPassword,
					LoginActivity.this,
					getApplicationContext().getResources().getString(
							R.string.app_login_error_no_password));
			return false;
		}
		return true;
	}

	/**
	 * Setup the URL Login
	 *
	 * @return
	 */
	public String prepareUrlForLogin() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(mWifi.isConnected()){
			return String.format("http://186.132.0.58:8081/"
					+ CONFIG.CONFIG_APP_URL_UPLOAD_LOGIN);
		}else{
			return String.format(CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_LOGIN);
		}

	}

	/**
	 * Post Login ke Server
	 *
	 * @return
	 */
	public HttpResponse postDataForLogin() {
		String textUrl = prepareUrlForLogin();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		HttpResponse response;
		nameValuePairs.add(new BasicNameValuePair("username", userNameStaff));
		nameValuePairs.add(new BasicNameValuePair("password", passwordStaff));
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(textUrl);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(httppost);
		} catch (UnsupportedEncodingException e1) {
			response = null;
		} catch (IOException e) {
			response = null;
		}
		return response;
	}

	public void showCustomDialogLoginSuccess(String msg) {
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
								extractDataLogin();
								alertDialog.dismiss();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	/**
	 *
	 * Check Login to Server
	 *
	 */
	class LoginToServer extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setTitle(getApplicationContext().getResources()
					.getString(R.string.app_name));
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_login_processing));
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			HttpResponse response = postDataForLogin();
			int retCode = (response != null) ? response.getStatusLine()
					.getStatusCode() : -1;
			if (retCode != 200) {
				message = getApplicationContext().getResources().getString(
						R.string.app_login_processing_error);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(message);
					}
				});
			} else {
				try {
					response_server = EntityUtils
							.toString(response.getEntity());
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
			if (response_server != null) {
				JSONObject objectResponse;
				String status = "false";
				Log.d(LOG_TAG, "response server:" + response_server);
				try {
					objectResponse = new JSONObject(response_server);
					status = objectResponse.isNull("error") ? "false"
							: objectResponse.getString("error");
					Log.d(LOG_TAG, "status:" + status);
				} catch (JSONException e) {
					final String msg = getApplicationContext().getResources()
							.getString(R.string.app_login_processing_error);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(msg);
						}
					});
				}

				if (status.equalsIgnoreCase("True")) {
					final String msg = act.getApplicationContext()
							.getResources()
							.getString(R.string.app_login_result_failed);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(msg);
						}
					});
				} else {
					GlobalApp.storeDataProfile(response_server,
							getSharedPrefereces());
					final String msg = getApplicationContext().getResources()
							.getString(R.string.app_login_result_success);
					showCustomDialogLoginSuccess(msg);
				}
			} else {
				final String msg = getApplicationContext().getResources()
						.getString(R.string.app_login_processing_error);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}

	}

	/**
	 * Extract Json Response
	 */
	public void extractDataLogin() {
		SharedPreferences sharedpreference = getSharedPrefereces();
		String staff_profile = sharedpreference.getString(
				GlobalApp.SHARED_PREFERENCES_PROFILE_DATA, null);
		JSONObject objectResponse;
		try {
			objectResponse = new JSONObject(staff_profile);
			JSONArray jsonArray = objectResponse.getJSONArray("login_response");
			if (databaseHandler.getCountStaff() > 0) {
				databaseHandler.deleteTableStaff();
			}
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject oResponsealue = jsonArray.getJSONObject(i);
				String id_staff = oResponsealue.isNull("id_staff") ? null
						: oResponsealue.getString("id_staff");
				String nama_lengkap = oResponsealue.isNull("nama_lengkap") ? null
						: oResponsealue.getString("nama_lengkap");
				String username = oResponsealue.isNull("username") ? null
						: oResponsealue.getString("username");
				String no_telp = oResponsealue.isNull("no_telp") ? null
						: oResponsealue.getString("no_telp");
				String level = oResponsealue.isNull("level") ? null
						: oResponsealue.getString("level");
				String id_branch = oResponsealue.isNull("id_branch") ? null
						: oResponsealue.getString("id_branch");
				String id_type_customer = oResponsealue
						.isNull("id_type_customer") ? null : oResponsealue
						.getString("id_type_customer");
				String id_depo = oResponsealue.isNull("id_depo") ? null
						: oResponsealue.getString("id_depo");
				Log.d(LOG_TAG, "id_staff:" + id_staff);
				Log.d(LOG_TAG, "nama_lengkap:" + nama_lengkap);
				Log.d(LOG_TAG, "username:" + username);
				Log.d(LOG_TAG, "no_telp:" + no_telp);
				Log.d(LOG_TAG, "level:" + level);
				Log.d(LOG_TAG, "id_branch:" + id_branch);
				Log.d(LOG_TAG, "id_type_customer:" + id_type_customer);
				Log.d(LOG_TAG, "id_depo:" + id_depo);
				databaseHandler.add_Staff(new Staff(Integer.parseInt(id_staff),
						nama_lengkap, userNameStaff, no_telp, passwordStaff,
						Integer.parseInt(level), id_branch, id_type_customer,
						Integer.parseInt(id_depo)));
				saveAppDataStaffUsername(userNameStaff);
				saveAppDataStaffLevel(level);
				saveAppDataStaffNamaLengkap(nama_lengkap);
				saveAppDataStaffKodeBranch(id_branch);
				saveAppDataStaffIdWilayah(id_depo);
				saveAppDataStaffIdDepo(id_depo);
				saveAppDataStaffIdStaff(id_staff);
			}
			gotoMenuUtama();
		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

		}
	}

	public void startMonitoring() {
		act.startService(new Intent(act, TrackingService.class));
		/*try {
			final AlarmManager alarms = (AlarmManager) this
					.getSystemService(Context.ALARM_SERVICE);
			Intent intent = new Intent(getApplicationContext(),
					AlarmReceiver.class);
			intent.putExtra(AlarmReceiver.ACTION_ALARM,
					AlarmReceiver.ACTION_ALARM);
			intent.putExtra("mahkota", "tracking");
			final PendingIntent pIntent = PendingIntent.getBroadcast(this,
					1234567, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			tm = 20 * 1000 * 60;
			alarms.setRepeating(AlarmManager.RTC_WAKEUP,
					System.currentTimeMillis(), tm, pIntent);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.d(LOG_TAG, "Initialisasi startMonitoring Error");
		}*/
	}

	public void saveAppDataStaffUsername(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, responsedata);
		editor.commit();
	}

	public void saveAppDataStaffNamaLengkap(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_NAMA_LENGKAP,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStaffKodeBranch(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStaffIdWilayah(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStaffIdStaff(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, responsedata);
		editor.commit();
	}

	public void saveAppDataStaffIdDepo(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_ID_DEPO, responsedata);
		editor.commit();
	}

	public void saveAppDataStaffLevel(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_STAFF_LEVEL, responsedata);
		editor.commit();
	}

}
