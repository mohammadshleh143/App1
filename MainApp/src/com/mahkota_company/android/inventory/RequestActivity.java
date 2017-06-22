package com.mahkota_company.android.inventory;

import android.app.Activity;
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
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.R;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.supervisor.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.ReqLoad;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class RequestActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<ReqLoad> req_load_list = new ArrayList<ReqLoad>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = RequestActivity.class
			.getSimpleName();
	private Button addSalesOrder;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvKodeSalesOrder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_req_load);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeSalesOrder = (TextView) findViewById(R.id.activity_sales_order_title_nomer_order);
		tvKodeCustomer = (TextView) findViewById(R.id.activity_sales_order_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_sales_order_title_nama_customer);
		tvKodeSalesOrder.setTypeface(typefaceSmall);
		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);

		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_customer_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		addSalesOrder = (Button) findViewById(R.id.activity_sales_order_btn_add);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(13);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		showReqLoad();
		addSalesOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences spPreferences = getSharedPrefereces();
				String main_app_staff_username = spPreferences.getString(
						CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
				if (main_app_staff_username.length() > 0) {


						gotoTambahReqLoad();

				}
			}
		});

	}

	public void gotoTambahReqLoad() {
		Intent i = new Intent(this, AddRequestActivity.class);
		startActivity(i);
		finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showReqLoad() {
		req_load_list.clear();
		ArrayList<ReqLoad> req_load_from_db = databaseHandler
				.getAllReqLoadGroupByNomerOrder();
		if (req_load_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < req_load_from_db.size(); i++) {
				int id_sales_order = req_load_from_db.get(i)
						.getId_sales_order();
				String nomer_request_load = req_load_from_db.get(i).getNomer_request_load();
				String date_order = req_load_from_db.get(i).getDate_order();
				String time_order = req_load_from_db.get(i).getTime_order();
				int id_promosi = req_load_from_db.get(i).getId_promosi();
				String username = req_load_from_db.get(i).getUsername();
				String satuan_terkecil = req_load_from_db.get(i).getSatuan_terkecil();
				String nama_product = req_load_from_db.get(i).getNama_product();
				String jumlah_order = req_load_from_db.get(i).getJumlah_order();
				String jumlah_order1 = req_load_from_db.get(i).getJumlah_order1();
				String jumlah_order2 = req_load_from_db.get(i).getJumlah_order2();
				String jumlah_order3 = req_load_from_db.get(i).getJumlah_order3();
				int id_staff = req_load_from_db.get(i).getId_staff();
				int id_product = req_load_from_db.get(i).getId_product();
				String ttd1 = req_load_from_db.get(i).getTtd1();

				ReqLoad reqLoad = new ReqLoad();
				reqLoad.setId_sales_order(id_sales_order);
				reqLoad.setNomer_request_load(nomer_request_load);
				reqLoad.setDate_order(date_order);
				reqLoad.setTime_order(time_order);
				reqLoad.setId_promosi(id_promosi);
				reqLoad.setUsername(username);
				reqLoad.setSatuan_terkecil(satuan_terkecil);
				reqLoad.setNama_product(nama_product);
				reqLoad.setJumlah_order(jumlah_order);
				reqLoad.setJumlah_order1(jumlah_order1);
				reqLoad.setJumlah_order2(jumlah_order2);
				reqLoad.setJumlah_order3(jumlah_order3);
				reqLoad.setId_staff(id_staff);
				reqLoad.setId_product(id_product);
				reqLoad.setTtd1(ttd1);
				req_load_list.add(reqLoad);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_sales_order, req_load_list);
			listview.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();
		} else {
			//listview.setVisibility(View.INVISIBLE);
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
		MenuItem item = menu.findItem(R.id.menu_refresh);
		if (item != null) {
			item.setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_upload:
			if (GlobalApp.checkInternetConnection(act)) {
				int countUpload = databaseHandler.getCountReqLoad();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_reqload_processing_upload_no_data);
					showCustomDialog(message);
				} else {
					new UploadData().execute();
				}
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_sales_order_processing_upload_empty);
				showCustomDialog(message);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_reqload_processing_upload));
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
			if (mWifi.isConnected()){
				String url_add_req_load = "http://186.132.0.58:8081/"
						+ CONFIG.CONFIG_APP_URL_UPLOAD_REQ_LOAD;
				List<ReqLoad> dataReqLoad = databaseHandler
						.getAllReqLoad();
				for (ReqLoad reqLoad : dataReqLoad) {
					if (reqLoad.getId_promosi() == -1) {
						response_data = uploadReqLoad(url_add_req_load,
								reqLoad.getNomer_request_load(),
								reqLoad.getDate_order(),
								reqLoad.getTime_order(),
								String.valueOf("0"),
								reqLoad.getUsername(),
								reqLoad.getSatuan_terkecil(),
								reqLoad.getNama_product(),
								String.valueOf(reqLoad.getJumlah_order()),
								String.valueOf(reqLoad.getJumlah_order1()),
								String.valueOf(reqLoad.getJumlah_order2()),
								String.valueOf(reqLoad.getJumlah_order3()),
								String.valueOf(reqLoad.getId_staff()),
								String.valueOf(reqLoad.getId_product()),
								String.valueOf(reqLoad.getTtd1()));

					} else {
						response_data = uploadReqLoad(url_add_req_load,
								reqLoad.getNomer_request_load(),
								reqLoad.getDate_order(),
								reqLoad.getTime_order(),
								String.valueOf(reqLoad.getId_promosi()),
								reqLoad.getUsername(),
								reqLoad.getSatuan_terkecil(),
								reqLoad.getNama_product(),
								String.valueOf(reqLoad.getJumlah_order()),
								String.valueOf(reqLoad.getJumlah_order1()),
								String.valueOf(reqLoad.getJumlah_order2()),
								String.valueOf(reqLoad.getJumlah_order3()),
								String.valueOf(reqLoad.getId_staff()),
								String.valueOf(reqLoad.getId_product()),
								String.valueOf(reqLoad.getTtd1()));
					}
				}
				return null;
			}else{
				String url_add_req_load = CONFIG.CONFIG_APP_URL_PUBLIC
						+ CONFIG.CONFIG_APP_URL_UPLOAD_REQ_LOAD;
				List<ReqLoad> dataReqLoad = databaseHandler
						.getAllReqLoad();
				for (ReqLoad reqLoad : dataReqLoad) {
					if (reqLoad.getId_promosi() == -1) {
						response_data = uploadReqLoad(url_add_req_load,
								reqLoad.getNomer_request_load(),
								reqLoad.getDate_order(),
								reqLoad.getTime_order(),
								String.valueOf("0"),
								reqLoad.getUsername(),
								reqLoad.getSatuan_terkecil(),
								reqLoad.getNama_product(),
								String.valueOf(reqLoad.getJumlah_order()),
								String.valueOf(reqLoad.getJumlah_order1()),
								String.valueOf(reqLoad.getJumlah_order2()),
								String.valueOf(reqLoad.getJumlah_order3()),
								String.valueOf(reqLoad.getId_staff()),
								String.valueOf(reqLoad.getId_product()),
								String.valueOf(reqLoad.getTtd1()));

					} else {
						response_data = uploadReqLoad(url_add_req_load,
								reqLoad.getNomer_request_load(),
								reqLoad.getDate_order(),
								reqLoad.getTime_order(),
								String.valueOf(reqLoad.getId_promosi()),
								reqLoad.getUsername(),
								reqLoad.getSatuan_terkecil(),
								reqLoad.getNama_product(),
								String.valueOf(reqLoad.getJumlah_order()),
								String.valueOf(reqLoad.getJumlah_order1()),
								String.valueOf(reqLoad.getJumlah_order2()),
								String.valueOf(reqLoad.getJumlah_order3()),
								String.valueOf(reqLoad.getId_staff()),
								String.valueOf(reqLoad.getId_product()),
								String.valueOf(reqLoad.getTtd1()));
					}
				}
				return null;
			}

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<ReqLoad> dataAddReqLoad = databaseHandler
					.getAllReqLoad();
			int countData = dataAddReqLoad.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataAddReqLoad.size() * 1000 * 3);
				} catch (final InterruptedException e) {
					Log.d(LOG_TAG, "InterruptedException " + e.getMessage());
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(e.getMessage());
						}
					});
				}
				if (response_data != null && response_data.length() > 0) {
					if (response_data.startsWith("Error occurred")) {
						final String msg = act
								.getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_order_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadReqLoad();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_reqload_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	private String uploadReqLoad(final String url,
			final String nomer_request_load,
			final String date_order,
			final String time_order,
			final String id_promosi,
			final String username,
			final String satuan_terkecil,
			final String nama_product,
			final String jumlah_order,
			final String jumlah_order1,
			final String jumlah_order2,
			final String jumlah_order3,
			final String id_staff,
			final String id_product,
			final String ttd1) {

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

			File sourceTtd1 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_REQ_LOAD + "/" + ttd1);
			if (sourceTtd1.exists() && ttd1 != null)
				entity.addPart("ttd_1", new FileBody(sourceTtd1));

			entity.addPart("nomer_request_load", new StringBody(nomer_request_load));
			entity.addPart("date_order", new StringBody(date_order));
			entity.addPart("time_order", new StringBody(time_order));
			entity.addPart("id_promosi", new StringBody(id_promosi));
			entity.addPart("username", new StringBody(username));
			entity.addPart("satuan_terkecil", new StringBody(satuan_terkecil));
			entity.addPart("nama_product", new StringBody(nama_product));
			entity.addPart("jumlah_order", new StringBody(jumlah_order));
			entity.addPart("jumlah_order1", new StringBody(jumlah_order1));
			entity.addPart("jumlah_order2", new StringBody(jumlah_order2));
			entity.addPart("jumlah_order3", new StringBody(jumlah_order3));
			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("ttd1", new StringBody(ttd1));

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

	public void initUploadReqLoad() {
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
								R.string.app_reqload_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_reqload_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_reqload_processing_upload_success);
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
								databaseHandler.deleteTableReqLoad();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void saveAppDataSalesOrderNomerSalesOrder(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_SALES_ORDER_NOMER_ORDER,
				responsedata);
		editor.commit();
	}

	public class ListViewAdapter extends ArrayAdapter<ReqLoad> {
		Activity activity;
		int layoutResourceId;
		ReqLoad reqLoadData;
		ArrayList<ReqLoad> data = new ArrayList<ReqLoad>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<ReqLoad> data) {
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
				holder.list_kodeCustomer = (TextView) row
						.findViewById(R.id.sales_order_title_kode_customer);
				holder.list_namaCustomer = (TextView) row
						.findViewById(R.id.sales_order_title_nama_customer);
				holder.list_kode_sales_order = (TextView) row
						.findViewById(R.id.sales_order_title_kode_sales_order);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			reqLoadData = data.get(position);
			holder.list_kodeCustomer.setText(reqLoadData.getDate_order());
			holder.list_namaCustomer.setText(reqLoadData.getTime_order());
			holder.list_kode_sales_order.setText(reqLoadData.getNomer_request_load());

			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_kode_sales_order.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String nomerReqLoad = String.valueOf(data.get(position)
							.getNomer_request_load());
					showEditDeleteDialog(nomerReqLoad);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_kode_sales_order;
		}

	}

	public void gotoDetailSalesOrder() {
		//Intent i = new Intent(this, DetailSalesOrderActivity.class);
		//startActivity(i);
		//finish();
	}

	// show edit delete dialog
	public void showEditDeleteDialog(final String nomerReqLoad) {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_DATA_DELETE_DIALOG);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(true)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_DELETE),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								databaseHandler.deleteTableReqloadWhereNomerreqload(nomerReqLoad);
								gotoSalesOrder();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NHAPUS),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//gotoSalesOrder();
								dialog.dismiss();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void gotoSalesOrder() {
		Intent i = new Intent(this, RequestActivity.class);
		startActivity(i);
		finish();
	}

	// Show Succeed Delete Item dialog
	public void showSucceedDeleteDialog() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_DELETE_ITEM_SUCCESS);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 13) {
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
				}/* else if (position == 4) {
					Intent intentActivity = new Intent(this,
							LocatorStaffActivity.class);
					startActivity(intentActivity);
					finish();
				}*/else if (position == 5) {
					Intent intentActivity = new Intent(this,
							SalesOrderActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 6) {
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
				}/*else if (position == 11) {
					Intent intentActivity = new Intent(this,
							CustomerMerchandiseActivity.class);
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
				}else if (position == 14) {
					Intent intentActivity = new Intent(this,
							Kanvas_Uploaded.class);
					startActivity(intentActivity);
					finish();
				}
			}
		}

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
