package com.mahkota_company.android.database;

public class Penjualan {

	private int id_penjualan;
	private String nomer_product_terjual;
	private String date_product_terjual;
	private String time_product_terjual;
	private int id_customer;
	private int id_staff;
	private int diskon;

	public Penjualan() {

	}

	// constructor
	public Penjualan(int id_penjualan, String nomer_product_terjual,
			String date_product_terjual, String time_product_terjual,
			int id_customer, int id_staff, int diskon) {
		this.id_penjualan = id_penjualan;
		this.nomer_product_terjual = nomer_product_terjual;
		this.date_product_terjual = date_product_terjual;
		this.time_product_terjual = time_product_terjual;
		this.id_customer = id_customer;
		this.id_staff = id_staff;
		this.diskon = diskon;
	}

	public int getId_penjualan() {
		return id_penjualan;
	}

	public void setId_penjualan(int id_penjualan) {
		this.id_penjualan = id_penjualan;
	}

	public String getNomer_product_terjual() {
		return nomer_product_terjual;
	}

	public void setNomer_product_terjual(String nomer_product_terjual) {
		this.nomer_product_terjual = nomer_product_terjual;
	}

	public String getDate_product_terjual() {
		return date_product_terjual;
	}

	public void setDate_product_terjual(String date_product_terjual) {
		this.date_product_terjual = date_product_terjual;
	}

	public String getTime_product_terjual() {
		return time_product_terjual;
	}

	public void setTime_product_terjual(String time_product_terjual) {
		this.time_product_terjual = time_product_terjual;
	}

	public int getId_customer() {
		return id_customer;
	}

	public void setId_customer(int id_customer) {
		this.id_customer = id_customer;
	}

	public int getId_staff() {
		return id_staff;
	}

	public void setId_staff(int id_staff) {
		this.id_staff = id_staff;
	}

	public int getDiskon() {
		return diskon;
	}

	public void setDiskon(int diskon) {
		this.diskon = diskon;
	}

}