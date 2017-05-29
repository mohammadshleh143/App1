package com.mahkota_company.android.jadwal;

import java.util.ArrayList;

import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.customer.DetailEditCustomer;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.jadwal.JadwalActivity;
import com.mahkota_company.android.product.ProductActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.R;
import com.mahkota_company.android.utils.GlobalApp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class JadwalCustomerActivity extends Activity {
	private Context act;
	private DatabaseHandler databaseHandler;
	private ListView listview;
	private ArrayList<Jadwal> jadwal_list = new ArrayList<Jadwal>();
	private ListViewAdapter cAdapter;
	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;
	private TextView tvNamaCustomer;
	private TextView tvAlamat;
	private TextView tvStatus;
	private ImageView menuBackButton;
	private EditText search;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_customer_jadwal);
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		tvKodeCustomer = (TextView) findViewById(R.id.activity_customer_title_kode_customer);
		tvNamaCustomer = (TextView) findViewById(R.id.activity_customer_title_nama_customer);
		tvAlamat = (TextView) findViewById(R.id.activity_customer_title_alamat_customer);
		tvStatus = (TextView) findViewById(R.id.activity_customer_title_status_customer);
		search = (EditText) findViewById(R.id.activity_customer_edittext_search);
		tvKodeCustomer.setTypeface(typefaceSmall);
		tvNamaCustomer.setTypeface(typefaceSmall);
		tvAlamat.setTypeface(typefaceSmall);
		tvStatus.setTypeface(typefaceSmall);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoJadwal();
			}
		});
		act = this;

		databaseHandler = new DatabaseHandler(this);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);

		SharedPreferences spPreferences = getSharedPrefereces();
		String kode_jadwal = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_KODE_JADWAL, null);
		if (kode_jadwal != null) {
			saveAppDataKodeJadwal(kode_jadwal);
			showCustomerFromKodeJadwal(kode_jadwal);
		} else {
			gotoJadwal();
		}

		search.addTextChangedListener(new TextWatcher() {


			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
									  int arg3) {
				if (cs.toString().length() > 0) {
					jadwal_list.clear();
					ArrayList<Jadwal> jadwal_from_db = databaseHandler.
							getAllJadwalBaseOnSearch(
							cs.toString());
					if (jadwal_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < jadwal_from_db.size(); i++) {
							int id_jadwal = jadwal_from_db.get(i)
									.getId_jadwal();
							String kode_jadwal = jadwal_from_db.get(i)
									.getKode_jadwal();
							String kode_customer = jadwal_from_db.get(i)
									.getKode_customer();
							String username = jadwal_from_db.get(i)
									.getUsername();
							String alamat = jadwal_from_db.get(i).getAlamat();
							String nama_lengkap = jadwal_from_db.get(i)
									.getNama_toko();
							int id_wilayah = jadwal_from_db.get(i).getId_wilayah();
							String status = jadwal_from_db.get(i).getStatus();
							String date = jadwal_from_db.get(i).getDate();
							String check_in = jadwal_from_db.get(i).getCheckin();
							String check_out = jadwal_from_db.get(i).getCheckout();
							String status_update = jadwal_from_db.get(i).getStatus_update();

							Jadwal jadwal = new Jadwal();
							jadwal.setId_jadwal(id_jadwal);
							jadwal.setKode_jadwal(kode_jadwal);
							jadwal.setKode_customer(kode_customer);
							jadwal.setUsername(username);
							jadwal.setAlamat(alamat);
							jadwal.setNama_toko(nama_lengkap);
							jadwal.setId_wilayah(id_wilayah);
							jadwal.setStatus(status);
							jadwal.setDate(date);
							jadwal.setCheckin(check_in);
							jadwal.setCheckout(check_out);
							jadwal.setStatus_update(status_update);
							jadwal_list.add(jadwal);
							cAdapter = new ListViewAdapter(
									JadwalCustomerActivity.this,
									R.layout.list_item_jadwal_customer,
									jadwal_list);
							listview.setAdapter(cAdapter);
							cAdapter.notifyDataSetChanged();
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					jadwal_list.clear();
					ArrayList<Jadwal> jadwal_from_db = databaseHandler
							.getAllJadwal();
					if (jadwal_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < jadwal_from_db.size(); i++) {
							int id_jadwal = jadwal_from_db.get(i)
									.getId_jadwal();
							String kode_jadwal = jadwal_from_db.get(i)
									.getKode_jadwal();
							String kode_customer = jadwal_from_db.get(i)
									.getKode_customer();
							String username = jadwal_from_db.get(i)
									.getUsername();
							String alamat = jadwal_from_db.get(i).getAlamat();
							String nama_lengkap = jadwal_from_db.get(i)
									.getNama_toko();
							int id_wilayah = jadwal_from_db.get(i).getId_wilayah();
							String status = jadwal_from_db.get(i).getStatus();
							String date = jadwal_from_db.get(i).getDate();
							String check_in = jadwal_from_db.get(i).getCheckin();
							String check_out = jadwal_from_db.get(i).getCheckout();
							String status_update = jadwal_from_db.get(i).getStatus_update();

							Jadwal jadwal = new Jadwal();
							jadwal.setId_jadwal(id_jadwal);
							jadwal.setKode_jadwal(kode_jadwal);
							jadwal.setKode_customer(kode_customer);
							jadwal.setUsername(username);
							jadwal.setAlamat(alamat);
							jadwal.setNama_toko(nama_lengkap);
							jadwal.setId_wilayah(id_wilayah);
							jadwal.setStatus(status);
							jadwal.setDate(date);
							jadwal.setCheckin(check_in);
							jadwal.setCheckout(check_out);
							jadwal.setStatus_update(status_update);
							jadwal_list.add(jadwal);
							cAdapter = new ListViewAdapter(
									JadwalCustomerActivity.this,
									R.layout.list_item_jadwal_customer,
									jadwal_list);
							listview.setAdapter(cAdapter);
							cAdapter.notifyDataSetChanged();
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

	private void gotoJadwal() {
		Intent i = new Intent(this, JadwalActivity.class);
		startActivity(i);
		finish();
	}

	public void saveAppDataKodeJadwal(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_KODE_JADWAL,
				responsedata);
		editor.commit();
	}

	public void saveAppDataCustomerKodeCustomer(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(
				CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_KODE_CUSTOMER,
				responsedata);
		editor.commit();
	}

	public void saveAppDataCustomerIdJadwal(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_JADWAL_ID_JADWAL,
				responsedata);
		editor.commit();
	}

	private SharedPreferences getSharedPrefereces() {
		return act.getSharedPreferences(CONFIG.SHARED_PREFERENCES_NAME,
				Context.MODE_PRIVATE);
	}

	public void showCustomerFromKodeJadwal(String kode_jadwal_from_preferenced) {
		jadwal_list.clear();
		ArrayList<Jadwal> jadwal_from_db = databaseHandler
				.getAllJadwalWhereKodeJadwal(kode_jadwal_from_preferenced);
		if (jadwal_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < jadwal_from_db.size(); i++) {
				int id_jadwal = jadwal_from_db.get(i).getId_jadwal();
				String kode_jadwal = jadwal_from_db.get(i).getKode_jadwal();
				String kode_customer = jadwal_from_db.get(i).getKode_customer();
				String username = jadwal_from_db.get(i).getUsername();
				String alamat = jadwal_from_db.get(i).getAlamat();
				String nama_lengkap = jadwal_from_db.get(i).getNama_toko();
				int id_wilayah = jadwal_from_db.get(i).getId_wilayah();
				String status = jadwal_from_db.get(i).getStatus();
				String date = jadwal_from_db.get(i).getDate();
				String checkin = jadwal_from_db.get(i).getCheckin();
				String checkout = jadwal_from_db.get(i).getCheckout();
				String status_update = jadwal_from_db.get(i).getStatus_update();

				Jadwal jadwal = new Jadwal();
				jadwal.setId_jadwal(id_jadwal);
				jadwal.setKode_jadwal(kode_jadwal);
				jadwal.setKode_customer(kode_customer);
				jadwal.setUsername(username);
				jadwal.setAlamat(alamat);
				jadwal.setNama_toko(nama_lengkap);
				jadwal.setId_wilayah(id_wilayah);
				jadwal.setStatus(status);
				jadwal.setDate(date);
				jadwal.setCheckin(checkin);
				jadwal.setCheckout(checkout);
				jadwal.setStatus_update(status_update);
				jadwal_list.add(jadwal);
			}
			cAdapter = new ListViewAdapter(this,
					R.layout.list_item_jadwal_customer, jadwal_list);
			listview.setAdapter(cAdapter);
			cAdapter.notifyDataSetChanged();

		} else {
			listview.setVisibility(View.INVISIBLE);
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

	public class ListViewAdapter extends ArrayAdapter<Jadwal> {
		Activity activity;
		int layoutResourceId;
		Jadwal customerData;
		ArrayList<Jadwal> data = new ArrayList<Jadwal>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<Jadwal> data) {
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
				holder.list_status = (TextView) row
						.findViewById(R.id.customer_title_status_customer);
				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}
			customerData = data.get(position);
			holder.list_kodeCustomer.setText(customerData.getKode_customer());
			holder.list_namaCustomer.setText(customerData.getNama_toko());
			holder.list_alamat.setText(customerData.getAlamat());
			int status_update = Integer.parseInt(data.get(position)
					.getStatus_update());
			if (status_update == 1) {
				String msg = getApplicationContext().getResources().getString(
						R.string.app_jadwal_status_update_1);
				holder.list_status.setText(msg);
			} else if (status_update == 2) {
				String msg = getApplicationContext().getResources().getString(
						R.string.app_jadwal_status_update_2);
				holder.list_status.setText(msg);
			} else {
				String msg = getApplicationContext().getResources().getString(
						R.string.app_jadwal_status_update_3);
				holder.list_status.setText(msg);
			}
			holder.list_kodeCustomer.setTypeface(typefaceSmall);
			holder.list_namaCustomer.setTypeface(typefaceSmall);
			holder.list_alamat.setTypeface(typefaceSmall);
			holder.list_status.setTypeface(typefaceSmall);
			row.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					//int countCustomer = databaseHandler.getCountCustomerWhereValidAndUpdate();
					//if (countCustomer != 0) {
					//		String message = act.getApplicationContext().getResources()
					//				.getString(R.string.app_customer_status_update_unuploaded);
					//		showCustomDialog(message);
					//}else{
					String kode_customer = String.valueOf(data.get(position)
							.getKode_customer());
					saveAppDataCustomerKodeCustomer(kode_customer);
					String id_jadwal = String.valueOf(data.get(position)
							.getId_jadwal());
					saveAppDataCustomerIdJadwal(id_jadwal);
					gotoDetailCustomer();
					//}
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeCustomer;
			TextView list_namaCustomer;
			TextView list_alamat;
			TextView list_status;
		}

	}

	public void gotoDetailCustomer() {
		Intent i = new Intent(this, DetailJadwalCustomerActivity.class);
		startActivity(i);
		finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
