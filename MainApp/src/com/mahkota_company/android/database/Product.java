package com.mahkota_company.android.database;

public class Product {
	private int id_product;
	private String nama_product;
	private String kode_product;
	private String harga_jual;
	private String stock;
	private String id_kemasan;
	private String foto;
	private String deskripsi;
	private String uomqtyl1;
	private String uomqtyl2;
	private String uomqtyl3;
	private String uomqtyl4;
	private String status;

	public Product() {

	}

	public Product(int id_product, String nama_product, String kode_product,
			String harga_jual, String stock, String id_kemasan, String foto,
			String deskripsi, String uomqtyl1, String uomqtyl2, String uomqtyl3,
			String uomqtyl4,String status) {
		this.id_product = id_product;
		this.nama_product = nama_product;
		this.kode_product = kode_product;
		this.harga_jual = harga_jual;
		this.stock = stock;
		this.id_kemasan = id_kemasan;
		this.foto = foto;
		this.deskripsi = deskripsi;
		this.uomqtyl1 = uomqtyl1;
		this.uomqtyl2 = uomqtyl2;
		this.uomqtyl3 = uomqtyl3;
		this.uomqtyl4 = uomqtyl4;
		this.status= status;

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

	public String getStock() {
		return stock;
	}

	public void setStock(String stock) {
		this.stock = stock;
	}

	public String getId_kemasan() {
		return id_kemasan;
	}

	public void setId_kemasan(String id_kemasan) {
		this.id_kemasan = id_kemasan;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
