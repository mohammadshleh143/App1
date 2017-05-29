package com.mahkota_company.android.database;

public class ProductTarget {

	private int id_product_target;
	private String nomer_product_target;
	private String created_date_product_target;
	private String created_time_product_target;
	private String updated_date_product_target;
	private String updated_time_product_target;
	private int created_by;
	private int updated_by;
	private int id_staff;
	private int id_customer;
	private int id_product;
	private int jumlah_target;
	private int jumlah_terjual;

	public ProductTarget() {

	}

	// constructor
	public ProductTarget(int id_product_target, String nomer_product_target,
			String created_date_product_target,
			String created_time_product_target,
			String updated_date_product_target,
			String updated_time_product_target, int created_by,
			int updated_by, int id_staff, int id_customer, int id_product,
			int jumlah_target, int jumlah_terjual) {
		this.id_product_target = id_product_target;
		this.nomer_product_target = nomer_product_target;
		this.created_date_product_target = created_date_product_target;
		this.created_time_product_target = created_time_product_target;
		this.updated_date_product_target = updated_date_product_target;
		this.updated_time_product_target = updated_time_product_target;
		this.created_by = created_by;
		this.updated_by = updated_by;
		this.id_staff = id_staff;
		this.id_customer = id_customer;
		this.id_product = id_product;
		this.jumlah_target = jumlah_target;
		this.jumlah_terjual = jumlah_terjual;
	}

	public int getId_product_target() {
		return id_product_target;
	}

	public void setId_product_target(int id_product_target) {
		this.id_product_target = id_product_target;
	}

	public String getNomer_product_target() {
		return nomer_product_target;
	}

	public void setNomer_product_target(String nomer_product_target) {
		this.nomer_product_target = nomer_product_target;
	}

	public String getCreated_date_product_target() {
		return created_date_product_target;
	}

	public void setCreated_date_product_target(
			String created_date_product_target) {
		this.created_date_product_target = created_date_product_target;
	}

	public String getCreated_time_product_target() {
		return created_time_product_target;
	}

	public void setCreated_time_product_target(
			String created_time_product_target) {
		this.created_time_product_target = created_time_product_target;
	}

	public String getUpdated_date_product_target() {
		return updated_date_product_target;
	}

	public void setUpdated_date_product_target(
			String updated_date_product_target) {
		this.updated_date_product_target = updated_date_product_target;
	}

	public String getUpdated_time_product_target() {
		return updated_time_product_target;
	}

	public void setUpdated_time_product_target(
			String updated_time_product_target) {
		this.updated_time_product_target = updated_time_product_target;
	}

	public int getCreated_by() {
		return created_by;
	}

	public void setCreated_by(int created_by) {
		this.created_by = created_by;
	}

	public int getUpdated_by() {
		return updated_by;
	}

	public void setUpdated_by(int updated_by) {
		this.updated_by = updated_by;
	}

	public int getId_staff() {
		return id_staff;
	}

	public void setId_staff(int id_staff) {
		this.id_staff = id_staff;
	}

	public int getId_customer() {
		return id_customer;
	}

	public void setId_customer(int id_customer) {
		this.id_customer = id_customer;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
	}

	public int getJumlah_target() {
		return jumlah_target;
	}

	public void setJumlah_target(int jumlah_target) {
		this.jumlah_target = jumlah_target;
	}

	public int getJumlah_terjual() {
		return jumlah_terjual;
	}

	public void setJumlah_terjual(int jumlah_terjual) {
		this.jumlah_terjual = jumlah_terjual;
	}

}
