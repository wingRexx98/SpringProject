package com.restaurant.model;

import java.io.Serializable;

public class Food implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private int cateID;

	private String foodName;

	private double price;

	private String ingredients;

	private byte[] image;

	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCateID() {
		return cateID;
	}

	public void setCateID(int cateID) {
		this.cateID = cateID;
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

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Food(int id, int cateID, String foodName, double price, String ingredients, byte[] image, boolean enabled) {
		super();
		this.id = id;
		this.cateID = cateID;
		this.foodName = foodName;
		this.price = price;
		this.ingredients = ingredients;
		this.image = image;
		this.enabled = enabled;
	}

	public Food() {
		super();
	}

	@Override
	public String toString() {
		return this.id+ "|cateID : " + this.cateID + "	|foodName: " + this.foodName + "	|price: " + this.price
				+ "	|ingredients: " + this.ingredients;
	}

}
