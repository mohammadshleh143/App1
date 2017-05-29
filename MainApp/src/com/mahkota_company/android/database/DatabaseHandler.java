package com.mahkota_company.android.database;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.mahkota_company.android.prospect.AddCustomerProspectActivity;

public class DatabaseHandler extends SQLiteOpenHelper {
	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;
	private double latitude; // latitude
	private double longitude; // longitude

	private Location location; // location
	private LocationManager locationManager;

	// Database Name
	private static final String DATABASE_NAME = "mahkota";

	// Contacts table name
	private static final String TABLE_STAFF = "staff";
	private static final String TABLE_STAFF_TEMP = "staff_temp";
	private static final String TABLE_BRANCH = "branch";
	private static final String TABLE_TYPE_CUSTOMER = "type_customer";
	private static final String TABLE_CLUSTER = "cluster";
	private static final String TABLE_WILAYAH = "wilayah";
	private static final String TABLE_KEMASAN = "kemasan";
	private static final String TABLE_PRODUCT = "product";
	private static final String TABLE_PRODUCT_PRICE = "product_price";
	private static final String TABLE_DTG = "dtg";
	private static final String TABLE_STOCK_VAN = "stok_van";
	private static final String TABLE_CUSTOMER = "customer";
	private static final String TABLE_JADWAL = "jadwal";
	private static final String TABLE_PROMOSI = "promosi";
	private static final String TABLE_PHOTO_PURCHASE = "photo_purchase";
	private static final String TABLE_DISPLAY_PRODUCT = "display_product";
	private static final String TABLE_STOCK_ON_HAND = "stock_on_hand";
	private static final String TABLE_SALES_ORDER = "sales_order";
	private static final String TABLE_REQLOAD = "req_load";
	private static final String TABLE_RETUR = "retur";
	private static final String TABLE_TRACKING = "tracking";
	private static final String TABLE_PRODUCT_TARGET = "product_target";
	private static final String TABLE_PENJUALAN = "penjualan";
	private static final String TABLE_PENJUALAN_DETAIL = "penjualan_detail";
	private static final String LOG_TAG = AddCustomerProspectActivity.class.getSimpleName();
	private static final String TABLE_TRACKING_LOGS = "tracking_logs";

	// STAFF Table Columns names
	private static final String KEY_STAFF_ID_STAFF = "id_staff";
	private static final String KEY_STAFF_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_STAFF_USERNAME = "username";
	private static final String KEY_STAFF_NO_TELP = "notelp";
	private static final String KEY_STAFF_PASSWORD = "password";
	private static final String KEY_STAFF_LEVEL = "level";
	private static final String KEY_STAFF_ID_BRANCH = "id_branch";
	private static final String KEY_STAFF_TYPE_CUSTOMER = "id_type_customer";
	private static final String KEY_STAFF_ID_DEPO = "id_depo";


	// STAFF TEMP Table Columns names
	private static final String KEY_STAFF_TEMP_ID_STAFF = "id_staff";
	private static final String KEY_STAFF_TEMP_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_STAFF_TEMP_USERNAME = "username";
	private static final String KEY_STAFF_TEMP_NO_TELP = "notelp";
	private static final String KEY_STAFF_TEMP_PASSWORD = "password";
	private static final String KEY_STAFF_TEMP_LEVEL = "level";
	private static final String KEY_STAFF_TEMP_ID_BRANCH = "id_branch";
	private static final String KEY_STAFF_TEMP_TYPE_CUSTOMER = "id_type_customer";
	private static final String KEY_STAFF_TEMP_ID_DEPO = "id_depo";

	// CUSTOMER Table Columns names
	private static final String KEY_CUSTOMER_ID_CUSTOMER = "id_customer";
	private static final String KEY_CUSTOMER_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_CUSTOMER_EMAIL_CUSTOMER = "email";
	private static final String KEY_CUSTOMER_ALAMAT_CUSTOMER = "alamat";
	private static final String KEY_CUSTOMER_LATS_CUSTOMER = "lats";
	private static final String KEY_CUSTOMER_LONGS_CUSTOMER = "longs";
	private static final String KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER = "nama_lengkap";
	private static final String KEY_CUSTOMER_NO_TELP_CUSTOMER = "no_telp";
	private static final String KEY_CUSTOMER_ID_WILAYAH_CUSTOMER = "id_wilayah";
	private static final String KEY_CUSTOMER_FOTO_1_CUSTOMER = "foto_1";
	private static final String KEY_CUSTOMER_FOTO_2_CUSTOMER = "foto_2";
	private static final String KEY_CUSTOMER_FOTO_3_CUSTOMER = "foto_3";
	private static final String KEY_CUSTOMER_ID_TYPE_CUSTOMER = "id_type_customer";
	private static final String KEY_CUSTOMER_BLOKIR = "blokir";
	private static final String KEY_CUSTOMER_DATE = "date";
	private static final String KEY_CUSTOMER_STATUS_UPDATE = "status_update";
	private static final String KEY_CUSTOMER_ID_STAFF = "id_staff";
	private static final String KEY_CUSTOMER_NO_KTP = "no_ktp";
	private static final String KEY_CUSTOMER_TANGGAL_LAHIR = "tanggal_lahir";
	private static final String KEY_CUSTOMER_NAMA_BANK = "nama_bank";
	private static final String KEY_CUSTOMER_NO_REKENING = "no_rekening";
	private static final String KEY_CUSTOMER_ATAS_NAMA = "atas_nama";
	private static final String KEY_CUSTOMER_NPWP = "npwp";
	private static final String KEY_CUSTOMER_NAMA_PASAR = "nama_pasar";
	private static final String KEY_CUSTOMER_ID_CLUSTER = "id_cluster";
	private static final String KEY_CUSTOMER_TELP = "telp";
	private static final String KEY_CUSTOMER_FAX = "fax";
	private static final String KEY_CUSTOMER_OMSET= "omset";
	private static final String KEY_CUSTOMER_CARA_PEMBAYARAN = "cara_pembayaran";
	private static final String KEY_CUSTOMER_PLAFON_KREDIT = "plafon_kredit";
	private static final String KEY_CUSTOMER_TERM_KREDIT = "term_kredit";
	private static final String KEY_CUSTOMER_NAMA_ISTRI = "nama_istri";
	private static final String KEY_CUSTOMER_NAMA_ANAK1 = "nama_anak1";
	private static final String KEY_CUSTOMER_NAMA_ANAK2 = "nama_anak2";
	private static final String KEY_CUSTOMER_NAMA_ANAK3 = "nama_anak3";
	private static final String KEY_CUSTOMER_KODE_POS = "kode_pos";
	private static final String KEY_CUSTOMER_ID_DEPO = "id_depo";
	private static final String KEY_CUSTOMER_ISACTIVE = "isactive";
	private static final String KEY_CUSTOMER_DESCRIPTION = "description";
	private static final String KEY_CUSTOMER_NAMA_TOKO = "nama_toko";
	private static final String KEY_CUSTOMER_TTD1 = "ttd1";
	private static final String KEY_CUSTOMER_TTD2 = "ttd2";


	// PRODUCT Table Columns names
	private static final String KEY_PRODUCT_ID_PRODUCT = "id_product";
	private static final String KEY_PRODUCT_NAMA_PRODUCT = "nama_product";
	private static final String KEY_PRODUCT_KODE_PRODUCT = "kode_product";
	private static final String KEY_PRODUCT_HARGA_JUAL = "harga_jual";
	private static final String KEY_PRODUCT_STOCK = "stock";
	private static final String KEY_PRODUCT_ID_KEMASAN = "id_kemasan";
	private static final String KEY_PRODUCT_FOTO = "foto";
	private static final String KEY_PRODUCT_DESKRIPSI = "deskripsi";
	private static final String KEY_PRODUCT_UOMQTYL1 = "uomqtyl1";
	private static final String KEY_PRODUCT_UOMQTYL2 = "uomqtyl2";
	private static final String KEY_PRODUCT_UOMQTYL3 = "uomqtyl3";
	private static final String KEY_PRODUCT_UOMQTYL4 = "uomqtyl4";
	private static final String KEY_PRODUCT_STATUS = "status";

	// PRODUCT_PRICE Table Columns names
	private static final String KEY_DTG_ID = "id";
	private static final String KEY_DTG_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_DTG_NAMA_TOKO = "nama_toko";
	private static final String KEY_DTG_NOMER_FAKTUR = "no_faktur";
	private static final String KEY_DTG_NILAI_FAKTUR = "nilai_faktur";
	private static final String KEY_DTG_JML_BELUM_BAYAR = "jml_belum_bayar";
	private static final String KEY_DTG_NO_DTG = "no_dtg";

	// PRODUCT_PRICE Table Columns names
	private static final String KEY_PRODUCT_ID = "id";
	private static final String KEY_PRODUCT_PRICELIST = "pricelist";
	private static final String KEY_PRODUCT_PRICESTD = "pricestd";
	private static final String KEY_PRODUCT_PRICELIMIT = "pricelimit";


	// STOK VAN Table Columns names
	private static final String KEY_STOCK_VAN_ID_PRODUCT = "id_product";
	private static final String KEY_STOCK_VAN_NAMA_PRODUCT = "nama_product";
	private static final String KEY_STOCK_VAN_KODE_PRODUCT = "kode_product";
	private static final String KEY_STOCK_VAN_HARGA_JUAL = "harga_jual";
	private static final String KEY_STOCK_VAN_JUMLAH_REQUEST = "jumlahRequest";
	private static final String KEY_STOCK_VAN_JUMLAH_ACCEPT = "jumlahAccept";
	private static final String KEY_STOCK_VAN_JUMLAH_SISA = "jumlahSisa";
	private static final String KEY_STOCK_VAN_ID_KEMASAN = "id_kemasan";
	private static final String KEY_STOCK_VAN_FOTO = "foto";
	private static final String KEY_STOCK_VAN_DESKRIPSI = "deskripsi";
	private static final String KEY_STOCK_VAN_NOMER_REQUEST_LOAD = "nomer_request_load";

	// PRODUCT TARGET Table Columns names
	private static final String KEY_PRODUCT_TARGET_ID_PRODUCT_TARGET = "id_product_target";
	private static final String KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET = "nomer_product_target";
	private static final String KEY_PRODUCT_TARGET_CD_PRODUCT_TARGET = "created_date_product_target";
	private static final String KEY_PRODUCT_TARGET_CT_PRODUCT_TARGET = "created_time_product_target";
	private static final String KEY_PRODUCT_TARGET_UD_PRODUCT_TARGET = "updated_date_product_target";
	private static final String KEY_PRODUCT_TARGET_UT_PRODUCT_TARGET = "updated_time_product_target";
	private static final String KEY_PRODUCT_TARGET_CB = "created_by";
	private static final String KEY_PRODUCT_TARGET_UB = "updated_by";
	private static final String KEY_PRODUCT_TARGET_ID_STAFF = "id_staff";
	private static final String KEY_PRODUCT_TARGET_ID_CUSTOMER = "id_customer";
	private static final String KEY_PRODUCT_TARGET_ID_PRODUCT = "id_product";
	private static final String KEY_PRODUCT_TARGET_JUMLAH_TARGET = "jumlah_target";
	private static final String KEY_PRODUCT_TARGET_JUMLAH_TERJUAL = "jumlah_terjual";
	// Penjualan Column Name
	private static final String KEY_PENJUALAN_ID_PENJUALAN = "id_penjualan";
	private static final String KEY_PENJUALAN_NOMER_PENJUALAN = "nomer_product_terjual";
	private static final String KEY_PENJUALAN_DATE_PENJUALAN = "date_product_terjual";
	private static final String KEY_PENJUALAN_TIME_PENJUALAN = "time_product_terjual";
	private static final String KEY_PENJUALAN_ID_CUSTOMER = "id_customer";
	private static final String KEY_PENJUALAN_ID_STAFF = "id_staff";
	private static final String KEY_PENJUALAN_DISKON = "diskon";
	// Penjualan Detail Column Name
	private static final String KEY_PENJUALAN_DETAIL_ID_PENJUALAN_DETAIL = "id_penjualan_detail";
	private static final String KEY_PENJUALAN_DETAIL_NOMER_PENJUALAN = "nomer_product_terjual";
	private static final String KEY_PENJUALAN_DETAIL_ID_PRODUCT = "id_product";
	private static final String KEY_PENJUALAN_DETAIL_JUMLAH = "jumlah";

	// JADWAL Table Columns names
	private static final String KEY_JADWAL_ID_JADWAL = "id_jadwal";
	private static final String KEY_JADWAL_KODE_JADWAL = "kode_jadwal";
	private static final String KEY_JADWAL_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_JADWAL_USERNAME = "username";
	private static final String KEY_JADWAL_ALAMAT = "alamat";
	private static final String KEY_JADWAL_NAMA_TOKO = "nama_toko";
	private static final String KEY_JADWAL_ID_WILAYAH = "id_wilayah";
	private static final String KEY_JADWAL_STATUS = "status";
	private static final String KEY_JADWAL_DATE = "date";
	private static final String KEY_JADWAL_CHECK_IN = "checkin";
	private static final String KEY_JADWAL_CHECK_OUT = "checkout";
	private static final String KEY_JADWAL_STATUS_UPDATE = "status_update";
	private static final String KEY_JADWAL_KETERANGAN = "keterangan";

//	// PROMOSI Table Columns names
//	private static final String KEY_PROMOSI_ID_PROMOSI = "id_promosi";
//	private static final String KEY_PROMOSI_TITLE_PROMOSI = "title_promosi";
//	private static final String KEY_PROMOSI_JENIS_PROMOSI = "jenis_promosi";
//	private static final String KEY_PROMOSI_JUMLAH_DISKON = "jumlah_diskon";
//	private static final String KEY_PROMOSI_DESKRIPSI = "deskripsi";
//	private static final String KEY_PROMOSI_FOTO = "foto";
//	private static final String KEY_PROMOSI_DATE_TIME = "datetime";

	// DISPLAY_PRODUCT Table Columns names
	private static final String KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT = "id_display_product";
	private static final String KEY_DISPLAY_PRODUCT_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_DISPLAY_PRODUCT_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_DISPLAY_PRODUCT_FOTO = "foto";
	private static final String KEY_DISPLAY_PRODUCT_NAMA_DISPLAY_PRODUCT = "nama_display_product";
	private static final String KEY_DISPLAY_PRODUCT_DESKRIPSI = "deskripsi";
	private static final String KEY_DISPLAY_PRODUCT_DATE_TIME = "datetime";

//	// PHOTO_PURCHASE Table Columns names
//	private static final String KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE = "id_photo_purchase";
//	private static final String KEY_PHOTO_PURCHASE_KODE_CUSTOMER = "kode_customer";
//	private static final String KEY_PHOTO_PURCHASE_NAMA_LENGKAP = "nama_lengkap";
//	private static final String KEY_PHOTO_PURCHASE_FOTO = "foto";
//	private static final String KEY_PHOTO_PURCHASE_NAMA_PHOTO_PURCHASE = "nama_photo_purchase";
//	private static final String KEY_PHOTO_PURCHASE_DESKRIPSI = "deskripsi";
//	private static final String KEY_PHOTO_PURCHASE_DATE_TIME = "datetime";

	// TYPE_CUSTOMER Table Columns names
	private static final String KEY_TYPE_CUSTOMER_ID_TYPE_CUSTOMER = "id_type_customer";
	private static final String KEY_TYPE_CUSTOMER_TYPE_CUSTOMER = "type_customer";
	private static final String KEY_TYPE_CUSTOMER_DESKRIPSI = "deskripsi";

	//CLUSTER table columns names
	private static final String KEY_CLUSTER_ID_CLUSTER = "id_cluster";
	private static final String KEY_CLUSTER_NAMA_CLUSTER = "nama_cluster";
	private static final String KEY_CLUSTER_DESCRIPTION = "description";

	// BRANCH Table Columns names
	private static final String KEY_BRANCH_ID_BRANCH = "id_branch";
	private static final String KEY_BRANCH_KODE_BRANCH = "kode_branch";
	private static final String KEY_BRANCH_DESKRIPSI_BRANCH = "deskripsi";

	// WILAYAH Table Columns names
	private static final String KEY_WILAYAH_ID_WILAYAH = "id_wilayah";
	private static final String KEY_WILAYAH_NAMA_WILAYAH = "nama_wilayah";
	private static final String KEY_WILAYAH_DESKRIPSI_WILAYAH = "deskripsi";
	private static final String KEY_WILAYAH_LATS_WILAYAH = "lats";
	private static final String KEY_WILAYAH_LONGS_WILAYAH = "longs";

	// KEMASAN Table Columns names
	private static final String KEY_KEMASAN_ID_KEMASAN = "id_kemasan";
	private static final String KEY_KEMASAN_NAMA_KEMASAN = "nama_id_kemasan";

	// STOCK_ON_HAND Table Columns names
	private static final String KEY_STOCK_ON_HAND_ID_STOCK_ON_HAND = "id_stock_on_hand";
	private static final String KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND = "nomer_stock_on_hand";
	private static final String KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND_DETAIL = "nomer_stock_on_hand_detail";
	private static final String KEY_STOCK_ON_HAND_DATE_STOCK_ON_HAND = "date_stock_on_hand";
	private static final String KEY_STOCK_ON_HAND_TIME_STOCK_ON_HAND = "time_stock_on_hand";
	private static final String KEY_STOCK_ON_HAND_DESKRIPSI = "deskripsi";
	private static final String KEY_STOCK_ON_HAND_USERNAME = "username";
	private static final String KEY_STOCK_ON_HAND_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_STOCK_ON_HAND_ALAMAT = "alamat";
	private static final String KEY_STOCK_ON_HAND_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_STOCK_ON_HAND_NAMA_PRODUCT = "nama_product";
	private static final String KEY_STOCK_ON_HAND_KODE_PRODUCT = "kode_product";
	private static final String KEY_STOCK_ON_HAND_HARGA_JUAL = "harga_jual";
	private static final String KEY_STOCK_ON_HAND_STOCKPCS = "stockpcs";
	private static final String KEY_STOCK_ON_HAND_STOCKRCG = "stockrenceng";
	private static final String KEY_STOCK_ON_HAND_STOCKPCK = "stockpack";
	private static final String KEY_STOCK_ON_HAND_STOCKDUS = "stockdus";

	// SALES_ORDER Table Columns names
	private static final String KEY_SALES_ORDER_ID_SALES_ORDER = "id_sales_order";
	private static final String KEY_SALES_ORDER_NOMER_ORDER = "nomer_order";
	private static final String KEY_SALES_ORDER_NOMER_REQUEST_LOAD = "nomer_request_load";
	private static final String KEY_SALES_ORDER_NOMER_ORDER_DETAIL = "nomer_order_detail";
	private static final String KEY_SALES_ORDER_DATE_STOCK_ON_HAND = "date_order";
	private static final String KEY_SALES_ORDER_TIME_STOCK_ON_HAND = "time_order";
	private static final String KEY_SALES_ORDER_DESKRIPSI = "deskripsi";
	private static final String KEY_SALES_ORDER_ID_PROMOSI = "id_promosi";
	private static final String KEY_SALES_ORDER_ID_STAFF = "id_staff";
	private static final String KEY_SALES_ORDER_USERNAME = "username";
	private static final String KEY_SALES_ORDER_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_SALES_ORDER_ALAMAT = "alamat";
	private static final String KEY_SALES_ORDER_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_SALES_ORDER_SATUAN_TERKECIL = "satuan_terkecil";
	private static final String KEY_SALES_ORDER_NAMA_PRODUCT = "nama_product";
	private static final String KEY_SALES_ORDER_KODE_PRODUCT = "kode_product";
	private static final String KEY_SALES_ORDER_HARGA_JUAL = "harga_jual";
	private static final String KEY_SALES_ORDER_JUMLAH_ORDER = "jumlah_order";
	private static final String KEY_SALES_ORDER_JUMLAH_ORDER1 = "jumlah_order1";
	private static final String KEY_SALES_ORDER_JUMLAH_ORDER2 = "jumlah_order2";
	private static final String KEY_SALES_ORDER_JUMLAH_ORDER3 = "jumlah_order3";
	private static final String KEY_SALES_ORDER_ID_PRODUCT = "id_product";
	private static final String KEY_SALES_ORDER_TTD1 = "ttd1";
	private static final String KEY_SALES_ORDER_ID_WILAYAH = "id_wilayah";
	private static final String KEY_SALES_ORDER_UOMQTYL1 = "uomqtyl1";
	private static final String KEY_SALES_ORDER_UOMQTYL2 = "uomqtyl2";
	private static final String KEY_SALES_ORDER_UOMQTYL3 = "uomqtyl3";
	private static final String KEY_SALES_ORDER_UOMQTYL4 = "uomqtyl4";

	// RETUR Table Columns names
	private static final String KEY_RETUR_ID_RETUR = "id_retur";
	private static final String KEY_RETUR_NOMER_RETUR = "nomer_retur";
	private static final String KEY_RETUR_NOMER_RETUR_DETAIL = "nomer_retur_detail";
	private static final String KEY_RETUR_DATE_STOCK_ON_HAND = "date_retur";
	private static final String KEY_RETUR_TIME_STOCK_ON_HAND = "time_retur";
	private static final String KEY_RETUR_DESKRIPSI = "deskripsi";
	private static final String KEY_RETUR_ID_PROMOSI = "id_promosi";
	private static final String KEY_RETUR_USERNAME = "username";
	private static final String KEY_RETUR_KODE_CUSTOMER = "kode_customer";
	private static final String KEY_RETUR_ALAMAT = "alamat";
	private static final String KEY_RETUR_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_RETUR_NAMA_PRODUCT = "nama_product";
	private static final String KEY_RETUR_KODE_PRODUCT = "kode_product";
	private static final String KEY_RETUR_HARGA_JUAL = "harga_jual";
	private static final String KEY_RETUR_JUMLAH_RETUR = "jumlah_retur";

	// TRACKING Table Columns names
	private static final String KEY_TRACKING_ID_LOCATOR = "id_locator";
	private static final String KEY_TRACKING_USERNAME = "username";
	private static final String KEY_TRACKING_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_TRACKING_LEVEL = "level";
	private static final String KEY_TRACKING_LATS = "lats";
	private static final String KEY_TRACKING_LONGS = "longs";
	private static final String KEY_TRACKING_ADDRESS = "address";
	private static final String KEY_TRACKING_IMEI = "imei";
	private static final String KEY_TRACKING_MCC = "mcc";
	private static final String KEY_TRACKING_MNC = "mnc";
	private static final String KEY_TRACKING_DATE = "date";
	private static final String KEY_TRACKING_TIME = "time";

	private static final String KEY_TRACKING_LOGS_ID_LOCATOR = "id_locator";
	private static final String KEY_TRACKING_LOGS_USERNAME = "username";
	private static final String KEY_TRACKING_LOGS_NAMA_LENGKAP = "nama_lengkap";
	private static final String KEY_TRACKING_LOGS_LEVEL = "level";
	private static final String KEY_TRACKING_LOGS_LATS = "lats";
	private static final String KEY_TRACKING_LOGS_LONGS = "longs";
	private static final String KEY_TRACKING_LOGS_ADDRESS = "address";
	private static final String KEY_TRACKING_LOGS_IMEI = "imei";
	private static final String KEY_TRACKING_LOGS_MCC = "mcc";
	private static final String KEY_TRACKING_LOGS_MNC = "mnc";
	private static final String KEY_TRACKING_LOGS_DATE = "date";
	private static final String KEY_TRACKING_LOGS_TIME = "time";

	private final ArrayList<Staff> staff_list = new ArrayList<Staff>();
	private final ArrayList<TypeCustomer> type_customer_list = new ArrayList<TypeCustomer>();
	private final ArrayList<Cluster> cluster_list = new ArrayList<Cluster>();

	private final ArrayList<Branch> branch_list = new ArrayList<Branch>();
	private final ArrayList<Kemasan> kemasan_list = new ArrayList<Kemasan>();
	private final ArrayList<Wilayah> wilayah_list = new ArrayList<Wilayah>();
	private final ArrayList<Product> product_list = new ArrayList<Product>();
	private final ArrayList<ProductPrice> product_price_list = new ArrayList<ProductPrice>();
	private final ArrayList<ProductStockVan> productStockVanList = new ArrayList<ProductStockVan>();
	private final ArrayList<StockVan> stock_van_list = new ArrayList<StockVan>();

