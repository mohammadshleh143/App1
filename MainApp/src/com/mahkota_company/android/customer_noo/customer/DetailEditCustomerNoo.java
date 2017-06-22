package com.mahkota_company.android.customer_noo.customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.mahkota_company.android.R;
import com.mahkota_company.android.customer.CustomerActivity;
import com.mahkota_company.android.customer.CustomerLocatorActivity;
import com.mahkota_company.android.customer.MainActivity;
import com.mahkota_company.android.database.Cluster;
import com.mahkota_company.android.database.Customer;
import com.mahkota_company.android.database.DatabaseHandler;
import com.mahkota_company.android.database.Jadwal;
import com.mahkota_company.android.database.Staff;
import com.mahkota_company.android.database.TypeCustomer;
import com.mahkota_company.android.database.Wilayah;
import com.mahkota_company.android.display_product.DisplayProductActivity;
import com.mahkota_company.android.retur.ReturActivity;
import com.mahkota_company.android.sales_order.SalesOrderActivity;
import com.mahkota_company.android.stock_on_hand.StockOnHandActivity;
import com.mahkota_company.android.utils.CONFIG;
import com.mahkota_company.android.utils.SpinnerAdapter;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class DetailEditCustomerNoo extends FragmentActivity {
    private Context act;
    private ImageView menuBackButton;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CANVAS_REQ =100;
    private double tempCheckInLatitude;
    private double tempCheckInLongitude;
    private ArrayList<Customer> customer_list = new ArrayList<Customer>();
    private String newImageName1;
    private String newImageName2;
    private String newImageName3;
    private Spinner spinnerCluster;
    private ArrayList<Cluster> clusterList;
    private ArrayList<String> clusterStringList;
    private int idCluster = 0;
    private DatabaseHandler databaseHandler;
    private ProgressDialog progressDialog;
    private Typeface typefaceSmall;
    private TextView tvKodeCustomer;
    private TextView tvId_staff;
    private EditText etNamaCustomer;
    private EditText etEmailCustomer;
    private EditText etAlamatCustomer;
    private TextView tvWilayahCustomer;
    private Spinner spinnerTypeCustomer;
    private ArrayList<TypeCustomer> typeCustomerList;
    private ArrayList<String> typeCustomerStringList;
    private int idTypeCustomer = 0;
    private TextView tvGpsCustomer;
    private TextView tvImage1Customer;
    private TextView tvImage2Customer;
    private TextView tvImage3Customer;
    private EditText etTelpCustomer;
    private EditText etno_ktp;
    private EditText etTanggal_lahir;
    private EditText etNama_bank;
    private EditText etNo_rekenig;
    private EditText etAtas_nama;
    private EditText etNpwp;
    private EditText etNama_pasar;
    private EditText etCluster;
    private EditText etTelp;
    private EditText etFax;
    private EditText etOmset;
    private EditText etCara_pembayaran;
    private EditText etPlafon_kredit;
    private EditText etTerm_kredit;
    private EditText etNama_istri;
    private EditText etNama_anak1;
    private EditText etNama_anak2;
    private EditText etNama_anak3;
    private EditText etKode_pos;
    private ToggleButton tgStatus;
    private EditText etDescription;
    private EditText approval;
    private TextView etNama_toko;
    private Customer customer;
    private Customer customer1;
    private Staff staff;
    private TextView tvstatus;
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
    private Button mButtonCustomerDetailMap;
    private Button mButtonCustomerDetailImage1;
    private Button mButtonCustomerDetailImage2;
    private Button mButtonCustomerDetailImage3;
    private Button mButtonCustomerDetailPreview;
    private Button mButtonCustomerDetailSave;
    private CheckBox a1,a2,a3,a4,a5,a6,a7,a8,a9,a10,a11,a12,a13,a14,a15,a16,a17,a18,a19,a20,a21,a22,a23,a24,a25;
    private LocationManager locationManager;
    private static final String LOG_TAG = DetailEditCustomerNoo.class
            .getSimpleName();
    private Uri fileUri1, fileUri2, fileUri3;
    private double latitude; // latitude
    private double longitude; // longitude
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute
    private Location location; // location
    private String tempLatitude;
    private String tempLongitude;
    private Jadwal jadwal;
    final int Date_Dialog_ID=0;
    int cDay,cMonth,cYear; // this is the instances of the current date
    Calendar cDate;
    int sDay,sMonth,sYear;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer_edit_noo);
        act = this;
        databaseHandler = new DatabaseHandler(this);
        menuBackButton = (ImageView) findViewById(R.id.menuBackButton);
        menuBackButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                gotoCustomer();
            }
        });
        typefaceSmall = Typeface.createFromAsset(getAssets(),
                "fonts/AliquamREG.ttf");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getApplicationContext().getResources()
                .getString(R.string.app_name));
        progressDialog.setMessage(getApplicationContext().getResources()
                .getString(R.string.app_promosi_processing));
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        tvKodeCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_kode_customer);
        etNamaCustomer = (EditText) findViewById(R.id.activity_customer_prospect_nama_customer_value);
        etEmailCustomer = (EditText) findViewById(R.id.activity_customer_prospect_email_customer_value);
        etAlamatCustomer = (EditText) findViewById(R.id.activity_customer_prospect_alamat_customer_value);
        tvWilayahCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_wilayah_customer);
        spinnerTypeCustomer = (Spinner) findViewById(R.id.activity_customer_prospect_type_customer_value);
        tvGpsCustomer = (TextView) findViewById(R.id.activity_customer_detail_value_gps_location);
        tvImage1Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image);
        tvImage2Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_2);
        tvImage3Customer = (TextView) findViewById(R.id.activity_customer_detail_value_image_3);
        etTelpCustomer = (EditText) findViewById(R.id.activity_customer_prospect_telp_customer_value);
        etno_ktp = (EditText) findViewById(R.id.activity_customer_prospect_no_ktp_value);
        etTanggal_lahir = (EditText) findViewById(R.id.activity_customer_prospect_tanggal_lahir_value);
        etNama_bank = (EditText) findViewById(R.id.activity_customer_prospect_nama_bank_value);
        etNo_rekenig = (EditText) findViewById(R.id.activity_customer_prospect_no_rekening_value);
        etAtas_nama = (EditText) findViewById(R.id.activity_customer_prospect_atas_nama_value);
        etNpwp = (EditText) findViewById(R.id.activity_customer_prospect_npwp_value);

        etNama_pasar = (EditText) findViewById(R.id.activity_customer_prospect_nama_pasar_value);
        spinnerCluster = (Spinner) findViewById(R.id.activity_customer_prospect_cluster_value);
        //etCluster = (EditText) findViewById(R.id.activity_customer_prospect_cluster_value);
        etTelp = (EditText) findViewById(R.id.activity_customer_prospect_telp_value);
        etFax = (EditText) findViewById(R.id.activity_customer_prospect_fax_value);
        etOmset = (EditText) findViewById(R.id.activity_customer_prospect_omset_value);
        etCara_pembayaran = (EditText) findViewById(R.id.activity_customer_prospect_cara_pembayaran_value);
        etPlafon_kredit = (EditText) findViewById(R.id.activity_customer_prospect_plafon_value);
        etTerm_kredit = (EditText) findViewById(R.id.activity_customer_prospect_term_value);

        etNama_istri = (EditText) findViewById(R.id.activity_customer_prospect_nama_istri_value);
        etNama_anak1 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak1_value);
        etNama_anak2 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak2_value);
        etNama_anak3 = (EditText) findViewById(R.id.activity_customer_prospect_nama_anak3_value);
        etKode_pos = (EditText) findViewById(R.id.activity_customer_prospect_kode_pos_value);
        tgStatus = (ToggleButton) findViewById(R.id.activity_customer_detail_status_value);
        etDescription = (EditText) findViewById(R.id.activity_customer_prospect_description_value);
        approval=(EditText) findViewById(R.id.activity_customer_prospect_validation);
        etNama_toko = (TextView) findViewById(R.id.activity_customer_prospect_nama_toko_value);

        tvstatus = (TextView) findViewById(R.id.activity_customer_detail_status);
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

        mButtonCustomerDetailMap = (Button) findViewById(R.id.activity_customer_detail_btn_map);
        mButtonCustomerDetailImage1 = (Button) findViewById(R.id.activity_customer_detail_btn_image);
        mButtonCustomerDetailImage2 = (Button) findViewById(R.id.activity_customer_detail_btn_image_2);
        mButtonCustomerDetailImage3 = (Button) findViewById(R.id.activity_customer_detail_btn_image_3);
        mButtonCustomerDetailPreview = (Button) findViewById(R.id.activity_customer_detail_btn_preview);
        mButtonCustomerDetailSave = (Button) findViewById(R.id.activity_customer_detail_btn_save);

        a1= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_customer_value2);
        a2= (CheckBox) findViewById(R.id.activity_customer_prospect_email_customer_value2);
        a3= (CheckBox) findViewById(R.id.activity_customer_prospect_no_ktp_value2);
        a4= (CheckBox) findViewById(R.id.activity_customer_prospect_tanggal_lahir_value2);
        a5= (CheckBox) findViewById(R.id.activity_customer_prospect_kode_pos_value2);
        a6= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_bank_value2);
        a7= (CheckBox) findViewById(R.id.activity_customer_prospect_no_rekening_value2);
        a8= (CheckBox) findViewById(R.id.activity_customer_prospect_atas_nama_value2);
        a9= (CheckBox) findViewById(R.id.activity_customer_prospect_npwp_value2);
        a10= (CheckBox) findViewById(R.id.activity_customer_prospect_alamat_customer_value2);
        a11= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_pasar_value2);
        a12= (CheckBox) findViewById(R.id.activity_customer_prospect_cluster_value2);
        a13= (CheckBox) findViewById(R.id.activity_customer_prospect_telp_value2);
        a14= (CheckBox) findViewById(R.id.activity_customer_prospect_telp_customer_value2);
        a15= (CheckBox) findViewById(R.id.activity_customer_prospect_fax_value2);
        a16= (CheckBox) findViewById(R.id.activity_customer_prospect_type_customer_value2);
        a17= (CheckBox) findViewById(R.id.activity_customer_prospect_omset_value2);
        a18= (CheckBox) findViewById(R.id.activity_customer_prospect_cara_pembayaran_value2);
        a19= (CheckBox) findViewById(R.id.activity_customer_prospect_plafon_value2);
        a20= (CheckBox) findViewById(R.id.activity_customer_prospect_term_value2);
        a21= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_istri_value2);
        a22= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_anak1_value2);
        a23= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_anak2_value2);
        a24= (CheckBox) findViewById(R.id.activity_customer_prospect_nama_anak3_value2);
        a25= (CheckBox) findViewById(R.id.activity_customer_detail_status_value2);

        a1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a1.isChecked()){
                    etNamaCustomer.setFocusable(false);
                }
                if(!a1.isChecked()){
                    etNamaCustomer.setFocusable(true);
                }return;
            }
        });

        a2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a2.isChecked()){
                    etEmailCustomer.setFocusable(false);
                }if(!a1.isChecked()){
                    etEmailCustomer.setFocusable(true);
                }return;
            }
        });

        a3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a3.isChecked()){
                    etno_ktp.setFocusable(false);
                }if(!a1.isChecked()){
                    etno_ktp.setFocusable(true);
                }return;
            }
        });

        a4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a4.isChecked()){
                    etTanggal_lahir.setFocusable(false);
                }if(!a1.isChecked()){
                    etTanggal_lahir.setFocusable(true);
                }return;
            }
        });

        a5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a5.isChecked()){
                    etKode_pos.setFocusable(false);
                }if(!a1.isChecked()){
                    etKode_pos.setFocusable(true);
                }return;
            }
        });

        a6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a6.isChecked()){
                    etNama_bank.setFocusable(false);
                }if(!a1.isChecked()){
                    etNama_bank.setFocusable(true);
                }return;
            }
        });

        a7.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a7.isChecked()){
                    etNo_rekenig.setFocusable(false);
                }if(!a1.isChecked()){
                    etNo_rekenig.setFocusable(true);
                }return;
            }
        });

        a8.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a8.isChecked()){
                    etAtas_nama.setFocusable(false);
                }if(!a1.isChecked()){
                    etAtas_nama.setFocusable(true);
                }return;
            }
        });

        a9.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a9.isChecked()){
                    etNpwp.setFocusable(false);
                }if(!a1.isChecked()){
                    etNpwp.setFocusable(true);
                }return;
            }
        });

        a10.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a10.isChecked()){
                    etAlamatCustomer.setFocusable(false);
                }if(!a1.isChecked()){
                    etAlamatCustomer.setFocusable(true);
                }return;
            }
        });

        a11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a11.isChecked()){
                    etNama_pasar.setFocusable(false);
                }if(!a1.isChecked()){
                    etNama_pasar.setFocusable(true);
                }return;
            }
        });

        a12.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a12.isChecked()){
                    spinnerCluster.setFocusable(false);
                }if(!a1.isChecked()){
                    spinnerCluster.setFocusable(true);
                }return;
            }
        });

        a13.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a13.isChecked()){
                    etTelp.setFocusable(false);
                }if(!a1.isChecked()){
                    etTelp.setFocusable(true);
                }return;
            }
        });

        a14.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a14.isChecked()){
                    etTelpCustomer.setFocusable(false);
                }if(!a1.isChecked()){
                    etTelpCustomer.setFocusable(true);
                }return;
            }
        });

        a15.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a15.isChecked()){
                    etFax.setFocusable(false);
                }if(!a1.isChecked()){
                    etFax.setFocusable(true);
                }return;
            }
        });

        a16.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a16.isChecked()){
                    spinnerTypeCustomer.setFocusable(false);
                }if(!a1.isChecked()){
                    spinnerTypeCustomer.setFocusable(true);
                }return;
            }
        });

        a17.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a17.isChecked()){
                    etOmset.setFocusable(false);
                }if(!a1.isChecked()){
                    etOmset.setFocusable(true);
                }return;
            }
        });

        a18.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a18.isChecked()){
                    etCara_pembayaran.setFocusable(false);
                }if(!a1.isChecked()){
                    etCara_pembayaran.setFocusable(true);
                }return;
            }
        });

        a19.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a19.isChecked()){
                    etPlafon_kredit.setFocusable(false);
                }if(!a1.isChecked()){
                    etPlafon_kredit.setFocusable(true);
                }return;
            }
        });

        a20.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a20.isChecked()){
                    etTerm_kredit.setFocusable(false);
                }if(!a1.isChecked()){
                    etTerm_kredit.setFocusable(true);
                }return;
            }
        });

        a21.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a21.isChecked()){
                    etNama_istri.setFocusable(false);
                }if(!a1.isChecked()){
                    etNama_istri.setFocusable(true);
                }return;
            }
        });

        a22.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a22.isChecked()){
                    etNama_anak1.setFocusable(false);
                }if(!a1.isChecked()){
                    etNama_anak1.setFocusable(true);
                }return;
            }
        });

        a23.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a23.isChecked()){
                    etNama_anak2.setFocusable(false);
                }if(!a1.isChecked()){
                    etNama_anak2.setFocusable(true);
                }return;
            }
        });

        a24.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a24.isChecked()){
                    etNama_anak3.setFocusable(false);
                }if(!a1.isChecked()){
                    etNama_anak3.setFocusable(true);
                }
                return;
            }
        });

        a25.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(a25.isChecked()){
                    tgStatus.setFocusable(false);
                }if(!a1.isChecked()){
                    tgStatus.setFocusable(true);
                }return;
            }
        });

        etTanggal_lahir.setClickable(true);
        etTanggal_lahir.setFocusable(false);
        etDescription.setFocusable(false);
        etTanggal_lahir.setOnClickListener(new OnClickListener() {

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
        updateDateDisplay(sYear,sMonth,sDay);

        //set list cluster
        clusterList = new ArrayList<Cluster>();
        clusterStringList = new ArrayList<String>();
        List<Cluster> dataCluster = databaseHandler
                .getAllCluster();
        for (Cluster cluster : dataCluster) {
            clusterList.add(cluster);
            clusterStringList.add(cluster.getNama_cluster());
        }

        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_staff_pwd = spPreferences.getString(CONFIG.SHARED_PREFERENCES_STAFF_PASSWORD, null);

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
        adapterCustomer
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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

        tgStatus.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tgStatus.isChecked()){
                    tvstatus.setText("Y");
                    etDescription.setText("Aktif");
                }else{
                    tvstatus.setText("N");
                    etDescription.setText("Tidak Aktif");
                }
            }
        });

        idTypeCustomer = typeCustomerList.get(0).getId_type_customer();

        tvKodeCustomer.setTypeface(typefaceSmall);
        tvWilayahCustomer.setTypeface(typefaceSmall);
        tvGpsCustomer.setTypeface(typefaceSmall);
        tvImage1Customer.setTypeface(typefaceSmall);
        tvImage1Customer.setTypeface(typefaceSmall);
        tvImage2Customer.setTypeface(typefaceSmall);
        tvImage3Customer.setTypeface(typefaceSmall);

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
        mButtonCustomerDetailImage1.setVisibility(View.VISIBLE);
        mButtonCustomerDetailImage2.setVisibility(View.VISIBLE);
        mButtonCustomerDetailImage3.setVisibility(View.VISIBLE);
        mButtonCustomerDetailImage1
                .setOnClickListener(mDetailCustomerButtonOnClickListener);
        mButtonCustomerDetailImage2
                .setOnClickListener(mDetailCustomerButtonOnClickListener);
        mButtonCustomerDetailImage3
                .setOnClickListener(mDetailCustomerButtonOnClickListener);
        mButtonCustomerDetailSave
                .setOnClickListener(mDetailCustomerButtonOnClickListener);
        mButtonCustomerDetailPreview
                .setOnClickListener(mDetailCustomerButtonOnClickListener);
        mButtonCustomerDetailMap
                .setOnClickListener(mDetailCustomerButtonOnClickListener);
        //SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_id = spPreferences.getString(
                CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
        if (main_app_id != null) {
            saveAppDataCustomerIdCustomer(main_app_id);
            showKodeCustomerFromDB(main_app_id);
        } else {
            gotoCustomer();
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
        etTanggal_lahir.setText(adate);
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
                                gotoCustomer();
                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
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
                case R.id.activity_customer_detail_btn_map:
                    gotoCustomerLocator();
                    break;
                case R.id.activity_customer_detail_btn_preview:
                    previewImageDialog();
                    break;
                case R.id.activity_customer_detail_btn_save:
                    if (customer != null) {
                        SharedPreferences spPreferences = getSharedPrefereces();
                        String main_app_staff_pwd = spPreferences.getString(CONFIG.SHARED_PREFERENCES_STAFF_PASSWORD, null);
                        String pwdapp=approval.getText().toString();
                        String pwd=md5(pwdapp);

                        if (etAlamatCustomer.getText().length() > 0
                                && etno_ktp.getText().length() > 0
                                && etTanggal_lahir.getText().length() > 0
                                && etNama_bank.getText().length() > 0
                                && etNo_rekenig.getText().length() > 0
                                && etAtas_nama.getText().length() > 0
                                && etNpwp.getText().length() > 0
                                && etAlamatCustomer.getText().length() > 0
                                && idCluster != 0
                                && etTelp.getText().length() > 0
                                && etFax.getText().length() > 0
                                && etNama_pasar.getText().length() > 0
                                && etOmset.getText().length() > 0
                                && idTypeCustomer != 0
                                && etCara_pembayaran.getText().length() > 0
                                && etPlafon_kredit.getText().length() > 0
                                && etTerm_kredit.getText().length() > 0
                                && etNamaCustomer.getText().length() > 0
                                && etTelpCustomer.getText().length() > 0
                                && approval.getText().length() > 0
                                && a1.isChecked()
                                && a2.isChecked()
                                && a3.isChecked()
                                && a4.isChecked()
                                && a5.isChecked()
                                && a6.isChecked()
                                && a7.isChecked()
                                && a8.isChecked()
                                && a9.isChecked()
                                && a10.isChecked()
                                && a11.isChecked()
                                && a12.isChecked()
                                && a13.isChecked()
                                && a14.isChecked()
                                && a15.isChecked()
                                && a16.isChecked()
                                && a17.isChecked()
                                && a18.isChecked()
                                && a19.isChecked()
                                && a20.isChecked()
                                && a21.isChecked()
                                && a22.isChecked()
                                && a23.isChecked()
                                && a24.isChecked()
                                && a25.isChecked()) {
                            String curLatitude = String.valueOf((int) latitude);
                            String curLongitude = String.valueOf((int) longitude);
                            if (curLatitude.equalsIgnoreCase("0")
                                    || curLongitude.equalsIgnoreCase("0")) {
                                //saveGPSandProfile();
                                String msg = getApplicationContext()
                                       .getResources()
                                        .getString(R.string.MSG_DLG_LABEL_FAILED_GPS_DIALOG);
                                showCustomDialog(msg);
                            } else {
                                if(pwd.equals(main_app_staff_pwd)){
                                    //showConfirmationUpdateCustomerProspect();
                                    saveGPSandProfile();
                                }else{
                                    String dlg = getApplicationContext()
                                            .getResources()
                                            .getString(R.string.MSG_DLG_LABEL_NOVALID_PWD);
                                    showCustomDialog(dlg);
                                }
                            }
                        } else  {
                            String msg = getApplicationContext()
                                    .getResources()
                                    .getString(
                                            R.string.app_customer_prospect_save_failed_empty);
                            showCustomDialog(msg);
                        }
                    } else {
                        gotoCustomer();
                    }
                    break;

                //useless
                case R.id.activity_customer_detail_btn_check_in:
                    customer1 = databaseHandler.getCustomer("CST.HO.201702111038031");
                    if (Integer.parseInt(customer1.getLats()) != 0) {
                        String curLatitude = String
                                .valueOf((int) tempCheckInLatitude);
                        String curLongitude = String
                                .valueOf((int) tempCheckInLongitude);
                        if (curLatitude.equalsIgnoreCase("0")
                                || curLongitude.equalsIgnoreCase("0")) {
                            String msg = getApplicationContext().getResources()
                                    .getString(R.string.app_jadwal_no_gps_location);
                            showCustomDialog(msg);
                        } else {
                            jadwal=databaseHandler.getJadwal1("CST.HO.201702111038031");
                            if (Integer.parseInt(jadwal.getStatus_update()) == 1) {
                                double differentKm = distanceNew(
                                        tempCheckInLatitude, tempCheckInLongitude,
                                        latitude, longitude);
                                int getMeter = (int) differentKm * 1000;
                                String message = getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_jadwal_detail_far_away_location_check_in);
                                if (getMeter > 50) {
                                    showCustomDialog(message);
                                } else {
                                    String kode_customer = "CST.HO.201702111038031";//tvKodeCustomer.getText().toString();
                                    databaseHandler.updateJadwalwhereCheckIn(kode_customer);
                                    /*final String date = "yyyy-MM-dd";
                                    Calendar calendar = Calendar.getInstance();
                                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                                            date);
                                    final String dateOutput = dateFormat
                                            .format(calendar.getTime());
                                    Calendar now = Calendar.getInstance();
                                    int hrs = now.get(Calendar.HOUR_OF_DAY);
                                    int min = now.get(Calendar.MINUTE);
                                    int sec = now.get(Calendar.SECOND);
                                    final String time = zero(hrs) + ":" + zero(min)
                                            + ":" + zero(sec);
                                    String datetime = dateOutput + " " + time;
                                    Jadwal newJadwal = new Jadwal();
                                    newJadwal.setId_jadwal(jadwal.getId_jadwal());
                                    newJadwal.setKode_jadwal(jadwal.getKode_jadwal());
                                    newJadwal.setAlamat(jadwal.getAlamat());
                                    newJadwal.setCheckin(datetime);
                                    newJadwal.setCheckout(jadwal.getCheckout());
                                    newJadwal.setDate(jadwal.getDate());
                                    newJadwal.setId_wilayah(jadwal.getId_wilayah());
                                    newJadwal.setKode_customer(jadwal
                                            .getKode_customer());
                                    newJadwal.setNama_toko(jadwal
                                            .getNama_toko());
                                    newJadwal.setStatus(jadwal.getStatus());
                                    newJadwal.setStatus_update("2");
                                    newJadwal.setUsername(jadwal.getUsername());
                                    databaseHandler.updateJadwal(
                                            jadwal.getId_jadwal(), newJadwal);
                                    String msg = getApplicationContext()
                                            .getResources()
                                            .getString(
                                                    R.string.app_jadwal_success_check_in);
                                    showCustomDialogSaveSuccess(msg);
                                    */
                                }
                            } else {
                                String msg = getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_jadwal_failed_check_in);
                                showCustomDialog(msg);
                            }
                        }
                    } else {
                        String msg = getApplicationContext().getResources()
                                .getString(R.string.app_jadwal_no_gps_location);
                        showCustomDialog(msg);
                    }
                    break;
                case R.id.activity_customer_detail_btn_check_out:
                    if (jadwal != null) {
                        String curLatitude = String
                                .valueOf((int) tempCheckInLatitude);
                        String curLongitude = String
                                .valueOf((int) tempCheckInLongitude);
                        if (curLatitude.equalsIgnoreCase("0")
                                || curLongitude.equalsIgnoreCase("0")) {
                            String msg = getApplicationContext().getResources()
                                    .getString(R.string.app_jadwal_no_gps_location);
                            showCustomDialog(msg);
                        } else {
                            if (Integer.parseInt(jadwal.getStatus_update()) == 2) {
                                String kode_customer = tvKodeCustomer.getText().toString();
                                databaseHandler.updateJadwalwhereCheckOut(kode_customer);
                                /*
                                final String date = "yyyy-MM-dd";
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat dateFormat = new SimpleDateFormat(
                                        date);
                                final String dateOutput = dateFormat
                                        .format(calendar.getTime());
                                Calendar now = Calendar.getInstance();
                                int hrs = now.get(Calendar.HOUR_OF_DAY);
                                int min = now.get(Calendar.MINUTE);
                                int sec = now.get(Calendar.SECOND);
                                final String time = zero(hrs) + ":" + zero(min)
                                        + ":" + zero(sec);
                                String datetime = dateOutput + " " + time;
                                Jadwal newJadwal = new Jadwal();
                                newJadwal.setId_jadwal(jadwal.getId_jadwal());
                                newJadwal.setKode_jadwal(jadwal.getKode_jadwal());
                                newJadwal.setAlamat(jadwal.getAlamat());
                                newJadwal.setCheckin(jadwal.getCheckin());
                                newJadwal.setCheckout(datetime);
                                newJadwal.setDate(jadwal.getDate());
                                newJadwal.setId_wilayah(jadwal.getId_wilayah());
                                newJadwal.setKode_customer(jadwal
                                        .getKode_customer());
                                newJadwal.setNama_toko(jadwal.getNama_toko());
                                newJadwal.setStatus(jadwal.getStatus());
                                newJadwal.setStatus_update("3");
                                newJadwal.setUsername(jadwal.getUsername());
                                databaseHandler.updateJadwal(jadwal.getId_jadwal(),newJadwal);
                                */
                                String msg = getApplicationContext()
                                        .getResources()
                                        .getString(
                                                R.string.app_jadwal_success_check_out);
                                showCustomDialogSaveSuccess(msg);
                            } else {
                                if (Integer.parseInt(jadwal.getStatus_update()) == 1) {
                                    String msg = getApplicationContext()
                                            .getResources()
                                            .getString(
                                                    R.string.app_jadwal_failed_check_out_need_check_in);
                                    showCustomDialog(msg);
                                } else {
                                    String msg = getApplicationContext()
                                            .getResources()
                                            .getString(
                                                    R.string.app_jadwal_failed_check_out);
                                    showCustomDialog(msg);
                                }

                            }
                        }
                    }
                    break;
                default:
                    break;
            }
        }

    };


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
                    + tvKodeCustomer.getText().toString()+kodeBranch+"_"
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

    public void gotoCustomerLocator() {
        SharedPreferences spPreferences = getSharedPrefereces();
        String main_app_table_id = spPreferences.getString(
                CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER, null);
        if (main_app_table_id != null) {
            saveAppDataCustomerIdCustomer(main_app_table_id);
            Intent intentActivity = new Intent(
                    DetailEditCustomerNoo.this,
                    CustomerNooLocatorActivity.class);
            startActivity(intentActivity);
            finish();
        } else {
            gotoCustomer();
        }

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

    protected void showConfirmationUpdateCustomerProspect() {
        String msg = getApplicationContext().getResources().getString(
                R.string.MSG_DLG_LABEL_UPDATE_GPS_DIALOG);
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
                                saveGPSandProfile();
                            }
                        })
                .setPositiveButton(
                        getApplicationContext().getResources().getString(
                                R.string.MSG_DLG_LABEL_NO_ONLY_DATA),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                saveOnlyProfile();

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private double distanceNew(double lat1, double lon1, double lat2,
                               double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        // if (unit == 'K') {
        dist = dist * 1.609344;
        // } else if (unit == 'N') {
        // dist = dist * 0.8684;
        // }
        return (dist) * 2;
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts decimal degrees to radians : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
	/* :: This function converts radians to decimal degrees : */
	/* ::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: */
    private double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    protected void saveGPSandProfile() {
        tempCheckInLatitude = Double.parseDouble(customer.getLats());
        tempCheckInLongitude = Double.parseDouble(customer.getLongs());

        ////////////////////////////////////////////////////////////////////////////////////////////
        if( tvImage1Customer.getText().toString().length() > 0){
            String curLatitude = String
                    .valueOf((int) tempCheckInLatitude);
            String curLongitude = String
                    .valueOf((int) tempCheckInLongitude);
            if(curLatitude.equalsIgnoreCase("0")
                    || curLongitude.equalsIgnoreCase("0")){
                if(customer.getFoto_1() .length() == 0){
                    final String date = "yyyy-MM-dd";
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            date);
                    final String checkDate = dateFormat.format(calendar
                            .getTime());
                    SharedPreferences spPreferences = getSharedPrefereces();
                    String idStaff = spPreferences.getString(
                            CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                            null);

                    Customer newCustomer = new Customer();
                    newCustomer.setId_customer(customer.getId_customer());
                    newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                    newCustomer.setBlokir(customer.getBlokir());
                    newCustomer.setDate(checkDate);
                    newCustomer.setEmail(etEmailCustomer.getText().toString());
                    newCustomer.setFoto_1(newImageName1);
                    newCustomer.setFoto_2(newImageName2);
                    newCustomer.setFoto_3(newImageName3);
                    newCustomer.setId_type_customer(idTypeCustomer);
                    newCustomer.setId_wilayah(customer.getId_wilayah());
                    newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                    newCustomer.setLats(String.valueOf(latitude));
                    newCustomer.setLongs(String.valueOf(longitude));
                    newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                    newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                    newCustomer.setStatus_update(customer.getStatus_update());
                    newCustomer.setId_staff(Integer.parseInt(idStaff));
                    newCustomer.setNo_ktp(etno_ktp.getText().toString());
                    newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                    newCustomer.setNama_bank(etNama_bank.getText().toString());
                    newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                    newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                    newCustomer.setNpwp(etNpwp.getText().toString());
                    newCustomer.setStatus_update("2");
                    newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                    newCustomer.setId_cluster(idCluster);
                    newCustomer.setTelp(etTelp.getText().toString());
                    newCustomer.setFax(etFax.getText().toString());
                    newCustomer.setOmset(etOmset.getText().toString());
                    newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                    newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                    newCustomer.setNama_istri(etNama_istri.getText().toString());
                    newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                    newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                    newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                    newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                    newCustomer.setKode_pos(etKode_pos.getText().toString());
                    newCustomer.setId_depo(customer.getId_depo());
                    newCustomer.setIsactive(tvstatus.getText().toString());
                    newCustomer.setDescription(etDescription.getText().toString());
                    newCustomer.setNama_toko(etNama_toko.getText().toString());

                    databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                    String msg = getApplicationContext().getResources().getString(
                            R.string.app_customer_update_success);
                    showCustomDialogSaveSuccess(msg);
                }else if(customer.getFoto_1().length()> 0 ||customer.getFoto_2() .length() == 0){
                    final String date = "yyyy-MM-dd";
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            date);
                    final String checkDate = dateFormat.format(calendar
                            .getTime());
                    SharedPreferences spPreferences = getSharedPrefereces();
                    String idStaff = spPreferences.getString(
                            CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                            null);

                    Customer newCustomer = new Customer();
                    newCustomer.setId_customer(customer.getId_customer());
                    newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                    newCustomer.setBlokir(customer.getBlokir());
                    newCustomer.setDate(checkDate);
                    newCustomer.setEmail(etEmailCustomer.getText().toString());
                    newCustomer.setFoto_1(customer.getFoto_1());
                    newCustomer.setFoto_2(newImageName2);
                    newCustomer.setFoto_3(newImageName3);
                    newCustomer.setId_type_customer(idTypeCustomer);
                    newCustomer.setId_wilayah(customer.getId_wilayah());
                    newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                    newCustomer.setLats(String.valueOf(latitude));
                    newCustomer.setLongs(String.valueOf(longitude));
                    newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                    newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                    newCustomer.setStatus_update(customer.getStatus_update());
                    newCustomer.setId_staff(Integer.parseInt(idStaff));
                    newCustomer.setNo_ktp(etno_ktp.getText().toString());
                    newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                    newCustomer.setNama_bank(etNama_bank.getText().toString());
                    newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                    newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                    newCustomer.setNpwp(etNpwp.getText().toString());
                    newCustomer.setStatus_update("2");
                    newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                    newCustomer.setId_cluster(idCluster);
                    newCustomer.setTelp(etTelp.getText().toString());
                    newCustomer.setFax(etFax.getText().toString());
                    newCustomer.setOmset(etOmset.getText().toString());
                    newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                    newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                    newCustomer.setNama_istri(etNama_istri.getText().toString());
                    newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                    newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                    newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                    newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                    newCustomer.setKode_pos(etKode_pos.getText().toString());
                    newCustomer.setId_depo(customer.getId_depo());
                    newCustomer.setIsactive(tvstatus.getText().toString());
                    newCustomer.setDescription(etDescription.getText().toString());
                    newCustomer.setNama_toko(etNama_toko.getText().toString());

                    databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                    String msg = getApplicationContext().getResources().getString(
                            R.string.app_customer_update_success);
                    showCustomDialogSaveSuccess(msg);
                }else{
                    final String date = "yyyy-MM-dd";
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            date);
                    final String checkDate = dateFormat.format(calendar
                            .getTime());
                    SharedPreferences spPreferences = getSharedPrefereces();
                    String idStaff = spPreferences.getString(
                            CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                            null);

                    Customer newCustomer = new Customer();
                    newCustomer.setId_customer(customer.getId_customer());
                    newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                    newCustomer.setBlokir(customer.getBlokir());
                    newCustomer.setDate(checkDate);
                    newCustomer.setEmail(etEmailCustomer.getText().toString());
                    newCustomer.setFoto_1(customer.getFoto_1());
                    newCustomer.setFoto_2(customer.getFoto_2());
                    newCustomer.setFoto_3(newImageName3);
                    newCustomer.setId_type_customer(idTypeCustomer);
                    newCustomer.setId_wilayah(customer.getId_wilayah());
                    newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                    newCustomer.setLats(String.valueOf(latitude));
                    newCustomer.setLongs(String.valueOf(longitude));
                    newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                    newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                    newCustomer.setStatus_update(customer.getStatus_update());
                    newCustomer.setId_staff(Integer.parseInt(idStaff));
                    newCustomer.setNo_ktp(etno_ktp.getText().toString());
                    newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                    newCustomer.setNama_bank(etNama_bank.getText().toString());
                    newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                    newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                    newCustomer.setNpwp(etNpwp.getText().toString());
                    newCustomer.setStatus_update("2");
                    newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                    newCustomer.setId_cluster(idCluster);
                    newCustomer.setTelp(etTelp.getText().toString());
                    newCustomer.setFax(etFax.getText().toString());
                    newCustomer.setOmset(etOmset.getText().toString());
                    newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                    newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                    newCustomer.setNama_istri(etNama_istri.getText().toString());
                    newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                    newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                    newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                    newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                    newCustomer.setKode_pos(etKode_pos.getText().toString());
                    newCustomer.setId_depo(customer.getId_depo());
                    newCustomer.setIsactive(tvstatus.getText().toString());
                    newCustomer.setDescription(etDescription.getText().toString());
                    newCustomer.setNama_toko(etNama_toko.getText().toString());

                    databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                    String msg = getApplicationContext().getResources().getString(
                            R.string.app_customer_update_success);
                    showCustomDialogSaveSuccess(msg);
                }
            }else{
                double differentKm = distanceNew(tempCheckInLatitude,
                        tempCheckInLongitude, latitude, longitude);
                int getMeter = (int) differentKm * 1000;
                String message = getApplicationContext()
                        .getResources()
                        .getString(
                                R.string.app_customer_detail_far_away_location);
                if(getMeter <= 50){
                    if(customer.getFoto_1() .length() == 0){
                        final String date = "yyyy-MM-dd";
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                date);
                        final String checkDate = dateFormat.format(calendar
                                .getTime());
                        SharedPreferences spPreferences = getSharedPrefereces();
                        String idStaff = spPreferences.getString(
                                CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                                null);
                        Customer newCustomer = new Customer();
                        newCustomer.setId_customer(customer.getId_customer());
                        newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                        newCustomer.setBlokir(customer.getBlokir());
                        newCustomer.setDate(checkDate);
                        newCustomer.setEmail(etEmailCustomer.getText().toString());
                        newCustomer.setFoto_1(newImageName1);
                        newCustomer.setFoto_2(newImageName2);
                        newCustomer.setFoto_3(newImageName3);
                        newCustomer.setId_type_customer(idTypeCustomer);
                        newCustomer.setId_wilayah(customer.getId_wilayah());
                        newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                        newCustomer.setLats(String.valueOf(latitude));
                        newCustomer.setLongs(String.valueOf(longitude));
                        newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                        newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                        newCustomer.setStatus_update(customer.getStatus_update());
                        newCustomer.setId_staff(Integer.parseInt(idStaff));
                        newCustomer.setNo_ktp(etno_ktp.getText().toString());
                        newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                        newCustomer.setNama_bank(etNama_bank.getText().toString());
                        newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                        newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                        newCustomer.setNpwp(etNpwp.getText().toString());
                        newCustomer.setStatus_update("2");
                        newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                        newCustomer.setId_cluster(idCluster);
                        newCustomer.setTelp(etTelp.getText().toString());
                        newCustomer.setFax(etFax.getText().toString());
                        newCustomer.setOmset(etOmset.getText().toString());
                        newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                        newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                        newCustomer.setNama_istri(etNama_istri.getText().toString());
                        newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                        newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                        newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                        newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                        newCustomer.setKode_pos(etKode_pos.getText().toString());
                        newCustomer.setId_depo(customer.getId_depo());
                        newCustomer.setIsactive(tvstatus.getText().toString());
                        newCustomer.setDescription(etDescription.getText().toString());
                        newCustomer.setNama_toko(etNama_toko.getText().toString());

                        databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                        String msg = getApplicationContext().getResources().getString(
                                R.string.app_customer_update_success);
                        showCustomDialogSaveSuccess(msg);
                    }else if(customer.getFoto_1().length() > 0 || customer.getFoto_2() .length() == 0){
                        final String date = "yyyy-MM-dd";
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                date);
                        final String checkDate = dateFormat.format(calendar
                                .getTime());
                        SharedPreferences spPreferences = getSharedPrefereces();
                        String idStaff = spPreferences.getString(
                                CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                                null);
                        Customer newCustomer = new Customer();
                        newCustomer.setId_customer(customer.getId_customer());
                        newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                        newCustomer.setBlokir(customer.getBlokir());
                        newCustomer.setDate(checkDate);
                        newCustomer.setEmail(etEmailCustomer.getText().toString());
                        newCustomer.setFoto_1(customer.getFoto_1());
                        newCustomer.setFoto_2(newImageName2);
                        newCustomer.setFoto_3(newImageName3);
                        newCustomer.setId_type_customer(idTypeCustomer);
                        newCustomer.setId_wilayah(customer.getId_wilayah());
                        newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                        newCustomer.setLats(customer.getLats());
                        newCustomer.setLongs(customer.getLongs());
                        newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                        newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                        newCustomer.setStatus_update(customer.getStatus_update());
                        newCustomer.setId_staff(Integer.parseInt(idStaff));
                        newCustomer.setNo_ktp(etno_ktp.getText().toString());
                        newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                        newCustomer.setNama_bank(etNama_bank.getText().toString());
                        newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                        newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                        newCustomer.setNpwp(etNpwp.getText().toString());
                        newCustomer.setStatus_update("2");
                        newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                        newCustomer.setId_cluster(idCluster);
                        newCustomer.setTelp(etTelp.getText().toString());
                        newCustomer.setFax(etFax.getText().toString());
                        newCustomer.setOmset(etOmset.getText().toString());
                        newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                        newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                        newCustomer.setNama_istri(etNama_istri.getText().toString());
                        newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                        newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                        newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                        newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                        newCustomer.setKode_pos(etKode_pos.getText().toString());
                        newCustomer.setId_depo(customer.getId_depo());
                        newCustomer.setIsactive(tvstatus.getText().toString());
                        newCustomer.setDescription(etDescription.getText().toString());
                        newCustomer.setNama_toko(etNama_toko.getText().toString());

                        databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                        String msg = getApplicationContext().getResources().getString(
                                R.string.app_customer_update_success);
                        showCustomDialogSaveSuccess(msg);
                    }else{
                        final String date = "yyyy-MM-dd";
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat(
                                date);
                        final String checkDate = dateFormat.format(calendar
                                .getTime());
                        SharedPreferences spPreferences = getSharedPrefereces();
                        String idStaff = spPreferences.getString(
                                CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                                null);
                        Customer newCustomer = new Customer();
                        newCustomer.setId_customer(customer.getId_customer());
                        newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                        newCustomer.setBlokir(customer.getBlokir());
                        newCustomer.setDate(checkDate);
                        newCustomer.setEmail(etEmailCustomer.getText().toString());
                        newCustomer.setFoto_1(customer.getFoto_1());
                        newCustomer.setFoto_2(customer.getFoto_2());
                        newCustomer.setFoto_3(newImageName3);
                        newCustomer.setId_type_customer(idTypeCustomer);
                        newCustomer.setId_wilayah(customer.getId_wilayah());
                        newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                        newCustomer.setLats(customer.getLats());
                        newCustomer.setLongs(customer.getLongs());
                        newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                        newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                        newCustomer.setStatus_update(customer.getStatus_update());
                        newCustomer.setId_staff(Integer.parseInt(idStaff));
                        newCustomer.setNo_ktp(etno_ktp.getText().toString());
                        newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                        newCustomer.setNama_bank(etNama_bank.getText().toString());
                        newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                        newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                        newCustomer.setNpwp(etNpwp.getText().toString());
                        newCustomer.setStatus_update("2");
                        newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                        newCustomer.setId_cluster(idCluster);
                        newCustomer.setTelp(etTelp.getText().toString());
                        newCustomer.setFax(etFax.getText().toString());
                        newCustomer.setOmset(etOmset.getText().toString());
                        newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                        newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                        newCustomer.setNama_istri(etNama_istri.getText().toString());
                        newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                        newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                        newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                        newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                        newCustomer.setKode_pos(etKode_pos.getText().toString());
                        newCustomer.setId_depo(customer.getId_depo());
                        newCustomer.setIsactive(tvstatus.getText().toString());
                        newCustomer.setDescription(etDescription.getText().toString());
                        newCustomer.setNama_toko(etNama_toko.getText().toString());

                        databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                        String msg = getApplicationContext().getResources().getString(
                                R.string.app_customer_update_success);
                        showCustomDialogSaveSuccess(msg);
                    }
                }else{
                    showCustomDialog(message);
                }
            }
        }else{
            String msg = getApplicationContext()
                    .getResources()
                    .getString(R.string.app_customer_prospect_save_failed_no_image_new);
                showCustomDialog(msg);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////
        /*
        if (newImageName1 != null){
            String curLatitude = String
                    .valueOf((int) tempCheckInLatitude);
            String curLongitude = String
                    .valueOf((int) tempCheckInLongitude);
            if (curLatitude.equalsIgnoreCase("0")
                    || curLongitude.equalsIgnoreCase("0")){
                final String date = "yyyy-MM-dd";
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        date);
                final String checkDate = dateFormat.format(calendar
                        .getTime());
                Customer newCustomer = new Customer();
                newCustomer.setId_customer(customer.getId_customer());
                newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                newCustomer.setBlokir(customer.getBlokir());
                newCustomer.setDate(checkDate);
                newCustomer.setEmail(etEmailCustomer.getText().toString());
                newCustomer.setFoto_1(newImageName1);
                newCustomer.setFoto_2(newImageName2);
                newCustomer.setFoto_3(newImageName3);
                newCustomer.setId_type_customer(idTypeCustomer);
                newCustomer.setId_wilayah(customer.getId_wilayah());
                newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                newCustomer.setLats(String.valueOf(latitude));
                newCustomer.setLongs(String.valueOf(longitude));
                newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                newCustomer.setStatus_update(customer.getStatus_update());
                newCustomer.setId_staff(customer.getId_staff());
                newCustomer.setNo_ktp(etno_ktp.getText().toString());
                newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                newCustomer.setNama_bank(etNama_bank.getText().toString());
                newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                newCustomer.setNpwp(etNpwp.getText().toString());
                newCustomer.setStatus_update("2");
                newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                newCustomer.setId_cluster(idCluster);
                newCustomer.setTelp(etTelp.getText().toString());
                newCustomer.setFax(etFax.getText().toString());
                newCustomer.setOmset(etOmset.getText().toString());
                newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                newCustomer.setNama_istri(etNama_istri.getText().toString());
                newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                newCustomer.setKode_pos(etKode_pos.getText().toString());
                newCustomer.setId_depo(customer.getId_depo());
                newCustomer.setIsactive(tvstatus.getText().toString());
                newCustomer.setDescription(etDescription.getText().toString());
                newCustomer.setNama_toko(etNama_toko.getText().toString());

                databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                String msg = getApplicationContext().getResources().getString(
                        R.string.app_customer_update_success);
                showCustomDialogSaveSuccess(msg);

            }else{
                double differentKm = distanceNew(tempCheckInLatitude,
                        tempCheckInLongitude, latitude, longitude);
                int getMeter = (int) differentKm * 1000;
                String message = getApplicationContext()
                        .getResources()
                        .getString(
                                R.string.app_customer_detail_far_away_location);

                if(getMeter < 50){
                    final String date = "yyyy-MM-dd";
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            date);
                    final String checkDate = dateFormat.format(calendar
                            .getTime());
                    Customer newCustomer = new Customer();
                    newCustomer.setId_customer(customer.getId_customer());
                    newCustomer.setAlamat(etAlamatCustomer.getText().toString());
                    newCustomer.setBlokir(customer.getBlokir());
                    newCustomer.setDate(checkDate);
                    newCustomer.setEmail(etEmailCustomer.getText().toString());
                    newCustomer.setFoto_1(newImageName1);
                    newCustomer.setFoto_2(newImageName2);
                    newCustomer.setFoto_3(newImageName3);
                    newCustomer.setId_type_customer(idTypeCustomer);
                    newCustomer.setId_wilayah(customer.getId_wilayah());
                    newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
                    newCustomer.setLats(String.valueOf(latitude));
                    newCustomer.setLongs(String.valueOf(longitude));
                    newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
                    newCustomer.setNo_telp(etTelpCustomer.getText().toString());
                    newCustomer.setStatus_update(customer.getStatus_update());
                    newCustomer.setId_staff(customer.getId_staff());
                    newCustomer.setNo_ktp(etno_ktp.getText().toString());
                    newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
                    newCustomer.setNama_bank(etNama_bank.getText().toString());
                    newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
                    newCustomer.setAtas_nama(etAtas_nama.getText().toString());
                    newCustomer.setNpwp(etNpwp.getText().toString());
                    newCustomer.setStatus_update("2");
                    newCustomer.setNama_pasar(etNama_pasar.getText().toString());
                    newCustomer.setId_cluster(idCluster);
                    newCustomer.setTelp(etTelp.getText().toString());
                    newCustomer.setFax(etFax.getText().toString());
                    newCustomer.setOmset(etOmset.getText().toString());
                    newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
                    newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
                    newCustomer.setNama_istri(etNama_istri.getText().toString());
                    newCustomer.setNama_anak1(etNama_anak1.getText().toString());
                    newCustomer.setNama_anak2(etNama_anak2.getText().toString());
                    newCustomer.setNama_anak3(etNama_anak3.getText().toString());
                    newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
                    newCustomer.setKode_pos(etKode_pos.getText().toString());
                    newCustomer.setId_depo(customer.getId_depo());
                    newCustomer.setIsactive(tvstatus.getText().toString());
                    newCustomer.setDescription(etDescription.getText().toString());
                    newCustomer.setNama_toko(etNama_toko.getText().toString());

                    databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
                    String msg = getApplicationContext().getResources().getString(
                            R.string.app_customer_update_success);
                    showCustomDialogSaveSuccess(msg);
                }else {
                    showCustomDialog(message);
                }
            }

        } else{
            String msg = getApplicationContext()
                    .getResources()
                    .getString(
                            R.string.app_customer_prospect_save_failed_no_image_new);
            showCustomDialog(msg);
        }
        */
        ////////////////////////////////////////////////////////////////////////////////////////////
    }

    protected void saveOnlyProfile() {
        if (customer.getFoto_1().equals("'")){
            String msg = getApplicationContext()
                    .getResources()
                    .getString(
                            R.string.app_customer_prospect_save_failed_no_image_old);
            showCustomDialog(msg);
        }else if (customer.getFoto_1().equals("")){
            String msg = getApplicationContext()
                    .getResources()
                    .getString(
                            R.string.app_customer_prospect_save_failed_no_image_old);
            showCustomDialog(msg);
        }else{
            final String date = "yyyy-MM-dd";
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    date);
            final String checkDate = dateFormat.format(calendar
                    .getTime());
            SharedPreferences spPreferences = getSharedPrefereces();
            String idStaff = spPreferences.getString(
                    CONFIG.SHARED_PREFERENCES_STAFF_ID_STAFF,
                    null);
            Customer newCustomer = new Customer();
            newCustomer.setId_customer(customer.getId_customer());
            newCustomer.setAlamat(etAlamatCustomer.getText().toString());
            newCustomer.setBlokir(customer.getBlokir());
            newCustomer.setDate(checkDate);
            newCustomer.setEmail(etEmailCustomer.getText().toString());
            newCustomer.setFoto_1(customer.getFoto_1());
            newCustomer.setFoto_2(customer.getFoto_2());
            newCustomer.setFoto_3(customer.getFoto_3());
            newCustomer.setId_type_customer(idTypeCustomer);
            newCustomer.setId_wilayah(customer.getId_wilayah());
            newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
            newCustomer.setLats(customer.getLats());
            newCustomer.setLongs(customer.getLongs());
            newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
            newCustomer.setNo_telp(etTelpCustomer.getText().toString());
            newCustomer.setStatus_update(customer.getStatus_update());
            newCustomer.setId_staff(Integer.parseInt(idStaff));
            newCustomer.setNo_ktp(etno_ktp.getText().toString());
            newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
            newCustomer.setNama_bank(etNama_bank.getText().toString());
            newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
            newCustomer.setAtas_nama(etAtas_nama.getText().toString());
            newCustomer.setNpwp(etNpwp.getText().toString());
            newCustomer.setStatus_update("2");
            newCustomer.setNama_pasar(etNama_pasar.getText().toString());
            newCustomer.setId_cluster(idCluster);
            newCustomer.setTelp(etTelp.getText().toString());
            newCustomer.setFax(etFax.getText().toString());
            newCustomer.setOmset(etOmset.getText().toString());
            newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
            newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
            newCustomer.setNama_istri(etNama_istri.getText().toString());
            newCustomer.setNama_anak1(etNama_anak1.getText().toString());
            newCustomer.setNama_anak2(etNama_anak2.getText().toString());
            newCustomer.setNama_anak3(etNama_anak3.getText().toString());
            newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
            newCustomer.setKode_pos(etKode_pos.getText().toString());
            newCustomer.setId_depo(customer.getId_depo());
            newCustomer.setIsactive(tvstatus.getText().toString());
            newCustomer.setDescription(etDescription.getText().toString());
            newCustomer.setNama_toko(etNama_toko.getText().toString());

            databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
            String msg = getApplicationContext().getResources().getString(
                    R.string.app_customer_update_success);
            showCustomDialogSaveSuccess(msg);
        }

    }

    ////////////////////////////////////////////// dibawah gak dipake sementara
    /*
    protected void ssaveOnlyProfile() {
        if (customer.getFoto_1().equals("'")){
            String msg = getApplicationContext()
                    .getResources()
                    .getString(
                            R.string.app_customer_prospect_save_failed_no_image_old);
            showCustomDialog(msg);
        }else if (customer.getFoto_1().equals("")){
            String msg = getApplicationContext()
                    .getResources()
                    .getString(
                            R.string.app_customer_prospect_save_failed_no_image_old);
            showCustomDialog(msg);
        }else{
            final String date = "yyyy-MM-dd";
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    date);
            final String checkDate = dateFormat.format(calendar
                .getTime());
            Customer newCustomer = new Customer();
            newCustomer.setId_customer(customer.getId_customer());
            newCustomer.setAlamat(etAlamatCustomer.getText().toString());
            newCustomer.setBlokir(customer.getBlokir());
            newCustomer.setDate(checkDate);
            newCustomer.setEmail(etEmailCustomer.getText().toString());
            newCustomer.setFoto_1(customer.getFoto_1());
            newCustomer.setFoto_2(customer.getFoto_2());
            newCustomer.setFoto_3(customer.getFoto_3());
            newCustomer.setId_type_customer(idTypeCustomer);
            newCustomer.setId_wilayah(customer.getId_wilayah());
            newCustomer.setKode_customer(tvKodeCustomer.getText().toString());
            newCustomer.setLats(customer.getLats());
            newCustomer.setLongs(customer.getLongs());
            newCustomer.setNama_lengkap(etNamaCustomer.getText().toString());
            newCustomer.setNo_telp(etTelpCustomer.getText().toString());
            newCustomer.setStatus_update(customer.getStatus_update());
            newCustomer.setId_staff(customer.getId_staff());
            newCustomer.setNo_ktp(etno_ktp.getText().toString());
            newCustomer.setTanggal_lahir(etTanggal_lahir.getText().toString());
            newCustomer.setNama_bank(etNama_bank.getText().toString());
            newCustomer.setNo_rekening(etNo_rekenig.getText().toString());
            newCustomer.setAtas_nama(etAtas_nama.getText().toString());
            newCustomer.setNpwp(etNpwp.getText().toString());
            newCustomer.setStatus_update("2");
            newCustomer.setNama_pasar(etNama_pasar.getText().toString());
            newCustomer.setId_cluster(idCluster);
            newCustomer.setTelp(etTelp.getText().toString());
            newCustomer.setFax(etFax.getText().toString());
            newCustomer.setOmset(etOmset.getText().toString());
            newCustomer.setCara_pembayaran(etCara_pembayaran.getText().toString());
            newCustomer.setPlafon_kredit(etPlafon_kredit.getText().toString());
            newCustomer.setNama_istri(etNama_istri.getText().toString());
            newCustomer.setNama_anak1(etNama_anak1.getText().toString());
            newCustomer.setNama_anak2(etNama_anak2.getText().toString());
            newCustomer.setNama_anak3(etNama_anak3.getText().toString());
            newCustomer.setTerm_kredit(etTerm_kredit.getText().toString());
            newCustomer.setKode_pos(etKode_pos.getText().toString());
            newCustomer.setId_depo(customer.getId_depo());
            newCustomer.setIsactive(tvstatus.getText().toString());
            newCustomer.setDescription(etDescription.getText().toString());
            newCustomer.setNama_toko(etNama_toko.getText().toString());

            databaseHandler.updateCustomer(customer.getId_customer(), newCustomer);
            String msg = getApplicationContext().getResources().getString(
                    R.string.app_customer_update_success);
            showCustomDialogSaveSuccess(msg);
        }
    }
    */

    private void previewImageDialog() {
        if (customer != null) {
            List<File> mListImages = new ArrayList<File>();
            File dir = new File(CONFIG.getFolderPath() + "/"
                    + CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
                    + customer.getFoto_1());
            if (dir.isFile()) {
                mListImages.add(dir);
            }
            File dir2 = new File(CONFIG.getFolderPath() + "/"
                    + CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
                    + customer.getFoto_2());
            if (dir2.isFile()) {
                mListImages.add(dir2);
            }
            File dir3 = new File(CONFIG.getFolderPath() + "/"
                    + CONFIG.CONFIG_APP_FOLDER_CUSTOMER + "/"
                    + customer.getFoto_3());
            if (dir3.isFile()) {
                mListImages.add(dir3);
            }
            final Dialog imagesDialog = new Dialog(act);
            imagesDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            imagesDialog.setContentView(DetailEditCustomerNoo.this
                    .getLayoutInflater().inflate(R.layout.activity_popup_image,
                            null));

            Gallery galleryImages = (Gallery) imagesDialog
                    .findViewById(R.id.gallery1);
            galleryImages.setAdapter(new ImageAdapter(this, mListImages));

            imagesDialog.show();
        }

    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        // private int itemBackground;
        private List<File> listFile;

        public ImageAdapter(Context context, List<File> listFile) {
            this.context = context;
            this.listFile = listFile;
            // TypedArray a = obtainStyledAttributes(R.styleable.MyGallery);
            // itemBackground = a.getResourceId(
            // R.styleable.MyGallery_android_galleryItemBackground, 0);
            // a.recycle();
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
            // imageView.setLayoutParams(new Gallery.LayoutParams(260, 260));
            // imageView.setBackgroundResource(itemBackground);
            return imageView;
        }
    }

    public String zero(int num) {
        String number = (num < 10) ? ("0" + num) : ("" + num);
        return number;
    }

    public void showKodeCustomerFromDB(String customerId) {
        customer = databaseHandler.getCustomer(Integer.parseInt(customerId));
        if (customer != null) {
            tvKodeCustomer.setText(customer.getKode_customer());
            Wilayah wilayah = databaseHandler.getWilayah(customer
                    .getId_wilayah());
            tvWilayahCustomer.setText(wilayah.getNama_wilayah());
            etNamaCustomer.setText(customer.getNama_lengkap());
            etEmailCustomer.setText(customer.getEmail());
            etAlamatCustomer.setText(customer.getAlamat());
            tvImage1Customer.setText(customer.getFoto_1());
            tvImage2Customer.setText(customer.getFoto_2() != null ? String
                    .valueOf(customer.getFoto_2()) : "");
            tvImage3Customer.setText(customer.getFoto_3() != null ? String
                    .valueOf(customer.getFoto_3()) : "");
            etTelpCustomer.setText(customer.getNo_telp());
            etno_ktp.setText(customer.getNo_ktp());
            etTanggal_lahir.setText(customer.getTanggal_lahir());
            etNama_bank.setText(customer.getNama_bank());
            etNo_rekenig.setText(customer.getNo_rekening());
            etAtas_nama.setText(customer.getAtas_nama());
            etNpwp.setText(customer.getNpwp());
            etNama_pasar.setText(customer.getNama_pasar());
            etTelp.setText(customer.getTelp());
            etFax.setText(customer.getFax());
            etOmset.setText(customer.getOmset());
            etCara_pembayaran.setText(customer.getCara_pembayaran());
            etPlafon_kredit.setText(customer.getPlafon_kredit());
            etTerm_kredit.setText(customer.getTerm_kredit());
            etNama_istri.setText(customer.getNama_istri());
            etNama_anak1.setText(customer.getNama_anak1());
            etNama_anak2.setText(customer.getNama_anak2());
            etNama_anak3.setText(customer.getNama_anak3());
            etKode_pos.setText(customer.getKode_pos());
            tvstatus.setText((customer.getIsactive()));
            etDescription.setText(customer.getDescription());
            etNama_toko.setText(customer.getNama_toko());

            if(customer.getIsactive().equals("Y")){
                tgStatus.setTextOn("Aktif");
                tgStatus.setChecked(true);
            }else{
                tgStatus.setTextOff("Tidak Aktif");
                tgStatus.setChecked(false);
            }
            tempLatitude = customer.getLats();
            tempLongitude = customer.getLongs();

            List<TypeCustomer> dataTypeCustomer = databaseHandler
                    .getAllTypeCustomer();
            int index = 0;
            for (TypeCustomer typeCustomer : dataTypeCustomer) {
                if (typeCustomer.getId_type_customer() == customer
                        .getId_type_customer())
                    break;
                index += 1;
            }
            spinnerTypeCustomer.setSelection(index);
            idTypeCustomer = typeCustomerList.get(index).getId_type_customer();


            List<Cluster> dataCluster = databaseHandler
                    .getAllCluster();
            int index1 = 0;
            for (Cluster cluster : dataCluster) {
                if (cluster.getId_cluster() == customer
                        .getId_cluster())
                    break;
                index1 += 1;
            }
            spinnerCluster.setSelection(index1);
            idCluster = clusterList.get(index1).getId_cluster();

        } else {
            gotoCustomer();
        }

    }

    public void saveAppDataCustomerIdCustomer(String responsedata) {
        SharedPreferences sp = getSharedPrefereces();
        Editor editor = sp.edit();
        editor.putString(CONFIG.SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER,
                responsedata);
        editor.commit();
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

    public void gotoCustomer() {
        Intent i = new Intent(this, CustomerActivity.class);
        startActivity(i);
        finish();
    }

    public void gotoSalesOrder() {
        Intent i = new Intent(this, SalesOrderActivity.class);
        startActivity(i);
        finish();
    }

    public void gotoStockOnHand() {
        Intent i = new Intent(this, StockOnHandActivity.class);
        startActivity(i);
        finish();
    }

    public void gotoSalesRewtur() {
        Intent i = new Intent(this, ReturActivity.class);
        startActivity(i);
        finish();
    }

    public void gotoSalesDisplay() {
        Intent i = new Intent(this, DisplayProductActivity.class);
        startActivity(i);
        finish();
    }
}