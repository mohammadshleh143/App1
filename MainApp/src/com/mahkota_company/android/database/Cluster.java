package com.mahkota_company.android.database;

public class Cluster {

	private int id_cluster;
	private String nama_cluster;
	private String description;

	public Cluster() {

	}

	// constructor
	public Cluster(int id_cluster, String nama_cluster, String description) {
		this.id_cluster = id_cluster;
		this.nama_cluster = nama_cluster;
		this.description = description;
	}

	public int getId_cluster() {
		return id_cluster;
	}

	public void setId_cluster(int id_cluster) {
		this.id_cluster = id_cluster;
	}

	public String getNama_cluster() {
		return nama_cluster;
	}

	public void setNama_cluster(String nama_cluster) {
		this.nama_cluster = nama_cluster;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}