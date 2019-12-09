package com.restaurant.model;

import java.io.Serializable;


public class OrderDetail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int orderId;

	private int foodId;

	private int quantity;

	private double quantityPrice;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public int getFoodId() {
		return foodId;
	}

	public void setFoodId(int foodId) {
		this.foodId = foodId;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getQuantityPrice() {
		return quantityPrice;
	}

	public void setQuantityPrice(double quantityPrice) {
		this.quantityPrice = quantityPrice;
	}

	public OrderDetail(int orderId, int foodId, int quanity, double quantityPrice) {
		super();
		this.orderId = orderId;
		this.foodId = foodId;
		this.quantity = quanity;
		this.quantityPrice = quantityPrice;
	}

	public OrderDetail() {
		
	}

}
