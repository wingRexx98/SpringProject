package com.restaurant.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.restaurant.dao.FoodDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.form.CustomerForm;
import com.restaurant.model.Customer;
import com.restaurant.model.Food;
import com.restaurant.model.FoodInfo;
import com.restaurant.model.Order;
import com.restaurant.model.OrderInfo;
import com.restaurant.model.ShoppingCart;
import com.restaurant.util.Utils;

@Controller
public class BaseController {
	@Autowired
	private FoodDAO foodDAO;

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	public JavaMailSender emailSender;

	@RequestMapping("/403")
	public String accessDenied(Principal principal) {
		return "/403";
	}

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	////////////////////////////////// Shopping cart

	/**
	 * Add the product into the cart
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping({ "/buyProduct" })
	public String listProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "id", defaultValue = "") int id) {

		Food product = null;
		if (id != 0) {
			product = foodDAO.findFood(id);
		}
		if (product != null) {

			FoodInfo info = new FoodInfo(product);
			ShoppingCart cartInfo = Utils.getCartInSession(request);

			cartInfo.addProductIntoCart(info, 1);
		}

		return "redirect:/shoppingCart";
	}

	/**
	 * Remove product from cart
	 * 
	 * @param request
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping({ "/shoppingCartRemoveProduct" })
	public String removeProductHandler(HttpServletRequest request, Model model, //
			@RequestParam(value = "id", defaultValue = "") int id) {
		Food product = null;
		if (id != 0) {
			product = foodDAO.findFood(id);
		}
		if (product != null) {
			FoodInfo info = new FoodInfo(product);
			ShoppingCart cartInfo = Utils.getCartInSession(request);

			cartInfo.removeProduct(info);

		}

		return "redirect:/shoppingCart";
	}

	/**
	 * Update the quantity of a product in a order
	 * 
	 * @param request
	 * @param model
	 * @param cartForm
	 * @return
	 */
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
	public String shoppingCartUpdateQty(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("cartForm") ShoppingCart cartForm) {

		ShoppingCart cartInfo = Utils.getCartInSession(request);
		cartInfo.updateQuantity(cartForm);

		return "redirect:/shoppingCart";
	}

	/**
	 * Handle the cart by storing it into the session
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		ShoppingCart myCart = Utils.getCartInSession(request);

		model.addAttribute("cartForm", myCart);
		return "shoppingCart";
	}

	/**
	 * Input customer info
	 */
	@RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
	public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {

		ShoppingCart cartInfo = Utils.getCartInSession(request);

		if (cartInfo.isEmpty()) {

			return "redirect:/shoppingCart";
		}
		Customer customer = cartInfo.getCustomer();

		CustomerForm customerForm = new CustomerForm();
		if (customer != null) {
			customerForm.setCustName(customer.getCustName());
			customerForm.setEmail(customer.getEmail());
			customerForm.setPhone(customer.getPhone());
			customerForm.setAddress(customer.getAddress());
		}
		model.addAttribute("customerForm", customerForm);

		return "shoppingCartCustomer";
	}

	/**
	 * Save the input customer info
	 * 
	 * @param request
	 * @param model
	 * @param customerForm
	 * @param result
	 * @param redirectAttributes
	 * @return
	 */
	@RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
	public String shoppingCartCustomerSave(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("customerForm") CustomerForm customerForm, BindingResult result, //
			final RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			customerForm.setValid(false);
			return "shoppingCartCustomer";
		}
		customerForm.setValid(true);
		ShoppingCart cartInfo = Utils.getCartInSession(request);
		Customer customer = new Customer();
		if (customerForm != null) {
			customer.setCustName(customerForm.getCustName());
			customer.setEmail(customerForm.getEmail());
			customer.setPhone(customerForm.getPhone());
			customer.setAddress(customerForm.getAddress());
		}
		cartInfo.setCustomer(customer);
		model.addAttribute("customer", customer);

		return "redirect:/shoppingCartConfirmation";
	}

	/**
	 * Confirm form
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		ShoppingCart cartInfo = Utils.getCartInSession(request);
		// if the cart has no items
		if (cartInfo == null || cartInfo.isEmpty()) {
			// go back to cart to add items
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {
			// the customer is not valid
			return "redirect:/shoppingCartCustomer";
		}
		model.addAttribute("myCart", cartInfo);

		return "shoppingCartConfirmation";
	}

	/**
	 * Complete the purchase
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/completeConfirmation" }, method = RequestMethod.POST)

	public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
		ShoppingCart cartInfo = Utils.getCartInSession(request);

		if (cartInfo == null || cartInfo.isEmpty()) {

			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustomer()) {

			return "redirect:/shoppingCartCustomer";
		}
		try {
			orderDAO.saveOrder(cartInfo);
			sendSimpleEmail(orderDAO.toOrderInfo(cartInfo));
		} catch (Exception e) {

			return "shoppingCartConfirmation";
		}

		return "redirect:/confirmPurchaseForm";
	}

	/**
	 * Confirm the order
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/confirmPurchaseForm" }, method = RequestMethod.GET)
	public String completePurChaseForm(Model model) {

		return "confirmPurchase";
	}

	/**
	 * Confirm code validate
	 * 
	 * @param request
	 * @param model
	 * @param conCode
	 * @return
	 */
	@RequestMapping(value = { "/confirmPurchase" }, method = RequestMethod.POST)
	public String completePurChase(HttpServletRequest request, Model model, @RequestParam("conCode") String conCode) {
		try {
			String message = orderDAO.confirmPurchase(conCode);
			model.addAttribute("message", message);
		} catch (Exception e) {
			model.addAttribute("message", e.getMessage());
			return "confirmPurchase";
		}

		ShoppingCart cartInfo = Utils.getCartInSession(request);

		// store lastest order for finalization
		Utils.storeLastOrderedCartInSession(request, cartInfo);

		// Delete cart from session
		Utils.removeCartInSession(request);
		return "redirect:/shoppingCartFinalize";
	}

	/**
	 * Cancel/change status the order
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/cancelOrder" })
	public String cancelOrder(HttpServletRequest request, Model model) {
		orderDAO.cancelOrder();

		ShoppingCart cartInfo = Utils.getCartInSession(request);

		// store lastest order for finalization
		Utils.storeLastOrderedCartInSession(request, cartInfo);

		// Delete cart from session
		Utils.removeCartInSession(request);
		return "redirect:/productList";
	}

	/**
	 * Send the order detail via email
	 * 
	 * @param order
	 */
	public void sendSimpleEmail(OrderInfo order) {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();
		Order o = orderDAO.findOrder(order.getId());

		message.setTo(order.getEmail());
		message.setSubject("Japanese Restaurant");
		message.setText("Hello, this is the receive for your order:\n " + order.toString2()
				+ "\n\n This is the confirm code: " + o.getConCode());

		// Send Message!
		this.emailSender.send(message);
	}

	/**
	 * Finalize the cart
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {

		ShoppingCart lastOrderedCart = Utils.getLastOrderedCartInSession(request);

		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		return "shoppingCartFinalize";
	}

}