	private final ArrayList<Customer> customer_list = new ArrayList<Customer>();
	private final ArrayList<Dtg> dtg_list = new ArrayList<Dtg>();
	private final ArrayList<Jadwal> jadwal_list = new ArrayList<Jadwal>();
	//	private final ArrayList<Promosi> promosi_list = new ArrayList<Promosi>();
	private final ArrayList<DisplayProduct> display_product_list = new ArrayList<DisplayProduct>();
	//	private final ArrayList<PhotoPurchase> photo_purchase_list = new ArrayList<PhotoPurchase>();
	private final ArrayList<StockOnHand> stock_on_hand_list = new ArrayList<StockOnHand>();
	private final ArrayList<SalesOrder> sales_order_list = new ArrayList<SalesOrder>();
	private final ArrayList<ReqLoad> reqload_list = new ArrayList<ReqLoad>();
	private final ArrayList<Retur> retur_list = new ArrayList<Retur>();
	private final ArrayList<ProductTarget> productTarget_list = new ArrayList<ProductTarget>();
	private final ArrayList<Penjualan> penjualan_list = new ArrayList<Penjualan>();
	private final ArrayList<PenjualanDetail> penjualan_detail_list = new ArrayList<PenjualanDetail>();
	private final ArrayList<Tracking> tracking_list = new ArrayList<Tracking>();
	private final ArrayList<StaffTemp> staff_temp_list = new ArrayList<StaffTemp>();
	private final ArrayList<TrackingLogs> tracking_logs_list = new ArrayList<TrackingLogs>();

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_TABLE_STAFF = "CREATE TABLE " + TABLE_STAFF + "("
				+ KEY_STAFF_ID_STAFF + " INTEGER PRIMARY KEY,"
				+ KEY_STAFF_NAMA_LENGKAP + " TEXT," + KEY_STAFF_USERNAME
				+ " TEXT," + KEY_STAFF_PASSWORD + " TEXT," + KEY_STAFF_NO_TELP
				+ " TEXT," + KEY_STAFF_LEVEL + " INTEGER,"
				+ KEY_STAFF_ID_BRANCH + " TEXT," + KEY_STAFF_TYPE_CUSTOMER
				+ " TEXT," + KEY_STAFF_ID_DEPO + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_STAFF);

		String CREATE_TABLE_STAFF_TEMP = "CREATE TABLE " + TABLE_STAFF_TEMP
				+ "(" + KEY_STAFF_TEMP_ID_STAFF + " INTEGER PRIMARY KEY,"
				+ KEY_STAFF_TEMP_NAMA_LENGKAP + " TEXT,"
				+ KEY_STAFF_TEMP_USERNAME + " TEXT," + KEY_STAFF_TEMP_PASSWORD
				+ " TEXT," + KEY_STAFF_TEMP_NO_TELP + " TEXT,"
				+ KEY_STAFF_TEMP_LEVEL + " INTEGER," + KEY_STAFF_TEMP_ID_BRANCH
				+ " TEXT," + KEY_STAFF_TEMP_TYPE_CUSTOMER + " TEXT,"
				+ KEY_STAFF_TEMP_ID_DEPO + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_STAFF_TEMP);

		String CREATE_TABLE_TYPE_CUSTOMER = "CREATE TABLE "
				+ TABLE_TYPE_CUSTOMER + "("
				+ KEY_TYPE_CUSTOMER_ID_TYPE_CUSTOMER + " INTEGER PRIMARY KEY,"
				+ KEY_TYPE_CUSTOMER_TYPE_CUSTOMER + " TEXT,"
				+ KEY_TYPE_CUSTOMER_DESKRIPSI + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_TYPE_CUSTOMER);

		String CREATE_TABLE_CLUSTER = "CREATE TABLE "
				+ TABLE_CLUSTER + "("
				+ KEY_CLUSTER_ID_CLUSTER + " INTEGER PRIMARY KEY,"
				+ KEY_CLUSTER_NAMA_CLUSTER + " TEXT,"
				+ KEY_CLUSTER_DESCRIPTION + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_CLUSTER);

		String CREATE_TABLE_BRANCH = "CREATE TABLE " + TABLE_BRANCH + "("
				+ KEY_BRANCH_ID_BRANCH + " INTEGER PRIMARY KEY,"
				+ KEY_BRANCH_KODE_BRANCH + " TEXT,"
				+ KEY_BRANCH_DESKRIPSI_BRANCH + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_BRANCH);

		String CREATE_TABLE_KEMASAN = "CREATE TABLE " + TABLE_KEMASAN + "("
				+ KEY_KEMASAN_ID_KEMASAN + " INTEGER PRIMARY KEY,"
				+ KEY_KEMASAN_NAMA_KEMASAN + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_KEMASAN);

		String CREATE_TABLE_WILAYAH = "CREATE TABLE " + TABLE_WILAYAH + "("
				+ KEY_WILAYAH_ID_WILAYAH + " INTEGER PRIMARY KEY,"
				+ KEY_WILAYAH_NAMA_WILAYAH + " TEXT,"
				+ KEY_WILAYAH_DESKRIPSI_WILAYAH + " TEXT,"
				+ KEY_WILAYAH_LATS_WILAYAH + " TEXT,"
				+ KEY_WILAYAH_LONGS_WILAYAH + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_WILAYAH);

		String CREATE_TABLE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + "("
				+ KEY_PRODUCT_ID_PRODUCT + " INTEGER PRIMARY KEY,"
				+ KEY_PRODUCT_NAMA_PRODUCT + " TEXT,"
				+ KEY_PRODUCT_KODE_PRODUCT + " TEXT,"
				+ KEY_PRODUCT_HARGA_JUAL + " TEXT,"
				+ KEY_PRODUCT_STOCK + " TEXT,"
				+ KEY_PRODUCT_ID_KEMASAN + " TEXT,"
				+ KEY_PRODUCT_FOTO	+ " TEXT,"
				+ KEY_PRODUCT_DESKRIPSI + " TEXT,"
				+ KEY_PRODUCT_UOMQTYL1 + " TEXT,"
				+ KEY_PRODUCT_UOMQTYL2 + " TEXT,"
				+ KEY_PRODUCT_UOMQTYL3 + " TEXT,"
				+ KEY_PRODUCT_UOMQTYL4 + " TEXT,"
				+ KEY_PRODUCT_STATUS + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_PRODUCT);

		String CREATE_TABLE_PRODUCT_PRICE = "CREATE TABLE " + TABLE_PRODUCT_PRICE + "("
				+ KEY_PRODUCT_ID + " INTEGER PRIMARY KEY,"
				+ KEY_PRODUCT_ID_PRODUCT + " TEXT,"
				+ KEY_PRODUCT_PRICELIST + " TEXT,"
				+ KEY_PRODUCT_PRICESTD + " TEXT,"
				+ KEY_PRODUCT_PRICELIMIT + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_PRODUCT_PRICE);

		String CREATE_TABLE_DTG = "CREATE TABLE " + TABLE_DTG + "("
				+ KEY_DTG_ID + " INTEGER PRIMARY KEY,"
				+ KEY_DTG_KODE_CUSTOMER+ " TEXT,"
				+ KEY_DTG_NAMA_TOKO + " TEXT,"
				+ KEY_DTG_NOMER_FAKTUR + " TEXT,"
				+ KEY_DTG_NILAI_FAKTUR + " TEXT,"
				+ KEY_DTG_JML_BELUM_BAYAR + " TEXT,"
				+ KEY_DTG_NO_DTG + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_DTG);

		String CREATE_TABLE_STOK_VAN = "CREATE TABLE " + TABLE_STOCK_VAN + "("
				+ KEY_STOCK_VAN_ID_PRODUCT + " INTEGER PRIMARY KEY,"
				+ KEY_STOCK_VAN_NAMA_PRODUCT + " TEXT,"
				+ KEY_STOCK_VAN_KODE_PRODUCT + " TEXT,"
				+ KEY_STOCK_VAN_HARGA_JUAL + " TEXT,"
				+ KEY_STOCK_VAN_JUMLAH_REQUEST + " INTEGER,"
				+ KEY_STOCK_VAN_JUMLAH_ACCEPT + " INTEGER,"
				+ KEY_STOCK_VAN_JUMLAH_SISA + " INTEGER,"
				+ KEY_STOCK_VAN_ID_KEMASAN + " TEXT," +
				KEY_STOCK_VAN_FOTO + " TEXT," +
				KEY_STOCK_VAN_DESKRIPSI + " TEXT," +
				KEY_STOCK_VAN_NOMER_REQUEST_LOAD + " TEXT" +")";
		db.execSQL(CREATE_TABLE_STOK_VAN);

		String CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TABLE_CUSTOMER + "("
				+ KEY_CUSTOMER_ID_CUSTOMER + " INTEGER PRIMARY KEY,"
				+ KEY_CUSTOMER_KODE_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_EMAIL_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_ALAMAT_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_LATS_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_LONGS_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_NO_TELP_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_ID_WILAYAH_CUSTOMER + " INTEGER,"
				+ KEY_CUSTOMER_FOTO_1_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_FOTO_2_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_FOTO_3_CUSTOMER + " TEXT,"
				+ KEY_CUSTOMER_ID_TYPE_CUSTOMER + " INTEGER, "
				+ KEY_CUSTOMER_BLOKIR + " TEXT, "
				+ KEY_CUSTOMER_DATE + " TEXT, "
				+ KEY_CUSTOMER_STATUS_UPDATE + " TEXT, "
				+ KEY_CUSTOMER_ID_STAFF + " INTEGER,"
				+ KEY_CUSTOMER_NO_KTP + " TEXT,"
				+ KEY_CUSTOMER_TANGGAL_LAHIR + " TEXT,"
				+ KEY_CUSTOMER_NAMA_BANK + " TEXT,"
				+ KEY_CUSTOMER_NO_REKENING + " TEXT,"
				+ KEY_CUSTOMER_ATAS_NAMA + " TEXT,"
				+ KEY_CUSTOMER_NPWP + " TEXT,"
				+ KEY_CUSTOMER_NAMA_PASAR + " TEXT,"
				+ KEY_CUSTOMER_ID_CLUSTER + " INTEGER,"
				+ KEY_CUSTOMER_TELP + " TEXT,"
				+ KEY_CUSTOMER_FAX + " TEXT,"
				+ KEY_CUSTOMER_OMSET + " TEXT,"
				+ KEY_CUSTOMER_CARA_PEMBAYARAN + " TEXT,"
				+ KEY_CUSTOMER_PLAFON_KREDIT + " TEXT,"
				+ KEY_CUSTOMER_TERM_KREDIT + " TEXT,"
				+ KEY_CUSTOMER_NAMA_ISTRI + " TEXT,"
				+ KEY_CUSTOMER_NAMA_ANAK1 + " TEXT,"
				+ KEY_CUSTOMER_NAMA_ANAK2 + " TEXT,"
				+ KEY_CUSTOMER_NAMA_ANAK3 + " TEXT,"
				+ KEY_CUSTOMER_KODE_POS + " TEXT,"
				+ KEY_CUSTOMER_ID_DEPO + " TEXT,"
				+ KEY_CUSTOMER_ISACTIVE + " TEXT,"
				+ KEY_CUSTOMER_DESCRIPTION + " TEXT,"
				+ KEY_CUSTOMER_NAMA_TOKO + " TEXT,"
				+ KEY_CUSTOMER_TTD1 + " TEXT,"
				+ KEY_CUSTOMER_TTD2 + " TEXT"

				+ ")";
		db.execSQL(CREATE_TABLE_CUSTOMER);

		String CREATE_TABLE_JADWAL = "CREATE TABLE " + TABLE_JADWAL + "("
				+ KEY_JADWAL_ID_JADWAL + " INTEGER PRIMARY KEY,"
				+ KEY_JADWAL_KODE_JADWAL + " TEXT,"
				+ KEY_JADWAL_KODE_CUSTOMER + " TEXT,"
				+ KEY_JADWAL_USERNAME + " TEXT,"
				+ KEY_JADWAL_ALAMAT + " TEXT,"
				+ KEY_JADWAL_NAMA_TOKO + " TEXT,"
				+ KEY_JADWAL_ID_WILAYAH + " INTEGER,"
				+ KEY_JADWAL_STATUS + " TEXT,"
				+ KEY_JADWAL_DATE + " TEXT,"
				+ KEY_JADWAL_CHECK_IN + " TEXT,"
				+ KEY_JADWAL_CHECK_OUT + " TEXT,"
				+ KEY_JADWAL_STATUS_UPDATE + " TEXT,"
				+ KEY_JADWAL_KETERANGAN + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_JADWAL);

//		String CREATE_TABLE_PROMOSI = "CREATE TABLE " + TABLE_PROMOSI + "("
//				+ KEY_PROMOSI_ID_PROMOSI + " INTEGER PRIMARY KEY,"
//				+ KEY_PROMOSI_TITLE_PROMOSI + " TEXT,"
//				+ KEY_PROMOSI_JENIS_PROMOSI + " TEXT,"
//				+ KEY_PROMOSI_JUMLAH_DISKON + " INTEGER,"
//				+ KEY_PROMOSI_DESKRIPSI + " TEXT," + KEY_PROMOSI_FOTO
//				+ " TEXT," + KEY_PROMOSI_DATE_TIME + " TEXT" + ")";
//		db.execSQL(CREATE_TABLE_PROMOSI);

		String CREATE_TABLE_DISPLAY_PRODUCT = "CREATE TABLE "
				+ TABLE_DISPLAY_PRODUCT + "("
				+ KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT
				+ " INTEGER PRIMARY KEY," + KEY_DISPLAY_PRODUCT_KODE_CUSTOMER
				+ " TEXT," + KEY_DISPLAY_PRODUCT_NAMA_LENGKAP + " TEXT,"
				+ KEY_DISPLAY_PRODUCT_FOTO + " TEXT,"
				+ KEY_DISPLAY_PRODUCT_NAMA_DISPLAY_PRODUCT + " TEXT,"
				+ KEY_DISPLAY_PRODUCT_DESKRIPSI + " TEXT,"
				+ KEY_DISPLAY_PRODUCT_DATE_TIME + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_DISPLAY_PRODUCT);

