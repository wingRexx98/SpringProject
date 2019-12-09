package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Category;

public class CategoryMapper implements RowMapper<Category> {

	@Override
	public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt("id");
		String name = rs.getString("cateName");
		String description = rs.getString("cateDescript");
		boolean enabled  = rs.getBoolean("enabled");
		return new Category(id, name, description, enabled);
	}

}
