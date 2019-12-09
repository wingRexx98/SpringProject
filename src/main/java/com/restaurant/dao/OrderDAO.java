package com.restaurant.dao;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.mapper.OrderDetailMapper;
import com.restaurant.mapper.OrderMapper;
import com.restaurant.model.Customer;
import com.restaurant.model.Food;
import com.restaurant.model.FoodInfo;
import com.restaurant.model.Order;
import com.restaurant.model.OrderDetail;
import com.restaurant.model.OrderDetailInfo;
import com.restaurant.model.OrderInfo;
import com.restaurant.model.ShoppingCart;
import com.restaurant.model.ShoppingCartLine;

@Transactional
@Repository
public class OrderDAO extends JdbcDaoSupport {

	@Autowired
	private FoodDAO foodDAO;

	@Autowired
	public OrderDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	/**
	 * Save the new customer first then save the new order then save the order
	 * details
	 * 
	 * @param cart
	 * @return
	 * @throws DataAccessException
	 */
	@Transactional
	public int saveOrder(ShoppingCart cart) throws DataAccessException {
		int status = 0;
		int newOrderId = getMaxOrderId() + 1;
		Customer customer = cart.getCustomer();
		Order order = new Order();

		order.setId(newOrderId);
		order.setCustName(customer.getCustName());
		order.setEmail(customer.getEmail());
		order.setPhone(customer.getPhone());
		order.setDeliverAddress(customer.getAddress());
		order.setTotalPrice(cart.getAmountTotal());
		order.setOrderStatus("Not done");

		String sql = "Insert into Orders VALUES (?, ?, ?, ?, ?, ?, ?, 1)";
		status = this.getJdbcTemplate().update(sql, order.getId(), order.getCustName(), order.getEmail(),
				order.getPhone(), order.getDeliverAddress(), order.getTotalPrice(), order.getOrderStatus());

		List<ShoppingCartLine> lines = cart.getCartLines();
		sql = "Insert into order_detail VALUES (?, ?, ?, ?)";
		for (ShoppingCartLine line : lines) {
			OrderDetail detail = new OrderDetail();
			detail.setOrderId(newOrderId);
			detail.setQuantity(line.getQuantity());
			detail.setQuantityPrice(line.getAmount());
			FoodInfo food = line.getFood();
			detail.setFoodId(food.getId());

			this.getJdbcTemplate().update(sql, detail.getOrderId(), detail.getFoodId(), detail.getQuantity(),
					detail.getQuantityPrice());
		}

		return status;
	}

	/**
	 * get the highest value order id
	 * 
	 * @return
	 */
	public int getMaxOrderId() {
		int maxId = 0;
		List<Order> list = listOfOrder();
		if (list != null && !list.isEmpty()) {
			for (Order o : list) {
				if (maxId < o.getId()) {
					maxId = o.getId();
				}
			}
		}
		return maxId;

	}

	/**
	 * get all the order
	 * 
	 * @return
	 */
	public List<Order> listOfOrder() {
		String sql = "SELECT * FROM Orders";
		Object[] params = new Object[] {};
		OrderMapper mapper = new OrderMapper();
		List<Order> list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	public int updateOrderStatus(OrderInfo order, int id) {
		int check = 0;
		String sql = "UPDATE Orders SET orderStatus = ? WHERE id = ?";
		check = this.getJdbcTemplate().update(sql, order.getOrderStatus(), id);
		return check;
	}

	public Order findOrder(int id) {
		Order order = null;
		List<Order> list = listOfOrder();
		if (list != null && !list.isEmpty()) {
			for (Order o : list) {
				if (id == o.getId()) {
					order = o;
				}
			}
		}
		return order;
	}

	public OrderInfo getOrderInfo(int id) {
		Order order = this.findOrder(id);
		if (order != null) {
			return new OrderInfo(order.getId(), order.getCustName(), order.getEmail(), order.getPhone(),
					order.getDeliverAddress(), order.getTotalPrice(), order.getOrderStatus(), order.isEnabled());
		}
		return null;
	}

	public List<OrderDetailInfo> detailInfos(int orderId) {
		String sql = "SELECT * FROM order_detail WHERE orderId = ?";
		Object[] params = new Object[] { orderId };
		OrderDetailMapper mapper = new OrderDetailMapper();
		List<OrderDetail> list = this.getJdbcTemplate().query(sql, params, mapper);
		List<OrderDetailInfo> infoList = new ArrayList<>();
		for (OrderDetail d : list) {
			Food food = foodDAO.findFood(d.getFoodId());
			OrderDetailInfo info = new OrderDetailInfo(d.getFoodId(), food.getFoodName(), d.getQuantity(),
					d.getQuantityPrice(), food.getImage());
			infoList.add(info);
		}
		return infoList;
	}

	public List<Order> pagingOrders(int pageIndex, int pageSize) {
		List<Order> list = new ArrayList<>();
		String sql = "{call pageOrderDevider(?,?)}";
		Object[] params = new Object[] { pageIndex, pageSize };
		OrderMapper mapper = new OrderMapper();
		try {
			list = this.getJdbcTemplate().query(sql, params, mapper);
			return list;
		} catch (Exception e) {
			return null;
		}
	}
	
	public void removeOrder(int id) {
		String sql = "UPDATE Orders SET enabled = 0 where id = ?";
		this.getJdbcTemplate().update(sql, id);
	}

}
