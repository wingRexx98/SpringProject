package com.restaurant.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.mapper.UserRoleMapper;
import com.restaurant.model.User_Role;

@Repository
@Transactional
public class UserRoleDAO extends JdbcDaoSupport {

	@Autowired
	public UserRoleDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public List<String> getRole(int id) {
		String sql = "Select roleName FROM User_Role ur, User_Login ul WHERE ur.id = ul.roleId AND ul.empId = ?";
		Object[] params = new Object[] { id };

		List<String> list = this.getJdbcTemplate().queryForList(sql, params, String.class);
		return list;
	}

	public List<User_Role> allRole() {
		String sql = "Select * FROM User_Role";
		Object[] params = new Object[] {};
		UserRoleMapper mapper = new UserRoleMapper();

		List<User_Role> list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}
}
