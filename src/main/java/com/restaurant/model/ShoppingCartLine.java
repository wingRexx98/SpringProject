package com.restaurant.model;

public class ShoppingCartLine {
	private FoodInfo food;
	private int quantity;

	public FoodInfo getFood() {
		return food;
	}

	public void setFood(FoodInfo food) {
		this.food = food;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ShoppingCartLine(FoodInfo food, int quantity) {
		super();
		this.food = food;
		this.quantity = quantity;
	}

	public ShoppingCartLine() {
		this.quantity = 0;
	}
	
	public double getAmount() {
		return this.food.getPrice()*quantity;
	}

}
