package com.mahkota_company.android.database;

public class PenjualanDetail {

	private int id_penjualan_detail;
	private String no_penjualan;
	private String id_product;
	private String harga;
	private String harga_jual;
	private String nomer_request_load;
	private String id_staff;
	private String jumlah;
	private String jumlah1;
	private String jumlah2;
	private String jumlah3;

	public PenjualanDetail() {

	}

	// constructor
	public PenjualanDetail(int id_penjualan_detail, String no_penjualan,
						   String id_product, String harga, String  harga_jual, String nomer_request_load, String id_staff,
						   String jumlah, String jumlah1, String jumlah2, String jumlah3) {
		this.id_penjualan_detail=id_penjualan_detail;
		this.no_penjualan = no_penjualan;
		this.id_product= id_product;
		this.harga = harga;
		this.harga_jual = harga_jual;
		this.nomer_request_load = nomer_request_load;
		this.id_staff=id_staff;
		this.jumlah = jumlah;
		this.jumlah1 = jumlah1;
		this.jumlah2 = jumlah2;
		this.jumlah3 = jumlah3;
	}

	public int getId_penjualan_detail() {
		return id_penjualan_detail;
	}

	public void setId_penjualan_detail(int id_penjualan_detail) {
		this.id_penjualan_detail = id_penjualan_detail;
	}

	public String getNo_penjualan() {
		return no_penjualan;
	}

	public void setNo_penjualan(String no_penjualan) {
		this.no_penjualan= no_penjualan;
	}

	public String getId_product() {
		return id_product;
	}

	public void setId_product(String id_product) {
		this.id_product= id_product;
	}

	public String getHarga() {
		return harga;
	}

	public void setHarga(String harga) {
		this.harga= harga;
	}

	public String getHarga_jual() {
		return harga_jual;
	}

	public void setHarga_jual(String harga_jual) {
		this.harga_jual= harga_jual;
	}

	public String getNomer_request_load() {
		return nomer_request_load;
	}

	public void setNomer_request_load(String nomer_request_load) {
		this.nomer_request_load= nomer_request_load;
	}

	public String getId_staff(){
		return id_staff;
	}

	public void setId_staff (String id_staff){
		this.id_staff=id_staff;
	}

	public String getJumlah() {
		return jumlah;
	}

	public void setJumlah(String jumlah) {
		this.jumlah= jumlah;
	}

	public String getJumlah1() {
		return jumlah1;
	}

	public void setJumlah1(String jumlah1) {
		this.jumlah1= jumlah1;
	}

	public String getJumlah2() {
		return jumlah2;
	}

	public void setJumlah2(String jumlah2) {
		this.jumlah2= jumlah2;
	}

	public String getJumlah3() {
		return jumlah3;
	}

	public void setJumlah3(String jumlah3) {
		this.jumlah3= jumlah3;
	}

}