package com.restaurant.model;

import java.io.Serializable;

public class Ingedients implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private int stockQuantity;
	private double price;
	private String lastReStockDate;
	private byte[] image;

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

	public int getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(int stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getLastReStockDate() {
		return lastReStockDate;
	}

	public void setLastReStockDate(String lastReStockDate) {
		this.lastReStockDate = lastReStockDate;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Ingedients(int id, String name, int stockQuantity, double price, String lastReStockDate, byte[] image) {
		super();
		this.id = id;
		this.name = name;
		this.stockQuantity = stockQuantity;
		this.price = price;
		this.lastReStockDate = lastReStockDate;
		this.image = image;
	}

	public Ingedients() {

	}

}
