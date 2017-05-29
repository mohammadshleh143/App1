package com.mahkota_company.android.retur;

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
import com.mahkota_company.android.contact.ContactActivty;
import com.mahkota_company.android.contact.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Retur;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.InventoryActivity;
import com.mahkota_company.android.inventory.RequestActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.merchandise.CustomerMerchandiseActivity;
import com.mahkota_company.android.merchandise.LocatorStaffActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
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
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class ReturActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<Retur> retur_list = new ArrayList<Retur>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = ReturActivity.class
			.getSimpleName();
	private Button addRetur;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvKodeRetur;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_retur);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeRetur = (TextView) findViewById(R.id.activity_sales_order_title_nomer_order);
		tvKodeCustomer = (TextView) findViewById(R.id.activity_sales_order_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_sales_order_title_nama_customer);
		tvKodeRetur.setTypeface(typefaceSmall);
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
		addRetur = (Button) findViewById(R.id.activity_sales_order_btn_add);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(10);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		showRetur();
		addRetur.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences spPreferences = getSharedPrefereces();
				String main_app_staff_username = spPreferences.getString(
						CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
				if (main_app_staff_username.length() > 0) {
					int countData = databaseHandler.getCountJadwal();
					if (countData == 0) {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_order_no_data_jadwal);
						showCustomDialog(msg);
					} else {
						List<Jadwal> dataJadwal = databaseHandler
								.getAllJadwalWhereKodeStaffAndGroupByCustomer(main_app_staff_username);
						List<Customer> dataCustomer = new ArrayList<Customer>();
						for (Jadwal jadwal : dataJadwal) {
							Customer customer = new Customer(0, jadwal
									.getKode_customer(), null, null, null,
									null, jadwal.getNama_toko(), null,
									jadwal.getId_wilayah(), null, null, null,
									0, null, null, null, 0, null, null, null,
									null, null, null, null, 0, null, null, null,
									null, null, null, null, null, null, null,null,
									0,null,null,null,null,null);
							dataCustomer.add(customer);
						}

						boolean containCustomer = false;
						for (Customer customer : dataCustomer) {
							int countJadwal = databaseHandler
									.getCountRetur(customer
											.getKode_customer());
							if (countJadwal == 0) {
								containCustomer = true;
								break;
							}
						}
						if (containCustomer) {
							gotoTambahRetur();
						} else {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_to_add);
							showCustomDialog(msg);
						}
					}
				}
			}
		});

	}

	public void gotoTambahRetur() {
		Intent i = new Intent(this, AddReturActivity.class);
		startActivity(i);
		finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showRetur() {
		retur_list.clear();
		ArrayList<Retur> retur_from_db = databaseHandler
				.getAllReturGroupByNomerOrder();
		if (retur_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < retur_from_db.size(); i++) {
				int id_retur = retur_from_db.get(i)
						.getId_retur();
				String nomer_retur = retur_from_db.get(i)
						.getNomer_retur();
				String nomer_retur_detail = retur_from_db.get(i)
						.getNomer_retur_detail();
				String date_retur = retur_from_db.get(i).getDate_retur();
				String time_retur = retur_from_db.get(i).getTime_retur();
				String deskripsi = retur_from_db.get(i).getDeskripsi();
				int id_promosi = retur_from_db.get(i).getId_promosi();
				String username = retur_from_db.get(i).getUsername();
				String kode_customer = retur_from_db.get(i)
						.getKode_customer();
				String alamat = retur_from_db.get(i).getAlamat();
				String nama_lengkap = retur_from_db.get(i)
						.getNama_lengkap();
				String nama_product = retur_from_db.get(i)
						.getNama_product();
				String kode_product = retur_from_db.get(i)
						.getKode_product();
				String harga_jual = retur_from_db.get(i).getHarga_jual();
				String jumlah_retur = retur_from_db.get(i)
						.getJumlah_retur();

				Retur retur = new Retur();
				retur.setId_retur(id_retur);
				retur.setNomer_retur(nomer_retur);
				retur.setNomer_retur_detail(nomer_retur_detail);
				retur.setDate_retur(date_retur);
				retur.setTime_retur(time_retur);
				retur.setDeskripsi(deskripsi);
				retur.setId_promosi(id_promosi);
				retur.setUsername(username);
				retur.setKode_customer(kode_customer);
				retur.setAlamat(alamat);
				retur.setNama_lengkap(nama_lengkap);
				retur.setNama_product(nama_product);
				retur.setKode_product(kode_product);
				retur.setHarga_jual(harga_jual);
				retur.setJumlah_retur(jumlah_retur);
				retur_list.add(retur);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_retur, retur_list);
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
				int countUpload = databaseHandler.getCountRetur();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_no_data);
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
					.getString(R.string.app_sales_order_processing_upload));
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
			String url_add_retur = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_RETUR;

			List<Retur> dataRetur = databaseHandler
					.getAllRetur();
			for (Retur retur : dataRetur) {
				if (retur.getId_promosi() == -1) {
					response_data = uploadRetur(url_add_retur,
							retur.getNomer_retur(),
							retur.getNomer_retur_detail(),
							retur.getDate_retur(),
							retur.getTime_retur(),
							retur.getDeskripsi(), String.valueOf("0"),
							retur.getUsername(),
							retur.getKode_customer(),
							retur.getAlamat(),
							retur.getNama_lengkap(),
							retur.getNama_product(),
							retur.getKode_product(),
							String.valueOf(retur.getHarga_jual()),
							String.valueOf(retur.getJumlah_retur()));
				} else {
					response_data = uploadRetur(url_add_retur,
							retur.getNomer_retur(),
							retur.getNomer_retur_detail(),
							retur.getDate_retur(),
							retur.getTime_retur(),
							retur.getDeskripsi(),
							String.valueOf(retur.getId_promosi()),
							retur.getUsername(),
							retur.getKode_customer(),
							retur.getAlamat(),
							retur.getNama_lengkap(),
							retur.getNama_product(),
							retur.getKode_product(),
							String.valueOf(retur.getHarga_jual()),
							String.valueOf(retur.getJumlah_retur()));
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<Retur> dataAddRetur = databaseHandler
					.getAllRetur();
			int countData = dataAddRetur.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataAddRetur.size() * 1000 * 3);
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
										R.string.app_retur_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadRetur();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_retur_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	private String uploadRetur(final String url, final String nomer_retur,
			final String nomer_retur_detail, final String date_retur,
			final String time_retur, final String deskripsi,
			final String id_promosi, final String username,
			final String kode_customer, final String alamat,
			final String nama_lengkap, final String nama_product,
			final String kode_product, final String harga_jual,
			final String jumlah_retur) {

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

			entity.addPart("nomer_retur", new StringBody(nomer_retur));
			entity.addPart("nomer_retur_detail", new StringBody(
					nomer_retur_detail));
			entity.addPart("date_retur", new StringBody(date_retur));
			entity.addPart("time_retur", new StringBody(time_retur));
			entity.addPart("deskripsi", new StringBody(deskripsi));
			entity.addPart("id_promosi", new StringBody(id_promosi));
			entity.addPart("username", new StringBody(username));
			entity.addPart("kode_customer", new StringBody(kode_customer));
			entity.addPart("alamat", new StringBody(alamat));
			entity.addPart("nama_lengkap", new StringBody(nama_lengkap));
			entity.addPart("nama_product", new StringBody(nama_product));
			entity.addPart("kode_product", new StringBody(kode_product));
			entity.addPart("harga_jual", new StringBody(harga_jual));
			entity.addPart("jumlah_retur", new StringBody(jumlah_retur));

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

	public void initUploadRetur() {
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
								R.string.app_retur_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_retur_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_retur_processing_upload_success);
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
								databaseHandler.deleteTableRetur();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void saveAppDataReturNomerRetur(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_RETUR_NOMER_ORDER,
				responsedata);
		editor.commit();
	}

	public class ListViewAdapter extends ArrayAdapter<Retur> {
		Activity activity;
		int layoutResourceId;
		Retur returData;
		ArrayList<Retur> data = new ArrayList<Retur>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<Retur> data) {
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
				holder.list_nomer_retur = (TextView) row
						.findViewById(R.id.sales_order_title_kode_sales_order);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			returData = data.get(position);
			holder.list_kodeCustomer.setText(returData.getKode_customer());
			holder.list_namaCustomer.setText(returData.getNama_lengkap());
			holder.list_nomer_retur.setText(returData.getNomer_retur());

			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_nomer_retur.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String nomerRetur = String.valueOf(data.get(position)
							.getNomer_retur());
					showEditDeleteDialog(nomerRetur);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_nomer_retur;
		}

	}

	public void gotoDetailRetur() {
		Intent i = new Intent(this, DetailReturActivity.class);
		startActivity(i);
		finish();
	}

	// show edit delete dialog
	public void showEditDeleteDialog(final String nomer_retur) {
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
								saveAppDataReturNomerRetur(nomer_retur);
								gotoDetailRetur();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NHAPUS),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								gotoRetur();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void gotoRetur() {
		Intent i = new Intent(this, ReturActivity.class);
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
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 10) {
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
