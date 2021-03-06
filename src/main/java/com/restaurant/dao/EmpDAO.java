package com.restaurant.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.restaurant.form.EmpForm;
import com.restaurant.mapper.EmpMapper;
import com.restaurant.mapper.UserLoginMapper;
import com.restaurant.model.Employee;
import com.restaurant.model.UserLogin;

@Repository
@Transactional
public class EmpDAO extends JdbcDaoSupport {

	@Autowired
	public EmpDAO(DataSource dataSource) {
		this.setDataSource(dataSource);
	}

	/**
	 * From employee form into employee
	 * 
	 * @param form
	 * @return
	 * @throws IOException
	 */
	public Employee toEmployee(EmpForm form) throws IOException {
		Employee emp = new Employee();

		emp.setEmpName(form.getEmpName());
		emp.setDob(form.getDob());
		emp.setAddress(form.getAddress());
		emp.setEmail(form.getEmail());
		emp.setHire_date(form.getHire_date());
		emp.setPhone(form.getPhone());
		emp.setSalary(form.getSalary());

		if (form.getImage() != null) {
			byte[] image = form.getImage().getBytes();
			if (image != null && image.length > 0) {
				emp.setImage(image);
			}
		} else {
			emp.setImage(null);
		}

		return emp;
	}

	/**
	 * from emp form into user login
	 * 
	 * @param form
	 * @return
	 */
	public UserLogin toUserLogin(EmpForm form) {
		UserLogin loginInfo = new UserLogin();

		loginInfo.setEmpId(this.maxEmpId() + 1);
		loginInfo.setUserName(form.getUserName());
		loginInfo.setEncryptedPassword(this.passwordEncoder(form.getEncryptedPassword()));
		loginInfo.setActive(true);
		loginInfo.setRoleId(form.getRoleId());

		return loginInfo;
	}

	/**
	 * Save emp into db
	 * 
	 * @param form
	 * @return
	 * @throws IOException
	 */
	public int saveEmp(EmpForm form) throws IOException {
		int check = 0;

		if (!validUserName(form.getUserName())) {
			return check = 0;
		}

		Employee emp = this.toEmployee(form);

		UserLogin loginInfo = this.toUserLogin(form);

		String sql = "insert into Employee (id, empName, dob, email, phone, address, hire_date,salary, image)"
				+ " values(?,?,?,?,?,?,?,?,?)";
		this.getJdbcTemplate().update(sql, this.maxEmpId() + 1, emp.getEmpName(), emp.getDob(), emp.getEmail(),
				emp.getPhone(), emp.getAddress(), emp.getHire_date(), emp.getSalary(), emp.getImage());

		sql = "insert into User_Login(empid, roleid, username, password, enabled) values (?,?,?,?, 1)";
		check = this.getJdbcTemplate().update(sql, loginInfo.getEmpId(), loginInfo.getRoleId(), loginInfo.getUserName(),
				loginInfo.getEncryptedPassword());
		return check;
	}

	/**
	 * Update emp info, username and password not included
	 * 
	 * @param form
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public int updateEmpbyEmp(EmpForm form, int id) throws IOException {
		int check = 0;
		Employee emp = this.toEmployee(form);

		if (form.getImage() != null) {
			byte[] image = form.getImage().getBytes();
			if (image != null && image.length > 0) {
				emp.setImage(image);
			}
		} else {
			emp.setImage(null);
		}
		if (emp.getImage() == null) {
			String sql = "Update Employee SET empName = ?, dob =?, email =?, phone =?, address =? WHERE id = ?";
			check = this.getJdbcTemplate().update(sql, emp.getEmpName(), emp.getDob(), emp.getEmail(), emp.getPhone(),
					emp.getAddress(), id);
		} else {
			String sql = "Update Employee SET empName = ?, dob =?, email =?, phone =?, address =?, image=? WHERE id = ?";
			check = this.getJdbcTemplate().update(sql, emp.getEmpName(), emp.getDob(), emp.getEmail(), emp.getPhone(),
					emp.getAddress(), emp.getImage(), id);
		}
		return check;
	}

	/**
	 * Emp promotion
	 * 
	 * @param id
	 * @return
	 * @throws IOException
	 */
	public int updateEmpbyAdmin(int id) throws IOException {
		int check = 0;
		Employee emp = this.findEmp(id);
		emp.setSalary(emp.getSalary() * 2);
		String sql = "Update Employee SET Salary =? WHERE id = ?";
		this.getJdbcTemplate().update(sql, emp.getSalary(), id);

		sql = "UPDATE User_Login SET roleid = 1 where empid = ?";
		check = this.getJdbcTemplate().update(sql, id);
		return check;
	}

	/**
	 * Disable account
	 * 
	 * @param id
	 */
	public void deactivateAccount(int id) {
		String sql = "UPDATE User_Login SET enabled =0 where empid = ?";
		this.getJdbcTemplate().update(sql, id);
	}

