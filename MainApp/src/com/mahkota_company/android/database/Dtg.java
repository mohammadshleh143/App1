package com.mahkota_company.android.database;

public class Dtg {
	private int id;
	private String kode_customer;
	private String nama_toko;
	private String no_faktur;
	private String nilai_faktur;
	private String jml_belum_bayar;
	private String no_dtg;


	public Dtg() {

	}

	public Dtg(int id, String kode_customer, String nama_toko, String no_faktur,
			   String nilai_faktur,String jml_belum_bayar,String no_dtg) {
		this.id = id;
		this.kode_customer = kode_customer;
		this.nama_toko = nama_toko;
		this.no_faktur = no_faktur;
		this.nilai_faktur = nilai_faktur;
		this.jml_belum_bayar = jml_belum_bayar;
		this.no_dtg = no_dtg;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKode_customer() {
		return kode_customer;
	}

	public void setKode_customer(String kode_customer) {
		this.kode_customer= kode_customer;
	}

	public String getNama_toko() {
		return nama_toko;
	}

	public void setNama_toko(String nama_toko) {
		this.nama_toko = nama_toko;
	}

	public String getNo_faktur() {
		return no_faktur;
	}

	public void setNo_faktur(String no_faktur) {
		this.no_faktur= no_faktur;
	}

	public String getNilai_faktur() {
		return nilai_faktur;
	}

	public void setNilai_faktur(String nilai_faktur) {
		this.nilai_faktur= nilai_faktur;
	}

	public String getJml_belum_bayar() {
		return jml_belum_bayar;
	}

	public void setJml_belum_bayar(String jml_belum_bayar) {
		this.jml_belum_bayar= jml_belum_bayar;
	}

	public String getNo_dtg() {
		return no_dtg;
	}

	public void setNo_dtg(String no_dtg) {
		this.no_dtg= no_dtg;
	}

}
