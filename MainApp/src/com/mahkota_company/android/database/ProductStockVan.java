package com.mahkota_company.android.database;

public class ProductStockVan {
	private int id_product;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private int stockGudang;
	private String stockVan;
	private String idKemasan;
	private String foto;
	private String deskripsi;

	public ProductStockVan() {

	}

	public ProductStockVan(int id_product, String nama_product,
			String kode_product, String harga_jual, int stockGudang,
			String stockVan, String id_kemasan, String foto, String deskripsi) {
		this.id_product = id_product;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.stockGudang = stockGudang;
		this.stockVan = stockVan;
		this.idKemasan = id_kemasan;
		this.foto = foto;
		this.deskripsi = deskripsi;

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

	public int getStockGudang() {
		return stockGudang;
	}

	public void setStockGudang(int stockGudang) {
		this.stockGudang = stockGudang;
	}

	public String getStockVan() {
		return stockVan;
	}

	public void setStockVan(String stockVan) {
		this.stockVan = stockVan;
	}

	public String getIdKemasan() {
		return idKemasan;
	}

	public void setIdKemasan(String idKemasan) {
		this.idKemasan = idKemasan;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
}
