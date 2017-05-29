package com.mahkota_company.android.database;

public class ProductPrice {
	private int id;
	private String id_product;
	private String pricelist;
	private String pricestd;
	private String pricelimit;


	public ProductPrice() {

	}

	public ProductPrice(int id, String id_product, String pricelist, String pricestd,
						String pricelimit) {
		this.id = id;
		this.id_product = id_product;
		this.pricelist = pricelist;
		this.pricestd = pricestd;
		this.pricelimit = pricelimit;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getId_product() {
		return id_product;
	}

	public void setId_product(String id_product) {
		this.id_product = id_product;
	}

	public String getPricelist() {
		return pricelist;
	}

	public void setPricelist(String pricelist) {
		this.pricelist = pricelist;
	}

	public String getPricestd() {
		return pricestd;
	}

	public void setPricestd(String pricestd) {
		this.pricestd = pricestd;
	}

	public String getPricelimit() {
		return pricelimit;
	}

	public void setPricelimit(String pricelimit) {
		this.pricelimit = pricelimit;
	}


}
