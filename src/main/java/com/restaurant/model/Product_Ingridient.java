package com.restaurant.model;

public class Product_Ingridient {

	private int foodId;
	private int ingridientId;
	private int quantity;

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getIngridientId() {
		return ingridientId;
	}

	public void setIngridientId(int ingridientId) {
		this.ingridientId = ingridientId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Product_Ingridient(int foodId, int ingridientId, int quantity) {
		super();
		this.foodId = foodId;
		this.ingridientId = ingridientId;
		this.quantity = quantity;
	}

	public Product_Ingridient() {
		super();
	}

}
