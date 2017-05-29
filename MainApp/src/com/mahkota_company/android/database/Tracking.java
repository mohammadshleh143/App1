package com.mahkota_company.android.database;

public class Tracking {

	private int id_locator;
	private String username;
	private String nama_lengkap;
	private int level;
	private String lats;
	private String longs;
	private String address;
	private String imei;
	private String mcc;
	private String mnc;
	private String date;
	private String time;

	public Tracking() {

	}

	// constructor
	public Tracking(int id_locator, String username, String nama_lengkap,
			int level, String lats, String longs, String address, String imei,
			String mcc, String mnc, String date, String time) {
		this.id_locator = id_locator;
		this.username = username;
		this.nama_lengkap = nama_lengkap;
		this.level = level;
		this.lats = lats;
		this.longs = longs;
		this.address = address;
		this.imei = imei;
		this.mcc = mcc;
		this.mnc = mnc;
		this.date = date;
		this.time = time;

	}

	public int getId_locator() {
		return id_locator;
	}

	public void setId_locator(int id_locator) {
		this.id_locator = id_locator;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNama_lengkap() {
		return nama_lengkap;
	}

	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}