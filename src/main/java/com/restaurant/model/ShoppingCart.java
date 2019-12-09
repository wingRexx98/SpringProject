package com.restaurant.model;

import java.util.ArrayList;
import java.util.List;

public class ShoppingCart {

	private int orderid;

	private Customer customer;

	public ShoppingCart() {
		super();
	}

	public ShoppingCart(int orderid, Customer customer, List<ShoppingCartLine> cartLines) {
		super();
		this.orderid = orderid;
		this.customer = customer;
		this.cartLines = cartLines;
	}

	private List<ShoppingCartLine> cartLines = new ArrayList<ShoppingCartLine>();


	public int getOrderid() {
		return orderid;
	}

	public void setOrderid(int orderid) {
		this.orderid = orderid;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<ShoppingCartLine> getCartLines() {
		return cartLines;
	}

	public void setCartLines(List<ShoppingCartLine> cartLines) {
		this.cartLines = cartLines;
	}

	/**
	 * Find the cart line
	 * 
	 * @param id
	 * @return
	 */
	private ShoppingCartLine findLineById(int id) {
		for (ShoppingCartLine line : this.cartLines) {
			if (line.getFood().getId() == (id)) {
				return line;
			}
		}
		return null;
	}

	/**
	 * Add a product into a cart line, remove cart line if the quantity is 0
	 * 
	 * @param food
	 * @param quantity
	 */
	public void addProductIntoCart(FoodInfo food, int quantity) {
		ShoppingCartLine line = this.findLineById(food.getId());
		if (line == null) {
			line = new ShoppingCartLine();
			line.setQuantity(0);
			line.setFood(food);
			this.cartLines.add(line);
		}
		int newQuantity = line.getQuantity() + quantity;
		if (newQuantity <= 0) {
			this.cartLines.remove(line);
		} else {
			line.setQuantity(newQuantity);
		}
	}

	/**
	 * Update the quantity that is bought only
	 * 
	 * @param code
	 * @param quantity
	 */
	public void updateProduct(int code, int quantity) {
		ShoppingCartLine line = this.findLineById(code);

		if (line != null) {
			if (quantity <= 0) {
				this.cartLines.remove(line);
			} else {
				line.setQuantity(quantity);
			}
		}
	}

	/**
	 * Remove product from cart/remove cart line
	 * 
	 * @param productInfo
	 */
	public void removeProduct(FoodInfo productInfo) {
		ShoppingCartLine line = this.findLineById(productInfo.getId());
		if (line != null) {
			this.cartLines.remove(line);
		}
	}

	/**
	 * Check if the cart is empty
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.cartLines.isEmpty();
	}

	/**
	 * Valida customer on buying
	 * 
	 * @return
	 */
	public boolean isValidCustomer() {
		return this.customer != null;
	}

	/**
	 * get total quantity
	 * 
	 * @return
	 */
	public int getQuantityTotal() {
		int quantity = 0;
		for (ShoppingCartLine line : this.cartLines) {
			quantity += line.getQuantity();
		}
		return quantity;
	}

	/**
	 * get total price based on quantity
	 * 
	 * @return
	 */
	public double getAmountTotal() {
		double total = 0;
		for (ShoppingCartLine line : this.cartLines) {
			total += line.getAmount();
		}
		return total;
	}

	/**
	 * update the shopping cart by updating cart lines
	 * 
	 * @param cartForm
	 */
	public void updateQuantity(ShoppingCart cartForm) {
		if (cartForm != null) {
			List<ShoppingCartLine> lines = cartForm.getCartLines();
			for (ShoppingCartLine line : lines) {
				this.updateProduct(line.getFood().getId(), line.getQuantity());
			}
		}

	}

}
