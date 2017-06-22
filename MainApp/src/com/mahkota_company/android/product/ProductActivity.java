package com.mahkota_company.android.product;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
import org.apache.http.entity.mime.content.FileBody;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.inventory.Kanvas_Uploaded;
import com.mahkota_company.android.supervisor.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.InventoryActivity;
import com.mahkota_company.android.inventory.RequestActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.FileUtils;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.RowItem;
import com.mahkota_company.android.R;

@SuppressWarnings("deprecation")
public class ProductActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<Product> product_list = new ArrayList<Product>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private ListViewAdapter cAdapterChooseAdapter;
	private EditText searchproduct;
	private static final String LOG_TAG = ProductActivity.class.getSimpleName();
	private Typeface typefaceSmall;
	private String response;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_product);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		searchproduct = (EditText) findViewById(R.id.activity_product_edittext_search);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_product_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		databaseHandler = new DatabaseHandler(this);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		mNavigationDrawerFragment.selectItem(2);
		int countProduct = databaseHandler.getCountProduct();

		if (countProduct == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataProduct().execute();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_product_processing_empty);
				showCustomDialog(message);
			}
		} else {
			updateContentProduct();
		}

		searchproduct.addTextChangedListener(new TextWatcher() {


			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
									  int arg3) {
				if (cs.toString().length() > 0) {
					product_list.clear();
					ArrayList<Product> product_from_db = databaseHandler.getAllProductBaseOnSearch(
							cs.toString());
					if (product_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < product_from_db.size(); i++) {
							int id_product = product_from_db.get(i)
									.getId_product();
							String nama_product = product_from_db.get(i)
									.getNama_product();
							String kode_product = product_from_db.get(i)
									.getKode_product();
							String harga_jual = product_from_db.get(i)
									.getHarga_jual();
							String stock = product_from_db.get(i).getStock();
							String id_kemasan = product_from_db.get(i)
									.getId_kemasan();
							String foto = product_from_db.get(i).getFoto();
							String deskripsi = product_from_db.get(i)
									.getDeskripsi();

							Product product = new Product();
							product.setId_product(id_product);
							product.setNama_product(nama_product);
							product.setKode_product(kode_product);
							product.setHarga_jual(harga_jual);
							product.setStock(stock);
							product.setId_kemasan(id_kemasan);
							product.setFoto(foto);
							product.setDeskripsi(deskripsi);
							product_list.add(product);
							cAdapterChooseAdapter = new ListViewAdapter(
									ProductActivity.this,
									R.layout.list_item_product,
									product_list);
							listview.setAdapter(cAdapterChooseAdapter);
							cAdapterChooseAdapter.notifyDataSetChanged();
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					product_list.clear();
					ArrayList<Product> product_from_db = databaseHandler
							.getAllProduct();
					if (product_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < product_from_db.size(); i++) {
							int id_product = product_from_db.get(i)
									.getId_product();
							String nama_product = product_from_db.get(i)
									.getNama_product();
							String kode_product = product_from_db.get(i)
									.getKode_product();
							String harga_jual = product_from_db.get(i)
									.getHarga_jual();
							String stock = product_from_db.get(i).getStock();
							String id_kemasan = product_from_db.get(i)
									.getId_kemasan();
							String foto = product_from_db.get(i).getFoto();
							String deskripsi = product_from_db.get(i)
									.getDeskripsi();

							Product product = new Product();
							product.setId_product(id_product);
							product.setNama_product(nama_product);
							product.setKode_product(kode_product);
							product.setHarga_jual(harga_jual);
							product.setStock(stock);
							product.setId_kemasan(id_kemasan);
							product.setFoto(foto);
							product.setDeskripsi(deskripsi);
							product_list.add(product);
							cAdapterChooseAdapter = new ListViewAdapter(
									ProductActivity.this,
									R.layout.list_item_product,
									product_list);
							listview.setAdapter(cAdapterChooseAdapter);
							cAdapterChooseAdapter.notifyDataSetChanged();

						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}
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

	private class DownloadDataProduct extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_product_processing));
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
			String download_data_url = CONFIG.CONFIG_APP_URL_PUBLIC
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_PRODUCT;
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
							CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataProductSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableProduct();
							saveAppDataProductSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableProduct();
						saveAppDataProductSameData(act.getApplicationContext()
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
				saveAppDataProduct(response_data);
				extractDataProduct();
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
								updateContentRefreshProduct();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void extractDataProduct() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("product");
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id_product = oResponsealue.isNull("id_product") ? null
							: oResponsealue.getString("id_product");
					String nama_product = oResponsealue.isNull("nama_product") ? null
							: oResponsealue.getString("nama_product");
					String kode_product = oResponsealue.isNull("kode_product") ? null
							: oResponsealue.getString("kode_product");
					String harga_jual = oResponsealue.isNull("harga_jual") ? null
							: oResponsealue.getString("harga_jual");
					String stock = oResponsealue.isNull("stock") ? null
							: oResponsealue.getString("stock");
					String id_kemasan = oResponsealue.isNull("id_kemasan") ? null
							: oResponsealue.getString("id_kemasan");
					String foto = oResponsealue.isNull("foto") ? null
							: oResponsealue.getString("foto");
					String deskripsi = oResponsealue.isNull("deskripsi") ? null
							: oResponsealue.getString("deskripsi");
					String uomqtyl1 = oResponsealue.isNull("uomqtyl1") ? null
							: oResponsealue.getString("uomqtyl1");
					String uomqtyl2 = oResponsealue.isNull("uomqtyl2") ? null
							: oResponsealue.getString("uomqtyl2");
					String uomqtyl3 = oResponsealue.isNull("uomqtyl3") ? null
							: oResponsealue.getString("uomqtyl3");
					String uomqtyl4 = oResponsealue.isNull("uomqtyl4") ? null
							: oResponsealue.getString("uomqtyl4");
					Log.d(LOG_TAG, "id_product:" + id_product);
					Log.d(LOG_TAG, "nama_product:" + nama_product);
					Log.d(LOG_TAG, "kode_product:" + kode_product);
					Log.d(LOG_TAG, "harga_jual:" + harga_jual);
					Log.d(LOG_TAG, "stock:" + stock);
					Log.d(LOG_TAG, "id_kemasan:" + id_kemasan);
					Log.d(LOG_TAG, "foto:" + foto);
					Log.d(LOG_TAG, "deskripsi:" + deskripsi);
					Log.d(LOG_TAG, "uomqtyl1:" + uomqtyl1);
					Log.d(LOG_TAG, "uomqtyl2:" + uomqtyl2);
					Log.d(LOG_TAG, "uomqtyl3:" + uomqtyl3);
					Log.d(LOG_TAG, "uomqtyl4:" + uomqtyl4);
					databaseHandler.add_Product(new Product(Integer
							.parseInt(id_product), nama_product, kode_product,
							harga_jual, stock, id_kemasan, foto, deskripsi,
							uomqtyl1, uomqtyl2,uomqtyl3,uomqtyl4,"1"));
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

	public void saveAppDataProduct(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT, responsedata);
		editor.commit();
	}

	public void saveAppDataProductSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataProductIdProduct(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_ID_PRODUCT,
				responsedata);
		editor.commit();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void updateContentProduct() {
		product_list.clear();
		ArrayList<Product> product_from_db = databaseHandler.getAllProduct();
		if (product_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < product_from_db.size(); i++) {
				int id_product = product_from_db.get(i).getId_product();
				String nama_product = product_from_db.get(i).getNama_product();
				String kode_product = product_from_db.get(i).getKode_product();
				String harga_jual = product_from_db.get(i).getHarga_jual();
				String stock = product_from_db.get(i).getStock();
				String id_kemasan = product_from_db.get(i).getId_kemasan();
				String foto = product_from_db.get(i).getFoto();
				String deskripsi = product_from_db.get(i).getDeskripsi();
				String uomqtyl1 = product_from_db.get(i).getUomqtyl1();
				String uomqtyl2 = product_from_db.get(i).getUomqtyl2();
				String uomqtyl3 = product_from_db.get(i).getUomqtyl3();
				String uomqtyl4 = product_from_db.get(i).getUomqtyl4();
				String status = product_from_db.get(i).getStatus();

				Product product = new Product();
				product.setId_product(id_product);
				product.setNama_product(nama_product);
				product.setKode_product(kode_product);
				product.setHarga_jual(harga_jual);
				product.setStock(stock);
				product.setId_kemasan(id_kemasan);
				product.setFoto(foto);
				product.setDeskripsi(deskripsi);
				product.setUomqtyl1(uomqtyl1);
				product.setUomqtyl2(uomqtyl2);
				product.setUomqtyl3(uomqtyl3);
				product.setUomqtyl4(uomqtyl4);
				product.setStatus(status);
				File dir = new File(CONFIG.getFolderPath() + "/"
						+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/" + foto);
				if (!dir.exists()) {
					product_list.add(product);
				}
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}

		if (product_list.size() > 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				processDownloadContentProduct();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_product_processing_empty);
				showCustomDialog(message);
			}

		} else {
			showListProduct();
		}
	}

	public void updateContentRefreshProduct() {
		product_list.clear();
		ArrayList<Product> product_from_db = databaseHandler.getAllProduct();
		if (product_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < product_from_db.size(); i++) {
				int id_product = product_from_db.get(i).getId_product();
				String nama_product = product_from_db.get(i).getNama_product();
				String kode_product = product_from_db.get(i).getKode_product();
				String harga_jual = product_from_db.get(i).getHarga_jual();
				String stock = product_from_db.get(i).getStock();
				String id_kemasan = product_from_db.get(i).getId_kemasan();
				String foto = product_from_db.get(i).getFoto();
				String deskripsi = product_from_db.get(i).getDeskripsi();
				String uomqtyl1 = product_from_db.get(i).getUomqtyl1();
				String uomqtyl2 = product_from_db.get(i).getUomqtyl2();
				String uomqtyl3 = product_from_db.get(i).getUomqtyl3();
				String uomqtyl4 = product_from_db.get(i).getUomqtyl4();
				String status = product_from_db.get(i).getStatus();

				Product product = new Product();
				product.setId_product(id_product);
				product.setNama_product(nama_product);
				product.setKode_product(kode_product);
				product.setHarga_jual(harga_jual);
				product.setStock(stock);
				product.setId_kemasan(id_kemasan);
				product.setFoto(foto);
				product.setDeskripsi(deskripsi);
				product.setUomqtyl1(uomqtyl1);
				product.setUomqtyl2(uomqtyl2);
				product.setUomqtyl3(uomqtyl3);
				product.setUomqtyl4(uomqtyl4);
				product.setStatus(status);
				File dir = new File(CONFIG.getFolderPath() + "/"
						+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/" + foto);
				if (dir.exists()) {
					dir.delete();
				}
				// if (!dir.exists()) {
				product_list.add(product);
				// }
			}

		} else {
			listview.setVisibility(View.INVISIBLE);
		}

		if (product_list.size() > 0) { // awalnya > 0
			if (GlobalApp.checkInternetConnection(act)) {
				processDownloadContentProduct();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_product_processing_empty);
				showCustomDialog(message);
			}

		} else {
			showListProduct();
		}
	}

	private class DownloadContentProduct extends
			AsyncTask<String, Integer, List<RowItem>> {
		List<RowItem> rowItems;
		int noOfURLs;

		protected List<RowItem> doInBackground(String... urls) {
			noOfURLs = urls.length;
			rowItems = new ArrayList<RowItem>();
			Bitmap map = null;
			for (String url : urls) {
				map = downloadImage(url.split("#")[0], url.split("#")[1]);
				rowItems.add(new RowItem(map));
			}
			return rowItems;
		}

		  private Bitmap downloadImage(String urlString, String fileName) {
			int count = 0;
			Bitmap bitmap = null;
			URL url;
			InputStream inputStream = null;
			BufferedOutputStream outputStream = null;
			OutputStream output = null;

			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			try {
				url = new URL(urlString);
				output = new FileOutputStream(dir + "/" + fileName);
				URLConnection connection = url.openConnection();
				int lenghtOfFile = connection.getContentLength();
				inputStream = new BufferedInputStream(url.openStream());
				ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
				outputStream = new BufferedOutputStream(dataStream);
				byte data[] = new byte[512];
				long total = 0;
				while ((count = inputStream.read(data)) != -1) {
					total += count;
					publishProgress((int) ((total * 100) / lenghtOfFile));
					outputStream.write(data, 0, count);
					output.write(data, 0, count);
				}
				outputStream.flush();
				BitmapFactory.Options bmOptions = new BitmapFactory.Options();
				bmOptions.inSampleSize = 1;
				byte[] bytes = dataStream.toByteArray();
				bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						bmOptions);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				FileUtils.close(output);
				FileUtils.close(inputStream);
				FileUtils.close(outputStream);
			}
			return bitmap;
		}

		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
			if (rowItems != null) {
				progressDialog.setMessage("Loading " + (rowItems.size() + 1)
						+ "/" + noOfURLs);
			}
		}

		protected void onPostExecute(List<RowItem> rowItems) {
			progressDialog.dismiss();
			showListProduct();
		}
	}

	public void processDownloadContentProduct() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_product_processing_download_content));
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setIndeterminate(false);
		progressDialog.setMax(100);
		progressDialog.setCancelable(true);
		progressDialog.show();
		DownloadContentProduct task = new DownloadContentProduct();
		List<String> stringImg = new ArrayList<String>();
		for (int i = 0; i < product_list.size(); i++) {
			String img1 = product_list.get(i).getFoto().replaceAll(" ", "%20");
			String download_image = CONFIG.CONFIG_APP_URL_DIR_IMG_PRODUCT
					+ img1 + "#" + product_list.get(i).getFoto();
			stringImg.add(download_image);
		}
		String[] imgArr = new String[stringImg.size()];
		imgArr = stringImg.toArray(imgArr);
		task.execute(imgArr);
	}

	public void showListProduct() {
		product_list.clear();
		ArrayList<Product> product_from_db = databaseHandler.getAllProduct();
		if (product_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < product_from_db.size(); i++) {
				int id_product = product_from_db.get(i).getId_product();
				String nama_product = product_from_db.get(i).getNama_product();
				String kode_product = product_from_db.get(i).getKode_product();
				String harga_jual = product_from_db.get(i).getHarga_jual();
				String stock = product_from_db.get(i).getStock();
				String id_kemasan = product_from_db.get(i).getId_kemasan();
				String foto = product_from_db.get(i).getFoto();
				String deskripsi = product_from_db.get(i).getDeskripsi();
				String uomqtyl1 = product_from_db.get(i).getUomqtyl1();
				String uomqtyl2 = product_from_db.get(i).getUomqtyl2();
				String uomqtyl3 = product_from_db.get(i).getUomqtyl3();
				String uomqtyl4 = product_from_db.get(i).getUomqtyl4();
				String status = product_from_db.get(i).getStatus();

				Product product = new Product();
				product.setId_product(id_product);
				product.setNama_product(nama_product);
				product.setKode_product(kode_product);
				product.setHarga_jual(harga_jual);
				product.setStock(stock);
				product.setId_kemasan(id_kemasan);
				product.setFoto(foto);
				product.setDeskripsi(deskripsi);
				product.setUomqtyl1(uomqtyl1);
				product.setUomqtyl2(uomqtyl2);
				product.setUomqtyl3(uomqtyl3);
				product.setUomqtyl4(uomqtyl4);
				product.setStatus(status);

				product_list.add(product);
			}

			cAdapter = new ListViewAdapter(this, R.layout.list_item_product,
					product_list);
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
				new DownloadDataProduct().execute();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_product_processing_empty);
				showCustomDialog(message);
			}
			return true;
			case R.id.menu_upload:
				if (GlobalApp.checkInternetConnection(act)) {
					int countUpload = databaseHandler.getCountProductWhereValidAndUpdate();
					if (countUpload == 0) {
						String message = act
								.getApplicationContext()
								.getResources()
								.getString(
										R.string.app_product_processing_upload_no_data);
						showCustomDialog(message);
					} else {
						new UploadData().execute();
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
								databaseHandler.updateStatusProduct();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_product_processing_upload));
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
					+ CONFIG.CONFIG_APP_URL_UPLOAD_PRODUCT ;

			List<Product> dataUpload = databaseHandler
					.getAllProductActiveAndUpdateByUser();
			for (Product product : dataUpload) {
				response = uploadProduct(upload_url,
						product.getNama_product(),
						product.getKode_product(),
						product.getHarga_jual(),
						product.getStock(),
						String.valueOf(product.getId_kemasan()),
						product.getFoto(),
						product.getDeskripsi(),
						product.getUomqtyl1(),
						product.getUomqtyl2(),
						product.getUomqtyl3(),
						product.getUomqtyl4());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {


			super.onPostExecute(result);
			if (response_data != null) {
				//initUploadCustomer();
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_product_processing_upload_success);
				CustomDialogUploadSuccess(msg);

			}
		}

	}

	private String uploadProduct(final String url,
								 final String nama_product, final String kode_product,
								 final String harga_jual, final String stock,
								 final String id_kemasan, final String foto,
								 final String deskripsi, final String uomqtyl1,
								 final String uomqtyl2, final String uomqtyl3,
								 final String uomqtyl4) {

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

			File sourceFoto = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/" + foto);
			if (sourceFoto.exists() && foto != null)
				entity.addPart("image", new FileBody(sourceFoto));

			entity.addPart("kode_product", new StringBody(kode_product));
			entity.addPart("nama_product", new StringBody(nama_product));
			entity.addPart("harga_jual", new StringBody(harga_jual));
			entity.addPart("stock", new StringBody(stock));
			entity.addPart("id_kemasan", new StringBody(id_kemasan));
			entity.addPart("foto", new StringBody(foto));
			entity.addPart("deskripsi", new StringBody(deskripsi));
			entity.addPart("uomqtyl1", new StringBody(uomqtyl1));
			entity.addPart("uomqtyl2", new StringBody(uomqtyl2));
			entity.addPart("uomqtyl3", new StringBody(uomqtyl3));
			entity.addPart("uomqtyl4", new StringBody(uomqtyl4));

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

	public class ListViewAdapter extends ArrayAdapter<Product> {
		Activity activity;
		int layoutResourceId;
		Product productData;
		ArrayList<Product> data = new ArrayList<Product>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<Product> data) {
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
				holder.list_title = (TextView) row.findViewById(R.id.title);
				holder.list_deskripsi = (TextView) row
						.findViewById(R.id.deskripsi);
				holder.list_img = (ImageView) row.findViewById(R.id.image);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_title.setText(productData.getNama_product());
			holder.list_deskripsi.setText(productData.getDeskripsi());
			holder.list_title.setTypeface(typefaceSmall);
			holder.list_deskripsi.setTypeface(typefaceSmall);
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/"
					+ data.get(position).getFoto());
			if (dir.exists()) {
				holder.list_img.setImageBitmap(BitmapFactory.decodeFile(dir
						.getAbsolutePath()));
			} else {
				Bitmap icon = BitmapFactory.decodeResource(
						activity.getResources(), R.drawable.ic_launcher);
				holder.list_img.setImageBitmap(icon);
			}
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String id_kuratif = String.valueOf(data.get(position)
							.getId_product());
					saveAppDataProductIdProduct(id_kuratif);
					gotoDetailProduct();
				}
			});
			return row;

		}

		class UserHolder {
			ImageView list_img;
			TextView list_title;
			TextView list_deskripsi;
		}

	}

	public void gotoDetailProduct() {
		Intent i = new Intent(this, DetailProductActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 2) {
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
