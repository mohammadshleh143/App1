package com.mahkota_company.android.inventory;

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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.R;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.ProductPrice;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.product.DetailProductActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.prospect.CustomerProspectActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.FileUtils;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.RowItem;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

@SuppressWarnings("deprecation")
public class ProductPriceActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<ProductPrice> product_price_list = new ArrayList<ProductPrice>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private static final String LOG_TAG = ProductPriceActivity.class.getSimpleName();
	private Typeface typefaceSmall;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_product_price);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
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
		int countProductPrice = databaseHandler.getCountProductPrice();

		if (countProductPrice == 0) {
			if (GlobalApp.checkInternetConnection(act)) {
				new DownloadDataProductPrice().execute();
			} else {
				String message = act.getApplicationContext().getResources()
						.getString(R.string.app_product_processing_empty);
				showCustomDialog(message);
			}
		} else {
			showListProductPrice();
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

	private class DownloadDataProductPrice extends
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
					+ CONFIG.CONFIG_APP_URL_DOWNLOAD_PRODUCT_PRICE;
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
							CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_PRICE, null);
					if (main_app_table_data != null) {
						if (main_app_table_data.equalsIgnoreCase(response_data)) {
							saveAppDataProductPriceSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_true));
						} else {
							databaseHandler.deleteTableProductPrice();
							saveAppDataProductPriceSameData(act
									.getApplicationContext().getResources()
									.getString(R.string.app_value_false));
						}
					} else {
						databaseHandler.deleteTableProductPrice();
						saveAppDataProductPriceSameData(act.getApplicationContext()
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
				saveAppDataProductPrice(response_data);
				extractDataProductPrice();
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
								//gotoInventory();

							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public void extractDataProductPrice() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_same_data = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_PRICE_SAME_DATA, null);
		String main_app_table = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_PRICE, null);
		if (main_app_table_same_data.equalsIgnoreCase(act
				.getApplicationContext().getResources()
				.getString(R.string.app_value_false))) {
			JSONObject oResponse;
			try {
				oResponse = new JSONObject(main_app_table);
				JSONArray jsonarr = oResponse.getJSONArray("productprice"); //array berbentuk JSON dari web service
				for (int i = 0; i < jsonarr.length(); i++) {
					JSONObject oResponsealue = jsonarr.getJSONObject(i);
					String id = oResponsealue.isNull("id") ? null
							: oResponsealue.getString("id");
					String id_product = oResponsealue.isNull("id_product") ? null
							: oResponsealue.getString("id_product");
					String pricelist = oResponsealue.isNull("pricelist") ? null
							: oResponsealue.getString("pricelist");
					String pricestd = oResponsealue.isNull("pricestd") ? null
							: oResponsealue.getString("pricestd");
					String pricelimit = oResponsealue.isNull("pricelimit") ? null
							: oResponsealue.getString("pricelimit");

					Log.d(LOG_TAG, "id:" + id);
					Log.d(LOG_TAG, "id_product:" + id_product);
					Log.d(LOG_TAG, "pricelist:" + pricelist);
					Log.d(LOG_TAG, "pricestd:" + pricestd);
					Log.d(LOG_TAG, "pricelimit:" + pricelimit);

					databaseHandler.add_ProductPrice(new ProductPrice(Integer
							.parseInt(id), id_product, pricelist,
							pricestd, pricelimit));
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

	public void saveAppDataProductPrice(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_PRICE, responsedata);
		editor.commit();
	}

	public void saveAppDataProductPriceSameData(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_PRICE_SAME_DATA,
				responsedata);
		editor.commit();
	}

	public void saveAppDataProductPriceIdProduct(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_PRODUCT_PRICE_ID_PRODUCT,
				responsedata);
		editor.commit();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showListProductPrice() {
		product_price_list.clear();
		ArrayList<ProductPrice> price_from_db = databaseHandler.getAllProductPrice();
		if (price_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < price_from_db.size(); i++) {
				int id = price_from_db.get(i).getId();
				String id_product = price_from_db.get(i).getId_product();
				String pricelist = price_from_db.get(i).getPricelist();
				String pricestd = price_from_db.get(i).getPricestd();
				String pricelimit = price_from_db.get(i).getPricelimit();

				ProductPrice productPrice = new ProductPrice();
				productPrice.setId(id);
				productPrice.setId_product(id_product);
				productPrice.setPricelist(pricelist);
				productPrice.setPricestd(pricestd);
				productPrice.setPricelimit(pricelimit);

				product_price_list.add(productPrice);
			}

			cAdapter = new ListViewAdapter(this, R.layout.list_item_product_price,
					product_price_list);
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

	public class ListViewAdapter extends ArrayAdapter<ProductPrice> {
		Activity activity;
		int layoutResourceId;
		ProductPrice productPriceData;
		ArrayList<ProductPrice> data = new ArrayList<ProductPrice>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<ProductPrice> data) {
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
			productPriceData= data.get(position);
			holder.list_title.setText(productPriceData.getId_product());
			holder.list_deskripsi.setText(productPriceData.getPricelimit());
			holder.list_title.setTypeface(typefaceSmall);
			holder.list_deskripsi.setTypeface(typefaceSmall);

			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String id_kuratif = String.valueOf(data.get(position)
							.getId_product());
					saveAppDataProductPriceIdProduct(id_kuratif);
					//gotoDetailProduct();
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

	public void gotoInventory() {
		Intent i = new Intent(this, InventoryActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		MenuItem item = menu.findItem(R.id.menu_upload);
		if (item != null) {
			item.setVisible(false);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_refresh:
				if (GlobalApp.checkInternetConnection(act)) {
					new DownloadDataProductPrice().execute();
				} else {
					String message = act.getApplicationContext().getResources()
							.getString(R.string.app_product_processing_empty);
					showCustomDialog(message);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
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
				}/* else if (position == 4) {
					Intent intentActivity = new Intent(this,
							LocatorStaffActivity.class);
					startActivity(intentActivity);
					finish();
				}*/ else if (position == 5) {
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
				}/*else if (position == 8) {
					Intent intentActivity = new Intent(this,
							ContactActivty.class);
					startActivity(intentActivity);
					finish();
				}*/else if (position == 9) {
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
				}*/else if (position == 12) {
					Intent intentActivity = new Intent(this,
							CheckCustomer.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 13) {
					Intent intentActivity = new Intent(this,
							CheckCustomerProspectActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 14) {
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
