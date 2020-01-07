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
import com.restaurant.model.Product_Ingridient;

@Repository
@Transactional
public class IngridientDAO extends JdbcDaoSupport {

	@Autowired
	public IngridientDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

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
	public int useIngridient(int quantity, int id) {
		String sql = " Update Ingedients Set stockQuantity = stockQuantity - ? Where id = ?";
		int status = this.getJdbcTemplate().update(sql, quantity, id);
		return status;
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

	public void reUseIngridient(int quantity, int id) {
		List<Ingedients> list = new ArrayList<>();

		String sql = "Select * from Ingedients where id IN "
				+ "( Select ingreId from Product_Ingridient where foodId IN "
				+ "(Select foodId from OrderDetail where orderId IN "
				+ "( Select id from Orders where orderStatus = 'Cancel')))";
		Object[] params = new Object[] {};
		IngedientMapper mapper = new IngedientMapper();
		list = this.getJdbcTemplate().query(sql, params, mapper);

	}

	/**
	 * Get the quantity of 1 ingridient used for a product in 1 order detail
	 * 
	 * @param id
	 * @param foodId
	 * @param productQuantity
	 * @return
	 */
	public int getTotalIngridientUsedForOrder(int id, int foodId, int productQuantity) {
		int i = 0;
		String sql = "Select * from Product_Ingridient where ingreId = ? AND foodId = ?";
		Object[] params = new Object[] { id, foodId };
		Product_IngridientMapper mapper = new Product_IngridientMapper();
		Product_Ingridient pi = this.getJdbcTemplate().queryForObject(sql, params, mapper);
		i = pi.getQuantity() * productQuantity;
		return i;
	}

}
