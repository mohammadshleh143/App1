package com.mahkota_company.android.inventory;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DetailProductTerjual;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.ProductTarget;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.SpinnerAdapter;
import com.mahkota_company.android.R;

public class DetailTargetPenjualanActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private EditText etNamaCustomerValue;
	private TextView tvHeaderNomorTP;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderKodeCustomer;
	private TextView tvNomorTpValue;
	private Button mButtonSave;
	private Button mButtonCancel;
	private Spinner spinnerKodeCustomerValue;
	private ArrayList<String> customerStringList;
	private ArrayList<DetailProductTerjual> detailProductTerjualList = new ArrayList<DetailProductTerjual>();
	private ListView listView;
	private ListViewAdapter cAdapter;
	private String nomerTp;
	private static final String LOG_TAG = DetailTargetPenjualanActivity.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_target_penjualan);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoTargetPenjualan();
			}
		});
		mButtonCancel = (Button) findViewById(R.id.detail_target_penjualan_btn_back);
		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoTargetPenjualan();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		listView = (ListView) findViewById(R.id.list);
		listView.setItemsCanFocus(false);
		spinnerKodeCustomerValue = (Spinner) findViewById(R.id.detail_target_penjualan_kode_customer_value);
		etNamaCustomerValue = (EditText) findViewById(R.id.detail_target_penjualan_nama_customer_value);
		tvNomorTpValue = (TextView) findViewById(R.id.detail_target_penjualan_value_nomor_tp);
		tvHeaderNomorTP = (TextView) findViewById(R.id.detail_target_penjualan_textview_nomor_tp);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.detail_target_penjualan_textview_kode_customer);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.detail_target_penjualan_textview_nama_customer);
		mButtonSave = (Button) findViewById(R.id.detail_target_penjualan_btn_add_penjualan);
		mButtonSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoPenjualanActivity();
			}
		});
		etNamaCustomerValue.setTypeface(typefaceSmall);
		tvHeaderNomorTP.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvNomorTpValue.setTypeface(typefaceSmall);
		customerStringList = new ArrayList<String>();

		try {
			SharedPreferences spPreferences = getSharedPrefereces();
			nomerTp = spPreferences
					.getString(
							CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NO_TARGET_PENJUALAN,
							null);
			if (nomerTp.length() > 0) {
				ArrayList<ProductTarget> productTarget_list = databaseHandler
						.getAllProductTargetWhereNomerPenjualan(nomerTp);
				if (productTarget_list != null) {
					Customer customer = databaseHandler
							.getCustomer(productTarget_list.get(0)
									.getId_customer());
					if (customer != null) {
						customerStringList.add(customer.getKode_customer());
						etNamaCustomerValue.setText(customer.getNama_lengkap());
						etNamaCustomerValue.setEnabled(false);
						SpinnerAdapter adapterCustomer = new SpinnerAdapter(
								getApplicationContext(),
								android.R.layout.simple_spinner_item,
								customerStringList);
						adapterCustomer
								.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
						spinnerKodeCustomerValue.setAdapter(adapterCustomer);
						spinnerKodeCustomerValue
								.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
									public void onItemSelected(
											AdapterView<?> parent, View view,
											int position, long id) {

									}

									public void onNothingSelected(
											AdapterView<?> parent) {

									}
								});
						spinnerKodeCustomerValue.setEnabled(false);
						saveAppDataTargetPenjualanIdCustomer(String
								.valueOf(customer.getId_customer()));
						saveAppDataTargetPenjualanKodeCustomer(customer
								.getKode_customer());
						saveAppDataTargetPenjualanNamaCustomer(customer
								.getNama_lengkap());
						showDetailTargetPenjualan(productTarget_list);
					} else {
						gotoTargetPenjualan();
					}
				} else {
					gotoTargetPenjualan();
				}
			} else {
				gotoTargetPenjualan();
			}
		} catch (Exception ex) {
			Log.d(LOG_TAG, "Exception:" + ex.getMessage());
		}
	}

	public void saveAppDataTargetPenjualanIdCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_ID_CUSTOMER,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTargetPenjualanKodeCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_KODE_CUSTOMER,
				responsedata);
		editor.commit();
	}

	public void saveAppDataTargetPenjualanNamaCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NAMA_CUSTOMER,
				responsedata);
		editor.commit();
	}

	private void showDetailTargetPenjualan(
			ArrayList<ProductTarget> productTarget_list) {
		detailProductTerjualList.clear();
		if (productTarget_list.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			for (int i = 0; i < productTarget_list.size(); i++) {
				int id_product = productTarget_list.get(i).getId_product();
				int jumlah_target = productTarget_list.get(i)
						.getJumlah_target();
				int jumlah_terjual = productTarget_list.get(i)
						.getJumlah_terjual();

				Product product = databaseHandler.getProduct(id_product);
				DetailProductTerjual detailProductTerjual = new DetailProductTerjual();
				detailProductTerjual.setId_product_target(id_product);
				detailProductTerjual.setId_product(id_product);
				detailProductTerjual.setJumlah_target(jumlah_target);
				detailProductTerjual.setJumlah_terjual(jumlah_terjual);
				detailProductTerjual.setKode_product(product.getKode_product());
				detailProductTerjual.setNama_product(product.getNama_product());
				detailProductTerjualList.add(detailProductTerjual);
			}
			cAdapter = new ListViewAdapter(DetailTargetPenjualanActivity.this,
					R.layout.list_item_detail_target_penjualan,
					detailProductTerjualList);
			listView.setAdapter(cAdapter);
			listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			cAdapter.notifyDataSetChanged();
		} else
			listView.setVisibility(View.INVISIBLE);

	}

	public class ListViewAdapter extends ArrayAdapter<DetailProductTerjual> {
		Activity activity;
		int layoutResourceId;
		ArrayList<DetailProductTerjual> data = new ArrayList<DetailProductTerjual>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<DetailProductTerjual> data) {
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
				holder.list_kode_product = (TextView) row
						.findViewById(R.id.detail_target_penjualan_kode_product);
				holder.list_nama_product = (TextView) row
						.findViewById(R.id.detail_target_penjualan_nama_product);
				holder.list_jumlah_target = (TextView) row
						.findViewById(R.id.detail_target_penjualan_jumlah);
				holder.list_jumlah_terjual = (TextView) row
						.findViewById(R.id.detail_target_penjualan_jumlah_terjual);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			holder.list_kode_product.setText(data.get(position)
					.getKode_product());
			holder.list_nama_product.setText(data.get(position)
					.getNama_product());
			holder.list_jumlah_target.setText(String.valueOf(data.get(position)
					.getJumlah_target()));
			holder.list_jumlah_terjual.setText(String.valueOf(data
					.get(position).getJumlah_terjual()));
			holder.list_kode_product.setTypeface(typefaceSmall);
			holder.list_nama_product.setTypeface(typefaceSmall);
			holder.list_jumlah_target.setTypeface(typefaceSmall);
			holder.list_jumlah_terjual.setTypeface(typefaceSmall);
			return row;

		}

		class UserHolder {
			TextView list_kode_product;
			TextView list_nama_product;
			TextView list_jumlah_target;
			TextView list_jumlah_terjual;
		}

	}

	public void showCustomDialog(String msg) {
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void gotoTargetPenjualan() {
		Intent i = new Intent(this, TargetPenjualanActivity.class);
		startActivity(i);
		finish();
	}

	public void gotoPenjualanActivity() {
		Intent i = new Intent(this, DetailPenjualanActivity.class);
		startActivity(i);
		finish();
	}

	public Typeface getTypefaceSmall() {
		return typefaceSmall;
	}

	public void setTypefaceSmall(Typeface typefaceSmall) {
		this.typefaceSmall = typefaceSmall;
	}
}
