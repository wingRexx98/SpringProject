package com.restaurant.model;

import org.springframework.util.SocketUtils;

public class OrderDetailInfo {

	private int foodId;

	private String foodName;

	private int quantity;

	private double quantityPrice;

	private byte[] image;

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getQuanity() {
		return quantity;
	}

	public void setQuanity(int quanity) {
		this.quantity = quanity;
	}

	public double getQuantityPrice() {
		return quantityPrice;
	}

	public void setQuantityPrice(double quantityPrice) {
		this.quantityPrice = quantityPrice;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public OrderDetailInfo(int foodId, String foodName, int quantity, double quantityPrice, byte[] image) {
		super();
		this.foodId = foodId;
		this.foodName = foodName;
		this.quantity = quantity;
		this.quantityPrice = quantityPrice;
		this.image = image;
	}

	public OrderDetailInfo() {
		super();
	}

	@Override
	public String toString() {
		return "|Item name: " + this.getFoodName() + " %-15|Quantity: " + this.getQuanity()
				+ " %-15|Price based on quantity: " + this.getQuantityPrice() + " |\n";
	}
}
