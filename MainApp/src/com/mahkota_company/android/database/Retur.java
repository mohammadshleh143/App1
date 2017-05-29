package com.mahkota_company.android.database;

public class Retur {

	private int id_retur;
	private String nomer_retur;
	private String nomer_retur_detail;
	private String date_retur;
	private String time_retur;
	private String deskripsi;
	private int id_promosi;
	private String username;
	private String kode_customer;
	private String alamat;
	private String nama_lengkap;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String jumlah_retur;

	public Retur() {

	}

	// constructor
	public Retur(int id_retur, String nomer_retur,
				 String nomer_retur_detail, String date_retur,
				 String time_retur, int id_promosi, String deskripsi,
				 String username, String kode_customer, String alamat,
				 String nama_lengkap, String nama_product, String kode_product,
				 String harga_jual, String jumlah_retur) {
		this.id_retur = id_retur;
		this.nomer_retur = nomer_retur;
		this.nomer_retur_detail = nomer_retur_detail;
		this.date_retur = date_retur;
		this.time_retur = time_retur;
		this.deskripsi = deskripsi;
		this.id_promosi = id_promosi;
		this.username = username;
		this.kode_customer = kode_customer;
		this.alamat = alamat;
		this.nama_lengkap = nama_lengkap;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlah_retur = jumlah_retur;

	}

	public int getId_retur() {
		return id_retur;
	}

	public void setId_retur(int id_retur) {
		this.id_retur = id_retur;
	}

	public String getNomer_retur() {
		return nomer_retur;
	}

	public void setNomer_retur(String nomer_retur) {
		this.nomer_retur = nomer_retur;
	}

	public String getNomer_retur_detail() {
		return nomer_retur_detail;
	}

	public void setNomer_retur_detail(String nomer_retur_detail) {
		this.nomer_retur_detail = nomer_retur_detail;
	}

	public String getDate_retur() {
		return date_retur;
	}

	public void setDate_retur(String date_retur) {
		this.date_retur = date_retur;
	}

	public String getTime_retur() {
		return time_retur;
	}

	public void setTime_retur(String time_retur) {
		this.time_retur = time_retur;
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

	public String getJumlah_retur() {
		return jumlah_retur;
	}

	public void setJumlah_retur(String jumlah_retur) {
		this.jumlah_retur = jumlah_retur;
	}

}