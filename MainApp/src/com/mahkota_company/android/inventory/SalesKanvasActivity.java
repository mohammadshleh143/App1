package com.mahkota_company.android.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.R;
import com.mahkota_company.android.contact.PerbaikanActivity;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DetailSalesOrder;
import com.mahkota_company.android.database.NewDetailPenjualan;
import com.mahkota_company.android.database.Penjualan;
import com.mahkota_company.android.database.PenjualanDetail;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.StockVan;
import com.mahkota_company.android.prospect.AddCustomerProspectActivity;
import com.mahkota_company.android.sales_order.AddSalesOrderActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SalesKanvasActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private EditText etNamaCustomer;
	private EditText etAlamat;
	private EditText etKeterangan;
	private Location location;
	private double latitude; // latitude
	private double longitude; // longitude
	private TextView tvNoNotaValue,tvImage1Customer,tvImage2Customer,ttd1;
	private TextView tvTotalbayarValue;
	private TextView tvHeaderTotalbayarTitle;
	private LocationManager locationManager;
	//private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private Button mButtonAddProduct,mButtonCustomerDetailImage1,mButtonCustomerDetailImage2,mButtonCustomerttd1;
	private Button mButtonSave;
	//private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private ListViewChooseAdapter cAdapterChooseAdapter;
	private ArrayList<NewDetailPenjualan> detailPenjualanList = new ArrayList<NewDetailPenjualan>();
	private ArrayList<Penjualan> penjualanList = new ArrayList<Penjualan>();
	private ArrayList<PenjualanDetail> penjualanDetailList = new ArrayList<PenjualanDetail>();
	private ListView listview;
	private ListViewAdapter cAdapter;

	private ArrayList<StockVan> stokvan_from_db = new ArrayList<StockVan>();

	private ScrollView scrollView;

	private static final String LOG_TAG = SalesKanvasActivity.class
			.getSimpleName();
	private String main_app_id_staff;
	private String username;
	private String nama;
	private String alamat;
	private String keterangan;
	private String newImageName;

	private ProgressDialog progressDialog;
	private Handler handler = new Handler();
	private String message;
	private String response_data;
	private String no_penjualan, ttd, foto1, foto2;
	private String lats;
	private String longs;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Uri fileUri; // file url to store image/video
	private final int MEDIA_TYPE_IMAGE = 1;
	private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private String IMAGE_DIRECTORY_NAME = "Kanvas";
	private String id_Image = "";
	private int counterFoto = 0;
	File file;
	Dialog dialog;
	LinearLayout mContent;
	View view;
	signature mSignature;
	Bitmap bitmap;
	private String newTTD1;
	Button btn_get_sign, mClear, mGetSign, mCancel;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_sales_kanvas);
		act = this;
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle(getApplicationContext().getResources()
				.getString(R.string.app_name));
		progressDialog
				.setMessage(getApplicationContext()
						.getResources()
						.getString(
								R.string.app_inventory_processing_download_stock_summary));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				gotoInventory();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		scrollView = (ScrollView) findViewById(R.id.scrollView);
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		tvNoNotaValue = (TextView) findViewById(R.id.sales_to_value_nomor_nota);
		etNamaCustomer = (EditText) findViewById(R.id.sales_to_value_nama_customer);
		etAlamat = (EditText) findViewById(R.id.sales_to_value_alamat);
		etKeterangan = (EditText) findViewById(R.id.sales_to_value_keterangan);

		tvImage1Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image);
		tvImage2Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_2);
		ttd1 = (TextView) findViewById(R.id.activity_customer_detail_ttd1);

		mButtonCustomerDetailImage1 = (Button) findViewById(R.id.activity_customer_detail_btn_image);
		mButtonCustomerDetailImage2 = (Button) findViewById(R.id.activity_customer_detail_btn_image_2);
		mButtonCustomerttd1 = (Button) findViewById(R.id.activity_customer_detail_btn_ttd1);

		tvTotalbayarValue = (TextView) findViewById(R.id.sales_to_total_bayar_value);
		tvHeaderTotalbayarTitle = (TextView) findViewById(R.id.sales_to_total_bayar_title);

		mButtonAddProduct = (Button) findViewById(R.id.sales_to_btn_add_product);
		mButtonSave = (Button) findViewById(R.id.sales_to_btn_save);

		tvTotalbayarValue.setTypeface(typefaceSmall);
		tvHeaderTotalbayarTitle.setTypeface(typefaceSmall);
		SharedPreferences spPreferences = getSharedPrefereces();
		main_app_id_staff = spPreferences.getString(CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,null);
		username= spPreferences.getString(CONFIG.SHARED_PREFERENCES_STAFF_USERNAME,null);
		String unNota = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault()).format(new Date());
		tvNoNotaValue.setText(CONFIG.CONFIG_APP_KODE_SK_HEADER+unNota+"."+username);
		//mButtonSave.setOnClickListener(buttonOnClickListener);
		mButtonSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(passValidationForUpload()) {
					nama = etNamaCustomer.getText().toString();
					alamat = etAlamat.getText().toString();
					keterangan = etKeterangan.getText().toString();
					no_penjualan = tvNoNotaValue.getText().toString();
					foto1 = tvImage1Customer.getText().toString();
					foto2 = tvImage1Customer.getText().toString();
					ttd = ttd1.getText().toString();
					lats = String.valueOf(latitude);
					longs = String.valueOf(longitude);
					new UploadData().execute();
				}

			}
		});

		mButtonAddProduct.setOnClickListener(buttonOnClickListener);
		checkGPS();

		final String main_app_foto_1 = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_KANVAS_FOTO_1, null);
		final String main_app_foto_2 = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_KANVAS_FOTO_2, null);

		if (main_app_foto_1 != null && main_app_foto_1.length() > 0) {
			counterFoto +=1;
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ IMAGE_DIRECTORY_NAME);
			String imgResource = dir.getPath() + "/" + main_app_foto_1;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			final Bitmap bitmap = BitmapFactory.decodeFile(imgResource, options);
			tvImage1Customer.setText(main_app_foto_1);

			tvImage1Customer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					final Dialog settingsDialog = new Dialog(act);
					settingsDialog.getWindow().requestFeature(
							Window.FEATURE_NO_TITLE);
					settingsDialog.setContentView(getLayoutInflater().inflate(
							R.layout.activity_popup_image_supplier, null));
					ImageView imgPreview = (ImageView) settingsDialog
							.findViewById(R.id.mygallery);
					imgPreview.setImageBitmap(bitmap);
					imgPreview.setImageBitmap(bitmap);
					Button button = (Button) settingsDialog
							.findViewById(R.id.btn);
					button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							settingsDialog.dismiss();
						}
					});
					settingsDialog.show();

				}
			});

		}

		if (main_app_foto_2 != null && main_app_foto_2.length() > 0) {
			counterFoto +=1;
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ IMAGE_DIRECTORY_NAME);
			String imgResource = dir.getPath() + "/" + main_app_foto_2;
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 3;
			final Bitmap bitmap = BitmapFactory.decodeFile(imgResource, options);
			tvImage2Customer.setText(main_app_foto_2);

			tvImage2Customer.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {

					final Dialog settingsDialog = new Dialog(act);
					settingsDialog.getWindow().requestFeature(
							Window.FEATURE_NO_TITLE);
					settingsDialog.setContentView(getLayoutInflater().inflate(
							R.layout.activity_popup_image_supplier, null));
					ImageView imgPreview = (ImageView) settingsDialog
							.findViewById(R.id.mygallery);
					imgPreview.setImageBitmap(bitmap);
					imgPreview.setImageBitmap(bitmap);
					Button button = (Button) settingsDialog
							.findViewById(R.id.btn);
					button.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							settingsDialog.dismiss();
						}
					});
					settingsDialog.show();

				}
			});
		}

		mButtonCustomerDetailImage1.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(etNamaCustomer.getText().length()<=0){
					String msg = getApplicationContext()
							.getResources()
							.getString(R.string.app_inventory_physical_counting_failed_empty_nama);
					showCustomDialog(msg);

				}else{
					id_Image = "1";
					String msg = getApplicationContext().getResources().getString(R.string.app_supplier_already_take_foto_1);
					if(tvImage1Customer.getText().length() > 0) {
						showCustomDialogReplaceOldFoto(msg);
					}
					else {
						gotoCaptureImage();
					}

				}

			}
		});
		mButtonCustomerDetailImage2.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(etNamaCustomer.getText().length()<=0){
					String msg = getApplicationContext()
							.getResources()
							.getString(R.string.app_inventory_physical_counting_failed_empty_nama);
					showCustomDialog(msg);
				}else{
					id_Image = "2";
					String msg = getApplicationContext().getResources().getString(R.string.app_supplier_already_take_foto_2);
					if(tvImage2Customer.getText().length() > 0) {
						showCustomDialogReplaceOldFoto(msg);
					}
					else {
						gotoCaptureImage();
					}
				}

			}
		});

		mButtonCustomerttd1.setOnClickListener(mDetailCustomerButtonOnClickListener);
	}

	public class UploadData extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog
					.setMessage(getApplicationContext()
							.getResources()
							.getString(
									R.string.app_supplier_processing));
			progressDialog.show();
			progressDialog.setCancelable(false);
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
			String url = CONFIG.CONFIG_APP_URL_PUBLIC;
			String uploadSupplier = CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_SALES;
			String upload_image_kanvas_url = url
					+ uploadSupplier;

			response_data = uploadImage(
					upload_image_kanvas_url,
					main_app_id_staff,
					"0",no_penjualan,
					nama,alamat,
					keterangan,
					lats,longs,
					foto1,foto2,ttd);
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				if (response_data.startsWith("Error occurred")) {
					message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_to_processing_error);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} else {
					JSONObject objectResponse;
					String status = "false";
					Log.d(LOG_TAG, "response_data:" + response_data);
					try {
						objectResponse = new JSONObject(response_data);
						status = objectResponse.isNull("error") ? "True"
								: objectResponse.getString("error");
						Log.d(LOG_TAG, "status:" + status);
						if (status.equalsIgnoreCase("True")) {
							final String msg = act
									.getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_to_processing_failed);
							handler.post(new Runnable() {
								public void run() {
									showCustomDialog(msg);
								}
							});
						} else {
							if (progressDialog != null) {
								progressDialog.dismiss();
							}
							new UpdatePenjualanDetailToServer().execute();

						}
					} catch (JSONException e) {
						final String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_to_processing_error);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					}
				}
			} else {
				final String msg = getApplicationContext().getResources()
						.getString(R.string.app_sales_to_processing_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	protected String uploadImage(final String url,  final String id_staff,
								 final String type_penjualan, final String no_penjualan,
								 final String nama_customer, final String alamat,
								 final String keterangan, final String lats,
								 final String longs,final String foto1,
								 final String foto2,final String ttd) {
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
			File dir1 = new File(CONFIG.getFolderPath() + "/"
					+ IMAGE_DIRECTORY_NAME + "/"
					+ foto1);
			if (dir1.exists() && foto1 != null) {
				entity.addPart("foto1", new FileBody(dir1));
				entity.addPart("foto1", new StringBody(foto1));
			}
			else {
				entity.addPart("foto1", new StringBody(""));
			}


			File dir2 = new File(CONFIG.getFolderPath() + "/"
					+ IMAGE_DIRECTORY_NAME + "/"
					+ foto2);
			if (dir2.exists() && foto2 != null) {
				entity.addPart("foto2", new FileBody(dir2));
				entity.addPart("foto2", new StringBody(foto2));
			}
			else {
				entity.addPart("foto2", new StringBody(""));
			}

			File dir3 = new File(CONFIG.getFolderPath() + "/"
					+ IMAGE_DIRECTORY_NAME + "/"
					+ ttd);
			if (dir3.exists() && ttd != null) {
				entity.addPart("ttd", new FileBody(dir3));
				entity.addPart("ttd", new StringBody(ttd));
			}
			else {
				entity.addPart("ttd", new StringBody(""));
			}

			entity.addPart("id_staff", new StringBody(id_staff));
			entity.addPart("type_penjualan", new StringBody(type_penjualan));
			entity.addPart("no_penjualan", new StringBody(no_penjualan));
			entity.addPart("nama_customer", new StringBody(nama_customer));
			entity.addPart("alamat", new StringBody(alamat));
			entity.addPart("keterangan", new StringBody(keterangan));
			entity.addPart("lats", new StringBody(lats));
			entity.addPart("longs", new StringBody(longs));

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

	private final OnClickListener mDetailCustomerButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			if(etNamaCustomer.getText().length()<=0){
				String msg = getApplicationContext()
						.getResources()
						.getString(R.string.app_inventory_physical_counting_failed_empty_nama);
				showCustomDialog(msg);
			}else {
				int getId = arg0.getId();
				switch (getId) {
					case R.id.activity_customer_detail_btn_ttd1:
						String msg = getApplicationContext().getResources().getString(R.string.app_supplier_already_take_ttd);
						if(ttd1.getText().length() > 0){
							showCustomDialogReplaceOldttd(msg);
						}else{
							gotoTTD1();
						}
						break;
				}
			}
		}

	};

	public void gotoTTD1() {
		dialog = new Dialog(SalesKanvasActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_signature);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);


		mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
		mSignature = new signature(getApplicationContext(), null);
		mSignature.setBackgroundColor(Color.WHITE);
		mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mClear = (Button) dialog.findViewById(R.id.clear);
		mGetSign = (Button) dialog.findViewById(R.id.getsign);
		mGetSign.setEnabled(false);
		mCancel = (Button) dialog.findViewById(R.id.cancel);
		view = mContent;

		mClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Cleared");
				mSignature.clear();
				mGetSign.setEnabled(false);
			}
		});

		mGetSign.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				File dir = new File(CONFIG.getFolderPath() + "/"+ IMAGE_DIRECTORY_NAME);
				if (!dir.exists()) {
					dir.mkdirs();
				}

				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
						Locale.getDefault()).format(new Date());

				File mediaFile;
				mediaFile = new File(dir.getPath() + File.separator +"TTD1_IMG."
						+ username +"_"+timeStamp+ ".png");

				Log.v("log_tag", "Panel Saved");
				view.setDrawingCacheEnabled(true);
				mSignature.save(view, mediaFile);
				dialog.dismiss();
				Toast.makeText(getApplicationContext(), "Successfully Saved", Toast.LENGTH_SHORT).show();
				// Calling the same class
				//recreate();

			}
		});

		mCancel.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Canceled");
				dialog.dismiss();
				// Calling the same class
				//recreate();
			}
		});
		dialog.show();
	}

	public class signature extends View {
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public signature(Context context, AttributeSet attrs) {
			super(context, attrs);
			paint.setAntiAlias(true);
			paint.setColor(Color.BLACK);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeJoin(Paint.Join.ROUND);
			paint.setStrokeWidth(STROKE_WIDTH);
		}

		public void save(View v, File StoredPath) {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
					Locale.getDefault()).format(new Date());
			newTTD1 = "TTD1_IMG."+ username +"_"+timeStamp+ ".png";

			Log.v("log_tag", "Width: " + v.getWidth());
			Log.v("log_tag", "Height: " + v.getHeight());
			if (bitmap == null) {
				bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
			}
			ttd1.setText(newTTD1);

			Canvas canvas = new Canvas(bitmap);
			try {
				// Output the file
				FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
				v.draw(canvas);

				// Convert the output file to Image such as .png
				bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
				mFileOutStream.flush();
				mFileOutStream.close();

			} catch (Exception e) {
				Log.v("log_tag", e.toString());
			}
			return ;
		}


		public void clear() {
			path.reset();
			invalidate();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawPath(path, paint);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float eventX = event.getX();
			float eventY = event.getY();
			mGetSign.setEnabled(true);

			switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					path.moveTo(eventX, eventY);
					lastTouchX = eventX;
					lastTouchY = eventY;
					return true;

				case MotionEvent.ACTION_MOVE:

				case MotionEvent.ACTION_UP:
					resetDirtyRect(eventX, eventY);
					int historySize = event.getHistorySize();
					for (int i = 0; i < historySize; i++) {
						float historicalX = event.getHistoricalX(i);
						float historicalY = event.getHistoricalY(i);
						expandDirtyRect(historicalX, historicalY);
						path.lineTo(historicalX, historicalY);
					}
					path.lineTo(eventX, eventY);
					break;
				default:
					debug("Ignored touch event: " + event.toString());
					return false;
			}

			invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
					(int) (dirtyRect.top - HALF_STROKE_WIDTH),
					(int) (dirtyRect.right + HALF_STROKE_WIDTH),
					(int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

			lastTouchX = eventX;
			lastTouchY = eventY;
			return true;
		}

		private void debug(String string) {

			Log.v("log_tag", string);

		}

		private void expandDirtyRect(float historicalX, float historicalY) {
			if (historicalX < dirtyRect.left) {
				dirtyRect.left = historicalX;
			} else if (historicalX > dirtyRect.right) {
				dirtyRect.right = historicalX;
			}

			if (historicalY < dirtyRect.top) {
				dirtyRect.top = historicalY;
			} else if (historicalY > dirtyRect.bottom) {
				dirtyRect.bottom = historicalY;
			}
		}

		private void resetDirtyRect(float eventX, float eventY) {
			dirtyRect.left = Math.min(lastTouchX, eventX);
			dirtyRect.right = Math.max(lastTouchX, eventX);
			dirtyRect.top = Math.min(lastTouchY, eventY);
			dirtyRect.bottom = Math.max(lastTouchY, eventY);
		}
	}

	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	private File getOutputMediaFile(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ IMAGE_DIRECTORY_NAME);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator + "IMG_KANVAS"+ id_Image + "." + username + "_" + timeStamp + ".jpg");
			newImageName = "IMG_KANVAS"+ id_Image + "." + username + "_" + timeStamp + ".jpg";
		} else {
			return null;
		}
		return mediaFile;
	}

	private LocationListener locationListener = new LocationListener() {

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			try {
				String strStatus = "";
				switch (status) {
					case GpsStatus.GPS_EVENT_FIRST_FIX:
						strStatus = "GPS_EVENT_FIRST_FIX";
						break;
					case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
						strStatus = "GPS_EVENT_SATELLITE_STATUS";
						break;
					case GpsStatus.GPS_EVENT_STARTED:
						strStatus = "GPS_EVENT_STARTED";
						break;
					case GpsStatus.GPS_EVENT_STOPPED:
						strStatus = "GPS_EVENT_STOPPED";
						break;
					default:
						strStatus = String.valueOf(status);
						break;
				}
				Log.i(LOG_TAG, "locationListener " + strStatus);
			} catch (Exception e) {
				Log.d(LOG_TAG, "locationListener Error");
			}
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onLocationChanged(Location location) {
			try {
				latitude = location.getLatitude();
				longitude = location.getLongitude();
				Log.d(LOG_TAG, "latitude " + latitude);
				Log.d(LOG_TAG, "longitude " + longitude);
			} catch (Exception e) {
				Log.i(LOG_TAG, "onLocationChanged " + e.toString());
			}
		}
	};

	private void checkGPS() {
		locationManager = (LocationManager) getApplicationContext()
				.getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000L, 1.0f, locationListener);
		boolean isGPSEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!isGPSEnabled) {
			startActivityForResult(new Intent(
							android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
					0);
		} else {
			// if GPS Enabled get lat/long using GPS Services
			if (isGPSEnabled) {
				if (location == null) {
					locationManager.requestLocationUpdates(
							LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES,
							MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
					Log.d(LOG_TAG, "GPS Enabled");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.GPS_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();

						}
					}
				}
			} else {
				Intent intentGps = new Intent(
						"android.location.GPS_ENABLED_CHANGE");
				intentGps.putExtra("enabled", true);
				act.sendBroadcast(intentGps);
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
								gotoInventory();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	/**
	 * Validasi Empty Box
	 *
	 * @return
	 */
	public boolean passValidationForUpload() {
		if (GlobalApp.isBlank(etNamaCustomer)) {
			GlobalApp.takeDefaultAction(
					etNamaCustomer,
					SalesKanvasActivity.this,
					getApplicationContext().getResources().getString(
							R.string.app_sales_to_failed_no_nama));
			return false;
		}
		else if (GlobalApp.isBlank(etAlamat)) {
			GlobalApp.takeDefaultAction(
					etAlamat,
					SalesKanvasActivity.this,
					getApplicationContext().getResources().getString(
							R.string.app_sales_to_failed_no_alamat));
			return false;
		}
		else if (GlobalApp.isBlank(etKeterangan)) {
			GlobalApp.takeDefaultAction(
					etKeterangan,
					SalesKanvasActivity.this,
					getApplicationContext().getResources().getString(
							R.string.app_sales_to_failed_no_keterangan));
			return false;
		}
		return true;
	}

	private final OnClickListener buttonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
				case R.id.sales_to_btn_add_product:
					if(isRunningDummyVersion()) {
						ChooseProductDialog();
					}
					else {
						int countProduct = databaseHandler.getCountStockVan();
						if (countProduct == 0) {
							String msg = getApplicationContext()
									.getResources()
									.getString(R.string.app_inventory_physical_counting_failed_empty_data_stock_van);
							showCustomDialog(msg);
						} else {
							ChooseProductDialog();
						}
					}

					break;
				case R.id.sales_to_btn_save:
					if(passValidationForUpload()) {
						nama = etNamaCustomer.getText().toString();
						alamat = etAlamat.getText().toString();
						keterangan = etKeterangan.getText().toString();
						no_penjualan = tvNoNotaValue.getText().toString();
						foto1 = tvImage1Customer.getText().toString();
						foto2 = tvImage1Customer.getText().toString();
						ttd = ttd1.getText().toString();
						lats = String.valueOf(latitude);
						longs = String.valueOf(longitude);
						if (GlobalApp.checkInternetConnection(act)) {
							new UploadPenjualanKeServer().execute();
						} else {
							String message = act
									.getApplicationContext()
									.getResources()
									.getString(
											R.string.MSG_DLG_LABEL_CHECK_INTERNET_CONNECTION);
							showCustomDialog(message);
						}
//						for (DetailPenjualan detailPenjualan : detailPenjualanList) {
//
//						}
//						String msg = getApplicationContext()
//								.getResources()
//								.getString(
//										R.string.app_inventory_unload_product_target_penjualan_add_save_success);
//						showCustomDialogSaveSuccess(msg);
					}
					break;
				default:
					break;
			}
		}

	};


	/**
	 * Setup the URL Sales Order
	 *
	 * @return
	 */
	public String prepareUrlForPenjualan() {
		return String.format(CONFIG.CONFIG_APP_URL_PUBLIC
				+ CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_SALES);
	}

	/**
	 * Post Penjualan ke Server
	 *
	 * @return
	 */
	public String postDataForPenjualan() {
		String textUrl = prepareUrlForPenjualan();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		HttpResponse response;

		nameValuePairs.add(new BasicNameValuePair("no_penjualan", no_penjualan));
		nameValuePairs.add(new BasicNameValuePair("type_penjualan", "0"));
		nameValuePairs.add(new BasicNameValuePair("nama_customer",nama));
		nameValuePairs.add(new BasicNameValuePair("alamat", alamat));
		nameValuePairs.add(new BasicNameValuePair("id_staff", main_app_id_staff));
		nameValuePairs.add(new BasicNameValuePair("keterangan", keterangan));
		nameValuePairs.add(new BasicNameValuePair("foto1", foto1));
		nameValuePairs.add(new BasicNameValuePair("foto2", foto2));
		nameValuePairs.add(new BasicNameValuePair("ttd", ttd));
		nameValuePairs.add(new BasicNameValuePair("lats", lats));
		nameValuePairs.add(new BasicNameValuePair("longs", longs));

		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(textUrl);
		String responseString = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// Server response
				responseString = EntityUtils.toString(r_entity);
			} else {
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (UnsupportedEncodingException e1) {
			response = null;
		} catch (IOException e) {
			response = null;
		}
		return responseString;
	}

	class UploadPenjualanKeServer extends AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setTitle(getApplicationContext().getResources()
					.getString(R.string.app_name));
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_sales_to_processing));
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			response_data = postDataForPenjualan();

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				if (response_data.startsWith("Error occurred")) {
					message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_to_processing_error);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} else {
					JSONObject objectResponse;
					String status = "false";
					Log.d(LOG_TAG, "response_data:" + response_data);
					try {
						objectResponse = new JSONObject(response_data);
						status = objectResponse.isNull("error") ? "True"
								: objectResponse.getString("error");
						Log.d(LOG_TAG, "status:" + status);
						if (status.equalsIgnoreCase("True")) {
							final String msg = act
									.getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_to_processing_failed);
							handler.post(new Runnable() {
								public void run() {
									showCustomDialog(msg);
								}
							});
						} else {
							if (progressDialog != null) {
								progressDialog.dismiss();
							}
							new UpdatePenjualanDetailToServer().execute();

						}
					} catch (JSONException e) {
						final String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_to_processing_error);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					}
				}
			} else {
				final String msg = getApplicationContext().getResources()
						.getString(R.string.app_sales_to_processing_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	/**
	 * Setup the URL Sales Order
	 *
	 * @return
	 */
	public String prepareUrlForPenjualanDetail() {
		return String.format(CONFIG.CONFIG_APP_URL_PUBLIC
				+ CONFIG.CONFIG_APP_URL_UPLOAD_INSERT_SALES_DETAIL);
	}

	/**
	 * Post Add Sales Order ke Server
	 *
	 * @return
	 */
	public String postDataForPenjualanDetail(String no_penjualan, String id_product,
											 String jumlah,String jumlah1,String jumlah2,
											 String jumlah3, String harga, String harga_jual,
											 String nomer_request_load, String id_staff) {
		String textUrl = prepareUrlForPenjualanDetail();
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		HttpResponse response;
		nameValuePairs.add(new BasicNameValuePair("no_penjualan", no_penjualan));
		nameValuePairs.add(new BasicNameValuePair("id_product", id_product));
		nameValuePairs.add(new BasicNameValuePair("jumlah", jumlah));
		nameValuePairs.add(new BasicNameValuePair("jumlah1", jumlah1));
		nameValuePairs.add(new BasicNameValuePair("jumlah2", jumlah2));
		nameValuePairs.add(new BasicNameValuePair("jumlah3", jumlah3));
		nameValuePairs.add(new BasicNameValuePair("harga", harga));
		nameValuePairs.add(new BasicNameValuePair("harga_jual", harga_jual));
		nameValuePairs.add(new BasicNameValuePair("nomer_request_load", nomer_request_load));
		nameValuePairs.add(new BasicNameValuePair("id_staff", id_staff));
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(textUrl);
		String responseString = null;
		try {
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			response = httpclient.execute(httppost);
			HttpEntity r_entity = response.getEntity();
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode == 200) {
				// Server response
				responseString = EntityUtils.toString(r_entity);
			} else {
				responseString = "Error occurred! Http Status Code: "
						+ statusCode;
			}

		} catch (UnsupportedEncodingException e1) {
			response = null;
		} catch (IOException e) {
			response = null;
		}
		return responseString;
	}

	class UpdatePenjualanDetailToServer extends
			AsyncTask<String, Integer, String> {
		@Override
		protected void onPreExecute() {
			progressDialog.setTitle(getApplicationContext().getResources()
					.getString(R.string.app_name));
			progressDialog.setMessage(getApplicationContext().getResources()
					.getString(R.string.app_sales_to_processing));
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			for (NewDetailPenjualan detailPenjualan : detailPenjualanList) {
				response_data = postDataForPenjualanDetail(
						no_penjualan, String.valueOf(detailPenjualan.getId_product()),
						String.valueOf(detailPenjualan.getJumlah_order()),String.valueOf(detailPenjualan.getJumlah_order1()),
						String.valueOf(detailPenjualan.getJumlah_order2()),String.valueOf(detailPenjualan.getJumlah_order3()),
						String.valueOf(detailPenjualan.getHarga()),String.valueOf(detailPenjualan.getHarga_jual()),
						String.valueOf(detailPenjualan.getNomer_request_load()),
						String.valueOf(detailPenjualan.getId_staff()));
				if (response_data.startsWith("Error occurred")) {
					break;
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (response_data != null) {
				if (response_data.startsWith("Error occurred")) {
					message = act
							.getApplicationContext()
							.getResources()
							.getString(
									R.string.app_sales_to_processing_error);
					handler.post(new Runnable() {
						public void run() {
							showCustomDialog(message);
						}
					});
				} else {
					JSONObject objectResponse;
					String status = "false";
					Log.d(LOG_TAG, "response_data:" + response_data);
					try {
						objectResponse = new JSONObject(response_data);
						status = objectResponse.isNull("error") ? "True"
								: objectResponse.getString("error");
						Log.d(LOG_TAG, "status:" + status);
					} catch (JSONException e) {
						final String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_to_processing_error);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					}

					if (status.equalsIgnoreCase("True")) {
						final String msg = act
								.getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_to_processing_failed);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialog(msg);
							}
						});
					} else {
						final String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.app_sales_to_processing_sukses);
						handler.post(new Runnable() {
							public void run() {
								showCustomDialogPenjualanSuccess(msg);
							}
						});

					}

				}
			} else {
				final String msg = getApplicationContext().getResources()
						.getString(R.string.app_sales_to_processing_failed);
				handler.post(new Runnable() {
					public void run() {
						showCustomDialog(msg);
					}
				});
			}
		}
	}

	public void showCustomDialogPenjualanSuccess(String msg) {
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
								refreshStatus();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void refreshStatus() {
		finish();
		startActivity(getIntent());
	}

	protected boolean isRunningDummyVersion() {
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_staff_username = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
		if (main_app_staff_username != null) {
			return main_app_staff_username.equalsIgnoreCase("demo");
		}
		return false;
	}

	private void ChooseProductDialog() {
		final Dialog chooseProductDialog = new Dialog(act);
		chooseProductDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseProductDialog.setContentView(R.layout.activity_main_product_choose_dialog);
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
		final ArrayList<StockVan> stockvan_list = new ArrayList<StockVan>();
		final ListView listview = (ListView) chooseProductDialog
				.findViewById(R.id.list);
		searchProduct.setFocusable(true);
		searchProduct.setFocusableInTouchMode(true);
		searchProduct.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(searchProduct, InputMethodManager.SHOW_IMPLICIT);

		final EditText jumlahPieces = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pieces);
		final EditText jumlahRenceng = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_renceng);
		final EditText jumlahPack = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_pack);
		final EditText jumlahDus = (EditText) chooseProductDialog
				.findViewById(R.id.activity_product_edittext_dus);
		final EditText spiDisc = (EditText) chooseProductDialog
				.findViewById(R.id.spinner_disc);



		listview.setItemsCanFocus(false);
		ArrayList<StockVan> stockvan_from_db = databaseHandler.getAllStockVan();
		if (stockvan_from_db.size() > 0) {
			listview.setVisibility(View.VISIBLE);
			for (int i = 0; i < stockvan_from_db.size(); i++) {
				int id_product = stockvan_from_db.get(i).getId_product();
				String nama_product = stockvan_from_db.get(i).getNama_product();
				String kode_product = stockvan_from_db.get(i).getKode_product();
				String harga_jual = stockvan_from_db.get(i).getHarga_jual();
				String jumlah_request = stockvan_from_db.get(i).getJumlahRequest();
				String jumlah_accept = stockvan_from_db.get(i).getJumlahAccept();
				String jumlah_sisa = stockvan_from_db.get(i).getJumlahSisa();
				String id_kemasan = stockvan_from_db.get(i).getIdKemasan();
				String foto = stockvan_from_db.get(i).getFoto();
				String deskripsi = stockvan_from_db.get(i).getDeskripsi();
				String nomer_request_load = stockvan_from_db.get(i).getNomer_request_load();

				StockVan stockvan = new StockVan();
				stockvan.setId_product(id_product);
				stockvan.setNama_product(nama_product);
				stockvan.setKode_product(kode_product);
				stockvan.setHarga_jual(harga_jual);
				stockvan.setJumlahRequest(jumlah_request);
				stockvan.setJumlahAccept(jumlah_accept);
				stockvan.setJumlahSisa(jumlah_sisa);
				stockvan.setIdKemasan(id_kemasan);
				stockvan.setFoto(foto);
				stockvan.setDeskripsi(deskripsi);
				stockvan.setNomer_request_load(nomer_request_load);
				stockvan_list.add(stockvan);
				cAdapterChooseAdapter = new ListViewChooseAdapter(
						SalesKanvasActivity.this,
						R.layout.list_item_product_sales_order,
						jumlahPieces,
						jumlahRenceng,
						jumlahPack,
						jumlahDus, spiDisc,
						stockvan_list, chooseProductDialog);
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
					stockvan_list.clear();
					ArrayList<StockVan> stockvan_from_db = databaseHandler
							.getAllStockVanBaseOnSearch(cs.toString());
					if (stockvan_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < stockvan_from_db.size(); i++) {
							int id_product = stockvan_from_db.get(i).getId_product();
							String nama_product = stockvan_from_db.get(i).getNama_product();
							String kode_product = stockvan_from_db.get(i).getKode_product();
							String harga_jual = stockvan_from_db.get(i).getHarga_jual();
							String jumlah_request = stockvan_from_db.get(i).getJumlahRequest();
							String jumlah_accept = stockvan_from_db.get(i).getJumlahAccept();
							String jumlah_sisa = stockvan_from_db.get(i).getJumlahSisa();
							String id_kemasan = stockvan_from_db.get(i).getIdKemasan();
							String foto = stockvan_from_db.get(i).getFoto();
							String deskripsi = stockvan_from_db.get(i).getDeskripsi();
							String nomer_request_load = stockvan_from_db.get(i).getNomer_request_load();

							StockVan stockvan = new StockVan();
							stockvan.setId_product(id_product);
							stockvan.setNama_product(nama_product);
							stockvan.setKode_product(kode_product);
							stockvan.setHarga_jual(harga_jual);
							stockvan.setJumlahRequest(jumlah_request);
							stockvan.setJumlahAccept(jumlah_accept);
							stockvan.setJumlahSisa(jumlah_sisa);
							stockvan.setIdKemasan(id_kemasan);
							stockvan.setFoto(foto);
							stockvan.setDeskripsi(deskripsi);
							stockvan.setNomer_request_load(nomer_request_load);
							stockvan_list.add(stockvan);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									SalesKanvasActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahPieces,
									jumlahRenceng,
									jumlahPack,
									jumlahDus,
									spiDisc,
									stockvan_list, chooseProductDialog);
							listview.setAdapter(cAdapterChooseAdapter);
							cAdapterChooseAdapter.notifyDataSetChanged();
						}
					} else {
						listview.setVisibility(View.INVISIBLE);
					}

				} else {
					ArrayList<StockVan> stockvan_from_db = databaseHandler
							.getAllStockVan();
					if (stockvan_from_db.size() > 0) {
						listview.setVisibility(View.VISIBLE);
						for (int i = 0; i < stockvan_from_db.size(); i++) {
							int id_product = stockvan_from_db.get(i).getId_product();
							String nama_product = stockvan_from_db.get(i).getNama_product();
							String kode_product = stockvan_from_db.get(i).getKode_product();
							String harga_jual = stockvan_from_db.get(i).getHarga_jual();
							String jumlah_request = stockvan_from_db.get(i).getJumlahRequest();
							String jumlah_accept = stockvan_from_db.get(i).getJumlahAccept();
							String jumlah_sisa = stockvan_from_db.get(i).getJumlahSisa();
							String id_kemasan = stockvan_from_db.get(i).getIdKemasan();
							String foto = stockvan_from_db.get(i).getFoto();
							String deskripsi = stockvan_from_db.get(i).getDeskripsi();
							String nomer_request_load = stockvan_from_db.get(i).getNomer_request_load();

							StockVan stockvan = new StockVan();
							stockvan.setId_product(id_product);
							stockvan.setNama_product(nama_product);
							stockvan.setKode_product(kode_product);
							stockvan.setHarga_jual(harga_jual);
							stockvan.setJumlahRequest(jumlah_request);
							stockvan.setJumlahAccept(jumlah_accept);
							stockvan.setJumlahSisa(jumlah_sisa);
							stockvan.setIdKemasan(id_kemasan);
							stockvan.setFoto(foto);
							stockvan.setDeskripsi(deskripsi);
							stockvan.setNomer_request_load(nomer_request_load);
							stockvan_list.add(stockvan);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									SalesKanvasActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahPieces,
									jumlahRenceng,
									jumlahPack,
									jumlahDus,
									spiDisc,
									stockvan_list, chooseProductDialog);
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


	private void updateListViewDetailOrder(NewDetailPenjualan detailPenjualan) {
		detailPenjualanList.add(detailPenjualan);

		listview.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				scrollView.requestDisallowInterceptTouchEvent(true);
				int action = event.getActionMasked();
				switch (action) {
					case MotionEvent.ACTION_UP:
						scrollView.requestDisallowInterceptTouchEvent(false);
						break;
				}
				return false;
			}
		});

		cAdapter = new ListViewAdapter(SalesKanvasActivity.this,
				R.layout.list_item_detail_new_penjualan, detailPenjualanList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
		updateTotalBayar();
	}

	private void updateTotalBayar() {
		int subtotalBayar = 0;
		float hargaterkecil = 0;
		for (NewDetailPenjualan tempDetailPenjualan : detailPenjualanList) {
			Product tempProduct = databaseHandler.getProduct(tempDetailPenjualan.getId_product());
			float uom1,uom2,uom3,uom4, pcs, rcg, pck, dus, jmldisc;
			uom1 = Float.parseFloat(tempProduct.getUomqtyl1());
			uom2 = Float.parseFloat(tempProduct.getUomqtyl2());
			uom3 = Float.parseFloat(tempProduct.getUomqtyl3());
			uom4 = Float.parseFloat(tempProduct.getUomqtyl4());
			hargaterkecil =  Float.parseFloat(tempDetailPenjualan.getHarga_jual())/(uom1*uom2*uom3*uom4);

			pcs = tempDetailPenjualan.getJumlah_order();
			rcg = tempDetailPenjualan.getJumlah_order1();
			pck = tempDetailPenjualan.getJumlah_order2();
			dus = tempDetailPenjualan.getJumlah_order3();
			jmldisc = tempDetailPenjualan.getJmldisc();

			subtotalBayar += hargaterkecil * pcs * uom4 + hargaterkecil * rcg * uom3 * uom4 +
					hargaterkecil * pck * uom2 * uom3 * uom4 + hargaterkecil * dus * uom1 * uom2 * uom3 * uom4 -
					((hargaterkecil * pcs * uom4 + hargaterkecil * rcg * uom3 * uom4 +
							hargaterkecil * pck * uom2 * uom3 * uom4 + hargaterkecil * dus * uom1 * uom2 * uom3 * uom4)/100.0f)*jmldisc;
		}

		Float nilai_awal = Float.parseFloat(tvTotalbayarValue.getText().toString());
		Float totalBayarTemp = nilai_awal + subtotalBayar;
		//Float priceIDR = Float.valueOf(totalBayarTemp);
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
		tvTotalbayarValue.setText(df.format(0+subtotalBayar));
		/*
		NewDetailPenjualan newDetailPenjualan = new NewDetailPenjualan();
		String totalBayarTemp = newDetailPenjualan.getHarga();
		//Float priceIDR = Float.valueOf(totalBayarTemp);
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
		otherSymbols.setDecimalSeparator(',');
		otherSymbols.setGroupingSeparator('.');
		DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
		tvTotalbayarValue.setText("Rp. " +(totalBayarTemp));
		*/
	}


	public class ListViewAdapter extends ArrayAdapter<NewDetailPenjualan> {
		Activity activity;
		int layoutResourceId;
		ArrayList<NewDetailPenjualan> data = new ArrayList<NewDetailPenjualan>();

		public ListViewAdapter(Activity act, int layoutResourceId,
							   ArrayList<NewDetailPenjualan> data) {
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
						.findViewById(R.id.sales_kode_product);
				holder.list_harga_jual = (TextView) row
						.findViewById(R.id.sales_harga);
				holder.list_jumlah_order = (TextView) row
						.findViewById(R.id.sales_jumlah_order);
				holder.list_jumlah_order1 = (TextView) row
						.findViewById(R.id.sales_jumlah_order1);
				holder.list_jumlah_order2 = (TextView) row
						.findViewById(R.id.sales_jumlah_order2);
				holder.list_jumlah_order3 = (TextView) row
						.findViewById(R.id.sales_jumlah_order3);
				holder.list_jumlah_subtotal = (TextView) row
						.findViewById(R.id.sales_jumlah_subtotal);

				row.setTag(holder);
			} else {
				holder = (UserHolder) row.getTag();
			}

			holder.list_kode_product.setText(data.get(position).getKode_product());
			holder.list_jumlah_order.setText(String.valueOf(data.get(position).getJumlah_order()));
			Float priceIDR = Float.valueOf(data.get(position).getHarga_jual());
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
			holder.list_harga_jual.setText(df.format(priceIDR));
			holder.list_jumlah_order1.setText(String.valueOf(data.get(position).getJumlah_order1()));
			holder.list_jumlah_order2.setText(String.valueOf(data.get(position).getJumlah_order2()));
			holder.list_jumlah_order3.setText(String.valueOf(data.get(position).getJumlah_order3()));
			holder.list_jumlah_subtotal.setText(String.valueOf(data.get(position).getHarga()));
			holder.list_kode_product.setTypeface(typefaceSmall);
			holder.list_jumlah_order.setTypeface(typefaceSmall);
			holder.list_harga_jual.setTypeface(typefaceSmall);
			holder.list_jumlah_order.setTypeface(typefaceSmall);
			holder.list_jumlah_order1.setTypeface(typefaceSmall);
			holder.list_jumlah_order2.setTypeface(typefaceSmall);
			holder.list_jumlah_order3.setTypeface(typefaceSmall);
			holder.list_jumlah_subtotal.setTypeface(typefaceSmall);
			//holder.list_disc.setTypeface(typefaceSmall);

			return row;

		}

		class UserHolder {
			TextView list_kode_product;
			TextView list_harga_jual;
			TextView list_jumlah_order;
			TextView list_jumlah_order1;
			TextView list_jumlah_order2;
			TextView list_jumlah_order3;
			TextView list_jumlah_subtotal;
			//TextView list_disc;
		}

	}

	class ListViewChooseAdapter extends ArrayAdapter<StockVan> {
		int layoutResourceId;
		StockVan productData;
		ArrayList<StockVan> data = new ArrayList<StockVan>();
		Activity mainActivity;
		EditText jumlahPieces;
		EditText jumlahRenceng;
		EditText jumlahPack;
		EditText jumlahDus;
		EditText spiDisc;
		Dialog chooseProductDialog;

		public ListViewChooseAdapter(Activity mainActivity,
									 int layoutResourceId,
									 EditText jumlahPieces,
									 EditText jumlahRenceng,
									 EditText jumlahPack,
									 EditText jumlahDus,
									 EditText spiDisc,
									 ArrayList<StockVan> data, Dialog chooseProductDialog) {
			super(mainActivity, layoutResourceId, data);
			this.layoutResourceId = layoutResourceId;
			this.data = data;
			this.chooseProductDialog = chooseProductDialog;
			this.mainActivity = mainActivity;
			this.jumlahPieces = jumlahPieces;
			this.jumlahRenceng = jumlahRenceng;
			this.jumlahPack = jumlahPack;
			this.jumlahDus = jumlahDus;
			this.spiDisc = spiDisc;
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
				holder.list_img = (ImageView) row.findViewById(R.id.image);
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
			File dir = new File(CONFIG.getFolderPath() + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/"
					+ CONFIG.CONFIG_APP_FOLDER_PRODUCT + "/"
					+ data.get(position).getFoto());
			holder.list_img.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath()));
			//holder.list_kodeProduct.setText(productData.getKode_product());
			holder.list_namaProduct.setText(productData.getNama_product());
			Float priceIDR = Float.valueOf(productData.getHarga_jual());
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols();
			otherSymbols.setDecimalSeparator(',');
			otherSymbols.setGroupingSeparator('.');

			DecimalFormat df = new DecimalFormat("#,##0", otherSymbols);
			//holder.list_harga.setText("Rp. " + df.format(priceIDR));
			//holder.list_kodeProduct.setTypeface(getTypefaceSmall());
			holder.list_namaProduct.setTypeface(getTypefaceSmall());
			//holder.list_harga.setTypeface(getTypefaceSmall());
			holder.mButtonAddItem.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (Float.parseFloat(spiDisc.getText().toString())>=0 && Float.parseFloat(spiDisc.getText().toString()) <= 2 ){
						/*
						if (jumlahPieces.getText().length()==0){
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_pcs);
							showCustomDialog(msg);
						}else if(jumlahRenceng.getText().length()==0){
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_pck);
							showCustomDialog(msg);
						} else if(jumlahPack.getText().length()==0){
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_dus);
							showCustomDialog(msg);
						}else if(jumlahDus.getText().length()==0){
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_dus);
							showCustomDialog(msg);
						}
						*/
						if (jumlahPieces.getText().length()==0&&jumlahRenceng.getText().length()==0&&
								jumlahPack.getText().length()==0&&jumlahDus.getText().length()==0) {

							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_jumlah);
							showCustomDialog(msg);
						} else if(jumlahPieces.getText().length()==0||jumlahRenceng.getText().length()==0||
								jumlahPack.getText().length()==0||jumlahDus.getText().length()==0){
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_0);
							showCustomDialog(msg);
						}else if(jumlahPieces.getText().toString().equals("0")&&jumlahRenceng.getText().toString().equals("0")&&
								jumlahPack.getText().toString().equals("0")&&jumlahDus.getText().toString().equals("0")){

							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_jumlah);
							showCustomDialog(msg);
						}else{
							if (jumlahPieces.getText().toString().length() > 0 || jumlahRenceng.getText().toString().length() > 0 ||
									jumlahPack.getText().toString().length() > 0 || jumlahDus.getText().toString().length() > 0) {
								boolean containSameProduct = false;
								for (NewDetailPenjualan detailPenjualan : detailPenjualanList) {
									if (detailPenjualan.getKode_product().equalsIgnoreCase(
											data.get(position).getKode_product())) {
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
									Product tempProduct = databaseHandler.getProduct(data.get(position).getId_product());
									//StockVan temp = databaseHandler.getProduct(data.get(position).getId_product());
									float uom1,uom2,uom3,uom4;
									uom1=Float.parseFloat(tempProduct.getUomqtyl1());
									uom2=Float.parseFloat(tempProduct.getUomqtyl2());
									uom3=Float.parseFloat(tempProduct.getUomqtyl3());
									uom4=Float.parseFloat(tempProduct.getUomqtyl4());

									updateListViewDetailOrder(new NewDetailPenjualan(
											count,
											data.get(position).getId_product(),
											data.get(position).getNama_product(),
											data.get(position).getKode_product(),
											data.get(position).getHarga_jual(),
											Integer.parseInt(jumlahPieces.getText().toString()),
											Integer.parseInt(jumlahRenceng.getText().toString()),
											Integer.parseInt(jumlahPack.getText().toString()),
											Integer.parseInt(jumlahDus.getText().toString()),
											String.valueOf(
													(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
															* Float.parseFloat(jumlahPieces.getText().toString())*uom4+
															(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																	* Float.parseFloat(jumlahRenceng.getText().toString())*uom3*uom4 +
															(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																	* Float.parseFloat(jumlahPack.getText().toString())*uom2*uom3*uom4 +
															(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																	* Float.parseFloat(jumlahDus.getText().toString())*uom1*uom2*uom3*uom4 -
															(((Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																	* Float.parseFloat(jumlahPieces.getText().toString())*uom4+
																	(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																			* Float.parseFloat(jumlahRenceng.getText().toString())*uom3*uom4 +
																	(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																			* Float.parseFloat(jumlahPack.getText().toString())*uom2*uom3*uom4 +
																	(Float.parseFloat(data.get(position).getHarga_jual())/(uom1*uom2*uom3*uom4))
																			* Float.parseFloat(jumlahDus.getText().toString())*uom1*uom2*uom3*uom4
																			/100.0f) * Float.parseFloat(spiDisc.getText().toString()))
											),
											data.get(position).getNomer_request_load(),
											Integer.parseInt(main_app_id_staff),
											Float.parseFloat(spiDisc.getText().toString())));
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

					}else{
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.saleskanvas_alret_disc);
						showCustomDialog(msg);
					}
				}
			});
			return row;

		}

		class UserHolder {
			TextView list_kodeProduct;
			TextView list_namaProduct;
			ImageView list_img;
			TextView list_harga;
			Button mButtonAddItem;
		}

	}



	public Bitmap addWatermark(Bitmap srcBitmap, String watermark, Point point) {
		Runtime.getRuntime().gc();
		int w = srcBitmap.getWidth();
		int h = srcBitmap.getHeight();
		Bitmap result = Bitmap.createBitmap(w, h, srcBitmap.getConfig());

		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(srcBitmap, 0, 0, null);

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(27);
		paint.setAntiAlias(true);
		// paint.setUnderlineText(true);
		canvas.drawText(watermark, point.x, point.y, paint);

		return result;
	}
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				if(id_Image.equalsIgnoreCase("1")) {
					File newFile = new File(CONFIG.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_NAME + "/" + newImageName);
					Uri contentUri = Uri.fromFile(newFile);
					Bitmap photo = null;

					try {
						photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
								contentUri);
						String watermarkText = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss",
								Locale.getDefault()).format(new Date());
						checkGPS();
						String lokasigps = String.valueOf(latitude) + "/" + String.valueOf(longitude);

						int sizeW = photo.getWidth() / 9;
						int sizeH = photo.getHeight() / 5;
						int sizeH2 = photo.getHeight() / 3;
						photo = addWatermark(photo, lokasigps , new Point(sizeW,sizeH));
						photo = addWatermark(photo, watermarkText + "_MAS", new Point(sizeW,sizeH2));
						OutputStream fOut = new FileOutputStream(newFile);
						photo.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
						fOut.flush();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					//saveAppKanvasFoto1(newImageName);
					tvImage1Customer.setText(newImageName);
				}
				if(id_Image.equalsIgnoreCase("2")) {
					File newFile = new File(CONFIG.getFolderPath() + "/"
							+ IMAGE_DIRECTORY_NAME + "/" + newImageName);
					Uri contentUri = Uri.fromFile(newFile);
					Bitmap  photo = null;
					try {
						photo = MediaStore.Images.Media.getBitmap(getContentResolver(),
								contentUri);
						String watermarkText = new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss",
								Locale.getDefault()).format(new Date());
						checkGPS();
						String lokasigps = String.valueOf(latitude) + "/" + String.valueOf(longitude);
						//String namajalan = alamat.getText().toString();

						int sizeW = photo.getWidth() / 9;
						int sizeH = photo.getHeight() / 5;
						//int sizeH1 = photo.getHeight() / 4;
						int sizeH2 = photo.getHeight() / 3;
						photo = addWatermark(photo, lokasigps , new Point(sizeW,sizeH));
						//photo = addWatermark(photo, namajalan , new Point(sizeW,sizeH1));
						photo = addWatermark(photo, watermarkText + "_MAS", new Point(sizeW,sizeH2));
						OutputStream fOut = new FileOutputStream(newFile);
						photo.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
						fOut.flush();
						fOut.close();
					} catch (IOException e) {
						e.printStackTrace();
					}

					//saveAppKanvasFoto2(newImageName);
					tvImage2Customer.setText(newImageName);
				}

				//refreshStatus();
				// successfully captured the image
				// display it in image view
			} else if (resultCode == RESULT_CANCELED) {
				// user cancelled Image capture
				Toast.makeText(getApplicationContext(),
						"User cancelled image capture", Toast.LENGTH_SHORT)
						.show();
			} else {
				// failed to capture image
				Toast.makeText(getApplicationContext(),
						"Sorry! Failed to capture image", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	public void showCustomDialogReplaceOldFoto(String msg) {
		final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
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
								gotoCaptureImage();

							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NO),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();

							}
						});
		android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void showCustomDialogReplaceOldttd(String msg) {
		final android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
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
								gotoTTD1();

							}
						})
				.setPositiveButton(
						getApplicationContext().getResources().getString(
								R.string.MSG_DLG_LABEL_NO),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder
										.create();
								alertDialog.dismiss();

							}
						});
		android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void gotoCaptureImage() {

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

	public void gotoInventory() {
		Intent i = new Intent(this, InventoryActivity.class);
		startActivity(i);
		finish();
	}

	public Typeface getTypefaceSmall() {
		return typefaceSmall;
	}

	public void saveAppKanvasFoto1(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_KANVAS_FOTO_1,
				responsedata);
		editor.commit();
	}

	public void saveAppKanvasFoto2(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_KANVAS_FOTO_2,
				responsedata);
		editor.commit();
	}

	public void saveAppKanvasTTD(String responsedata) {
		SharedPreferences sp = getSharedPrefereces();
		SharedPreferences.Editor editor = sp.edit();
		editor.putString(CONFIG.SHARED_PREFERENCES_KANVAS_TTD,
				responsedata);
		editor.commit();
	}

	public void setTypefaceSmall(Typeface typefaceSmall) {
		this.typefaceSmall = typefaceSmall;
	}
}
