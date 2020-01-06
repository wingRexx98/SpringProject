package com.restaurant.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.restaurant.dao.CategoryDAO;
import com.restaurant.dao.FoodDAO;
import com.restaurant.form.FoodForm;
import com.restaurant.model.Category;
import com.restaurant.model.Food;

@Controller
public class ProductController {

	@Autowired
	private FoodDAO foodDAO;

	@Autowired
	private CategoryDAO categoryDAO;

	/**
	 * Show list of product with pagination
	 * 
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
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

	/**
	 * Show form for creating new product
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/newProduct" })
	public String newProductHandler(Model model) {
		FoodForm form = new FoodForm();
		List<Category> cates = categoryDAO.getCategory();
		model.addAttribute("foodForm", form);
		model.addAttribute("categoryList", cates);
		return "saveProduct";
	}

	/**
	 * Save new product info
	 * 
	 * @param model
	 * @param form
	 * @return
	 * @throws DataAccessException
	 * @throws IOException
	 */
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

	/**
	 * Disable product that are no longer available
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/removeProduct" }, method = RequestMethod.GET)
	public String removeProductHandler(Model model, @RequestParam(name = "id") int id) {
		int check = foodDAO.removeFood(id);
		System.out.println(check);
		return "redirect:/productList";
	}

	/**
	 * Update outdated info
	 * 
	 * @param model
	 * @param id
	 * @return
	 */
	@RequestMapping(value = { "/editProduct" }, method = RequestMethod.GET)
	public String editProductHandler(Model model, @RequestParam(name = "id") int id) {
		Food food = foodDAO.findFood(id);
		List<Category> cates = categoryDAO.getCategory();
		model.addAttribute("food", food);
		model.addAttribute("categoryList", cates);
		return "editProduct";
	}

	/**
	 * Update the info in db
	 * 
	 * @param model
	 * @param food
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * @throws IOException
	 */
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

	/**
	 * Get the product image from db onto the screen
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param id
	 * @throws IOException
	 */
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

	/**
	 * Show detail of a product
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = { "/productDetail" }, method = RequestMethod.GET)
	public String productView(@RequestParam(name = "id") int id, Model model) {
		Food food = foodDAO.findFood(id);
		model.addAttribute("food", food);
		Category cate = categoryDAO.findCategory(food.getCateID());
		model.addAttribute("cate", cate);
		return "productDetail";
	}

	/**
	 * Search a product
	 * 
	 * @param searchName
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
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

	/**
	 * Sort product based on Category
	 * 
	 * @param cateId
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
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
