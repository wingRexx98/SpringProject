package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.User_Role;

public class UserRoleMapper implements RowMapper<User_Role> {

	@Override
	public User_Role mapRow(ResultSet rs, int rowNum) throws SQLException {
		User_Role role = new User_Role();
		role.setId(rs.getInt("id"));
		role.setRoleName(rs.getString("roleName"));
		role.setDescript(rs.getString("descript"));
		return role;
	}

}
