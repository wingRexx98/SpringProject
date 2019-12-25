package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Order;

public class OrderMapper implements RowMapper<Order> {

	@Override
	public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
		Order order = new Order();
		order.setId(rs.getInt("id"));
		order.setCustName(rs.getString("custName"));
		order.setEmail(rs.getString("email"));
		order.setPhone(rs.getString("phone"));
		order.setDeliverAddress(rs.getString("deliverAddress"));
		order.setOrderStatus(rs.getString("orderStatus"));
		order.setTotalPrice(rs.getDouble("totalPrice"));
		order.setConCode(rs.getString("conCode"));
		order.setConfirmed(rs.getBoolean("confirmed"));
		order.setEnabled(rs.getBoolean("enabled"));

		return order;
	}

}
