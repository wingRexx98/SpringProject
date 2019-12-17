package com.restaurant.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.model.Food;
import com.restaurant.model.FoodInfo;
import com.restaurant.form.FoodForm;
import com.restaurant.mapper.FoodMapper;
import com.restaurant.util.SQLCommands;

@Repository
@Transactional
public class FoodDAO extends JdbcDaoSupport {

	Food food = null;

	@Autowired
	public FoodDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	@Transactional(rollbackFor = Exception.class)
	public int addFood(FoodForm foodForm) throws IOException, DataAccessException {
		int status;
		List<Food> foods = new ArrayList<Food>();
		Food food = new Food();
		food.setCateID(foodForm.getCateID());
		food.setFoodName(foodForm.getFoodName());
		food.setPrice(foodForm.getPrice());
		food.setIngredients(foodForm.getIngredients());

		String sql = SQLCommands.ALL_FOOD;
		Object[] params = new Object[] {};
		FoodMapper mapper = new FoodMapper();
		if (foodForm.getImage() != null) {
			// images
			byte[] image = foodForm.getImage().getBytes();
			if (image != null && image.length > 0) {
				food.setImage(image);
			}
		} else {
			food.setImage(null);
		}
		foods = this.getJdbcTemplate().query(sql, params, mapper);
		for (Food rest : foods) {
			if (rest.getFoodName().equals(food.getFoodName())) {
				return status = 0;
			}
		}
		sql = SQLCommands.ADD_FOOD;
		status = this.getJdbcTemplate().update(sql, food.getCateID(), food.getFoodName(), food.getPrice(),
				food.getIngredients(), food.getImage());
		return status;
	}

	@Transactional(rollbackFor = Exception.class)
	public int updateFood(FoodForm foodForm, int id) throws DataAccessException, IOException {
		int i = 0;
		List<Food> foods = new ArrayList<Food>();
		Food food = new Food();
		food.setCateID(foodForm.getCateID());
		food.setFoodName(foodForm.getFoodName());
		food.setPrice(foodForm.getPrice());
		food.setIngredients(foodForm.getIngredients());

		String sql = SQLCommands.ALL_FOOD;
		Object[] params = new Object[] {};
		FoodMapper mapper = new FoodMapper();
		if (foodForm.getImage() != null) {
			// images
			byte[] image = foodForm.getImage().getBytes();
			if (image != null && image.length > 0) {
				food.setImage(image);
			}
		} else {
			food.setImage(null);
		}
		foods = this.getJdbcTemplate().query(sql, params, mapper);
		for (Food rest : foods) {
			if (rest.getFoodName().equals(food.getFoodName()) && rest.getId() != id) {
				return i = 0;
			}
		}
		if (food.getImage() == null) {
			sql = SQLCommands.UPDATE_FOOD_WITHOUT_IMAGE;
			i = this.getJdbcTemplate().update(sql, food.getCateID(), food.getFoodName(), food.getPrice(),
					food.getIngredients(), id);
		} else {
			sql = SQLCommands.UPDATE_FOOD_WITH_IMAGE;
			i = this.getJdbcTemplate().update(sql, food.getCateID(), food.getFoodName(), food.getPrice(),
					food.getIngredients(), food.getImage(), id);
		}
		return i;
	}

	@Transactional(rollbackFor = Exception.class)
	public int removeFood(int id) throws DataAccessException {
		int status;
		String sql = SQLCommands.REMOVE_FOOD;
		status = this.getJdbcTemplate().update(sql, id);

		return status;
	}

	public List<Food> getFood() {
		List<Food> foods = new ArrayList<Food>();

		String sql = SQLCommands.ALL_FOOD;
		Object[] params = new Object[] {};
		FoodMapper mapper = new FoodMapper();
		try {
			foods = this.getJdbcTemplate().query(sql, params, mapper);
			return foods;
		} catch (Exception e) {
			return null;
		}
	}

	public Food findFood(int id) {
		Food food = new Food();

		String sql = SQLCommands.FIND_FOOD;
		Object[] params = new Object[] { id };
		FoodMapper mapper = new FoodMapper();
		try {
			food = this.getJdbcTemplate().queryForObject(sql, params, mapper);
			return food;
		} catch (Exception e) {
			return null;
		}
	}

	public FoodInfo findFoodInfo(int id) {
		Food food = this.findFood(id);
		if (food != null) {
			FoodInfo info = new FoodInfo(food.getId(), food.getFoodName(), food.getPrice());
			return info;
		}
		return null;
	}

	@Transactional
	public List<Food> pageRecords(int pageIndex, int pageSize) {
		List<Food> foods = new ArrayList<Food>();
		String sql = SQLCommands.PAGING;
		Object[] params = new Object[] { pageIndex, pageSize };
		FoodMapper mapper = new FoodMapper();
		try {
			foods = this.getJdbcTemplate().query(sql, params, mapper);
			return foods;
		} catch (Exception e) {
			return null;
		}
	}
}
