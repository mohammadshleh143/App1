package com.mahkota_company.android.database;

public class DetailRetur {
	private int id_sales_order;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String jumlah_order;

	public DetailRetur() {

	}

	// constructor
	public DetailRetur(int id_sales_order, String nama_product,
					   String kode_product, String harga_jual, String jumlah_order) {
		this.setId_sales_order(id_sales_order);
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlah_order = jumlah_order;

	}

	public int getId_sales_order() {
		return id_sales_order;
	}

	public void setId_sales_order(int id_sales_order) {
		this.id_sales_order = id_sales_order;
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

}