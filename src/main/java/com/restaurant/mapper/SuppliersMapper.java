package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Suppliers;

public class SuppliersMapper implements RowMapper<Suppliers> {

	@Override
	public Suppliers mapRow(ResultSet rs, int rowNum) throws SQLException {
		Suppliers sup = new Suppliers();
		sup.setId(rs.getInt("id"));
		sup.setName(rs.getString("name"));
		sup.setDescr(rs.getString("descript"));
		sup.setAddress(rs.getString("address"));
		return sup;
	}
}
