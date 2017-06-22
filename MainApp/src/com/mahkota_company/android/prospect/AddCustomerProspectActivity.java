package com.mahkota_company.android.prospect;

import com.mahkota_company.android.database.Branch;
import com.mahkota_company.android.database.Cluster;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.TypeCustomer;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.SpinnerAdapter;
import com.mahkota_company.android.R;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class AddCustomerProspectActivity extends FragmentActivity {
	Button btn_get_sign, mClear, mGetSign, mCancel;

	File file;
	Dialog dialog;
	LinearLayout mContent;
	View view;
	signature mSignature;
	signature1 mSignature1;
	Bitmap bitmap;

	// Creating Separate Directory for saving Generated Images
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private ProgressDialog progressDialog;
	private String newImageName1;
	private String newImageName2;
	private String newImageName3;
	private String newTTD1;
	private String newTTD2;

	private Typeface typefaceSmall;
	private TextView tvKodeCustomer;

	public static final String KODE_CUSTOMER = "USERNAME";

	private EditText etNamaCustomer;
	private EditText etEmailCustomer;
	private EditText etAlamatCustomer;
	private EditText etTelpCustomer;
	private EditText etNoKtp;
	private EditText etTanggalLahir;
	private EditText etNamaBank;
	private EditText etNoRekening;
	private EditText etAtasNama;
	private EditText etNPWP;
	private EditText etNamaPasar;
    private EditText etCluster;
    private EditText etTelp;
    private EditText etFax;
    private EditText etOmset;
	private EditText etCaraPembayaran;
	private EditText etPlafonKredit;
	private EditText etTermKredit;

	private EditText etNama_istri;
	private EditText etNama_anak1;
	private EditText etNama_anak2;
	private EditText etNama_anak3;
	private EditText etKode_pos;
	private EditText etNama_toko;

	private TextView tvId_depo;
    private Spinner spinnerWilayah;
    private ArrayList<Wilayah> wilayahList;
    private ArrayList<String> wilayahStringList;
    private int idWilayah = 0;


	private Spinner spinnerCluster;
	private ArrayList<Cluster> clusterList;
	private ArrayList<String> clusterStringList;
	private int idCluster = 0;


    private Spinner spinnerTypeCustomer;
	private ArrayList<TypeCustomer> typeCustomerList;
	private ArrayList<String> typeCustomerStringList;
	private int idTypeCustomer = 0;

	private TextView tvGpsCustomer;
	private TextView tvImage1Customer;
	private TextView tvImage2Customer;
	private TextView tvImage3Customer;
	private TextView ttd1;
	private TextView ttd2;


	private TextView tvHeaderKodeCustomer;
	private TextView tvHeaderNamaCustomer;
	private TextView tvHeaderEmailCustomer;
	private TextView tvHeaderAlamatCustomer;
	private TextView tvHeaderWilayahCustomer;
	private TextView tvHeaderTypeCustomer;
	private TextView tvHeaderGpsCustomer;
	private TextView tvHeaderImage1Customer;
	private TextView tvHeaderImage2Customer;
	private TextView tvHeaderImage3Customer;
	private TextView tvHeaderTelpCustomer;
	private Button mButtonCustomerDetailImage1;
	private Button mButtonCustomerDetailImage2;
	private Button mButtonCustomerDetailImage3;
	private Button mButtonCustomerttd1;
	private Button mButtonCustomerttd2;
	private Button mButtonCustomerDetailCancel;
	private Button mButtonCustomerDetailSave;
	private LocationManager locationManager;
	private static final String LOG_TAG = AddCustomerProspectActivity.class
			.getSimpleName();
	boolean isContainImage = true;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private Uri fileUri1, fileUri2, fileUri3; // file url to store image/video
	private double latitude; // latitude
	private double longitude; // longitude
	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Location location; // location
	private Jadwal jadwal;
	final int Date_Dialog_ID=0;
	int cDay,cMonth,cYear; // this is the instances of the current date
	Calendar cDate;
	int sDay,sMonth,sYear;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_customer_prospect);

        //Intent intent = getIntent();
        //String hasil_ttd = intent.getStringExtra(Useless_AndroidCanvas1.HASIL_TTD);
        //ttd1 = (TextView) findViewById(R.id.activity_customer_detail_ttd1);
        //ttd1.setText(newTTD1);

		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoCustomerProspect();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		progressDialog = new ProgressDialog(this);
		//progressDialog.setTitle(getApplicationContext().getResources()
		//		.getString(R.string.app_name));
		progressDialog.setMessage(getApplicationContext().getResources()
				.getString(R.string.app_promosi_processing));
		progressDialog.setCancelable(true);
		progressDialog.setCanceledOnTouchOutside(false);
		tvKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_kode_customer);
		etNamaCustomer = (EditText) findViewById(R.id.activity_customer_prospect_nama_customer_value);
		etEmailCustomer = (EditText) findViewById(R.id.activity_customer_prospect_email_customer_value);
		etAlamatCustomer = (EditText) findViewById(R.id.activity_customer_prospect_alamat_customer_value);

        spinnerWilayah = (Spinner) findViewById(R.id.activity_customer_prospect_wilayah_value);
        spinnerTypeCustomer = (Spinner) findViewById(R.id.activity_customer_prospect_type_customer_value);
		spinnerCluster = (Spinner) findViewById(R.id.activity_customer_prospect_cluster);
		tvGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_gps_location);
		tvImage1Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image);
		tvImage2Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_2);
		tvImage3Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_3);
		ttd1 = (TextView) findViewById(R.id.activity_customer_detail_ttd1);
		ttd2 = (TextView) findViewById(R.id.activity_customer_detail_ttd2);
		etTelpCustomer = (EditText) findViewById(R.id.activity_customer_prospect_telp_customer_value);
		etNoKtp = (EditText) findViewById(R.id.activity_customer_prospect_noktp_value);
		etTanggalLahir = (EditText) findViewById(R.id.activity_customer_prospect_tanggal_lahir_value);
		etNamaBank = (EditText) findViewById(R.id.activity_customer_prospect_nama_bank_value);
		etAtasNama = (EditText) findViewById(R.id.activity_customer_prospect_atas_nama_value);
		etNoRekening = (EditText) findViewById(R.id.activity_customer_prospect_no_rekening_value);
		etNPWP = (EditText) findViewById(R.id.activity_customer_prospect_npwp_value);
        etNamaPasar = (EditText) findViewById(R.id.activity_customer_prospect_nama_pasar_value);
        etCluster = (EditText) findViewById(R.id.activity_customer_prospect_cluster_value);
        etTelp = (EditText) findViewById(R.id.activity_customer_prospect_telp_value);
        etFax = (EditText) findViewById(R.id.activity_customer_prospect_fax_value);
        etOmset = (EditText) findViewById(R.id.activity_customer_prospect_omset_value);
        etCaraPembayaran = (EditText) findViewById(R.id.activity_customer_prospect_cara_pembayaran_value);
        etPlafonKredit = (EditText) findViewById(R.id.activity_customer_prospect_plafon_value);
        etTermKredit = (EditText) findViewById(R.id.activity_customer_prospect_term_value);
		etNama_istri = (EditText) findViewById(R.id.activity_customer_prospect_nama_istri_value);
		etNama_anak1 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak1_value);
		etNama_anak2 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak2_value);
		etNama_anak3 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak3_value);
		etKode_pos = (EditText) findViewById(R.id.activity_customer_prospect_kode_pos_value);
		etNama_toko = (EditText) findViewById(R.id.activity_customer_prospect_nama_toko_value);

		tvHeaderKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_kode_customer);
		tvHeaderNamaCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_nama_customer);
		tvHeaderEmailCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_email_customer);
		tvHeaderAlamatCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_alamat_customer);
		tvHeaderWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_wilayah_customer);
		tvHeaderTypeCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_type_customer);
		tvHeaderGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_gps_location);
		tvHeaderImage1Customer = (TextView) findViewById(R.id.activity_customer_detail_title_image);
		tvHeaderImage2Customer = (TextView) findViewById(R.id.activity_customer_detail_title_image_2);
		tvHeaderImage3Customer = (TextView) findViewById(R.id.activity_customer_detail_title_image_3);
		tvHeaderTelpCustomer = (TextView) findViewById(R.id.activity_customer_detail_title_telp_customer);
		mButtonCustomerDetailImage1 = (Button) findViewById(R.id.activity_customer_detail_btn_image);
		mButtonCustomerDetailImage2 = (Button) findViewById(R.id.activity_customer_detail_btn_image_2);
		mButtonCustomerDetailImage3 = (Button) findViewById(R.id.activity_customer_detail_btn_image_3);
		mButtonCustomerttd1 = (Button) findViewById(R.id.activity_customer_detail_btn_ttd1);
		mButtonCustomerttd2 = (Button) findViewById(R.id.activity_customer_detail_btn_ttd2);
		mButtonCustomerDetailCancel = (Button) findViewById(R.id.activity_customer_detail_btn_cancel);
		mButtonCustomerDetailSave = (Button) findViewById(R.id.activity_customer_detail_btn_save);

		etTanggalLahir.setClickable(true);
		etTanggalLahir.setFocusable(false);
		etTanggalLahir.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog(Date_Dialog_ID);
			}
		});

		cDate=Calendar.getInstance();
		cDay=cDate.get(Calendar.DAY_OF_MONTH);
		cMonth=cDate.get(Calendar.MONTH);
		cYear=cDate.get(Calendar.YEAR);
		sDay=cDay;
		sMonth=cMonth;
		sYear=cYear;
		//updateDateDisplay(sYear,sMonth,sDay);

        //set list wiayah
        wilayahList = new ArrayList<Wilayah>();
        wilayahStringList = new ArrayList<String>();
        List<Wilayah> dataWilayah = databaseHandler
                .getAllWilayah();
        for (Wilayah wilayah : dataWilayah) {
            wilayahList.add(wilayah);
            wilayahStringList.add(wilayah.getNama_wilayah());
        }

        SpinnerAdapter adapterWilayah = new SpinnerAdapter(
                getApplicationContext(), android.R.layout.simple_spinner_item,
                wilayahStringList);
        adapterWilayah.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWilayah.setAdapter(adapterWilayah);
        spinnerWilayah
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        idWilayah = wilayahList.get(position)
                                .getId_wilayah();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
        idWilayah = wilayahList.get(0).getId_wilayah();

        //set list type customer
		typeCustomerList = new ArrayList<TypeCustomer>();
		typeCustomerStringList = new ArrayList<String>();
		List<TypeCustomer> dataTypeCustomer = databaseHandler
				.getAllTypeCustomer();
		for (TypeCustomer typeCustomer : dataTypeCustomer) {
			typeCustomerList.add(typeCustomer);
			typeCustomerStringList.add(typeCustomer.getType_customer());
		}

		SpinnerAdapter adapterCustomer = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				typeCustomerStringList);
		adapterCustomer.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerTypeCustomer.setAdapter(adapterCustomer);
		spinnerTypeCustomer
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						idTypeCustomer = typeCustomerList.get(position)
								.getId_type_customer();
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		idTypeCustomer = typeCustomerList.get(0).getId_type_customer();


		//set list cluster
		clusterList = new ArrayList<Cluster>();
		clusterStringList = new ArrayList<String>();
		List<Cluster> dataCluster = databaseHandler
				.getAllCluster();
		for (Cluster cluster : dataCluster) {
			clusterList.add(cluster);
			clusterStringList.add(cluster.getNama_cluster());
		}

		SpinnerAdapter adapterCluster = new SpinnerAdapter(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				clusterStringList);
		adapterCluster.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerCluster.setAdapter(adapterCluster);
		spinnerCluster
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
											   View view, int position, long id) {
						idCluster = clusterList.get(position)
								.getId_cluster();
					}
					public void onNothingSelected(AdapterView<?> parent) {
					}
				});
		idCluster = clusterList.get(0).getId_cluster();

		tvKodeCustomer.setTypeface(typefaceSmall);
		tvGpsCustomer.setTypeface(typefaceSmall);
		tvImage1Customer.setTypeface(typefaceSmall);
		tvImage2Customer.setTypeface(typefaceSmall);
		tvImage3Customer.setTypeface(typefaceSmall);
		ttd1.setTypeface(typefaceSmall);
		ttd2.setTypeface(typefaceSmall);

		tvHeaderKodeCustomer.setTypeface(typefaceSmall);
		tvHeaderNamaCustomer.setTypeface(typefaceSmall);
		tvHeaderEmailCustomer.setTypeface(typefaceSmall);
		tvHeaderAlamatCustomer.setTypeface(typefaceSmall);
		tvHeaderWilayahCustomer.setTypeface(typefaceSmall);
		tvHeaderTypeCustomer.setTypeface(typefaceSmall);
		tvHeaderGpsCustomer.setTypeface(typefaceSmall);
		tvHeaderImage1Customer.setTypeface(typefaceSmall);
		tvHeaderImage2Customer.setTypeface(typefaceSmall);
		tvHeaderImage3Customer.setTypeface(typefaceSmall);
		tvHeaderTelpCustomer.setTypeface(typefaceSmall);

		mButtonCustomerDetailImage1
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailImage2
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailImage3
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerttd1
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerttd2
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailSave
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		mButtonCustomerDetailCancel
				.setOnClickListener(mDetailCustomerButtonOnClickListener);
		SharedPreferences spPreferences = getSharedPrefereces();
		String main_app_kode_branch = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
		if (main_app_kode_branch != null) {
			showKodeCustomerFromDB();
		} else {
			gotoCustomerProspect();
		}
		checkGPS();
	}

	protected Dialog onCreateDialog(int id) {

		switch (id) {
			case Date_Dialog_ID:
				return new DatePickerDialog(this, onDateSet, cYear, cMonth,
						cDay);
		}
		return null;
	}

	private void updateDateDisplay(int year,int month,int date) {
		String adate;
		month++;
		if (date < 10) {
			adate = "0" + date + "-";
		} else {
			adate = date + "-";
		}
		if (month < 10) {
			adate += "0" + month + "-";
		} else {
			adate += month + "-";
		}

		adate += year;
		etTanggalLahir.setText(adate);
	}
	private DatePickerDialog.OnDateSetListener onDateSet=new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
							  int dayOfMonth) {

			System.out.println("2");
			sYear=year;
			sMonth=monthOfYear;
			sDay=dayOfMonth;
			updateDateDisplay(sYear,sMonth,sDay);
		}
	};

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
			newTTD1 = "TTD1_IMG."+ tvKodeCustomer.getText().toString() +"_"+timeStamp+ ".png";

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

	public class signature1 extends View {
		private static final float STROKE_WIDTH = 5f;
		private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
		private Paint paint = new Paint();
		private Path path = new Path();

		private float lastTouchX;
		private float lastTouchY;
		private final RectF dirtyRect = new RectF();

		public signature1(Context context, AttributeSet attrs) {
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
			newTTD2 = "TTD2_IMG."+ tvKodeCustomer.getText().toString() +"_"+timeStamp+ ".png";

			Log.v("log_tag", "Width: " + v.getWidth());
			Log.v("log_tag", "Height: " + v.getHeight());
			if (bitmap == null) {
				bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
			}
			ttd2.setText(newTTD2);

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
							tvGpsCustomer.setText(String.valueOf(latitude)
									+ "/" + String.valueOf(longitude));

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
								gotoCustomerProspect();
							}
						});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	private final OnClickListener mDetailCustomerButtonOnClickListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			int getId = arg0.getId();
			switch (getId) {
			case R.id.activity_customer_detail_btn_image:
				gotoCaptureImage1();
				break;
			case R.id.activity_customer_detail_btn_image_2:
				gotoCaptureImage2();
				break;
			case R.id.activity_customer_detail_btn_image_3:
				gotoCaptureImage3();
				break;
			case R.id.activity_customer_detail_btn_ttd1:
				gotoTTD1();
				break;
			case R.id.activity_customer_detail_btn_ttd2:
				gotoTTD2();
				break;
			case R.id.activity_customer_detail_btn_cancel:
				gotoCustomerProspect();
				break;
			case R.id.activity_customer_detail_btn_save:
				if (etAlamatCustomer.getText().length() > 0
						//&& etEmailCustomer.getText().length() > 0
						&& etNamaCustomer.getText().length() > 0
						&& etTelpCustomer.getText().length() > 0
						&& etNoKtp.getText().length() > 0
						&& etTanggalLahir.getText().length() > 0
						&& etNama_toko.getText().length() > 0
						//&& etNamaCustomer.getText().length() > 0
						&& etKode_pos.getText().length() > 4
						&& etAlamatCustomer.getText().length() > 0
						&& etNamaPasar.getText().length() > 0
						&& idCluster!=0
						//&& etTelpCustomer.getText().length() > 0
						&& idWilayah != 0
						&& idTypeCustomer!=0
						&& etOmset.getText().length() >0
						&& etCaraPembayaran.getText().length()>0
						&& etPlafonKredit.getText().length()>0
						&& etTermKredit.getText().length()>0) {
					String curLatitude = String.valueOf((int) latitude);
					String curLongitude = String.valueOf((int) longitude);
					if (curLatitude.equalsIgnoreCase("0")
							|| curLongitude.equalsIgnoreCase("0")) {
						String msg = getApplicationContext()
								.getResources()
								.getString(
										R.string.MSG_DLG_LABEL_FAILED_GPS_DIALOG);
						showCustomDialog(msg);
					} else {
						if (tvImage1Customer.getText().length()!=0
								&& tvImage2Customer.getText().length()!=0
								&& ttd1.getText().length()>0
								&& ttd2.getText().length()>0) {
							SharedPreferences spPreferences = getSharedPrefereces();
							String idDepo = spPreferences.getString(
									CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH,
									null);
							String idStaff = spPreferences.getString(
									CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
									null);
							String timeStamp = new SimpleDateFormat("HHmmss",
									Locale.getDefault()).format(new Date());
							int countData = Integer.parseInt(timeStamp) + 1;
							final String date = "yyyy-MM-dd";
							Calendar calendar = Calendar.getInstance();
							SimpleDateFormat dateFormat = new SimpleDateFormat(
									date);
							final String checkDate = dateFormat.format(calendar
									.getTime());

							Customer newCustomer = new Customer();
							newCustomer.setId_customer(countData);
							newCustomer.setAlamat(etAlamatCustomer.getText()
									.toString());
							newCustomer.setBlokir("Y");
							newCustomer.setDate(checkDate);
							newCustomer.setEmail(etEmailCustomer.getText()
									.toString());
							newCustomer.setFoto_1(newImageName1);
							newCustomer.setFoto_2(newImageName2);
							newCustomer.setFoto_3(newImageName3);
							newCustomer.setId_type_customer(idTypeCustomer);
							newCustomer.setId_wilayah(idWilayah);
							newCustomer.setKode_customer(tvKodeCustomer
									.getText().toString());
							newCustomer.setLats(String.valueOf(latitude));
							newCustomer.setLongs(String.valueOf(longitude));
							newCustomer.setNama_lengkap(etNamaCustomer
									.getText().toString());
							newCustomer.setNo_telp(etTelpCustomer.getText()
									.toString());
							newCustomer.setStatus_update("2");
							newCustomer.setId_staff(Integer.parseInt(idStaff));
							newCustomer.setNo_ktp(etNoKtp.getText().toString());
							newCustomer.setTanggal_lahir(etTanggalLahir.getText().toString());
							newCustomer.setNama_bank(etNamaBank.getText().toString());
                            newCustomer.setNo_rekening(etNoRekening.getText().toString());
                            newCustomer.setAtas_nama(etAtasNama.getText().toString());
                            newCustomer.setNpwp(etNPWP.getText().toString());
                            newCustomer.setNama_pasar(etNamaPasar.getText().toString());
                            newCustomer.setId_cluster(idCluster);
                            newCustomer.setTelp(etTelp.getText().toString());
                            newCustomer.setFax(etFax.getText().toString());
                            newCustomer.setOmset(etOmset.getText().toString());
                            newCustomer.setCara_pembayaran(etCaraPembayaran.getText().toString());
                            newCustomer.setPlafon_kredit(etPlafonKredit.getText().toString());
                            newCustomer.setTerm_kredit(etTermKredit.getText().toString());
                            newCustomer.setId_depo(Integer.parseInt(idDepo));
							newCustomer.setNama_istri(etNama_istri.getText().toString());
							newCustomer.setNama_anak1(etNama_anak1.getText().toString());
							newCustomer.setNama_anak2(etNama_anak2.getText().toString());
							newCustomer.setNama_anak3(etNama_anak3.getText().toString());
							newCustomer.setKode_pos(etKode_pos.getText().toString());
							newCustomer.setNama_toko(etNama_toko.getText().toString());
                            newCustomer.setIsactive("N");
                            newCustomer.setDescription("Belum Aktif");
							newCustomer.setTtd1(ttd1.getText().toString());
							newCustomer.setTtd2(ttd2.getText().toString());

							databaseHandler.add_Customer(newCustomer);
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_customer_prospect_save_success);
							showCustomDialogSaveSuccess(msg);
						} else {
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_customer_prospect_save_failed_no_image);
							showCustomDialog(msg);
						}
					}
				} else {
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_customer_prospect_save_failed_empty_box);
					showCustomDialog(msg);
				}
				break;
			default:
				break;
			}
		}

	};

	public void gotoCaptureImage1() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri1 = getOutputMediaFileUri1(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	public void gotoCaptureImage2() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri2 = getOutputMediaFileUri2(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri2);
		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	public void gotoCaptureImage3() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri3 = getOutputMediaFileUri3(MEDIA_TYPE_IMAGE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri3);
		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	public void gotoTTD1() {
		dialog = new Dialog(AddCustomerProspectActivity.this);
		// Removing the features of Normal Dialogs
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_signature);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

		mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
		mSignature = new signature(getApplicationContext(), null);
		mSignature.setBackgroundColor(Color.WHITE);
		// Dynamically generating Layout through java code
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
				/*
				file = new File(DIRECTORY);
				if (!file.exists()) {
					file.mkdir();
				}

				String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/Android/data/com.mahkota_company.android/"+"customer_prospect/";
				String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
				String pic_name1 = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date())+1;
				String StoredPath = DIRECTORY + pic_name + ".png";
				String StoredPath1 = DIRECTORY + pic_name1 + ".png";
				*/

				File dir = new File(CONFIG.getFolderPath() + "/"+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
				if (!dir.exists()) {
					dir.mkdirs();
				}

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
				File mediaFile;
				mediaFile = new File(dir.getPath() + File.separator +"TTD1_IMG."
                        + tvKodeCustomer.getText().toString() +"_"+timeStamp+ ".png");

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
		/*String kode_customer;
		kode_customer = tvKodeCustomer.getText().toString();
		Intent intentActivity = new Intent(
				AddCustomerProspectActivity.this,
				Useless_AndroidCanvas1.class);
		intentActivity.putExtra(KODE_CUSTOMER, kode_customer);
		startActivity(intentActivity);
		*/
	}

	public void gotoTTD2() {
		dialog = new Dialog(AddCustomerProspectActivity.this);
		// Removing the features of Normal Dialogs
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_signature1);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(false);

		mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
		mSignature1 = new signature1(getApplicationContext(), null);
		mSignature1.setBackgroundColor(Color.WHITE);
		// Dynamically generating Layout through java code
		mContent.addView(mSignature1, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		mClear = (Button) dialog.findViewById(R.id.clear);
		mGetSign = (Button) dialog.findViewById(R.id.getsign);
		mGetSign.setEnabled(false);
		mCancel = (Button) dialog.findViewById(R.id.cancel);
		view = mContent;

		mClear.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.v("log_tag", "Panel Cleared");
				mSignature1.clear();
				mGetSign.setEnabled(false);
			}
		});

		mGetSign.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				File dir = new File(CONFIG.getFolderPath() + "/"+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
				if (!dir.exists()) {
					dir.mkdirs();
				}

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
				File mediaFile;
				mediaFile = new File(dir.getPath() + File.separator +"TTD2_IMG."
						+ tvKodeCustomer.getText().toString() +"_"+timeStamp+ ".png");

				Log.v("log_tag", "Panel Saved");
				view.setDrawingCacheEnabled(true);
				mSignature1.save(view, mediaFile);
				//ttd1.setText(newTTD1);
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

		/*String kode_customer;
		kode_customer = tvKodeCustomer.getText().toString();
		Intent intentActivity = new Intent(
				AddCustomerProspectActivity.this,
				AndroidCanvas2.class);
		intentActivity.putExtra(KODE_CUSTOMER, kode_customer);
		startActivity(intentActivity);
		*/
	}

	/*
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri1(int type) {
		return Uri.fromFile(getOutputMediaFile1(type));
	}

	public Uri getOutputMediaFileUri2(int type) {
		return Uri.fromFile(getOutputMediaFile2(type));
	}

	public Uri getOutputMediaFileUri3(int type) {
		return Uri.fromFile(getOutputMediaFile3(type));
	}

	private File getOutputMediaFile1(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		SharedPreferences spPreferences = getSharedPrefereces();
		String kodeBranch = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator
					+ tvKodeCustomer.getText().toString() + kodeBranch + "_"
					+ "IMG_" + timeStamp + ".png");
			newImageName1 = tvKodeCustomer.getText().toString() + kodeBranch
					+ "_" + "IMG_" + timeStamp + ".png";
		} else {
			return null;
		}
		return mediaFile;
	}

	private File getOutputMediaFile2(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		SharedPreferences spPreferences = getSharedPrefereces();
		String kodeBranch = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator
					+ tvKodeCustomer.getText().toString() + kodeBranch + "_"
					+ "IMG_" + timeStamp + ".png");
			newImageName2 = tvKodeCustomer.getText().toString() + kodeBranch
					+ "_" + "IMG_" + timeStamp + ".png";
		} else {
			return null;
		}
		return mediaFile;
	}

	private File getOutputMediaFile3(int type) {
		File dir = new File(CONFIG.getFolderPath() + "/"
				+ CONFIG.CONFIG_APP_FOLDER_CUSTOMER_PROSPECT);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		SharedPreferences spPreferences = getSharedPrefereces();
		String kodeBranch = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(dir.getPath() + File.separator
					+ tvKodeCustomer.getText().toString() + kodeBranch + "_"
					+ "IMG_" + timeStamp + ".png");
			newImageName3 = tvKodeCustomer.getText().toString() + kodeBranch
					+ "_" + "IMG_" + timeStamp + ".png";
		} else {
			return null;
		}
		return mediaFile;
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		checkGPS();
		// if the result is capturing Image
		if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Log.d(LOG_TAG, "take image success");
				if (newImageName1 != null)
					tvImage1Customer.setText(newImageName1);
				if (newImageName2 != null)
					tvImage2Customer.setText(newImageName2);
				if (newImageName3 != null)
					tvImage3Customer.setText(newImageName3);
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

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	public void showKodeCustomerFromDB() {

		final String date = "yyyy-MM-dd";
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat dateFormat = new SimpleDateFormat(date);
		final String checkDate = dateFormat.format(calendar.getTime());

		final String timeUnique = "yyyyMMdd";
		Calendar calendarDate = Calendar.getInstance();
		SimpleDateFormat dateFormatDate = new SimpleDateFormat(timeUnique);
		final String dateUnique = dateFormatDate.format(calendarDate.getTime());

		Calendar now = Calendar.getInstance();
		int hrs = now.get(Calendar.HOUR_OF_DAY);
		int min = now.get(Calendar.MINUTE);
		int sec = now.get(Calendar.SECOND);
		final String time = zero(hrs) + zero(min) + zero(sec);
		int countData = databaseHandler.getCountCustomerProspect(checkDate);
		countData = countData + 1;
		String datetime = dateUnique;

		SharedPreferences spPreferences = getSharedPrefereces();
		String kodeBranch = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
		String idWilayah = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_ID_WILAYAH, null);
		Branch branch = databaseHandler.getBranch(Integer.parseInt(kodeBranch));
		String headerKodeCustomer = CONFIG.CONFIG_APP_KODE_CUSTOMER_HEADER
				+ branch.getKode_branch() + "." + datetime + time + countData;
		tvKodeCustomer.setText(headerKodeCustomer);
		//Wilayah wilayah = databaseHandler.getWilayah(Integer
				//.parseInt(idWilayah));
		//tvWilayahCustomer.setText(wilayah.getNama_wilayah());

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

	public void gotoCustomerProspect() {
		Intent i = new Intent(this, CustomerProspectActivity.class);
		startActivity(i);
		finish();
	}
}
