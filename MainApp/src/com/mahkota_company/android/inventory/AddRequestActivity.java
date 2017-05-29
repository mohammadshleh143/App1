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
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mahkota_company.android.R;
import com.mahkota_company.android.database.Branch;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.DetailReqLoad;
import com.mahkota_company.android.database.DetailSalesOrder;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Product;
import com.mahkota_company.android.database.ReqLoad;
import com.mahkota_company.android.database.SalesOrder;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.GlobalApp;
import com.mahkota_company.android.utils.SpinnerAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddRequestActivity extends FragmentActivity {
	private Context act;
	private ImageView menuBackButton;
	private DatabaseHandler databaseHandler;
	private Typeface typefaceSmall;
	private Button mButtonAddProduct;
	private Button mButtonSave;
	private Button mButtonCancel;
	private Button mButtonTTD;
	private Handler handler = new Handler();
	private static final String LOG_TAG = RequestActivity.class
			.getSimpleName();
	private ProgressDialog progressDialog;
	public ArrayList<Customer> customerList;
	public ArrayList<String> customerStringList;
	private String kodeCustomer;
	private Spinner spinnerPromosi;
	public ArrayList<String> promosiStringList;
	private int idPromosi = -1;
	private String response_data;
	private ListViewChooseAdapter cAdapterChooseAdapter;
	public ArrayList<DetailReqLoad> detailReqLoadList = new ArrayList<DetailReqLoad>();
	private ListView listview;
	private ListViewAdapter cAdapter;
	private Jadwal jadwal;
	private String status_update;
	private TextView ttd1;
	Dialog dialog;
	LinearLayout mContent;
	View view;
	signature mSignature;
	Bitmap bitmap;
	private String newTTD1;
	Button btn_get_sign, mClear, mGetSign, mCancel;
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
	private Uri fileUri; // file url to store image/video
	private final int MEDIA_TYPE_IMAGE = 1;
	private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	private String IMAGE_DIRECTORY_NAME = "Reqload";
	private String id_Image = "";
	private int counterFoto = 0;
	private String main_app_id_staff;
	private String username;
	private String nama;
	private String alamat;
	private String keterangan;
	private String newImageName;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_request);
		act = this;
		databaseHandler = new DatabaseHandler(this);
		menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
		menuBackButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoInventory();
			}
		});
		mButtonCancel = (Button) findViewById(R.id.activity_sales_order_btn_cancel);
		mButtonCancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				gotoInventory();
			}
		});
		typefaceSmall = Typeface.createFromAsset(getAssets(),
				"fonts/AliquamREG.ttf");
		listview = (ListView) findViewById(R.id.list);
		listview.setItemsCanFocus(false);
		spinnerPromosi = (Spinner) findViewById(R.id.activity_sales_order_promosi_value);
		mButtonAddProduct = (Button) findViewById(R.id.activity_sales_order_btn_add_product);
		mButtonSave = (Button) findViewById(R.id.activity_sales_order_btn_save);
		mButtonTTD = (Button) findViewById(R.id.ttd_customer);
		ttd1 = (TextView) findViewById(R.id.activity_kanvaser_detail_ttd1);

		customerList = new ArrayList<Customer>();
		customerStringList = new ArrayList<String>();
		SharedPreferences spPreferences = getSharedPrefereces();
		username = spPreferences.getString(
				CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);

		promosiStringList = new ArrayList<String>();
		promosiStringList.add("Tidak Ada");

		mButtonSave.setOnClickListener(maddSalesOrderButtonOnClickListener);
		mButtonTTD.setOnClickListener(maddSalesOrderButtonOnClickListener);
		mButtonAddProduct.setOnClickListener(maddSalesOrderButtonOnClickListener);
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

	private final OnClickListener maddSalesOrderButtonOnClickListener = new OnClickListener() {
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
			case R.id.ttd_customer:
				String msg1 = getApplicationContext().getResources().getString(R.string.app_supplier_already_take_ttd);
				if(ttd1.getText().length() > 0){
					showCustomDialogReplaceOldttd(msg1);
				}else{
					BukaTTD();
				}
				break;
			case R.id.activity_sales_order_btn_save:
				if (detailReqLoadList.isEmpty()){
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_reqload_product_empty_box);
					showCustomDialog(msg);
				}else if(ttd1.getText().length()>1){
					String timeStamp = new SimpleDateFormat("HHmmss",
							Locale.getDefault()).format(new Date());

					final String date = "yyyyMMdd";
					Calendar calendar = Calendar.getInstance();
					SimpleDateFormat dateFormat = new SimpleDateFormat(date);
					final String dateOutput = dateFormat.format(calendar
							.getTime());

					int countData = databaseHandler.getCountReqLoad();
					countData = countData + 1;
					String datetime = dateOutput ;

					SharedPreferences spPreferences = getSharedPrefereces();
					String kodeBranch = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_STAFF_KODE_BRANCH, null);
					String username = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_STAFF_USERNAME, null);
					String main_app_id_staff = spPreferences.getString(
							CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF, null);
					Branch branch = databaseHandler.getBranch(Integer
							.parseInt(kodeBranch));
					String nomerRequestLoad = CONFIG.CONFIG_APP_KODE_RL_HEADER
							+ branch.getKode_branch()+"."+ username + "." + datetime;

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

					ArrayList<ReqLoad> tempReq_load_list = databaseHandler
							.getAllReqLoad();
					int tempIndex = 0;
					for (ReqLoad reqLoad : tempReq_load_list) {
						tempIndex = reqLoad.getId_sales_order();
					}
					int index = 1;
					for (DetailReqLoad detailReqLoad : detailReqLoadList) {
						ReqLoad reqLoad = new ReqLoad();
						reqLoad.setId_sales_order(tempIndex + index);
						reqLoad.setDate_order(checkDate);
						//reqLoad.setHarga_jual(detailReqLoad.getHarga_jual());

						reqLoad.setJumlah_order(detailReqLoad.getJumlah_order());
						reqLoad.setJumlah_order1(detailReqLoad.getJumlah_order1());
						reqLoad.setJumlah_order2(detailReqLoad.getJumlah_order2());
						reqLoad.setJumlah_order3(detailReqLoad.getJumlah_order3());

						reqLoad.setNama_product(detailReqLoad.getNama_product());
						reqLoad.setId_product(detailReqLoad.getId_product());
						double st1 = Double.parseDouble(detailReqLoad.getJumlah_order());
						double st2 = Double.parseDouble(detailReqLoad.getJumlah_order1());
						double st3 = Double.parseDouble(detailReqLoad.getJumlah_order2());
						double st4 = Double.parseDouble(detailReqLoad.getJumlah_order3());

						String uom1= detailReqLoad.getUomqtyl1();
						String uom2= detailReqLoad.getUomqtyl2();
						String uom3= detailReqLoad.getUomqtyl3();
						String uom4= detailReqLoad.getUomqtyl4();

						double uomqtyl1 = Double.parseDouble(uom1);
						double uomqtyl2 = Double.parseDouble(uom2);
						double uomqtyl3 = Double.parseDouble(uom3);
						double uomqtyl4 = Double.parseDouble(uom4);

						Double jumlah =
								st1*uomqtyl4 +
										st2*uomqtyl3*uomqtyl4+
										st3*uomqtyl2*uomqtyl3*uomqtyl4+
										st4*uomqtyl1*uomqtyl2*uomqtyl3*uomqtyl4;

						reqLoad.setSatuan_terkecil(jumlah.toString());
						reqLoad.setNomer_request_load(nomerRequestLoad);
						reqLoad.setTime_order(time);
						reqLoad.setUsername(username);
						reqLoad.setId_staff(Integer.parseInt(main_app_id_staff));
						reqLoad.setTtd1(ttd1.getText().toString());
						databaseHandler.add_ReqLoad(reqLoad);
						index += 1;
					}
					String msg = getApplicationContext().getResources()
							.getString(R.string.app_req_load_save_success);
					showCustomDialogSaveSuccess(msg);

					break;
				}else {
					String msg = getApplicationContext()
							.getResources()
							.getString(
									R.string.app_reqload_ttd_empty_box);
					showCustomDialog(msg);
				}
			default:
				break;
			}
		}

	};

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
								BukaTTD();

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

	private void BukaTTD() {
		/*Intent intentActivity = new Intent(
				AddRequestActivity.this,
				AndroidCanvasExample.class);
		startActivity(intentActivity);
		*/

		dialog = new Dialog(AddRequestActivity.this);
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

				String timeStamp = new SimpleDateFormat("yyyyMMdd",
						Locale.getDefault()).format(new Date());

				File mediaFile;
				mediaFile = new File(dir.getPath() + File.separator +"TTD1_REQ_VAN."
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
			String timeStamp = new SimpleDateFormat("yyyyMMdd",
					Locale.getDefault()).format(new Date());
			newTTD1 = "TTD1_REQ_VAN."+ username +"_"+timeStamp+ ".png";

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

	private void ChooseProductDialog() {
		final Dialog chooseProductDialog = new Dialog(act);
		chooseProductDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		chooseProductDialog
				.setContentView(R.layout.activity_main_product_choose_dialog_req_load);
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
		searchProduct.setFocusable(true);
		searchProduct.setFocusableInTouchMode(true);
		searchProduct.requestFocus();
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(searchProduct, InputMethodManager.SHOW_IMPLICIT);


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
				String uomqtyl1 = product_from_db.get(i).getUomqtyl1();
				String uomqtyl2 = product_from_db.get(i).getUomqtyl2();
				String uomqtyl3 = product_from_db.get(i).getUomqtyl3();
				String uomqtyl4 = product_from_db.get(i).getUomqtyl4();

				Product product = new Product();
				product.setId_product(id_product);
				product.setNama_product(nama_product);
				product.setKode_product(kode_product);
				product.setHarga_jual(harga_jual);
				product.setStock(stock);
				product.setId_kemasan(id_kemasan);
				product.setFoto(foto);
				product.setDeskripsi(deskripsi);
				product.setUomqtyl1(uomqtyl1);
				product.setUomqtyl2(uomqtyl2);
				product.setUomqtyl3(uomqtyl3);
				product.setUomqtyl4(uomqtyl4);
				product_list.add(product);
				cAdapterChooseAdapter = new ListViewChooseAdapter(
						AddRequestActivity.this,
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
							String uomqtyl1 = product_from_db.get(i).getUomqtyl1();
							String uomqtyl2 = product_from_db.get(i).getUomqtyl2();
							String uomqtyl3 = product_from_db.get(i).getUomqtyl3();
							String uomqtyl4 = product_from_db.get(i).getUomqtyl4();

							Product product = new Product();
							product.setId_product(id_product);
							product.setNama_product(nama_product);
							product.setKode_product(kode_product);
							product.setHarga_jual(harga_jual);
							product.setStock(stock);
							product.setId_kemasan(id_kemasan);
							product.setFoto(foto);
							product.setDeskripsi(deskripsi);
							product.setUomqtyl1(uomqtyl1);
							product.setUomqtyl2(uomqtyl2);
							product.setUomqtyl3(uomqtyl3);
							product.setUomqtyl4(uomqtyl4);
							product_list.add(product);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									AddRequestActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct,jumlahProduct1, jumlahProduct2,jumlahProduct3, product_list,
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
							String uomqtyl1 = product_from_db.get(i).getUomqtyl1();
							String uomqtyl2 = product_from_db.get(i).getUomqtyl2();
							String uomqtyl3 = product_from_db.get(i).getUomqtyl3();
							String uomqtyl4 = product_from_db.get(i).getUomqtyl4();

							Product product = new Product();
							product.setId_product(id_product);
							product.setNama_product(nama_product);
							product.setKode_product(kode_product);
							product.setHarga_jual(harga_jual);
							product.setStock(stock);
							product.setId_kemasan(id_kemasan);
							product.setFoto(foto);
							product.setDeskripsi(deskripsi);
							product.setUomqtyl1(uomqtyl1);
							product.setUomqtyl2(uomqtyl2);
							product.setUomqtyl3(uomqtyl3);
							product.setUomqtyl4(uomqtyl4);
							product_list.add(product);
							cAdapterChooseAdapter = new ListViewChooseAdapter(
									AddRequestActivity.this,
									R.layout.list_item_product_sales_order,
									jumlahProduct, jumlahProduct1, jumlahProduct2, jumlahProduct3, product_list,
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

	private void updateListViewDetailOrder(DetailReqLoad detailReqLoad) {
		detailReqLoadList.add(detailReqLoad);
		cAdapter = new ListViewAdapter(AddRequestActivity.this,
				R.layout.list_item_detail_sales_order, detailReqLoadList);
		listview.setAdapter(cAdapter);
		cAdapter.notifyDataSetChanged();
	}

	public class ListViewAdapter extends ArrayAdapter<DetailReqLoad> {
		Activity activity;
		int layoutResourceId;
		DetailReqLoad productData;
		ArrayList<DetailReqLoad> data = new ArrayList<DetailReqLoad>();

		public ListViewAdapter(Activity act, int layoutResourceId,
				ArrayList<DetailReqLoad> data) {
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
			holder.list_harga_jual.setTypeface(typefaceSmall);

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
				int layoutResourceId, EditText jumlahProduct, EditText jumlahProduct1, EditText jumlahProduct2,EditText jumlahProduct3,
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
					+ data.get(position).getFoto());
			holder.list_img.setImageBitmap(BitmapFactory.decodeFile(dir.getAbsolutePath()));
			//holder.list_kodeProduct.setText(productData.getKode_product());
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

						if (jumlahProduct.getText().length()==0&&jumlahProduct1.getText().length()==0&&
							jumlahProduct2.getText().length()==0&&jumlahProduct3.getText().length()==0) {

							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_jumlah);
							showCustomDialog(msg);
						} else if(jumlahProduct.getText().length()==0||jumlahProduct1.getText().length()==0||
								jumlahProduct2.getText().length()==0||jumlahProduct3.getText().length()==0){
							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_0);
							showCustomDialog(msg);
						}else if(jumlahProduct.getText().toString().equals("0")&&jumlahProduct1.getText().toString().equals("0")&&
							jumlahProduct2.getText().toString().equals("0")&&jumlahProduct3.getText().toString().equals("0")){

							String msg = getApplicationContext()
									.getResources()
									.getString(
											R.string.app_sales_order_failed_please_add_jumlah);
							showCustomDialog(msg);
						}else{
							boolean containSameProduct = false;
							for (DetailReqLoad detailReqLoad : detailReqLoadList) {
								if (detailReqLoad.getKode_product()
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
								int count = detailReqLoadList.size() + 1;
								updateListViewDetailOrder(new DetailReqLoad(
										count,
										data.get(position).getNama_product(),
										data.get(position).getKode_product(),
										data.get(position).getHarga_jual(),
										jumlahProduct.getText().toString(),
										jumlahProduct1.getText().toString(),
										jumlahProduct2.getText().toString(),
										jumlahProduct3.getText().toString(),
										data.get(position).getUomqtyl1(),
										data.get(position).getUomqtyl2(),
										data.get(position).getUomqtyl3(),
										data.get(position).getUomqtyl4(),
										data.get(position).getId_product()
								));
								/*
								if (jumlahProduct.getText().toString().length()!=0 && jumlahProduct1.getText().toString().length()!=0&&
										jumlahProduct2.getText().toString().length()!=0&&jumlahProduct3.getText().toString().length()!=0)
									updateListViewDetailOrder(new DetailReqLoad(
											count,
											data.get(position).getNama_product(),
											data.get(position).getKode_product(),
											data.get(position).getHarga_jual(),
											jumlahProduct.getText().toString(),
											jumlahProduct1.getText().toString(),
											jumlahProduct2.getText().toString(),
											jumlahProduct3.getText().toString(),
											data.get(position).getUomqtyl1(),
											data.get(position).getUomqtyl2(),
											data.get(position).getUomqtyl3(),
											data.get(position).getUomqtyl4(),
											data.get(position).getId_product()
									));
								else if(jumlahProduct1.getText().toString().length()!=0&&
										jumlahProduct2.getText().toString().length()!=0&&jumlahProduct3.getText().toString().length()!=0){
									updateListViewDetailOrder(new DetailReqLoad(
											count,
											data.get(position).getNama_product(),
											data.get(position).getKode_product(),
											data.get(position).getHarga_jual(),
											"0",
											jumlahProduct1.getText().toString(),
											jumlahProduct2.getText().toString(),
											jumlahProduct3.getText().toString(),
											data.get(position).getUomqtyl1(),
											data.get(position).getUomqtyl2(),
											data.get(position).getUomqtyl3(),
											data.get(position).getUomqtyl4(),
											data.get(position).getId_product()
									));
								}else if (jumlahProduct2.getText().toString().length()!=0&&jumlahProduct3.getText().toString().length()!=0){
									updateListViewDetailOrder(new DetailReqLoad(
											count,
											data.get(position).getNama_product(),
											data.get(position).getKode_product(),
											data.get(position).getHarga_jual(),
											"0",
											"0",
											jumlahProduct2.getText().toString(),
											jumlahProduct3.getText().toString(),
											data.get(position).getUomqtyl1(),
											data.get(position).getUomqtyl2(),
											data.get(position).getUomqtyl3(),
											data.get(position).getUomqtyl4(),
											data.get(position).getId_product()
									));
								}else if (jumlahProduct3.getText().toString().length()!=0){
									updateListViewDetailOrder(new DetailReqLoad(
											count,
											data.get(position).getNama_product(),
											data.get(position).getKode_product(),
											data.get(position).getHarga_jual(),
											"0",
											"0",
											"0",
											jumlahProduct3.getText().toString(),
											data.get(position).getUomqtyl1(),
											data.get(position).getUomqtyl2(),
											data.get(position).getUomqtyl3(),
											data.get(position).getUomqtyl4(),
											data.get(position).getId_product()
									));
								}else if(jumlahProduct.getText().toString().length()!=0 && jumlahProduct1.getText().toString().length()!=0&&
										jumlahProduct2.getText().toString().length()!=0){
									updateListViewDetailOrder(new DetailReqLoad(
											count,
											data.get(position).getNama_product(),
											data.get(position).getKode_product(),
											data.get(position).getHarga_jual(),
											jumlahProduct.getText().toString(),
											jumlahProduct1.getText().toString(),
											jumlahProduct2.getText().toString(),
											"0",
											data.get(position).getUomqtyl1(),
											data.get(position).getUomqtyl2(),
											data.get(position).getUomqtyl3(),
											data.get(position).getUomqtyl4(),
											data.get(position).getId_product()
									));
								}*/
								chooseProductDialog.hide();
							}
						}

				}
			});
			return row;

		}

		class UserHolder {
			ImageView list_img;
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

	public void gotoInventory() {
		Intent i = new Intent(this, RequestActivity.class);
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
