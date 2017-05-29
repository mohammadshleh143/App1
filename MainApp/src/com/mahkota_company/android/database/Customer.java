package com.mahkota_company.android.database;

public class Customer {
	private int id_customer;
	private String kode_customer;
	private String email;
	private String alamat;
	private String lats;
	private String longs;
	private String nama_lengkap;
	private String no_telp;
	private int id_wilayah;
	private String foto_1;
	private String foto_2;
	private String foto_3;
	private int id_type_customer;
	private String blokir;
	private String date;
	private String status_update;
	private int id_staff;

	private String no_ktp;
	private String tanggal_lahir;
	private String nama_bank;

	private String no_rekening;
	private String atas_nama;
	private String npwp;
	private String nama_pasar;
	private int id_cluster;
	private String telp;
	private String fax;
	private String omset;

	private String cara_pembayaran;
	private String plafon_kredit;
	private String term_kredit;
	private String nama_istri;
	private String nama_anak1;
	private String nama_anak2;
	private String nama_anak3;
	private String kode_pos;
	private int id_depo;
	private String isactive;
	private String description;
	private String nama_toko;
	private String ttd1;
	private String ttd2;


	public Customer() {

	}

	public Customer(int id_customer, String kode_customer, String email,

					String alamat, String lats, String longs, String nama_lengkap,

					String no_telp, int id_wilayah, String foto_1, String foto_2,

					String foto_3, int id_type_customer, String blokir, String date,

					String status_update, int id_staff, String no_ktp,

					String tanggal_lahir, String nama_bank, String no_rekening,

					String atas_nama, String npwp, String nama_pasar, int id_cluster,

					String telp, String fax, String omset, String cara_pembayaran,

					String plafon_kredit, String term_kredit, String nama_istri,

					String nama_anak1 ,String nama_anak2,String nama_anak3, String kode_pos,

					int id_depo, String isactive, String description, String nama_toko, String ttd1,

					String ttd2){


		this.id_customer = id_customer;
		this.kode_customer = kode_customer;
		this.email = email;
		this.alamat = alamat;
		this.lats = lats;
		this.longs = longs;
		this.nama_lengkap = nama_lengkap;
		this.no_telp = no_telp;
		this.id_wilayah = id_wilayah;
		this.foto_1 = foto_1;
		this.foto_2 = foto_2;
		this.foto_3 = foto_3;
		this.id_type_customer = id_type_customer;
		this.blokir = blokir;
		this.date = date;
		this.status_update = status_update;
		this.id_staff = id_staff;

		this.no_ktp = no_ktp;
		this.tanggal_lahir = tanggal_lahir;
		this.nama_bank = nama_bank;
		this.no_rekening = no_rekening;
		this.atas_nama = atas_nama;
		this.npwp = npwp;

		this.nama_pasar = nama_pasar;
		this.id_cluster = id_cluster;
		this.telp = telp;
		this.fax = fax;
		this.omset = omset;
		this.cara_pembayaran = cara_pembayaran;
		this.plafon_kredit = plafon_kredit;
		this.term_kredit = term_kredit;
		this.nama_istri = nama_istri;
		this.nama_anak1 = nama_anak1;
		this.nama_anak2 = nama_anak2;
		this.nama_anak3 = nama_anak3;
		this.kode_pos = kode_pos;
		this.id_depo = id_depo;
		this.isactive = isactive;
		this.description = description;
		this.nama_toko = nama_toko;
		this.ttd1 = ttd1;
		this.ttd2 = ttd2;

	}



	public int getId_customer() {
		return id_customer;
	}

	public void setId_customer(int id_customer) {
		this.id_customer = id_customer;
	}

	public String getKode_customer() {
		return kode_customer;
	}

	public void setKode_customer(String kode_customer) {
		this.kode_customer=kode_customer;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlamat() {
		return alamat;
	}

	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}

	public String getLats() {
		return lats;
	}

	public void setLats(String lats) {
		this.lats = lats;
	}

	public String getLongs() {
		return longs;
	}

	public void setLongs(String longs) {
		this.longs = longs;
	}

	public String getNama_lengkap() {
		return nama_lengkap;
	}

	public void setNama_lengkap(String nama_lengkap) {
		this.nama_lengkap = nama_lengkap;
	}

	public String getNo_telp() {
		return no_telp;
	}

	public void setNo_telp(String no_telp) {
		this.no_telp = no_telp;
	}

	public int getId_wilayah() {
		return id_wilayah;
	}

	public void setId_wilayah(int id_wilayah) {
		this.id_wilayah = id_wilayah;
	}

