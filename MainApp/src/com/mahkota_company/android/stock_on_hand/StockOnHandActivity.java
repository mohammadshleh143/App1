package com.mahkota_company.android.stock_on_hand;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.contact.ContactActivty;
import com.mahkota_company.android.contact.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Request_load;
import com.mahkota_company.android.database.StockOnHand;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.InventoryActivity;

import com.mahkota_company.android.inventory.RequestActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.merchandise.CustomerMerchandiseActivity;
import com.mahkota_company.android.merchandise.LocatorStaffActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.R;

@SuppressWarnings("deprecation")
public class StockOnHandActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<StockOnHand> stockOnHand_list = new ArrayList<StockOnHand>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = StockOnHandActivity.class
			.getSimpleName();
	private Button addSalesOrder;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvKodeStockOnHand;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_stock_on_hand);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeStockOnHand = (TextView) findViewById(R.id.activity_sales_order_title_nomer_order);
		tvKodeCustomer = (TextView) findViewById(R.id.activity_sales_order_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_sales_order_title_nama_customer);
		tvKodeStockOnHand.setTypeface(typefaceSmall);
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
		mNavigationDrawerFragment.selectItem(6);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		showStckOnHand();
		addSalesOrder.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences spPreferences = getSharedPrefereces();
				String main_app_staff_username = spPreferences.getString(
						CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
				if (main_app_staff_username.length() > 0) {
					List<Jadwal> dataJadwal = databaseHandler
							.getAllJadwalWhereKodeStaffAndGroupByCustomer(main_app_staff_username);
					List<Customer> dataCustomer = new ArrayList<Customer>();
					for (Jadwal jadwal : dataJadwal) {
						Customer customer = new Customer(0, jadwal
								.getKode_customer(), null, null, null, null,
								jadwal.getNama_toko(), null, jadwal
										.getId_wilayah(), null, null, null, 0,
								null, null, null, 0, null, null, null, null, null, null, null,
								0, null, null, null,null, null, null, null, null, null, null,null,
								0,null,null,null,null,null);
						dataCustomer.add(customer);

					}

					boolean containCustomer = false;
					for (Customer customer : dataCustomer) {
						int countJadwal = databaseHandler
								.getCountStockOnHand(customer
										.getKode_customer());
						if (countJadwal == 0) {
							containCustomer = true;
							break;
						}
					}
					if (containCustomer) {
						gotoTambahStockOnHand();
					} else {
						String msg = getApplicationContext().getResources()
								.getString(
										R.string.app_sales_order_failed_to_add);
						showCustomDialog(msg);
					}

				}
			}
		});

	}

	public void gotoTambahStockOnHand() {
		Intent i = new Intent(this, AddStockOnHandActivity.class);
		startActivity(i);
		finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showStckOnHand() {
		stockOnHand_list.clear();
		ArrayList<StockOnHand> stock_on_hand_from_db = databaseHandler
				.getAllStockOnHandGroupByNomerOrder();
		if (stock_on_hand_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < stock_on_hand_from_db.size(); i++) {
				int id_stock_on_hand = stock_on_hand_from_db.get(i)
						.getId_stock_on_hand();
				String nomer_stock = stock_on_hand_from_db.get(i)
						.getNomer_stock_on_hand();
				String nomer_stock_detail = stock_on_hand_from_db.get(i)
						.getNomer_stock_on_hand_detail();
				String date_stock_on_hand = stock_on_hand_from_db.get(i)
						.getDate_stock_on_hand();
				String time_stock_on_hand = stock_on_hand_from_db.get(i)
						.getTime_stock_on_hand();
				String deskripsi = stock_on_hand_from_db.get(i).getDeskripsi();
				String username = stock_on_hand_from_db.get(i).getUsername();
				String kode_customer = stock_on_hand_from_db.get(i)
						.getKode_customer();
				String alamat = stock_on_hand_from_db.get(i).getAlamat();
				String nama_lengkap = stock_on_hand_from_db.get(i)
						.getNama_lengkap();
				String nama_product = stock_on_hand_from_db.get(i)
						.getNama_product();
				String kode_product = stock_on_hand_from_db.get(i)
						.getKode_product();
				String harga_jual = stock_on_hand_from_db.get(i)
						.getHarga_jual();
				String stockpcs = stock_on_hand_from_db.get(i).getStockpcs();
				String stockrcg = stock_on_hand_from_db.get(i).getStockrcg();
				String stockpck = stock_on_hand_from_db.get(i).getStockpck();
				String stockdus = stock_on_hand_from_db.get(i).getStockdus();

				StockOnHand stockOnHand = new StockOnHand();
				stockOnHand.setId_stock_on_hand(id_stock_on_hand);
				stockOnHand.setNomer_stock_on_hand(nomer_stock);
				stockOnHand.setNomer_stock_on_hand_detail(nomer_stock_detail);
				stockOnHand.setDate_stock_on_hand(date_stock_on_hand);
				stockOnHand.setTime_stock_on_hand(time_stock_on_hand);
				stockOnHand.setDeskripsi(deskripsi);
				stockOnHand.setUsername(username);
				stockOnHand.setKode_customer(kode_customer);
				stockOnHand.setAlamat(alamat);
				stockOnHand.setNama_lengkap(nama_lengkap);
				stockOnHand.setNama_product(nama_product);
				stockOnHand.setKode_product(kode_product);
				stockOnHand.setHarga_jual(harga_jual);
				stockOnHand.setStockpcs(stockpcs);
				stockOnHand.setStockrcg(stockrcg);
				stockOnHand.setStockpck(stockpck);
				stockOnHand.setStockdus(stockdus);
				stockOnHand_list.add(stockOnHand);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_stok_on_hand, stockOnHand_list);
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
				int countUpload = databaseHandler.getCountStockOnHand();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_stock_on_hand_processing_upload_no_data);
					showCustomDialog(message);
				} else {
					new UploadData().execute();
				}
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_stock_on_hand_processing_upload_empty);
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
					.getString(R.string.app_stock_on_hand_processing_upload));
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
			String url_add_stock = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_STOCK_ON_HAND;

			List<StockOnHand> dataAddPhotoUpload = databaseHandler
					.getAllStockOnHand();
			for (StockOnHand stockOnHand : dataAddPhotoUpload) {
				response_data = uploadStockOnHand(url_add_stock,
						stockOnHand.getNomer_stock_on_hand(),
						stockOnHand.getNomer_stock_on_hand_detail(),
						stockOnHand.getDate_stock_on_hand(),
						stockOnHand.getTime_stock_on_hand(),
						stockOnHand.getDeskripsi(), stockOnHand.getUsername(),
						stockOnHand.getKode_customer(),
						stockOnHand.getAlamat(), stockOnHand.getNama_lengkap(),
						stockOnHand.getNama_product(),
						stockOnHand.getKode_product(),
						String.valueOf(stockOnHand.getHarga_jual()),
						String.valueOf(stockOnHand.getStockpcs()),
						String.valueOf(stockOnHand.getStockrcg()),
						String.valueOf(stockOnHand.getStockpck()),
						String.valueOf(stockOnHand.getStockdus()));

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<StockOnHand> dataAddStockOnHand = databaseHandler
					.getAllStockOnHand();
			int countData = dataAddStockOnHand.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataAddStockOnHand.size() * 1000 * 3);
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
										R.string.app_stock_on_hand_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initStockOnHand();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_stock_on_hand_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	private String uploadStockOnHand(final String url,
			final String nomer_stock_on_hand,
			final String nomer_stock_on_hand_detail,
			final String date_stock_on_hand, final String time_stock_on_hand,
			final String deskripsi, final String username,
			final String kode_customer, final String alamat,
			final String nama_lengkap, final String nama_product,
			final String kode_product, final String harga_jual,
			final String stockpcs, final String stockrcg,
			final String stockpck,final String stockdus) {
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
			entity.addPart("nomer_stock_on_hand", new StringBody(
					nomer_stock_on_hand));
			entity.addPart("nomer_stock_on_hand_detail", new StringBody(
					nomer_stock_on_hand_detail));
			entity.addPart("date_stock_on_hand", new StringBody(
					date_stock_on_hand));
			entity.addPart("time_stock_on_hand", new StringBody(
					time_stock_on_hand));
			entity.addPart("deskripsi", new StringBody(deskripsi));
			entity.addPart("username", new StringBody(username));
			entity.addPart("kode_customer", new StringBody(kode_customer));
			entity.addPart("alamat", new StringBody(alamat));
			entity.addPart("nama_lengkap", new StringBody(nama_lengkap));
			entity.addPart("nama_product", new StringBody(nama_product));
			entity.addPart("kode_product", new StringBody(kode_product));
			entity.addPart("harga_jual", new StringBody(harga_jual));
			entity.addPart("stockpcs", new StringBody(stockpcs));
			entity.addPart("stockrcg", new StringBody(stockrcg));
			entity.addPart("stockpck", new StringBody(stockpck));
			entity.addPart("stockdus", new StringBody(stockdus));

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

	public void initStockOnHand() {
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
								R.string.app_stock_on_hand_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_stock_on_hand_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_stock_on_hand_processing_upload_success);
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
								databaseHandler.deleteTableStockOnHand();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void saveAppDataStockOnHandNomerOrderStockOnHand(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_STOCK_ON_HAND_NOMER_ORDER_STOCK_ON_HAND,
				responsedata);
		editor.commit();
	}

	public class ListViewAdapter extends ArrayAdapter<StockOnHand> {
		Activity activity;
		int layoutResourceId;
		StockOnHand salesOrderData;
		ArrayList<StockOnHand> data = new ArrayList<StockOnHand>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<StockOnHand> data) {
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
				holder.list_kode_stock_on_hand = (TextView) row
						.findViewById(R.id.sales_order_title_kode_sales_order);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			salesOrderData = data.get(position);
			holder.list_kodeCustomer.setText(salesOrderData.getKode_customer());
			holder.list_namaCustomer.setText(salesOrderData.getNama_lengkap());
			holder.list_kode_stock_on_hand.setText(salesOrderData
					.getNomer_stock_on_hand());

			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_kode_stock_on_hand.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String nomerOrder = String.valueOf(data.get(position)
							.getNomer_stock_on_hand());
					showEditDeleteDialog(nomerOrder);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_kode_stock_on_hand;
		}

	}

	public void gotoDetailStockOnHand() {
		Intent i = new Intent(this, DetailStockOnHandActivity.class);
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
								R.string.MSG_DLG_LABEL_EDIT),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								saveAppDataStockOnHandNomerOrderStockOnHand(nomer_order);
								gotoDetailStockOnHand();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_DELETE),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								databaseHandler
										.deleteTableStockOnHandWhereNomerOrder(nomer_order);
								showSucceedDeleteDialog();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 6) {
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
				} else if (position == 5) {
					Intent intentActivity = new Intent(this,
							SalesOrderActivity.class);
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
