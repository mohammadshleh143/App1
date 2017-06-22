package com.mahkota_company.android.inventory;

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
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.R;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Penjualan;
import com.mahkota_company.android.database.PenjualanDetail;
import com.mahkota_company.android.database.SalesOrder;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.AddSalesOrderActivity;
import com.mahkota_company.android.sales_order.DetailSalesOrderActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.supervisor.SuperVisor;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class Kanvas_Uploaded extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<Penjualan> penjualan_list = new ArrayList<Penjualan>();
	private ArrayList<PenjualanDetail> penjualan_detail_list = new ArrayList<PenjualanDetail>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private String response_data1;
	private static final String LOG_TAG = Kanvas_Uploaded.class
			.getSimpleName();
	private Button addKanvas;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvKodeKanvas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_kanvas_uploaded);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeKanvas= (TextView) findViewById(R.id.activity_sales_order_title_nomer_order);
		tvKodeCustomer = (TextView) findViewById(R.id.activity_sales_order_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_sales_order_title_nama_customer);
		tvKodeKanvas.setTypeface(typefaceSmall);
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
		addKanvas = (Button) findViewById(R.id.activity_sales_order_btn_add);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(14);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		showKanvas();
		addKanvas.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences spPreferences = getSharedPrefereces();
				String main_app_staff_username = spPreferences.getString(
						CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
				if (main_app_staff_username.length() > 0) {
					gotoKanvas();
				}
			}
		});

	}

	public void gotoKanvas() {
		Intent i = new Intent(this, SalesKanvasActivity.class);
		startActivity(i);
		finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showKanvas() {
		penjualan_list.clear();
		ArrayList<Penjualan> kanvas_from_db = databaseHandler
				.getAllPenjualan();
		ArrayList<PenjualanDetail> kanvas_detail_from_db = databaseHandler
				.getAllPenjualanDetail();
		if (kanvas_from_db.size() > 0 && kanvas_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < kanvas_from_db.size(); i++) {
				int id_penjualan= kanvas_from_db.get(i)
						.getId_penjualan();
				int id_staff = kanvas_from_db.get(i)
						.getId_staff();
				int type_penjualan = kanvas_from_db.get(i)
						.getType_penjualan();
				String no_penjualan = kanvas_from_db.get(i)
						.getNo_penjualan();
				String nama_customer = kanvas_from_db.get(i)
						.getNama_customer();
				String alamat = kanvas_from_db.get(i)
						.getAlamat();
				String keterangan = kanvas_from_db.get(i)
						.getKeterangan();
				String lats = kanvas_from_db.get(i)
						.getLats();
				String longs = kanvas_from_db.get(i)
						.getLongs();
				String foto1 = kanvas_from_db.get(i)
						.getFoto1();
				String ttd = kanvas_from_db.get(i)
						.getTtd();

				Penjualan penjualan= new Penjualan();
				penjualan.setId_penjualan(id_penjualan);
				penjualan.setId_staff(id_staff);
				penjualan.setType_penjualan(type_penjualan);
				penjualan.setNo_penjualan(no_penjualan);
				penjualan.setNama_customer(nama_customer);
				penjualan.setAlamat(alamat);
				penjualan.setKeterangan(keterangan);
				penjualan.setLats(lats);
				penjualan.setLongs(longs);
				penjualan.setFoto1(foto1);
				penjualan.setTtd(ttd);
				penjualan_list.add(penjualan);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_penjualan, penjualan_list);
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
				int countUpload = databaseHandler.getAllCountPenjualan();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_kanvas_processing_upload_no_data);
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
					.getString(R.string.app_kanvas_processing_upload));
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
			String url_add_kanvas = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_SALES_KANVAS;
			String url_add_kanvas_detail = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_SALES_KANVAS_DETAIL;

			List<Penjualan> dataPenjualan= databaseHandler
					.getAllPenjualan();
			List<PenjualanDetail> dataPenjualandetail= databaseHandler
					.getAllPenjualanDetail();
			for (Penjualan penjualan : dataPenjualan) {
					response_data = uploadPenjualan(url_add_kanvas,
							String.valueOf(penjualan.getId_penjualan()),
							String.valueOf(penjualan.getId_staff()),
							String.valueOf(penjualan.getType_penjualan()),
							penjualan.getNo_penjualan(),
							penjualan.getNama_customer(),
							penjualan.getAlamat(),
							penjualan.getKeterangan(),
							penjualan.getLats(),
							penjualan.getLongs(),
							penjualan.getFoto1(),
							penjualan.getTtd());
			}

			for (PenjualanDetail penjualanDetail : dataPenjualandetail) {
					response_data1 = uploadPenjualan1(url_add_kanvas_detail,
							String.valueOf(penjualanDetail.getId_penjualan_detail()),
							penjualanDetail.getNo_penjualan(),
							penjualanDetail.getId_product(),
							penjualanDetail.getHarga(),
							penjualanDetail.getHarga_jual(),
							penjualanDetail.getNomer_request_load(),
							penjualanDetail.getId_staff(),
							penjualanDetail.getJumlah(),
							penjualanDetail.getJumlah1(),
							penjualanDetail.getJumlah2(),
							penjualanDetail.getJumlah3());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<Penjualan> dataAddPenjualan = databaseHandler
					.getAllPenjualan();
			int countData = dataAddPenjualan.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataAddPenjualan.size() * 1000 * 3);
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
								initUploadPenjualan();
								initUploadPenjualan1();
							}
						});
					}
				}
			} else {
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
			}
		}
	}

	private String uploadPenjualan(
			final String url,
			final String id_penjualan,
			final String id_staff,
			final String type_penjualan,
			final String no_penjualan,
			final String nama_customer,
			final String alamat,
			final String keterangan,
			final String lats,
			final String longs,
			final String foto1,
			final String ttd) {

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
			entity.addPart("type_penjualan", new StringBody(type_penjualan));
			entity.addPart("no_penjualan", new StringBody(no_penjualan));
			entity.addPart("nama_customer", new StringBody(nama_customer));
			entity.addPart("alamat", new StringBody(alamat));
			entity.addPart("keterangan", new StringBody(keterangan));
			entity.addPart("lats", new StringBody(lats));
			entity.addPart("longs", new StringBody(longs));
			entity.addPart("foto1", new StringBody(foto1));
			entity.addPart("ttd", new StringBody(ttd));

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

	private String uploadPenjualan1(
			final String url,
			final String id_penjualan_detail,
			final String no_penjualan,
			final String id_product,
			final String harga,
			final String harga_jual,
			final String nomer_request_load,
			final String id_staff,
			final String jumlah,
			final String jumlah1,
			final String jumlah2,
			final String jumlah3) {

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

			entity.addPart("no_penjualan", new StringBody(no_penjualan));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("harga", new StringBody(harga));
			entity.addPart("harga_jual", new StringBody(harga_jual));
			entity.addPart("nomer_request_load", new StringBody(nomer_request_load));
			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("jumlah", new StringBody(jumlah));
			entity.addPart("jumlah1", new StringBody(jumlah1));
			entity.addPart("jumlah2", new StringBody(jumlah2));
			entity.addPart("jumlah3", new StringBody(jumlah3));

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

	public void initUploadPenjualan() {
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
								R.string.app_sales_order_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_KANVAS_processing_upload_success);
					CustomDialogUploadSuccess(msg);
				}

			}

		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

		}
	}

	public void initUploadPenjualan1() {
		JSONObject oResponse;
		try {
			oResponse = new JSONObject(response_data1);
			String status = oResponse.isNull("error") ? "True" : oResponse
					.getString("error");
			if (response_data1.isEmpty()) {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_sales_order_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_success);
					CustomDialogUploadSuccess1(msg);
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
								//databaseHandler.deleteAllTablePenjualan();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void CustomDialogUploadSuccess1(String msg) {
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
								databaseHandler.deleteAllTablePenjualan();
								databaseHandler.deleteAllTablePenjualanDetail();
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

	public class ListViewAdapter extends ArrayAdapter<Penjualan> {
		Activity activity;
		int layoutResourceId;
		Penjualan penjualanData;
		ArrayList<Penjualan> data = new ArrayList<Penjualan>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<Penjualan> data) {
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
			penjualanData = data.get(position);
			holder.list_kodeCustomer.setText(penjualanData.getNama_customer());
			holder.list_namaCustomer.setText(penjualanData.getNo_penjualan());
			holder.list_kode_sales_order.setText(penjualanData.getAlamat());

			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_kode_sales_order.setTypeface(typefaceSmall);
			/*
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String nomerOrder = String.valueOf(data.get(position).getNo_penjualan());
					showEditDeleteDialog(nomerOrder);
				}
			});
			*/
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_kode_sales_order;
		}

	}

	public void gotoDetailSalesOrder() {
		Intent i = new Intent(this, DetailSalesOrderActivity.class);
		startActivity(i);
		finish();
	}

	// show edit delete dialog
	public void showEditDeleteDialog(final String nomer_order) {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_DATA_EDIT_DELETE_DIALOG);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(true)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_HAPUS),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								saveAppDataSalesOrderNomerSalesOrder(nomer_order);
								gotoDetailSalesOrder();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NHAPUS),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								gotoSalesOrder();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void gotoSalesOrder() {
		Intent i = new Intent(this, Kanvas_Uploaded.class);
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
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 14) {
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
				} else if (position == 4) {
					Intent intentActivity = new Intent(this,
							LocatorActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
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
