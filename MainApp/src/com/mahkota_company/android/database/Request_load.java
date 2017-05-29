package com.mahkota_company.android.database;

public class Request_load {
	private int id_request_load;
	private String id_staff;
	private String id_product;
	private String status;
	private int jumlah_request;

	public Request_load() {

	}

	public Request_load(int id_request_load, String id_staff, String id_product,
						String status, int jumlah_request) {
		this.id_request_load = id_request_load;
		this.id_staff = id_staff;
		this.id_product = id_product;
		this.status = status;
		this.jumlah_request = jumlah_request;


	}

	public int getId_request_load() {
		return id_request_load;
	}

	public void setId_request_load(int id_product) {
		this.id_request_load = id_request_load;
	}

	public String getId_staff() {
		return id_staff;
	}

	public void setId_staff(String id_staff) {
		this.id_staff = id_staff;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId_product() {
		return id_product;
	}

	public void setId_product(String id_product) {
		this.id_product = id_product;
	}

	public int getJumlah_request() {
		return jumlah_request;
	}

	public void setJumlah_request(int jumlah_request) {
		this.jumlah_request = jumlah_request;
	}

}
