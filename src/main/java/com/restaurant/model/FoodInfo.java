package com.restaurant.model;

public class FoodInfo {

	private int id;

	private String foodName;

	private double price;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}


	public FoodInfo(Food food) {
		super();
		this.id = food.getId();
		this.foodName = food.getFoodName();
		this.price = food.getPrice();
	}
	
	public FoodInfo(int id, String foodName, double price) {
		super();
		this.id = id;
		this.foodName = foodName;
		this.price = price;
	}

	public FoodInfo() {
		super();
	}

}
