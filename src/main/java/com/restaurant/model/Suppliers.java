package com.restaurant.model;

public class Suppliers {
	private int id;
	private String name;
	private String descr;
	private String address;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Suppliers(int id, String name, String descr, String address) {
		super();
		this.id = id;
		this.name = name;
		this.descr = descr;
		this.address = address;
	}

	public Suppliers() {

	}

}
