package com.mahkota_company.android.database;

public class Jadwal {

	private int id_jadwal;
	private String kode_jadwal;
	private String kode_customer;
	private String username;
	private String alamat;
	private String nama_toko;
	private int id_wilayah;
	private String status;
	private String date;
	private String checkin;
	private String checkout;
	private String status_update;
	private String keterangan;

	public Jadwal() {

	}

	// constructor
	public Jadwal(int id_jadwal, String kode_jadwal, String kode_customer,
			String username, String alamat, String nama_toko,
			int id_wilayah, String status, String date, String checkin,
			String checkout, String status_update, String keterangan) {
		this.id_jadwal = id_jadwal;
		this.kode_jadwal = kode_jadwal;
		this.kode_customer = kode_customer;
		this.username = username;
		this.alamat = alamat;
		this.nama_toko = nama_toko;
		this.id_wilayah = id_wilayah;
		this.status = status;
		this.date = date;
		this.checkin = checkin;
		this.checkout = checkout;
		this.status_update = status_update;
		this.keterangan = keterangan;
	}

	public int getId_jadwal() {
		return id_jadwal;
	}

	public void setId_jadwal(int id_jadwal) {
		this.id_jadwal = id_jadwal;
	}

	public String getKode_jadwal() {
		return kode_jadwal;
	}

	public void setKode_jadwal(String kode_jadwal) {
		this.kode_jadwal = kode_jadwal;
	}

	public String getKode_customer() {
		return kode_customer;
	}

	public void setKode_customer(String kode_customer) {
		this.kode_customer = kode_customer;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getNama_toko() {
		return nama_toko;
	}

	public void setNama_toko(String nama_toko) {
		this.nama_toko = nama_toko;
	}

	public int getId_wilayah() {
		return id_wilayah;
	}

	public void setId_wilayah(int id_wilayah) {
		this.id_wilayah = id_wilayah;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCheckin() {
		return checkin;
	}

	public void setCheckin(String checkin) {
		this.checkin = checkin;
	}

	public String getCheckout() {
		return checkout;
	}

	public void setCheckout(String checkout) {
		this.checkout = checkout;
	}

	public String getStatus_update() {
		return status_update;
	}

	public void setStatus_update(String status_update) {
		this.status_update = status_update;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
	}

}