//		String CREATE_TABLE_PHOTO_PURCHASE = "CREATE TABLE "
//				+ TABLE_PHOTO_PURCHASE + "("
//				+ KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE
//				+ " INTEGER PRIMARY KEY," + KEY_PHOTO_PURCHASE_KODE_CUSTOMER
//				+ " TEXT," + KEY_PHOTO_PURCHASE_NAMA_LENGKAP + " TEXT,"
//				+ KEY_PHOTO_PURCHASE_FOTO + " TEXT,"
//				+ KEY_PHOTO_PURCHASE_NAMA_PHOTO_PURCHASE + " TEXT,"
//				+ KEY_PHOTO_PURCHASE_DESKRIPSI + " TEXT,"
//				+ KEY_PHOTO_PURCHASE_DATE_TIME + " TEXT" + ")";
//		db.execSQL(CREATE_TABLE_PHOTO_PURCHASE);

		String CREATE_TABLE_STOCK_ON_HAND = "CREATE TABLE "
				+ TABLE_STOCK_ON_HAND + "("
				+ KEY_STOCK_ON_HAND_ID_STOCK_ON_HAND + " INTEGER PRIMARY KEY,"
				+ KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND + " TEXT,"
				+ KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND_DETAIL + " TEXT,"
				+ KEY_STOCK_ON_HAND_DATE_STOCK_ON_HAND + " TEXT,"
				+ KEY_STOCK_ON_HAND_TIME_STOCK_ON_HAND + " TEXT,"
				+ KEY_STOCK_ON_HAND_DESKRIPSI + " TEXT,"
				+ KEY_STOCK_ON_HAND_USERNAME + " TEXT,"
				+ KEY_STOCK_ON_HAND_KODE_CUSTOMER + " TEXT,"
				+ KEY_STOCK_ON_HAND_ALAMAT + " TEXT,"
				+ KEY_STOCK_ON_HAND_NAMA_LENGKAP + " TEXT,"
				+ KEY_STOCK_ON_HAND_NAMA_PRODUCT + " TEXT,"
				+ KEY_STOCK_ON_HAND_KODE_PRODUCT + " TEXT,"
				+ KEY_STOCK_ON_HAND_HARGA_JUAL + " TEXT,"
				+ KEY_STOCK_ON_HAND_STOCKPCS + " TEXT,"
				+ KEY_STOCK_ON_HAND_STOCKRCG + " TEXT,"
				+ KEY_STOCK_ON_HAND_STOCKPCK + " TEXT,"
				+ KEY_STOCK_ON_HAND_STOCKDUS + " TEXT"	+ ")";
		db.execSQL(CREATE_TABLE_STOCK_ON_HAND);

		String CREATE_TABLE_SALES_ORDER = "CREATE TABLE " + TABLE_SALES_ORDER
				+ "(" + KEY_SALES_ORDER_ID_SALES_ORDER
				+ " INTEGER PRIMARY KEY," + KEY_SALES_ORDER_NOMER_ORDER
				+ " TEXT," + KEY_SALES_ORDER_NOMER_ORDER_DETAIL + " TEXT,"
				+ KEY_SALES_ORDER_DATE_STOCK_ON_HAND + " TEXT,"
				+ KEY_SALES_ORDER_TIME_STOCK_ON_HAND + " TEXT,"
				+ KEY_SALES_ORDER_DESKRIPSI + " TEXT,"
				+ KEY_SALES_ORDER_ID_PROMOSI + " INTEGER,"
				+ KEY_SALES_ORDER_USERNAME + " TEXT,"
				+ KEY_SALES_ORDER_KODE_CUSTOMER + " TEXT,"
				+ KEY_SALES_ORDER_ALAMAT + " TEXT,"
				+ KEY_SALES_ORDER_NAMA_LENGKAP + " TEXT,"
				+ KEY_SALES_ORDER_NAMA_PRODUCT + " TEXT,"
				+ KEY_SALES_ORDER_KODE_PRODUCT + " TEXT,"
				+ KEY_SALES_ORDER_HARGA_JUAL + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER1 + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER2 + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER3 + " TEXT,"
				+ KEY_SALES_ORDER_ID_WILAYAH + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_SALES_ORDER);

		String CREATE_TABLE_REQLOAD = "CREATE TABLE " + TABLE_REQLOAD
				+ "("
				+ KEY_SALES_ORDER_ID_SALES_ORDER+ " INTEGER PRIMARY KEY,"
				+ KEY_SALES_ORDER_NOMER_REQUEST_LOAD+ " TEXT,"
				+ KEY_SALES_ORDER_DATE_STOCK_ON_HAND + " TEXT,"
				+ KEY_SALES_ORDER_TIME_STOCK_ON_HAND + " TEXT,"
				+ KEY_SALES_ORDER_ID_PROMOSI + " INTEGER,"
				+ KEY_SALES_ORDER_USERNAME + " TEXT,"
				//+ KEY_SALES_ORDER_KODE_CUSTOMER + " TEXT,"
				//+ KEY_SALES_ORDER_ALAMAT + " TEXT,"
				+ KEY_SALES_ORDER_SATUAN_TERKECIL + " TEXT,"
				+ KEY_SALES_ORDER_NAMA_PRODUCT + " TEXT,"
				//+ KEY_SALES_ORDER_KODE_PRODUCT + " TEXT,"
				//+ KEY_SALES_ORDER_HARGA_JUAL + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER1 + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER2 + " TEXT,"
				+ KEY_SALES_ORDER_JUMLAH_ORDER3 + " TEXT,"
				+ KEY_SALES_ORDER_ID_STAFF + " TEXT,"
				+ KEY_SALES_ORDER_ID_PRODUCT + " TEXT,"
				+ KEY_SALES_ORDER_TTD1 + " TEXT"
				+ ")";
		db.execSQL(CREATE_TABLE_REQLOAD);

		String CREATE_TABLE_RETUR = "CREATE TABLE " + TABLE_RETUR
				+ "(" + KEY_RETUR_ID_RETUR
				+ " INTEGER PRIMARY KEY," + KEY_RETUR_NOMER_RETUR
				+ " TEXT," + KEY_RETUR_NOMER_RETUR_DETAIL + " TEXT,"
				+ KEY_RETUR_DATE_STOCK_ON_HAND + " TEXT,"
				+ KEY_RETUR_TIME_STOCK_ON_HAND + " TEXT,"
				+ KEY_RETUR_DESKRIPSI + " TEXT,"
				+ KEY_RETUR_ID_PROMOSI + " INTEGER,"
				+ KEY_RETUR_USERNAME + " TEXT,"
				+ KEY_RETUR_KODE_CUSTOMER + " TEXT,"
				+ KEY_RETUR_ALAMAT + " TEXT,"
				+ KEY_RETUR_NAMA_LENGKAP + " TEXT,"
				+ KEY_RETUR_NAMA_PRODUCT + " TEXT,"
				+ KEY_RETUR_KODE_PRODUCT + " TEXT,"
				+ KEY_RETUR_HARGA_JUAL + " TEXT,"
				+ KEY_RETUR_JUMLAH_RETUR + " TEXT" + ")";
		db.execSQL(CREATE_TABLE_RETUR);

		String CREATE_TABLE_TRACKING = "CREATE TABLE " + TABLE_TRACKING + "("
				+ KEY_TRACKING_ID_LOCATOR + " INTEGER PRIMARY KEY,"
				+ KEY_TRACKING_USERNAME + " TEXT," + KEY_TRACKING_NAMA_LENGKAP
				+ " TEXT," + KEY_TRACKING_LEVEL + " INTEGER,"
				+ KEY_TRACKING_LATS + " TEXT," + KEY_TRACKING_LONGS + " TEXT,"
				+ KEY_TRACKING_ADDRESS + " TEXT," + KEY_TRACKING_IMEI
				+ " TEXT," + KEY_TRACKING_MCC + " TEXT," + KEY_TRACKING_MNC
				+ " TEXT," + KEY_TRACKING_DATE + " TEXT," + KEY_TRACKING_TIME
				+ " TEXT" + ")";
		db.execSQL(CREATE_TABLE_TRACKING);

		String CREATE_TABLE_PRODUCT_TARGET = "CREATE TABLE "
				+ TABLE_PRODUCT_TARGET + "("
				+ KEY_PRODUCT_TARGET_ID_PRODUCT_TARGET
				+ " INTEGER PRIMARY KEY,"
				+ KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET + " TEXT,"
				+ KEY_PRODUCT_TARGET_CD_PRODUCT_TARGET + " TEXT,"
				+ KEY_PRODUCT_TARGET_CT_PRODUCT_TARGET + " TEXT,"
				+ KEY_PRODUCT_TARGET_UD_PRODUCT_TARGET + " TEXT,"
				+ KEY_PRODUCT_TARGET_UT_PRODUCT_TARGET + " TEXT,"
				+ KEY_PRODUCT_TARGET_CB + " INTEGER," + KEY_PRODUCT_TARGET_UB
				+ " INTEGER," + KEY_PRODUCT_TARGET_ID_STAFF + " INTEGER,"
				+ KEY_PRODUCT_TARGET_ID_CUSTOMER + " INTEGER,"
				+ KEY_PRODUCT_TARGET_ID_PRODUCT + " INTEGER,"
				+ KEY_PRODUCT_TARGET_JUMLAH_TARGET + " INTEGER,"
				+ KEY_PRODUCT_TARGET_JUMLAH_TERJUAL + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE_PRODUCT_TARGET);
		String CREATE_TABLE_PENJUALAN = "CREATE TABLE " + TABLE_PENJUALAN + "("
				+ KEY_PENJUALAN_ID_PENJUALAN + " INTEGER PRIMARY KEY,"
				+ KEY_PENJUALAN_NOMER_PENJUALAN + " TEXT,"
				+ KEY_PENJUALAN_DATE_PENJUALAN + " TEXT,"
				+ KEY_PENJUALAN_TIME_PENJUALAN + " TEXT,"
				+ KEY_PENJUALAN_ID_CUSTOMER + " INTEGER,"
				+ KEY_PENJUALAN_ID_STAFF + " INTEGER," + KEY_PENJUALAN_DISKON
				+ " INTEGER" + ")";
		db.execSQL(CREATE_TABLE_PENJUALAN);
		String CREATE_TABLE_PENJUALAN_DETAIL = "CREATE TABLE "
				+ TABLE_PENJUALAN_DETAIL + "("
				+ KEY_PENJUALAN_DETAIL_ID_PENJUALAN_DETAIL
				+ " INTEGER PRIMARY KEY,"
				+ KEY_PENJUALAN_DETAIL_NOMER_PENJUALAN + " TEXT,"
				+ KEY_PENJUALAN_DETAIL_ID_PRODUCT + " INTEGER,"
				+ KEY_PENJUALAN_DETAIL_JUMLAH + " INTEGER" + ")";
		db.execSQL(CREATE_TABLE_PENJUALAN_DETAIL);

		String CREATE_TABLE_TRACKING_LOGS = "CREATE TABLE " + TABLE_TRACKING_LOGS + "("
				+ KEY_TRACKING_LOGS_ID_LOCATOR + " INTEGER PRIMARY KEY,"
				+ KEY_TRACKING_LOGS_USERNAME + " TEXT," + KEY_TRACKING_LOGS_NAMA_LENGKAP
				+ " TEXT," + KEY_TRACKING_LOGS_LEVEL + " INTEGER,"
				+ KEY_TRACKING_LOGS_LATS + " TEXT," + KEY_TRACKING_LOGS_LONGS + " TEXT,"
				+ KEY_TRACKING_LOGS_ADDRESS + " TEXT," + KEY_TRACKING_LOGS_IMEI
				+ " TEXT," + KEY_TRACKING_LOGS_MCC + " TEXT," + KEY_TRACKING_LOGS_MNC
				+ " TEXT," + KEY_TRACKING_LOGS_DATE + " TEXT," + KEY_TRACKING_LOGS_TIME
				+ " TEXT" + ")";
		db.execSQL(CREATE_TABLE_TRACKING_LOGS);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_BRANCH);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TYPE_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CLUSTER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_WILAYAH);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_KEMASAN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_VAN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_JADWAL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROMOSI);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_PURCHASE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISPLAY_PRODUCT);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STOCK_ON_HAND);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_ORDER);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_REQLOAD);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_RETUR);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKING);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_STAFF_TEMP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT_TARGET);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENJUALAN);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PENJUALAN_DETAIL);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRACKING_LOGS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DTG);
		onCreate(db);
	}

	/**
	 * All CRUD(Create, Read, Update, Delete) Operations
	 */

	// Adding new staff
	public void add_Staff(Staff staff) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_STAFF_ID_STAFF, staff.getId_staff());
		values.put(KEY_STAFF_NAMA_LENGKAP, staff.getNama_lengkap());
		values.put(KEY_STAFF_USERNAME, staff.getUsername());
		values.put(KEY_STAFF_NO_TELP, staff.getNotelp());
		values.put(KEY_STAFF_PASSWORD, staff.getPassword());
		values.put(KEY_STAFF_LEVEL, staff.getLevel());
		values.put(KEY_STAFF_ID_BRANCH, staff.getId_branch());
		values.put(KEY_STAFF_TYPE_CUSTOMER, staff.getId_type_customer());
		values.put(KEY_STAFF_ID_DEPO, staff.getId_depo());
		// Inserting Row
		db.insert(TABLE_STAFF, null, values);
		db.close(); // Closing database connection
	}

	// Adding new staff temp
	public void add_Staff_Temp(StaffTemp staff_temp) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_STAFF_TEMP_ID_STAFF, staff_temp.getId_staff());
		values.put(KEY_STAFF_TEMP_NAMA_LENGKAP, staff_temp.getNama_lengkap());
		values.put(KEY_STAFF_TEMP_USERNAME, staff_temp.getUsername());
		values.put(KEY_STAFF_TEMP_PASSWORD, staff_temp.getPassword());
		values.put(KEY_STAFF_TEMP_NO_TELP, staff_temp.getNotelp());
		values.put(KEY_STAFF_TEMP_PASSWORD, staff_temp.getPassword());
		values.put(KEY_STAFF_TEMP_LEVEL, staff_temp.getLevel());
		values.put(KEY_STAFF_TEMP_ID_BRANCH, staff_temp.getId_branch());
		values.put(KEY_STAFF_TEMP_TYPE_CUSTOMER,staff_temp.getId_type_customer());
		values.put(KEY_STAFF_TEMP_ID_DEPO, staff_temp.getId_depo());
		// Inserting Row
		db.insert(TABLE_STAFF_TEMP, null, values);
		db.close(); // Closing database connection
	}


	// Adding new customer
	public void add_Customer(Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CUSTOMER_ID_CUSTOMER, customer.getId_customer());
		values.put(KEY_CUSTOMER_KODE_CUSTOMER, customer.getKode_customer());
		values.put(KEY_CUSTOMER_EMAIL_CUSTOMER, customer.getEmail());
		values.put(KEY_CUSTOMER_ALAMAT_CUSTOMER, customer.getAlamat());
		values.put(KEY_CUSTOMER_LATS_CUSTOMER, customer.getLats());
		values.put(KEY_CUSTOMER_LONGS_CUSTOMER, customer.getLongs());
		values.put(KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,customer.getNama_lengkap());
		values.put(KEY_CUSTOMER_NO_TELP_CUSTOMER, customer.getNo_telp());
		values.put(KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, customer.getId_wilayah());
		values.put(KEY_CUSTOMER_FOTO_1_CUSTOMER, customer.getFoto_1());
		values.put(KEY_CUSTOMER_FOTO_2_CUSTOMER, customer.getFoto_2());
		values.put(KEY_CUSTOMER_FOTO_3_CUSTOMER, customer.getFoto_3());
		values.put(KEY_CUSTOMER_ID_TYPE_CUSTOMER, customer.getId_type_customer());
		values.put(KEY_CUSTOMER_BLOKIR, customer.getBlokir());
		values.put(KEY_CUSTOMER_DATE, customer.getDate());
		values.put(KEY_CUSTOMER_STATUS_UPDATE, customer.getStatus_update());
		values.put(KEY_CUSTOMER_ID_STAFF, customer.getId_staff());
		values.put(KEY_CUSTOMER_NO_KTP, customer.getNo_ktp());
		values.put(KEY_CUSTOMER_TANGGAL_LAHIR, customer.getTanggal_lahir());
		values.put(KEY_CUSTOMER_NAMA_BANK, customer.getNama_bank());
		values.put(KEY_CUSTOMER_NO_REKENING, customer.getNo_rekening());
		values.put(KEY_CUSTOMER_ATAS_NAMA, customer.getAtas_nama());
		values.put(KEY_CUSTOMER_NPWP, customer.getNpwp());
		values.put(KEY_CUSTOMER_NAMA_PASAR, customer.getNama_pasar());
		values.put(KEY_CUSTOMER_ID_CLUSTER, customer.getId_cluster());
		values.put(KEY_CUSTOMER_TELP, customer.getTelp());
		values.put(KEY_CUSTOMER_FAX, customer.getFax());
		values.put(KEY_CUSTOMER_OMSET, customer.getOmset());
		values.put(KEY_CUSTOMER_CARA_PEMBAYARAN, customer.getCara_pembayaran());
		values.put(KEY_CUSTOMER_PLAFON_KREDIT, customer.getPlafon_kredit());
		values.put(KEY_CUSTOMER_TERM_KREDIT, customer.getTerm_kredit());

		values.put(KEY_CUSTOMER_NAMA_ISTRI, customer.getNama_istri());
		values.put(KEY_CUSTOMER_NAMA_ANAK1, customer.getNama_anak1());
		values.put(KEY_CUSTOMER_NAMA_ANAK2, customer.getNama_anak2());
		values.put(KEY_CUSTOMER_NAMA_ANAK3, customer.getNama_anak3());
		values.put(KEY_CUSTOMER_KODE_POS, customer.getKode_pos());
		values.put(KEY_CUSTOMER_ID_DEPO, customer.getId_depo());
		values.put(KEY_CUSTOMER_ISACTIVE, customer.getIsactive());
		values.put(KEY_CUSTOMER_DESCRIPTION, customer.getDescription());
		values.put(KEY_CUSTOMER_NAMA_TOKO, customer.getNama_toko());
		values.put(KEY_CUSTOMER_TTD1, customer.getTtd1());
		values.put(KEY_CUSTOMER_TTD2, customer.getTtd2());


		// Inserting Row
		db.insert(TABLE_CUSTOMER, null, values);
		db.close(); // Closing database connection
	}

	// Adding new jadwal
	public void add_Jadwal(Jadwal jadwal) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_JADWAL_ID_JADWAL, jadwal.getId_jadwal());
		values.put(KEY_JADWAL_KODE_JADWAL, jadwal.getKode_jadwal());
		values.put(KEY_JADWAL_KODE_CUSTOMER, jadwal.getKode_customer());
		values.put(KEY_JADWAL_USERNAME, jadwal.getUsername());
		values.put(KEY_JADWAL_ALAMAT, jadwal.getAlamat());
		values.put(KEY_JADWAL_NAMA_TOKO, jadwal.getNama_toko());
		values.put(KEY_JADWAL_ID_WILAYAH, jadwal.getId_wilayah());
		values.put(KEY_JADWAL_STATUS, jadwal.getStatus());
		values.put(KEY_JADWAL_DATE, jadwal.getDate());
		values.put(KEY_JADWAL_CHECK_IN, jadwal.getCheckin());
		values.put(KEY_JADWAL_CHECK_OUT, jadwal.getCheckout());
		values.put(KEY_JADWAL_KETERANGAN, jadwal.getKeterangan());
		values.put(KEY_JADWAL_STATUS_UPDATE, jadwal.getStatus_update());
		// Inserting Row
		db.insert(TABLE_JADWAL, null, values);
		db.close(); // Closing database connection
	}

	// Adding new product
	public void add_Product(Product product) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_ID_PRODUCT, product.getId_product());
		values.put(KEY_PRODUCT_NAMA_PRODUCT, product.getNama_product());
		values.put(KEY_PRODUCT_KODE_PRODUCT, product.getKode_product());
		values.put(KEY_PRODUCT_HARGA_JUAL, product.getHarga_jual());
		values.put(KEY_PRODUCT_STOCK, product.getStock());
		values.put(KEY_PRODUCT_ID_KEMASAN, product.getId_kemasan());
		values.put(KEY_PRODUCT_FOTO, product.getFoto());
		values.put(KEY_PRODUCT_DESKRIPSI, product.getDeskripsi());
		values.put(KEY_PRODUCT_UOMQTYL1, product.getUomqtyl1());
		values.put(KEY_PRODUCT_UOMQTYL2, product.getUomqtyl2());
		values.put(KEY_PRODUCT_UOMQTYL3, product.getUomqtyl3());
		values.put(KEY_PRODUCT_UOMQTYL4, product.getUomqtyl4());
		values.put(KEY_PRODUCT_STATUS, product.getStatus());
		// Inserting Row
		db.insert(TABLE_PRODUCT, null, values);
		db.close(); // Closing database connection
	}

	// Adding new product
	public void add_ProductPrice(ProductPrice productprice) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_ID, productprice.getId());
		values.put(KEY_PRODUCT_ID_PRODUCT, productprice.getId_product());
		values.put(KEY_PRODUCT_PRICELIST, productprice.getPricelist());
		values.put(KEY_PRODUCT_PRICESTD, productprice.getPricestd());
		values.put(KEY_PRODUCT_PRICELIMIT, productprice.getPricelimit());
		// Inserting Row
		db.insert(TABLE_PRODUCT_PRICE, null, values);
		db.close(); // Closing database connection
	}

	public void add_Dtg(Dtg dtg) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DTG_ID, dtg.getId());
		values.put(KEY_DTG_KODE_CUSTOMER, dtg.getKode_customer());
		values.put(KEY_DTG_NAMA_TOKO, dtg.getNama_toko());
		values.put(KEY_DTG_NOMER_FAKTUR, dtg.getNo_faktur());
		values.put(KEY_DTG_NILAI_FAKTUR, dtg.getNilai_faktur());
		values.put(KEY_DTG_JML_BELUM_BAYAR, dtg.getJml_belum_bayar());
		values.put(KEY_DTG_NO_DTG, dtg.getNo_dtg());
		// Inserting Row
		db.insert(TABLE_DTG, null, values);
		db.close(); // Closing database connection
	}

	// Adding new StockVan
	public void addStockVan(StockVan stock_van) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_STOCK_VAN_ID_PRODUCT, stock_van.getId_product());
		values.put(KEY_STOCK_VAN_NAMA_PRODUCT, stock_van.getNama_product());
		values.put(KEY_STOCK_VAN_KODE_PRODUCT, stock_van.getKode_product());
		values.put(KEY_STOCK_VAN_HARGA_JUAL, stock_van.getHarga_jual());
		values.put(KEY_STOCK_VAN_JUMLAH_REQUEST, stock_van.getJumlahRequest());
		values.put(KEY_STOCK_VAN_JUMLAH_ACCEPT, stock_van.getJumlahAccept());
		values.put(KEY_STOCK_VAN_JUMLAH_SISA, stock_van.getJumlahSisa());
		values.put(KEY_STOCK_VAN_ID_KEMASAN, stock_van.getIdKemasan());
		values.put(KEY_STOCK_VAN_FOTO, stock_van.getFoto());
		values.put(KEY_STOCK_VAN_DESKRIPSI, stock_van.getDeskripsi());
		values.put(KEY_STOCK_VAN_NOMER_REQUEST_LOAD, stock_van.getNomer_request_load());
		// Inserting Row
		db.insert(TABLE_STOCK_VAN, null, values);
		db.close(); // Closing database connection
	}

	// Adding new ProductTarget
	public void addProductTarget(ProductTarget productTarget) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_TARGET_ID_PRODUCT_TARGET,
				productTarget.getId_product_target());
		values.put(KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET,
				productTarget.getNomer_product_target());
		values.put(KEY_PRODUCT_TARGET_CD_PRODUCT_TARGET,
				productTarget.getCreated_date_product_target());
		values.put(KEY_PRODUCT_TARGET_CT_PRODUCT_TARGET,
				productTarget.getCreated_time_product_target());
		values.put(KEY_PRODUCT_TARGET_UD_PRODUCT_TARGET,
				productTarget.getUpdated_date_product_target());
		values.put(KEY_PRODUCT_TARGET_UT_PRODUCT_TARGET,
				productTarget.getUpdated_time_product_target());
		values.put(KEY_PRODUCT_TARGET_CB, productTarget.getCreated_by());
		values.put(KEY_PRODUCT_TARGET_UB, productTarget.getUpdated_by());
		values.put(KEY_PRODUCT_TARGET_ID_STAFF, productTarget.getId_staff());
		values.put(KEY_PRODUCT_TARGET_ID_CUSTOMER,
				productTarget.getId_customer());
		values.put(KEY_PRODUCT_TARGET_ID_PRODUCT, productTarget.getId_product());
		values.put(KEY_PRODUCT_TARGET_JUMLAH_TARGET,
				productTarget.getJumlah_target());
		values.put(KEY_PRODUCT_TARGET_JUMLAH_TERJUAL,
				productTarget.getJumlah_terjual());
		// Inserting Row
		db.insert(TABLE_PRODUCT_TARGET, null, values);
		db.close(); // Closing database connection
	}

//	// Adding new promosi
//	public void add_Promosi(Promosi promosi) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(KEY_PROMOSI_ID_PROMOSI, promosi.getId_promosi());
//		values.put(KEY_PROMOSI_TITLE_PROMOSI, promosi.getTitle_promosi());
//		values.put(KEY_PROMOSI_JENIS_PROMOSI, promosi.getJenis_promosi());
//		values.put(KEY_PROMOSI_JUMLAH_DISKON, promosi.getJumlah_diskon());
//		values.put(KEY_PROMOSI_DESKRIPSI, promosi.getDeskripsi());
//		values.put(KEY_PROMOSI_FOTO, promosi.getFoto());
//		values.put(KEY_PROMOSI_DATE_TIME, promosi.getDatetime());
//
//		// Inserting Row
//		db.insert(TABLE_PROMOSI, null, values);
//		db.close(); // Closing database connection
//	}

	// Adding new displayProduct
	public void add_Display_Product(DisplayProduct displayProduct) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT,
				displayProduct.getId_display_product());
		values.put(KEY_DISPLAY_PRODUCT_KODE_CUSTOMER,
				displayProduct.getKode_customer());
		values.put(KEY_DISPLAY_PRODUCT_NAMA_LENGKAP,
				displayProduct.getNama_lengkap());
		values.put(KEY_DISPLAY_PRODUCT_FOTO, displayProduct.getFoto());
		values.put(KEY_DISPLAY_PRODUCT_NAMA_DISPLAY_PRODUCT,
				displayProduct.getNama_display_product());
		values.put(KEY_DISPLAY_PRODUCT_DESKRIPSI, displayProduct.getDeskripsi());
		values.put(KEY_DISPLAY_PRODUCT_DATE_TIME, displayProduct.getDatetime());

		// Inserting Row
		db.insert(TABLE_DISPLAY_PRODUCT, null, values);
		db.close(); // Closing database connection
	}

