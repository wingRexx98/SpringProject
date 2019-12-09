package com.restaurant.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.form.EmpForm;
import com.restaurant.mapper.UserLoginMapper;
import com.restaurant.model.UserLogin;

@Repository
@Transactional
public class UserLoginDAO extends JdbcDaoSupport {

	@Autowired
	public UserLoginDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public UserLogin findAccount(String username) {
		String sql = "Select * from User_Login Where userName = ?";
		Object[] params = new Object[] { username };
		UserLoginMapper mapper = new UserLoginMapper();
		try {
			UserLogin user = this.getJdbcTemplate().queryForObject(sql, params, mapper);
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public UserLogin findAccountByEmp(int id) {
		String sql = "Select * from User_Login Where empid = ?";
		Object[] params = new Object[] { id };
		UserLoginMapper mapper = new UserLoginMapper();
		try {
			UserLogin user = this.getJdbcTemplate().queryForObject(sql, params, mapper);
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public int updateAccountByEmp(EmpForm form, int id) {
		int check = 0;
		UserLogin loginInfo = new UserLogin();
		loginInfo.setUserName(form.getUserName());
		loginInfo.setEncryptedPassword(passwordEncoder(form.getEncryptedPassword()));
		String sql = "UPDATE User_Login SET username = ?, password = ? Where empid = ?";
		check = this.getJdbcTemplate().update(sql, loginInfo.getUserName(), loginInfo.getEncryptedPassword(), id);
		return check;
	}

	public String passwordEncoder(String password) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(password);
	}
}
