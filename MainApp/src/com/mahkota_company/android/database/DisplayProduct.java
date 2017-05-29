package com.mahkota_company.android.database;

public class DisplayProduct {
	private int id_display_product;
	private String kode_customer;
	private String nama_lengkap;
	private String foto;
	private String nama_display_product;
	private String deskripsi;
	private String datetime;

	public DisplayProduct() {

	}

	public DisplayProduct(int id_display_product, String kode_customer,
			String nama_lengkap, String nama_display_product, String deskripsi,
			String foto, String datetime) {
		this.id_display_product = id_display_product;
		this.kode_customer = kode_customer;
		this.nama_lengkap = nama_lengkap;
		this.nama_display_product = nama_display_product;
		this.deskripsi = deskripsi;
		this.foto = foto;
		this.datetime = datetime;

	}

	public int getId_display_product() {
		return id_display_product;
	}

	public void setId_display_product(int id_display_product) {
		this.id_display_product = id_display_product;
	}

	public String getKode_customer() {
		return kode_customer;
	}

	public void setKode_customer(String kode_customer) {
		this.kode_customer = kode_customer;
	}

	public String getNama_lengkap() {
		return nama_lengkap;
	}

	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getNama_display_product() {
		return nama_display_product;
	}

	public void setNama_display_product(String nama_display_product) {
		this.nama_display_product = nama_display_product;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
