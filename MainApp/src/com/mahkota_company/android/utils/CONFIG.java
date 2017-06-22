package com.mahkota_company.android.utils;

import java.io.File;

import android.os.Environment;

public class CONFIG {

	public static final String SHARED_PREFERENCES_Product_FOTO_1 = "mahkota_supplier_foto_1";

	public static final String SHARED_PREFERENCES_SUPPLIER_FOTO_1 = "mahkota_supplier_foto_1";
	public static final String SHARED_PREFERENCES_SUPPLIER_FOTO_2 = "mahkota_supplier_foto_2";
	public static final String SHARED_PREFERENCES_SUPPLIER_FOTO_3 = "mahkota_supplier_foto_3";
	public static final String SHARED_PREFERENCES_SUPPLIER_FOTO_4 = "mahkota_supplier_foto_4";
	public static final String CONFIG_APP_URL_UPLOAD_INSERT_SUPPLIER = "web_flg_mas/ws/insert_supervisor_report.php";

	public static final String SHARED_PREFERENCES_KANVAS_FOTO_1 = "mahkota_kanvas_foto1";
	public static final String SHARED_PREFERENCES_KANVAS_FOTO_2 = "mahkota_kanvas_foto2";
	public static final String SHARED_PREFERENCES_KANVAS_TTD = "mahkota_kanvas_ttd";

	public static final String CONFIG_APP_URL_UPLOAD_INSERT_SALES = "web_flg_mas/ws/insert_penjualan.php";
	public static final String CONFIG_APP_URL_UPLOAD_INSERT_SALES_DETAIL = "web_flg_mas/ws/insert_penjualan_detail.php";

	public static final String CONFIG_APP_URL_UPLOAD_INSERT_SALES_KANVAS = "web_flg_mas/ws/update_sales_kanvas.php";
	public static final String CONFIG_APP_URL_UPLOAD_INSERT_SALES_KANVAS_DETAIL = "web_flg_mas/ws/update_sales_kanvas_detail.php";

	////////////////////////////////
	//186.132.0.58

	public static final String CONFIG_APP_URL_DOWNLOAD_STOCK_VAN = "web_flg_mas/ws/get_request_load.php";
	public static final String SHARED_PREFERENCES_TABLE_STOCK_VAN = "mahkota_table_stock_van";
	public static final String SHARED_PREFERENCES_TABLE_STOCK_VAN_SAME_DATA = "mahkota_table_stock_van_same_data";
	public static final String SHARED_PREFERENCES_TABLE_STOCK_VAN_NO_REQUEST_LOAD = "mahkota_table_no_ request_load";

	public static final String CONFIG_APP_URL_DOWNLOAD_TARGET_PENJUALAN = "web_flg_mas/ws/get_product_target.php";
	public static final String CONFIG_APP_URL_UPLOAD_INSERT_RETUR_STOK = "web_flg_mas/ws/insert_retur_load.php";
	public static final String SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN = "mahkota_table_target_penjualan";
	public static final String SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_SAME_DATA = "mahkota_table_target_penjualan_same_data";
	public static final String SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NO_TARGET_PENJUALAN = "mahkota_table_no_target_penjualan";
	public static final String SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_ID_CUSTOMER = "mahkota_table_no_target_penjualan_id_customer";
	public static final String SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_KODE_CUSTOMER = "mahkota_table_no_target_penjualan_kode_customer";
	public static final String SHARED_PREFERENCES_TABLE_TARGET_PENJUALAN_NAMA_CUSTOMER = "mahkota_table_no_target_penjualan_nama_customer";

	public static final String CONFIG_APP_FOLDER_PROMOSI = "promosi";
	public static final String CONFIG_APP_FOLDER_PRODUCT = "product";
	public static final String CONFIG_APP_FOLDER_STAFF = "staff";
	public static final String CONFIG_APP_FOLDER_CUSTOMER = "customer";
	public static final String CONFIG_APP_FOLDER_CUSTOMER_PROSPECT = "customer_prospect";
	public static final String CONFIG_APP_FOLDER_REQ_LOAD = "Reqload";
	public static final String CONFIG_APP_FOLDER_PHOTO_PURCHASE = "photo_purchase";
	public static final String CONFIG_APP_FOLDER_DISPLAY_PRODUCT = "photo_display_product";
	// public static final String CONFIG_APP_URL_PUBLIC = "http://mahkota.com/";
	public static final String CONFIG_APP_URL_PUBLIC = "http://186.132.0.58"+":8081/";
	public static final String CONFIG_APP_APP_FOLDER = "mahkota";
	public static final String SHARED_PREFERENCES_NAME = "mahkota_android";

