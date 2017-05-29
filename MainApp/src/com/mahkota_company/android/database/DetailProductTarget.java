package com.mahkota_company.android.database;

public class DetailProductTarget {
	private int id_product_target;
	private int id_product;
	private String kode_product;
	private String nama_product;
	private int jumlah_target;
	private String jumlah_stok_van;

	public DetailProductTarget() {

	}

	// constructor
	public DetailProductTarget(int id_product_target, int id_product,
			String kode_product, String nama_product, int jumlah_target,
			String jumlah_stok_van) {
		this.id_product_target = id_product_target;
		this.id_product = id_product;
		this.kode_product = kode_product;
		this.nama_product = nama_product;
		this.jumlah_target = jumlah_target;
		this.jumlah_stok_van = jumlah_stok_van;
	}

	public int getId_product_target() {
		return id_product_target;
	}

	public void setId_product_target(int id_product_target) {
		this.id_product_target = id_product_target;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
	}

	public String getKode_product() {
		return kode_product;
	}

	public void setKode_product(String kode_product) {
		this.kode_product = kode_product;
	}

	public String getNama_product() {
		return nama_product;
	}

	public void setNama_product(String nama_product) {
		this.nama_product = nama_product;
	}

	public int getJumlah_target() {
		return jumlah_target;
	}

	public void setJumlah_target(int jumlah_target) {
		this.jumlah_target = jumlah_target;
	}

	public String getJumlah_stok_van() {
		return jumlah_stok_van;
	}

	public void setJumlah_stok_van(String jumlah_stok_van) {
		this.jumlah_stok_van = jumlah_stok_van;
	}

}
