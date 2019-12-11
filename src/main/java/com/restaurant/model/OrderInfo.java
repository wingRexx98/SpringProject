package com.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {
	private int id;

	private String custName;

	private String email;

	private String phone;

	private String deliverAddress;

	private double totalPrice;

	private String orderStatus;

	private boolean enabled;

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	private List<OrderDetailInfo> details;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getDeliverAddress() {
		return deliverAddress;
	}

	public void setDeliverAddress(String deliverAddress) {
		this.deliverAddress = deliverAddress;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public List<OrderDetailInfo> getDetails() {
		return details;
	}

	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}

	public OrderInfo(int id, String custName, String email, String phone, String deliverAddress, double totalPrice,
			String orderStatus, boolean enabled) {
		super();
		this.id = id;
		this.custName = custName;
		this.email = email;
		this.phone = phone;
		this.deliverAddress = deliverAddress;
		this.totalPrice = totalPrice;
		this.orderStatus = orderStatus;
		this.enabled = enabled;
	}

	public OrderInfo() {
		super();
	}

	@Override
	public String toString() {
		String order = "Order number: " + this.getId() + "\nCustomer: " + this.getCustName() + "\nPhone  number: "
				+ this.getPhone() + "\nDeliver to this address: " + this.getDeliverAddress() + "\nItems: ";
		for (OrderDetailInfo detail : this.details) {
			order = order.concat(detail.toString());
		}
		order = order.concat("\nTotal: " + this.totalPrice);
		return order;
	}

}
