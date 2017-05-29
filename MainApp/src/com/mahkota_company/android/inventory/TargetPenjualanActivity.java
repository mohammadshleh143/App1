package com.mahkota_company.android.inventory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.R;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Penjualan;
import com.mahkota_company.android.database.PenjualanDetail;
import com.mahkota_company.android.database.ProductTarget;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

@SuppressWarnings("deprecation")
public class TargetPenjualanActivity extends FragmentActivity {
	private Context act;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<ProductTarget> productTargetList = new ArrayList<ProductTarget>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private String message;
	private static final String LOG_TAG = TargetPenjualanActivity.class
			.getSimpleName();
	private Button btnAddTP;
	private Button btnUploadTP;
	private Typeface typefaceSmall;
	private ImageView menuBackButton;
	private EditText searchCustomer;
	private ArrayList<Customer> customer_list = new ArrayList<Customer>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_target_penjualan);
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
		progressDialog
				.setMessage(getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_unload_product_target_penjualan_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		btnAddTP = (Button) findViewById(R.id.target_penjualan_btn_add);
		btnUploadTP = (Button) findViewById(R.id.target_penjualan_btn_upload);
		searchCustomer = (EditText) findViewById(R.id.target_penjualan_et_search_nama);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		// showTargetPenjualanList();

		int countTargetPenjualan = databaseHandler.getCountTargetPenjualan();
		if (countTargetPenjualan == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataTargetPenjualan().execute();
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_unload_product_target_penjualan_processing_download_failed);
				showCustomDialog(message);
			}
		} else
			showListTargetPenjualan();

		btnAddTP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(TargetPenjualanActivity.this,
						AddTargetPenjualanActivity.class);
				startActivity(i);
				finish();
			}
		});
		btnUploadTP.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				doUploadTp();
			}
		});
		searchCustomer.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.toString().length() > 0) {
					customer_list.clear();
					SharedPreferences spPreferences = getSharedPrefereces();
					String main_app_staff_id_wilayah = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH, null);
					ArrayList<Customer> customer_from_db = databaseHandler.getAllCustomerActiveBaseOnSearch(
							cs.toString(),
							Integer.parseInt(main_app_staff_id_wilayah));
					if (customer_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < customer_from_db.size(); i++) {
							int id_customer = customer_from_db.get(i)
									.getId_customer();
							String kode_customer = customer_from_db.get(i)
									.getKode_customer();
							String email = customer_from_db.get(i).getEmail();
							String alamat = customer_from_db.get(i).getAlamat();
							String lats = customer_from_db.get(i).getLats();
							String longs = customer_from_db.get(i).getLongs();
							String nama_lengkap = customer_from_db.get(i)
									.getNama_lengkap();
							String no_telp = customer_from_db.get(i)
									.getNo_telp();
							int id_wilayah = customer_from_db.get(i)
									.getId_wilayah();
							String foto1 = customer_from_db.get(i).getFoto_1();
							String foto2 = customer_from_db.get(i).getFoto_2();
							String foto3 = customer_from_db.get(i).getFoto_3();
							int id_type = customer_from_db.get(i)
									.getId_type_customer();
							String blockir = customer_from_db.get(i)
									.getBlokir();
							String date = customer_from_db.get(i).getDate();
							String status_update = customer_from_db.get(i)
									.getStatus_update();
							int id_staff = customer_from_db.get(i)
									.getId_staff();
							String no_ktp = customer_from_db.get(i).getNo_ktp();
							String tanggal_lahir = customer_from_db.get(i)
									.getTanggal_lahir();
							String nama_bank = customer_from_db.get(i)
									.getNama_bank();
							String no_rekening = customer_from_db.get(i)
									.getNo_rekening();
							String atas_nama = customer_from_db.get(i)
									.getAtas_nama();
							String npwp = customer_from_db.get(i).getNpwp();
							String nama_pasar = customer_from_db.get(i)
									.getNama_pasar();
							int id_cluster = customer_from_db.get(i)
									.getId_cluster();
							String telp = customer_from_db.get(i).getTelp();
							String fax = customer_from_db.get(i).getFax();
							String omset = customer_from_db.get(i).getOmset();
							String cara_pembayaran = customer_from_db.get(i)
									.getCara_pembayaran();
							String plafon_kredit = customer_from_db.get(i)
									.getPlafon_kredit();
							String term_kredit = customer_from_db.get(i)
									.getTerm_kredit();
							String nama_istri = customer_from_db.get(i)
									.getNama_istri();
							String nama_anak1 = customer_from_db.get(i)
									.getNama_anak1();
							String nama_anak2 = customer_from_db.get(i)
									.getNama_anak2();
							String nama_anak3 = customer_from_db.get(i)
									.getNama_anak3();

							Customer customer = new Customer();
							customer.setId_customer(id_customer);
							customer.setKode_customer(kode_customer);
							customer.setEmail(email);
							customer.setAlamat(alamat);
							customer.setLats(lats);
							customer.setLongs(longs);
							customer.setNama_lengkap(nama_lengkap);
							customer.setNo_telp(no_telp);
							customer.setId_wilayah(id_wilayah);
							customer.setFoto_1(foto1);
							customer.setFoto_2(foto2);
							customer.setFoto_3(foto3);
							customer.setId_type_customer(id_type);
							customer.setBlokir(blockir);
							customer.setDate(date);
							customer.setStatus_update(status_update);
							customer.setId_staff(id_staff);
							customer.setNo_ktp(no_ktp);
							customer.setTanggal_lahir(tanggal_lahir);
							customer.setNama_bank(nama_bank);
							customer.setNo_rekening(no_rekening);
							customer.setAtas_nama(atas_nama);
							customer.setNpwp(npwp);
							customer.setNama_pasar(nama_pasar);
							customer.setId_cluster(id_cluster);
							customer.setTelp(telp);
							customer.setFax(fax);
							customer.setOmset(omset);
							customer.setCara_pembayaran(cara_pembayaran);
							customer.setPlafon_kredit(plafon_kredit);
							customer.setTerm_kredit(term_kredit);

							customer.setNama_istri(nama_istri);
							customer.setNama_anak1(nama_anak1);
							customer.setNama_anak2(nama_anak2);
							customer.setNama_anak3(nama_anak3);

							customer_list.add(customer);
						}

						productTargetList.clear();
						ArrayList<ProductTarget> productTargetFromDB = databaseHandler
								.getAllProductTargetGroupByNomerPenjualan();
						if (productTargetFromDB.size() > 0) {
							listview.setVisibility(View.VISIBLE);
							for (int i = 0; i < productTargetFromDB.size(); i++) {
								int id_product_target = productTargetFromDB
										.get(i).getId_product_target();
								String nomer_product_target = productTargetFromDB
										.get(i).getNomer_product_target();
								String created_date_product_target = productTargetFromDB
										.get(i)
										.getCreated_date_product_target();
								String created_time_product_target = productTargetFromDB
										.get(i)
										.getCreated_time_product_target();
								String updated_date_product_target = productTargetFromDB
										.get(i)
										.getUpdated_date_product_target();
								String updated_time_product_target = productTargetFromDB
										.get(i)
										.getUpdated_time_product_target();
								int created_by = productTargetFromDB.get(i)
										.getCreated_by();
								int updated_by = productTargetFromDB.get(i)
										.getUpdated_by();
								int id_staff = productTargetFromDB.get(i)
										.getId_staff();
								int id_customer = productTargetFromDB.get(i)
										.getId_customer();
								int id_product = productTargetFromDB.get(i)
										.getId_product();
								int jumlah_target = productTargetFromDB.get(i)
										.getJumlah_target();
								int jumlah_terjual = productTargetFromDB.get(i)
										.getJumlah_terjual();

								ProductTarget productTarget = new ProductTarget();
								productTarget
										.setId_product_target(id_product_target);
								productTarget
										.setNomer_product_target(nomer_product_target);
								productTarget
										.setCreated_date_product_target(created_date_product_target);
								productTarget
										.setCreated_time_product_target(created_time_product_target);
								productTarget
										.setUpdated_date_product_target(updated_date_product_target);
								productTarget
										.setUpdated_time_product_target(updated_time_product_target);
								productTarget.setCreated_by(created_by);
								productTarget.setUpdated_by(updated_by);
								productTarget.setId_staff(id_staff);
								productTarget.setId_customer(id_customer);
								productTarget.setId_product(id_product);
								productTarget.setJumlah_target(jumlah_target);
								productTarget.setJumlah_terjual(jumlah_terjual);

								for (Customer customer : customer_list) {
									if (customer.getId_customer() == productTarget
											.getId_customer())
										productTargetList.add(productTarget);
								}
							}
							cAdapter = new ListViewAdapter(
									TargetPenjualanActivity.this,
									R.layout.list_item_sales_order,
									productTargetList);
							listview.setAdapter(cAdapter);
							cAdapter.notifyDataSetChanged();
						} else {
							listview.setVisibility(View.INVISIBLE);
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					showListTargetPenjualan();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

	}

	protected void doUploadTp() {
		if (GlobalApp.checkInternetConnection(act)) {
			int countUpload = databaseHandler.getCountTargetPenjualan();
			if (countUpload == 0) {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_unload_product_target_penjualan_processing_upload_tp_failed_no_data);
				showCustomDialog(message);
			} else {
				new UploadDataProductTarget().execute();
			}
		} else {
			String message = act
					.getApplicationContext()
					.getResources()
					.getString(
							R.string.app_inventory_unload_product_target_penjualan_processing_upload_tp_failed);
			showCustomDialog(message);
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

	private class DownloadDataTargetPenjualan extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_processing));
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
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_TARGET_PENJUALAN
					+ "?id_staff=" + main_app_id_staff;
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
							CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN,
							null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataTargetPenjualanSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableProductTarget();
							saveAppDataTargetPenjualanSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableProductTarget();
						saveAppDataTargetPenjualanSameData(act
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

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				saveAppDataTargetPenjualan(response_data);
				extractDataTargetPenjualan();
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
								showListTargetPenjualan();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void extractDataTargetPenjualan() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_SAME_DATA,
				null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				if (oResponse.getString("product_target").length() > 0) {
					JSONArray jsonarr = oResponse
							.getJSONArray("product_target");
					for (int i = 0; i < jsonarr.length(); i++) {
						JSONObject oResponsealue = jsonarr.getJSONObject(i);
						String id_product_target = oResponsealue
								.isNull("id_product_target") ? null
								: oResponsealue.getString("id_product_target");
						String nomer_product_target = oResponsealue
								.isNull("nomer_product_target") ? null
								: oResponsealue
										.getString("nomer_product_target");
						String created_date_product_target = oResponsealue
								.isNull("created_date_product_target") ? null
								: oResponsealue
										.getString("created_date_product_target");
						String created_time_product_target = oResponsealue
								.isNull("created_time_product_target") ? null
								: oResponsealue
										.getString("created_time_product_target");
						String updated_date_product_target = oResponsealue
								.isNull("updated_date_product_target") ? null
								: oResponsealue
										.getString("updated_date_product_target");
						String updated_time_product_target = oResponsealue
								.isNull("updated_time_product_target") ? null
								: oResponsealue
										.getString("updated_time_product_target");
						String created_by = oResponsealue.isNull("created_by") ? null
								: oResponsealue.getString("created_by");
						String updated_by = oResponsealue.isNull("updated_by") ? null
								: oResponsealue.getString("updated_by");
						String id_staff = oResponsealue.isNull("id_staff") ? null
								: oResponsealue.getString("id_staff");
						String id_customer = oResponsealue
								.isNull("id_customer") ? null : oResponsealue
								.getString("id_customer");
						String id_product = oResponsealue.isNull("id_product") ? null
								: oResponsealue.getString("id_product");
						String jumlah_target = oResponsealue
								.isNull("jumlah_target") ? null : oResponsealue
								.getString("jumlah_target");
						String jumlah_terjual = oResponsealue
								.isNull("jumlah_terjual") ? null
								: oResponsealue.getString("jumlah_terjual");
						Log.d(LOG_TAG, "id_product_target:" + id_product_target);
						Log.d(LOG_TAG, "nomer_product_target:"
								+ nomer_product_target);
						Log.d(LOG_TAG, "created_date_product_target:"
								+ created_date_product_target);
						Log.d(LOG_TAG, "created_time_product_target:"
								+ created_time_product_target);
						Log.d(LOG_TAG, "updated_date_product_target:"
								+ updated_date_product_target);
						Log.d(LOG_TAG, "updated_time_product_target:"
								+ updated_time_product_target);
						Log.d(LOG_TAG, "created_by:" + created_by);
						Log.d(LOG_TAG, "updated_by:" + updated_by);
						Log.d(LOG_TAG, "id_staff:" + id_staff);
						Log.d(LOG_TAG, "id_customer:" + id_customer);
						Log.d(LOG_TAG, "id_product:" + id_product);
						Log.d(LOG_TAG, "jumlah_target:" + jumlah_target);
						Log.d(LOG_TAG, "jumlah_terjual:" + jumlah_terjual);

						try {
							int tempCounter = databaseHandler
									.getCountTargetPenjualan() + 1;
							databaseHandler.addProductTarget(new ProductTarget(
									tempCounter, nomer_product_target,
									created_date_product_target,
									created_time_product_target,
									updated_date_product_target,
									updated_time_product_target, Integer
											.parseInt(created_by), Integer
											.parseInt(updated_by), Integer
											.parseInt(id_staff), Integer
											.parseInt(id_customer), Integer
											.parseInt(id_product), Integer
											.parseInt(jumlah_target), Integer
											.parseInt(jumlah_terjual)));
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

	public void saveAppDataNoTargetPenjualan(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NO_TARGET_PENJUALAN,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTargetPenjualan(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTargetPenjualanSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void gotoInventory() {
		Intent i = new Intent(this, InventoryActivity.class);
		startActivity(i);
		finish();
	}

	public void gotoTambahSalesOrder() {
		// Intent i = new Intent(this, AddPenjualanActivity.class);
		// startActivity(i);
		// finish();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showListTargetPenjualan() {
		productTargetList.clear();
		ArrayList<ProductTarget> productTargetFromDB = databaseHandler
				.getAllProductTargetGroupByNomerPenjualan();
		if (productTargetFromDB.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < productTargetFromDB.size(); i++) {
				int id_product_target = productTargetFromDB.get(i)
						.getId_product_target();
				String nomer_product_target = productTargetFromDB.get(i)
						.getNomer_product_target();
				String created_date_product_target = productTargetFromDB.get(i)
						.getCreated_date_product_target();
				String created_time_product_target = productTargetFromDB.get(i)
						.getCreated_time_product_target();
				String updated_date_product_target = productTargetFromDB.get(i)
						.getUpdated_date_product_target();
				String updated_time_product_target = productTargetFromDB.get(i)
						.getUpdated_time_product_target();
				int created_by = productTargetFromDB.get(i).getCreated_by();
				int updated_by = productTargetFromDB.get(i).getUpdated_by();
				int id_staff = productTargetFromDB.get(i).getId_staff();
				int id_customer = productTargetFromDB.get(i).getId_customer();
				int id_product = productTargetFromDB.get(i).getId_product();
				int jumlah_target = productTargetFromDB.get(i)
						.getJumlah_target();
				int jumlah_terjual = productTargetFromDB.get(i)
						.getJumlah_terjual();

				ProductTarget productTarget = new ProductTarget();
				productTarget.setId_product_target(id_product_target);
				productTarget.setNomer_product_target(nomer_product_target);
				productTarget
						.setCreated_date_product_target(created_date_product_target);
				productTarget
						.setCreated_time_product_target(created_time_product_target);
				productTarget
						.setUpdated_date_product_target(updated_date_product_target);
				productTarget
						.setUpdated_time_product_target(updated_time_product_target);
				productTarget.setCreated_by(created_by);
				productTarget.setUpdated_by(updated_by);
				productTarget.setId_staff(id_staff);
				productTarget.setId_customer(id_customer);
				productTarget.setId_product(id_product);
				productTarget.setJumlah_target(jumlah_target);
				productTarget.setJumlah_terjual(jumlah_terjual);
				productTargetList.add(productTarget);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_target_penjualan, productTargetList);
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
				int countUpload = databaseHandler.getCountSalesOrder();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_order_processing_upload_no_data);
					showCustomDialog(message);
				} else {
					new UploadDataProductTarget().execute();
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

	public class UploadDataProductTarget extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_processing_upload));
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
			String url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_UPDATE_PRODUCT_TARGET;

			List<ProductTarget> dataProductTarget = databaseHandler
					.getAllProductTarget();
			for (ProductTarget productTarget : dataProductTarget) {
				response_data = uploadProductTarget(url,
						productTarget.getNomer_product_target(),
						String.valueOf(productTarget.getId_staff()),
						String.valueOf(productTarget.getId_customer()),
						String.valueOf(productTarget.getId_product()),
						String.valueOf(productTarget.getJumlah_target()),
						String.valueOf(productTarget.getJumlah_terjual()), "2");
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<ProductTarget> dataProductTarget = databaseHandler
					.getAllProductTarget();
			int countData = dataProductTarget.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataProductTarget.size() * 1000 * 3);
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
										R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								new UploadDataProductTargetPenjualan()
										.execute();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	public class UploadDataProductTargetPenjualan extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_processing_upload));
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
			String url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_UPDATE_PENJUALAN;

			List<Penjualan> dataProductPenjualan = databaseHandler
					.getAllPenjualan();

			for (Penjualan penjualan : dataProductPenjualan) {
				response_data = uploadPenjualan(url,
						penjualan.getNomer_product_terjual(),
						String.valueOf(penjualan.getDate_product_terjual()),
						String.valueOf(penjualan.getTime_product_terjual()),
						String.valueOf(penjualan.getId_staff()),
						String.valueOf(penjualan.getId_customer()),
						String.valueOf(penjualan.getDiskon()));
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<Penjualan> dataProductPenjualan = databaseHandler
					.getAllPenjualan();
			int countData = dataProductPenjualan.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataProductPenjualan.size() * 1000 * 3);
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
										R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadTargetPenjualan();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	public class UploadDataProductTargetPenjualanDetail extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_processing_upload));
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
			String url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_UPLOAD_UPDATE_PENJUALAN_DETAIL;

			List<PenjualanDetail> dataProductPenjualanDetail = databaseHandler
					.getAllPenjualanDetail();

			for (PenjualanDetail penjualan : dataProductPenjualanDetail) {
				response_data = uploadPenjualanDetail(url,
						penjualan.getNomer_product_terjual(),
						String.valueOf(penjualan.getIdProduct()),
						String.valueOf(penjualan.getJumlah()));
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<PenjualanDetail> dataProductPenjualanDetail = databaseHandler
					.getAllPenjualanDetail();
			int countData = dataProductPenjualanDetail.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataProductPenjualanDetail.size() * 1000 * 3);
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
										R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadTargetPenjualan();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private String uploadProductTarget(final String url,
			final String nomer_product_target, final String id_staff,
			final String id_customer, final String id_product,
			final String jumlah_target, final String jumlah_terjual,
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

			entity.addPart("nomer_product_target", new StringBody(
					nomer_product_target));
			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("id_customer", new StringBody(id_customer));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("jumlah_target", new StringBody(jumlah_target));
			entity.addPart("jumlah_terjual", new StringBody(jumlah_terjual));
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

	private String uploadPenjualan(final String url,
			final String nomer_product_terjual,
			final String date_product_terjual,
			final String time_product_terjual, final String id_staff,
			final String id_customer, final String diskon) {

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

			entity.addPart("nomer_product_terjual", new StringBody(
					nomer_product_terjual));
			entity.addPart("date_product_terjual", new StringBody(
					date_product_terjual));
			entity.addPart("time_product_terjual", new StringBody(
					time_product_terjual));
			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("id_customer", new StringBody(id_customer));
			entity.addPart("diskon", new StringBody(diskon));

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

	private String uploadPenjualanDetail(final String url,
			final String nomer_product_terjual, final String id_product,
			final String jumlah) {

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

			entity.addPart("nomer_product_terjual", new StringBody(
					nomer_product_terjual));
			entity.addPart("id_product", new StringBody(id_product));
			entity.addPart("jumlah", new StringBody(jumlah));

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

	public void initUploadTargetPenjualan() {
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
								R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_processing_upload_success);
					showDialogUploadSuccess(msg);
				}

			}

		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

		}
	}

	public void showDialogUploadSuccess(String msg) {
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
								databaseHandler.deleteTableProductTarget();
								databaseHandler.deleteTablePenjualan();
								databaseHandler.deleteTablePenjualanDetail();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public class ListViewAdapter extends ArrayAdapter<ProductTarget> {
		Activity activity;
		int layoutResourceId;
		ArrayList<ProductTarget> data = new ArrayList<ProductTarget>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<ProductTarget> data) {
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
				holder.listTvNomerTP = (TextView) row
						.findViewById(R.id.target_penjualan_nomer_tp);
				holder.listTvNamaTP = (TextView) row
						.findViewById(R.id.target_penjualan_nama_customer);
				holder.listBtnDeleteTP = (Button) row
						.findViewById(R.id.target_penjualan_btn_delete);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			holder.listTvNomerTP.setText(data.get(position)
					.getNomer_product_target());
			try {
				Customer customer = databaseHandler.getCustomer(data.get(
						position).getId_customer());
				if (customer != null)
					holder.listTvNamaTP.setText(customer.getNama_lengkap());
				else
					holder.listTvNamaTP.setText("");
			} catch (Exception ex) {
				Log.d(LOG_TAG, "Exception:" + ex.getMessage());
			}

			holder.listTvNomerTP.setTypeface(typefaceSmall);
			holder.listTvNamaTP.setTypeface(typefaceSmall);
			holder.listBtnDeleteTP.setTypeface(typefaceSmall);
			holder.listBtnDeleteTP
					.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							String nomerProductTarget = String.valueOf(data
									.get(position).getNomer_product_target());
							showConfirmationDeleteDialog(nomerProductTarget);
						}
					});

			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String nomerProductTarget = String.valueOf(data.get(
							position).getNomer_product_target());
					saveAppDataNoTargetPenjualan(nomerProductTarget);
					gotoDetailTargetPenjualan();
				}
			});
			return row;

		}

		class UserHolder {
			TextView listTvNomerTP;
			TextView listTvNamaTP;
			TextView listBtnDeleteTP;
		}

	}

	public void gotoDetailTargetPenjualan() {
		Intent i = new Intent(this, DetailTargetPenjualanActivity.class);
		startActivity(i);
		finish();
	}

	// show delete dialog
	public void showConfirmationDeleteDialog(final String nomer_tp) {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_DATA_DELETE_DIALOG);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(true)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_YES),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();
								try {
									databaseHandler
											.deleteTableProductTarget(Integer
													.parseInt(nomer_tp));
									databaseHandler
											.deleteTablePenjualan(nomer_tp);
									databaseHandler
											.deleteTablePenjualanDetail(nomer_tp);
								} catch (Exception ex) {
									Log.i(LOG_TAG,
											"Exception:" + ex.getMessage());
								}
								showListTargetPenjualan();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NO),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
