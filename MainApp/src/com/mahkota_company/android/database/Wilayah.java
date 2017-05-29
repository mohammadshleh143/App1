package com.mahkota_company.android.database;

public class Wilayah {

	private int id_wilayah;
	private String nama_wilayah;
	private String deskripsi;
	private String lats;
	private String longs;

	public Wilayah() {

	}

	// constructor
	public Wilayah(int id_wilayah, String nama_wilayah, String deskripsi,
			String lats, String longs) {
		this.id_wilayah = id_wilayah;
		this.nama_wilayah = nama_wilayah;
		this.deskripsi = deskripsi;
		this.lats = lats;
		this.longs = longs;
	}

	public int getId_wilayah() {
		return id_wilayah;
	}

	public void setId_wilayah(int id_wilayah) {
		this.id_wilayah = id_wilayah;
	}

	public String getNama_wilayah() {
		return nama_wilayah;
	}

	public void setNama_wilayah(String nama_wilayah) {
		this.nama_wilayah = nama_wilayah;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getLats() {
		return lats;
	}

	public void setLats(String lats) {
		this.lats = lats;
	}

	public String getLongs() {
		return longs;
	}

	public void setLongs(String longs) {
		this.longs = longs;
	}

}