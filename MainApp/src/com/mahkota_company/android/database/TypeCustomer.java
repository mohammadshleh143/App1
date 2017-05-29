package com.mahkota_company.android.database;

public class TypeCustomer {
	private int id_type_customer;
	private String type_customer;
	private String deskripsi;

	public TypeCustomer() {

	}

	// constructor
	public TypeCustomer(int id_type_customer, String type_customer,
			String deskripsi) {
		this.id_type_customer = id_type_customer;
		this.type_customer = type_customer;
		this.deskripsi = deskripsi;
	}

	public int getId_type_customer() {
		return id_type_customer;
	}

	public void setId_type_customer(int id_type_customer) {
		this.id_type_customer = id_type_customer;
	}

	public String getType_customer() {
		return type_customer;
	}

	public void setType_customer(String type_customer) {
		this.type_customer = type_customer;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

}