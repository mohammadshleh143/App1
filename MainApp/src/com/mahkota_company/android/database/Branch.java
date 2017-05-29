package com.mahkota_company.android.database;

public class Branch {

	private int id_branch;
	private String kode_branch;
	private String deskripsi;

	public Branch() {
		
	}

	// constructor
	public Branch(int id_branch, String kode_branch, String deskripsi) {
		this.id_branch = id_branch;
		this.kode_branch = kode_branch;
		this.deskripsi = deskripsi;
	}

	public int getId_branch() {
		return id_branch;
	}

	public void setId_branch(int id_branch) {
		this.id_branch = id_branch;
	}

	public String getKode_branch() {
		return kode_branch;
	}

	public void setKode_branch(String kode_branch) {
		this.kode_branch = kode_branch;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

}