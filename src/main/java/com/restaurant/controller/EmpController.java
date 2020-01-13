package com.restaurant.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.restaurant.dao.EmpDAO;
import com.restaurant.dao.UserRoleDAO;
import com.restaurant.form.EmpForm;
import com.restaurant.model.Employee;
import com.restaurant.model.User_Role;

@Controller
public class EmpController {

	@Autowired
	private EmpDAO empDAO;

	@Autowired
	private UserRoleDAO roleDAO;

	/**
	 * Add new employee info
	 * 
	 * @param model
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = { "/addEmployee" }, method = RequestMethod.GET)
	public String addEmployee(Model model, Principal principal) {
		EmpForm form = new EmpForm();
		List<User_Role> roles = roleDAO.allRole();
		model.addAttribute("empForm", form);
		model.addAttribute("roles", roles);
		return "addEmployee";
	}

	/**
	 * Save new emp info
	 * 
	 * @param model
	 * @param form
	 * @return
	 * @throws DataAccessException
	 * @throws IOException
	 */
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

	/**
	 * List all emp
	 * 
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
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
		return "empList";
	}

	/**
	 * List all admin
	 * 
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = { "/adminList" })
	public String listOfAdmins(Model model, @RequestParam(value = "page", defaultValue = "1") int pageIndex,
			Principal principal) {

		int total = empDAO.allAdmin().size();
		int pageSize = total;
		List<Employee> emps = empDAO.allAdmin();
		model.addAttribute("list", emps);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalRecord", total);
		model.addAttribute("size", total);
		return "empList";
	}

	/**
	 * List all workers
	 * 
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = { "/list" })
	public String allEmployee(Model model, @RequestParam(value = "page", defaultValue = "1") int pageIndex,
			Principal principal) {

		int total = empDAO.allList().size();
		int pageSize = total;
		List<Employee> emps = empDAO.allList();
		model.addAttribute("list", emps);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("totalRecord", total);
		model.addAttribute("size", total);
		return "empList";
	}

	/**
	 * Search emp base on letters
	 * 
	 * @param searchName
	 * @param model
	 * @param pageIndex
	 * @param principal
	 * @return
	 */
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
		model.addAttribute("size", searchResults.size());
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalRecord", total);
		return "empList";
	}

//	/**
//	 * Get account info
//	 * 
//	 * @param model
//	 * @param principal
//	 * @return
//	 */
//	@RequestMapping(value = { "/accountInfo" }, method = RequestMethod.GET)
//	public String accountInfo(Model model, Principal principal) {
//
//		String userName = principal.getName();
//
//		System.out.println("User Name: " + userName);
//
//		Employee emp = empDAO.findEmpByUsername(userName);
//
//		model.addAttribute("userInfo", emp);
//
//		return "accountInfo";
//	}

	/**
	 * Form to edit account
	 * 
	 * @param model
	 * @param id
	 * @param principal
	 * @return
	 */
	@RequestMapping(value = { "/editAccount" }, method = RequestMethod.GET)
	public String editEmp(Model model, @RequestParam(name = "id") int id, Principal principal) {
		Employee emp = empDAO.findEmp(id);
		model.addAttribute("employee", emp);
		return "editAccount";
	}

	/**
	 * Save account update
	 * 
	 * @param model
	 * @param form
	 * @param id
	 * @param principal
	 * @return
	 * @throws IOException
	 */
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

}
