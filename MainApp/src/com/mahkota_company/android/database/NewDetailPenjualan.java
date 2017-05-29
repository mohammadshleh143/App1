package com.mahkota_company.android.database;

public class NewDetailPenjualan {
	private int id_detail_penjualan;
	private int id_product;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private int jumlah_order;
	private int jumlah_order1;
	private int jumlah_order2;
	private int jumlah_order3;
	private String unit;
	private String harga;
	private String nomer_request_load;
	private int id_staff;
	private float jmldisc;

	public NewDetailPenjualan() {

	}

	// constructor
	public NewDetailPenjualan(int id_sales_order, int id_product,
							  String nama_product, String kode_product, String harga_jual,
							  int jumlah_order,int jumlah_order1,int jumlah_order2,
							  int jumlah_order3,String harga, String nomer_request_load,
							  int id_staff,float jmldisc){//, String unit) {
		this.id_detail_penjualan = id_sales_order;
		this.id_product = id_product;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlah_order = jumlah_order;
		this.jumlah_order1 = jumlah_order1;
		this.jumlah_order2 = jumlah_order2;
		this.jumlah_order3 = jumlah_order3;
		this.harga = harga;
		this.nomer_request_load = nomer_request_load;
		this.id_staff = id_staff;
		this.jmldisc = jmldisc;
		//this.unit = unit;

	}

	public int getId_sales_order() {
		return id_detail_penjualan;
	}

	public void setId_sales_order(int id_sales_order) {
		this.id_detail_penjualan = id_sales_order;
	}

	public int getId_product() {
		return id_product;
	}

	public void setId_product(int id_product) {
		this.id_product = id_product;
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

	public int getJumlah_order() {
		return jumlah_order;
	}

	public void setJumlah_order(int jumlah_order) {
		this.jumlah_order= jumlah_order;
	}

	public int getJumlah_order1() {
		return jumlah_order1;
	}

	public void setJumlah_order1(int jumlah_order1) {
		this.jumlah_order1 = jumlah_order1;
	}

	public int getJumlah_order2() {
		return jumlah_order2;
	}

	public void setJumlah_order2(int jumlah_order2) {
		this.jumlah_order2 = jumlah_order2;
	}

	public int getJumlah_order3() {
		return jumlah_order3;
	}

	public void setJumlah_order3(int jumlah_order3) {
		this.jumlah_order3 = jumlah_order3;
	}

	public String getHarga() {
		return harga;
	}

	public void setHarga(String harga) {
		this.harga= harga;
	}


	public String getNomer_request_load() {
		return nomer_request_load;
	}

	public void setNomer_request_load(String nomer_request_load) {
		this.nomer_request_load= nomer_request_load;
	}

	public int getId_staff() {
		return id_staff;
	}

	public void setId_staff(int id_staff) {
		this.id_staff= id_staff;
	}

	public float getJmldisc() {
		return jmldisc;
	}

	public void setJmldisc(float jmldisc) {
		this.jmldisc = jmldisc;
	}
	//public String getUnit() {
	//	return unit;
	//}

	//public void setUnit(String unit) {
	//	this.unit = unit;
	//}

}