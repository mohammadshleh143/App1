package com.mahkota_company.android.stock_on_hand;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DetailSalesOrder;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.StockOnHand;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.R;

public class DetailStockOnHandActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private EditText etNamaCustomer;
	private EditText etAlamatCustomer;
	private EditText etDeskripsiStockOnHand;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderAlamatCustomer;
	private TextView tvHeaderDeskripsiPhotoPurchase;
	private Button mButtonAddProduct;
	private Button mButtonSave;
	private Button mButtonCancel;
	private EditText etKodeCustomer;
	private ListViewChooseAdapter cAdapterChooseAdapter;
	public ArrayList<DetailSalesOrder> detailSalesOrderList = new ArrayList<DetailSalesOrder>();
	public ArrayList<StockOnHand> stockOnHandList = new ArrayList<StockOnHand>();
	private ListView listview;
	private ListViewAdapter cAdapter;
	private String nomerOrder;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_stock_on_hand);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoStockOnHand();
			}
		});
		mButtonCancel = (Button) findViewById(R.id.activity_sales_order_btn_cancel);
		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoStockOnHand();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		etKodeCustomer = (EditText) findViewById(R.id.activity_sales_order_kode_customer_value);
		etNamaCustomer = (EditText) findViewById(R.id.activity_sales_order_nama_customer_value);
		etAlamatCustomer = (EditText) findViewById(R.id.activity_sales_order_alamat_value);
		etDeskripsiStockOnHand = (EditText) findViewById(R.id.activity_sales_order_deskripsi_value);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_app_sales_order_kode_customer_title);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_app_sales_order_nama_customer_title);
		tvHeaderAlamatCustomer = (TextView) findViewById(R.id.activity_app_sales_order_alamat_title);
		tvHeaderDeskripsiPhotoPurchase = (TextView) findViewById(R.id.activity_app_sales_order_deskripsi_title);
		mButtonAddProduct = (Button) findViewById(R.id.activity_sales_order_btn_add_product);
		mButtonSave = (Button) findViewById(R.id.activity_sales_order_btn_save);

		etNamaCustomer.setTypeface(typefaceSmall);
		etAlamatCustomer.setTypeface(typefaceSmall);
		etDeskripsiStockOnHand.setTypeface(typefaceSmall);
		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderAlamatCustomer.setTypeface(typefaceSmall);
		tvHeaderDeskripsiPhotoPurchase.setTypeface(typefaceSmall);

		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_nomer_order = spPreferences
				.getString(
						CONFIG.SHARED_PREFERENCES_TABLE_STOCK_ON_HAND_NOMER_ORDER_STOCK_ON_HAND,
						null);
		if (main_app_nomer_order.length() > 0) {
			showDetailOrder(main_app_nomer_order);
		} else {
			gotoStockOnHand();
		}

		mButtonSave.setOnClickListener(maddPhotoPurchaseButtonOnClickListener);
		mButtonAddProduct
				.setOnClickListener(maddPhotoPurchaseButtonOnClickListener);

	}

	private void showDetailOrder(String main_app_nomer_order) {
		stockOnHandList.clear();
		stockOnHandList = databaseHandler
				.getAllStockOnHandWhereStockOnHand(main_app_nomer_order);
		etKodeCustomer.setEnabled(false);
		etNamaCustomer.setEnabled(false);
		etAlamatCustomer.setEnabled(false);
		if (stockOnHandList.size() > 0) {
			etKodeCustomer.setText(stockOnHandList.get(0).getKode_customer());
			etNamaCustomer.setText(stockOnHandList.get(0).getNama_lengkap());
			etAlamatCustomer.setText(stockOnHandList.get(0).getAlamat());
			etDeskripsiStockOnHand.setText(stockOnHandList.get(0)
					.getDeskripsi());
			nomerOrder = stockOnHandList.get(0).getNomer_stock_on_hand();

			int count = detailSalesOrderList.size() + 1;
			for (StockOnHand stockOnHand : stockOnHandList) {
				updateListViewDetailOrder(new DetailSalesOrder(count,
						stockOnHand.getNama_product(),
						stockOnHand.getKode_product(),
						stockOnHand.getHarga_jual(),
						stockOnHand.getStockpcs(),
						stockOnHand.getStockrcg(),
						stockOnHand.getStockpck(),
						stockOnHand.getStockdus()));
				count += 1;
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
								gotoStockOnHand();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
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
				if (etDeskripsiStockOnHand.getText().toString().length() > 0) {

					databaseHandler
							.deleteTableStockOnHandWhereNomerOrder(nomerOrder);

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

					ArrayList<StockOnHand> tempStockOnHand_list = databaseHandler
							.getAllStockOnHand();
					int tempIndex = 0;
					for (StockOnHand salesOrder : tempStockOnHand_list) {
						tempIndex = salesOrder.getId_stock_on_hand();
					}
					int index = 1;
					for (DetailSalesOrder detailSalesOrder : detailSalesOrderList) {
						StockOnHand stockOnHand = new StockOnHand();
						stockOnHand.setId_stock_on_hand(tempIndex + index);
						stockOnHand.setAlamat(etAlamatCustomer.getText()
								.toString());
						stockOnHand.setDate_stock_on_hand(checkDate);
						stockOnHand.setDeskripsi(etDeskripsiStockOnHand
								.getText().toString());
						stockOnHand.setHarga_jual(detailSalesOrder
								.getHarga_jual());
						stockOnHand.setStockpcs(detailSalesOrder.getJumlah_order());
						stockOnHand.setStockrcg(detailSalesOrder.getJumlah_order1());
						stockOnHand.setStockpck(detailSalesOrder.getJumlah_order2());
						stockOnHand.setStockdus(detailSalesOrder.getJumlah_order3());
						stockOnHand.setKode_customer(etKodeCustomer.getText()
								.toString());
						stockOnHand.setNama_lengkap(etNamaCustomer.getText()
								.toString());
						stockOnHand.setNama_product(detailSalesOrder
								.getNama_product());
						stockOnHand.setKode_product(detailSalesOrder
								.getKode_product());
						stockOnHand.setNomer_stock_on_hand(nomerOrder);
						stockOnHand.setNomer_stock_on_hand_detail(nomerOrder
								+ String.valueOf(countData));
						stockOnHand.setTime_stock_on_hand(time);
						stockOnHand.setUsername(username);
						databaseHandler.add_StockOnHand(stockOnHand);
						index += 1;
					}
					String msg = getApplicationContext().getResources()
							.getString(
									R.string.app_stock_on_hand_update_success);
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
						DetailStockOnHandActivity.this,
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
									DetailStockOnHandActivity.this,
									R.layout.list_item_product_stock_on_hand,
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
									DetailStockOnHandActivity.this,
									R.layout.list_item_product_stock_on_hand,
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

	private void updateListViewDetailOrder(DetailSalesOrder detailSalesOrder) {
		detailSalesOrderList.add(detailSalesOrder);
		cAdapter = new ListViewAdapter(DetailStockOnHandActivity.this,
				R.layout.list_item_detail_stock_on_hand, detailSalesOrderList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
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
				holder.list_jumlah_order = (TextView) row
						.findViewById(R.id.sales_order_title_jumlah_order);
				holder.list_harga_jual = (TextView) row
						.findViewById(R.id.sales_order_title_harga);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			productData = data.get(position);
			holder.list_kode_product.setText(productData.getKode_product());
			holder.list_jumlah_order.setText(productData.getJumlah_order());
			Float priceIDR = Float.valueOf(productData.getHarga_jual());
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
			holder.list_harga_jual.setText("Rp. " + df.format(priceIDR));
			holder.list_kode_product.setTypeface(typefaceSmall);
			holder.list_jumlah_order.setTypeface(typefaceSmall);
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
										DetailStockOnHandActivity.this,
										R.layout.list_item_detail_stock_on_hand,
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
									data.get(position).getNama_product(), data
											.get(position).getKode_product(),
									data.get(position).getHarga_jual(),
									jumlahProduct.getText().toString()));
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

	public void gotoStockOnHand() {
		Intent i = new Intent(this, StockOnHandActivity.class);
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
