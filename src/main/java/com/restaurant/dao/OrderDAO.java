package com.restaurant.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
	 * Convert cart info into order info
	 * 
	 * @param cart
	 * @return
	 */
	public Order toOrder(ShoppingCart cart) {
		Order order = new Order();

		Customer customer = cart.getCustomer();

		int newOrderId = getMaxOrderId() + 1;
		order.setId(newOrderId);
		order.setCustName(customer.getCustName());
		order.setEmail(customer.getEmail());
		order.setPhone(customer.getPhone());
		order.setDeliverAddress(customer.getAddress());
		order.setTotalPrice(cart.getAmountTotal());
		order.setOrderStatus("Not done");
		order.setConCode(this.validationCodeGenerator());

		return order;
	}

	/**
	 * conver cart lin into order detail
	 * 
	 * @param line
	 * @param newOrderId
	 * @return
	 */
	public OrderDetail toDetail(ShoppingCartLine line, int newOrderId) {
		OrderDetail detail = new OrderDetail();

		detail.setOrderId(newOrderId);
		detail.setQuantity(line.getQuantity());
		detail.setQuantityPrice(line.getAmount());
		FoodInfo food = line.getFood();
		detail.setFoodId(food.getId());

		return detail;
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
		Order order = this.toOrder(cart);

		String sql = "Insert into Orders VALUES (?, ?, ?, ?, ?, ?, ?, 1, ?,0)";

		status = this.getJdbcTemplate().update(sql, order.getId(), order.getCustName(), order.getEmail(),
				order.getPhone(), order.getDeliverAddress(), order.getTotalPrice(), order.getOrderStatus(),
				order.getConCode());

		List<ShoppingCartLine> lines = cart.getCartLines();
		sql = "Insert into order_detail VALUES (?, ?, ?, ?)";

		for (ShoppingCartLine line : lines) {

			OrderDetail detail = this.toDetail(line, order.getId());

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

	// update order status
	public int updateOrderStatus(OrderInfo order, int id) {
		int check = 0;
		String sql = "UPDATE Orders SET orderStatus = ? WHERE id = ?";
		check = this.getJdbcTemplate().update(sql, order.getOrderStatus(), id);
		return check;
	}

	// find order by id
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

	// get specific order info
	public OrderInfo getOrderInfo(int id) {
		Order order = this.findOrder(id);
		if (order != null) {
			return new OrderInfo(order.getId(), order.getCustName(), order.getEmail(), order.getPhone(),
					order.getDeliverAddress(), order.getTotalPrice(), order.getOrderStatus(), order.isEnabled(),
					order.getConCode(), order.isConfirmed());
		}
		return null;
	}

	// get specific detail infos
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

	// Pagin the orders
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

	// disable order
	public void removeOrder(int id) {
		String sql = "UPDATE Orders SET enabled = 0 where id = ?";
		this.getJdbcTemplate().update(sql, id);
	}

	// convert order into order info
	public OrderInfo toOrderInfo(ShoppingCart cart) {
		OrderInfo info = new OrderInfo();
		info.setId(this.getMaxOrderId());
		info.setCustName(cart.getCustomer().getCustName());
		info.setDeliverAddress(cart.getCustomer().getAddress());
		info.setEmail(cart.getCustomer().getEmail());
		info.setPhone(cart.getCustomer().getPhone());

		List<OrderDetailInfo> details = new ArrayList<>();
		for (ShoppingCartLine line : cart.getCartLines()) {
			OrderDetailInfo detail = new OrderDetailInfo();
			detail.setFoodId(line.getFood().getId());
			detail.setFoodName(line.getFood().getFoodName());
			detail.setQuanity(line.getQuantity());
			detail.setQuantityPrice(line.getAmount());
			details.add(detail);
		}

		info.setDetails(details);

		info.setTotalPrice(cart.getAmountTotal());
		return info;
	}

	// generate random number
	public int randomNumbers() {
		Random random = new Random();
		int rand = 0;
		while (true) {
			rand = random.nextInt(10);
			if (rand != 0)
				break;
		}
		return rand;
	}

	// generate validation code
	public String validationCodeGenerator() {
		String code = "";
		for (int i = 0; i < 6; i++) {
			int random = randomNumbers();
			code += random;
		}
		return code;
	}

	/**
	 * Confirm validation code to complete order
	 * 
	 * @param conCode
	 * @return
	 */
	public String confirmPurchase(String conCode) {
		int id = this.getMaxOrderId();
		Order order = findOrder(id);
		String message = "";
		if (conCode.equals(order.getConCode())) {
			String sql = "UPDATE Orders SET confirmed = 1 WHERE id = ?";
			this.getJdbcTemplate().update(sql, id);
			message = "Confirmed completed";
		} else
			message = "Incorrect confirmation code";
		return message;
	}

	/**
	 * Customer cancel order
	 * 
	 * @return
	 */
	public int cancelOrder() {
		int id = this.getMaxOrderId();
		String sql = "UPDATE Orders SET enabled = 0, orderStatus = 'Cancel' WHERE id = ?";
		int i = this.getJdbcTemplate().update(sql, id);
		return i;
	}

}
