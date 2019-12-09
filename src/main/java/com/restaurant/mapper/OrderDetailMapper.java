package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.OrderDetail;

public class OrderDetailMapper implements RowMapper<OrderDetail> {

	@Override
	public OrderDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		OrderDetail detail = new OrderDetail();
		detail.setOrderId(rs.getInt("orderId"));
		detail.setFoodId(rs.getInt("foodId"));
		detail.setQuantity(rs.getInt("quantity"));
		detail.setQuantityPrice(rs.getDouble("quantityPrice"));
		return detail;
	}


}
