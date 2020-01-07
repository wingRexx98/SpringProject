package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Ingedients;

public class IngedientMapper implements RowMapper<Ingedients>{

	@Override
	public Ingedients mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		Ingedients ingre = new Ingedients();
		ingre.setId(rs.getInt("id"));
		ingre.setName(rs.getString("name"));
		ingre.setStockQuantity(rs.getInt("stockQuantity"));
		ingre.setPrice(rs.getDouble("price"));
		ingre.setLastReStockDate(String.valueOf(rs.getDate("lastReStockDate")));
		ingre.setImage(rs.getBytes("image"));
		return ingre;
	}
}
