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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.restaurant.dao.EmpDAO;
import com.restaurant.dao.OrderDAO;
import com.restaurant.dao.UserLoginDAO;
import com.restaurant.dao.UserRoleDAO;
import com.restaurant.form.EmpForm;
import com.restaurant.form.FoodForm;
import com.restaurant.model.Employee;
import com.restaurant.model.Order;
import com.restaurant.model.OrderDetailInfo;
import com.restaurant.model.OrderInfo;
import com.restaurant.model.UserLogin;
import com.restaurant.model.User_Role;

@Controller
public class AdminController {

	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private EmpDAO empDAO;

	@Autowired
	private UserRoleDAO roleDAO;

	@Autowired
	private UserLoginDAO loginDAO;

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.println("Target=" + target);

		if (target.getClass() == FoodForm.class) {
			// dataBinder.setValidator(productFormValidator);
		}
	}

	// Order list
	@RequestMapping({ "/orderList" })
	private String orderList(Model model, //
			@RequestParam(value = "page", defaultValue = "1") int pageIndex, Principal principal) {
		int pageSize = 10;
		int records = orderDAO.listOfOrder().size();
		List<Order> orders = orderDAO.pagingOrders(pageIndex, pageSize);
		List<OrderInfo> infos = new ArrayList<OrderInfo>();
		for (Order o : orders) {
			OrderInfo info = new OrderInfo(o.getId(), o.getCustName(), o.getEmail(), o.getPhone(),
					o.getDeliverAddress(), o.getTotalPrice(), o.getOrderStatus(), o.isEnabled(), o.getConCode(),o.isConfirmed());
			infos.add(info);
		}
		model.addAttribute("list", infos);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalRecords", records);
		return "orderList";
	}

	@RequestMapping(value = "/updateOrder", method = RequestMethod.POST)
	public String updateProduct(Model model, @ModelAttribute("orderInfo") OrderInfo order,
			@RequestParam(name = "id") int id, Principal principal) throws DataAccessException, IOException {
		orderDAO.updateOrderStatus(order, id);
		return "redirect:/orderList";
	}

	// Order detail view
	@RequestMapping(value = { "/orderView" }, method = RequestMethod.GET)
	public String orderView(Model model, @RequestParam(name = "id") int id, Principal principal) {
		if (id == 0) {
			model.addAttribute("ErrorMessage", "ID not found");
			return "redirect:/orderList";
		}
		OrderInfo info = orderDAO.getOrderInfo(id);
		List<OrderDetailInfo> details = orderDAO.detailInfos(id);
		info.setDetails(details);
		model.addAttribute("orderInfo", info);
		return "orderView";
	}

	// Order remove
	@RequestMapping(value = { "/removeOrder" }, method = RequestMethod.GET)
	public String removeOrder(Model model, @RequestParam(name = "id") int id, Principal principal) {
		if (id == 0) {
			model.addAttribute("ErrorMessage", "ID not found");
			return "redirect:/orderList";
		}
		orderDAO.removeOrder(id);
		return "redirect:/orderList";
	}

	/////////////////////// Login
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(Model model) {

		return "login";
	}

	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
	public String logoutSuccessfulPage(Model model) {
		model.addAttribute("title", "Logout");
		return "logoutSuccessfulPage";
	}

	@RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
	public String accountInfo(Model model, Principal principal) {

		String userName = principal.getName();

		System.out.println("User Name: " + userName);

		Employee emp = empDAO.findEmpByUsername(userName);

		model.addAttribute("userInfo", emp);

		return "accountInfo";
	}

	////////////////////////// EMP

	@RequestMapping(value = { "/addEmployee" }, method = RequestMethod.GET)
	public String addEmployee(Model model, Principal principal) {
		EmpForm form = new EmpForm();
		List<User_Role> roles = roleDAO.allRole();
		model.addAttribute("empForm", form);
		model.addAttribute("roles", roles);
		return "addEmployee";
	}

	// Emp save
	@RequestMapping(value = { "/saveEmpInfo" }, method = RequestMethod.POST)
	public String saveProductHandler(Model model, @ModelAttribute("empForm") EmpForm form)
			throws DataAccessException, IOException {
		String errorMessage;
		int i = empDAO.saveEmp(form);
		if (i == 0) {
			errorMessage = "Something went wrong, Most likely the user login name already exist";
			List<User_Role> roles = roleDAO.allRole();
			model.addAttribute("categoryList", roles);
			model.addAttribute("error", errorMessage);
			return "addEmployee";
		}
		return "redirect:/";
	}

	// Emp list
	@RequestMapping(value = { "/empList" })
	public String listOfEmployees(Model model, @RequestParam(value = "page", defaultValue = "1") int pageIndex,
			Principal principal) {
		int pageSize = 5;
		int total = empDAO.allEmpWithEmpRole().size();
		List<Employee> emps = empDAO.employees(pageIndex, pageSize);
		model.addAttribute("list", emps);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalRecord", total);
		model.addAttribute("size", total);
		System.out.println(emps.size());
		return "empList";
	}

	@RequestMapping(value = { "/empImage" }, method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(name = "id") int id) throws IOException {
		Employee emp = null;
		if (id != 0) {
			emp = this.empDAO.findEmp(id);
		}
		if (emp != null && emp.getImage() != null) {
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(emp.getImage());
		}
		response.getOutputStream().close();
	}

	@RequestMapping(value = { "/promoteEmp" })
	public String promoteEmp(Model model, @RequestParam(name = "id") int id, Principal principal) throws IOException {
		empDAO.updateEmpbyAdmin(id);
		return "redirect:/empList";
	}

	@RequestMapping(value = { "/editAccount" }, method = RequestMethod.GET)
	public String editEmp(Model model, @RequestParam(name = "id") int id, Principal principal) {
		Employee emp = empDAO.findEmp(id);
		model.addAttribute("employee", emp);
		return "editAccount";
	}

	@RequestMapping(value = { "/updateAccount" }, method = RequestMethod.POST)
	public String updateEmp(Model model, @ModelAttribute("empForm") EmpForm form, @RequestParam(name = "id") int id,
			Principal principal) throws IOException {
		int check = 0;
		check = empDAO.updateEmpbyEmp(form, id);
		if (check == 0) {
			Employee emp = empDAO.findEmp(id);
			model.addAttribute("employee", emp);
			return "redirect:/editAccount";
		}
		return "redirect:/empList";
	}

	@RequestMapping(value = { "/editUsername" }, method = RequestMethod.GET)
	public String changeUnamePass(Model model, @RequestParam(name = "id") int id, Principal principal) {
		UserLogin loginInfo = loginDAO.findAccountByEmp(id);
		model.addAttribute("userLogin", loginInfo);
		return "editUsername";
	}

	@RequestMapping(value = { "/updateUsername" }, method = RequestMethod.POST)
	public String change(Model model, @ModelAttribute("empForm") EmpForm form, @RequestParam(name = "id") int id,
			Principal principal) {
		int check = 0;
		check = loginDAO.updateAccountByEmp(form, id);
		if (check == 0) {
			UserLogin loginInfo = loginDAO.findAccountByEmp(id);
			model.addAttribute("userLogin", loginInfo);
			return "redirect:/editUsername";
		}
		return "redirect:/accountInfo";
	}

	@RequestMapping(value = { "/empSearch" })
	public String empSearch(@RequestParam(name = "search") String searchName, Model model,
			@RequestParam(value = "page", defaultValue = "1") int pageIndex, Principal principal) {
		List<Employee> emps = empDAO.allEmpWithEmpRole();
		int total = emps.size();
		List<Employee> searchResults = new ArrayList<>();
		for (Employee e : emps) {
			if (e.getEmpName().contains(searchName)) {
				searchResults.add(e);
			}
		}
		String error = null;
		String message = null;
		if (searchResults.isEmpty() || searchResults == null) {
			error = "No emp found";
			model.addAttribute("error", error);
		} else {
			message = "Found " + searchResults.size() + " Employees";
			model.addAttribute("message", message);
			model.addAttribute("list", searchResults);
		}
		model.addAttribute("pageSize", 5);
		model.addAttribute("size",searchResults.size());
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalRecord", total);
		return "empList";
	}
	
	@RequestMapping(value = { "/orderSearch" })
	public String orderSearch(@RequestParam(name = "search") String searchName, Model model,
			@RequestParam(value = "page", defaultValue = "1") int pageIndex, Principal principal) {
		List<Order> orders = orderDAO.listOfOrder();
		List<Order> list = new ArrayList<>();
		for(Order o : orders) {
			if(o.getCustName().contains(searchName)) {
				list.add(o);
			}
		}
		List<OrderInfo> infos = toInfo(list);
		String error = null;
		String message = null;
		if (list.isEmpty() || list == null) {
			error = "No emp found";
			model.addAttribute("error", error);
		} else {
			message = "Found " + list.size() + " Orders made by customer "+searchName;
			model.addAttribute("message", message);
			model.addAttribute("list", infos);
		}
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalRecords", list.size());
		return "orderList";
	}
	
	public List<OrderInfo> toInfo(List<Order> orders){
		List<OrderInfo> infos = new ArrayList<>();
		for(Order o : orders) {
			OrderInfo info = new OrderInfo(o.getId(), o.getCustName(), o.getEmail(), o.getPhone(),
					o.getDeliverAddress(), o.getTotalPrice(), o.getOrderStatus(), o.isEnabled(), o.getConCode(),o.isConfirmed());
			infos.add(info);
		}
		return infos;
	}
}
