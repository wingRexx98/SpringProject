package com.restaurant.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.mapper.CategoryMapper;
import com.restaurant.model.Category;
import com.restaurant.util.SQLCommands;

@Repository
@Transactional
public class CategoryDAO extends JdbcDaoSupport {

	Category category = null;
	
	@Autowired
	public CategoryDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	public List<Category> getCategory() throws DataAccessException {
		List<Category> cates = new ArrayList<Category>();

		String sql = SQLCommands.ALL_CATE;
		Object[] params = new Object[] {};
		CategoryMapper mapper = new CategoryMapper();
		cates = this.getJdbcTemplate().query(sql, params, mapper);
		return cates;
	}
	
	public Category findCategory(int id) throws DataAccessException {
		Category cate = null;
		String sql = SQLCommands.ALL_CATE + "WHERE id = ?";
		Object[] params = new Object[] {id};
		CategoryMapper mapper = new CategoryMapper();
		cate = this.getJdbcTemplate().queryForObject(sql, params, mapper);
		return cate;
	}

}
