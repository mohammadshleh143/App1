package com.mahkota_company.android.inventory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DetailPenjualan;
import com.mahkota_company.android.database.Penjualan;
import com.mahkota_company.android.database.PenjualanDetail;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.ProductTarget;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.R;

public class DetailPenjualanActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private EditText etKodeCustomer;
	private EditText etNamaCustomer;
	private EditText etDiskonValue;

	private TextView tvNoPenjualanValue;
	private TextView tvTotalbayarValue;
	private TextView tvHeaderTotalbayarTitle;

	private TextView tvHeaderNoPenjualan;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderDiskonCustomer;

	private Button mButtonAddProduct;
	private Button mButtonSave;
	private Button mButtonCancel;

	private ListViewChooseAdapter cAdapterChooseAdapter;
	private ArrayList<DetailPenjualan> detailPenjualanList = new ArrayList<DetailPenjualan>();
	private ArrayList<Penjualan> penjualanList = new ArrayList<Penjualan>();
	private ArrayList<PenjualanDetail> penjualanDetailList = new ArrayList<PenjualanDetail>();
	private ListView listview;
	private ListViewAdapter cAdapter;

	private String main_app_id_customer;
	private String main_app_nomer_tp;
	private String main_app_kode_customer;
	private String main_app_nama_customer;

	private static final String LOG_TAG = DetailPenjualanActivity.class
			.getSimpleName();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_penjualan);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoDetailTargetPenjualan();
			}
		});
		mButtonCancel = (Button) findViewById(R.id.add_penjualan_btn_cancel);
		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoDetailTargetPenjualan();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		tvNoPenjualanValue = (TextView) findViewById(R.id.add_penjualan_value_nomor_tp);
		etKodeCustomer = (EditText) findViewById(R.id.add_penjualan_kode_customer_value);
		etNamaCustomer = (EditText) findViewById(R.id.add_penjualan_nama_customer_value);
		etDiskonValue = (EditText) findViewById(R.id.add_penjualan_diskon_value);

		tvHeaderNoPenjualan = (TextView) findViewById(R.id.add_penjualan_textview_nomor_tp);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.add_penjualan_kode_customer_title);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.add_penjualan_nama_customer_title);
		tvHeaderDiskonCustomer = (TextView) findViewById(R.id.add_penjualan_diskon_title);

		tvTotalbayarValue = (TextView) findViewById(R.id.add_penjualan_total_bayar_value);
		tvHeaderTotalbayarTitle = (TextView) findViewById(R.id.add_penjualan_total_bayar_title);

		mButtonAddProduct = (Button) findViewById(R.id.add_penjualan_btn_add_product);
		mButtonSave = (Button) findViewById(R.id.add_penjualan_btn_save);

		etNamaCustomer.setTypeface(typefaceSmall);
		etKodeCustomer.setTypeface(typefaceSmall);
		etDiskonValue.setTypeface(typefaceSmall);
		tvNoPenjualanValue.setTypeface(typefaceSmall);
		tvHeaderNoPenjualan.setTypeface(typefaceSmall);
		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderDiskonCustomer.setTypeface(typefaceSmall);
		tvTotalbayarValue.setTypeface(typefaceSmall);
		tvHeaderTotalbayarTitle.setTypeface(typefaceSmall);
		SharedPreferences spPreferences = getSharedPrefereces();
		main_app_id_customer = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_ID_CUSTOMER,
				null);
		main_app_nomer_tp = spPreferences
				.getString(
						CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NO_TARGET_PENJUALAN,
						null);
		main_app_kode_customer = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_KODE_CUSTOMER,
				null);
		main_app_nama_customer = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NAMA_CUSTOMER,
				null);
		if (main_app_nomer_tp.length() > 0
				&& main_app_kode_customer.length() > 0
				&& main_app_nama_customer.length() > 0) {
			etNamaCustomer.setText(main_app_nama_customer);
			etKodeCustomer.setText(main_app_kode_customer);
			tvNoPenjualanValue.setText(main_app_nomer_tp);
			showDetailOrder(main_app_nomer_tp);
		} else {
			gotoDetailTargetPenjualan();
		}
		etNamaCustomer.setEnabled(false);
		etKodeCustomer.setEnabled(false);
		mButtonSave.setOnClickListener(buttonOnClickListener);
		mButtonAddProduct.setOnClickListener(buttonOnClickListener);

	}

	private void showDetailOrder(String main_app_nomer_order) {
		penjualanList.clear();
		penjualanDetailList.clear();
		penjualanList = databaseHandler
				.getAllPenjualanOrderWhereNomerPenjualan(main_app_nomer_order);
		penjualanDetailList = databaseHandler
				.getAllPenjualanDetailWhereNomerPenjualan(main_app_nomer_order);
		if (penjualanList.size() > 0 && penjualanDetailList.size() > 0) {
			etDiskonValue.setText(String.valueOf(penjualanList.get(0)
					.getDiskon()));
			int count = detailPenjualanList.size() + 1;
			try {
				Product product = databaseHandler
						.getProduct(penjualanDetailList.get(0).getIdProduct());
				for (PenjualanDetail penjualanDetail : penjualanDetailList) {
					updateListViewDetailOrder(new DetailPenjualan(count,
							product.getId_product(), product.getNama_product(),
							product.getKode_product(), product.getHarga_jual(),
							penjualanDetail.getJumlah()));
					count += 1;
				}
				updateTotalBayar();
			} catch (Exception e) {
				Log.d(LOG_TAG, "Exception:" + e.getMessage());
			}
		}

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
								gotoDetailTargetPenjualan();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	private final OnClickListener buttonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.add_penjualan_btn_add_product:
				int countProduct = databaseHandler.getCountProduct();
				if (countProduct == 0) {
					String msg = getApplicationContext()
							.getResources()
							.getString(R.string.app_sales_order_no_data_product);
					showCustomDialog(msg);
				} else {
					ChooseProductDialog();
				}
				break;
			case R.id.add_penjualan_btn_save:
				if (etDiskonValue.getText().toString().length() > 0) {
					int countPenjualan = databaseHandler
							.getCountPenjualan(main_app_nomer_tp);
					if (countPenjualan > 0) {
						databaseHandler.deleteTablePenjualan(main_app_nomer_tp);
					}

					ArrayList<Penjualan> tempPenjualan_list = databaseHandler
							.getAllPenjualan();
					int tempIndexPenjualan = 0;
					for (Penjualan salesOrder : tempPenjualan_list) {
						tempIndexPenjualan = salesOrder.getId_penjualan();
					}
					SharedPreferences spPreferences = getSharedPrefereces();
					String id_staff = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);

					final String date2 = "yyyy-MM-dd";
					Calendar calendar2 = Calendar.getInstance();
					SimpleDateFormat dateFormat2 = new SimpleDateFormat(date2);
					final String checkDate = dateFormat2.format(calendar2
							.getTime());
					Calendar now = Calendar.getInstance();
					int hrs = now.get(Calendar.HOUR_OF_DAY);
					int min = now.get(Calendar.MINUTE);
					int sec = now.get(Calendar.SECOND);
					final String time = zero(hrs) + ":" + zero(min) + ":"
							+ zero(sec);

					Penjualan penjualan = new Penjualan();
					penjualan.setId_penjualan(tempIndexPenjualan);
					penjualan.setNomer_product_terjual(main_app_nomer_tp);
					penjualan.setDate_product_terjual(checkDate);
					penjualan.setTime_product_terjual(time);
					penjualan.setId_customer(Integer
							.parseInt(main_app_id_customer));
					penjualan.setId_staff(Integer.parseInt(id_staff));
					penjualan.setDiskon(Integer.parseInt(etDiskonValue
							.getText().toString()));

					databaseHandler.addPenjualan(penjualan);
					ArrayList<PenjualanDetail> tempPenjualanDetail_list = databaseHandler
							.getAllPenjualanDetail();

					if (tempPenjualanDetail_list.size() > 0) {
						databaseHandler
								.deleteTablePenjualanDetail(main_app_nomer_tp);
					}

					int tempIndex = 0;
					for (PenjualanDetail penjualDetail : tempPenjualanDetail_list) {
						tempIndex = penjualDetail.getId_penjualan_detail();
					}
					int index = 1;
					for (DetailPenjualan detailPenjualan : detailPenjualanList) {
						if (databaseHandler.getCountTargetPenjualan(
								main_app_nomer_tp,
								detailPenjualan.getId_product()) == 0) {
							int tempCounter = databaseHandler
									.getCountTargetPenjualan() + 1;
							databaseHandler.addProductTarget(new ProductTarget(
									tempCounter, main_app_nomer_tp, checkDate,
									time, checkDate, time, Integer
											.parseInt(id_staff), Integer
											.parseInt(id_staff), Integer
											.parseInt(id_staff), Integer
											.parseInt(main_app_id_customer),
									detailPenjualan.getId_product(),
									detailPenjualan.getJumlah_order(),
									detailPenjualan.getJumlah_order()));
						} else {
							databaseHandler.updateTargetPenjualan(
									detailPenjualan.getId_product(),
									detailPenjualan.getJumlah_order(),
									checkDate, time, Integer
									.parseInt(id_staff),
									main_app_nomer_tp);
						}
						PenjualanDetail penjualanDetail = new PenjualanDetail();
						penjualanDetail.setId_penjualan_detail(tempIndex
								+ index);
						penjualanDetail.setIdProduct(detailPenjualan
								.getId_product());
						penjualanDetail.setJumlah(detailPenjualan
								.getJumlah_order());
						penjualanDetail
								.setNomer_product_terjual(tvNoPenjualanValue
										.getText().toString());
						databaseHandler.addPenjualanDetail(penjualanDetail);
						index += 1;
					}
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_add_save_success);
					showCustomDialogSaveSuccess(msg);
				} else {
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_inventory_unload_product_target_penjualan_add_save_failed_no_data);
					showCustomDialog(msg);
				}

				break;
			default:
				break;
			}
		}

	};

	private void ChooseProductDialog() {
		final Dialog chooseProductDialog = new Dialog(act);
		chooseProductDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseProductDialog
				.setContentView(R.layout.activity_main_product_choose_dialog);
		chooseProductDialog.setCanceledOnTouchOutside(false);
		chooseProductDialog.setCancelable(true);
		chooseProductDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						chooseProductDialog.dismiss();
					}
				});
		TextView tvHeaderKodeProduct = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_kode_product);
		TextView tvHeaderNamaProduct = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_nama_product);
		TextView tvHeaderHargaProduct = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_harga_product);
		TextView tvHeaderAction = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_action);
		tvHeaderKodeProduct.setTypeface(typefaceSmall);
		tvHeaderNamaProduct.setTypeface(typefaceSmall);
		tvHeaderHargaProduct.setTypeface(typefaceSmall);
		tvHeaderAction.setTypeface(typefaceSmall);
		EditText searchProduct = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_search);
		final ArrayList<Product> product_list = new ArrayList<Product>();
		final ListView listview = (ListView) chooseProductDialog
				.findViewById(R.id.list);
		final EditText jumlahProduct = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pieces);

		listview.setItemsCanFocus(false);
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
				cAdapterChooseAdapter = new ListViewChooseAdapter(
						DetailPenjualanActivity.this,
						R.layout.list_item_product_sales_order, jumlahProduct,
						product_list, chooseProductDialog);
				listview.setAdapter(cAdapterChooseAdapter);
				cAdapterChooseAdapter.notifyDataSetChanged();
			}
		} else {
			listview.setVisibility(View.INVISIBLE);
		}

		searchProduct.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.toString().length() > 0) {
					product_list.clear();
					ArrayList<Product> product_from_db = databaseHandler
							.getAllProductBaseOnSearch(cs.toString());
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
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									DetailPenjualanActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct, product_list,
									chooseProductDialog);
							listview.setAdapter(cAdapterChooseAdapter);
							cAdapterChooseAdapter.notifyDataSetChanged();
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
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
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									DetailPenjualanActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct, product_list,
									chooseProductDialog);
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
		Handler handler = new Handler();
		handler.post(new Runnable() {
			public void run() {
				chooseProductDialog.show();
			}
		});
	}

	private void updateListViewDetailOrder(DetailPenjualan detailPenjualan) {
		detailPenjualanList.add(detailPenjualan);
		cAdapter = new ListViewAdapter(DetailPenjualanActivity.this,
				R.layout.list_item_detail_sales_order, detailPenjualanList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
		updateTotalBayar();
	}

	private void updateTotalBayar() {
		int totalBayar = 0;
		for (DetailPenjualan tempDetailPenjualan : detailPenjualanList) {
			totalBayar += Integer.parseInt(tempDetailPenjualan.getHarga_jual())
					* tempDetailPenjualan.getJumlah_order();
		}

		int totalDiskon = 0;
		if (etDiskonValue.getText().toString().length() > 0)
			totalDiskon = Integer.parseInt(etDiskonValue.getText().toString());

		if (totalBayar > 0) {
			totalBayar = totalBayar - totalDiskon;
		}
		String totalBayarTemp = String.valueOf(totalBayar);
		Float priceIDR = Float.valueOf(totalBayarTemp);
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
		tvTotalbayarValue.setText("Rp. " + df.format(priceIDR));
	}

	public class ListViewAdapter extends ArrayAdapter<DetailPenjualan> {
		Activity activity;
		int layoutResourceId;
		ArrayList<DetailPenjualan> data = new ArrayList<DetailPenjualan>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<DetailPenjualan> data) {
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
						.findViewById(R.id.sales_order_title_kode_product);
				holder.list_jumlah_order = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order);
				holder.list_harga_jual = (TextView) row
						.findViewById(R.id.sales_order_title_harga);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			holder.list_kode_product.setText(data.get(position)
					.getKode_product());
			holder.list_jumlah_order.setText(String.valueOf(data.get(position)
					.getJumlah_order()));
			Float priceIDR = Float.valueOf(data.get(position).getHarga_jual());
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
			holder.list_harga_jual.setText("Rp. " + df.format(priceIDR));
			holder.list_kode_product.setTypeface(typefaceSmall);
			holder.list_jumlah_order.setTypeface(typefaceSmall);
			holder.list_harga_jual.setTypeface(typefaceSmall);

			return row;

		}

		class UserHolder {
			TextView list_kode_product;
			TextView list_jumlah_order;
			TextView list_harga_jual;
		}

	}

	class ListViewChooseAdapter extends ArrayAdapter<Product> {
		int layoutResourceId;
		Product productData;
		ArrayList<Product> data = new ArrayList<Product>();
		Activity mainActivity;
		EditText jumlahProduct;
		Dialog chooseProductDialog;

		public ListViewChooseAdapter(Activity mainActivity,
				int layoutResourceId, EditText jumlahProduct,
				ArrayList<Product> data, Dialog chooseProductDialog) {
			super(mainActivity, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.data = data;
			this.chooseProductDialog = chooseProductDialog;
			this.mainActivity = mainActivity;
			this.jumlahProduct = jumlahProduct;
			notifyDataSetChanged();
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View row = convertView;
			UserHolder holder = null;

			if (row == null) {
				LayoutInflater inflater = LayoutInflater.from(mainActivity);

				row = inflater.inflate(layoutResourceId, parent, false);
				holder = new UserHolder();
				holder.list_kodeProduct = (TextView) row
						.findViewById(R.id.sales_order_title_kode_product);
				holder.list_namaProduct = (TextView) row
						.findViewById(R.id.sales_order_title_nama_product);
				holder.list_harga = (TextView) row
						.findViewById(R.id.sales_order_title_harga_product);
				holder.mButtonAddItem = (Button) row
						.findViewById(R.id.sales_order_dialog_btn_add_item);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_kodeProduct.setText(productData.getKode_product());
			holder.list_namaProduct.setText(productData.getNama_product());
			Float priceIDR = Float.valueOf(productData.getHarga_jual());
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
			holder.list_harga.setText("Rp. " + df.format(priceIDR));
			holder.list_kodeProduct.setTypeface(getTypefaceSmall());
			holder.list_namaProduct.setTypeface(getTypefaceSmall());
			holder.list_harga.setTypeface(getTypefaceSmall());
			holder.mButtonAddItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (jumlahProduct.getText().toString().length() > 0) {
						boolean containSameProduct = false;
						for (DetailPenjualan detailPenjualan : detailPenjualanList) {
							if (detailPenjualan.getKode_product()
									.equalsIgnoreCase(
											data.get(position)
													.getKode_product())) {
								containSameProduct = true;
								break;
							}
						}
						if (containSameProduct) {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_another_item);
							showCustomDialog(msg);
						} else {
							int count = detailPenjualanList.size() + 1;
							updateListViewDetailOrder(new DetailPenjualan(
									count, data.get(position).getId_product(),
									data.get(position).getNama_product(), data
											.get(position).getKode_product(),
									data.get(position).getHarga_jual(), Integer
											.parseInt(jumlahProduct.getText()
													.toString())));
							chooseProductDialog.hide();
						}
					} else {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_order_failed_please_add_jumlah);
						showCustomDialog(msg);

					}
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeProduct;
			TextView list_namaProduct;
			TextView list_harga;
			Button mButtonAddItem;
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

	public void gotoDetailTargetPenjualan() {
		Intent i = new Intent(this, DetailTargetPenjualanActivity.class);
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
