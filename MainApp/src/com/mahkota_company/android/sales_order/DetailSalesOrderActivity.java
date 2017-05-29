package com.mahkota_company.android.sales_order;

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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DetailSalesOrder;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.SalesOrder;
import com.mahkota_company.android.inventory.DetailRequest;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.SpinnerAdapter;
import com.mahkota_company.android.R;

public class DetailSalesOrderActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private EditText etNamaCustomer;
	private EditText etAlamatCustomer;
	private EditText etDeskripsiPhotoPurchase;
	private TextView tvHeaderKodeCustomer;
	private TextView tvTotalbayarValue;
	private TextView tvHeaderTotalbayarTitle;
	private TextView tvTotalDiskonValue;
	private TextView tvHeaderTotalDiskonTitle;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderAlamatCustomer;
	private TextView tvHeaderDeskripsiPhotoPurchase;
	private Button mButtonAddProduct;
	private Button mButtonSave;
	private Button mButtonCancel;
	private EditText etKodeCustomer;
	private Spinner spinnerPromosi;
	public ArrayList<String> promosiStringList;
	private int idPromosi = -1;
	private ListViewChooseAdapter cAdapterChooseAdapter;
	public ArrayList<DetailSalesOrder> detailSalesOrderList = new ArrayList<DetailSalesOrder>();
	public ArrayList<SalesOrder> salesOrderList = new ArrayList<SalesOrder>();
	private ListView listview;
	private ListViewAdapter cAdapter;
	private String nomerOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_sales_order);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoSalesOrder();
			}
		});
		mButtonCancel = (Button) findViewById(R.id.activity_sales_order_btn_cancel);
		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoSalesOrder();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		etKodeCustomer = (EditText) findViewById(R.id.activity_sales_order_kode_customer_value);
		spinnerPromosi = (Spinner) findViewById(R.id.activity_sales_order_promosi_value);
		etNamaCustomer = (EditText) findViewById(R.id.activity_sales_order_nama_customer_value);
		etAlamatCustomer = (EditText) findViewById(R.id.activity_sales_order_alamat_value);
		etDeskripsiPhotoPurchase = (EditText) findViewById(R.id.activity_sales_order_deskripsi_value);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_app_sales_order_kode_customer_title);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_app_sales_order_nama_customer_title);
		tvHeaderAlamatCustomer = (TextView) findViewById(R.id.activity_app_sales_order_alamat_title);
		tvHeaderDeskripsiPhotoPurchase = (TextView) findViewById(R.id.activity_app_sales_order_deskripsi_title);
		tvTotalbayarValue = (TextView) findViewById(R.id.activity_app_sales_order_total_bayar_value);
		tvHeaderTotalbayarTitle = (TextView) findViewById(R.id.activity_app_sales_order_total_bayar_title);
		tvTotalDiskonValue = (TextView) findViewById(R.id.activity_app_sales_order_total_diskon_value);
		tvHeaderTotalDiskonTitle = (TextView) findViewById(R.id.activity_app_sales_order_total_diskon_title);
		mButtonAddProduct = (Button) findViewById(R.id.activity_sales_order_btn_add_product);
		mButtonSave = (Button) findViewById(R.id.activity_sales_order_btn_save);

		etNamaCustomer.setTypeface(typefaceSmall);
		etAlamatCustomer.setTypeface(typefaceSmall);
		etDeskripsiPhotoPurchase.setTypeface(typefaceSmall);
		tvTotalDiskonValue.setTypeface(typefaceSmall);
		tvHeaderTotalDiskonTitle.setTypeface(typefaceSmall);
		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderAlamatCustomer.setTypeface(typefaceSmall);
		tvHeaderDeskripsiPhotoPurchase.setTypeface(typefaceSmall);
		tvHeaderTotalbayarTitle.setTypeface(typefaceSmall);
		tvTotalbayarValue.setTypeface(typefaceSmall);

		promosiStringList = new ArrayList<String>();
		promosiStringList.add("Tidak Ada");

		SpinnerAdapter adapterPromosi = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				promosiStringList);
		adapterPromosi
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerPromosi.setAdapter(adapterPromosi);
		spinnerPromosi
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						if (idPromosi == -1) {
							tvTotalDiskonValue.setText("0%");
						}
						updateTotalBayar();
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

		if (idPromosi == -1)
			tvTotalDiskonValue.setText("0%");

		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_nomer_order = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_SALES_ORDER_NOMER_ORDER, null);
		if (main_app_nomer_order.length() > 0) {
			showDetailOrder(main_app_nomer_order);
		} else {
			gotoSalesOrder();
		}

		mButtonSave.setOnClickListener(maddPhotoPurchaseButtonOnClickListener);
		mButtonAddProduct
				.setOnClickListener(maddPhotoPurchaseButtonOnClickListener);

	}

	private void showDetailOrder(String main_app_nomer_order) {
		salesOrderList.clear();
		salesOrderList = databaseHandler
				.getAllSalesOrderWhereNomerOrder(main_app_nomer_order);
		etKodeCustomer.setEnabled(false);
		etNamaCustomer.setEnabled(false);
		etAlamatCustomer.setEnabled(false);
		if (salesOrderList.size() > 0) {
			etKodeCustomer.setText(salesOrderList.get(0).getKode_customer());
			etNamaCustomer.setText(salesOrderList.get(0).getNama_lengkap());
			etAlamatCustomer.setText(salesOrderList.get(0).getAlamat());
			etDeskripsiPhotoPurchase.setText(salesOrderList.get(0)
					.getDeskripsi());
			nomerOrder = salesOrderList.get(0).getNomer_order();
			int tempIndex = 0;
			spinnerPromosi.setSelection(tempIndex);
			if (idPromosi == -1)
				tvTotalDiskonValue.setText("0%");

			int count = detailSalesOrderList.size() + 1;
			for (SalesOrder salesOrder : salesOrderList) {
				updateListViewDetailOrder(new DetailSalesOrder(count,
						salesOrder.getNama_product(),
						salesOrder.getKode_product(),
						salesOrder.getHarga_jual(),
						salesOrder.getJumlah_order(),
						salesOrder.getJumlah_order1(),
						salesOrder.getJumlah_order2(),
						salesOrder.getJumlah_order3()));
				count += 1;
			}
			updateTotalBayar();
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
								gotoSalesOrder();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	/*
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
		final EditText jumlahProduct1 = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_renceng);
		final EditText jumlahProduct2 = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pack);
		final EditText jumlahProduct3 = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_dus);

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
						DetailSalesOrderActivity.this,
						R.layout.list_item_product_sales_order, jumlahProduct,
						jumlahProduct1, jumlahProduct2,jumlahProduct3,
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
									DetailSalesOrderActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct, jumlahProduct1, jumlahProduct2,jumlahProduct3,  product_list,
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
									DetailSalesOrderActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct,jumlahProduct1, jumlahProduct2,jumlahProduct3, product_list,
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
	*/
	private void ChooseProductDialog() {
		final Dialog chooseProductDialog = new Dialog(act);
		chooseProductDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseProductDialog
				.setContentView(R.layout.activity_main_product_choose_dialog);
		chooseProductDialog.setCanceledOnTouchOutside(false);
		chooseProductDialog.setCancelable(true);


		chooseProductDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				chooseProductDialog.dismiss();
			}
		});
		TextView tvHeaderKodeProduct = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_kode_product);
		TextView tvHeaderNamaProduct = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_nama_product);
		//TextView tvHeaderHargaProduct = (TextView) chooseProductDialog
		//		.findViewById(R.id.activity_sales_order_title_harga_product);
		TextView tvHeaderAction = (TextView) chooseProductDialog
				.findViewById(R.id.activity_sales_order_title_action);
		tvHeaderKodeProduct.setTypeface(typefaceSmall);
		tvHeaderNamaProduct.setTypeface(typefaceSmall);
		//tvHeaderHargaProduct.setTypeface(typefaceSmall);
		tvHeaderAction.setTypeface(typefaceSmall);
		EditText searchProduct = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_search);
		final ArrayList<Product> product_list = new ArrayList<Product>();
		final ListView listview = (ListView) chooseProductDialog
				.findViewById(R.id.list);
		final EditText jumlahProduct = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pieces);
		final EditText jumlahProduct1 = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_renceng);
		final EditText jumlahProduct2 = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pack);
		final EditText jumlahProduct3 = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_dus);

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
						DetailSalesOrderActivity.this,
						R.layout.list_item_product_sales_order, jumlahProduct,
						jumlahProduct1, jumlahProduct2,jumlahProduct3,
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
									DetailSalesOrderActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct,jumlahProduct1, jumlahProduct2, jumlahProduct3, product_list,
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
									DetailSalesOrderActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct, jumlahProduct1, jumlahProduct2,jumlahProduct3, product_list,
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


	private final OnClickListener maddPhotoPurchaseButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_sales_order_btn_add_product:
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
			case R.id.activity_sales_order_btn_save:
				if (etDeskripsiPhotoPurchase.getText().toString().length() > 0) {
					databaseHandler.deleteTableSalesOrderWhereNomerOrder(nomerOrder);

					int countData = databaseHandler.getCountSalesOrder();
					countData = countData + 1;

					final String date2 = "yyyy-MM-dd";
					Calendar calendar2 = Calendar.getInstance();
					SimpleDateFormat dateFormat2 = new SimpleDateFormat(date2);
					final String checkDate = dateFormat2.format(calendar2
							.getTime());
					Calendar now = Calendar.getInstance();
					int hrs = now.get(Calendar.HOUR_OF_DAY);
					int min = now.get(Calendar.MINUTE);
					int sec = now.get(Calendar.SECOND);
					final String time = zero(hrs) + zero(min) + zero(sec);

					SharedPreferences spPreferences = getSharedPrefereces();
					String username = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);

					ArrayList<SalesOrder> tempSales_order_list = databaseHandler
							.getAllSalesOrder();
					int tempIndex = 0;
					for (SalesOrder salesOrder : tempSales_order_list) {
						tempIndex = salesOrder.getId_sales_order();
					}
					int index = 1;
					for (DetailSalesOrder detailSalesOrder : detailSalesOrderList) {
						SalesOrder salesOrder = new SalesOrder();
						salesOrder.setId_sales_order(tempIndex + index);
						salesOrder.setAlamat(etAlamatCustomer.getText()
								.toString());
						salesOrder.setDate_order(checkDate);
						salesOrder.setDeskripsi(etDeskripsiPhotoPurchase
								.getText().toString());
						salesOrder.setHarga_jual(detailSalesOrder
								.getHarga_jual());
						salesOrder.setId_promosi(idPromosi);
						salesOrder.setJumlah_order(detailSalesOrder.getJumlah_order());
						salesOrder.setJumlah_order1(detailSalesOrder.getJumlah_order1());
						salesOrder.setJumlah_order2(detailSalesOrder.getJumlah_order2());
						salesOrder.setJumlah_order3(detailSalesOrder.getJumlah_order3());
						salesOrder.setKode_customer(etKodeCustomer.getText()
								.toString());
						salesOrder.setNama_lengkap(etNamaCustomer.getText()
								.toString());
						salesOrder.setNama_product(detailSalesOrder
								.getNama_product());
						salesOrder.setKode_product(detailSalesOrder
								.getKode_product());
						salesOrder.setNomer_order(nomerOrder);
						salesOrder.setNomer_order_detail(nomerOrder
								+ String.valueOf(countData));
						salesOrder.setTime_order(time);
						salesOrder.setUsername(username);
						databaseHandler.add_SalesOrder(salesOrder);
						index += 1;
					}
					String msg = getApplicationContext().getResources()
							.getString(R.string.app_sales_order_update_success);
					showCustomDialogSaveSuccess(msg);

				} else {

					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_photo_purchase_save_failed_no_data);
					showCustomDialog(msg);
				}

				break;
			default:
				break;
			}
		}

	};




	private void updateListViewDetailOrder(DetailSalesOrder detailSalesOrder) {
		detailSalesOrderList.add(detailSalesOrder);
		cAdapter = new ListViewAdapter(DetailSalesOrderActivity.this,
				R.layout.list_item_detail_sales_order, detailSalesOrderList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
		updateTotalBayar();
	}

	private void updateTotalBayar() {
		int totalBayar = 0;
		for (DetailSalesOrder tempDetailSalesOrder : detailSalesOrderList) {
			totalBayar += Integer
					.parseInt(tempDetailSalesOrder.getHarga_jual())
					* Integer.parseInt(tempDetailSalesOrder.getJumlah_order());
		}
		int totalDiskon = 0;
		if (idPromosi == -1)
			totalDiskon = 0;

		if (totalBayar > 0) {
			totalBayar = totalBayar - ((totalBayar * totalDiskon) / 100);
		}
		String totalBayarTemp = String.valueOf(totalBayar);
		Float priceIDR = Float.valueOf(totalBayarTemp);
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
		tvTotalbayarValue.setText("Rp. " + df.format(priceIDR));
	}

	public class ListViewAdapter extends ArrayAdapter<DetailSalesOrder> {
		Activity activity;
		int layoutResourceId;
		DetailSalesOrder productData;
		ArrayList<DetailSalesOrder> data = new ArrayList<DetailSalesOrder>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<DetailSalesOrder> data) {
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
                holder.list_harga_jual = (TextView) row
                        .findViewById(R.id.sales_order_title_harga);
				holder.list_jumlah_order = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order);
                holder.list_jumlah_order1 = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order1);
                holder.list_jumlah_order2 = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order2);
				holder.list_jumlah_order3 = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order3);


				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_kode_product.setText(productData.getKode_product());

                holder.list_jumlah_order.setText(productData.getJumlah_order());

                holder.list_jumlah_order1.setText(productData.getJumlah_order1());

                holder.list_jumlah_order2.setText(productData.getJumlah_order2());

				holder.list_jumlah_order3.setText(productData.getJumlah_order3());

            //holder.list_jumlah_order.setText(productData.getJumlah_order());
			//holder.list_jumlah_order1.setText(productData.getJumlah_order1());
			//holder.list_jumlah_order2.setText(productData.getJumlah_order2());
			Float priceIDR = Float.valueOf(productData.getHarga_jual());
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
			holder.list_harga_jual.setText("Rp. " + df.format(priceIDR));
			holder.list_kode_product.setTypeface(typefaceSmall);
			holder.list_jumlah_order.setTypeface(typefaceSmall);
			holder.list_jumlah_order1.setTypeface(typefaceSmall);
			holder.list_jumlah_order2.setTypeface(typefaceSmall);
			holder.list_jumlah_order3.setTypeface(typefaceSmall);
			holder.list_harga_jual.setTypeface(typefaceSmall);
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showDeleteDialog(position);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kode_product;
			TextView list_jumlah_order;
			TextView list_jumlah_order1;
			TextView list_jumlah_order2;
			TextView list_jumlah_order3;
			TextView list_harga_jual;
		}

	}

	// show edit delete dialog
	public void showDeleteDialog(final int position) {
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
								detailSalesOrderList.remove(position);
								cAdapter = new ListViewAdapter(
										DetailSalesOrderActivity.this,
										R.layout.list_item_detail_sales_order,
										detailSalesOrderList);
								listview.setAdapter(cAdapter);
								cAdapter.notifyDataSetChanged();
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

	class ListViewChooseAdapter extends ArrayAdapter<Product> {
		int layoutResourceId;
		Product productData;
		ArrayList<Product> data = new ArrayList<Product>();
		Activity mainActivity;
		EditText jumlahProduct;
		EditText jumlahProduct1;
		EditText jumlahProduct2;
		EditText jumlahProduct3;
		Dialog chooseProductDialog;

		public ListViewChooseAdapter(Activity mainActivity,
				int layoutResourceId, EditText jumlahProduct,
                EditText jumlahProduct1,EditText jumlahProduct2,EditText jumlahProduct3,
				ArrayList<Product> data, Dialog chooseProductDialog) {
			super(mainActivity, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.data = data;
			this.chooseProductDialog = chooseProductDialog;
			this.mainActivity = mainActivity;
			this.jumlahProduct = jumlahProduct;
			this.jumlahProduct1 = jumlahProduct1;
			this.jumlahProduct2 = jumlahProduct2;
			this.jumlahProduct3 = jumlahProduct3;
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
                    if (jumlahProduct.getText().length()==0){
                        String msg = getApplicationContext()
                                .getResources()
                                .getString(
                                        R.string.app_sales_order_failed_please_add_pcs);
                        showCustomDialog(msg);
                    }else if(jumlahProduct1.getText().length()==0){
                        String msg = getApplicationContext()
                                .getResources()
                                .getString(
                                        R.string.app_sales_order_failed_please_add_renceng);
                        showCustomDialog(msg);
                    } else if(jumlahProduct2.getText().length()==0){
                        String msg = getApplicationContext()
                                .getResources()
                                .getString(
                                        R.string.app_sales_order_failed_please_add_pck);
                        showCustomDialog(msg);
                    }else if(jumlahProduct3.getText().length()==0){
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_order_failed_please_add_dus);
						showCustomDialog(msg);
					}else {
                        if (jumlahProduct.getText().toString().length() > 0 || jumlahProduct1.getText().toString().length() > 0 ||
                                jumlahProduct2.getText().toString().length() > 0||jumlahProduct3.getText().toString().length() > 0) {
                            boolean containSameProduct = false;
                            for (DetailSalesOrder detailSalesOrder : detailSalesOrderList) {
                                if (detailSalesOrder.getKode_product()
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
                                int count = detailSalesOrderList.size() + 1;
                                updateListViewDetailOrder(new DetailSalesOrder(
                                        count,
                                        data.get(position).getNama_product(),
                                        data.get(position).getKode_product(),
                                        data.get(position).getHarga_jual(),
                                        jumlahProduct.getText().toString(),
                                        jumlahProduct1.getText().toString(),
                                        jumlahProduct2.getText().toString(),
                                        jumlahProduct3.getText().toString()));
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

	public void gotoSalesOrder() {
		Intent i = new Intent(this, SalesOrderActivity.class);
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
