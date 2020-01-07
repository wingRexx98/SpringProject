package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;
import com.restaurant.model.Product_Ingridient;

public class Product_IngridientMapper implements RowMapper<Product_Ingridient> {

	@Override
	public Product_Ingridient mapRow(ResultSet rs, int rowNum) throws SQLException {
		Product_Ingridient map = new Product_Ingridient();

		map.setFoodId(rs.getInt("foodId"));
		map.setIngridientId(rs.getInt("ingreId"));
		map.setQuantity(rs.getInt("quantity"));
		return map;
	}

}
