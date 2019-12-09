package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.UserLogin;

public class UserLoginMapper implements RowMapper<UserLogin>{

	@Override
	public UserLogin mapRow(ResultSet rs, int rowNum) throws SQLException {
		UserLogin login = new UserLogin();
		login.setEmpId(rs.getInt("empId"));
		login.setEncryptedPassword(rs.getString("password"));
		login.setUserName(rs.getString("userName"));
		login.setRoleId(rs.getInt("roleId"));
		return login;
	}

}
