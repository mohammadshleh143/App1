package com.mahkota_company.android.inventory;

import java.io.IOException;
import java.util.ArrayList;

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
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.R;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.StockVan;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

@SuppressWarnings("deprecation")
public class PhysicalCountingActivity extends FragmentActivity {
	private Context act;
	private DatabaseHandler databaseHandler;
	private ListView listView;
	private ArrayList<StockVan> stockVanList = new ArrayList<StockVan>();
	private ArrayList<StockVan> updateStockVanList = new ArrayList<StockVan>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = PhysicalCountingActivity.class
			.getSimpleName();
	private Typeface typefaceSmall;
	private ImageView menuBackButton;
	private Button buttonUpdate, buttonCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_physical_counting);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				gotoInventory();
			}
		});
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_product_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		databaseHandler = new DatabaseHandler(this);
		listView = (ListView) findViewById(R.id.list);
		listView.setItemsCanFocus(false);

		buttonCancel = (Button) findViewById(R.id.activity_physical_counting_btn_cancel);
		buttonUpdate = (Button) findViewById(R.id.activity_physical_counting_btn_update);
		buttonCancel.setTypeface(typefaceSmall);
		buttonUpdate.setTypeface(typefaceSmall);
		showListPhysicalCounting();
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoInventory();
			}
		});
		buttonUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateStockVanList.clear();
				updateStockVanList = cAdapter.getDataSisaStockVanList();
				// for (StockVan stockVan : stockVanlist) {
				// if (stockVan.getJumlahRequest() != 0) {
				// Log.d(LOG_TAG,
				// "jumlah req:" + stockVan.getJumlahRequest());
				// updateStockVanList.add(stockVan);
				// }
				// }
				// for (StockVan stockVan : reqLoadStockVanList)
				// Log.d(LOG_TAG, "jumlah req:" + stockVan.getJumlahRequest());

				if (GlobalApp.checkInternetConnection(act))
					new UploadRequestLoadData().execute();
				else {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
					showCustomDialog(message);
				}
			}
		});
	}

	private String uploadPhysicalCounting(final String url,
			final String nomer_request_load, final String id_product,
			final String jumlah_sisa, final String status) {

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

			entity.addPart("nomer_request_load", new StringBody(
					nomer_request_load));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("jumlah_sisa", new StringBody(jumlah_sisa));
			entity.addPart("status", new StringBody(status));
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public class UploadRequestLoadData extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_processing_upload_request_load));
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
					+ CONFIG.CONFIG_APP_URL_UPLOAD_UPDATE_REQUEST_LOAD;
			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_no_req_load = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN_NO_REQUEST_LOAD,
					null);
			String nomer_request_load = main_app_no_req_load;
			for (StockVan stockVanUpdate : updateStockVanList) {
				response_data = uploadPhysicalCounting(upload_url,
						nomer_request_load,
						String.valueOf(stockVanUpdate.getId_product()),
						String.valueOf(stockVanUpdate.getJumlahSisa()), "3");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			int countData = updateStockVanList.size();
			if (countData > 0) {
				try {
					Thread.sleep(updateStockVanList.size() * 1024 * 10);
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
										R.string.app_inventory_processing_upload_physical_counting_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadPhysicalCounting();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_processing_upload_physical_counting_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	public void initUploadPhysicalCounting() {
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
								R.string.app_inventory_processing_upload_physical_counting_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True2")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_processing_upload_physical_counting_failed_2);
					showMessageDialogSuccess(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_processing_upload_physical_counting_load_success);
					showMessageDialogSuccess(msg);
				}

			}

		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

		}
	}

	public void showMessageDialogSuccess(String msg) {
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

	public void gotoInventory() {
		Intent i = new Intent(this, InventoryActivity.class);
		startActivity(i);
		finish();
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
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	protected boolean isRunningDummyVersion() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_username = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
		if (main_app_staff_username != null) {
			return main_app_staff_username.equalsIgnoreCase("demo");
		}
		return false;
	}

	public void showListPhysicalCounting() {
		stockVanList.clear();
		if (isRunningDummyVersion()) {
			ArrayList<StockVan> stockvan_from_db = new ArrayList<StockVan>();
			stockvan_from_db.add(new StockVan(1, "product a", "0001", "100000",
					"10", "10", "10", "1", "", "test 01","12"));
			stockvan_from_db.add(new StockVan(2, "product b", "0002", "100000",
					"20", "20", "20", "1", "", "test 02","13"));
			if (stockvan_from_db.size() > 0) {
				listView.setVisibility(View.VISIBLE);
				for (int i = 0; i < stockvan_from_db.size(); i++) {
					int id_product = stockvan_from_db.get(i).getId_product();
					String nama_product = stockvan_from_db.get(i)
							.getNama_product();
					String kode_product = stockvan_from_db.get(i)
							.getKode_product();
					String harga_jual = stockvan_from_db.get(i).getHarga_jual();
					String jumlahAccept = stockvan_from_db.get(i)
							.getJumlahAccept();
					String jumlahSisa = stockvan_from_db.get(i).getJumlahSisa();
					String id_kemasan = stockvan_from_db.get(i).getIdKemasan();
					String foto = stockvan_from_db.get(i).getFoto();
					String deskripsi = stockvan_from_db.get(i).getDeskripsi();

					/****
					 * Nantinya tiap sales akan menghitung sisa stock yang ada
					 * di dalam van default sisa stock adalah sama dengan stock
					 * di van pertama kali
					 */
					StockVan stockVanSisa = new StockVan();
					stockVanSisa.setId_product(id_product);
					stockVanSisa.setNama_product(nama_product);
					stockVanSisa.setKode_product(kode_product);
					stockVanSisa.setHarga_jual(harga_jual);
					stockVanSisa.setJumlahAccept(jumlahAccept);
					stockVanSisa.setJumlahSisa(jumlahSisa);
					stockVanSisa.setIdKemasan(id_kemasan);
					stockVanSisa.setFoto(foto);
					stockVanSisa.setDeskripsi(deskripsi);
					stockVanList.add(stockVanSisa);
				}

				cAdapter = new ListViewAdapter(this,
						R.layout.list_item_physical_counting, stockVanList);
				listView.setAdapter(cAdapter);
				listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
				cAdapter.notifyDataSetChanged();
			} else {
				listView.setVisibility(View.INVISIBLE);
			}

		} else {
			ArrayList<StockVan> stockvan_from_db = databaseHandler
					.getAllStockVan();
			if (stockvan_from_db.size() > 0) {
				listView.setVisibility(View.VISIBLE);
				for (int i = 0; i < stockvan_from_db.size(); i++) {
					int id_product = stockvan_from_db.get(i).getId_product();
					String nama_product = stockvan_from_db.get(i)
							.getNama_product();
					String kode_product = stockvan_from_db.get(i)
							.getKode_product();
					String harga_jual = stockvan_from_db.get(i).getHarga_jual();
					String jumlahAccept = stockvan_from_db.get(i)
							.getJumlahAccept();
					String jumlahSisa = stockvan_from_db.get(i).getJumlahSisa();
					String id_kemasan = stockvan_from_db.get(i).getIdKemasan();
					String foto = stockvan_from_db.get(i).getFoto();
					String deskripsi = stockvan_from_db.get(i).getDeskripsi();

					/****
					 * Nantinya tiap sales akan menghitung sisa stock yang ada
					 * di dalam van default sisa stock adalah sama dengan stock
					 * di van pertama kali
					 */
					StockVan stockVanSisa = new StockVan();
					stockVanSisa.setId_product(id_product);
					stockVanSisa.setNama_product(nama_product);
					stockVanSisa.setKode_product(kode_product);
					stockVanSisa.setHarga_jual(harga_jual);
					stockVanSisa.setJumlahAccept(jumlahAccept);
					stockVanSisa.setJumlahSisa(jumlahSisa);
					stockVanSisa.setIdKemasan(id_kemasan);
					stockVanSisa.setFoto(foto);
					stockVanSisa.setDeskripsi(deskripsi);
					stockVanList.add(stockVanSisa);
				}

				cAdapter = new ListViewAdapter(this,
						R.layout.list_item_physical_counting, stockVanList);
				listView.setAdapter(cAdapter);
				listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
				cAdapter.notifyDataSetChanged();
			} else {
				listView.setVisibility(View.INVISIBLE);
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

	public class ListViewAdapter extends ArrayAdapter<StockVan> {
		Activity activity;
		int layoutResourceId;
		ArrayList<StockVan> data = new ArrayList<StockVan>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<StockVan> data) {
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
				holder.tvKodeProduct = (TextView) row
						.findViewById(R.id.physical_counting_kode_product);
				holder.tvNamaProduct = (TextView) row
						.findViewById(R.id.physical_counting_nama_product);
				holder.tvStockVan = (TextView) row
						.findViewById(R.id.physical_counting_stock_van);
				holder.etSisaStock = (EditText) row
						.findViewById(R.id.physical_counting_sisa_stock);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			holder.tvKodeProduct.setText(data.get(position).getKode_product());
			holder.tvNamaProduct.setText(data.get(position).getNama_product());
			holder.tvStockVan.setText(data.get(position).getJumlahAccept());
			holder.tvKodeProduct.setTypeface(typefaceSmall);
			holder.tvNamaProduct.setTypeface(typefaceSmall);
			holder.tvStockVan.setTypeface(typefaceSmall);
			holder.etSisaStock.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(final CharSequence cs, int arg1,
						int arg2, int arg3) {
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {

				}

				@Override
				public void afterTextChanged(Editable arg0) {
					if (arg0.toString().length() == 0) {
						/**
						 * update sisa stok di table sqlite juga fungsinya untuk
						 * report
						 */
						try {
							data.get(position).setJumlahSisa("0");
							databaseHandler.updateStockVanJumlahSisa(
									data.get(position).getId_product(), 0);
						} catch (Exception ex) {
							Log.d(LOG_TAG, "Exception:" + ex.getMessage());
						}
					} else {
						/**
						 * update sisa stok di table sqlite juga fungsinya untuk
						 * report
						 */
						try {
							data.get(position).setJumlahSisa(
									arg0.toString());
							databaseHandler.updateStockVanJumlahSisa(
									data.get(position).getId_product(),
									Integer.parseInt(arg0.toString()));
						} catch (Exception ex) {
							Log.d(LOG_TAG, "Exception:" + ex.getMessage());
						}
					}

				}
			});

			return row;

		}

		class UserHolder {
			TextView tvKodeProduct;
			TextView tvNamaProduct;
			TextView tvStockVan;
			EditText etSisaStock;
		}

		public ArrayList<StockVan> getDataSisaStockVanList() {
			return data;
		}

	}

}