	public static final String SHARED_PREFERENCES_STAFF_USERNAME = "mahkota_staff_username";
	public static final String SHARED_PREFERENCES_STAFF_NAMA_LENGKAP = "mahkota_staff_nama_lengkap";
	public static final String SHARED_PREFERENCES_STAFF_KODE_BRANCH = "mahkota_staff_kode_branch";
	public static final String SHARED_PREFERENCES_STAFF_ID_WILAYAH = "mahkota_staff_id_wilayah";
	public static final String SHARED_PREFERENCES_STAFF_ID_DEPO = "mahkota_staff_id_depo";
	public static final String SHARED_PREFERENCES_STAFF_ID_STAFF = "mahkota_staff_id_staff";
	public static final String SHARED_PREFERENCES_STAFF_LEVEL = "mahkota_staff_level";
	public static final String SHARED_PREFERENCES_STAFF_PASSWORD = "mahkota_staff_pwd";
	public static final String SHARED_PREFERENCES_STAFF_FOTO = "mahkota_staff_foto";

	public static final String CONFIG_APP_URL_UPLOAD_INSERT_REQUEST_LOAD = "web_flg_mas/ws/insert_request_load.php";
	public static final String CONFIG_APP_URL_UPLOAD_UPDATE_REQUEST_LOAD = "web_flg_mas/ws/update_request_load.php";
	public static final String CONFIG_APP_URL_UPLOAD_UPDATE_PRODUCT_TARGET = "web_flg_mas/ws/update_product_target.php";
	public static final String CONFIG_APP_URL_UPLOAD_UPDATE_PENJUALAN = "web_flg_mas/ws/insert_product_terjual.php";
	public static final String CONFIG_APP_URL_UPLOAD_UPDATE_PENJUALAN_DETAIL = "web_flg_mas/ws/insert_product_terjual_detail.php";

	public static final String CONFIG_APP_URL_DOWNLOAD_JADWAL = "web_flg_mas/ws/get_jadwal.php";
	// public static final String CONFIG_APP_URL_DIR_IMG_PRODUCT =
	// "http://mahkota.com/web_flg_mas/imgLib/product/";
	public static final String CONFIG_APP_URL_DIR_IMG_PRODUCT = "http://186.132.0.58" +
			":8081/web_flg_mas/imgLib/product/";
	public static final String CONFIG_APP_URL_DOWNLOAD_PRODUCT = "web_flg_mas/ws/get_product.php";
	public static final String CONFIG_APP_URL_DOWNLOAD_PRODUCT_PRICE = "web_flg_mas/ws/get_product_price.php";
	public static final String CONFIG_APP_URL_DOWNLOAD_DTG = "web_flg_mas/ws/get_dtg.php";
	public static final String SHARED_PREFERENCES_TABLE_PRODUCT = "mahkota_table_product";
	public static final String SHARED_PREFERENCES_TABLE_PRODUCT_PRICE = "mahkota_table_product_price";
	public static final String SHARED_PREFERENCES_TABLE_DTG = "mahkota_table_dtg";
	public static final String SHARED_PREFERENCES_TABLE_PRODUCT_SAME_DATA = "mahkota_table_product_same_data";
	public static final String SHARED_PREFERENCES_TABLE_PRODUCT_PRICE_SAME_DATA = "mahkota_table_product_price_same_data";
	public static final String SHARED_PREFERENCES_TABLE_DTG_SAME_DATA = "mahkota_table_dtg_same_data";
	public static final String SHARED_PREFERENCES_TABLE_PRODUCT_ID_PRODUCT = "mahkota_table_product_id_product";
	public static final String SHARED_PREFERENCES_TABLE_PRODUCT_PRICE_ID_PRODUCT = "mahkota_table_product_id_product";
	public static final String SHARED_PREFERENCES_TABLE_DTG_ID = "mahkota_table_product_id";

	// public static final String CONFIG_APP_URL_DIR_IMG_PROMOSI =
	// "http://mahkota.com/web_flg_mas/imgLib/promosi/";
	public static final String CONFIG_APP_URL_DIR_IMG_PROMOSI = "http://186.132.0.58" +
			":8081/web_flg_mas/imgLib/promosi/";
	public static final String CONFIG_APP_URL_DOWNLOAD_PROMOSI = "web_flg_mas/ws/get_promosi.php";
	public static final String SHARED_PREFERENCES_TABLE_PROMOSI = "mahkota_table_promosi";
	public static final String SHARED_PREFERENCES_TABLE_PROMOSI_SAME_DATA = "mahkota_table_promosi_same_data";
	public static final String SHARED_PREFERENCES_TABLE_PROMOSI_ID_PROMOSI = "mahkota_table_promosi_id_promosi";

