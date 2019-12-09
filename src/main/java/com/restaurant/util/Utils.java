package com.restaurant.util;

import javax.servlet.http.HttpServletRequest;

import com.restaurant.model.ShoppingCart;

//import com.restaurant.entities.ShoppingCart;

public class Utils {

	// Products info int the cart store in session
	public static ShoppingCart getCartInSession(HttpServletRequest request) {

		ShoppingCart cartInfo = (ShoppingCart) request.getSession().getAttribute("myCart");

		if (cartInfo == null) {
			cartInfo = new ShoppingCart();

			request.getSession().setAttribute("myCart", cartInfo);
		}

		return cartInfo;
	}

	public static void removeCartInSession(HttpServletRequest request) {
		request.getSession().removeAttribute("myCart");
	}

	public static void storeLastOrderedCartInSession(HttpServletRequest request, ShoppingCart cartInfo) {
		request.getSession().setAttribute("lastOrderedCart", cartInfo);
	}

	public static ShoppingCart getLastOrderedCartInSession(HttpServletRequest request) {
		return (ShoppingCart) request.getSession().getAttribute("lastOrderedCart");
	}
}
