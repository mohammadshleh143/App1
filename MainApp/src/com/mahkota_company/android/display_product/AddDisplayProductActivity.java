package com.mahkota_company.android.display_product;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DisplayProduct;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.SpinnerAdapter;
import com.mahkota_company.android.R;

public class AddDisplayProductActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private String newImageName;
	private Typeface typefaceSmall;
	private EditText etNamaCustomer;
	private EditText etNamaDisplayProduct, etNoDTG,etjmlBelumbayar,etjmlBayar;
	private EditText etDeskripsiDisplayProduct;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderNamaDisplayProduct;
	private TextView tvHeaderDeskripsiDisplayProduct;
	private Button mButtonDisplayProductUpdateImage;
	private Button mButtonDisplayProductSave;
	boolean isContainImage = true;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri; // file url to store image/video
	private Spinner spinnerKodeCustomer;
	public ArrayList<Customer> customerList;
	public ArrayList<String> customerStringList;
	private String kodeCustomer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_display_product);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoDisplayProduct();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		spinnerKodeCustomer = (Spinner) findViewById(R.id.activity_display_product_kode_customer_value);
		etNamaCustomer = (EditText) findViewById(R.id.activity_display_product_nama_customer_value);
		etNamaDisplayProduct = (EditText) findViewById(R.id.activity_display_product_nama_display_product_value);
		etNoDTG=(EditText) findViewById(R.id.activity_display_product_no_dtg_value);
		etjmlBelumbayar=(EditText) findViewById(R.id.activity_display_product_jml_blm_bayar_value);
		etjmlBayar=(EditText) findViewById(R.id.activity_display_product_jml_bayar_value);
		etDeskripsiDisplayProduct = (EditText) findViewById(R.id.activity_display_product_deskripsi_value);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_app_display_product_kode_customer_title);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_app_display_product_nama_customer_title);
		tvHeaderNamaDisplayProduct = (TextView) findViewById(R.id.activity_app_display_product_nama_display_product_title);
		tvHeaderDeskripsiDisplayProduct = (TextView) findViewById(R.id.activity_display_product_deskripsi_title);
		mButtonDisplayProductUpdateImage = (Button) findViewById(R.id.activity_display_product_btn_take_image);
		mButtonDisplayProductSave = (Button) findViewById(R.id.activity_display_product_btn_save);

		etNamaCustomer.setTypeface(typefaceSmall);
		etNamaDisplayProduct.setTypeface(typefaceSmall);
		etNoDTG.setTypeface(typefaceSmall);
		etjmlBelumbayar.setTypeface(typefaceSmall);
		etjmlBayar.setTypeface(typefaceSmall);
		etDeskripsiDisplayProduct.setTypeface(typefaceSmall);

		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaDisplayProduct.setTypeface(typefaceSmall);
		tvHeaderDeskripsiDisplayProduct.setTypeface(typefaceSmall);
		customerList = new ArrayList<Customer>();
		customerStringList = new ArrayList<String>();
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_id_wilayah = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH, null);
		String main_app_staff_username = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
		/*
		if (main_app_staff_id_wilayah.length() > 0) {
			List<Customer> dataCustomer = databaseHandler
					.getAllCustomerActive(Integer
							.parseInt(main_app_staff_id_wilayah));
			for (Customer customer : dataCustomer) {
				int countDisplayProduct = databaseHandler
						.getCountDisplayProduct(customer.getKode_customer());
				if (countDisplayProduct == 0) {
					customerList.add(customer);
					customerStringList.add(customer.getKode_customer());
				}
			}
		}
		*/
		if (main_app_staff_username.length() > 0) {
			List<Jadwal> dataJadwal = databaseHandler
					.getAllJadwalWhereKodeStaffAndGroupByCustomer(main_app_staff_username);
			List<Customer> dataCustomer = new ArrayList<Customer>();
			for (Jadwal jadwal : dataJadwal) {
				Customer customer = new Customer(0, jadwal.getKode_customer(),
						null, null, null, null, jadwal.getNama_toko(), null,
						jadwal.getId_wilayah(), null, null, null, 0, null,
						null, null, 0, null, null, null, null, null, null, null,
						0, null, null, null,null, null, null, null, null, null,
						null, null,0, null,null,null,null,null);
				dataCustomer.add(customer);

			}
			for (Customer customer : dataCustomer) {
				int countJadwal = databaseHandler.getCountSalesOrder(customer
						.getKode_customer());
				if (countJadwal == 0) {
					customerList.add(customer);
					customerStringList.add(customer.getKode_customer());
				}
			}
		}else {
			gotoSalesOrder();
		}

		etNamaCustomer.setEnabled(false);
		etNoDTG.setEnabled(false);
		etjmlBelumbayar.setEnabled(false);
		etjmlBayar.setEnabled(false);
		// ArrayAdapter<String> adapterCustomer = new ArrayAdapter<String>(this,
		// R.layout.my_spinner_item, customerStringList);
		// adapterCustomer
		// .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpinnerAdapter adapterCustomer = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				customerStringList);
		adapterCustomer
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerKodeCustomer.setAdapter(adapterCustomer);
		spinnerKodeCustomer
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						Customer customer = databaseHandler
								.getCustomerByKodeCustomer(customerStringList
										.get(position));
						etNamaCustomer.setText(customer.getNama_lengkap());
						kodeCustomer = customerStringList.get(position);
						etNamaCustomer.setText(customer.getNama_toko());
						kodeCustomer = customerStringList.get(position);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		kodeCustomer = customerStringList.get(0);
		Customer customer = databaseHandler
				.getCustomerByKodeCustomer(customerStringList.get(0));
		etNamaCustomer.setText(customer.getNama_lengkap());
		mButtonDisplayProductSave
				.setOnClickListener(maddisplayProductButtonOnClickListener);
		mButtonDisplayProductUpdateImage
				.setOnClickListener(maddisplayProductButtonOnClickListener);

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
								gotoDisplayProduct();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	private final OnClickListener maddisplayProductButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_display_product_btn_take_image:
				gotoCaptureImage();
				break;
			case R.id.activity_display_product_btn_save:
				if (etNamaCustomer.getText().toString().length() > 0
						&& etNamaDisplayProduct.getText().toString().length() > 0
						&& etDeskripsiDisplayProduct.getText().toString()
								.length() > 0)
					if (newImageName != null) {
						int id_display_product = databaseHandler
								.getCountDisplayProduct();
						final String date = "yyyy-MM-dd";
						Calendar calendar = Calendar.getInstance();
						SimpleDateFormat dateFormat = new SimpleDateFormat(date);
						final String dateOutput = dateFormat.format(calendar
								.getTime());
						Calendar now = Calendar.getInstance();
						int hrs = now.get(Calendar.HOUR_OF_DAY);
						int min = now.get(Calendar.MINUTE);
						int sec = now.get(Calendar.SECOND);
						final String time = zero(hrs) + ":" + zero(min) + ":"
								+ zero(sec);
						String datetime = dateOutput + " " + time;
						DisplayProduct displayProduct = new DisplayProduct();
						displayProduct
								.setId_display_product(id_display_product);
						displayProduct.setNama_lengkap(etNamaCustomer.getText()
								.toString());
						displayProduct.setDatetime(datetime);
						displayProduct.setDeskripsi(etDeskripsiDisplayProduct
								.getText().toString());
						displayProduct.setFoto(newImageName);
						displayProduct.setKode_customer(kodeCustomer);
						displayProduct
								.setNama_display_product(etNamaDisplayProduct
										.getText().toString());
						databaseHandler.add_Display_Product(displayProduct);
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_display_product_add_success);
						showCustomDialogSaveSuccess(msg);
					} else {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_display_product_save_failed_no_image);
						showCustomDialog(msg);
					}
				else {

					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_display_product_save_failed_no_data);
					showCustomDialog(msg);
				}

				break;
			default:
				break;
			}
		}

	};

	public void gotoCaptureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/*
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_DISPLAY_PRODUCT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_kode_staff = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);

		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator
					+ main_app_staff_kode_staff + "_" + "IMG_" + timeStamp
					+ ".png");
			newImageName = main_app_staff_kode_staff + "_" + "IMG_" + timeStamp
					+ ".png";
		} else {
			return null;
		}
		return mediaFile;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {

			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"Batal mengambil foto terbaru!", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Failed to capture image", Toast.LENGTH_SHORT).show();
			}
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

	public void gotoSalesOrder() {
		Intent i = new Intent(this, DisplayProductActivity.class);
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

	public void gotoDisplayProduct() {
		Intent i = new Intent(this, DisplayProductActivity.class);
		startActivity(i);
		finish();
	}
}
