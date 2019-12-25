package com.restaurant.model;

import java.io.Serializable;
//import java.util.Date;

public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String custName;

	private String email;

	private String phone;

	private String deliverAddress;

	private double totalPrice;

	private String orderStatus;

	private String conCode;

	private boolean confirmed;

	private boolean enabled;

	public String getConCode() {
		return conCode;
	}

	public void setConCode(String conCode) {
		this.conCode = conCode;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public Order() {
		super();
	}

	public Order(int id, String custName, String email, String phone, String deliverAddress, double totalPrice,
			String orderStatus, String conCode, boolean confirmed, boolean enabled) {
		super();
		this.id = id;
		this.custName = custName;
		this.email = email;
		this.phone = phone;
		this.deliverAddress = deliverAddress;
		this.totalPrice = totalPrice;
		this.orderStatus = orderStatus;
		this.conCode = conCode;
		this.confirmed = confirmed;
		this.enabled = enabled;
	}

}
