package com.mahkota_company.android.inventory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mahkota_company.android.database.DetailProductTarget;
import com.mahkota_company.android.database.ProductTarget;
import com.mahkota_company.android.database.StockVan;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.SpinnerAdapter;
import com.mahkota_company.android.R;

public class AddTargetPenjualanActivity extends FragmentActivity {
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
	private ArrayList<Customer> customerList;
	private ArrayList<String> customerStringList;
	private String idCustomer;
	private ArrayList<DetailProductTarget> detailProductTargetList = new ArrayList<DetailProductTarget>();
	private ListView listView;
	private ListViewAdapter cAdapter;
	private static final String LOG_TAG = AddTargetPenjualanActivity.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_target_penjualan);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoTargetPenjualan();
			}
		});
		mButtonCancel = (Button) findViewById(R.id.activity_target_penjualan_btn_cancel);
		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoTargetPenjualan();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		listView = (ListView) findViewById(R.id.list);
		listView.setItemsCanFocus(false);
		spinnerKodeCustomerValue = (Spinner) findViewById(R.id.unload_product_target_penjualan_kode_customer_value);
		etNamaCustomerValue = (EditText) findViewById(R.id.unload_product_target_penjualan_nama_customer_value);
		tvNomorTpValue = (TextView) findViewById(R.id.unload_product_target_penjualan_value_nomor_tp);

		tvHeaderNomorTP = (TextView) findViewById(R.id.unload_product_target_penjualan_textview_nomor_tp);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.unload_product_target_penjualan_textview_kode_customer);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.unload_product_target_penjualan_textview_nama_customer);

		mButtonSave = (Button) findViewById(R.id.activity_target_penjualan_btn_save);

		etNamaCustomerValue.setTypeface(typefaceSmall);
		tvHeaderNomorTP.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvNomorTpValue.setTypeface(typefaceSmall);
		customerList = new ArrayList<Customer>();
		customerStringList = new ArrayList<String>();
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_id_wilayah = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH, null);
		String main_app_staff_username = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
		String main_app_id_staff = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);

		if (main_app_staff_id_wilayah.length() > 0
				&& main_app_staff_username.length() > 0
				&& main_app_id_staff.length() > 0) {
			String nomer_tp = CONFIG.CONFIG_APP_KODE_TP_HEADER
					+ main_app_staff_username + "." + main_app_id_staff + "."
					+ GlobalApp.getUniqueId();
			tvNomorTpValue.setText(nomer_tp);
			List<Customer> dataCustomer = databaseHandler
					.getAllCustomerActive(Integer
							.parseInt(main_app_staff_id_wilayah));
			for (Customer customer : dataCustomer) {
				/****
				 * Cek apakah ada customer yang existing di target penjualan
				 * Apabila sudah ada tidak perlu di masukkan di spinner
				 */
				int countCustomer = databaseHandler
						.getCountTargetPenjualan(String.valueOf(customer
								.getId_customer()));
				if (countCustomer == 0) {
					customerList.add(customer);
					customerStringList.add(customer.getKode_customer());
				}
			}
		} else {
			gotoTargetPenjualan();
		}
		etNamaCustomerValue.setEnabled(false);

		// ArrayAdapter<String> adapterCustomer = new ArrayAdapter<String>(this,
		// R.layout.my_spinner_item, customerStringList);
		// adapterCustomer
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpinnerAdapter adapterCustomer = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				customerStringList);
		adapterCustomer
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerKodeCustomerValue.setAdapter(adapterCustomer);
		spinnerKodeCustomerValue
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						try {
							Customer customer = databaseHandler
									.getCustomerByKodeCustomer(customerStringList
											.get(position));
							if (customer != null) {
								etNamaCustomerValue.setText(customer
										.getNama_lengkap());
								idCustomer = String.valueOf(customer
										.getId_customer());
							} else {
								idCustomer = "-1";
							}
						} catch (Exception e) {
							Log.d(LOG_TAG, "Exception :" + e.getMessage());
						}
					}

					public void onNothingSelected(AdapterView<?> parent) {

					}
				});
		try {
			Customer customer = databaseHandler
					.getCustomerByKodeCustomer(customerStringList.get(0));
			if (customer != null) {
				idCustomer = String.valueOf(customer.getId_customer());
				etNamaCustomerValue.setText(customer.getNama_lengkap());
			} else {
				idCustomer = "-1";
			}
		} catch (Exception e) {
			Log.d(LOG_TAG, "Exception :" + e.getMessage());
		}
		mButtonSave.setOnClickListener(maddButtonOnClickListener);
		showProductTargetPenjualan();

	}

	protected void showProductTargetPenjualan() {
		detailProductTargetList.clear();
		ArrayList<StockVan> stockvan_from_db = databaseHandler.getAllStockVan();
		if (stockvan_from_db.size() > 0) {
			listView.setVisibility(View.VISIBLE);
			for (int i = 0; i < stockvan_from_db.size(); i++) {
				int id_product = stockvan_from_db.get(i).getId_product();
				String nama_product = stockvan_from_db.get(i).getNama_product();
				String kode_product = stockvan_from_db.get(i).getKode_product();
				String harga_jual = stockvan_from_db.get(i).getHarga_jual();
				String jumlahAccept = stockvan_from_db.get(i).getJumlahAccept();
				String jumlahSisa = stockvan_from_db.get(i).getJumlahSisa();
				String id_kemasan = stockvan_from_db.get(i).getIdKemasan();
				String foto = stockvan_from_db.get(i).getFoto();
				String deskripsi = stockvan_from_db.get(i).getDeskripsi();

				StockVan stockVan = new StockVan();
				stockVan.setId_product(id_product);
				stockVan.setNama_product(nama_product);
				stockVan.setKode_product(kode_product);
				stockVan.setHarga_jual(harga_jual);
				stockVan.setJumlahAccept(jumlahAccept);
				stockVan.setJumlahSisa(jumlahSisa);
				stockVan.setIdKemasan(id_kemasan);
				stockVan.setFoto(foto);
				stockVan.setDeskripsi(deskripsi);

				DetailProductTarget detailProductTarget = new DetailProductTarget();
				detailProductTarget.setId_product_target(id_product);
				detailProductTarget.setId_product(id_product);
				detailProductTarget.setJumlah_stok_van(jumlahAccept);
				detailProductTarget.setJumlah_target(0);
				detailProductTarget.setKode_product(kode_product);
				detailProductTarget.setNama_product(nama_product);
				detailProductTargetList.add(detailProductTarget);
			}

			cAdapter = new ListViewAdapter(AddTargetPenjualanActivity.this,
					R.layout.list_item_add_target_penjualan,
					detailProductTargetList);
			listView.setAdapter(cAdapter);
			listView.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
			cAdapter.notifyDataSetChanged();
		} else
			listView.setVisibility(View.INVISIBLE);

	}

	public void showCustomDialogSaveSuccess(String msg) {
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
								gotoTargetPenjualan();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	private final OnClickListener maddButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_sales_order_btn_save:
				final String date2 = "yyyy-MM-dd";
				Calendar calendar2 = Calendar.getInstance();
				SimpleDateFormat dateFormat2 = new SimpleDateFormat(date2);
				final String checkDate = dateFormat2
						.format(calendar2.getTime());
				Calendar now = Calendar.getInstance();
				int hrs = now.get(Calendar.HOUR_OF_DAY);
				int min = now.get(Calendar.MINUTE);
				int sec = now.get(Calendar.SECOND);
				final String time = zero(hrs) + "-" + zero(min) + "-"
						+ zero(sec);

				if (idCustomer.equalsIgnoreCase("-1")) {
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_save_error);
					showCustomDialog(msg);
				} else {
					ArrayList<DetailProductTarget> detailProductTargetlist = cAdapter
							.getDataList();
					for (DetailProductTarget detailProductTarget : detailProductTargetlist) {
						if (detailProductTarget.getJumlah_target() != 0) {
							SharedPreferences spPreferences = getSharedPrefereces();
							String main_app_id_staff = spPreferences.getString(
									CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
									null);
							int tempCounter = databaseHandler
									.getCountTargetPenjualan() + 1;
							databaseHandler.addProductTarget(new ProductTarget(
									tempCounter, tvNomorTpValue.getText()
											.toString(), checkDate, time,
									checkDate, time, Integer
											.parseInt(main_app_id_staff),
									Integer.parseInt(main_app_id_staff),
									Integer.parseInt(main_app_id_staff),
									Integer.parseInt(idCustomer),
									detailProductTarget.getId_product(),
									detailProductTarget.getJumlah_target(), 0));
							Log.d(LOG_TAG, "jumlah target:"
									+ detailProductTarget.getJumlah_target());
						}
					}
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_save_success);
					showCustomDialogSaveSuccess(msg);
				}

				break;
			default:
				break;
			}
		}

	};

	public class ListViewAdapter extends ArrayAdapter<DetailProductTarget> {
		Activity activity;
		int layoutResourceId;
		ArrayList<DetailProductTarget> data = new ArrayList<DetailProductTarget>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<DetailProductTarget> data) {
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
						.findViewById(R.id.target_penjualan_kode_product);
				holder.titleNamaProduct = (TextView) row
						.findViewById(R.id.target_penjualan_nama_product);
				holder.titleStokVan = (TextView) row
						.findViewById(R.id.target_penjualan_jumlah_stock_van);
				holder.edtTargetPenjualan = (EditText) row
						.findViewById(R.id.target_penjualan_jumlah);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			holder.titleKodeProduct.setText(data.get(position)
					.getKode_product());
			holder.titleNamaProduct.setText(data.get(position)
					.getNama_product());
			holder.titleStokVan
					.setText(data.get(position).getJumlah_stok_van());
			holder.titleKodeProduct.setTypeface(typefaceSmall);
			holder.titleNamaProduct.setTypeface(typefaceSmall);
			holder.titleStokVan.setTypeface(typefaceSmall);
			holder.edtTargetPenjualan.addTextChangedListener(new TextWatcher() {

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
						data.get(position).setJumlah_target(0);
					} else {
						int tempCurentValue = Integer.parseInt(arg0.toString());
						int tempMaxValue = Integer.parseInt(data.get(position).getJumlah_stok_van());
						if (tempCurentValue > tempMaxValue) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_inventory_unload_product_target_penjualan_failed_input_value);
							showCustomDialog(msg + tempMaxValue);
							int newTempCurrentValue = tempCurentValue - 1;
							data.get(position).setJumlah_target(
									newTempCurrentValue);
						} else {
							data.get(position).setJumlah_target(
									Integer.parseInt(arg0.toString()));
						}
					}
				}
			});

			return row;

		}

		class UserHolder {
			TextView titleKodeProduct;
			TextView titleNamaProduct;
			TextView titleStokVan;
			EditText edtTargetPenjualan;
		}

		public ArrayList<DetailProductTarget> getDataList() {
			return data;
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

	public Typeface getTypefaceSmall() {
		return typefaceSmall;
	}

	public void setTypefaceSmall(Typeface typefaceSmall) {
		this.typefaceSmall = typefaceSmall;
	}
}
