package com.restaurant.model;


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
		String format1 = String.format("|%-30s|", "Item name: "+ this.getFoodName());
		String format2 = String.format("|%-30s|", "Quantity: "+ this.getQuanity());
		String format3 = String.format("|%-30s|", "Price based on quantity: "+ this.getQuantityPrice());
		String finalString = "\n"+ format1.concat(format2).concat(format3);
		return finalString;
	}
}
