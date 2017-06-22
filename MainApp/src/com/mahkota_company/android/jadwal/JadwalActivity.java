package com.mahkota_company.android.jadwal;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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

import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.inventory.Kanvas_Uploaded;
import com.mahkota_company.android.supervisor.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.InventoryActivity;
import com.mahkota_company.android.inventory.RequestActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class JadwalActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<Jadwal> jadwal_list = new ArrayList<Jadwal>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = JadwalActivity.class.getSimpleName();
	private Typeface typefaceSmall;
	private TextView tvKodeJadwal;
	private TextView tvKodeDate;
	private TextView tvKodeTotalCustomer;
	private Button btreqadd;
	private EditText search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_jadwal);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeJadwal = (TextView) findViewById(R.id.activity_jadwal_kode_jadwal);
		tvKodeDate = (TextView) findViewById(R.id.activity_jadwal_date_jadwal);
		tvKodeTotalCustomer = (TextView) findViewById(R.id.activity_jadwal_jumlah_customer);
		btreqadd = (Button) findViewById(R.id.activity_button_req_jadwal);
		tvKodeDate.setTypeface(typefaceSmall);
		tvKodeJadwal.setTypeface(typefaceSmall);
		tvKodeTotalCustomer.setTypeface(typefaceSmall);

		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_jadwal_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(1);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		int countJadwal = databaseHandler.getCountJadwal();

		if (countJadwal == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataJadwal().execute();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_jadwal_processing_empty);
				showCustomDialog(message);
			}
		} else {
			showListJadwal();
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

	private class DownloadDataJadwal extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_jadwal_processing));
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
			SharedPreferences spPreferences1 = getSharedPrefereces();
			String main_app_staff_username = spPreferences1.getString(
					CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
			String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_JADWAL + "?username="
					+ main_app_staff_username;
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
							CONFIG.SHARED_PREFERENCES_TABLE_JADWAL, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataJadwalSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableJadwal();
							saveAppDataJadwalSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableJadwal();
						saveAppDataJadwalSameData(act.getApplicationContext()
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
				saveAppDataJadwal(response_data);
				extractDataJadwal();
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
								showListJadwal();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void extractDataJadwal() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_JADWAL, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("jadwal");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_jadwal = oResponsealue.isNull("id_jadwal") ? null
							: oResponsealue.getString("id_jadwal");
					String kode_jadwal = oResponsealue.isNull("kode_jadwal") ? null
							: oResponsealue.getString("kode_jadwal");
					String kode_customer = oResponsealue
							.isNull("kode_customer") ? null : oResponsealue
							.getString("kode_customer");
					String username = oResponsealue.isNull("username") ? null
							: oResponsealue.getString("username");
					String alamat = oResponsealue.isNull("alamat") ? null
							: oResponsealue.getString("alamat");
					String nama_toko = oResponsealue.isNull("nama_toko") ? null
							: oResponsealue.getString("nama_toko");
					String id_wilayah = oResponsealue.isNull("id_wilayah") ? null
							: oResponsealue.getString("id_wilayah");
					String status = oResponsealue.isNull("status") ? null
							: oResponsealue.getString("status");
					String date = oResponsealue.isNull("date") ? null
							: oResponsealue.getString("date");
					String checkin = oResponsealue.isNull("checkin") ? null
							: oResponsealue.getString("checkin");
					String checkout = oResponsealue.isNull("checkout") ? null
							: oResponsealue.getString("checkout");
					String keterangan = oResponsealue.isNull("keterangan") ? null
							: oResponsealue.getString("keterangan");

					Log.d(LOG_TAG, "id_jadwal:" + id_jadwal);
					Log.d(LOG_TAG, "kode_jadwal:" + kode_jadwal);
					Log.d(LOG_TAG, "kode_customer:" + kode_customer);
					Log.d(LOG_TAG, "username:" + username);
					Log.d(LOG_TAG, "alamat:" + alamat);
					Log.d(LOG_TAG, "nama_toko:" + nama_toko);
					Log.d(LOG_TAG, "id_wilayah:" + id_wilayah);
					Log.d(LOG_TAG, "status:" + status);
					Log.d(LOG_TAG, "date:" + date);
					Log.d(LOG_TAG, "checkin:" + checkin);
					Log.d(LOG_TAG, "checkout:" + checkout);
					Log.d(LOG_TAG, "keterangan:" + keterangan);
					databaseHandler.add_Jadwal(new Jadwal(Integer
							.parseInt(id_jadwal), kode_jadwal, kode_customer,
							username, alamat, nama_toko, Integer
									.parseInt(id_wilayah), status, date,
							checkin, checkout, "1",keterangan));
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

	public void saveAppDataJadwal(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_JADWAL, responsedata);
		editor.commit();
	}

	public void saveAppDataJadwalSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_SAME_DATA,
				responsedata);
		editor.commit();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showListJadwal() {
		jadwal_list.clear();
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_username = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);

		ArrayList<Jadwal> jadwal_from_db = databaseHandler
				.getAllJadwalWhereKodeStaffAndGroupBy(main_app_staff_username);
		if (jadwal_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < jadwal_from_db.size(); i++) {
				int id_jadwal = jadwal_from_db.get(i).getId_jadwal();
				String kode_jadwal = jadwal_from_db.get(i).getKode_jadwal();
				String kode_customer = jadwal_from_db.get(i).getKode_customer();
				String username = jadwal_from_db.get(i).getUsername();
				String alamat = jadwal_from_db.get(i).getAlamat();
				String nama_toko = jadwal_from_db.get(i).getNama_toko();
				int id_wilayah = jadwal_from_db.get(i).getId_wilayah();
				String status = jadwal_from_db.get(i).getStatus();
				String date = jadwal_from_db.get(i).getDate();
				String checkin = jadwal_from_db.get(i).getCheckin();
				String checkout = jadwal_from_db.get(i).getCheckout();
				String status_update = jadwal_from_db.get(i).getStatus_update();

				Jadwal jadwal = new Jadwal();
				jadwal.setId_jadwal(id_jadwal);
				jadwal.setKode_jadwal(kode_jadwal);
				jadwal.setKode_customer(kode_customer);
				jadwal.setUsername(username);
				jadwal.setAlamat(alamat);
				jadwal.setNama_toko(nama_toko);
				jadwal.setId_wilayah(id_wilayah);
				jadwal.setStatus(status);
				jadwal.setDate(date);
				jadwal.setCheckin(checkin);
				jadwal.setCheckout(checkout);
				jadwal.setStatus_update(status_update);
				jadwal_list.add(jadwal);
			}

			cAdapter = new ListViewAdapter(this, R.layout.gk_kpake_list_item_jadwal,
					jadwal_list);
			listview.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();
		} else {
			listview.setVisibility(View.INVISIBLE);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataJadwal().execute();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_jadwal_processing_empty);
				showCustomDialog(message);
			}
			return true;
		case R.id.menu_upload:
			if (GlobalApp.checkInternetConnection(act)) {
				int countUpload = databaseHandler
						.getCountJadwalNotYetCheckOut();
				if (countUpload == 0) {
					new UploadData().execute();
				} else {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_jadwal_processing_upload_failed_not_yet_check_out);
					showCustomDialog(message);
				}
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_customer_processing_upload_empty);
				showCustomDialog(message);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public HttpResponse uploadJadwal(final String url, final String checkin,
			final String checkout, final String id_jadwal, final String kode_jadwal, final String keterangan) {
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		HttpResponse response;
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("checkin", checkin));
		params.add(new BasicNameValuePair("checkout", checkout));
		params.add(new BasicNameValuePair("id_jadwal", id_jadwal));
		params.add(new BasicNameValuePair("kode_jadwal", kode_jadwal));
		params.add(new BasicNameValuePair("keterangan", keterangan));

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params));
			response = httpclient.execute(httppost);
		} catch (UnsupportedEncodingException e1) {
			response = null;
		} catch (IOException e) {
			response = null;
		}

		return response;
	}

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_jadwal_processing_upload));
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
			String upload_url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_JADWAL ;

			List<Jadwal> dataUpload = databaseHandler
					.getAllJadwalWhereAlreadyCheckOut();
			HttpResponse response = null;
			for (Jadwal jadwal : dataUpload) {
				response = uploadJadwal(
						upload_url, jadwal.getCheckin(),
						jadwal.getCheckout(),
						String.valueOf(jadwal.getId_jadwal()),
						String.valueOf(jadwal.getKode_jadwal()),
						String.valueOf(jadwal.getKeterangan()));
			}
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
				initUploadJadwal();
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.app_jadwal_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}

	}

	public void initUploadJadwal() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(R.string.app_jadwal_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_jadwal_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_jadwal_processing_upload_success);
					CustomDialogUploadSuccess(msg);
				}

			}

		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

		}
	}

	public void CustomDialogUploadSuccess(String msg) {
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
								//new DownloadDataJadwal().execute();
								//databaseHandler.deleteTableJadwal();
								showListJadwal();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public class ListViewAdapter extends ArrayAdapter<Jadwal> {
		Activity activity;
		int layoutResourceId;
		Jadwal jadwalData;
		ArrayList<Jadwal> data = new ArrayList<Jadwal>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<Jadwal> data) {
			super(act, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.activity = act;
			this.data = data;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			UserHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(activity);

				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
				holder.list_tanggal = (TextView) row
						.findViewById(R.id.jadwal_title_date);
				holder.list_jumlah_customer = (TextView) row
						.findViewById(R.id.jadwal_title_total_customer);
				holder.list_kode_jadwal = (TextView) row
						.findViewById(R.id.jadwal_title_kode_jadwal);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			jadwalData = data.get(position);
			SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");

			try {
				String inputString = jadwalData.getDate();
				String reformattedStr = myFormat.format(fromUser
						.parse(inputString));
				holder.list_tanggal.setText(reformattedStr);
			} catch (ParseException | java.text.ParseException e) {
				e.printStackTrace();
			}
			holder.list_kode_jadwal.setText(jadwalData.getKode_jadwal());
			int totalCustomer = databaseHandler
					.getCountJadwalGetTotalCustomerWhereKodeJadwal(jadwalData
							.getKode_jadwal());
			holder.list_jumlah_customer.setText(String.valueOf(totalCustomer));
			holder.list_jumlah_customer.setTypeface(typefaceSmall);
			holder.list_kode_jadwal.setTypeface(typefaceSmall);
			holder.list_tanggal.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String kode_jadwal = String.valueOf(data.get(position).getKode_jadwal());
					saveAppDataKodeJadwal(kode_jadwal);
					gotoJadwalCustomer();
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kode_jadwal;
			TextView list_tanggal;
			TextView list_jumlah_customer;
		}

	}

	public void saveAppDataKodeJadwal(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_KODE_JADWAL,
				responsedata);
		editor.commit();
	}

	public void gotoJadwalCustomer() {
		Intent i = new Intent(this, JadwalCustomerActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 1) {
				if (position == 0) {
					Intent intentActivity = new Intent(this,
							CustomerActivity.class);
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
				} else if (position == 4) {
					Intent intentActivity = new Intent(this,
							LocatorActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 5) {
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
				}else if (position == 9) {
					Intent intentActivity = new Intent(this,
							InventoryActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 10) {
					Intent intentActivity = new Intent(this,
							ReturActivity.class);
					startActivity(intentActivity);
					finish();
				}/*else if (position == 11) {
					Intent intentActivity = new Intent(this,
							LocatorStaffActivity.class);
					startActivity(intentActivity);
					finish();
				}*/else if (position == 11) {
					Intent intentActivity = new Intent(this,
							CheckCustomer.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 12) {
					Intent intentActivity = new Intent(this,
							CheckCustomerProspectActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 13) {
					Intent intentActivity = new Intent(this,
							RequestActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 14) {
					Intent intentActivity = new Intent(this,
							Kanvas_Uploaded.class);
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
		// String title = getApplicationContext().getResources().getString(
		// R.string.MSG_DLG_LABEL_TITLE_DIALOG);
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
