package com.restaurant.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.restaurant.model.Food;

public class FoodForm {

	private int cateID;

	private String foodName;

	private double price;

	private List<Integer> ingredients;

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

	public List<Integer> getIngredients() {
		return ingredients;
	}

	public void setIngredients(List<Integer> ingredients) {
		this.ingredients = ingredients;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public FoodForm(int cateID, String foodName, double price, List<Integer> ingredients, MultipartFile image) {
		super();
		this.cateID = cateID;
		this.foodName = foodName;
		this.price = price;
		this.ingredients = ingredients;
		this.image = image;
	}
	
	public FoodForm(Food food, List<Integer> ingredients) {
		this.cateID = food.getCateID();
		this.foodName = food.getFoodName();
		this.price = food.getPrice();
		this.ingredients = ingredients;
	}

	public FoodForm() {
		super();
	}

}
