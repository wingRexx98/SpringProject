package com.restaurant.form;

import org.springframework.web.multipart.MultipartFile;

public class FoodForm {
	
	private int cateID;

	private String foodName;

	private double price;

	private String ingredients;

	// Upload file.
    private MultipartFile image;

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

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public FoodForm(int cateID, String foodName, double price, String ingredients, MultipartFile image) {
		super();
		this.cateID = cateID;
		this.foodName = foodName;
		this.price = price;
		this.ingredients = ingredients;
		this.image = image;
	}

	public FoodForm() {
		super();
	}
	
}
