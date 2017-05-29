package com.mahkota_company.android.database;

public class DetailPenjualan {
	private int id_detail_penjualan;
	private int id_product;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private int jumlah_order;

	public DetailPenjualan() {

	}

	// constructor
	public DetailPenjualan(int id_sales_order, int id_product,
						   String nama_product, String kode_product, String harga_jual,
						   int jumlah_order) {
		this.id_detail_penjualan = id_sales_order;
		this.id_product = id_product;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlah_order = jumlah_order;

	}

	public int getId_sales_order() {
		return id_detail_penjualan;
	}

	public void setId_sales_order(int id_sales_order) {
		this.id_detail_penjualan = id_sales_order;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
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

	public int getJumlah_order() {
		return jumlah_order;
	}

	public void setJumlah_order(int jumlah_order) {
		this.jumlah_order = jumlah_order;
	}

}