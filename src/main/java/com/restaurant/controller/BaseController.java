package com.restaurant.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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

import com.restaurant.dao.CategoryDAO;
import com.restaurant.dao.FoodDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.form.CustomerForm;
import com.restaurant.form.FoodForm;
import com.restaurant.model.Category;
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
	private CategoryDAO categoryDAO;

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

	/////////////////////////////////////////////////// Product

	// Prodcut list
	@RequestMapping({ "/productList" })
	public String listProductHandler(Model model, //
			@RequestParam(value = "page", defaultValue = "1") int pageIndex, Principal principal) {
		int pageSize = 5;
		List<Food> foods = foodDAO.pageRecords(pageIndex, pageSize);
		List<Food> list = foodDAO.getFood();
		List<Category> cates = categoryDAO.getCategory();
		model.addAttribute("categoryList", cates);
		model.addAttribute("list", foods);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalRecord", list.size());
		return "productList";
	}

	// Prodcut input form
	@RequestMapping({ "/newProduct" })
	public String newProductHandler(Model model) {
		FoodForm form = new FoodForm();
		List<Category> cates = categoryDAO.getCategory();
		model.addAttribute("foodForm", form);
		model.addAttribute("categoryList", cates);
		return "saveProduct";
	}

	// Product save
	@RequestMapping(value = { "/saveProduct" }, method = RequestMethod.POST)
	public String saveProductHandler(Model model, @ModelAttribute("foodForm") FoodForm form)
			throws DataAccessException, IOException {
		String errorMessage;
		int i = foodDAO.addFood(form);
		if (i == 0) {
			errorMessage = "Something went wrong, Most likely the product name already exist";
			List<Category> cates = categoryDAO.getCategory();
			model.addAttribute("categoryList", cates);
			model.addAttribute("error", errorMessage);
			return "saveProduct";
		}
		return "redirect:/productList";
	}

	// Product delete/disable
	@RequestMapping(value = { "/removeProduct" }, method = RequestMethod.GET)
	public String removeProductHandler(Model model, @RequestParam(name = "id") int id) {
		int check = foodDAO.removeFood(id);
		System.out.println(check);
		return "redirect:/productList";
	}

	// Product update
	@RequestMapping(value = { "/editProduct" }, method = RequestMethod.GET)
	public String editProductHandler(Model model, @RequestParam(name = "id") int id) {
		Food food = foodDAO.findFood(id);
		List<Category> cates = categoryDAO.getCategory();
		model.addAttribute("food", food);
		model.addAttribute("categoryList", cates);
		return "editProduct";
	}

	@RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
	public String updateProduct(Model model, @ModelAttribute("food") FoodForm food, @RequestParam(name = "id") int id)
			throws DataAccessException, IOException {
		int check = foodDAO.updateFood(food, id);
		if (check == 0) {
			Food food2 = foodDAO.findFood(id);
			String errorMessage = "Something went wrong, Most likely the product name already exist";
			List<Category> cates = categoryDAO.getCategory();
			model.addAttribute("categoryList", cates);
			model.addAttribute("error", errorMessage);
			model.addAttribute("food", food2);
			return "editProduct";
		}
		return "redirect:/productList";
	}

	@RequestMapping(value = { "/productImage" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(name = "id") int id) throws IOException {
		Food food = null;
		if (id != 0) {
			food = this.foodDAO.findFood(id);
		}
		if (food != null && food.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(food.getImage());
		}
		response.getOutputStream().close();
	}

	////////////////////////////////// Shopping cart

	// Add product to cart
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

	// remove product from cart
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

	// POST: update product quantity
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.POST)
	public String shoppingCartUpdateQty(HttpServletRequest request, //
			Model model, //
			@ModelAttribute("cartForm") ShoppingCart cartForm) {

		ShoppingCart cartInfo = Utils.getCartInSession(request);
		cartInfo.updateQuantity(cartForm);

		return "redirect:/shoppingCart";
	}

	// GET: display cart
	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		ShoppingCart myCart = Utils.getCartInSession(request);

		model.addAttribute("cartForm", myCart);
		return "shoppingCart";
	}

	// GET: customer info input
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

	// POST: save customer info
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

	// GET: Review shopping cart
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

	// POST: Save order/cart info.
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
	
	@RequestMapping(value = { "/confirmPurchaseForm" }, method = RequestMethod.GET)
	public String completePurChaseForm(Model model) {
		
		return "confirmPurchase";
	}

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

	public void sendSimpleEmail(OrderInfo order) {

		// Create a Simple MailMessage.
		SimpleMailMessage message = new SimpleMailMessage();
		Order o = orderDAO.findOrder(order.getId());

		message.setTo(order.getEmail());
		message.setSubject("Japanese Restaurant");
		message.setText("Hello, this is the receive for your order:\n " + order.toString()
				+ "\n\n This is the confirm code: " + o.getConCode());

		// Send Message!
		this.emailSender.send(message);
	}

	// Finalise the purchase
	@RequestMapping(value = { "/shoppingCartFinalize" }, method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {

		ShoppingCart lastOrderedCart = Utils.getLastOrderedCartInSession(request);

		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		return "shoppingCartFinalize";
	}

	@RequestMapping(value = { "/productDetail" }, method = RequestMethod.GET)
	public String productView(@RequestParam(name = "id") int id, Model model) {
		Food food = foodDAO.findFood(id);
		model.addAttribute("food", food);
		Category cate = categoryDAO.findCategory(food.getCateID());
		model.addAttribute("cate", cate);
		return "productDetail";
	}

	@RequestMapping(value = { "/productSearch" }, method = RequestMethod.GET)
	public String findProduct(@RequestParam(name = "search") String searchName, Model model,
			@RequestParam(value = "page", defaultValue = "1") int pageIndex, Principal principal) {
		List<Food> foods = foodDAO.getFood();
		List<Food> list = new ArrayList<Food>();
		for (Food f : foods) {
			if (f.getFoodName().contains(searchName)) {
				list.add(f);
			}
		}
		String error = null;
		String message = null;
		if (list.isEmpty() || list == null) {
			error = "No such dish found";
			model.addAttribute("error", error);
		} else {
			message = "Found dish " + list.size();
			model.addAttribute("message", message);
			model.addAttribute("list", list);
		}
		model.addAttribute("pageSize", list.size());
		model.addAttribute("totalRecord", list.size());
		model.addAttribute("pageIndex", pageIndex);
		return "productList";
	}

	@RequestMapping(value = { "/byCategory" }, method = RequestMethod.GET)
	public String getProductWithCategory(@RequestParam(name = "cateId") int cateId, Model model,
			@RequestParam(value = "page", defaultValue = "1") int pageIndex, Principal principal) {

		if (cateId == 0) {
			return "redirect:/productList";
		}
		List<Food> foods = foodDAO.getFood();
		List<Food> list = new ArrayList<Food>();
		for (Food f : foods) {
			if (f.getCateID() == cateId) {
				list.add(f);
			}
		}
		String error = null;
		String message = null;
		if (list.isEmpty() || list == null) {
			error = "No such dish found";
			model.addAttribute("error", error);
		} else {
			message = "Found dish " + list.size();
			model.addAttribute("message", message);
			model.addAttribute("list", list);
		}
		List<Category> cates = categoryDAO.getCategory();
		model.addAttribute("categoryList", cates);
		model.addAttribute("pageSize", list.size());
		model.addAttribute("totalRecord", list.size());
		model.addAttribute("pageIndex", pageIndex);
		return "productList";
	}

}
