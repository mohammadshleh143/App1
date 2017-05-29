package com.mahkota_company.android.database;

public class SalesOrder {

	private int id_sales_order;
	private String nomer_order;
	private String nomer_order_detail;
	private String date_order;
	private String time_order;
	private String deskripsi;
	private int id_promosi;
	private String username;
	private String kode_customer;
	private String alamat;
	private String nama_lengkap;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String jumlah_order;
	private String jumlah_order1;
	private String jumlah_order2;
	private String jumlah_order3;
	private int id_wilayah;

	public SalesOrder() {

	}

	// constructor
	public SalesOrder(int id_sales_order, String nomer_order,
			String nomer_order_detail, String date_order,
			String time_order, int id_promosi, String deskripsi,
			String username, String kode_customer, String alamat,
			String nama_lengkap, String nama_product, String kode_product,
			String harga_jual, String jumlah_order, String jumlah_order1,
			String jumlah_order2,String jumlah_order3, int id_wilayah) {
		this.id_sales_order = id_sales_order;
		this.nomer_order = nomer_order;
		this.nomer_order_detail = nomer_order_detail;
		this.date_order = date_order;
		this.time_order = time_order;
		this.deskripsi = deskripsi;
		this.id_promosi = id_promosi;
		this.username = username;
		this.kode_customer = kode_customer;
		this.alamat = alamat;
		this.nama_lengkap = nama_lengkap;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlah_order = jumlah_order;
		this.jumlah_order1 = jumlah_order1;
		this.jumlah_order2 = jumlah_order2;
		this.jumlah_order3 = jumlah_order3;
		this.id_wilayah = id_wilayah;

	}

	public int getId_sales_order() {
		return id_sales_order;
	}

	public void setId_sales_order(int id_sales_order) {
		this.id_sales_order = id_sales_order;
	}

	public String getNomer_order() {
		return nomer_order;
	}

	public void setNomer_order(String nomer_order) {
		this.nomer_order = nomer_order;
	}

	public String getNomer_order_detail() {
		return nomer_order_detail;
	}

	public void setNomer_order_detail(String nomer_order_detail) {
		this.nomer_order_detail = nomer_order_detail;
	}

	public String getDate_order() {
		return date_order;
	}

	public void setDate_order(String date_order) {
		this.date_order = date_order;
	}

	public String getTime_order() {
		return time_order;
	}

	public void setTime_order(String time_order) {
		this.time_order = time_order;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public int getId_promosi() {
		return id_promosi;
	}

	public void setId_promosi(int id_promosi) {
		this.id_promosi = id_promosi;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKode_customer() {
		return kode_customer;
	}

	public void setKode_customer(String kode_customer) {
		this.kode_customer = kode_customer;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getNama_lengkap() {
		return nama_lengkap;
	}

	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
	}

	public String getNama_product() {
		return nama_product;
	}

	public void setNama_product(String nama_product) {
		this.nama_product = nama_product;
	}

	public String getKode_product() {
		return kode_product;
	}

	public void setKode_product(String kode_product) {
		this.kode_product = kode_product;
	}

	public String getHarga_jual() {
		return harga_jual;
	}

	public void setHarga_jual(String harga_jual) {
		this.harga_jual = harga_jual;
	}

	public String getJumlah_order() {
		return jumlah_order;
	}

	public void setJumlah_order(String jumlah_order) {
		this.jumlah_order = jumlah_order;
	}

	public String getJumlah_order1() {
		return jumlah_order1;
	}

	public void setJumlah_order1(String jumlah_order1) {
		this.jumlah_order1 = jumlah_order1;
	}

	public String getJumlah_order2() {
		return jumlah_order2;
	}
	public void setJumlah_order2(String jumlah_order2) {
		this.jumlah_order2 = jumlah_order2;
	}
	public String getJumlah_order3() {
		return jumlah_order3;
	}
	public void setJumlah_order3(String jumlah_order3) {
		this.jumlah_order3 = jumlah_order3;
	}

	public int getId_wilayah() {
		return id_wilayah;
	}
	public void setId_wilayah(int id_wilayah) {
		this.id_wilayah = id_wilayah;
	}
}