//	// Adding new displayProduct
//	public void add_Photo_Purchase(PhotoPurchase photoPurchase) {
//		SQLiteDatabase db = this.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE,
//				photoPurchase.getId_photo_purchase());
//		values.put(KEY_PHOTO_PURCHASE_KODE_CUSTOMER,
//				photoPurchase.getKode_customer());
//		values.put(KEY_PHOTO_PURCHASE_NAMA_LENGKAP,
//				photoPurchase.getNama_lengkap());
//		values.put(KEY_PHOTO_PURCHASE_FOTO, photoPurchase.getFoto());
//		values.put(KEY_PHOTO_PURCHASE_NAMA_PHOTO_PURCHASE,
//				photoPurchase.getNama_photo_purchase());
//		values.put(KEY_PHOTO_PURCHASE_DESKRIPSI, photoPurchase.getDeskripsi());
//		values.put(KEY_PHOTO_PURCHASE_DATE_TIME, photoPurchase.getDatetime());
//
//		// Inserting Row
//		db.insert(TABLE_PHOTO_PURCHASE, null, values);
//		db.close(); // Closing database connection
//
//	}

	// Adding new typeCustomer
	public void add_TypeCustomer(TypeCustomer typeCustomer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TYPE_CUSTOMER_ID_TYPE_CUSTOMER,
				typeCustomer.getId_type_customer());
		values.put(KEY_TYPE_CUSTOMER_TYPE_CUSTOMER,
				typeCustomer.getType_customer());
		values.put(KEY_TYPE_CUSTOMER_DESKRIPSI, typeCustomer.getDeskripsi());
		// Inserting Row
		db.insert(TABLE_TYPE_CUSTOMER, null, values);
		db.close(); // Closing database connection
	}

	// Adding new Cluster
	public void add_Cluster(Cluster cluster) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CLUSTER_ID_CLUSTER,
				cluster.getId_cluster());
		values.put(KEY_CLUSTER_NAMA_CLUSTER,
				cluster.getNama_cluster());
		values.put(KEY_CLUSTER_DESCRIPTION, cluster.getDescription());
		// Inserting Row
		db.insert(TABLE_CLUSTER, null, values);
		db.close(); // Closing database connection
	}

	// Adding new branch
	public void add_Branch(Branch branch) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_BRANCH_ID_BRANCH, branch.getId_branch());
		values.put(KEY_BRANCH_KODE_BRANCH, branch.getKode_branch());
		values.put(KEY_BRANCH_DESKRIPSI_BRANCH, branch.getDeskripsi());
		// Inserting Row
		db.insert(TABLE_BRANCH, null, values);
		db.close(); // Closing database connection
	}

	// adding new kemasan
	public void add_Kemasan(Kemasan kemasan) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_KEMASAN_ID_KEMASAN, kemasan.getId_kemasan());
		values.put(KEY_KEMASAN_NAMA_KEMASAN, kemasan.getNama_id_kemasan());
		// Inserting Row
		db.insert(TABLE_KEMASAN, null, values);
		db.close(); // Closing database connection
	}

	// Adding new branch
	public void add_Wilayah(Wilayah wilayah) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_WILAYAH_ID_WILAYAH, wilayah.getId_wilayah());
		values.put(KEY_WILAYAH_NAMA_WILAYAH, wilayah.getNama_wilayah());
		values.put(KEY_WILAYAH_DESKRIPSI_WILAYAH, wilayah.getDeskripsi());
		values.put(KEY_WILAYAH_LATS_WILAYAH, wilayah.getLats());
		values.put(KEY_WILAYAH_LONGS_WILAYAH, wilayah.getLongs());

		// Inserting Row
		db.insert(TABLE_WILAYAH, null, values);
		db.close(); // Closing database connection
	}

	// Adding new StockOnHand
	public void add_StockOnHand(StockOnHand stockOnHand) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_STOCK_ON_HAND_ID_STOCK_ON_HAND,
				stockOnHand.getId_stock_on_hand());
		values.put(KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND,
				stockOnHand.getNomer_stock_on_hand());
		values.put(KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND_DETAIL,
				stockOnHand.getNomer_stock_on_hand_detail());
		values.put(KEY_STOCK_ON_HAND_DATE_STOCK_ON_HAND,
				stockOnHand.getDate_stock_on_hand());
		values.put(KEY_STOCK_ON_HAND_TIME_STOCK_ON_HAND,
				stockOnHand.getTime_stock_on_hand());
		values.put(KEY_STOCK_ON_HAND_DESKRIPSI, stockOnHand.getDeskripsi());
		values.put(KEY_STOCK_ON_HAND_USERNAME, stockOnHand.getUsername());
		values.put(KEY_STOCK_ON_HAND_KODE_CUSTOMER,
				stockOnHand.getKode_customer());
		values.put(KEY_STOCK_ON_HAND_ALAMAT, stockOnHand.getAlamat());
		values.put(KEY_STOCK_ON_HAND_NAMA_LENGKAP,
				stockOnHand.getNama_lengkap());
		values.put(KEY_STOCK_ON_HAND_NAMA_PRODUCT,
				stockOnHand.getNama_product());
		values.put(KEY_STOCK_ON_HAND_KODE_PRODUCT,
				stockOnHand.getKode_product());
		values.put(KEY_STOCK_ON_HAND_HARGA_JUAL, stockOnHand.getHarga_jual());
		values.put(KEY_STOCK_ON_HAND_STOCKPCS, stockOnHand.getStockpcs());
		values.put(KEY_STOCK_ON_HAND_STOCKRCG, stockOnHand.getStockrcg());
		values.put(KEY_STOCK_ON_HAND_STOCKPCK, stockOnHand.getStockpck());
		values.put(KEY_STOCK_ON_HAND_STOCKDUS, stockOnHand.getStockdus());

		// Inserting Row
		db.insert(TABLE_STOCK_ON_HAND, null, values);
		db.close(); // Closing database connection

	}

	// Adding new SalesOrder
	public void add_SalesOrder(SalesOrder salesOrder) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SALES_ORDER_ID_SALES_ORDER,
				salesOrder.getId_sales_order());
		values.put(KEY_SALES_ORDER_NOMER_ORDER, salesOrder.getNomer_order());
		values.put(KEY_SALES_ORDER_NOMER_ORDER_DETAIL,
				salesOrder.getNomer_order_detail());
		values.put(KEY_SALES_ORDER_DATE_STOCK_ON_HAND,
				salesOrder.getDate_order());
		values.put(KEY_SALES_ORDER_TIME_STOCK_ON_HAND,
				salesOrder.getTime_order());
		values.put(KEY_SALES_ORDER_DESKRIPSI, salesOrder.getDeskripsi());
		values.put(KEY_SALES_ORDER_ID_PROMOSI, salesOrder.getId_promosi());
		values.put(KEY_SALES_ORDER_USERNAME, salesOrder.getUsername());
		values.put(KEY_SALES_ORDER_KODE_CUSTOMER, salesOrder.getKode_customer());
		values.put(KEY_SALES_ORDER_ALAMAT, salesOrder.getAlamat());
		values.put(KEY_SALES_ORDER_NAMA_LENGKAP, salesOrder.getNama_lengkap());
		values.put(KEY_SALES_ORDER_NAMA_PRODUCT, salesOrder.getNama_product());
		values.put(KEY_SALES_ORDER_KODE_PRODUCT, salesOrder.getKode_product());
		values.put(KEY_SALES_ORDER_HARGA_JUAL, salesOrder.getHarga_jual());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER, salesOrder.getJumlah_order());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER1, salesOrder.getJumlah_order1());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER2, salesOrder.getJumlah_order2());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER3, salesOrder.getJumlah_order3());
		values.put(KEY_SALES_ORDER_ID_WILAYAH, salesOrder.getId_wilayah());
		// Inserting Row
		db.insert(TABLE_SALES_ORDER, null, values);
		db.close(); // Closing database connection
	}
	// Adding new reqload
	public void add_ReqLoad(ReqLoad reqLoad) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_SALES_ORDER_ID_SALES_ORDER, reqLoad.getId_sales_order());
		values.put(KEY_SALES_ORDER_NOMER_REQUEST_LOAD, reqLoad.getNomer_request_load());
		values.put(KEY_SALES_ORDER_DATE_STOCK_ON_HAND, reqLoad.getDate_order());
		values.put(KEY_SALES_ORDER_TIME_STOCK_ON_HAND, reqLoad.getTime_order());
		values.put(KEY_SALES_ORDER_ID_PROMOSI, reqLoad.getId_promosi());
		values.put(KEY_SALES_ORDER_USERNAME, reqLoad.getUsername());
		values.put(KEY_SALES_ORDER_SATUAN_TERKECIL, reqLoad.getSatuan_terkecil());
		values.put(KEY_SALES_ORDER_NAMA_PRODUCT, reqLoad.getNama_product());
		//values.put(KEY_SALES_ORDER_HARGA_JUAL, reqLoad.getHarga_jual());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER, reqLoad.getJumlah_order());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER1, reqLoad.getJumlah_order1());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER2, reqLoad.getJumlah_order2());
		values.put(KEY_SALES_ORDER_JUMLAH_ORDER3, reqLoad.getJumlah_order3());
		values.put(KEY_SALES_ORDER_ID_STAFF, reqLoad.getId_staff());
		values.put(KEY_SALES_ORDER_ID_PRODUCT, reqLoad.getId_product());
		values.put(KEY_SALES_ORDER_TTD1, reqLoad.getTtd1());
		// Inserting Row
		db.insert(TABLE_REQLOAD, null, values);
		db.close(); // Closing database connection
	}

	// Adding new RETUR
	public void add_Retur(Retur retur) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_RETUR_ID_RETUR,
				retur.getId_retur());
		values.put(KEY_RETUR_NOMER_RETUR, retur.getNomer_retur());
		values.put(KEY_RETUR_NOMER_RETUR_DETAIL,
				retur.getNomer_retur_detail());
		values.put(KEY_RETUR_DATE_STOCK_ON_HAND,
				retur.getDate_retur());
		values.put(KEY_RETUR_TIME_STOCK_ON_HAND,
				retur.getTime_retur());
		values.put(KEY_RETUR_DESKRIPSI, retur.getDeskripsi());
		values.put(KEY_RETUR_ID_PROMOSI, retur.getId_promosi());
		values.put(KEY_RETUR_USERNAME, retur.getUsername());
		values.put(KEY_RETUR_KODE_CUSTOMER, retur.getKode_customer());
		values.put(KEY_RETUR_ALAMAT, retur.getAlamat());
		values.put(KEY_RETUR_NAMA_LENGKAP, retur.getNama_lengkap());
		values.put(KEY_RETUR_NAMA_PRODUCT, retur.getNama_product());
		values.put(KEY_RETUR_KODE_PRODUCT, retur.getKode_product());
		values.put(KEY_RETUR_HARGA_JUAL, retur.getHarga_jual());
		values.put(KEY_RETUR_JUMLAH_RETUR, retur.getJumlah_retur());
		// Inserting Row
		db.insert(TABLE_RETUR, null, values);
		db.close(); // Closing database connection
	}

	// Adding new Penjualan
	public void addPenjualan(Penjualan penjualan) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PENJUALAN_ID_PENJUALAN, penjualan.getId_penjualan());
		values.put(KEY_PENJUALAN_NOMER_PENJUALAN,
				penjualan.getNomer_product_terjual());
		values.put(KEY_PENJUALAN_DATE_PENJUALAN,
				penjualan.getDate_product_terjual());
		values.put(KEY_PENJUALAN_TIME_PENJUALAN,
				penjualan.getTime_product_terjual());
		values.put(KEY_PENJUALAN_ID_CUSTOMER, penjualan.getId_customer());
		values.put(KEY_PENJUALAN_ID_STAFF, penjualan.getId_staff());
		values.put(KEY_PENJUALAN_DISKON, penjualan.getDiskon());
		// Inserting Row
		db.insert(TABLE_PENJUALAN, null, values);
		db.close(); // Closing database connection
	}
	// Adding new PenjualanDetail
	public void addPenjualanDetail(PenjualanDetail penjualanDetail) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PENJUALAN_DETAIL_ID_PENJUALAN_DETAIL,
				penjualanDetail.getId_penjualan_detail());
		values.put(KEY_PENJUALAN_DETAIL_ID_PRODUCT,
				penjualanDetail.getIdProduct());
		values.put(KEY_PENJUALAN_DETAIL_JUMLAH, penjualanDetail.getJumlah());
		values.put(KEY_PENJUALAN_DETAIL_NOMER_PENJUALAN,
				penjualanDetail.getNomer_product_terjual());
		// Inserting Row
		db.insert(TABLE_PENJUALAN_DETAIL, null, values);
		db.close(); // Closing database connection
	}

	// Adding new Tracking
	public void add_Tracking(Tracking tracking) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TRACKING_ID_LOCATOR, tracking.getId_locator());
		values.put(KEY_TRACKING_USERNAME, tracking.getUsername());
		values.put(KEY_TRACKING_NAMA_LENGKAP, tracking.getNama_lengkap());
		values.put(KEY_TRACKING_LEVEL, tracking.getLevel());
		values.put(KEY_TRACKING_LATS, tracking.getLats());
		values.put(KEY_TRACKING_LONGS, tracking.getLongs());
		values.put(KEY_TRACKING_ADDRESS, tracking.getAddress());
		values.put(KEY_TRACKING_IMEI, tracking.getImei());
		values.put(KEY_TRACKING_MCC, tracking.getMcc());
		values.put(KEY_TRACKING_MNC, tracking.getMnc());
		values.put(KEY_TRACKING_DATE, tracking.getDate());
		values.put(KEY_TRACKING_TIME, tracking.getTime());
		// Inserting Row
		db.insert(TABLE_TRACKING, null, values);



		db.close(); // Closing database connection
	}

	// Adding new Tracking
	public void add_TrackingLogs(TrackingLogs tracking) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_TRACKING_LOGS_ID_LOCATOR, tracking.getId_locator());
		values.put(KEY_TRACKING_LOGS_USERNAME, tracking.getUsername());
		values.put(KEY_TRACKING_LOGS_NAMA_LENGKAP, tracking.getNama_lengkap());
		values.put(KEY_TRACKING_LOGS_LEVEL, tracking.getLevel());
		values.put(KEY_TRACKING_LOGS_LATS, tracking.getLats());
		values.put(KEY_TRACKING_LOGS_LONGS, tracking.getLongs());
		values.put(KEY_TRACKING_LOGS_ADDRESS, tracking.getAddress());
		values.put(KEY_TRACKING_LOGS_IMEI, tracking.getImei());
		values.put(KEY_TRACKING_LOGS_MCC, tracking.getMcc());
		values.put(KEY_TRACKING_LOGS_MNC, tracking.getMnc());
		values.put(KEY_TRACKING_LOGS_DATE, tracking.getDate());
		values.put(KEY_TRACKING_LOGS_TIME, tracking.getTime());
		// Inserting Row
		db.insert(TABLE_TRACKING_LOGS, null, values);

		db.close(); // Closing database connection
	}

	// Getting single Staff
	public Staff getStaff(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STAFF, new String[] {
						KEY_STAFF_ID_STAFF, KEY_STAFF_NAMA_LENGKAP, KEY_STAFF_USERNAME,
						KEY_STAFF_PASSWORD, KEY_STAFF_NO_TELP, KEY_STAFF_LEVEL,
						KEY_STAFF_ID_BRANCH, KEY_STAFF_TYPE_CUSTOMER,
						KEY_STAFF_ID_DEPO }, KEY_STAFF_ID_STAFF + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Staff staff = new Staff(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getInt(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8));
		// return staff
		cursor.close();
		db.close();

		return staff;
	}

	// Getting single Customer untuk nampilin dihalaman awal
	public Customer getCustomer(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
						KEY_CUSTOMER_ID_CUSTOMER, KEY_CUSTOMER_KODE_CUSTOMER,
						KEY_CUSTOMER_EMAIL_CUSTOMER, KEY_CUSTOMER_ALAMAT_CUSTOMER,
						KEY_CUSTOMER_LATS_CUSTOMER, KEY_CUSTOMER_LONGS_CUSTOMER,
						KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
						KEY_CUSTOMER_NO_TELP_CUSTOMER,
						KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, KEY_CUSTOMER_FOTO_1_CUSTOMER,
						KEY_CUSTOMER_FOTO_2_CUSTOMER, KEY_CUSTOMER_FOTO_3_CUSTOMER,
						KEY_CUSTOMER_ID_TYPE_CUSTOMER, KEY_CUSTOMER_BLOKIR,
						KEY_CUSTOMER_DATE, KEY_CUSTOMER_STATUS_UPDATE,
						KEY_CUSTOMER_ID_STAFF, KEY_CUSTOMER_NO_KTP, KEY_CUSTOMER_TANGGAL_LAHIR,
						KEY_CUSTOMER_NAMA_BANK, KEY_CUSTOMER_NO_REKENING, KEY_CUSTOMER_ATAS_NAMA,
						KEY_CUSTOMER_NPWP, KEY_CUSTOMER_NAMA_PASAR, KEY_CUSTOMER_ID_CLUSTER, KEY_CUSTOMER_TELP, KEY_CUSTOMER_FAX,
						KEY_CUSTOMER_OMSET, KEY_CUSTOMER_CARA_PEMBAYARAN, KEY_CUSTOMER_PLAFON_KREDIT,
						KEY_CUSTOMER_TERM_KREDIT,KEY_CUSTOMER_NAMA_ISTRI, KEY_CUSTOMER_NAMA_ANAK1, KEY_CUSTOMER_NAMA_ANAK2,
						KEY_CUSTOMER_NAMA_ANAK3,KEY_CUSTOMER_KODE_POS,KEY_CUSTOMER_ID_DEPO,KEY_CUSTOMER_ISACTIVE,KEY_CUSTOMER_DESCRIPTION,
						KEY_CUSTOMER_NAMA_TOKO, KEY_CUSTOMER_TTD1, KEY_CUSTOMER_TTD2},
				KEY_CUSTOMER_ID_CUSTOMER + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);


		if (cursor != null)
			cursor.moveToFirst();

		Customer customer = new Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getInt(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getInt(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getInt(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), cursor.getString(34),
				cursor.getString(35), cursor.getInt(36),cursor.getString(37),
				cursor.getString(38), cursor.getString(39), cursor.getString(40),
				cursor.getString(41));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	public Customer getCustomer1(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
						KEY_CUSTOMER_ID_CUSTOMER, KEY_CUSTOMER_KODE_CUSTOMER,
						KEY_CUSTOMER_EMAIL_CUSTOMER, KEY_CUSTOMER_ALAMAT_CUSTOMER,
						KEY_CUSTOMER_LATS_CUSTOMER, KEY_CUSTOMER_LONGS_CUSTOMER,
						KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
						KEY_CUSTOMER_NO_TELP_CUSTOMER,
						KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, KEY_CUSTOMER_FOTO_1_CUSTOMER,
						KEY_CUSTOMER_FOTO_2_CUSTOMER, KEY_CUSTOMER_FOTO_3_CUSTOMER,
						KEY_CUSTOMER_ID_TYPE_CUSTOMER, KEY_CUSTOMER_BLOKIR,
						KEY_CUSTOMER_DATE, KEY_CUSTOMER_STATUS_UPDATE,
						KEY_CUSTOMER_ID_STAFF, KEY_CUSTOMER_NO_KTP, KEY_CUSTOMER_TANGGAL_LAHIR,
						KEY_CUSTOMER_NAMA_BANK, KEY_CUSTOMER_NO_REKENING, KEY_CUSTOMER_ATAS_NAMA,
						KEY_CUSTOMER_NPWP, KEY_CUSTOMER_NAMA_PASAR, KEY_CUSTOMER_ID_CLUSTER, KEY_CUSTOMER_TELP, KEY_CUSTOMER_FAX,
						KEY_CUSTOMER_OMSET, KEY_CUSTOMER_CARA_PEMBAYARAN, KEY_CUSTOMER_PLAFON_KREDIT,
						KEY_CUSTOMER_TERM_KREDIT,KEY_CUSTOMER_NAMA_ISTRI, KEY_CUSTOMER_NAMA_ANAK1, KEY_CUSTOMER_NAMA_ANAK2,
						KEY_CUSTOMER_NAMA_ANAK3,KEY_CUSTOMER_KODE_POS,KEY_CUSTOMER_ID_DEPO,KEY_CUSTOMER_ISACTIVE,KEY_CUSTOMER_DESCRIPTION,
						KEY_CUSTOMER_NAMA_TOKO, KEY_CUSTOMER_TTD1, KEY_CUSTOMER_TTD2},
				KEY_CUSTOMER_KODE_CUSTOMER + "=?",
				new String[] { String.valueOf(kode_customer) }, null, null, null, null);


		if (cursor != null)
			cursor.moveToFirst();

		Customer customer = new Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getInt(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getInt(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getInt(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), cursor.getString(34),
				cursor.getString(35), cursor.getInt(36),cursor.getString(37),
				cursor.getString(38), cursor.getString(39), cursor.getString(40),
				cursor.getString(41));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	// Getting single Customer
	public Customer getCustomer(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
						KEY_CUSTOMER_ID_CUSTOMER, KEY_CUSTOMER_KODE_CUSTOMER,
						KEY_CUSTOMER_EMAIL_CUSTOMER, KEY_CUSTOMER_ALAMAT_CUSTOMER,
						KEY_CUSTOMER_LATS_CUSTOMER, KEY_CUSTOMER_LONGS_CUSTOMER,
						KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
						KEY_CUSTOMER_NO_TELP_CUSTOMER,
						KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, KEY_CUSTOMER_FOTO_1_CUSTOMER,
						KEY_CUSTOMER_FOTO_2_CUSTOMER, KEY_CUSTOMER_FOTO_3_CUSTOMER,
						KEY_CUSTOMER_ID_TYPE_CUSTOMER, KEY_CUSTOMER_BLOKIR,
						KEY_CUSTOMER_DATE, KEY_CUSTOMER_STATUS_UPDATE,
						KEY_CUSTOMER_ID_STAFF, KEY_CUSTOMER_NO_KTP, KEY_CUSTOMER_TANGGAL_LAHIR,
						KEY_CUSTOMER_NAMA_BANK, KEY_CUSTOMER_NO_REKENING, KEY_CUSTOMER_ATAS_NAMA,
						KEY_CUSTOMER_NPWP,KEY_CUSTOMER_NAMA_PASAR, KEY_CUSTOMER_ID_CLUSTER,
						KEY_CUSTOMER_TELP, KEY_CUSTOMER_FAX, KEY_CUSTOMER_OMSET,
						KEY_CUSTOMER_CARA_PEMBAYARAN, KEY_CUSTOMER_PLAFON_KREDIT,
						KEY_CUSTOMER_TERM_KREDIT,KEY_CUSTOMER_NAMA_ISTRI, KEY_CUSTOMER_NAMA_ANAK1, KEY_CUSTOMER_NAMA_ANAK2,
						KEY_CUSTOMER_NAMA_ANAK3, KEY_CUSTOMER_KODE_POS, KEY_CUSTOMER_ID_DEPO, KEY_CUSTOMER_ISACTIVE,
						KEY_CUSTOMER_DESCRIPTION, KEY_CUSTOMER_NAMA_TOKO, KEY_CUSTOMER_TTD1, KEY_CUSTOMER_TTD2}, KEY_CUSTOMER_KODE_CUSTOMER + "=?",
				new String[] { kode_customer }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Customer customer = new Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getInt(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getInt(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getInt(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), cursor.getString(34),
				cursor.getString(35),cursor.getInt(36),cursor.getString(37),
				cursor.getString(38),cursor.getString(39), cursor.getString(40),
				cursor.getString(41));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	public Customer getCustomer2(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
						KEY_CUSTOMER_ID_CUSTOMER, KEY_CUSTOMER_KODE_CUSTOMER,
						KEY_CUSTOMER_EMAIL_CUSTOMER, KEY_CUSTOMER_ALAMAT_CUSTOMER,
						KEY_CUSTOMER_LATS_CUSTOMER, KEY_CUSTOMER_LONGS_CUSTOMER,
						KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
						KEY_CUSTOMER_NO_TELP_CUSTOMER,
						KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, KEY_CUSTOMER_FOTO_1_CUSTOMER,
						KEY_CUSTOMER_FOTO_2_CUSTOMER, KEY_CUSTOMER_FOTO_3_CUSTOMER,
						KEY_CUSTOMER_ID_TYPE_CUSTOMER, KEY_CUSTOMER_BLOKIR,
						KEY_CUSTOMER_DATE, KEY_CUSTOMER_STATUS_UPDATE,
						KEY_CUSTOMER_ID_STAFF, KEY_CUSTOMER_NO_KTP, KEY_CUSTOMER_TANGGAL_LAHIR,
						KEY_CUSTOMER_NAMA_BANK, KEY_CUSTOMER_NO_REKENING, KEY_CUSTOMER_ATAS_NAMA,
						KEY_CUSTOMER_NPWP,KEY_CUSTOMER_NAMA_PASAR, KEY_CUSTOMER_ID_CLUSTER,
						KEY_CUSTOMER_TELP, KEY_CUSTOMER_FAX, KEY_CUSTOMER_OMSET,
						KEY_CUSTOMER_CARA_PEMBAYARAN, KEY_CUSTOMER_PLAFON_KREDIT,
						KEY_CUSTOMER_TERM_KREDIT,KEY_CUSTOMER_NAMA_ISTRI, KEY_CUSTOMER_NAMA_ANAK1, KEY_CUSTOMER_NAMA_ANAK2,
						KEY_CUSTOMER_NAMA_ANAK3, KEY_CUSTOMER_KODE_POS, KEY_CUSTOMER_ID_DEPO, KEY_CUSTOMER_ISACTIVE,
						KEY_CUSTOMER_DESCRIPTION, KEY_CUSTOMER_NAMA_TOKO, KEY_CUSTOMER_TTD1, KEY_CUSTOMER_TTD2}, KEY_CUSTOMER_KODE_CUSTOMER + "=?",
				new String[] { kode_customer }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Customer customer = new Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getInt(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getInt(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getInt(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), cursor.getString(34),
				cursor.getString(35),cursor.getInt(36),cursor.getString(37),
				cursor.getString(38),cursor.getString(39), cursor.getString(40),
				cursor.getString(41));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	// Getting single Customer
	public Customer getCustomerByKodeCustomer(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
						KEY_CUSTOMER_ID_CUSTOMER, KEY_CUSTOMER_KODE_CUSTOMER,
						KEY_CUSTOMER_EMAIL_CUSTOMER, KEY_CUSTOMER_ALAMAT_CUSTOMER,
						KEY_CUSTOMER_LATS_CUSTOMER, KEY_CUSTOMER_LONGS_CUSTOMER,
						KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
						KEY_CUSTOMER_NO_TELP_CUSTOMER,
						KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, KEY_CUSTOMER_FOTO_1_CUSTOMER,
						KEY_CUSTOMER_FOTO_2_CUSTOMER, KEY_CUSTOMER_FOTO_3_CUSTOMER,
						KEY_CUSTOMER_ID_TYPE_CUSTOMER, KEY_CUSTOMER_BLOKIR,
						KEY_CUSTOMER_DATE, KEY_CUSTOMER_STATUS_UPDATE,
						KEY_CUSTOMER_ID_STAFF, KEY_CUSTOMER_NO_KTP, KEY_CUSTOMER_TANGGAL_LAHIR,
						KEY_CUSTOMER_NAMA_BANK, KEY_CUSTOMER_NO_REKENING, KEY_CUSTOMER_ATAS_NAMA,
						KEY_CUSTOMER_NPWP,KEY_CUSTOMER_NAMA_PASAR, KEY_CUSTOMER_ID_CLUSTER,
						KEY_CUSTOMER_TELP, KEY_CUSTOMER_FAX, KEY_CUSTOMER_OMSET,
						KEY_CUSTOMER_CARA_PEMBAYARAN, KEY_CUSTOMER_PLAFON_KREDIT,
						KEY_CUSTOMER_TERM_KREDIT,KEY_CUSTOMER_NAMA_ISTRI, KEY_CUSTOMER_NAMA_ANAK1, KEY_CUSTOMER_NAMA_ANAK2,
						KEY_CUSTOMER_NAMA_ANAK3,KEY_CUSTOMER_KODE_POS, KEY_CUSTOMER_ID_DEPO, KEY_CUSTOMER_ISACTIVE,
						KEY_CUSTOMER_DESCRIPTION, KEY_CUSTOMER_NAMA_TOKO, KEY_CUSTOMER_TTD1,KEY_CUSTOMER_TTD2}, KEY_CUSTOMER_KODE_CUSTOMER + "=?",
				new String[] { String.valueOf(kode_customer) }, null, null,
				null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Customer customer = new Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getInt(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getInt(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getInt(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), cursor.getString(34),
				cursor.getString(35),cursor.getInt(36),cursor.getString(37),
				cursor.getString(38),cursor.getString(39), cursor.getString(40),
				cursor.getString(41));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	//get customer by nama_toko
	public Customer getCustomerByNamaToko(String nama_toko) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CUSTOMER, new String[] {
						KEY_CUSTOMER_ID_CUSTOMER, KEY_CUSTOMER_KODE_CUSTOMER,
						KEY_CUSTOMER_EMAIL_CUSTOMER, KEY_CUSTOMER_ALAMAT_CUSTOMER,
						KEY_CUSTOMER_LATS_CUSTOMER, KEY_CUSTOMER_LONGS_CUSTOMER,
						KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
						KEY_CUSTOMER_NO_TELP_CUSTOMER,
						KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, KEY_CUSTOMER_FOTO_1_CUSTOMER,
						KEY_CUSTOMER_FOTO_2_CUSTOMER, KEY_CUSTOMER_FOTO_3_CUSTOMER,
						KEY_CUSTOMER_ID_TYPE_CUSTOMER, KEY_CUSTOMER_BLOKIR,
						KEY_CUSTOMER_DATE, KEY_CUSTOMER_STATUS_UPDATE,
						KEY_CUSTOMER_ID_STAFF, KEY_CUSTOMER_NO_KTP, KEY_CUSTOMER_TANGGAL_LAHIR,
						KEY_CUSTOMER_NAMA_BANK, KEY_CUSTOMER_NO_REKENING, KEY_CUSTOMER_ATAS_NAMA,
						KEY_CUSTOMER_NPWP,KEY_CUSTOMER_NAMA_PASAR, KEY_CUSTOMER_ID_CLUSTER,
						KEY_CUSTOMER_TELP, KEY_CUSTOMER_FAX, KEY_CUSTOMER_OMSET,
						KEY_CUSTOMER_CARA_PEMBAYARAN, KEY_CUSTOMER_PLAFON_KREDIT,
						KEY_CUSTOMER_TERM_KREDIT,KEY_CUSTOMER_NAMA_ISTRI, KEY_CUSTOMER_NAMA_ANAK1, KEY_CUSTOMER_NAMA_ANAK2,
						KEY_CUSTOMER_NAMA_ANAK3,KEY_CUSTOMER_KODE_POS, KEY_CUSTOMER_ID_DEPO, KEY_CUSTOMER_ISACTIVE,
						KEY_CUSTOMER_DESCRIPTION, KEY_CUSTOMER_NAMA_TOKO, KEY_CUSTOMER_TTD1,KEY_CUSTOMER_TTD2}, KEY_CUSTOMER_NAMA_TOKO + "=?",
				new String[] { String.valueOf(nama_toko) }, null, null,
				null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Customer customer = new Customer(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getInt(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11), cursor.getInt(12), cursor.getString(13),
				cursor.getString(14), cursor.getString(15), cursor.getInt(16),
				cursor.getString(17), cursor.getString(18), cursor.getString(19),
				cursor.getString(20), cursor.getString(21), cursor.getString(22),
				cursor.getString(23), cursor.getInt(24), cursor.getString(25),
				cursor.getString(26), cursor.getString(27), cursor.getString(28),
				cursor.getString(29), cursor.getString(30), cursor.getString(31),
				cursor.getString(32), cursor.getString(33), cursor.getString(34),
				cursor.getString(35),cursor.getInt(36),cursor.getString(37),
				cursor.getString(38),cursor.getString(39), cursor.getString(40),
				cursor.getString(41));
		// return customer
		cursor.close();
		db.close();

		return customer;
	}

	// Getting single Jadwal
	public Jadwal getJadwal(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_JADWAL, new String[] {
						KEY_JADWAL_ID_JADWAL, KEY_JADWAL_KODE_JADWAL,
						KEY_JADWAL_KODE_CUSTOMER, KEY_JADWAL_USERNAME,
						KEY_JADWAL_ALAMAT, KEY_JADWAL_NAMA_TOKO,
						KEY_JADWAL_ID_WILAYAH, KEY_JADWAL_STATUS, KEY_JADWAL_DATE,
						KEY_JADWAL_CHECK_IN, KEY_JADWAL_CHECK_OUT,
						KEY_JADWAL_STATUS_UPDATE, KEY_JADWAL_KETERANGAN }, KEY_JADWAL_ID_JADWAL + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Jadwal jadwal = new Jadwal(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getInt(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11),cursor.getString(12));
		// return customer
		cursor.close();
		db.close();

		return jadwal;
	}

	public Jadwal getJadwal1(String kode) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_JADWAL, new String[] {
						KEY_JADWAL_ID_JADWAL, KEY_JADWAL_KODE_JADWAL,
						KEY_JADWAL_KODE_CUSTOMER, KEY_JADWAL_USERNAME,
						KEY_JADWAL_ALAMAT, KEY_JADWAL_NAMA_TOKO,
						KEY_JADWAL_ID_WILAYAH, KEY_JADWAL_STATUS, KEY_JADWAL_DATE,
						KEY_JADWAL_CHECK_IN, KEY_JADWAL_CHECK_OUT,
						KEY_JADWAL_STATUS_UPDATE,KEY_JADWAL_KETERANGAN }, KEY_JADWAL_KODE_CUSTOMER + "=?",
				new String[] { String.valueOf(kode) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Jadwal jadwal1 = new Jadwal(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getInt(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11),cursor.getString(12));
		// return customer
		cursor.close();
		db.close();

		return jadwal1;
	}

	// Getting single Product
	public Product getProduct(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PRODUCT, new String[] {
						KEY_PRODUCT_ID_PRODUCT, KEY_PRODUCT_NAMA_PRODUCT,
						KEY_PRODUCT_KODE_PRODUCT, KEY_PRODUCT_HARGA_JUAL,
						KEY_PRODUCT_STOCK, KEY_PRODUCT_ID_KEMASAN, KEY_PRODUCT_FOTO,
						KEY_PRODUCT_DESKRIPSI, KEY_PRODUCT_UOMQTYL1, KEY_PRODUCT_UOMQTYL2,
						KEY_PRODUCT_UOMQTYL3, KEY_PRODUCT_UOMQTYL4,KEY_PRODUCT_STATUS}, KEY_PRODUCT_ID_PRODUCT + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Product product = new Product(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10),
				cursor.getString(11),cursor.getString(12));
		// return staff
		cursor.close();
		db.close();

		return product;
	}

	// Getting single ProductPrice
	public ProductPrice getProductPrice(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_PRODUCT_PRICE, new String[] {
						KEY_PRODUCT_ID, KEY_PRODUCT_ID_PRODUCT, KEY_PRODUCT_PRICELIST,
						KEY_PRODUCT_PRICESTD, KEY_PRODUCT_PRICELIMIT}, KEY_PRODUCT_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		ProductPrice productPrice = new ProductPrice(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4));
		// return staff
		cursor.close();
		db.close();

		return productPrice;
	}

	// Getting single dtg
	public Dtg getDtg(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_DTG, new String[] {
						KEY_DTG_ID, KEY_DTG_KODE_CUSTOMER, KEY_DTG_NAMA_TOKO,
						KEY_DTG_NOMER_FAKTUR, KEY_DTG_NILAI_FAKTUR,KEY_DTG_JML_BELUM_BAYAR, KEY_DTG_NO_DTG}, KEY_DTG_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Dtg dtg = new Dtg(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
		// return staff
		cursor.close();
		db.close();

		return dtg;
	}


	// Getting single StockVan
	public StockVan getStockVan(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_STOCK_VAN, new String[] {
				KEY_STOCK_VAN_ID_PRODUCT, KEY_STOCK_VAN_NAMA_PRODUCT,
				KEY_STOCK_VAN_KODE_PRODUCT, KEY_STOCK_VAN_HARGA_JUAL,
				KEY_STOCK_VAN_JUMLAH_REQUEST, KEY_STOCK_VAN_JUMLAH_ACCEPT,
				KEY_STOCK_VAN_JUMLAH_SISA, KEY_STOCK_VAN_ID_KEMASAN,
				KEY_STOCK_VAN_FOTO, KEY_STOCK_VAN_DESKRIPSI,
				KEY_STOCK_VAN_NOMER_REQUEST_LOAD},
				KEY_STOCK_VAN_ID_PRODUCT + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		StockVan stockvan = new StockVan(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4),
				cursor.getString(5), cursor.getString(6), cursor.getString(7),
				cursor.getString(8), cursor.getString(9), cursor.getString(10));
		// return staff
		cursor.close();
		db.close();

		return stockvan;
	}

//	// Getting single Promosi
//	public Promosi getPromosi(int id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_PROMOSI,
//				new String[] { KEY_PROMOSI_ID_PROMOSI,
//						KEY_PROMOSI_TITLE_PROMOSI, KEY_PROMOSI_JENIS_PROMOSI,
//						KEY_PROMOSI_JUMLAH_DISKON, KEY_PROMOSI_DESKRIPSI,
//						KEY_PROMOSI_FOTO, KEY_PROMOSI_DATE_TIME },
//				KEY_PROMOSI_ID_PROMOSI + "=?",
//				new String[] { String.valueOf(id) }, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		Promosi promosi = new Promosi(cursor.getInt(0), cursor.getString(1),
//				cursor.getString(2), cursor.getInt(3), cursor.getString(4),
//				cursor.getString(5), cursor.getString(6));
//		// return staff
//		cursor.close();
//		db.close();
//
//		return promosi;
//	}

	// Getting single PhotoPurchase
	public DisplayProduct getDisplayProduct(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_DISPLAY_PRODUCT, new String[] {
						KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT,
						KEY_DISPLAY_PRODUCT_KODE_CUSTOMER,
						KEY_DISPLAY_PRODUCT_NAMA_LENGKAP,
						KEY_DISPLAY_PRODUCT_NAMA_DISPLAY_PRODUCT,
						KEY_DISPLAY_PRODUCT_DESKRIPSI, KEY_DISPLAY_PRODUCT_FOTO,
						KEY_DISPLAY_PRODUCT_DATE_TIME },
				KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		DisplayProduct displayProduct = new DisplayProduct(cursor.getInt(0),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4), cursor.getString(5), cursor.getString(6));
		// return staff
		cursor.close();
		db.close();

		return displayProduct;
	}

