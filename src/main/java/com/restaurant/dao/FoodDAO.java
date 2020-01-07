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
import com.restaurant.model.Ingedients;
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
	
	@Autowired
	public IngridientDAO ingDAO;

	/**
	 * validate food info
	 * 
	 * @param food
	 * @return
	 */
	public boolean validFood(Food food) {
		List<Food> foods = new ArrayList<Food>();

		String sql = SQLCommands.ALL_FOOD;
		Object[] params = new Object[] {};
		FoodMapper mapper = new FoodMapper();

		foods = this.getJdbcTemplate().query(sql, params, mapper);
		for (Food rest : foods) {
			if (rest.getFoodName().equals(food.getFoodName())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Transfer the input form info into the Food info
	 * 
	 * @param foodForm
	 * @return
	 * @throws IOException
	 */
	public Food toFood(FoodForm foodForm) throws IOException {
		Food food = new Food();

		food.setCateID(foodForm.getCateID());
		food.setFoodName(foodForm.getFoodName());
		food.setPrice(foodForm.getPrice());

		if (foodForm.getImage() != null) {
			// images
			byte[] image = foodForm.getImage().getBytes();
			if (image != null && image.length > 0) {
				food.setImage(image);
			}
		} else {
			food.setImage(null);
		}

		return food;
	}

	/**
	 * Add new food into DB
	 * 
	 * @param foodForm
	 * @return
	 * @throws IOException
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public int addFood(FoodForm foodForm) throws IOException, DataAccessException {
		int status;

		Food food = this.toFood(foodForm);

		if (!validFood(food)) {
			return status = 0;
		}

		String sql = SQLCommands.ADD_FOOD;
		status = this.getJdbcTemplate().update(sql, food.getCateID(), food.getFoodName(), food.getPrice(),
				food.getImage());
		return status;
	}

	/**
	 * update food
	 * 
	 * @param foodForm
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * @throws IOException
	 */
	@Transactional(rollbackFor = Exception.class)
	public int updateFood(FoodForm foodForm, int id) throws DataAccessException, IOException {
		int i = 0;
		Food food = this.toFood(foodForm);

		if (!validFood(food)) {
			return i = 0;
		}
		if (food.getImage() == null) {
			String sql = SQLCommands.UPDATE_FOOD_WITHOUT_IMAGE;
			i = this.getJdbcTemplate().update(sql, food.getCateID(), food.getFoodName(), food.getPrice(), id);
		} else {
			String sql = SQLCommands.UPDATE_FOOD_WITH_IMAGE;
			i = this.getJdbcTemplate().update(sql, food.getCateID(), food.getFoodName(), food.getPrice(),
					food.getImage(), id);
		}
		return i;
	}

	/**
	 * disable food
	 * 
	 * @param id
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional(rollbackFor = Exception.class)
	public int removeFood(int id) throws DataAccessException {
		int status;
		String sql = SQLCommands.REMOVE_FOOD;
		status = this.getJdbcTemplate().update(sql, id);

		return status;
	}

	/**
	 * List food
	 * 
	 * @return
	 */
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

	/**
	 * Search food
	 * 
	 * @param id
	 * @return
	 */
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

	/**
	 * from food to food info
	 * 
	 * @param id
	 * @return
	 */
	public FoodInfo findFoodInfo(int id) {
		Food food = this.findFood(id);
		if (food != null) {
			List<Ingedients> list = ingDAO.allIngriForFood(id);
			FoodInfo info = new FoodInfo(food.getId(), food.getFoodName(), food.getPrice(), list);
			return info;
		}
		return null;
	}

	/**
	 * Paging list
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
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