	public static final String SHARED_PREFERENCES_TABLE_JADWAL = "mahkota_table_jadwal";
	public static final String SHARED_PREFERENCES_TABLE_JADWAL_SAME_DATA = "mahkota_table_jadwal_same_data";
	public static final String SHARED_PREFERENCES_TABLE_JADWAL_ID_JADWAL = "mahkota_table_jadwal_id_jadwal";
	public static final String SHARED_PREFERENCES_TABLE_JADWAL_KODE_JADWAL = "mahkota_table_jadwal_kode_jadwal";

	// public static final String CONFIG_APP_URL_DIR_IMG_CUSTOMER =
	// "http://mahkota.com/web_flg_mas/imgLib/customer/";
	public static final String CONFIG_APP_URL_DIR_IMG_CUSTOMER = "http://186.132.0.58" +
			":8081/web_flg_mas/imgLib/customer/";
	public static final String CONFIG_APP_URL_DOWNLOAD_CUSTOMER = "web_flg_mas/ws/get_customer.php";
	public static final String CONFIG_APP_URL_DOWNLOAD_CUSTOMER_NOO = "web_flg_mas/ws/get_customer_noo.php";
	public static final String SHARED_PREFERENCES_TABLE_CUSTOMER = "mahkota_table_customer";
	public static final String SHARED_PREFERENCES_TABLE_CUSTOMER_SAME_DATA = "mahkota_table_customer_same_data";
	public static final String SHARED_PREFERENCES_TABLE_CUSTOMER_ID_CUSTOMER = "mahkota_table_customer_id_customer";
	public static final String SHARED_PREFERENCES_TABLE_CUSTOMER_KODE_CUSTOMER = "mahkota_table_customer_kode_customer";

	public static final String CONFIG_APP_URL_DOWNLOAD_STAFF = "web_flg_mas/ws/get_staff.php";
	public static final String SHARED_PREFERENCES_TABLE_STAFF = "mahkota_table_staff";
	public static final String SHARED_PREFERENCES_TABLE_STAFF_SAME_DATA = "mahkota_table_staff_same_data";

	public static final String CONFIG_APP_URL_DOWNLOAD_TRACKING = "web_flg_mas/ws/get_locator_tracking.php";
	public static final String SHARED_PREFERENCES_TABLE_TRACKING = "mahkota_table_tracking";
	public static final String SHARED_PREFERENCES_TABLE_TRACKING_SAME_DATA = "mahkota_table_tracking_same_data";

	public static final String CONFIG_APP_URL_UPLOAD_LOGIN = "web_flg_mas/ws/login.php";
	public static final String CONFIG_APP_URL_UPLOAD_CUSTOMER = "web_flg_mas/ws/update_customer.php";
	public static final String CONFIG_APP_URL_UPLOAD_APPROVE_PROSPECT = "web_flg_mas/ws/update_approve_prospect.php";
	public static final String CONFIG_APP_URL_UPLOAD_PRODUCT = "web_flg_mas/ws/update_product.php";
	public static final String CONFIG_APP_URL_UPLOAD_JADWAL = "web_flg_mas/ws/update_jadwal.php";
	public static final String CONFIG_APP_URL_UPLOAD_CUSTOMER_PROSPECT = "web_flg_mas/ws/update_customer_prospect1.php";
	public static final String CONFIG_APP_URL_UPLOAD_PHOTO_PURCHASE = "web_flg_mas/ws/update_photo_purchase.php";
	public static final String CONFIG_APP_URL_UPLOAD_SALES_ORDER = "web_flg_mas/ws/update_sales_order.php";
	public static final String CONFIG_APP_URL_UPLOAD_SALES_KANVAS= "web_flg_mas/ws/update_sales_kanvas.php";
	public static final String CONFIG_APP_URL_UPLOAD_REQ_LOAD = "web_flg_mas/ws/update_req_load.php";
	public static final String CONFIG_APP_URL_UPLOAD_RETUR = "web_flg_mas/ws/update_retur.php";
	public static final String CONFIG_APP_URL_UPLOAD_STOCK_ON_HAND = "web_flg_mas/ws/update_stock_on_hand.php";
	public static final String CONFIG_APP_URL_UPLOAD_DISPLAY_PRODUCT = "web_flg_mas/ws/update_display_product.php";
	public static final String CONFIG_APP_URL_UPLOAD_LOCATOR = "web_flg_mas/ws/update_locator_sales.php";
	public static final String CONFIG_APP_KODE_CUSTOMER_HEADER = "CST.";
	public static final String CONFIG_APP_KODE_SO_HEADER = "SO.";
	public static final String CONFIG_APP_KODE_RL_HEADER = "RL.";
	public static final String CONFIG_APP_KODE_SHO_HEADER = "SOH.";
	public static final String CONFIG_APP_KODE_TP_HEADER = "TP.";
	public static final String CONFIG_APP_KODE_RT_HEADER = "RT.";
	public static final String CONFIG_APP_KODE_SK_HEADER = "SK.";
	public static final String CONFIG_APP_ERROR_MESSAGE_ADDRESS_FAILED = "Alamat Tidak Terdeteksi";

