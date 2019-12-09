package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Food;

public class FoodMapper implements RowMapper<Food> {

	@Override
	public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("id");

		int cateID = rs.getInt("cateId");

		String foodName = rs.getString("foodName");

		double price = rs.getDouble("price");

		String ingredients = rs.getString("ingredients");

		byte[] image = rs.getBytes("image");

		boolean enabled = rs.getBoolean("enabled");

		return new Food(id, cateID, foodName, price, ingredients, image, enabled);

	}

}
