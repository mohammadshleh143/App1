package com.mahkota_company.android.prospect;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.mahkota_company.android.NavigationDrawerCallbacks;
import com.mahkota_company.android.NavigationDrawerFragment;
import com.mahkota_company.android.check_customer.CheckCustomer;
import com.mahkota_company.android.check_new_prospect.CheckCustomerProspectActivity;
import com.mahkota_company.android.contact.ContactActivty;
import com.mahkota_company.android.contact.SuperVisor;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Request_load;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.inventory.InventoryActivity;
import com.mahkota_company.android.inventory.RequestActivity;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.locator.LocatorActivity;
import com.mahkota_company.android.merchandise.CustomerMerchandiseActivity;
import com.mahkota_company.android.merchandise.LocatorStaffActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.R;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class CustomerProspectActivity extends ActionBarActivity implements
		NavigationDrawerCallbacks {
	private Context act;
	private Toolbar mToolbar;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<Customer> customer_list = new ArrayList<Customer>();
	private ListViewAdapter cAdapter;
	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String response_data;
	private static final String LOG_TAG = CustomerProspectActivity.class
			.getSimpleName();
	private EditText searchCustomer;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvNamaAlamat;
	private Button addCustomerProspectProduct;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_customer_prospect);
		mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeCustomer = (TextView) findViewById(R.id.activity_customer_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_customer_title_nama_customer);
		tvNamaAlamat = (TextView) findViewById(R.id.activity_customer_title_alamat_customer);
		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);
		tvNamaAlamat.setTypeface(typefaceSmall);

		act = this;
		setSupportActionBar(mToolbar);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_customer_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
				.findFragmentById(R.id.fragment_drawer);
		mNavigationDrawerFragment.setup(R.id.fragment_drawer,
				(DrawerLayout) findViewById(R.id.drawer), mToolbar);
		searchCustomer = (EditText) findViewById(R.id.activity_customer_edittext_search);
		databaseHandler = new DatabaseHandler(this);
		mNavigationDrawerFragment.selectItem(3);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		addCustomerProspectProduct = (Button) findViewById(R.id.activity_customer_btn_add);
		addCustomerProspectProduct
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						SharedPreferences spPreferences = getSharedPrefereces();
						String main_app_staff_id_wilayah = spPreferences
								.getString(
										CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH,
										null);
						if (main_app_staff_id_wilayah.length() > 0) {
							gotoTambahCustomerProspect();
						}
					}
				});
		showListCustomerProspect();

		searchCustomer.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.toString().length() > 0) {
					customer_list.clear();
					ArrayList<Customer> customer_from_db = databaseHandler
							.getAllCustomerProspectBaseOnSearch(cs.toString());
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
							String no_ktp = customer_from_db.get(i)
									.getNo_ktp();
							String tanggal_lahir = customer_from_db.get(i)
									.getTanggal_lahir();
							String nama_bank = customer_from_db.get(i)
									.getNama_bank();
							String no_rekening = customer_from_db.get(i)
									.getNo_rekening();
							String atas_nama = customer_from_db.get(i)
									.getAtas_nama();
							String npwp = customer_from_db.get(i)
									.getNpwp();

                            String nama_pasar = customer_from_db.get(i)
                                    .getNama_pasar();
                            int id_cluster = customer_from_db.get(i)
                                    .getId_cluster();
                            String telp = customer_from_db.get(i)
                                    .getTelp();
                            String fax = customer_from_db.get(i)
                                    .getFax();
                            String omset = customer_from_db.get(i)
                                    .getOmset();
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
							String kode_pos = customer_from_db.get(i)
									.getKode_pos();
							int id_depo = customer_from_db.get(i)
									.getId_depo();
							String isactive = customer_from_db.get(i)
									.getIsactive();
							String description = customer_from_db.get(i)
									.getDescription();
							String nama_toko = customer_from_db.get(i)
									.getNama_toko();
							String ttd1 = customer_from_db.get(i)
									.getTtd1();
							String ttd2 = customer_from_db.get(i)
									.getTtd2();


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
							customer.setKode_pos(kode_pos);
							customer.setId_depo(id_depo);
							customer.setIsactive(isactive);
							customer.setDescription(description);
							customer.setNama_toko(nama_toko);
							customer.setTtd1(ttd1);
							customer.setTtd2(ttd2);

							customer_list.add(customer);
						}

						cAdapter = new ListViewAdapter(
								CustomerProspectActivity.this,
								R.layout.list_item_customer_prospect,
								customer_list);
						listview.setAdapter(cAdapter);
						cAdapter.notifyDataSetChanged();
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					customer_list.clear();
					ArrayList<Customer> customer_from_db = databaseHandler
							.getAllCustomerProspect();
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
							int id_staff = customer_from_db.get(i)
									.getId_staff();
							String no_ktp = customer_from_db.get(i)
									.getNo_ktp();
							String tanggal_lahir = customer_from_db.get(i)
									.getTanggal_lahir();
							String nama_bank = customer_from_db.get(i)
									.getNama_bank();
							String no_rekening = customer_from_db.get(i)
									.getNo_rekening();
							String atas_nama = customer_from_db.get(i)
									.getAtas_nama();
							String npwp = customer_from_db.get(i)
									.getNpwp();
                            String nama_pasar = customer_from_db.get(i)
                                    .getNama_pasar();
                            int id_cluster = customer_from_db.get(i)
                                    .getId_cluster();
                            String telp = customer_from_db.get(i)
                                    .getTelp();
                            String fax = customer_from_db.get(i)
                                    .getFax();
                            String omset = customer_from_db.get(i)
                                    .getOmset();
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
							String kode_pos = customer_from_db.get(i)
									.getKode_pos();
							int id_depo = customer_from_db.get(i)
									.getId_depo();
							String isactive = customer_from_db.get(i)
									.getIsactive();
							String description = customer_from_db.get(i)
									.getDescription();
							String nama_toko = customer_from_db.get(i)
									.getNama_toko();
							String ttd1 = customer_from_db.get(i)
									.getTtd1();
							String ttd2 = customer_from_db.get(i)
									.getTtd2();

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
							customer.setKode_pos(kode_pos);
							customer.setId_depo(id_depo);
							customer.setIsactive(isactive);
							customer.setDescription(description);
							customer.setNama_toko(nama_toko);
							customer.setTtd1(ttd1);
							customer.setTtd2(ttd2);

							customer_list.add(customer);
						}

						cAdapter = new ListViewAdapter(
								CustomerProspectActivity.this,
								R.layout.list_item_customer, customer_list);
						listview.setAdapter(cAdapter);
						cAdapter.notifyDataSetChanged();
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

	public void gotoTambahCustomerProspect() {
		Intent i = new Intent(this, AddCustomerProspectActivity.class);
		startActivity(i);
		finish();
	}

	public void saveAppDataCustomerIdCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER,
				responsedata);
		editor.commit();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showListCustomerProspect() {
		customer_list.clear();
		ArrayList<Customer> customer_from_db = databaseHandler
				.getAllCustomerProspect();
		if (customer_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < customer_from_db.size(); i++) {
				int id_customer = customer_from_db.get(i).getId_customer();
				String kode_customer = customer_from_db.get(i)
						.getKode_customer();
				String email = customer_from_db.get(i).getEmail();
				String alamat = customer_from_db.get(i).getAlamat();
				String lats = customer_from_db.get(i).getLats();
				String longs = customer_from_db.get(i).getLongs();
				String nama_lengkap = customer_from_db.get(i).getNama_lengkap();
				String no_telp = customer_from_db.get(i).getNo_telp();
				int id_wilayah = customer_from_db.get(i).getId_wilayah();
				String foto1 = customer_from_db.get(i).getFoto_1();
				String foto2 = customer_from_db.get(i).getFoto_2();
				String foto3 = customer_from_db.get(i).getFoto_3();
				int id_type = customer_from_db.get(i).getId_type_customer();
				String blockir = customer_from_db.get(i).getBlokir();
				String date = customer_from_db.get(i).getDate();
				String status_update = customer_from_db.get(i)
						.getStatus_update();
				int id_staff = customer_from_db.get(i).getId_staff();
				String no_ktp= customer_from_db.get(i).getNo_ktp();
				String tanggal_lahir= customer_from_db.get(i).getTanggal_lahir();
				String nama_bank= customer_from_db.get(i).getNama_bank();
				String no_rekening= customer_from_db.get(i).getNo_rekening();
				String atas_nama= customer_from_db.get(i).getAtas_nama();
				String npwp= customer_from_db.get(i).getNpwp();
                String nama_pasar = customer_from_db.get(i)
                        .getNama_pasar();
                int id_cluster = customer_from_db.get(i)
                        .getId_cluster();
                String telp = customer_from_db.get(i)
                        .getTelp();
                String fax = customer_from_db.get(i)
                        .getFax();
                String omset = customer_from_db.get(i)
                        .getOmset();
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
				String kode_pos = customer_from_db.get(i)
						.getKode_pos();
				int id_depo = customer_from_db.get(i)
						.getId_depo();
				String isactive = customer_from_db.get(i)
						.getIsactive();
				String description = customer_from_db.get(i)
						.getDescription();
				String nama_toko = customer_from_db.get(i)
						.getNama_toko();
				String ttd1 = customer_from_db.get(i)
						.getTtd1();
				String ttd2 = customer_from_db.get(i)
						.getTtd2();


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
				customer.setKode_pos(kode_pos);
				customer.setId_depo(id_depo);
				customer.setIsactive(isactive);
				customer.setDescription(description);
				customer.setNama_toko(nama_toko);
				customer.setTtd1(ttd1);
				customer.setTtd2(ttd2);


				customer_list.add(customer);
			}

			cAdapter = new ListViewAdapter(this, R.layout.list_item_customer,
					customer_list);
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
				int countUpload = databaseHandler.getCountCustomerProspect();
				if (countUpload == 0) {
					String message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_customer_prospect_processing_upload_no_data);
					showCustomDialog(message);
				} else {
					new UploadData().execute();
				}
			} else {
				String message = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_customer_prospect_processing_upload_empty);
				showCustomDialog(message);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private String uploadCustomerProspect(final String url,
					final String kode_customer, final String email,
					final String alamat, final String lats, final String longs,
					final String nama_lengkap, final String no_telp,
					final String id_wilayah, final String foto_1, final String foto_2,
					final String foto_3, final String id_type_customer,
					final String date, final String id_staff, final String no_ktp,
					final String tanggal_lahir, final String nama_bank, final String no_rekening,
					final String atas_nama, final String npwp, final String nama_pasar, final String id_cluster,
                    final String telp, final String fax, final String omset, final String cara_pembayaran,
                    final String plafon_kredit, final String term_kredit, final String nama_istri, final String nama_anak1,
					final String nama_anak2, final String nama_anak3, final String kode_pos, final String id_depo,
					final String isactive, final String description, final String nama_toko, final String ttd1, final String ttd2) {

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

			File sourceFoto1 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/" + foto_1);
			if (sourceFoto1.exists() && foto_1 != null)
				entity.addPart("image", new FileBody(sourceFoto1));


			File sourceFoto2 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/" + foto_2);
			if (sourceFoto2.exists() && foto_2 != null)
				entity.addPart("image2", new FileBody(sourceFoto2));


			File sourceFoto3 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/" + foto_3);
			if (sourceFoto3.exists() && foto_3 != null)
				entity.addPart("image3", new FileBody(sourceFoto3));


			File sourceTtd1 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/" + ttd1);
			if (sourceTtd1.exists() && ttd1 != null)
				entity.addPart("ttd_1", new FileBody(sourceTtd1));


			File sourceTtd2 = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT + "/" + ttd2);
			if (sourceTtd2.exists() && ttd2 != null)
				entity.addPart("ttd_2", new FileBody(sourceTtd2));


			entity.addPart("kode_customer", new StringBody(kode_customer));
			entity.addPart("email", new StringBody(email));
			entity.addPart("alamat", new StringBody(alamat));
			entity.addPart("lats", new StringBody(lats));
			entity.addPart("longs", new StringBody(longs));
			entity.addPart("nama_lengkap", new StringBody(nama_lengkap));
			entity.addPart("no_telp", new StringBody(no_telp));
			entity.addPart("id_wilayah", new StringBody(id_wilayah));
			entity.addPart("foto_1", new StringBody(foto_1 != null ? foto_1
					: ""));
			entity.addPart("foto_2", new StringBody(foto_2 != null ? foto_2
					: ""));
			entity.addPart("foto_3", new StringBody(foto_3 != null ? foto_3
					: ""));
			entity.addPart("id_type_customer", new StringBody(id_type_customer));
			entity.addPart("date", new StringBody(date));
			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("no_ktp", new StringBody(no_ktp));
			entity.addPart("tanggal_lahir", new StringBody(tanggal_lahir));
			entity.addPart("nama_bank", new StringBody(nama_bank));
			entity.addPart("no_rekening", new StringBody(no_rekening));
			entity.addPart("atas_nama", new StringBody(atas_nama));
			entity.addPart("npwp", new StringBody(npwp));
            entity.addPart("nama_pasar", new StringBody(nama_pasar));
            entity.addPart("id_cluster", new StringBody(id_cluster));
            entity.addPart("telp", new StringBody(telp));
            entity.addPart("fax", new StringBody(fax));
            entity.addPart("omset", new StringBody(omset));
            entity.addPart("cara_pembayaran", new StringBody(cara_pembayaran));
            entity.addPart("plafon_kredit", new StringBody(plafon_kredit));
            entity.addPart("term_kredit", new StringBody(term_kredit));
			entity.addPart("nama_istri", new StringBody(nama_istri != null ? nama_istri
					: ""));
			entity.addPart("nama_anak1", new StringBody(nama_anak1 != null ? nama_anak1
					: ""));
			entity.addPart("nama_anak2", new StringBody(nama_anak2 != null ? nama_anak2
					: ""));
			entity.addPart("nama_anak3", new StringBody(nama_anak3 != null ? nama_anak3
					: ""));
			entity.addPart("kode_pos", new StringBody(kode_pos != null ? kode_pos
					: ""));

			entity.addPart("id_depo", new StringBody(id_depo != null ? id_depo
					: ""));
			entity.addPart("isactive", new StringBody(isactive != null ? isactive
					: ""));
			entity.addPart("description", new StringBody(description != null ? description
					: ""));
			entity.addPart("nama_toko", new StringBody(nama_toko != null ? nama_toko
					: ""));
			entity.addPart("ttd1", new StringBody(ttd1 != null ? ttd1
					: ""));
			entity.addPart("ttd2", new StringBody(ttd2 != null ? ttd2
					: ""));

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

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_customer_processing_upload));
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
					+ CONFIG.CONFIG_APP_URL_UPLOAD_CUSTOMER_PROSPECT;

			List<Customer> dataUpload = databaseHandler
					.getAllCustomerProspect();
			for (Customer customer : dataUpload) {
				response_data = uploadCustomerProspect(upload_url,
						customer.getKode_customer(),
						customer.getEmail(),
						customer.getAlamat(),
                        customer.getLats(),
						customer.getLongs(),
                        customer.getNama_lengkap(),
						customer.getNo_telp(),
						String.valueOf(customer.getId_wilayah()),
						customer.getFoto_1(),
						customer.getFoto_2(),
						customer.getFoto_3(),
						String.valueOf(customer.getId_type_customer()),
						customer.getDate(),
						String.valueOf(customer.getId_staff()),
						customer.getNo_ktp(),
						customer.getTanggal_lahir(),
						customer.getNama_bank(),
						customer.getNo_rekening(),
						customer.getAtas_nama(),
						customer.getNpwp(),
                        customer.getNama_pasar(),
						String.valueOf(customer.getId_cluster()),
                        customer.getTelp(),
                        customer.getFax(),
                        customer.getOmset(),
                        customer.getCara_pembayaran(),
                        customer.getPlafon_kredit(),
                        customer.getTerm_kredit(),
						customer.getNama_istri(),
						customer.getNama_anak1(),
						customer.getNama_anak2(),
						customer.getNama_anak3(),
						customer.getKode_pos(),
						String.valueOf(customer.getId_depo()),
						customer.getIsactive(),
						customer.getDescription(),
						customer.getNama_toko(),
						customer.getTtd1(),
						customer.getTtd2());
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			List<Customer> dataAddCustomerProspectUpload = databaseHandler
					.getAllCustomerProspect();
			int countData = dataAddCustomerProspectUpload.size();
			if (countData > 0) {
				try {
					Thread.sleep(dataAddCustomerProspectUpload.size() * 2048 * 10);//1024
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
										R.string.app_customer_prospect_processing_upload_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						handler.post(new Runnable() {
							public void run() {
								initUploadCustomerProspect();
							}
						});
					}
				}
			} else {
				final String msg = act
						.getApplicationContext()
						.getResources()
						.getString(
								R.string.app_customer_prospect_processing_upload_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}

	}

	public void initUploadCustomerProspect() {
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
								R.string.app_customer_prospect_processing_upload_failed);
				showCustomDialog(msg);
			} else {
				Log.d(LOG_TAG, "status=" + status);
				if (status.equalsIgnoreCase("True")) {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_customer_prospect_processing_upload_failed);
					showCustomDialog(msg);
				} else {
					final String msg = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_customer_prospect_processing_upload_success);
					CustomDialogUploadSuccess(msg);
				}

			}

		} catch (JSONException e) {
			final String message = e.toString();
			showCustomDialog(message);

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
								databaseHandler.updateStatusProspect();
								//databaseHandler.deleteTableCustomerProspect();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public class ListViewAdapter extends ArrayAdapter<Customer> {
		Activity activity;
		int layoutResourceId;
		Customer customerData;
		ArrayList<Customer> data = new ArrayList<Customer>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<Customer> data) {
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
				holder.list_kodeCustomer = (TextView) row
						.findViewById(R.id.customer_title_kode_customer);
				holder.list_namaCustomer = (TextView) row
						.findViewById(R.id.customer_title_nama_customer);
				holder.list_alamat = (TextView) row
						.findViewById(R.id.customer_title_alamat_customer);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			customerData = data.get(position);
			holder.list_kodeCustomer.setText(customerData.getKode_customer());
			holder.list_namaCustomer.setText(customerData.getNama_lengkap());
			// Wilayah wilayah = databaseHandler.getWilayah(customerData
			// .getId_wilayah());
			holder.list_alamat.setText(customerData.getAlamat());
			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_alamat.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					String id_customer = String.valueOf(data.get(position)
							.getId_customer());
					showEditDeleteDialog(id_customer);
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_alamat;
		}

	}

	public void gotoDetailCustomerProspect() {
		Intent i = new Intent(this, DetailCustomerProspectActivity.class);
		startActivity(i);
		finish();
	}

	// show edit delete dialog
	public void showEditDeleteDialog(final String id_customer) {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_DATA_EDIT_DELETE_DIALOG);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(true)
				.setNegativeButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_EDIT),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								saveAppDataCustomerIdCustomer(id_customer);
								gotoDetailCustomerProspect();
							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_DELETE),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								databaseHandler.deleteTableCustomer(Integer
										.parseInt(id_customer));
								showSucceedDeleteDialog();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	// Show Succeed Delete Item dialog
	public void showSucceedDeleteDialog() {
		String msg = getApplicationContext().getResources().getString(
				R.string.MSG_DLG_LABEL_DELETE_ITEM_SUCCESS);
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				this);
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {
		if (mNavigationDrawerFragment != null) {
			if (mNavigationDrawerFragment.getCurrentSelectedPosition() != 3) {
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
				} else if (position == 2) {
					Intent intentActivity = new Intent(this,
							ProductActivity.class);
					startActivity(intentActivity);
					finish();
				} else if (position == 4) {
					Intent intentActivity = new Intent(this,
							LocatorActivity.class);
					startActivity(intentActivity);
					finish();
				}else if (position == 5) {
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
				}
			}
		}

		// Toast.makeText(this, "Menu item selected -> " + position,
		// Toast.LENGTH_SHORT).show();
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
		final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				act);
		alertDialogBuilder
				.setMessage(msg)
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
