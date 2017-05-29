package com.mahkota_company.android.database;

public class StockOnHand {

	private int id_stock_on_hand;
	private String nomer_stock_on_hand;
	private String nomer_stock_on_hand_detail;
	private String date_stock_on_hand;
	private String time_stock_on_hand;
	private String deskripsi;
	private String username;
	private String kode_customer;
	private String alamat;
	private String nama_lengkap;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String stockpcs;
	private String stockrcg;
	private String stockpck;
	private String stockdus;

	public StockOnHand() {

	}

	// constructor
	public StockOnHand(int id_stock_on_hand, String nomer_stock_on_hand,
			String nomer_stock_on_hand_detail, String date_stock_on_hand,
			String time_stock_on_hand, String deskripsi, String username,
			String kode_customer, String alamat, String nama_lengkap,
			String nama_product, String kode_product, String harga_jual,
			String stockpcs, String stockrcg, String stockpck, String stockdus) {
		this.id_stock_on_hand = id_stock_on_hand;
		this.nomer_stock_on_hand = nomer_stock_on_hand;
		this.nomer_stock_on_hand_detail = nomer_stock_on_hand_detail;
		this.date_stock_on_hand = date_stock_on_hand;
		this.time_stock_on_hand = time_stock_on_hand;
		this.deskripsi = deskripsi;
		this.username = username;
		this.kode_customer = kode_customer;
		this.alamat = alamat;
		this.nama_lengkap = nama_lengkap;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.stockpcs = stockpcs;
		this.stockrcg = stockrcg;
		this.stockpck = stockpck;
		this.stockdus = stockdus;

	}

	public int getId_stock_on_hand() {
		return id_stock_on_hand;
	}

	public void setId_stock_on_hand(int id_stock_on_hand) {
		this.id_stock_on_hand = id_stock_on_hand;
	}

	public String getNomer_stock_on_hand() {
		return nomer_stock_on_hand;
	}

	public void setNomer_stock_on_hand(String nomer_stock_on_hand) {
		this.nomer_stock_on_hand = nomer_stock_on_hand;
	}

	public String getNomer_stock_on_hand_detail() {
		return nomer_stock_on_hand_detail;
	}

	public void setNomer_stock_on_hand_detail(String nomer_stock_on_hand_detail) {
		this.nomer_stock_on_hand_detail = nomer_stock_on_hand_detail;
	}

	public String getDate_stock_on_hand() {
		return date_stock_on_hand;
	}

	public void setDate_stock_on_hand(String date_stock_on_hand) {
		this.date_stock_on_hand = date_stock_on_hand;
	}

	public String getTime_stock_on_hand() {
		return time_stock_on_hand;
	}

	public void setTime_stock_on_hand(String time_stock_on_hand) {
		this.time_stock_on_hand = time_stock_on_hand;
	}

	public String getDeskripsi() {
		return deskripsi;
	}

	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getKode_customer() {
		return kode_customer;
	}

	public void setKode_customer(String kode_customer) {
		this.kode_customer = kode_customer;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getNama_lengkap() {
		return nama_lengkap;
	}

	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
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

	public String getStockpcs() {
		return stockpcs;
	}

	public void setStockpcs(String stockpcs) {
		this.stockpcs = stockpcs;
	}

	public String getStockrcg() {
		return stockrcg;
	}

	public void setStockrcg(String stockrcg) {
		this.stockrcg = stockrcg;
	}

	public String getStockpck() {
		return stockpck;
	}

	public void setStockpck(String stockcpk) {
		this.stockpck = stockpck;
	}

	public String getStockdus() {
		return stockdus;
	}

	public void setStockdus(String stockdus) {
		this.stockdus = stockdus;
	}

}