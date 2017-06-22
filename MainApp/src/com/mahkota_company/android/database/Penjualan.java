package com.mahkota_company.android.database;

public class Penjualan {

	private int id_penjualan,id_staff,type_penjualan;
	private String no_penjualan,nama_customer, alamat, keterangan,date,time, lats,longs,foto1,foto2,ttd,status_penjualan;

	public Penjualan() {

	}

	// constructor
	public Penjualan(int id_penjualan,int id_staff, int type_penjualan, String no_penjualan, String nama_customer,
					 String alamat, String keterangan,String date,String time, String lats, String longs,
					 String foto1, String foto2,String ttd,String status_penjualan ) {
		this.id_penjualan = id_penjualan;
		this.id_staff = id_staff;
		this.type_penjualan=type_penjualan;
		this.no_penjualan=no_penjualan;
		this.nama_customer=nama_customer;
		this.alamat=alamat;
		this.keterangan=keterangan;
		this.date = date;
		this.time = time;
		this.lats=lats;
		this.longs=longs;
		this.foto1=foto1;
		this.foto2=foto2;
		this.ttd=ttd;
		this.status_penjualan=status_penjualan;
	}

	public int getId_penjualan() {
		return id_penjualan;
	}

	public void setId_penjualan(int id_penjualan) {
		this.id_penjualan = id_penjualan;
	}

	public int getId_staff(){
		return id_staff;
	}

	public void setId_staff(int id_staff){
		this.id_staff=id_staff;
	}

	public int getType_penjualan(){
		return type_penjualan;
	}

	public void setType_penjualan(int type_penjualan){
		this.type_penjualan=type_penjualan;
	}

	public String getNo_penjualan() {
		return no_penjualan;
	}

	public void setNo_penjualan(String no_penjualan) {
		this.no_penjualan = no_penjualan;
	}

	public String getNama_customer() {
		return nama_customer;
	}

	public void setNama_customer(String nama_customer) {
		this.nama_customer = nama_customer;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getKeterangan() {
		return keterangan;
	}

	public void setKeterangan(String keterangan) {
		this.keterangan = keterangan;
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

	public String getFoto1() {
		return foto1;
	}

	public void setFoto1(String foto1) {
		this.foto1 = foto1;
	}

	public String getFoto2() {
		return foto2;
	}

	public void setFoto2(String foto2) {
		this.foto2 = foto2;
	}

	public String getTtd() {
		return ttd;
	}

	public void setTtd(String ttd) {
		this.ttd = ttd;
	}

	public String getStatus_penjualan() {
		return status_penjualan;
	}

	public void setStatus_penjualan(String status_penjualan) {
		this.status_penjualan= status_penjualan;
	}
}