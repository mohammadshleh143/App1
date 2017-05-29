package com.mahkota_company.android.database;

public class StaffTemp {
	private int id_staff;
	private String nama_lengkap;
	private String username;
	private String notelp;
	private String password;
	private int level;
	private String id_branch;
	private String id_type_customer;
	private int id_depo;
	private int id_wilayah;


	public StaffTemp() {

	}

	public StaffTemp(int id_staff, String nama_lengkap, String username,
			String notelp, String password, int level, String id_branch,
			String id_type_customer, int id_depo) {
		this.id_staff = id_staff;
		this.nama_lengkap = nama_lengkap;
		this.username = username;
		this.notelp = notelp;
		this.password = password;
		this.level = level;
		this.id_branch = id_branch;
		this.id_type_customer = id_type_customer;
		this.id_depo = id_depo;

	}

	public int getId_staff() {
		return id_staff;
	}

	public void setId_staff(int id_staff) {
		this.id_staff = id_staff;
	}

	public String getNama_lengkap() {
		return nama_lengkap;
	}

	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNotelp() {
		return notelp;
	}

	public void setNotelp(String notelp) {
		this.notelp = notelp;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getId_branch() {
		return id_branch;
	}

	public void setId_branch(String id_branch) {
		this.id_branch = id_branch;
	}

	public String getId_type_customer() {
		return id_type_customer;
	}

	public void setId_type_customer(String id_type_customer) {
		this.id_type_customer = id_type_customer;
	}

	public int getId_wilayah() {
		return id_wilayah;
	}

	public void setId_wilayah(int id_wilayah) {
		this.id_wilayah = id_wilayah;
	}

	public int getId_depo() {
		return id_depo;
	}

	public void setId_depo(int id_depo) {
		this.id_depo = id_depo;
	}
}
