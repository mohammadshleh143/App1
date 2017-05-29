package com.mahkota_company.android.database;

public class Kemasan {

	private int id_kemasan;
	private String nama_id_kemasan;

	public Kemasan() {

	}

	// constructor
	public Kemasan(int id_kemasan, String nama_id_kemasan) {
		this.id_kemasan = id_kemasan;
		this.nama_id_kemasan = nama_id_kemasan;
	}

	public int getId_kemasan() {
		return id_kemasan;
	}

	public void setId_kemasan(int id_kemasan) {
		this.id_kemasan = id_kemasan;
	}

	public String getNama_id_kemasan() {
		return nama_id_kemasan;
	}

	public void setNama_id_kemasan(String nama_id_kemasan) {
		this.nama_id_kemasan = nama_id_kemasan;
	}

}