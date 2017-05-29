package com.mahkota_company.android.inventory;

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
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.StockVan;
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

@SuppressWarnings("deprecation")
public class RequestLoadActivity extends FragmentActivity {
	private Context act;
	private DatabaseHandler databaseHandler;
	private ListView listView;
	private ArrayList<StockVan> stockVan_list = new ArrayList<StockVan>();
	private ArrayList<StockVan> reqLoadStockVanList = new ArrayList<StockVan>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = RequestLoadActivity.class
			.getSimpleName();
	private Typeface typefaceSmall;
	private ImageView menuBackButton;
	private Button buttonRequest, buttonCancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_request_load);
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
		listView.setItemsCanFocus(true);

		buttonCancel = (Button) findViewById(R.id.activity_request_load_btn_cancel);
		buttonRequest = (Button) findViewById(R.id.activity_request_load_btn_save);
		buttonCancel.setTypeface(typefaceSmall);
		buttonRequest.setTypeface(typefaceSmall);
		showListRequestLoad();
		buttonCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				gotoInventory();
			}
		});
		buttonRequest.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				reqLoadStockVanList.clear();
				ArrayList<StockVan> stockVanlist = cAdapter.getDataList();
				for (StockVan stockVan : stockVanlist) {
					if (stockVan.getJumlahRequest() != "0") {
						Log.d(LOG_TAG,
								"jumlah req:" + stockVan.getJumlahRequest());
						reqLoadStockVanList.add(stockVan);
					}
				}
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String uploadRequestLoad(final String url,
			final String nomer_request_load, final String id_staff,
			final String id_product, final String jumlah_request,
			final String status) {

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
			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("jumlah_request", new StringBody(jumlah_request));
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
					+ CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_REQUEST_LOAD;
			SharedPreferences spPreferences = getSharedPrefereces();
			String main_app_staff_username = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
			String main_app_id_staff = spPreferences.getString(
					CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);

			String nomer_request_load = main_app_staff_username + "."
					+ main_app_id_staff + "." + GlobalApp.getUniqueId();
			for (StockVan stockVanReqLoad : reqLoadStockVanList) {
				response_data = uploadRequestLoad(upload_url,
						nomer_request_load, main_app_id_staff,
						String.valueOf(stockVanReqLoad.getId_product()),
						String.valueOf(stockVanReqLoad.getJumlahRequest()), "1");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			int countData = reqLoadStockVanList.size();
			if (countData > 0) {
				try {
					Thread.sleep(reqLoadStockVanList.size() * 1024 * 10);
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
										R.string.app_inventory_processing_upload_request_load_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadRequestLoad();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_processing_upload_request_load_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	public void initUploadRequestLoad() {
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
								R.string.app_inventory_processing_upload_request_load_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True2")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_processing_upload_request_load_failed_2);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_processing_upload_request_load_success);
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
								gotoInventory();
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

	public void showListRequestLoad() {
		stockVan_list.clear();
		if(!isRunningDummyVersion()) {
			ArrayList<Product> requeststockvan_from_db = databaseHandler
					.getAllProduct();
			if (requeststockvan_from_db.size() > 0) {
				listView.setVisibility(View.VISIBLE);
				for (int i = 0; i < requeststockvan_from_db.size(); i++) {
					int id_product = requeststockvan_from_db.get(i)
							.getId_product();
					String nama_product = requeststockvan_from_db.get(i)
							.getNama_product();
					String kode_product = requeststockvan_from_db.get(i)
							.getKode_product();
					String harga_jual = requeststockvan_from_db.get(i)
							.getHarga_jual();
					// String stock = requeststockvan_from_db.get(i).getStock();
					String id_kemasan = requeststockvan_from_db.get(i)
							.getId_kemasan();
					String foto = requeststockvan_from_db.get(i).getFoto();
					String deskripsi = requeststockvan_from_db.get(i)
							.getDeskripsi();

					/****
					 * Make temp request stokvan Nantinya defaultnya 0,
					 * sedangkan apabila requestnya nol, tidak perlu di proses
					 */
					StockVan stockVan = new StockVan();
					stockVan.setId_product(id_product);
					stockVan.setNama_product(nama_product);
					stockVan.setKode_product(kode_product);
					stockVan.setHarga_jual(harga_jual);
					stockVan.setJumlahRequest("0");
					stockVan.setIdKemasan(id_kemasan);
					stockVan.setFoto(foto);
					stockVan.setDeskripsi(deskripsi);
					stockVan_list.add(stockVan);
				}

				cAdapter = new ListViewAdapter(this,
						R.layout.list_item_request_load, stockVan_list);
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
				holder.titleKodeProduct = (TextView) row
						.findViewById(R.id.request_load_kode_product);
				holder.titleNamaProduct = (TextView) row
						.findViewById(R.id.request_load_nama_product);
				holder.edtStock = (EditText) row
						.findViewById(R.id.request_load_jumlah);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			holder.titleKodeProduct.setText(data.get(position)
					.getKode_product());
			holder.titleNamaProduct.setText(data.get(position)
					.getNama_product());
			 //holder.edtStock.setText("0");
			 holder.edtStock.setFocusable(true);
			 holder.edtStock.setEnabled(true);
			holder.titleKodeProduct.setTypeface(typefaceSmall);
			holder.titleNamaProduct.setTypeface(typefaceSmall);

			holder.edtStock.addTextChangedListener(new TextWatcher() {

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
						data.get(position).setJumlahRequest("0");
					} else
						data.get(position).setJumlahRequest(
								arg0.toString());

				}

			});


			return row;

		}

		class UserHolder {
			TextView titleKodeProduct;
			TextView titleNamaProduct;
			EditText edtStock;
		}

		public ArrayList<StockVan> getDataList() {
			return data;
		}

	}

}