//	// Getting single PhotoPurchase
//	public PhotoPurchase getPhotoPurchase(int id) {
//		SQLiteDatabase db = this.getReadableDatabase();
//
//		Cursor cursor = db.query(TABLE_PHOTO_PURCHASE, new String[] {
//				KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE,
//				KEY_PHOTO_PURCHASE_KODE_CUSTOMER,
//				KEY_PHOTO_PURCHASE_NAMA_LENGKAP,
//				KEY_PHOTO_PURCHASE_NAMA_PHOTO_PURCHASE,
//				KEY_PHOTO_PURCHASE_DESKRIPSI, KEY_PHOTO_PURCHASE_FOTO,
//				KEY_PHOTO_PURCHASE_DATE_TIME },
//				KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE + "=?",
//				new String[] { String.valueOf(id) }, null, null, null, null);
//		if (cursor != null)
//			cursor.moveToFirst();
//
//		PhotoPurchase photoPurchase = new PhotoPurchase(cursor.getInt(0),
//				cursor.getString(1), cursor.getString(2), cursor.getString(3),
//				cursor.getString(4), cursor.getString(5), cursor.getString(6));
//		// return staff
//		cursor.close();
//		db.close();
//
//		return photoPurchase;
//	}

	// Getting single Kemasan
	public Kemasan getKemasan(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_KEMASAN, new String[] {
						KEY_KEMASAN_ID_KEMASAN, KEY_KEMASAN_NAMA_KEMASAN },
				KEY_KEMASAN_ID_KEMASAN + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Kemasan kemasan = new Kemasan(cursor.getInt(0), cursor.getString(1));
		// return kemasan
		cursor.close();
		db.close();

		return kemasan;
	}

	// Getting single Branch
	public Branch getBranch(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_BRANCH, new String[] {
						KEY_BRANCH_ID_BRANCH, KEY_BRANCH_KODE_BRANCH,
						KEY_BRANCH_DESKRIPSI_BRANCH }, KEY_BRANCH_ID_BRANCH + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Branch branch = new Branch(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2));
		// return branch
		cursor.close();
		db.close();

		return branch;
	}

	// Getting single TypeCustomer
	public TypeCustomer getTypeCustomer(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_TYPE_CUSTOMER, new String[] {
						KEY_TYPE_CUSTOMER_ID_TYPE_CUSTOMER,
						KEY_TYPE_CUSTOMER_TYPE_CUSTOMER, KEY_TYPE_CUSTOMER_DESKRIPSI },
				KEY_TYPE_CUSTOMER_ID_TYPE_CUSTOMER + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		TypeCustomer typeCustomer = new TypeCustomer(cursor.getInt(0),
				cursor.getString(1), cursor.getString(2));
		// return branch
		cursor.close();
		db.close();
		return typeCustomer;
	}

	// Getting single Cluster
	public Cluster getCluster(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_CLUSTER, new String[] {
						KEY_CLUSTER_ID_CLUSTER,
						KEY_CLUSTER_NAMA_CLUSTER, KEY_CLUSTER_DESCRIPTION },
				KEY_CLUSTER_ID_CLUSTER + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Cluster cluster = new Cluster(cursor.getInt(0),
				cursor.getString(1), cursor.getString(2));
		// return branch
		cursor.close();
		db.close();
		return cluster;
	}

	// Getting single Wilayah
	public Wilayah getWilayah(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_WILAYAH, new String[] {
						KEY_WILAYAH_ID_WILAYAH, KEY_WILAYAH_NAMA_WILAYAH,
						KEY_WILAYAH_DESKRIPSI_WILAYAH, KEY_WILAYAH_LATS_WILAYAH,
						KEY_WILAYAH_LONGS_WILAYAH }, KEY_WILAYAH_ID_WILAYAH + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);

		if (cursor != null)
			cursor.moveToFirst();

		Wilayah wilayah = new Wilayah(cursor.getInt(0), cursor.getString(1),
				cursor.getString(2), cursor.getString(3), cursor.getString(4));
		// return branch
		cursor.close();
		db.close();
		return wilayah;
	}



	// Getting ArrayList<Staff> below level
	public ArrayList<Staff> getAllStaff_Low_Level(int level) {

		try {
			staff_list.clear();

			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db
					.rawQuery("select * FROM " + TABLE_STAFF
									+ " WHERE level >?",
							new String[] { String.valueOf(level) });
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Staff staff = new Staff();
					staff.setId_staff(cursor.getInt(0));
					staff.setNama_lengkap(cursor.getString(1));
					staff.setUsername(cursor.getString(2));
					staff.setNotelp(cursor.getString(3));
					staff.setPassword(cursor.getString(4));
					staff.setLevel(cursor.getInt(5));
					staff.setId_branch(cursor.getString(6));
					staff.setId_type_customer(cursor.getString(7));
					staff.setId_depo(cursor.getInt(8));

					// Adding staff to list
					staff_list.add(staff);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			Log.e("staff_list", "staff_list = " + staff_list.size());
			return staff_list;
		} catch (Exception e) {
			Log.e("staff_list", "" + e);
		}

		return staff_list;

	}

	// Getting ArrayList<Staff> below level
	public ArrayList<StaffTemp> getAllStaff_Temp_Low_Level(int level) {

		try {
			staff_temp_list.clear();

			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db
					.rawQuery("select * FROM " + TABLE_STAFF_TEMP
									+ " WHERE level >?",
							new String[] { String.valueOf(level) });
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StaffTemp staff_temp = new StaffTemp();
					staff_temp.setId_staff(cursor.getInt(0));
					staff_temp.setNama_lengkap(cursor.getString(1));
					staff_temp.setUsername(cursor.getString(2));
					staff_temp.setNotelp(cursor.getString(3));
					staff_temp.setPassword(cursor.getString(4));
					staff_temp.setLevel(cursor.getInt(5));
					staff_temp.setId_branch(cursor.getString(6));
					staff_temp.setId_type_customer(cursor.getString(7));
					staff_temp.setId_depo(cursor.getInt(8));

					// Adding staff_temp_list to list
					staff_temp_list.add(staff_temp);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			Log.e("staff_temp_list",
					"staff_temp_list = " + staff_temp_list.size());
			return staff_temp_list;
		} catch (Exception e) {
			Log.e("staff_temp_list", "" + e);
		}

		return staff_temp_list;

	}

	// Getting All Staff
	public ArrayList<Staff> getAllStaff() {
		try {
			staff_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STAFF;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Staff staff = new Staff();
					staff.setId_staff(cursor.getInt(0));
					staff.setNama_lengkap(cursor.getString(1));
					staff.setUsername(cursor.getString(2));
					staff.setNotelp(cursor.getString(3));
					staff.setPassword(cursor.getString(4));
					staff.setLevel(cursor.getInt(5));
					staff.setId_branch(cursor.getString(6));
					staff.setId_type_customer(cursor.getString(7));
					staff.setId_depo(cursor.getInt(8));

					// Adding staff to list
					staff_list.add(staff);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return staff_list;
		} catch (Exception e) {
			Log.e("staff_list", "" + e);
		}

		return staff_list;
	}

	// Getting All Staff Temp
	public ArrayList<StaffTemp> getAllStaffTemp() {
		try {
			staff_temp_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STAFF_TEMP;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StaffTemp staff = new StaffTemp();
					staff.setId_staff(cursor.getInt(0));
					staff.setNama_lengkap(cursor.getString(1));
					staff.setUsername(cursor.getString(2));
					staff.setNotelp(cursor.getString(3));
					staff.setPassword(cursor.getString(4));
					staff.setLevel(cursor.getInt(5));
					staff.setId_branch(cursor.getString(6));
					staff.setId_type_customer(cursor.getString(7));
					staff.setId_depo(cursor.getInt(8));

					// Adding staff to list
					staff_temp_list.add(staff);
				} while (cursor.moveToNext());
			}

			// return staff_list
			cursor.close();
			db.close();
			return staff_temp_list;
		} catch (Exception e) {
			Log.e("staff_temp_list", "" + e);
		}

		return staff_temp_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomer() {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_CUSTOMER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));

					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwal() {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL + " limit 50";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwalWhereKodeStaffAndGroupBy(
			String username) {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL
					+ " WHERE username ='" + username + "' AND "
					+ KEY_JADWAL_CHECK_IN + "=''" + " GROUP BY "
					+ KEY_JADWAL_KODE_JADWAL;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwalWhereKodeStaffAndGroupByCustomer(
			String username) {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL
					+ " WHERE username ='" + username + "' AND "
					+ "status_update =" + "2" + " GROUP BY "
					+ KEY_JADWAL_KODE_CUSTOMER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwalWhereAlreadyCheckOut() {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL + " WHERE "
					+ KEY_JADWAL_STATUS_UPDATE + "='3'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwalWhereKodeJadwal(String kode_jadwal) {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL + " WHERE "
					+ KEY_JADWAL_KODE_JADWAL + "='" + kode_jadwal + "' limit 50";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwalWhereKodeJadwalAndSearch(
			String kode_jadwal, String search) {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL + " WHERE "
					+ KEY_JADWAL_KODE_JADWAL + "='" + kode_jadwal + "' AND "
					+ KEY_JADWAL_NAMA_TOKO + " LIKE '%" + search + "%'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Jadwal
	public ArrayList<Jadwal> getAllJadwalGroupByKodeJadwal() {
		try {
			jadwal_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL
					+ " GROUP BY " + KEY_JADWAL_KODE_JADWAL;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));
					// Adding jadwal_list to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return jadwal_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("jadwal_list", "" + e);
		}

		return jadwal_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomerProspect() {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='Y'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

    public ArrayList<Customer> getAllCustomerProspectUploaded() {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='NO'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomerActive() {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='N' limit 50";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));

					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}


    public ArrayList<Customer> getAllCustomerActiveUploaded() {
        try {
            customer_list.clear();

            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
                    + " WHERE blokir ='N' and status_update='3' order by date desc";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setId_customer(cursor.getInt(0));
                    customer.setKode_customer(cursor.getString(1));
                    customer.setEmail(cursor.getString(2));
                    customer.setAlamat(cursor.getString(3));
                    customer.setLats(cursor.getString(4));
                    customer.setLongs(cursor.getString(5));
                    customer.setNama_lengkap(cursor.getString(6));
                    customer.setNo_telp(cursor.getString(7));
                    customer.setId_wilayah(cursor.getInt(8));
                    customer.setFoto_1(cursor.getString(9));
                    customer.setFoto_2(cursor.getString(10));
                    customer.setFoto_3(cursor.getString(11));
                    customer.setId_type_customer(cursor.getInt(12));
                    customer.setBlokir(cursor.getString(13));
                    customer.setDate(cursor.getString(14));
                    customer.setStatus_update(cursor.getString(15));
                    customer.setId_staff(cursor.getInt(16));
                    customer.setNo_ktp(cursor.getString(17));
                    customer.setTanggal_lahir(cursor.getString(18));
                    customer.setNama_bank(cursor.getString(19));
                    customer.setNo_rekening(cursor.getString(20));
                    customer.setAtas_nama(cursor.getString(21));
                    customer.setNpwp(cursor.getString(22));
                    customer.setNama_pasar(cursor.getString(23));
                    customer.setId_cluster(cursor.getInt(24));
                    customer.setTelp(cursor.getString(25));
                    customer.setFax(cursor.getString(26));
                    customer.setOmset(cursor.getString(27));
                    customer.setCara_pembayaran(cursor.getString(28));
                    customer.setPlafon_kredit(cursor.getString(29));
                    customer.setTerm_kredit(cursor.getString(30));

                    customer.setNama_istri(cursor.getString(31));
                    customer.setNama_anak1(cursor.getString(32));
                    customer.setNama_anak2(cursor.getString(33));
                    customer.setNama_anak3(cursor.getString(34));
                    customer.setKode_pos(cursor.getString(35));
                    customer.setId_depo(cursor.getInt(36));
                    customer.setIsactive(cursor.getString(37));
                    customer.setDescription(cursor.getString(38));
                    customer.setNama_toko(cursor.getString(39));
                    customer.setTtd1(cursor.getString(40));
                    customer.setTtd2(cursor.getString(41));
                    // Adding customer_list to list
                    customer_list.add(customer);
                } while (cursor.moveToNext());
            }

            // return customer_list
            cursor.close();
            db.close();
            return customer_list;
        } catch (Exception e) {
            Log.e("customer_list", "" + e);
        }

        return customer_list;
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
				Log.d(LOG_TAG, "latitude " + latitude);
			} catch (Exception e) {
				Log.i(LOG_TAG, "onLocationChanged " + e.toString());
			}
		}
	};


	public ArrayList<Customer> getAllCustomerActiveUploaded12() {
		try {
			customer_list.clear();

			location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);
			latitude = location.getLatitude();
			Double latsTambah= latitude+0.01;
			Double latsKurang= latitude-0.01;
			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
			//+" where lats between "+ latsTambah+" and "+latsKurang;
			+" where lats between '-5.732801666666667' and '-5.752801666666667'";
			//+ " WHERE blokir ='N' and status_update='3' order by date desc";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));

					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomerActive(int id_depo) {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='N' AND id_depo=" + id_depo + " limit 50";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	public ArrayList<Customer> getAllCustomerActivemaps(int id_wilayah) {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='N' AND id_wilayah=" + id_wilayah;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	public ArrayList<Customer> getAllCustomerActivemaps1(int id_staff) {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='N' AND id_staff=" + id_staff;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

    public ArrayList<Customer> getAllCustomerActiveUploaded(int id_depo) {
        try {
            customer_list.clear();

            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
                    + " WHERE blokir ='N' and status_update ='3' AND id_depo=" + id_depo+" order by date desc";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setId_customer(cursor.getInt(0));
                    customer.setKode_customer(cursor.getString(1));
                    customer.setEmail(cursor.getString(2));
                    customer.setAlamat(cursor.getString(3));
                    customer.setLats(cursor.getString(4));
                    customer.setLongs(cursor.getString(5));
                    customer.setNama_lengkap(cursor.getString(6));
                    customer.setNo_telp(cursor.getString(7));
                    customer.setId_wilayah(cursor.getInt(8));
                    customer.setFoto_1(cursor.getString(9));
                    customer.setFoto_2(cursor.getString(10));
                    customer.setFoto_3(cursor.getString(11));
                    customer.setId_type_customer(cursor.getInt(12));
                    customer.setBlokir(cursor.getString(13));
                    customer.setDate(cursor.getString(14));
                    customer.setStatus_update(cursor.getString(15));
                    customer.setId_staff(cursor.getInt(16));
                    customer.setNo_ktp(cursor.getString(17));
                    customer.setTanggal_lahir(cursor.getString(18));
                    customer.setNama_bank(cursor.getString(19));
                    customer.setNo_rekening(cursor.getString(20));
                    customer.setAtas_nama(cursor.getString(21));
                    customer.setNpwp(cursor.getString(22));
                    customer.setNama_pasar(cursor.getString(23));
                    customer.setId_cluster(cursor.getInt(24));
                    customer.setTelp(cursor.getString(25));
                    customer.setFax(cursor.getString(26));
                    customer.setOmset(cursor.getString(27));
                    customer.setCara_pembayaran(cursor.getString(28));
                    customer.setPlafon_kredit(cursor.getString(29));
                    customer.setTerm_kredit(cursor.getString(30));
                    customer.setNama_istri(cursor.getString(31));
                    customer.setNama_anak1(cursor.getString(32));
                    customer.setNama_anak2(cursor.getString(33));
                    customer.setNama_anak3(cursor.getString(34));
                    customer.setKode_pos(cursor.getString(35));
                    customer.setId_depo(cursor.getInt(36));
                    customer.setIsactive(cursor.getString(37));
                    customer.setDescription(cursor.getString(38));
                    customer.setNama_toko(cursor.getString(39));
                    customer.setTtd1(cursor.getString(40));
                    customer.setTtd2(cursor.getString(41));
                    // Adding customer_list to list
                    customer_list.add(customer);
                } while (cursor.moveToNext());
            }

            // return customer_list
            cursor.close();
            db.close();
            return customer_list;
        } catch (Exception e) {
            Log.e("customer_list", "" + e);
        }

        return customer_list;
    }

	public ArrayList<Customer> getAllCustomerActiveUploaded12(int id_depo) {
		try {
			customer_list.clear();

			latitude = location.getLatitude();
			Double latsTambah= latitude+0.01;
			Double latsKurang= latitude-0.01;
			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+" where lats between "+ latsTambah+" and "+latsKurang;
			// Select All Query
			//String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
			//+" where lats between '-5.732801666666667' and '-5.862019999999999'";
			//+ " WHERE blokir ='N' and status_update ='3' AND id_depo=" + id_depo+" order by date desc";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Product
	public ArrayList<Product> getAllProductActiveAndUpdateByUser() {
		try {
			product_list.clear();

			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT
					+" WHERE status = 2";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			if (cursor.moveToFirst()) {
				do {
					Product product = new Product();
					product.setId_product(cursor.getInt(0));
					product.setNama_product(cursor.getString(1));
					product.setKode_product(cursor.getString(2));
					product.setHarga_jual(cursor.getString(3));
					product.setStock(cursor.getString(4));
					product.setId_kemasan(cursor.getString(5));
					product.setFoto(cursor.getString(6));
					product.setDeskripsi(cursor.getString(7));
					product.setUomqtyl1(cursor.getString(8));
					product.setUomqtyl2(cursor.getString(9));
					product.setUomqtyl3(cursor.getString(10));
					product.setUomqtyl4(cursor.getString(11));
					product.setStatus(cursor.getString(12));

					product_list.add(product);
				} while (cursor.moveToNext());
			}

			cursor.close();
			db.close();
			return product_list;
		} catch (Exception e) {
			Log.e("product_list", "" + e);
		}

		return product_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomerActiveAndUpdateByUser() {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='N' AND status_update='2'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomerActiveBaseOnSearch(String search,int id_wilayah) {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='N' AND "
					+ KEY_CUSTOMER_NAMA_TOKO + " LIKE '%" + search
					+ "%' AND id_depo=" + id_wilayah;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));

					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	public ArrayList<Jadwal> getAllJadwalBaseOnSearch(String search) {
		try {
			jadwal_list.clear();


			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_JADWAL + " WHERE "
					+ KEY_JADWAL_NAMA_TOKO + " LIKE '%" + search + "%'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Jadwal jadwal = new Jadwal();
					jadwal.setId_jadwal(cursor.getInt(0));
					jadwal.setKode_jadwal(cursor.getString(1));
					jadwal.setKode_customer(cursor.getString(2));
					jadwal.setUsername(cursor.getString(3));
					jadwal.setAlamat(cursor.getString(4));
					jadwal.setNama_toko(cursor.getString(5));
					jadwal.setId_wilayah(cursor.getInt(6));
					jadwal.setStatus(cursor.getString(7));
					jadwal.setDate(cursor.getString(8));
					jadwal.setCheckin(cursor.getString(9));
					jadwal.setCheckout(cursor.getString(10));
					jadwal.setStatus_update(cursor.getString(11));
					jadwal.setKeterangan(cursor.getString(12));

					// Adding product to list
					jadwal_list.add(jadwal);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return jadwal_list;
		} catch (Exception e) {
			Log.e("product_list", "" + e);
		}

		return jadwal_list;
	}

    public ArrayList<Customer> getAllCustomerActiveUploadedBaseOnSearch(String search,int id_wilayah) {
        try {
            customer_list.clear();

            // Select All Query
            String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
                    + " WHERE blokir ='N' and status_update='3' AND "
                    + KEY_CUSTOMER_NAMA_TOKO + " LIKE '%" + search
                    + "%' AND id_depo=" + id_wilayah;
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Customer customer = new Customer();
                    customer.setId_customer(cursor.getInt(0));
                    customer.setKode_customer(cursor.getString(1));
                    customer.setEmail(cursor.getString(2));
                    customer.setAlamat(cursor.getString(3));
                    customer.setLats(cursor.getString(4));
                    customer.setLongs(cursor.getString(5));
                    customer.setNama_lengkap(cursor.getString(6));
                    customer.setNo_telp(cursor.getString(7));
                    customer.setId_wilayah(cursor.getInt(8));
                    customer.setFoto_1(cursor.getString(9));
                    customer.setFoto_2(cursor.getString(10));
                    customer.setFoto_3(cursor.getString(11));
                    customer.setId_type_customer(cursor.getInt(12));
                    customer.setBlokir(cursor.getString(13));
                    customer.setDate(cursor.getString(14));
                    customer.setStatus_update(cursor.getString(15));
                    customer.setId_staff(cursor.getInt(16));
                    customer.setNo_ktp(cursor.getString(17));
                    customer.setTanggal_lahir(cursor.getString(18));
                    customer.setNama_bank(cursor.getString(19));
                    customer.setNo_rekening(cursor.getString(20));
                    customer.setAtas_nama(cursor.getString(21));
                    customer.setNpwp(cursor.getString(22));
                    customer.setNama_pasar(cursor.getString(23));
                    customer.setId_cluster(cursor.getInt(24));
                    customer.setTelp(cursor.getString(25));
                    customer.setFax(cursor.getString(26));
                    customer.setOmset(cursor.getString(27));
                    customer.setCara_pembayaran(cursor.getString(28));
                    customer.setPlafon_kredit(cursor.getString(29));
                    customer.setTerm_kredit(cursor.getString(30));

                    customer.setNama_istri(cursor.getString(31));
                    customer.setNama_anak1(cursor.getString(32));
                    customer.setNama_anak2(cursor.getString(33));
                    customer.setNama_anak3(cursor.getString(34));
                    customer.setId_depo(cursor.getInt(36));
                    customer.setIsactive(cursor.getString(37));
                    customer.setDescription(cursor.getString(38));
                    customer.setNama_toko(cursor.getString(39));
                    customer.setTtd1(cursor.getString(40));
                    customer.setTtd2(cursor.getString(41));
                    // Adding customer_list to list
                    customer_list.add(customer);
                } while (cursor.moveToNext());
            }

            // return customer_list
            cursor.close();
            db.close();
            return customer_list;
        } catch (Exception e) {
            Log.e("customer_list", "" + e);
        }

        return customer_list;
    }


	public ArrayList<Customer> getAllCustomerActiveUploaded12BaseOnSearch(String search,int id_wilayah) {
		try {
			customer_list.clear();

			latitude = location.getLatitude();
			Double latsTambah= latitude+0.01;
			Double latsKurang= latitude-0.01;
			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+" where lats between "+ latsTambah+" and "+latsKurang;
			// Select All Query
			//String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
			//+" where lats between '-5.732801666666667' and '-5.862019999999999'";
			//+ " WHERE blokir ='N' and status_update='3' AND "
					//+ KEY_CUSTOMER_NAMA_TOKO + " LIKE '" + search
					//+ "%' AND id_depo=" + id_wilayah;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));

					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Customer
	public ArrayList<Customer> getAllCustomerProspectBaseOnSearch(String search) {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='Y' AND "
					+ KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER + " LIKE '%" + search
					+ "%'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

    public ArrayList<Customer> getAllCustomerProspectUploadedBaseOnSearch(String search) {
		try {
			customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT * FROM " + TABLE_CUSTOMER
					+ " WHERE blokir ='NO' AND "
					+ KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER + " LIKE '%" + search
					+ "%'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Customer customer = new Customer();
					customer.setId_customer(cursor.getInt(0));
					customer.setKode_customer(cursor.getString(1));
					customer.setEmail(cursor.getString(2));
					customer.setAlamat(cursor.getString(3));
					customer.setLats(cursor.getString(4));
					customer.setLongs(cursor.getString(5));
					customer.setNama_lengkap(cursor.getString(6));
					customer.setNo_telp(cursor.getString(7));
					customer.setId_wilayah(cursor.getInt(8));
					customer.setFoto_1(cursor.getString(9));
					customer.setFoto_2(cursor.getString(10));
					customer.setFoto_3(cursor.getString(11));
					customer.setId_type_customer(cursor.getInt(12));
					customer.setBlokir(cursor.getString(13));
					customer.setDate(cursor.getString(14));
					customer.setStatus_update(cursor.getString(15));
					customer.setId_staff(cursor.getInt(16));
					customer.setNo_ktp(cursor.getString(17));
					customer.setTanggal_lahir(cursor.getString(18));
					customer.setNama_bank(cursor.getString(19));
					customer.setNo_rekening(cursor.getString(20));
					customer.setAtas_nama(cursor.getString(21));
					customer.setNpwp(cursor.getString(22));
					customer.setNama_pasar(cursor.getString(23));
					customer.setId_cluster(cursor.getInt(24));
					customer.setTelp(cursor.getString(25));
					customer.setFax(cursor.getString(26));
					customer.setOmset(cursor.getString(27));
					customer.setCara_pembayaran(cursor.getString(28));
					customer.setPlafon_kredit(cursor.getString(29));
					customer.setTerm_kredit(cursor.getString(30));
					customer.setNama_istri(cursor.getString(31));
					customer.setNama_anak1(cursor.getString(32));
					customer.setNama_anak2(cursor.getString(33));
					customer.setNama_anak3(cursor.getString(34));
					customer.setKode_pos(cursor.getString(35));
					customer.setId_depo(cursor.getInt(36));
					customer.setIsactive(cursor.getString(37));
					customer.setDescription(cursor.getString(38));
					customer.setNama_toko(cursor.getString(39));
					customer.setTtd1(cursor.getString(40));
					customer.setTtd2(cursor.getString(41));
					// Adding customer_list to list
					customer_list.add(customer);
				} while (cursor.moveToNext());
			}

			// return customer_list
			cursor.close();
			db.close();
			return customer_list;
		} catch (Exception e) {
			Log.e("customer_list", "" + e);
		}

		return customer_list;
	}

	// Getting All Stock On Hand
	public ArrayList<StockOnHand> getAllStockOnHand() {
		try {
			stock_on_hand_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STOCK_ON_HAND;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StockOnHand stockOnHand = new StockOnHand();
					stockOnHand.setId_stock_on_hand(cursor.getInt(0));
					stockOnHand.setNomer_stock_on_hand(cursor.getString(1));
					stockOnHand.setNomer_stock_on_hand_detail(cursor
							.getString(2));
					stockOnHand.setDate_stock_on_hand(cursor.getString(3));
					stockOnHand.setTime_stock_on_hand(cursor.getString(4));
					stockOnHand.setDeskripsi(cursor.getString(5));
					stockOnHand.setUsername(cursor.getString(6));
					stockOnHand.setKode_customer(cursor.getString(7));
					stockOnHand.setAlamat(cursor.getString(8));
					stockOnHand.setNama_lengkap(cursor.getString(9));
					stockOnHand.setNama_product(cursor.getString(10));
					stockOnHand.setKode_product(cursor.getString(11));
					stockOnHand.setHarga_jual(cursor.getString(12));
					stockOnHand.setStockpcs(cursor.getString(13));
					stockOnHand.setStockrcg(cursor.getString(14));
					stockOnHand.setStockpck(cursor.getString(15));
					stockOnHand.setStockdus(cursor.getString(16));

					// Adding stock_on_hand_list to list
					stock_on_hand_list.add(stockOnHand);
				} while (cursor.moveToNext());
			}

			// return stock_on_hand_list
			cursor.close();
			db.close();
			return stock_on_hand_list;
		} catch (Exception e) {
			Log.e("stock_on_hand_list", "" + e);
		}

		return stock_on_hand_list;
	}

	// Getting All Stock On Hand
	public ArrayList<StockOnHand> getAllStockOnHandWhereStockOnHand(
			String nomer_stock_on_hand) {
		try {
			stock_on_hand_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STOCK_ON_HAND
					+ " WHERE nomer_stock_on_hand ='" + nomer_stock_on_hand
					+ "' ";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StockOnHand stockOnHand = new StockOnHand();
					stockOnHand.setId_stock_on_hand(cursor.getInt(0));
					stockOnHand.setNomer_stock_on_hand(cursor.getString(1));
					stockOnHand.setNomer_stock_on_hand_detail(cursor
							.getString(2));
					stockOnHand.setDate_stock_on_hand(cursor.getString(3));
					stockOnHand.setTime_stock_on_hand(cursor.getString(4));
					stockOnHand.setDeskripsi(cursor.getString(5));
					stockOnHand.setUsername(cursor.getString(6));
					stockOnHand.setKode_customer(cursor.getString(7));
					stockOnHand.setAlamat(cursor.getString(8));
					stockOnHand.setNama_lengkap(cursor.getString(9));
					stockOnHand.setNama_product(cursor.getString(10));
					stockOnHand.setKode_product(cursor.getString(11));
					stockOnHand.setHarga_jual(cursor.getString(12));
					stockOnHand.setStockpcs(cursor.getString(13));
					stockOnHand.setStockrcg(cursor.getString(14));
					stockOnHand.setStockpck(cursor.getString(15));
					stockOnHand.setStockdus(cursor.getString(16));

					// Adding stock_on_hand_list to list
					stock_on_hand_list.add(stockOnHand);
				} while (cursor.moveToNext());
			}

			// return stock_on_hand_list
			cursor.close();
			db.close();
			return stock_on_hand_list;
		} catch (Exception e) {
			Log.e("stock_on_hand_list", "" + e);
		}

		return stock_on_hand_list;
	}

	// Getting All Product Target
	public ArrayList<ProductTarget> getAllProductTargetGroupByNomerPenjualan() {
		try {
			productTarget_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_TARGET
					+ " GROUP BY " + KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ProductTarget productTarget = new ProductTarget();
					productTarget.setId_product_target(cursor.getInt(0));
					productTarget.setNomer_product_target(cursor.getString(1));
					productTarget.setCreated_date_product_target(cursor
							.getString(2));
					productTarget.setCreated_time_product_target(cursor
							.getString(3));
					productTarget.setUpdated_date_product_target(cursor
							.getString(4));
					productTarget.setUpdated_time_product_target(cursor
							.getString(5));
					productTarget.setCreated_by(cursor.getInt(6));
					productTarget.setUpdated_by(cursor.getInt(7));
					productTarget.setId_staff(cursor.getInt(8));
					productTarget.setId_customer(cursor.getInt(9));
					productTarget.setId_product(cursor.getInt(10));
					productTarget.setJumlah_target(cursor.getInt(11));
					productTarget.setJumlah_terjual(cursor.getInt(12));
					// Adding productTarget_list to list
					productTarget_list.add(productTarget);
				} while (cursor.moveToNext());
			}
			// return productTarget_list
			cursor.close();
			db.close();
			return productTarget_list;
		} catch (Exception e) {
			Log.e("productTarget_list", "" + e);
		}
		return productTarget_list;
	}
	// Getting All Product Target
	public ArrayList<ProductTarget> getAllProductTarget() {
		try {
			productTarget_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_TARGET;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ProductTarget productTarget = new ProductTarget();
					productTarget.setId_product_target(cursor.getInt(0));
					productTarget.setNomer_product_target(cursor.getString(1));
					productTarget.setCreated_date_product_target(cursor
							.getString(2));
					productTarget.setCreated_time_product_target(cursor
							.getString(3));
					productTarget.setUpdated_date_product_target(cursor
							.getString(4));
					productTarget.setUpdated_time_product_target(cursor
							.getString(5));
					productTarget.setCreated_by(cursor.getInt(6));
					productTarget.setUpdated_by(cursor.getInt(7));
					productTarget.setId_staff(cursor.getInt(8));
					productTarget.setId_customer(cursor.getInt(9));
					productTarget.setId_product(cursor.getInt(10));
					productTarget.setJumlah_target(cursor.getInt(11));
					productTarget.setJumlah_terjual(cursor.getInt(12));
					// Adding productTarget_list to list
					productTarget_list.add(productTarget);
				} while (cursor.moveToNext());
			}
			// return productTarget_list
			cursor.close();
			db.close();
			return productTarget_list;
		} catch (Exception e) {
			Log.e("productTarget_list", "" + e);
		}
		return productTarget_list;
	}
	// Getting All Penjualan
	public ArrayList<Penjualan> getAllPenjualan() {
		try {
			penjualan_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PENJUALAN;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Penjualan penjualan = new Penjualan();
					penjualan.setId_penjualan(cursor.getInt(0));
					penjualan.setNomer_product_terjual(cursor.getString(1));
					penjualan.setDate_product_terjual(cursor.getString(2));
					penjualan.setTime_product_terjual(cursor.getString(3));
					penjualan.setId_customer(cursor.getInt(4));
					penjualan.setId_staff(cursor.getInt(5));
					penjualan.setDiskon(cursor.getInt(6));
					// Adding penjualan_list to list
					penjualan_list.add(penjualan);
				} while (cursor.moveToNext());
			}
			// return penjualan_list
			cursor.close();
			db.close();
			return penjualan_list;
		} catch (Exception e) {
			Log.e("penjualan_list", "" + e);
		}
		return penjualan_list;
	}
	// Getting All Penjualan
	public ArrayList<Penjualan> getAllPenjualanOrderWhereNomerPenjualan(
			String nomer_penjualan) {
		try {
			penjualan_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PENJUALAN
					+ " WHERE " + KEY_PENJUALAN_NOMER_PENJUALAN + "='"
					+ nomer_penjualan + "' ";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Penjualan penjualan = new Penjualan();
					penjualan.setId_penjualan(cursor.getInt(0));
					penjualan.setNomer_product_terjual(cursor.getString(1));
					penjualan.setDate_product_terjual(cursor.getString(2));
					penjualan.setTime_product_terjual(cursor.getString(3));
					penjualan.setId_customer(cursor.getInt(4));
					penjualan.setId_staff(cursor.getInt(5));
					penjualan.setDiskon(cursor.getInt(6));
					// Adding penjualan_list to list
					penjualan_list.add(penjualan);
				} while (cursor.moveToNext());
			}
			// return penjualan_list
			cursor.close();
			db.close();
			return penjualan_list;
		} catch (Exception e) {
			Log.e("penjualan_list", "" + e);
		}
		return penjualan_list;
	}
	// Getting All Penjualan Detail
	public ArrayList<PenjualanDetail> getAllPenjualanDetailWhereNomerPenjualan(
			String nomer_penjualan) {
		try {
			penjualan_detail_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PENJUALAN_DETAIL
					+ " WHERE " + KEY_PENJUALAN_DETAIL_NOMER_PENJUALAN + "='"
					+ nomer_penjualan + "' ";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					PenjualanDetail penjualanDetail = new PenjualanDetail();
					penjualanDetail.setId_penjualan_detail(cursor.getInt(0));
					penjualanDetail.setNomer_product_terjual(cursor
							.getString(1));
					penjualanDetail.setIdProduct(cursor.getInt(2));
					penjualanDetail.setJumlah(cursor.getInt(3));
					// Adding penjualan_detail_list to list
					penjualan_detail_list.add(penjualanDetail);
				} while (cursor.moveToNext());
			}
			// return penjualan_detail_list
			cursor.close();
			db.close();
			return penjualan_detail_list;
		} catch (Exception e) {
			Log.e("penjualan_detail_list", "" + e);
		}
		return penjualan_detail_list;
	}
	// Getting All Penjualan Detail
	public ArrayList<PenjualanDetail> getAllPenjualanDetail() {
		try {
			penjualan_detail_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PENJUALAN_DETAIL;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					PenjualanDetail penjualanDetail = new PenjualanDetail();
					penjualanDetail.setId_penjualan_detail(cursor.getInt(0));
					penjualanDetail.setNomer_product_terjual(cursor
							.getString(1));
					penjualanDetail.setIdProduct(cursor.getInt(2));
					penjualanDetail.setJumlah(cursor.getInt(3));
					// Adding penjualan_detail_list to list
					penjualan_detail_list.add(penjualanDetail);
				} while (cursor.moveToNext());
			}
			// return penjualan_detail_list
			cursor.close();
			db.close();
			return penjualan_detail_list;
		} catch (Exception e) {
			Log.e("penjualan_detail_list", "" + e);
		}
		return penjualan_detail_list;
	}
	// Getting All Product Target
	public ArrayList<ProductTarget> getAllProductTargetWhereNomerPenjualan(
			String nomer_tp) {
		try {
			productTarget_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_TARGET
					+ " WHERE " + KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET
					+ "='" + nomer_tp + "' ";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ProductTarget productTarget = new ProductTarget();
					productTarget.setId_product_target(cursor.getInt(0));
					productTarget.setNomer_product_target(cursor.getString(1));
					productTarget.setCreated_date_product_target(cursor
							.getString(2));
					productTarget.setCreated_time_product_target(cursor
							.getString(3));
					productTarget.setUpdated_date_product_target(cursor
							.getString(4));
					productTarget.setUpdated_time_product_target(cursor
							.getString(5));
					productTarget.setCreated_by(cursor.getInt(6));
					productTarget.setUpdated_by(cursor.getInt(7));
					productTarget.setId_staff(cursor.getInt(8));
					productTarget.setId_customer(cursor.getInt(9));
					productTarget.setId_product(cursor.getInt(10));
					productTarget.setJumlah_target(cursor.getInt(11));
					productTarget.setJumlah_terjual(cursor.getInt(12));
					// Adding productTarget_list to list
					productTarget_list.add(productTarget);
				} while (cursor.moveToNext());
			}
			// return productTarget_list
			cursor.close();
			db.close();
			return productTarget_list;
		} catch (Exception e) {
			Log.e("productTarget_list", "" + e);
		}
		return productTarget_list;
	}

	// Getting All Sales Order
	public ArrayList<StockOnHand> getAllStockOnHandGroupByNomerOrder() {
		try {
			stock_on_hand_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STOCK_ON_HAND
					+ " GROUP BY " + KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StockOnHand stockOnHand = new StockOnHand();
					stockOnHand.setId_stock_on_hand(cursor.getInt(0));
					stockOnHand.setNomer_stock_on_hand(cursor.getString(1));
					stockOnHand.setNomer_stock_on_hand_detail(cursor
							.getString(2));
					stockOnHand.setDate_stock_on_hand(cursor.getString(3));
					stockOnHand.setTime_stock_on_hand(cursor.getString(4));
					stockOnHand.setDeskripsi(cursor.getString(5));
					stockOnHand.setUsername(cursor.getString(6));
					stockOnHand.setKode_customer(cursor.getString(7));
					stockOnHand.setAlamat(cursor.getString(8));
					stockOnHand.setNama_lengkap(cursor.getString(9));
					stockOnHand.setNama_product(cursor.getString(10));
					stockOnHand.setKode_product(cursor.getString(11));
					stockOnHand.setHarga_jual(cursor.getString(12));
					stockOnHand.setStockpcs(cursor.getString(13));
					stockOnHand.setStockrcg(cursor.getString(14));
					stockOnHand.setStockpck(cursor.getString(15));
					stockOnHand.setStockdus(cursor.getString(16));

					// Adding stock_on_hand_list to list
					stock_on_hand_list.add(stockOnHand);
				} while (cursor.moveToNext());
			}

			// return stock_on_hand_list
			cursor.close();
			db.close();
			return stock_on_hand_list;
		} catch (Exception e) {
			Log.e("stock_on_hand_list", "" + e);
		}

		return stock_on_hand_list;
	}

	// Getting All Sales Order
	public ArrayList<SalesOrder> getAllSalesOrder() {
		try {
			sales_order_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_SALES_ORDER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					SalesOrder salesOrder = new SalesOrder();
					salesOrder.setId_sales_order(cursor.getInt(0));
					salesOrder.setNomer_order(cursor.getString(1));
					salesOrder.setNomer_order_detail(cursor.getString(2));
					salesOrder.setDate_order(cursor.getString(3));
					salesOrder.setTime_order(cursor.getString(4));
					salesOrder.setDeskripsi(cursor.getString(5));
					salesOrder.setId_promosi(cursor.getInt(6));
					salesOrder.setUsername(cursor.getString(7));
					salesOrder.setKode_customer(cursor.getString(8));
					salesOrder.setAlamat(cursor.getString(9));
					salesOrder.setNama_lengkap(cursor.getString(10));
					salesOrder.setNama_product(cursor.getString(11));
					salesOrder.setKode_product(cursor.getString(12));
					salesOrder.setHarga_jual(cursor.getString(13));
					salesOrder.setJumlah_order(cursor.getString(14));
					salesOrder.setJumlah_order1(cursor.getString(15));
					salesOrder.setJumlah_order2(cursor.getString(16));
					salesOrder.setJumlah_order3(cursor.getString(17));
					salesOrder.setId_wilayah(cursor.getInt(18));

					// Adding sales_order_list to list
					sales_order_list.add(salesOrder);
				} while (cursor.moveToNext());
			}

			// return sales_order_list
			cursor.close();
			db.close();
			return sales_order_list;
		} catch (Exception e) {
			Log.e("sales_order_list", "" + e);
		}

		return sales_order_list;
	}

	//Getting Req Load
	public ArrayList<ReqLoad> getAllReqLoad() {
		try {
			reqload_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_REQLOAD;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ReqLoad reqLoad = new ReqLoad();
					reqLoad.setId_sales_order(cursor.getInt(0));
					reqLoad.setNomer_request_load(cursor.getString(1));
					reqLoad.setDate_order(cursor.getString(2));
					reqLoad.setTime_order(cursor.getString(3));
					reqLoad.setId_promosi(cursor.getInt(4));
					reqLoad.setUsername(cursor.getString(5));
					reqLoad.setSatuan_terkecil(cursor.getString(6));
					reqLoad.setNama_product(cursor.getString(7));
					reqLoad.setJumlah_order(cursor.getString(8));
					reqLoad.setJumlah_order1(cursor.getString(9));
					reqLoad.setJumlah_order2(cursor.getString(10));
					reqLoad.setJumlah_order3(cursor.getString(11));
					reqLoad.setId_staff(cursor.getInt(12));
					reqLoad.setId_product(cursor.getInt(13));
					reqLoad.setTtd1(cursor.getString(14));

					// Adding reqload_list to list
					reqload_list.add(reqLoad);
				} while (cursor.moveToNext());
			}

			// return reqload_list
			cursor.close();
			db.close();
			return reqload_list;
		} catch (Exception e) {
			Log.e("reqload_list", "" + e);
		}

		return reqload_list;
	}

	// Getting All retur
	public ArrayList<Retur> getAllRetur() {
		try {
			retur_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_RETUR;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Retur retur = new Retur();
					retur.setId_retur(cursor.getInt(0));
					retur.setNomer_retur(cursor.getString(1));
					retur.setNomer_retur_detail(cursor.getString(2));
					retur.setDate_retur(cursor.getString(3));
					retur.setTime_retur(cursor.getString(4));
					retur.setDeskripsi(cursor.getString(5));
					retur.setId_promosi(cursor.getInt(6));
					retur.setUsername(cursor.getString(7));
					retur.setKode_customer(cursor.getString(8));
					retur.setAlamat(cursor.getString(9));
					retur.setNama_lengkap(cursor.getString(10));
					retur.setNama_product(cursor.getString(11));
					retur.setKode_product(cursor.getString(12));
					retur.setHarga_jual(cursor.getString(13));
					retur.setJumlah_retur(cursor.getString(14));

					// Adding retur_list to list
					retur_list.add(retur);
				} while (cursor.moveToNext());
			}

			// return retur_list
			cursor.close();
			db.close();
			return retur_list;
		} catch (Exception e) {
			Log.e("retur_list", "" + e);
		}

		return retur_list;
	}



	// Getting All Sales Order
	public ArrayList<SalesOrder> getAllSalesOrderGroupByNomerOrder() {
		try {
			sales_order_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_SALES_ORDER
					+ " GROUP BY " + KEY_SALES_ORDER_NOMER_ORDER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					SalesOrder salesOrder = new SalesOrder();
					salesOrder.setId_sales_order(cursor.getInt(0));
					salesOrder.setNomer_order(cursor.getString(1));
					salesOrder.setNomer_order_detail(cursor.getString(2));
					salesOrder.setDate_order(cursor.getString(3));
					salesOrder.setTime_order(cursor.getString(4));
					salesOrder.setDeskripsi(cursor.getString(5));
					salesOrder.setId_promosi(cursor.getInt(6));
					salesOrder.setUsername(cursor.getString(7));
					salesOrder.setKode_customer(cursor.getString(8));
					salesOrder.setAlamat(cursor.getString(9));
					salesOrder.setNama_lengkap(cursor.getString(10));
					salesOrder.setNama_product(cursor.getString(11));
					salesOrder.setKode_product(cursor.getString(12));
					salesOrder.setHarga_jual(cursor.getString(13));
					salesOrder.setJumlah_order(cursor.getString(14));
					salesOrder.setJumlah_order1(cursor.getString(15));
					salesOrder.setJumlah_order2(cursor.getString(16));
					salesOrder.setJumlah_order3(cursor.getString(17));
					salesOrder.setId_wilayah(cursor.getInt(18));

					// Adding sales_order_list to list
					sales_order_list.add(salesOrder);
				} while (cursor.moveToNext());
			}

			// return sales_order_list
			cursor.close();
			db.close();
			return sales_order_list;
		} catch (Exception e) {
			Log.e("sales_order_list", "" + e);
		}

		return sales_order_list;
	}

	// Getting All ReqLoad
	public ArrayList<ReqLoad> getAllReqLoadGroupByNomerOrder() {
		try {
			reqload_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_REQLOAD
					+ " GROUP BY " + KEY_SALES_ORDER_NOMER_REQUEST_LOAD;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ReqLoad reqLoad = new ReqLoad();
					reqLoad.setId_sales_order(cursor.getInt(0));
					reqLoad.setNomer_request_load(cursor.getString(1));
					reqLoad.setDate_order(cursor.getString(2));
					reqLoad.setTime_order(cursor.getString(3));
					reqLoad.setId_promosi(cursor.getInt(4));
					reqLoad.setUsername(cursor.getString(5));
					reqLoad.setSatuan_terkecil(cursor.getString(6));
					reqLoad.setNama_product(cursor.getString(7));
					reqLoad.setJumlah_order(cursor.getString(8));
					reqLoad.setJumlah_order1(cursor.getString(9));
					reqLoad.setJumlah_order2(cursor.getString(10));
					reqLoad.setJumlah_order3(cursor.getString(11));
					reqLoad.setId_staff(cursor.getInt(12));
					reqLoad.setId_product(cursor.getInt(13));
					reqLoad.setTtd1(cursor.getString(14));

					// Adding sales_order_list to list
					reqload_list.add(reqLoad);
				} while (cursor.moveToNext());
			}

			// return reqload_list
			cursor.close();
			db.close();
			return reqload_list;
		} catch (Exception e) {
			Log.e("reqload_list", "" + e);
		}

		return reqload_list;
	}


	// Getting All retur
	public ArrayList<Retur> getAllReturGroupByNomerOrder() {
		try {
			retur_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_RETUR
					+ " GROUP BY " + KEY_RETUR_NOMER_RETUR;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Retur retur = new Retur();
					retur.setId_retur(cursor.getInt(0));
					retur.setNomer_retur(cursor.getString(1));
					retur.setNomer_retur_detail(cursor.getString(2));
					retur.setDate_retur(cursor.getString(3));
					retur.setTime_retur(cursor.getString(4));
					retur.setDeskripsi(cursor.getString(5));
					retur.setId_promosi(cursor.getInt(6));
					retur.setUsername(cursor.getString(7));
					retur.setKode_customer(cursor.getString(8));
					retur.setAlamat(cursor.getString(9));
					retur.setNama_lengkap(cursor.getString(10));
					retur.setNama_product(cursor.getString(11));
					retur.setKode_product(cursor.getString(12));
					retur.setHarga_jual(cursor.getString(13));
					retur.setJumlah_retur(cursor.getString(14));

					// Adding retur_list to list
					retur_list.add(retur);
				} while (cursor.moveToNext());
			}

			// return retur_list
			cursor.close();
			db.close();
			return retur_list;
		} catch (Exception e) {
			Log.e("retur_list", "" + e);
		}

		return retur_list;
	}

	// Getting All Sales Order
	public ArrayList<SalesOrder> getAllSalesOrderWhereNomerOrder(
			String nomer_order) {
		try {
			sales_order_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_SALES_ORDER
					+ " WHERE nomer_order ='" + nomer_order + "' ";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					SalesOrder salesOrder = new SalesOrder();
					salesOrder.setId_sales_order(cursor.getInt(0));
					salesOrder.setNomer_order(cursor.getString(1));
					salesOrder.setNomer_order_detail(cursor.getString(2));
					salesOrder.setDate_order(cursor.getString(3));
					salesOrder.setTime_order(cursor.getString(4));
					salesOrder.setDeskripsi(cursor.getString(5));
					salesOrder.setId_promosi(cursor.getInt(6));
					salesOrder.setUsername(cursor.getString(7));
					salesOrder.setKode_customer(cursor.getString(8));
					salesOrder.setAlamat(cursor.getString(9));
					salesOrder.setNama_lengkap(cursor.getString(10));
					salesOrder.setNama_product(cursor.getString(11));
					salesOrder.setKode_product(cursor.getString(12));
					salesOrder.setHarga_jual(cursor.getString(13));
					salesOrder.setJumlah_order(cursor.getString(14));
					salesOrder.setJumlah_order1(cursor.getString(15));
					salesOrder.setJumlah_order2(cursor.getString(16));
					salesOrder.setJumlah_order3(cursor.getString(17));
					salesOrder.setId_wilayah(cursor.getInt(18));

					// Adding sales_order_list to list
					sales_order_list.add(salesOrder);
				} while (cursor.moveToNext());
			}

			// return sales_order_list
			cursor.close();
			db.close();
			return sales_order_list;
		} catch (Exception e) {
			Log.e("sales_order_list", "" + e);
		}

		return sales_order_list;
	}


	// Getting All reqload
	public ArrayList<ReqLoad> getAllReqLoadWhereNomerOrder(
			String nomer_order) {
		try {
			reqload_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_REQLOAD
					+ " WHERE nomer_order ='" + nomer_order + "' ";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ReqLoad reqLoad = new ReqLoad();
					reqLoad.setId_sales_order(cursor.getInt(0));
					reqLoad.setNomer_request_load(cursor.getString(1));
					reqLoad.setDate_order(cursor.getString(2));
					reqLoad.setTime_order(cursor.getString(3));
					reqLoad.setId_promosi(cursor.getInt(4));
					reqLoad.setUsername(cursor.getString(5));
					reqLoad.setSatuan_terkecil(cursor.getString(6));
					reqLoad.setNama_product(cursor.getString(7));
					reqLoad.setJumlah_order(cursor.getString(8));
					reqLoad.setJumlah_order1(cursor.getString(9));
					reqLoad.setJumlah_order2(cursor.getString(10));
					reqLoad.setJumlah_order3(cursor.getString(11));
					reqLoad.setId_staff(cursor.getInt(12));
					reqLoad.setId_product(cursor.getInt(13));
					reqLoad.setTtd1(cursor.getString(14));

					// Adding reqload_list to list
					reqload_list.add(reqLoad);
				} while (cursor.moveToNext());
			}

			// return reqload_list
			cursor.close();
			db.close();
			return reqload_list;
		} catch (Exception e) {
			Log.e("reqload_list", "" + e);
		}

		return reqload_list;
	}

	// Getting All Retur
	public ArrayList<Retur> getAllReturWhereNomerOrder(
			String nomer_retur) {
		try {
			retur_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_RETUR
					+ " WHERE nomer_retur ='" + nomer_retur + "' ";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Retur retur = new Retur();
					retur.setId_retur(cursor.getInt(0));
					retur.setNomer_retur(cursor.getString(1));
					retur.setNomer_retur_detail(cursor.getString(2));
					retur.setDate_retur(cursor.getString(3));
					retur.setTime_retur(cursor.getString(4));
					retur.setDeskripsi(cursor.getString(5));
					retur.setId_promosi(cursor.getInt(6));
					retur.setUsername(cursor.getString(7));
					retur.setKode_customer(cursor.getString(8));
					retur.setAlamat(cursor.getString(9));
					retur.setNama_lengkap(cursor.getString(10));
					retur.setNama_product(cursor.getString(11));
					retur.setKode_product(cursor.getString(12));
					retur.setHarga_jual(cursor.getString(13));
					retur.setJumlah_retur(cursor.getString(14));

					// Adding retur_list to list
					retur_list.add(retur);
				} while (cursor.moveToNext());
			}

			// return retur_list
			cursor.close();
			db.close();
			return retur_list;
		} catch (Exception e) {
			Log.e("retur_list", "" + e);
		}

		return retur_list;
	}

	// Getting All Tracking
	public ArrayList<Tracking> getAllTracking() {
		try {
			tracking_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRACKING;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Tracking tracking = new Tracking();
					tracking.setId_locator(cursor.getInt(0));
					tracking.setUsername(cursor.getString(1));
					tracking.setNama_lengkap(cursor.getString(2));
					tracking.setLevel(cursor.getInt(3));
					tracking.setLats(cursor.getString(4));
					tracking.setLongs(cursor.getString(5));
					tracking.setAddress(cursor.getString(6));
					tracking.setImei(cursor.getString(7));
					tracking.setMcc(cursor.getString(8));
					tracking.setMnc(cursor.getString(9));
					tracking.setDate(cursor.getString(10));
					tracking.setTime(cursor.getString(11));
					// Adding tracking_list to list
					tracking_list.add(tracking);
				} while (cursor.moveToNext());
			}

			// return tracking_list
			cursor.close();
			db.close();
			return tracking_list;
		} catch (Exception e) {
			Log.e("tracking_list", "" + e);
		}

		return tracking_list;
	}

	// Getting All Tracking
	public ArrayList<TrackingLogs> getAllTrackingLogs() {
		try {
			tracking_logs_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TRACKING_LOGS;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TrackingLogs tracking = new TrackingLogs();
					tracking.setId_locator(cursor.getInt(0));
					tracking.setUsername(cursor.getString(1));
					tracking.setNama_lengkap(cursor.getString(2));
					tracking.setLevel(cursor.getInt(3));
					tracking.setLats(cursor.getString(4));
					tracking.setLongs(cursor.getString(5));
					tracking.setAddress(cursor.getString(6));
					tracking.setImei(cursor.getString(7));
					tracking.setMcc(cursor.getString(8));
					tracking.setMnc(cursor.getString(9));
					tracking.setDate(cursor.getString(10));
					tracking.setTime(cursor.getString(11));
					// Adding tracking_logs_list to list
					tracking_logs_list.add(tracking);
				} while (cursor.moveToNext());
			}

			// return tracking_logs_list
			cursor.close();
			db.close();
			return tracking_logs_list;
		} catch (Exception e) {
			Log.e("tracking_logs_list", "" + e);
		}

		return tracking_logs_list;
	}


	// Update StockVan
	public int updateStockVanJumlahSisa(int id, int jumlah_sisa) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_STOCK_VAN_JUMLAH_SISA, jumlah_sisa);
		// updating row
		return db.update(TABLE_STOCK_VAN, values, KEY_STOCK_VAN_ID_PRODUCT
				+ " = ?", new String[] { String.valueOf(id) });
	}

	// Update Target Penjualan
	public int updateTargetPenjualan(int idProduct, int jumlahPenjualan,
			String updateDate, String updateTime, int updateBy, String nomerTp) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_TARGET_JUMLAH_TERJUAL, jumlahPenjualan);
		values.put(KEY_PRODUCT_TARGET_UD_PRODUCT_TARGET, updateDate);
		values.put(KEY_PRODUCT_TARGET_UT_PRODUCT_TARGET, updateTime);
		values.put(KEY_PRODUCT_TARGET_UB, updateBy);
		// updating row
		return db.update(TABLE_PRODUCT_TARGET, values,
				KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET + " = ? AND "
				+ KEY_PRODUCT_TARGET_ID_PRODUCT + " = ?",
				new String[] { String.valueOf(nomerTp), String.valueOf(idProduct) });
	}

	public int add_ttd1(String kode, Customer customer){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();

		values.put(KEY_CUSTOMER_TTD1, customer.getTtd1());

		return db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_KODE_CUSTOMER
				+ " = ?", new String[] { String.valueOf(kode) });
	}

	// Update Customer
	public int updateCustomer(int id, Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CUSTOMER_KODE_CUSTOMER, customer.getKode_customer());
		values.put(KEY_CUSTOMER_EMAIL_CUSTOMER, customer.getEmail());
		values.put(KEY_CUSTOMER_ALAMAT_CUSTOMER, customer.getAlamat());
		values.put(KEY_CUSTOMER_LATS_CUSTOMER, customer.getLats());
		values.put(KEY_CUSTOMER_LONGS_CUSTOMER, customer.getLongs());
		values.put(KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
				customer.getNama_lengkap());
		values.put(KEY_CUSTOMER_NO_TELP_CUSTOMER, customer.getNo_telp());
		values.put(KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, customer.getId_wilayah());
		values.put(KEY_CUSTOMER_FOTO_1_CUSTOMER, customer.getFoto_1());
		values.put(KEY_CUSTOMER_FOTO_2_CUSTOMER, customer.getFoto_2());
		values.put(KEY_CUSTOMER_FOTO_3_CUSTOMER, customer.getFoto_3());
		values.put(KEY_CUSTOMER_ID_TYPE_CUSTOMER, customer.getId_type_customer());
		values.put(KEY_CUSTOMER_BLOKIR, customer.getBlokir());
		values.put(KEY_CUSTOMER_DATE, customer.getDate());
		values.put(KEY_CUSTOMER_STATUS_UPDATE, customer.getStatus_update());
		values.put(KEY_CUSTOMER_ID_STAFF, customer.getId_staff());
		values.put(KEY_CUSTOMER_NO_KTP, customer.getNo_ktp());
		values.put(KEY_CUSTOMER_TANGGAL_LAHIR, customer.getTanggal_lahir());
		values.put(KEY_CUSTOMER_NAMA_BANK, customer.getNama_bank());
		values.put(KEY_CUSTOMER_NO_REKENING, customer.getNo_rekening());
		values.put(KEY_CUSTOMER_ATAS_NAMA, customer.getAtas_nama());
		values.put(KEY_CUSTOMER_NPWP, customer.getNpwp());
		values.put(KEY_CUSTOMER_NAMA_PASAR, customer.getNama_pasar());
		values.put(KEY_CUSTOMER_ID_CLUSTER, customer.getId_cluster());
		values.put(KEY_CUSTOMER_TELP, customer.getTelp());
		values.put(KEY_CUSTOMER_FAX, customer.getFax());
		values.put(KEY_CUSTOMER_OMSET, customer.getOmset());
		values.put(KEY_CUSTOMER_CARA_PEMBAYARAN, customer.getCara_pembayaran());
		values.put(KEY_CUSTOMER_PLAFON_KREDIT, customer.getPlafon_kredit());
		values.put(KEY_CUSTOMER_TERM_KREDIT, customer.getTerm_kredit());

		values.put(KEY_CUSTOMER_NAMA_ISTRI, customer.getNama_istri());
		values.put(KEY_CUSTOMER_NAMA_ANAK1, customer.getNama_anak1());
		values.put(KEY_CUSTOMER_NAMA_ANAK2, customer.getNama_anak2());
		values.put(KEY_CUSTOMER_NAMA_ANAK3, customer.getNama_anak3());
		values.put(KEY_CUSTOMER_KODE_POS, customer.getKode_pos());
		values.put(KEY_CUSTOMER_ID_DEPO, customer.getId_depo());
		values.put(KEY_CUSTOMER_ISACTIVE, customer.getIsactive());
		values.put(KEY_CUSTOMER_DESCRIPTION, customer.getDescription());
		values.put(KEY_CUSTOMER_NAMA_TOKO, customer.getNama_toko());
		values.put(KEY_CUSTOMER_TTD1, customer.getTtd1());
		values.put(KEY_CUSTOMER_TTD2, customer.getTtd2());


		// updating row
		return db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID_CUSTOMER
				+ " = ?", new String[] { String.valueOf(id) });
	}
	// Update product
	public int updateProduct(int id, Product product) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_PRODUCT_NAMA_PRODUCT, product.getNama_product());
		values.put(KEY_PRODUCT_KODE_PRODUCT, product.getKode_product());
		values.put(KEY_PRODUCT_HARGA_JUAL, product.getHarga_jual());
		values.put(KEY_PRODUCT_STOCK, product.getStock());
		values.put(KEY_KEMASAN_ID_KEMASAN, product.getId_kemasan());
		values.put(KEY_PRODUCT_FOTO, product.getFoto());
		values.put(KEY_PRODUCT_DESKRIPSI, product.getDeskripsi());
		values.put(KEY_PRODUCT_UOMQTYL1, product.getUomqtyl1());
		values.put(KEY_PRODUCT_UOMQTYL2, product.getUomqtyl2());
		values.put(KEY_PRODUCT_UOMQTYL3, product.getUomqtyl3());
		values.put(KEY_PRODUCT_UOMQTYL4, product.getUomqtyl4());
		values.put(KEY_PRODUCT_STATUS, "2");

		// updating row
		return db.update(TABLE_PRODUCT, values, KEY_PRODUCT_ID_PRODUCT
				+ " = ?", new String[] { String.valueOf(id) });
	}

	//update status update customer
	public int updateCustomer1(int id, Customer customer) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_CUSTOMER_KODE_CUSTOMER, customer.getKode_customer());
		values.put(KEY_CUSTOMER_EMAIL_CUSTOMER, customer.getEmail());
		values.put(KEY_CUSTOMER_ALAMAT_CUSTOMER, customer.getAlamat());
		values.put(KEY_CUSTOMER_LATS_CUSTOMER, customer.getLats());
		values.put(KEY_CUSTOMER_LONGS_CUSTOMER, customer.getLongs());

		values.put(KEY_CUSTOMER_NAMA_LENGKAP_CUSTOMER,
				customer.getNama_lengkap());
		values.put(KEY_CUSTOMER_NO_TELP_CUSTOMER, customer.getNo_telp());
		values.put(KEY_CUSTOMER_ID_WILAYAH_CUSTOMER, customer.getId_wilayah());
		values.put(KEY_CUSTOMER_FOTO_1_CUSTOMER, customer.getFoto_1());
		values.put(KEY_CUSTOMER_FOTO_2_CUSTOMER, customer.getFoto_2());
		values.put(KEY_CUSTOMER_FOTO_3_CUSTOMER, customer.getFoto_3());
		values.put(KEY_CUSTOMER_ID_TYPE_CUSTOMER, customer.getId_type_customer());
		values.put(KEY_CUSTOMER_BLOKIR, customer.getBlokir());
		values.put(KEY_CUSTOMER_DATE, customer.getDate());
		values.put(KEY_CUSTOMER_STATUS_UPDATE, customer.getStatus_update());
		values.put(KEY_CUSTOMER_ID_STAFF, customer.getId_staff());
		values.put(KEY_CUSTOMER_NO_KTP, customer.getNo_ktp());
		values.put(KEY_CUSTOMER_TANGGAL_LAHIR, customer.getTanggal_lahir());
		values.put(KEY_CUSTOMER_NAMA_BANK, customer.getNama_bank());
		values.put(KEY_CUSTOMER_NO_REKENING, customer.getNo_rekening());
		values.put(KEY_CUSTOMER_ATAS_NAMA, customer.getAtas_nama());
		values.put(KEY_CUSTOMER_NPWP, customer.getNpwp());
		values.put(KEY_CUSTOMER_NAMA_PASAR, customer.getNama_pasar());
		values.put(KEY_CUSTOMER_ID_CLUSTER, customer.getId_cluster());
		values.put(KEY_CUSTOMER_TELP, customer.getTelp());
		values.put(KEY_CUSTOMER_FAX, customer.getFax());
		values.put(KEY_CUSTOMER_OMSET, customer.getOmset());
		values.put(KEY_CUSTOMER_CARA_PEMBAYARAN, customer.getCara_pembayaran());
		values.put(KEY_CUSTOMER_PLAFON_KREDIT, customer.getPlafon_kredit());
		values.put(KEY_CUSTOMER_TERM_KREDIT, customer.getTerm_kredit());

		values.put(KEY_CUSTOMER_NAMA_ISTRI, customer.getNama_istri());
		values.put(KEY_CUSTOMER_NAMA_ANAK1, customer.getNama_anak1());
		values.put(KEY_CUSTOMER_NAMA_ANAK2, customer.getNama_anak2());
		values.put(KEY_CUSTOMER_NAMA_ANAK3, customer.getNama_anak3());
		values.put(KEY_CUSTOMER_KODE_POS, customer.getKode_pos());
		values.put(KEY_CUSTOMER_ID_DEPO, customer.getId_depo());


		// updating row
		return db.update(TABLE_CUSTOMER, values, KEY_CUSTOMER_ID_CUSTOMER
				+ " = ?", new String[] { String.valueOf(id) });
	}

	// Update Customer
	public int updateJadwal(int id, Jadwal jadwal) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_JADWAL_ID_JADWAL, jadwal.getId_jadwal());
		values.put(KEY_JADWAL_KODE_JADWAL, jadwal.getKode_jadwal());
		values.put(KEY_JADWAL_KODE_CUSTOMER, jadwal.getKode_customer());
		values.put(KEY_JADWAL_USERNAME, jadwal.getUsername());
		values.put(KEY_JADWAL_ALAMAT, jadwal.getAlamat());
		values.put(KEY_JADWAL_NAMA_TOKO, jadwal.getNama_toko());
		values.put(KEY_JADWAL_ID_WILAYAH, jadwal.getId_wilayah());
		values.put(KEY_JADWAL_STATUS, jadwal.getStatus());
		values.put(KEY_JADWAL_DATE, jadwal.getDate());
		values.put(KEY_JADWAL_CHECK_IN, jadwal.getCheckin());
		values.put(KEY_JADWAL_CHECK_OUT, jadwal.getCheckout());
		values.put(KEY_JADWAL_STATUS_UPDATE, jadwal.getStatus_update());
		values.put(KEY_JADWAL_KETERANGAN, jadwal.getKeterangan());

		// updating row
		return db.update(TABLE_JADWAL, values, KEY_JADWAL_ID_JADWAL + " = ?",
				new String[] { String.valueOf(id) });
	}

	// Update displayProduct
	public int updateDisplayProduct(int id, DisplayProduct displayProduct) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_DISPLAY_PRODUCT_KODE_CUSTOMER,
				displayProduct.getKode_customer());
		values.put(KEY_DISPLAY_PRODUCT_NAMA_LENGKAP,
				displayProduct.getNama_lengkap());
		values.put(KEY_DISPLAY_PRODUCT_FOTO, displayProduct.getFoto());
		values.put(KEY_DISPLAY_PRODUCT_NAMA_DISPLAY_PRODUCT,
				displayProduct.getNama_display_product());
		values.put(KEY_DISPLAY_PRODUCT_DESKRIPSI, displayProduct.getDeskripsi());
		values.put(KEY_DISPLAY_PRODUCT_DATE_TIME, displayProduct.getDatetime());

		// updating row
		return db.update(TABLE_DISPLAY_PRODUCT, values,
				KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT + " = ?",
				new String[] { String.valueOf(id) });
	}

	// // Update photoPurchase
	// public int updatePhotoPurchase(int id, PhotoPurchase photoPurchase) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// ContentValues values = new ContentValues();
	// values.put(KEY_PHOTO_PURCHASE_KODE_CUSTOMER,
	// photoPurchase.getKode_customer());
	// values.put(KEY_PHOTO_PURCHASE_NAMA_LENGKAP,
	// photoPurchase.getNama_lengkap());
	// values.put(KEY_PHOTO_PURCHASE_FOTO, photoPurchase.getFoto());
	// values.put(KEY_PHOTO_PURCHASE_NAMA_PHOTO_PURCHASE,
	// photoPurchase.getNama_photo_purchase());
	// values.put(KEY_PHOTO_PURCHASE_DESKRIPSI, photoPurchase.getDeskripsi());
	// values.put(KEY_PHOTO_PURCHASE_DATE_TIME, photoPurchase.getDatetime());
	//
	// // updating row
	// return db.update(TABLE_PHOTO_PURCHASE, values,
	// KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE + " = ?",
	// new String[] { String.valueOf(id) });
	// }

	// Getting All ProductStockVan
	public ArrayList<ProductStockVan> getAllProductStokVan() {
		try {
			productStockVanList.clear();
			// Select All Query
			// String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + ", "
			// + TABLE_STOCK_VAN + "." + KEY_STOCK_VAN_JUMLAH_ACCEPT
			// + " WHERE " + TABLE_PRODUCT + "." + KEY_PRODUCT_ID_PRODUCT
			// + "=" + TABLE_STOCK_VAN + "." + KEY_STOCK_VAN_ID_PRODUCT;


			String selectQuery = "SELECT * FROM " + TABLE_PRODUCT + ", "
					+ TABLE_STOCK_VAN + " WHERE " + TABLE_PRODUCT + "."
					+ KEY_PRODUCT_ID_PRODUCT + "=" + TABLE_STOCK_VAN + "."
					+ KEY_STOCK_VAN_ID_PRODUCT;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ProductStockVan productStockVan = new ProductStockVan();
					productStockVan.setId_product(cursor.getInt(0));
					productStockVan.setNama_product(cursor.getString(1));
					productStockVan.setKode_product(cursor.getString(2));
					productStockVan.setHarga_jual(cursor.getString(3));
					productStockVan.setStockGudang(Integer.parseInt(cursor.getString(4)));
					productStockVan.setIdKemasan(cursor.getString(5));
					productStockVan.setFoto(cursor.getString(6));
					productStockVan.setDeskripsi(cursor.getString(7));
					productStockVan.setStockVan(cursor.getString(17));
					// Adding productStockVanList to list
					productStockVanList.add(productStockVan);
				} while (cursor.moveToNext());
			}
			// return productStockVanList
			cursor.close();
			db.close();
			return productStockVanList;
		} catch (Exception e) {
			Log.e("productStockVanList", "" + e);
		}
		return productStockVanList;
	}
	// Getting All StockVan
	public ArrayList<StockVan> getAllStockVan() {
		try {
			stock_van_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STOCK_VAN;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StockVan stockVan = new StockVan();
					stockVan.setId_product(cursor.getInt(0));
					stockVan.setNama_product(cursor.getString(1));
					stockVan.setKode_product(cursor.getString(2));
					stockVan.setHarga_jual(cursor.getString(3));
					stockVan.setJumlahRequest(cursor.getString(4));
					stockVan.setJumlahAccept(cursor.getString(5));
					stockVan.setJumlahSisa(cursor.getString(6));
					stockVan.setIdKemasan(cursor.getString(7));
					stockVan.setFoto(cursor.getString(8));
					stockVan.setDeskripsi(cursor.getString(9));
					stockVan.setNomer_request_load(cursor.getString(10));
					// Adding stock_van to list
					stock_van_list.add(stockVan);
				} while (cursor.moveToNext());
			}
			// return stock_van_list
			cursor.close();
			db.close();
			return stock_van_list;
		} catch (Exception e) {
			Log.e("stock_van_list", "" + e);
		}
		return stock_van_list;
	}

	// Getting All Product
	public ArrayList<Product> getAllProduct() {
		try {
			product_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " limit 50";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Product product = new Product();
					product.setId_product(cursor.getInt(0));
					product.setNama_product(cursor.getString(1));
					product.setKode_product(cursor.getString(2));
					product.setHarga_jual(cursor.getString(3));
					product.setStock(cursor.getString(4));
					product.setId_kemasan(cursor.getString(5));
					product.setFoto(cursor.getString(6));
					product.setDeskripsi(cursor.getString(7));
					product.setUomqtyl1(cursor.getString(8));
					product.setUomqtyl2(cursor.getString(9));
					product.setUomqtyl3(cursor.getString(10));
					product.setUomqtyl4(cursor.getString(11));
					product.setStatus(cursor.getString(12));

					// Adding product to list
					product_list.add(product);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return product_list;
		} catch (Exception e) {
			Log.e("product_list", "" + e);
		}

		return product_list;
	}

	// Getting All Product
	public ArrayList<Product> getAllProductBaseOnSearch(String search) {
		try {
			product_list.clear();


			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT + " WHERE "
					+ KEY_PRODUCT_NAMA_PRODUCT + " LIKE '%" + search + "%' OR kode_product like'%"+search+"%'";

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Product product = new Product();
					product.setId_product(cursor.getInt(0));
					product.setNama_product(cursor.getString(1));
					product.setKode_product(cursor.getString(2));
					product.setHarga_jual(cursor.getString(3));
					product.setStock(cursor.getString(4));
					product.setId_kemasan(cursor.getString(5));
					product.setFoto(cursor.getString(6));
					product.setDeskripsi(cursor.getString(7));
					product.setUomqtyl1(cursor.getString(8));
					product.setUomqtyl2(cursor.getString(9));
					product.setUomqtyl3(cursor.getString(10));
					product.setUomqtyl4(cursor.getString(11));
					product.setStatus(cursor.getString(12));

					// Adding product to list
					product_list.add(product);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return product_list;
		} catch (Exception e) {
			Log.e("product_list", "" + e);
		}

		return product_list;
	}


	// Getting All Product
	public ArrayList<ProductPrice> getAllProductPrice() {
		try {
			product_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT_PRICE;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					ProductPrice productPrice = new ProductPrice();
					productPrice.setId(cursor.getInt(0));
					productPrice.setId_product(cursor.getString(1));
					productPrice.setPricelist(cursor.getString(2));
					productPrice.setPricestd(cursor.getString(3));
					productPrice.setPricelimit(cursor.getString(4));

					// Adding product to list
					product_price_list.add(productPrice);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return product_price_list;
		} catch (Exception e) {
			Log.e("product_price_list", "" + e);
		}

		return product_price_list;
	}

	// Getting All Dtg
	public ArrayList<Dtg> getAllDtg() {
		try {
			dtg_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_DTG;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Dtg dtg = new Dtg();
					dtg.setId(cursor.getInt(0));
					dtg.setKode_customer(cursor.getString(1));
					dtg.setNama_toko(cursor.getString(2));
					dtg.setNo_faktur(cursor.getString(3));
					dtg.setNilai_faktur(cursor.getString(4));
					dtg.setJml_belum_bayar(cursor.getString(5));
					dtg.setNo_dtg(cursor.getString(6));

					// Adding product to list
					dtg_list.add(dtg);
				} while (cursor.moveToNext());
			}

			// return product_list
			cursor.close();
			db.close();
			return dtg_list;
		} catch (Exception e) {
			Log.e("dtg_list", "" + e);
		}

		return dtg_list;
	}

	// Getting All StockVan
	public ArrayList<StockVan> getAllStockVanBaseOnSearch(String search) {
		try {
			stock_van_list.clear();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_STOCK_VAN
					+ " WHERE " + KEY_STOCK_VAN_NAMA_PRODUCT + " LIKE '%"
					+ search + "%'";
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					StockVan stockVan = new StockVan();
					stockVan.setId_product(cursor.getInt(0));
					stockVan.setNama_product(cursor.getString(1));
					stockVan.setKode_product(cursor.getString(2));
					stockVan.setHarga_jual(cursor.getString(3));
					stockVan.setJumlahRequest(cursor.getString(4));
					stockVan.setJumlahAccept(cursor.getString(5));
					stockVan.setJumlahSisa(cursor.getString(6));
					stockVan.setIdKemasan(cursor.getString(7));
					stockVan.setFoto(cursor.getString(8));
					stockVan.setDeskripsi(cursor.getString(9));
					stockVan.setNomer_request_load(cursor.getString(10));
					// Adding stock van to list
					stock_van_list.add(stockVan);
				} while (cursor.moveToNext());
			}
			// return stock_van_list
			cursor.close();
			db.close();
			return stock_van_list;
		} catch (Exception e) {
			Log.e("stock_van_list", "" + e);
		}
		return stock_van_list;
	}

	// // Getting All Promosi
	// public ArrayList<Promosi> getAllPromosi() {
	// try {
	// promosi_list.clear();
	//
	// // Select All Query
	// String selectQuery = "SELECT  * FROM " + TABLE_PROMOSI;
	//
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// Promosi promosi = new Promosi();
	// promosi.setId_promosi(cursor.getInt(0));
	// promosi.setTitle_promosi(cursor.getString(1));
	// promosi.setJenis_promosi(cursor.getString(2));
	// promosi.setJumlah_diskon(cursor.getInt(3));
	// promosi.setDeskripsi(cursor.getString(4));
	// promosi.setFoto(cursor.getString(5));
	// promosi.setDatetime(cursor.getString(6));
	//
	// // Adding promosi_list to list
	// promosi_list.add(promosi);
	// } while (cursor.moveToNext());
	// }
	//
	// // return promosi_list
	// cursor.close();
	// db.close();
	// return promosi_list;
	// } catch (Exception e) {
	// Log.e("promosi_list", "" + e);
	// }
	//
	// return promosi_list;
	// }
	//
	// // Getting All Promosi
	// public ArrayList<Promosi> getAllPromosiWhereDiskon() {
	// try {
	// promosi_list.clear();
	//
	// // Select All Query
	// String selectQuery = "SELECT  * FROM " + TABLE_PROMOSI + " WHERE "
	// + KEY_PROMOSI_JENIS_PROMOSI + "='Diskon'";
	//
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// Promosi promosi = new Promosi();
	// promosi.setId_promosi(cursor.getInt(0));
	// promosi.setTitle_promosi(cursor.getString(1));
	// promosi.setJenis_promosi(cursor.getString(2));
	// promosi.setJumlah_diskon(cursor.getInt(3));
	// promosi.setDeskripsi(cursor.getString(4));
	// promosi.setFoto(cursor.getString(5));
	// promosi.setDatetime(cursor.getString(6));
	//
	// // Adding promosi_list to list
	// promosi_list.add(promosi);
	// } while (cursor.moveToNext());
	// }
	//
	// // return promosi_list
	// cursor.close();
	// db.close();
	// return promosi_list;
	// } catch (Exception e) {
	// Log.e("promosi_list", "" + e);
	// }
	//
	// return promosi_list;
	// }

	// Getting All DisplayProduct
	public ArrayList<DisplayProduct> getAllDisplayProduct() {
		try {
			display_product_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_DISPLAY_PRODUCT;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					DisplayProduct displayProduct = new DisplayProduct();
					displayProduct.setId_display_product(cursor.getInt(0));
					displayProduct.setKode_customer(cursor.getString(1));
					displayProduct.setNama_lengkap(cursor.getString(2));
					displayProduct.setFoto(cursor.getString(3));
					displayProduct.setNama_display_product(cursor.getString(4));
					displayProduct.setDeskripsi(cursor.getString(5));
					displayProduct.setDatetime(cursor.getString(6));

					// Adding display_product_list to list
					display_product_list.add(displayProduct);
				} while (cursor.moveToNext());
			}

			// return display_product_list
			cursor.close();
			db.close();
			return display_product_list;
		} catch (Exception e) {
			Log.e("display_product_list", "" + e);
		}

		return display_product_list;
	}

	// // Getting All PhotoPurchase
	// public ArrayList<PhotoPurchase> getAllPhotoPurchase() {
	// try {
	// photo_purchase_list.clear();
	//
	// // Select All Query
	// String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_PURCHASE;
	//
	// SQLiteDatabase db = this.getWritableDatabase();
	// Cursor cursor = db.rawQuery(selectQuery, null);
	//
	// // looping through all rows and adding to list
	// if (cursor.moveToFirst()) {
	// do {
	// PhotoPurchase photoPurchase = new PhotoPurchase();
	// photoPurchase.setId_photo_purchase(cursor.getInt(0));
	// photoPurchase.setKode_customer(cursor.getString(1));
	// photoPurchase.setNama_lengkap(cursor.getString(2));
	// photoPurchase.setFoto(cursor.getString(3));
	// photoPurchase.setNama_photo_purchase(cursor.getString(4));
	// photoPurchase.setDeskripsi(cursor.getString(5));
	// photoPurchase.setDatetime(cursor.getString(6));
	//
	// // Adding photo_purchase_list to list
	// photo_purchase_list.add(photoPurchase);
	// } while (cursor.moveToNext());
	// }
	//
	// // return photo_purchase_list
	// cursor.close();
	// db.close();
	// return photo_purchase_list;
	// } catch (Exception e) {
	// Log.e("photo_purchase_list", "" + e);
	// }
	//
	// return photo_purchase_list;
	// }

	// Getting All Kemasan
	public ArrayList<Kemasan> getAllKemasan() {
		try {
			kemasan_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_KEMASAN;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Kemasan staff = new Kemasan();
					staff.setId_kemasan(cursor.getInt(0));
					staff.setNama_id_kemasan(cursor.getString(1));

					// Adding kemasan to list
					kemasan_list.add(staff);
				} while (cursor.moveToNext());
			}

			// return kemasan_list
			cursor.close();
			db.close();
			return kemasan_list;
		} catch (Exception e) {
			Log.e("kemasan_list", "" + e);
		}

		return kemasan_list;
	}

	// Getting All Branch
	public ArrayList<Branch> getAllBranch() {
		try {
			branch_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_BRANCH;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Branch branch = new Branch();
					branch.setId_branch(cursor.getInt(0));
					branch.setKode_branch(cursor.getString(1));
					branch.setDeskripsi(cursor.getString(2));

					// Adding branch to list
					branch_list.add(branch);
				} while (cursor.moveToNext());
			}

			// return branch_list
			cursor.close();
			db.close();
			return branch_list;
		} catch (Exception e) {
			Log.e("branch_list", "" + e);
		}

		return branch_list;
	}

	// Getting All TypeCustomer
	public ArrayList<TypeCustomer> getAllTypeCustomer() {
		try {
			type_customer_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_TYPE_CUSTOMER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					TypeCustomer typeCustomer = new TypeCustomer();
					typeCustomer.setId_type_customer(cursor.getInt(0));
					typeCustomer.setType_customer(cursor.getString(1));
					typeCustomer.setDeskripsi(cursor.getString(2));

					// Adding type_customer_list to list
					type_customer_list.add(typeCustomer);
				} while (cursor.moveToNext());
			}

			// return type_customer_list
			cursor.close();
			db.close();
			return type_customer_list;
		} catch (Exception e) {
			Log.e("type_customer_list", "" + e);
		}

		return type_customer_list;
	}

	// Getting All Cluster
	public ArrayList<Cluster> getAllCluster() {
		try {
			cluster_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_CLUSTER;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Cluster cluster = new Cluster();
					cluster.setId_cluster(cursor.getInt(0));
					cluster.setNama_cluster(cursor.getString(1));
					cluster.setDescription(cursor.getString(2));

					// Adding cluster_list to list
					cluster_list.add(cluster);
				} while (cursor.moveToNext());
			}

			// return cluster_list
			cursor.close();
			db.close();
			return cluster_list;
		} catch (Exception e) {
			Log.e("cluster_list", "" + e);
		}

		return cluster_list;
	}

	// Getting All Wilayah
	public ArrayList<Wilayah> getAllWilayah() {
		try {
			wilayah_list.clear();

			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_WILAYAH;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);

			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Wilayah wilayah = new Wilayah();
					wilayah.setId_wilayah(cursor.getInt(0));
					wilayah.setNama_wilayah(cursor.getString(1));
					wilayah.setDeskripsi(cursor.getString(2));
					wilayah.setLats(cursor.getString(3));
					wilayah.setLongs(cursor.getString(4));

					// Adding wilayah_list to list
					wilayah_list.add(wilayah);
				} while (cursor.moveToNext());
			}

			// return wilayah_list
			cursor.close();
			db.close();
			return wilayah_list;
		} catch (Exception e) {
			Log.e("wilayah_list", "" + e);
		}

		return wilayah_list;
	}



	public int getCountStaff() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_STAFF, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTrackingLogs() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_TRACKING_LOGS, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountStaffTemp() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db
				.rawQuery("select count(*) from " + TABLE_STAFF_TEMP, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}


	public int getCountCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_CUSTOMER,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCustomerProspect() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_CUSTOMER
				+ " WHERE " + KEY_CUSTOMER_BLOKIR + "='Y'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCustomerProspect(String date) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_CUSTOMER
				+ " WHERE " + KEY_CUSTOMER_BLOKIR + "='Y' AND "
				+ KEY_CUSTOMER_DATE + "='" + date + "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCustomerWhereValidAndUpdate() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_CUSTOMER
				//+ " WHERE status_update='2'", null);
				+ " WHERE " + KEY_CUSTOMER_BLOKIR
				+ "='N' AND status_update='2'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountProductWhereValidAndUpdate() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_PRODUCT
				+ " WHERE " + "status='2'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCustomerWhereValidAndUpdateAll() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_CUSTOMER
				+ " WHERE status_update='2'", null);
				//+ KEY_CUSTOMER_BLOKIR
				//+ "='N' AND status_update='2'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountJadwalNotYetCheckOut() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_JADWAL
				+ " WHERE " + KEY_JADWAL_STATUS_UPDATE + "='2'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountJadwal() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_JADWAL,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountJadwalGetTotalCustomerWhereKodeJadwal(String kode_jadwal) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"select count(*) from " + TABLE_JADWAL + " WHERE "
						+ KEY_JADWAL_KODE_JADWAL + "='" + kode_jadwal + "'",
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountProduct() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_PRODUCT,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountProductPrice() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_PRODUCT_PRICE,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountDtg() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_DTG,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountStockVan() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_STOCK_VAN,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountPromosi() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_PROMOSI,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountDisplayProduct() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_DISPLAY_PRODUCT, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountDisplayProduct(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_DISPLAY_PRODUCT + " WHERE "
				+ KEY_DISPLAY_PRODUCT_KODE_CUSTOMER + "='" + kode_customer
				+ "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountPhotoPurchase() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_PHOTO_PURCHASE, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	// public int getCountPhotoPurchase(String kode_customer) {
	// SQLiteDatabase db = this.getReadableDatabase();
	// Cursor mCount = db.rawQuery(
	// "select count(*) from " + TABLE_PHOTO_PURCHASE + " WHERE "
	// + KEY_PHOTO_PURCHASE_KODE_CUSTOMER + "='"
	// + kode_customer + "'", null);
	// mCount.moveToFirst();
	// int count = mCount.getInt(0);
	// mCount.close();
	// return count;
	// }

	public int getCountSalesOrder() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"select count(*) from " + TABLE_SALES_ORDER, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountReqLoad() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"select count(*) from " + TABLE_REQLOAD, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountRetur() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery(
				"select count(*) from " + TABLE_RETUR, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTargetPenjualan() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_PRODUCT_TARGET, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountTargetPenjualan(String idCustomer) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_PRODUCT_TARGET + " WHERE "
				+ KEY_PRODUCT_TARGET_ID_CUSTOMER + "=" + idCustomer + "", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	
	
	public int getCountTargetPenjualan(String nomerTp, int idProduct) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_PRODUCT_TARGET + " WHERE "
				+ KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET + "='" + nomerTp + "' AND "
				+ KEY_PRODUCT_TARGET_ID_PRODUCT + "=" + idProduct + "", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountPenjualan(String nomerPenjualan) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_PENJUALAN
				+ " WHERE " + KEY_PENJUALAN_NOMER_PENJUALAN + "='"
				+ nomerPenjualan + "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}
	public int getCountPenjualanDetail(String nomerPenjualan) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_PENJUALAN_DETAIL + " WHERE "
				+ KEY_PENJUALAN_DETAIL_NOMER_PENJUALAN + "='" + nomerPenjualan
				+ "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountSalesOrderWhereNomerOrder(String nomerOrder) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_SALES_ORDER
				+ " WHERE " + KEY_SALES_ORDER_NOMER_ORDER + "='" + nomerOrder
				+ "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountSalesOrder(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_SALES_ORDER
				+ " WHERE " + KEY_SALES_ORDER_KODE_CUSTOMER + "='"
				+ kode_customer + "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountSalesOrder1(String nama_toko) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_SALES_ORDER
				+ " WHERE " + KEY_SALES_ORDER_NAMA_LENGKAP + "='"
				+ nama_toko + "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountReqLoadWhereNomerOrder(String nomerOrder) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_REQLOAD
				+ " WHERE " + KEY_SALES_ORDER_NOMER_ORDER + "='" + nomerOrder
				+ "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountReqLoad(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_REQLOAD
				+ " WHERE " + KEY_SALES_ORDER_KODE_CUSTOMER + "='"
				+ kode_customer + "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountReturWhereNomerOrder(String nomerRetur) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_RETUR
				+ " WHERE " + KEY_RETUR_NOMER_RETUR + "='" + nomerRetur
				+ "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountRetur(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_RETUR
				+ " WHERE " + KEY_RETUR_KODE_CUSTOMER + "='"
				+ kode_customer + "'", null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountStockOnHand() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_STOCK_ON_HAND, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountStockOnHand(String kode_customer) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
						+ TABLE_STOCK_ON_HAND + " WHERE "
						+ KEY_STOCK_ON_HAND_KODE_CUSTOMER + "='" + kode_customer + "'",
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountBranch() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_BRANCH,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountKemasan() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_KEMASAN,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountTypeCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_TYPE_CUSTOMER, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountCluster() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from "
				+ TABLE_CLUSTER, null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public int getCountWilayah() {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor mCount = db.rawQuery("select count(*) from " + TABLE_WILAYAH,
				null);
		mCount.moveToFirst();
		int count = mCount.getInt(0);
		mCount.close();
		return count;
	}

	public void deleteTableBranch() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_BRANCH);
	}

	public void deleteTableKemasan() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_KEMASAN);
	}

	public void deleteTableStaff() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_STAFF);
	}

	public void deleteTableStaffTemp() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_STAFF_TEMP);
	}

	public void deleteTableCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_CUSTOMER);
	}

	public void deleteTableStockOnHand() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_STOCK_ON_HAND);
	}

	public void deleteTableSalesOrder() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_SALES_ORDER);
	}

	public void deleteTableSalesOrderWhereNomerOrder(String nomerOrder) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_SALES_ORDER + " WHERE "
				+ KEY_SALES_ORDER_NOMER_ORDER + "='" + nomerOrder + "'");
	}

	public void deleteTableReqloadWhereNomerreqload(String nomer_request_load) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_REQLOAD + " WHERE "
				+ KEY_SALES_ORDER_NOMER_REQUEST_LOAD + "='" + nomer_request_load + "'");
	}

	public String zero(int num) {
		String number = (num < 10) ? ("0" + num) : ("" + num);
		return number;
	}

	public void updateJadwalwhereCheckOut(String kode_customer) {
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

		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("Update jadwal set  status_update='3', keterangan='ORDER', checkout='"+datetime+"' WHERE kode_customer='" + kode_customer + "'");
	}

	public void updateJadwalwhereCheckIn(String kode_customer) {
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
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("Update jadwal set  status_update='2', checkin='"+datetime+"' WHERE kode_customer='CST.HO.201702111038031'");
				//+ kode_customer + "'");
	}

	public void deleteTableReqLoad() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_REQLOAD);
	}

	public void deleteTableReqLoadWhereNomerOrder(String nomerOrder) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_REQLOAD + " WHERE "
				+ KEY_SALES_ORDER_NOMER_ORDER + "='" + nomerOrder + "'");
	}

	public void deleteTableRetur() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_RETUR);
	}

	public void deleteTableReturWhereNomerOrder(String nomerRetur) {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_RETUR + " WHERE "
				+ KEY_RETUR_NOMER_RETUR + "='" + nomerRetur + "'");
	}

	public void deleteTableTracking() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRACKING);
	}

	public void deleteTableTrackingLogs() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TRACKING_LOGS);
	}

	// Deleting single Table Customer
	public void deleteTableCustomer(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CUSTOMER, KEY_CUSTOMER_ID_CUSTOMER + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single Table Tracking
	public void deleteTableTracking(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_TRACKING, KEY_TRACKING_ID_LOCATOR + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single Table Stock On Hand
	public void deleteTableStockOnHand(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_STOCK_ON_HAND, KEY_STOCK_ON_HAND_ID_STOCK_ON_HAND
				+ " = ?", new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single Table Stock On Hand
	public void deleteTableStockOnHandWhereNomerOrder(String nomerOrder) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DELETE FROM " + TABLE_STOCK_ON_HAND + " WHERE "
				+ KEY_STOCK_ON_HAND_NOMER_STOCK_ON_HAND + "='" + nomerOrder
				+ "'");
		db.close();
	}

	// Deleting single Table Sales Order
	public void deleteTableSalesOrder(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_SALES_ORDER, KEY_SALES_ORDER_ID_SALES_ORDER + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single Table Sales Order
	public void deleteTableReqLoad(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_REQLOAD, KEY_SALES_ORDER_ID_SALES_ORDER + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting single Table Retur
	public void deleteTableRetur(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_RETUR, KEY_RETUR_ID_RETUR + " = ?",
				new String[] { String.valueOf(id) });
		db.close();
	}

	// Deleting Table Target Penjualan
	public void deleteTableProductTarget(int nomor_tp) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PRODUCT_TARGET, KEY_PRODUCT_TARGET_NOMER_PRODUCT_TARGET
				+ " ='?'", new String[] { String.valueOf(nomor_tp) });
		db.close();
	}
	// Deleting Table Target Penjualan
	public void deleteTablePenjualan(String nomor_tp) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PENJUALAN, KEY_PENJUALAN_NOMER_PENJUALAN + " = ? ",
				new String[] { String.valueOf(nomor_tp) });
		db.close();
	}
	// Deleting Table Target Penjualan
	public void deleteTablePenjualanDetail(String nomor_tp) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PENJUALAN_DETAIL, KEY_PENJUALAN_DETAIL_NOMER_PENJUALAN
				+ " = ?", new String[] { String.valueOf(nomor_tp) });
		db.close();
	}

	public void deleteTableJadwal() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_JADWAL);
	}

	public void deleteTableCustomerProspect() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CUSTOMER, KEY_CUSTOMER_BLOKIR + " = 'Y'", null);
		db.close();
	}

	public void deleteTableProduct() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRODUCT);
	}

	public void deleteTableStockVan() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_STOCK_VAN);
	}
	public void deleteTableProductTarget() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRODUCT_TARGET);
	}
	public void deleteTablePenjualan() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PENJUALAN);
	}
	public void deleteTablePenjualanDetail() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PENJUALAN_DETAIL);
	}
	
	public void deleteTablePromosi() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PROMOSI);
	}

	public void deleteTableDisplayProduct() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DISPLAY_PRODUCT);
	}

	// Deleting single Photo Purchase
	public void deleteTableDisplayProduct(int id) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_DISPLAY_PRODUCT, KEY_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT
				+ " = ?", new String[] { String.valueOf(id) });
		db.close();
	}

	public void deleteTablePhotoPurchase() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PHOTO_PURCHASE);
	}

	// // Deleting single Photo Purchase
	// public void deleteTablePhotoPurchase(int id) {
	// SQLiteDatabase db = this.getWritableDatabase();
	// db.delete(TABLE_PHOTO_PURCHASE, KEY_PHOTO_PURCHASE_ID_PHOTO_PURCHASE
	// + " = ?", new String[] { String.valueOf(id) });
	// db.close();
	// }

	public void deleteTableTypeCustomer() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_TYPE_CUSTOMER);
	}

	public void deleteTableCluster() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_CLUSTER);
	}

	public void deleteTableWilayah() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_WILAYAH);
	}

	public void deleteTableProductPrice() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_PRODUCT_PRICE);
	}

	public void deleteTableDtg() {
		SQLiteDatabase db = this.getReadableDatabase();
		db.execSQL("DELETE FROM " + TABLE_DTG);
	}


	public void updateStatus() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("update customer set status_update='3' where status_update='2' and blokir='N'");
	}

	public void updateStatusProduct() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("update product set status='1' where status='2'");
	}
	public void updateStatusProspect() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("update customer set status_update='1',blokir='NO' where status_update='2' and blokir='Y'");
	}

}