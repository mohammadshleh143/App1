package com.mahkota_company.android.display_product;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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
import com.mahkota_company.android.database.DisplayProduct;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Request_load;
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
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.R;

@SuppressWarnings("deprecation")
public class DisplayProductActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<DisplayProduct> display_product_list = new ArrayList<DisplayProduct>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = DisplayProductActivity.class
			.getSimpleName();
	private Button addDisplayProduct;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvWaktu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_display_product);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeCustomer = (TextView) findViewById(R.id.activity_display_product_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_display_product_title_nama_customer);
		tvWaktu = (TextView) findViewById(R.id.activity_display_product_title_date_time);
		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);
		tvWaktu.setTypeface(typefaceSmall);

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
		addDisplayProduct = (Button) findViewById(R.id.activity_display_product_btn_add);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(7);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		updateContentDisplayProduct();
		addDisplayProduct.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences spPreferences = getSharedPrefereces();
				String main_app_staff_username = spPreferences.getString(
						CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
				String main_app_staff_id_wilayah = spPreferences.getString(
						CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH, null);
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
									.getCountSalesOrder(customer
											.getKode_customer());
							if (countJadwal == 0) {
								containCustomer = true;
								break;
							}
						}
						if (containCustomer) {
							gotoTambahDisplayProduct();
						} else {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_to_add);
							showCustomDialog(msg);
						}
					}
				}
				/*if (main_app_staff_id_wilayah.length() > 0) {
					List<Customer> dataCustomer = databaseHandler
							.getAllCustomerActive(Integer
									.parseInt(main_app_staff_id_wilayah));
					boolean containCustomer = false;
					for (Customer customer : dataCustomer) {
						int countDisplayProduct = databaseHandler
								.getCountDisplayProduct(customer
										.getKode_customer());
						if (countDisplayProduct == 0) {
							containCustomer = true;
							break;
						}
					}
					if (containCustomer) {
						gotoTambahDisplayProduct();
					} else {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_display_product_failed_to_add);
						showCustomDialog(msg);
					}

				}
				*/
			}
		});

	}

	public void gotoTambahDisplayProduct() {
		Intent i = new Intent(this, AddDisplayProductActivity.class);
		startActivity(i);
		finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void updateContentDisplayProduct() {
		display_product_list.clear();
		ArrayList<DisplayProduct> display_product_from_db = databaseHandler
				.getAllDisplayProduct();
		if (display_product_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < display_product_from_db.size(); i++) {
				int id_display_product = display_product_from_db.get(i)
						.getId_display_product();
				String kode_customer = display_product_from_db.get(i)
						.getKode_customer();
				String nama_lengkap = display_product_from_db.get(i)
						.getNama_lengkap();
				String foto = display_product_from_db.get(i).getFoto();
				String nama_display_product = display_product_from_db.get(i)
						.getNama_display_product();
				String deskripsi = display_product_from_db.get(i)
						.getDeskripsi();
				String datetime = display_product_from_db.get(i).getDatetime();

				DisplayProduct display_product = new DisplayProduct();
				display_product.setId_display_product(id_display_product);
				display_product.setKode_customer(kode_customer);
				display_product.setNama_lengkap(nama_lengkap);
				display_product.setFoto(foto);
				display_product.setNama_display_product(nama_display_product);
				display_product.setDeskripsi(deskripsi);
				display_product.setDatetime(datetime);
				display_product_list.add(display_product);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_display_product, display_product_list);
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
				int countUpload = databaseHandler.getCountDisplayProduct();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_display_product_processing_upload_no_data);
					showCustomDialog(message);
				} else {
					new UploadData().execute();
				}
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_display_product_processing_upload_empty);
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
					.getString(R.string.app_display_product_processing_upload));
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
			String url_add_display_product = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_DISPLAY_PRODUCT;

			List<DisplayProduct> dataAddDisplayProductUpload = databaseHandler
					.getAllDisplayProduct();
			for (DisplayProduct displayProduct : dataAddDisplayProductUpload) {
				response_data = uploadDisplayProduct(url_add_display_product,
						displayProduct.getKode_customer(),
						displayProduct.getNama_lengkap(),
						displayProduct.getNama_display_product(),
						displayProduct.getFoto(),
						displayProduct.getDeskripsi(),
						displayProduct.getDatetime());

			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<DisplayProduct> dataAddDisplayProductUpload = databaseHandler
					.getAllDisplayProduct();
			int countData = dataAddDisplayProductUpload.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataAddDisplayProductUpload.size() * 1000 * 5);
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
										R.string.app_display_product_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadDisplayProduct();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_display_product_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	private String uploadDisplayProduct(final String url,
			final String kode_customer, final String nama_lengkap,
			final String nama_display_product, final String foto,
			final String deskripsi, final String datetime) {
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
			File sourceFile = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_DISPLAY_PRODUCT + "/" + foto);

			// Adding file data to http body
			entity.addPart("image", new FileBody(sourceFile));
			entity.addPart("kode_customer", new StringBody(kode_customer));
			entity.addPart("nama_lengkap", new StringBody(nama_lengkap));
			entity.addPart("foto", new StringBody(foto));
			entity.addPart("nama_display_product", new StringBody(
					nama_display_product));
			entity.addPart("deskripsi", new StringBody(deskripsi));
			entity.addPart("datetime", new StringBody(datetime));

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

	public void initUploadDisplayProduct() {
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
								R.string.app_display_product_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_display_product_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_display_product_processing_upload_success);
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
								databaseHandler.deleteTableDisplayProduct();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void saveAppDataDisplayProductIdDisplayProduct(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT,
				responsedata);
		editor.commit();
	}

	public class ListViewAdapter extends ArrayAdapter<DisplayProduct> {
		Activity activity;
		int layoutResourceId;
		DisplayProduct displayProductData;
		ArrayList<DisplayProduct> data = new ArrayList<DisplayProduct>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<DisplayProduct> data) {
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
						.findViewById(R.id.display_product_title_kode_customer);
				holder.list_namaCustomer = (TextView) row
						.findViewById(R.id.display_product_title_nama_customer);
				holder.list_waktu = (TextView) row
						.findViewById(R.id.display_product_title_waktu);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			displayProductData = data.get(position);
			holder.list_kodeCustomer.setText(displayProductData
					.getKode_customer());
			holder.list_namaCustomer.setText(displayProductData
					.getNama_lengkap());

			SimpleDateFormat fromUser = new SimpleDateFormat(
					"yyyy-MM-dd h:m:ss");
			SimpleDateFormat myFormat = new SimpleDateFormat(
					"dd-MM-yyyy h:m:ss");

			try {
				String inputString = displayProductData.getDatetime();
				String reformattedStr = myFormat.format(fromUser
						.parse(inputString));
				holder.list_waktu.setText(reformattedStr);
			} catch (ParseException | java.text.ParseException e) {
				e.printStackTrace();
			}
			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_waktu.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String id_display_product = String.valueOf(data.get(
							position).getId_display_product());
					showEditDeleteDialog(id_display_product);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_waktu;
		}

	}

	public void gotoDetailDisplayProduct() {
		Intent i = new Intent(this, DetailDisplayProductActivity.class);
		startActivity(i);
		finish();
	}

	// show edit delete dialog
	public void showEditDeleteDialog(final String id_display_product) {
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
								saveAppDataDisplayProductIdDisplayProduct(id_display_product);
								gotoDetailDisplayProduct();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_DELETE),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								databaseHandler
										.deleteTableDisplayProduct(Integer
												.parseInt(id_display_product));
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
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 7) {
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
				} else if (position == 6) {
					Intent intentActivity = new Intent(this,
							StockOnHandActivity.class);
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
