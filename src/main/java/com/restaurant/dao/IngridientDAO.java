package com.restaurant.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.mapper.IngedientMapper;
import com.restaurant.mapper.Product_IngridientMapper;
import com.restaurant.model.Ingedients;
import com.restaurant.model.OrderDetail;
import com.restaurant.model.Product_Ingridient;

@Repository
@Transactional
public class IngridientDAO extends JdbcDaoSupport {

	@Autowired
	public IngridientDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	/**
	 * List of all ingridients
	 * 
	 * @return
	 */
	public List<Ingedients> allIngri() {
		List<Ingedients> list = new ArrayList<Ingedients>();

		String sql = "Select * from Ingedients";
		Object[] params = new Object[] {};
		IngedientMapper mapper = new IngedientMapper();
		try {
			list = this.getJdbcTemplate().query(sql, params, mapper);
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * List all ingridients need to make a dish
	 * 
	 * @param foodId
	 * @return
	 */
	public List<Ingedients> allIngriForFood(int foodId) {
		List<Ingedients> list = new ArrayList<Ingedients>();
		String sql = "Select * from Ingedients where id IN "
				+ "(SELECT ingreId from Product_Ingridient where foodId = ?)";
		Object[] params = new Object[] { foodId };
		IngedientMapper mapper = new IngedientMapper();
		try {
			list = this.getJdbcTemplate().query(sql, params, mapper);
			return list;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Use the get total quantity to get the total amount of 1 ingridient used
	 * 
	 * @param quantity
	 * @param id
	 * @return
	 */
	public String useIngridient(OrderDetail detail) {
		String sql = " Update Ingedients Set stockQuantity = stockQuantity - ? Where id = ?";
		List<Ingedients> list = this.allIngriForFood(detail.getFoodId());
		String message = "No problem";
		for (Ingedients ing : list) {
			int quantity = this.getTotalIngridientUsedForOrder(ing.getId(), detail);
			if (ing.getStockQuantity() < quantity) {
				message = "Not enough: " + ing.getName();
				System.out.println(message);
				break;
			} else {
				this.getJdbcTemplate().update(sql, quantity, ing.getId());
			}
		}
		return message;
	}

	/**
	 * Restock amount will always be the same amount
	 * 
	 * @param quantity
	 * @param id
	 */
	public void restock(int id) {
		String sql = " Update Ingedients Set stockQuantity = stockQuantity + 20 Where id = ?";
		this.getJdbcTemplate().update(sql, id);
	}

	/**
	 * Get the quantity of 1 ingridient used for a product in 1 order detail
	 * 
	 * @param id
	 * @param foodId
	 * @param productQuantity
	 * @return
	 */
	public int getTotalIngridientUsedForOrder(int id, OrderDetail detail) {
		int i = 0;
		String sql = "Select * from Product_Ingridient where ingreId = ? AND foodId = ?";
		Object[] params = new Object[] { id, detail.getFoodId() };
		Product_IngridientMapper mapper = new Product_IngridientMapper();
		Product_Ingridient pi = this.getJdbcTemplate().queryForObject(sql, params, mapper);
		i = pi.getQuantity() * detail.getQuantity();
		return i;
	}

	/**
	 * Return ingridients that were not used due to order being canceled back into
	 * stock
	 * 
	 * @param detail
	 */
	public void returnIngridient(OrderDetail detail) {
		String sql = " Update Ingedients Set stockQuantity = stockQuantity + ? Where id = ?";
		List<Ingedients> list = this.allIngriForFood(detail.getFoodId());
		for (Ingedients ing : list) {
			int quantity = this.getTotalIngridientUsedForOrder(ing.getId(), detail);
			this.getJdbcTemplate().update(sql, quantity, ing.getId());
		}
	}

	/**
	 * Get the return list from checkbox to convert into list of Ingedients
	 * 
	 * @param listOfId
	 * @return
	 */
	public List<Ingedients> convertIntoIngedients(List<Integer> listOfId) {
		List<Ingedients> list = this.allIngri();
		List<Ingedients> found = new ArrayList<>();
		for (Ingedients ing : list) {
			for (Integer i : listOfId) {
				if (ing.getId() == i) {
					found.add(ing);
				}
			}
		}
		return found;
	}

	public void updateRelation(int id, List<Integer> listOfId) {
		String sql = "Delete from Product_Ingridient where foodId =?";
		this.getJdbcTemplate().update(sql, id);
		for (Integer i : listOfId) {
			sql = "Insert into Product_Ingridient Values (?,?,?)";
			this.getJdbcTemplate().update(sql, id, i, 3);
		}
	}
}
