package com.mahkota_company.android.database;

public class DetailReqLoad {
	private int id_sales_order;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String jumlah_order;
	private String jumlah_order1;
	private String jumlah_order2;
	private String jumlah_order3;
	private String uomqtyl1;
	private String uomqtyl2;
	private String uomqtyl3;
	private String uomqtyl4;
	private int id_product;

	public DetailReqLoad(int count, String nama_product, String kode_product, String harga_jual,
						 String stock, String uomqtyl1, String uomqtyl2, String uomqtyl3, String uomqtyl4, int id_product) {

	}

	// constructor
	public DetailReqLoad(int id_sales_order, String nama_product,
						 String kode_product, String harga_jual, String jumlah_order,
						 String jumlah_order1, String jumlah_order2, String jumlah_order3
						 ,String uomqtyl1, String uomqtyl2, String uomqtyl3, String uomqtyl4,int id_product
						 ) {
		this.setId_sales_order(id_sales_order);
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlah_order = jumlah_order;
		this.jumlah_order1 = jumlah_order1;
		this.jumlah_order2 = jumlah_order2;
		this.jumlah_order3 = jumlah_order3;
		this.uomqtyl1 = uomqtyl1;
		this.uomqtyl2 = uomqtyl2;
		this.uomqtyl3 = uomqtyl3;
		this.uomqtyl4 = uomqtyl4;
		this.id_product = id_product;

	}

	public int getId_sales_order() {
		return id_sales_order;
	}

	public void setId_sales_order(int id_sales_order) {
		this.id_sales_order = id_sales_order;
	}

	public String getNama_product() {
		return nama_product;
	}

	public void setNama_product(String nama_product) {
		this.nama_product = nama_product;
	}

	public String getKode_product() {
		return kode_product;
	}

	public void setKode_product(String kode_product) {
		this.kode_product = kode_product;
	}

	public String getHarga_jual() {
		return harga_jual;
	}

	public void setHarga_jual(String harga_jual) {
		this.harga_jual = harga_jual;
	}

	public String getJumlah_order() {
		return jumlah_order;
	}

	public void setJumlah_order(String jumlah_order) {
		this.jumlah_order = jumlah_order;
	}

	public String getJumlah_order1() {
		return jumlah_order1;
	}

	public void setJumlah_order1(String jumlah_order1) {
		this.jumlah_order1 = jumlah_order1;
	}

	public String getJumlah_order2() {
		return jumlah_order2;
	}

	public void setJumlah_order2(String jumlah_order2) {
		this.jumlah_order2 = jumlah_order2;
	}

	public String getJumlah_order3() {
		return jumlah_order3;
	}

	public void setJumlah_order3(String jumlah_order3) {
		this.jumlah_order3 = jumlah_order3;
	}

	public String getUomqtyl1() {
		return uomqtyl1;
	}

	public void setUomqtyl1(String uomqtyl1) {
		this.uomqtyl1 = uomqtyl1;
	}

	public String getUomqtyl2() {
		return uomqtyl2;
	}

	public void setUomqtyl2(String uomqtyl2) {
		this.uomqtyl2 = uomqtyl2;
	}

	public String getUomqtyl3() {
		return uomqtyl3;
	}

	public void setUomqtyl3(String uomqtyl3) {
		this.uomqtyl3 = uomqtyl3;
	}

	public String getUomqtyl4() {
		return uomqtyl4;
	}

	public void setUomqtyl4(String uomqtyl4) {
		this.uomqtyl4 = uomqtyl4;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
	}

}