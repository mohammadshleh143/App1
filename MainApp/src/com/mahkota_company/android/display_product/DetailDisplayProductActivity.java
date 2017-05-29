package com.mahkota_company.android.display_product;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DisplayProduct;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.R;

@SuppressWarnings("deprecation")
public class DetailDisplayProductActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private String newImageName;
	private Typeface typefaceSmall;
	private EditText etKodeCustomer;
	private EditText etNamaCustomer;
	private EditText etNamaDisplayProduct;
	private EditText etDeskripsiDisplayProduct;
	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderNamaDisplayProduct;
	private TextView tvHeaderDeskripsiDisplayProduct;
	private Button mButtonDisplayProductUpdateImage;
	private Button mButtonDisplayProductPreviewPhoto;
	private Button mButtonDisplayProductSave;
	boolean isContainImage = true;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri; // file url to store image/video
	private DisplayProduct displayProduct;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_display_product);
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
		etKodeCustomer = (EditText) findViewById(R.id.activity_display_product_kode_customer_value);
		etNamaCustomer = (EditText) findViewById(R.id.activity_display_product_nama_customer_value);
		etNamaDisplayProduct = (EditText) findViewById(R.id.activity_display_product_nama_display_product_value);
		etDeskripsiDisplayProduct = (EditText) findViewById(R.id.activity_display_product_deskripsi_value);
		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_app_display_product_kode_customer_title);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_app_display_product_nama_customer_title);
		tvHeaderNamaDisplayProduct = (TextView) findViewById(R.id.activity_app_display_product_nama_display_product_title);
		tvHeaderDeskripsiDisplayProduct = (TextView) findViewById(R.id.activity_display_product_deskripsi_title);
		mButtonDisplayProductUpdateImage = (Button) findViewById(R.id.activity_display_product_btn_take_image);
		mButtonDisplayProductSave = (Button) findViewById(R.id.activity_display_product_btn_save);
		mButtonDisplayProductPreviewPhoto = (Button) findViewById(R.id.activity_display_product_btn_preview);

		etNamaCustomer.setTypeface(typefaceSmall);
		etNamaDisplayProduct.setTypeface(typefaceSmall);
		etDeskripsiDisplayProduct.setTypeface(typefaceSmall);

		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaDisplayProduct.setTypeface(typefaceSmall);
		tvHeaderDeskripsiDisplayProduct.setTypeface(typefaceSmall);
		etKodeCustomer.setEnabled(false);
		etNamaCustomer.setEnabled(false);
		mButtonDisplayProductPreviewPhoto
				.setOnClickListener(maddDisplayProductButtonOnClickListener);
		mButtonDisplayProductSave
				.setOnClickListener(maddDisplayProductButtonOnClickListener);
		mButtonDisplayProductUpdateImage
				.setOnClickListener(maddDisplayProductButtonOnClickListener);
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_table_id = spPreferences
				.getString(
						CONFIG.SHARED_PREFERENCES_TABLE_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT,
						null);
		if (main_app_table_id != null) {
			displayProduct = databaseHandler.getDisplayProduct(Integer
					.parseInt(main_app_table_id));
			etKodeCustomer.setText(displayProduct.getKode_customer());
			etNamaCustomer.setText(displayProduct.getNama_lengkap());
			etDeskripsiDisplayProduct.setText(displayProduct.getDeskripsi());
			etNamaDisplayProduct.setText(displayProduct
					.getNama_display_product());
		} else {
			gotoDisplayProduct();
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

	private void previewImageDialog() {
		List<File> mListImages = new ArrayList<File>();
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_DISPLAY_PRODUCT + "/"
				+ displayProduct.getFoto());
		mListImages.add(dir);

		final Dialog imagesDialog = new Dialog(act);
		imagesDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		imagesDialog.setContentView(DetailDisplayProductActivity.this
				.getLayoutInflater().inflate(R.layout.activity_popup_image,
						null));

		Gallery galleryImages = (Gallery) imagesDialog
				.findViewById(R.id.gallery1);
		galleryImages.setAdapter(new ImageAdapter(this, mListImages));

		imagesDialog.show();

	}

	public class ImageAdapter extends BaseAdapter {
		private Context context;
		private int itemBackground;
		private List<File> listFile;

		public ImageAdapter(Context context, List<File> listFile) {
			this.context = context;
			this.listFile = listFile;
			TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
			itemBackground = a.getResourceId(
					R.styleable.MyGallery_android_galleryItemBackground, 0);
			a.recycle();
		}

		public int getCount() {
			return listFile.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView = new ImageView(context);
			if (listFile == null) {
				Bitmap icon = BitmapFactory.decodeResource(
						context.getResources(), R.drawable.ic_logo);
				imageView.setImageBitmap(icon);
			} else {
				if (listFile.get(position).exists()) {
					imageView.setImageBitmap(BitmapFactory.decodeFile(listFile
							.get(position).getAbsolutePath()));
				} else {
					Bitmap icon = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.ic_logo);
					imageView.setImageBitmap(icon);
				}
			}
			imageView.setBackgroundResource(itemBackground);
			return imageView;
		}
	}

	private final OnClickListener maddDisplayProductButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_display_product_btn_preview:
				if (displayProduct != null) {
					File dir1 = new File(CONFIG.getFolderPath() + "/"
							+ CONFIG.CONFIG_APP_FOLDER_DISPLAY_PRODUCT + "/"
							+ displayProduct.getFoto());
					if (dir1.exists()) {
						previewImageDialog();
					} else {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_display_product_missing_file);
						showCustomDialog(msg);
					}
				}
				break;
			case R.id.activity_display_product_btn_take_image:
				gotoCaptureImage();
				break;
			case R.id.activity_display_product_btn_save:
				if (etNamaCustomer.getText().toString().length() > 0
						&& etNamaDisplayProduct.getText().toString().length() > 0
						&& etDeskripsiDisplayProduct.getText().toString()
								.length() > 0)
					if (newImageName != null) {
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
						SharedPreferences spPreferences = getSharedPrefereces();
						String main_app_table_id = spPreferences
								.getString(
										CONFIG.SHARED_PREFERENCES_TABLE_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT,
										null);
						DisplayProduct displayProduct = new DisplayProduct();
						displayProduct.setId_display_product(Integer
								.parseInt(main_app_table_id));
						displayProduct.setNama_lengkap(etNamaCustomer.getText()
								.toString());
						displayProduct.setDatetime(datetime);
						displayProduct.setDeskripsi(etDeskripsiDisplayProduct
								.getText().toString());
						displayProduct.setFoto(newImageName);
						displayProduct.setKode_customer(etKodeCustomer
								.getText().toString());
						displayProduct
								.setNama_display_product(etNamaDisplayProduct
										.getText().toString());
						databaseHandler.updateDisplayProduct(
								Integer.parseInt(main_app_table_id),
								displayProduct);
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_display_product_save_success);
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
