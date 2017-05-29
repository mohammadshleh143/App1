package com.mahkota_company.android.inventory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
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
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.R;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.ProductStockVan;
import com.mahkota_company.android.database.StockVan;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

@SuppressWarnings("deprecation")
public class StockSummayActivity extends FragmentActivity {
	private Context act;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<ProductStockVan> productStockVan_list = new ArrayList<ProductStockVan>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = StockSummayActivity.class
			.getSimpleName();
	private Typeface typefaceSmall;
	private ImageView menuBackButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_stock_summary);
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
		progressDialog.setMessage(getApplicationContext()
				.getResources()
				.getString(
						R.string.app_inventory_processing_download_stock_summary));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		databaseHandler = new DatabaseHandler(this);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);

		int countStockVan = databaseHandler.getCountStockVan();
		if (countStockVan == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataStockVan().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_processing_download_stock_summary_empty);
				showCustomDialog(message);
			}

		} else {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataStockVan().execute();
			} else {
				showListStockSummary();
			}

		}

		// showListStockSummary();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void gotoInventory() {
		Intent i = new Intent(this, InventoryActivity.class);
		startActivity(i);
		finish();
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

	private class DownloadDataStockVan extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_processing_download_stock_summary));
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
			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_id_staff = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);
			String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_STOCK_VAN + "?id_staff="
					+ main_app_id_staff;
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
					String main_app_table_data = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataStockVanSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableStockVan();
							saveAppDataStockVanSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableStockVan();
						saveAppDataStockVanSameData(act.getApplicationContext()
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
				saveAppDataStockVan(response_data);
				extractDataStockVan();
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
								showListStockSummary();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void extractDataStockVan() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				if (oResponse.getString("request_load").length() > 0) {
					JSONArray jsonarr = oResponse.getJSONArray("request_load");
					for (int i = 0; i < jsonarr.length(); i++) {
						JSONObject oResponsealue = jsonarr.getJSONObject(i);
						/***
						 * Note TODO Di sisi Android yang di butuhkan hanya id
						 * Product dan Jumlah Item yang di Approve oleh admin
						 */
						String id_product = oResponsealue.isNull("id_product") ? null
								: oResponsealue.getString("id_product");
						String nomer_request_load = oResponsealue
								.isNull("nomer_request_load") ? null: oResponsealue.getString("nomer_request_load");
						saveAppDataNoRequestLoad(nomer_request_load);
						String date_request_load = oResponsealue
								.isNull("date_request_load") ? null: oResponsealue.getString("date_request_load");
						String jumlah_request = oResponsealue
								.isNull("jumlah_request") ? null: oResponsealue.getString("jumlah_request");
						String jumlah_accept = oResponsealue
								.isNull("jumlah_accept") ? null : oResponsealue.getString("jumlah_accept");
						String jumlah_sisa = oResponsealue
								.isNull("jumlah_sisa") ? null : oResponsealue.getString("jumlah_sisa");
						String harga = oResponsealue
								.isNull("harga") ? null : oResponsealue.getString("harga");

						Log.d(LOG_TAG, "id_product:" + id_product);
						Log.d(LOG_TAG, "nomer_request_load:" + nomer_request_load);
						Log.d(LOG_TAG, "jumlah_request:" + jumlah_request);
						Log.d(LOG_TAG, "jumlah_accept:" + jumlah_accept);
						Log.d(LOG_TAG, "date_request_load:" + date_request_load);
						Log.d(LOG_TAG, "harga:" + harga);
						try {
							Product tempProduct = databaseHandler.getProduct(Integer.parseInt(id_product));
							if (tempProduct != null)
								databaseHandler.addStockVan(new StockVan(
										tempProduct.getId_product(),
										tempProduct.getNama_product(),
										tempProduct.getKode_product(),
										harga,
										jumlah_request,
										jumlah_accept,
										jumlah_sisa,
										tempProduct.getId_kemasan(),
										tempProduct.getFoto(),
										tempProduct.getDeskripsi(),
										nomer_request_load));

						} catch (Exception ex) {
							Log.d(LOG_TAG, "exception:" + ex.getMessage());
						}
					}

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

	public void saveAppDataNoRequestLoad(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN_NO_REQUEST_LOAD,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStockVan(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN,
				responsedata);
		editor.commit();
	}

	public void saveAppDataStockVanSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_STOCK_VAN_SAME_DATA,
				responsedata);
		editor.commit();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showListStockSummary() {
		productStockVan_list.clear();
//		int countData = databaseHandler.getCountStockVan();
		productStockVan_list = databaseHandler.getAllProductStokVan();
		if (productStockVan_list.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_stock_summary, productStockVan_list);
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

	public class ListViewAdapter extends ArrayAdapter<ProductStockVan> {
		Activity activity;
		int layoutResourceId;
		ArrayList<ProductStockVan> data = new ArrayList<ProductStockVan>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<ProductStockVan> data) {
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
				//holder.tvKodeProduct = (TextView) row
				//		.findViewById(R.id.stock_summary_textview_kode_product);
				holder.tvNamaProduct = (TextView) row
						.findViewById(R.id.stock_summary_nama_product);
				holder.tvStockGudang = (TextView) row
						.findViewById(R.id.stock_summary_stock_gudang);
				holder.tvStockVan = (TextView) row
						.findViewById(R.id.stock_summary_stock_van);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			//holder.tvKodeProduct.setText(data.get(position).getKode_product());
			holder.tvNamaProduct.setText(data.get(position).getNama_product());
			holder.tvStockGudang.setText(String.valueOf(data.get(position).getStockGudang()));
			holder.tvStockVan.setText(data.get(position).getStockVan());
			//holder.tvKodeProduct.setTypeface(typefaceSmall);
			holder.tvNamaProduct.setTypeface(typefaceSmall);
			holder.tvStockGudang.setTypeface(typefaceSmall);
			holder.tvStockVan.setTypeface(typefaceSmall);
			return row;

		}

		class UserHolder {
			//TextView tvKodeProduct;
			TextView tvNamaProduct;
			TextView tvStockGudang;
			TextView tvStockVan;
		}

	}

}