	public String getFoto_1() {
		return foto_1;
	}

	public void setFoto_1(String foto_1) {
		this.foto_1 = foto_1;
	}

	public String getFoto_2() {
		return foto_2;
	}

	public void setFoto_2(String foto_2) {
		this.foto_2 = foto_2;
	}

	public String getFoto_3() {
		return foto_3;
	}

	public void setFoto_3(String foto_3) {
		this.foto_3 = foto_3;
	}

	public int getId_type_customer() {
		return id_type_customer;
	}

	public void setId_type_customer(int id_type_customer) {
		this.id_type_customer = id_type_customer;
	}

	public String getBlokir() {
		return blokir;
	}

	public void setBlokir(String blokir) {
		this.blokir = blokir;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getStatus_update() {
		return status_update;
	}

	public void setStatus_update(String status_update) {
		this.status_update = status_update;
	}

	public int getId_staff() {
		return id_staff;
	}

	public void setId_staff(int id_staff) {
		this.id_staff = id_staff;
	}

	public String getNo_ktp() {
		return no_ktp;
	}

	public void setNo_ktp(String no_ktp) {
		this.no_ktp = no_ktp;
	}

	public String getTanggal_lahir() {
		return tanggal_lahir;
	}

	public void setTanggal_lahir(String tanggal_lahir) {
		this.tanggal_lahir = tanggal_lahir;
	}

	public String getNama_bank() {
		return nama_bank;
	}

	public void setNama_bank(String nama_bank) {
		this.nama_bank = nama_bank;
	}

	public String getNo_rekening() {
		return no_rekening;
	}
	public void setNo_rekening(String no_rekening) {
		this.no_rekening = no_rekening;
	}

	public String getAtas_nama() {
		return atas_nama;
	}
	public void setAtas_nama(String atas_nama) {
		this.atas_nama = atas_nama;
	}

	public String getNpwp() {
		return npwp;
	}
	public void setNpwp(String npwp) {
		this.npwp = npwp;
	}

	public String getNama_pasar() {
		return nama_pasar;
	}
	public void setNama_pasar(String nama_pasar) {
		this.nama_pasar = nama_pasar;
	}

	public int getId_cluster(){
		return  id_cluster;
	}
	public void setId_cluster(int id_cluster){
		this.id_cluster = id_cluster;
	}

	public  String getTelp (){
		return telp;
	}

	public void setTelp(String telp) {
		this.telp = telp;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getOmset() {
		return omset;
	}

	public void setOmset(String omset) {
		this.omset = omset;
	}

	public String getCara_pembayaran() {
		return cara_pembayaran;
	}
	public void setCara_pembayaran(String cara_pembayaran) {
		this.cara_pembayaran = cara_pembayaran;
	}

	public String getPlafon_kredit() {
		return plafon_kredit;
	}
	public void setPlafon_kredit(String plafon_kredit) {
		this.plafon_kredit = plafon_kredit;
	}

	public String getTerm_kredit() {
		return term_kredit;
	}
	public void setTerm_kredit(String  term_kredit) {
		this.term_kredit = term_kredit;
	}

	public String getNama_istri() {
		return nama_istri;
	}
	public void setNama_istri(String  nama_istri) {
		this.nama_istri = nama_istri;
	}

	public String getNama_anak1() {
		return nama_anak1;
	}
	public void setNama_anak1(String  nama_anak1) {
		this.nama_anak1 = nama_anak1;
	}

	public String getNama_anak2() {
		return nama_anak2;
	}
	public void setNama_anak2(String  nama_anak2) {
		this.nama_anak2 = nama_anak2;
	}

	public String getNama_anak3() {
		return nama_anak3;
	}
	public void setNama_anak3(String  nama_anak3) {
		this.nama_anak3 = nama_anak3;
	}

	public String getKode_pos() {
		return kode_pos;
	}
	public void setKode_pos(String  kode_pos) {
		this.kode_pos = kode_pos;
	}

	public int getId_depo() {
		return id_depo;
	}
	public void setId_depo(int  id_depo) {
		this.id_depo = id_depo;
	}

	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String  isactive) {
		this.isactive = isactive;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String  description) {
		this.description = description;
	}

	public String getNama_toko() {
		return nama_toko;
	}
	public void setNama_toko(String  nama_toko) {
		this.nama_toko = nama_toko;
	}

	public String getTtd1() {
		return ttd1;
	}
	public void setTtd1(String  ttd1) {
		this.ttd1 = ttd1;
	}

	public String getTtd2() {
		return ttd2;
	}
	public void setTtd2(String  ttd2) {
		this.ttd2 = ttd2;
	}

}
