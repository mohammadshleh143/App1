package com.mahkota_company.android.database;

public class PenjualanDetail {

	private int id_penjualan_detail;
	private String nomer_product_terjual;
	private int id_product;
	private int jumlah;

	public PenjualanDetail() {

	}

	// constructor
	public PenjualanDetail(int id_penjualan_detail,
			String nomer_product_terjual, int id_product, int jumlah) {
		this.id_penjualan_detail = id_penjualan_detail;
		this.nomer_product_terjual = nomer_product_terjual;
		this.id_product = id_product;
		this.jumlah = jumlah;
	}

	public int getId_penjualan_detail() {
		return id_penjualan_detail;
	}

	public void setId_penjualan_detail(int id_penjualan_detail) {
		this.id_penjualan_detail = id_penjualan_detail;
	}

	public String getNomer_product_terjual() {
		return nomer_product_terjual;
	}

	public void setNomer_product_terjual(String nomer_product_terjual) {
		this.nomer_product_terjual = nomer_product_terjual;
	}

	public int getIdProduct() {
		return id_product;
	}

	public void setIdProduct(int id_product) {
		this.id_product = id_product;
	}

	public int getJumlah() {
		return jumlah;
	}

	public void setJumlah(int jumlah) {
		this.jumlah = jumlah;
	}

}