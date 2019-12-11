package com.restaurant.util;

public class SQLCommands {
	// Restaurant
	public static final String ALL_RESTAURANT = "SELECT * FROM Restaurant WHERE isOpen = 1";

	public static final String ADD_RESTAURANT = "INSERT INTO Restaurant(restName, location, isOpen) VALUES (?,?,1)";

	public static final String UPDATE_RESTAURANT = "UPDATE Restaurant SET restName = ?, location = ? WHERE id = ?";

	public static final String REMOVE_RESTAURANT = "UPDATE Restaurant SET isOpen = 0 WHERE id = ?";

	// Food/Product
	public static final String ALL_FOOD = "SELECT * FROM Food WHERE enabled = 1";
	
	public static final String FIND_FOOD = "SELECT * FROM Food WHERE id = ? AND enabled = 1";
	

	public static final String ADD_FOOD = "INSERT INTO Food(cateId, foodName, price, ingredients, image, enabled) VALUES (?, ?, ?, ?, ?, 1)";

	public static final String UPDATE_FOOD_WITH_IMAGE = "UPDATE Food SET cateId = ?,foodName = ?,price = ?,ingredients = ?, image =? WHERE id =?";
	
	public static final String UPDATE_FOOD_WITHOUT_IMAGE = "UPDATE Food SET cateId = ?,foodName = ?,price = ?,ingredients = ? WHERE id =?";

	public static final String REMOVE_FOOD = "UPDATE Food SET enabled = 0 WHERE id = ?";
	
	public static final String CALL_FOOD_PROC = "{CALL PROCEDURE pageDevider()}";
	
	public static final String PAGING = "{call pageDeviderResult(?,?)}";
	
	//Category
	public static final String ALL_CATE = "SELECT * FROM Category WHERE enabled = 1";

}