	 public static final String SHARED_PREFERENCES_TABLE_PHOTO_PURCHASE_ID_PHOTO_PURCHASE = "mahkota_table_photo_purchase";
	public static final String SHARED_PREFERENCES_TABLE_DISPLAY_PRODUCT_ID_DISPLAY_PRODUCT = "mahkota_table_display_product";
	public static final String SHARED_PREFERENCES_TABLE_SALES_ORDER_NOMER_ORDER = "mahkota_table_sales_order";
	public static final String SHARED_PREFERENCES_TABLE_REQ_LOAD_NOMER_ORDER = "mahkota_table_req_load";
	public static final String SHARED_PREFERENCES_TABLE_RETUR_NOMER_ORDER = "mahkota_table_retur";
	public static final String SHARED_PREFERENCES_TABLE_STOCK_ON_HAND_NOMER_ORDER_STOCK_ON_HAND = "mahkota_table_stock_on_hand";

	public static final String CONFIG_APP_URL_DOWNLOAD_KEMASAN = "web_flg_mas/ws/get_kemasan.php";
	public static final String SHARED_PREFERENCES_TABLE_KEMASAN = "mahkota_table_kemasan";
	public static final String SHARED_PREFERENCES_TABLE_KEMASAN_SAME_DATA = "mahkota_table_kemasan_same_data";
	public static final String SHARED_PREFERENCES_TABLE_KEMASAN_ID_KEMASAN = "mahkota_table_kemasan_id_kemasan";

	public static final String CONFIG_APP_URL_DOWNLOAD_BRANCH = "web_flg_mas/ws/get_branch.php";
	public static final String SHARED_PREFERENCES_TABLE_BRANCH = "mahkota_table_branch";
	public static final String SHARED_PREFERENCES_TABLE_BRANCH_SAME_DATA = "mahkota_table_branch_same_data";
	public static final String SHARED_PREFERENCES_TABLE_BRANCH_ID_BRANCH = "mahkota_table_branch_id_branch";

	public static final String CONFIG_APP_URL_DOWNLOAD_TYPE_CUSTOMER = "web_flg_mas/ws/get_type_customer.php";
	public static final String SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER = "mahkota_table_type_customer";
	public static final String SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER_SAME_DATA = "mahkota_table_type_customer_same_data";
	public static final String SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER_ID_TYPE_CUSTOMER = "mahkota_table_type_customer_id_type_customer";

	public static final String CONFIG_APP_URL_DOWNLOAD_CLUSTER = "web_flg_mas/ws/get_cluster.php";
	public static final String SHARED_PREFERENCES_TABLE_CLUSTER = "mahkota_table_cluster";
	public static final String SHARED_PREFERENCES_TABLE_CLUSTER_SAME_DATA = "mahkota_table_cluster_same_data";
	public static final String SHARED_PREFERENCES_TABLE_TYPE_CUSTOMER_ID_CLUSTER = "mahkota_table_cluster_id_cluster";

	public static final String CONFIG_APP_URL_DOWNLOAD_WILAYAH = "web_flg_mas/ws/get_wilayah.php";
	public static final String SHARED_PREFERENCES_TABLE_WILAYAH = "mahkota_table_wilayah";
	public static final String SHARED_PREFERENCES_TABLE_WILAYAH_SAME_DATA = "mahkota_table_wilayah_same_data";
	public static final String SHARED_PREFERENCES_TABLE_WILAYAH_ID_WILAYAH = "mahkota_table_wilayah_id_wilayah";

	public static final String APPLICATION_PACKAGE_NAME = "com.mahkota_company.android";

	public static String getFolderPath() {
		StringBuilder path_builder = new StringBuilder();
		path_builder.append(File.separatorChar);
		path_builder.append("Android");
		path_builder.append(File.separatorChar);
		path_builder.append("data");
		path_builder.append(File.separatorChar);
		path_builder.append(APPLICATION_PACKAGE_NAME);
		String temp_middle_path = path_builder.toString();

		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ temp_middle_path;

	}

}