	/**
	 * Get all emps
	 * 
	 * @return
	 */
	public List<Employee> allEmp() {
		List<Employee> list = new ArrayList<>();
		String sql = "Select * from Employee";
		Object[] params = new Object[] {};
		EmpMapper mapper = new EmpMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * All emp with emp role
	 * 
	 * @return
	 */
	public List<Employee> allEmpWithEmpRole() {
		List<Employee> list = new ArrayList<>();
		String sql = "SELECT e.id, e.empName, e.dob,e.email,e.phone,e.address,e.hire_date, e.salary, e.image "
				+ "FROM Employee e Join user_login ul on e.id = ul.empid join user_role ur "
				+ "on ur.id = ul.roleId Where ur.roleName = 'ROLE_EMPLOYEE' AND ul.enabled = 1";
		Object[] params = new Object[] {};
		EmpMapper mapper = new EmpMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * All emps with admin role
	 * 
	 * @return
	 */
	public List<Employee> allAdmin() {
		List<Employee> list = new ArrayList<>();
		String sql = "SELECT e.id, e.empName, e.dob,e.email,e.phone,e.address,e.hire_date, e.salary, e.image "
				+ "FROM Employee e Join user_login ul on e.id = ul.empid join user_role ur "
				+ "on ur.id = ul.roleId Where ur.roleName = 'ROLE_ADMIN' AND ul.enabled = 1";
		Object[] params = new Object[] {};
		EmpMapper mapper = new EmpMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * All emp
	 * 
	 * @return
	 */
	public List<Employee> allList() {
		List<Employee> list = new ArrayList<>();
		String sql = "SELECT e.id, e.empName, e.dob,e.email,e.phone,e.address,e.hire_date, e.salary, e.image "
				+ "FROM Employee e Join user_login ul on e.id = ul.empid join user_role ur "
				+ "on ur.id = ul.roleId Where ul.enabled = 1";
		Object[] params = new Object[] {};
		EmpMapper mapper = new EmpMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * All emp with disabled account
	 * 
	 * @return
	 */
	public List<Employee> disabledList() {
		List<Employee> list = new ArrayList<>();
		String sql = "SELECT e.id, e.empName, e.dob,e.email,e.phone,e.address,e.hire_date, e.salary, e.image "
				+ "FROM Employee e Join user_login ul on e.id = ul.empid join user_role ur "
				+ "on ur.id = ul.roleId Where ul.enabled = 0";
		Object[] params = new Object[] {};
		EmpMapper mapper = new EmpMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * all login account
	 * 
	 * @return
	 */
	public List<UserLogin> allUserLogin() {
		List<UserLogin> list = new ArrayList<>();
		String sql = "Select * from User_Login";
		Object[] params = new Object[] {};
		UserLoginMapper mapper = new UserLoginMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * Validate new username
	 * 
	 * @param username
	 * @return
	 */
	public boolean validUserName(String username) {
		List<UserLogin> list = this.allUserLogin();
		for (UserLogin u : list) {
			if (username.equals(u.getUserName())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * validate update Id
	 * 
	 * @param id
	 * @return
	 */
	public boolean validUpdateId(int id) {
		List<UserLogin> list = this.allUserLogin();
		for (UserLogin u : list) {
			if (id == u.getEmpId()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Get the max id
	 * 
	 * @return
	 */
	public int maxEmpId() {
		int id = 0;
		List<Employee> list = this.allEmp();
		for (Employee e : list) {
			if (id < e.getId()) {
				id = e.getId();
			}
		}
		return id;
	}

	/**
	 * Encode the password
	 * 
	 * @param password
	 * @return
	 */
	public String passwordEncoder(String password) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(password);
	}

	/**
	 * paging the emp list
	 * 
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public List<Employee> employees(int pageIndex, int pageSize) {
		List<Employee> list = new ArrayList<>();
		String sql = "{CALL pageEmployeeDevider (?,?)}";
		Object[] params = new Object[] { pageIndex, pageSize };
		EmpMapper mapper = new EmpMapper();

		list = this.getJdbcTemplate().query(sql, params, mapper);
		return list;
	}

	/**
	 * Search emp by id
	 * 
	 * @param id
	 * @return
	 */
	public Employee findEmp(int id) {
		String sql = "Select * from Employee where id = ?";
		Object[] params = new Object[] { id };
		EmpMapper mapper = new EmpMapper();
		Employee emp = this.getJdbcTemplate().queryForObject(sql, params, mapper);
		return emp;
	}

	/**
	 * Search emp by name
	 * 
	 * @param username
	 * @return
	 */
	public Employee findEmpByUsername(String username) {
		String sql = "Select * from Employee e JOIN User_Login ul ON e.id = ul.empid AND ul.username = ?";
		Object[] params = new Object[] { username };
		EmpMapper mapper = new EmpMapper();
		Employee emp = this.getJdbcTemplate().queryForObject(sql, params, mapper);
		return emp;
	}

}
