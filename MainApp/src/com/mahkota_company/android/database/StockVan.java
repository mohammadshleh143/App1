package com.mahkota_company.android.database;

public class StockVan {
	private int id_product;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String jumlahRequest;
	private String jumlahAccept;
	private String jumlahSisa;
	private String idKemasan;
	private String foto;
	private String deskripsi;
	private String nomer_request_load;

	public StockVan() {

	}

	public StockVan(int id_product, String nama_product, String kode_product,
			String harga_jual, String jumlahRequest, String jumlahAccept,
			String jumlahSisa, String id_kemasan, String foto, String deskripsi, String nomer_request_load) {
		this.id_product = id_product;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.jumlahRequest = jumlahRequest;
		this.jumlahAccept = jumlahAccept;
		this.jumlahSisa = jumlahSisa;
		this.idKemasan = id_kemasan;
		this.foto = foto;
		this.deskripsi = deskripsi;
		this.nomer_request_load = nomer_request_load;

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

	public String getJumlahRequest() {
		return jumlahRequest;
	}

	public void setJumlahRequest(String jumlahRequest) {
		this.jumlahRequest = jumlahRequest;
	}

	public String getJumlahAccept() {
		return jumlahAccept;
	}

	public void setJumlahAccept(String jumlahAccept) {
		this.jumlahAccept = jumlahAccept;
	}

	public String getJumlahSisa() {
		return jumlahSisa;
	}

	public void setJumlahSisa(String jumlahSisa) {
		this.jumlahSisa = jumlahSisa;
	}

	public String getIdKemasan() {
		return idKemasan;
	}

	public void setIdKemasan(String idKemasan) {
		this.idKemasan = idKemasan;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public  String getNomer_request_load(){
		return  nomer_request_load;
	}

	public void  setNomer_request_load(String nomer_request_load){
		this.nomer_request_load = nomer_request_load;
	}
}
