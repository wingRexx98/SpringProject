package com.restaurant.form;

import org.springframework.web.multipart.MultipartFile;

public class EmpForm {

	private String empName;

	private String dob;

	private String email;

	private String phone;

	private String address;

	private String hire_date;

	private double salary;

	private String userName;

	private String encryptedPassword;

	private int roleId;

	private MultipartFile image;

	private boolean active;

	public EmpForm(String empName, String dob, String email, String phone, String address, String hire_date,
			double salary, String userName, String encryptedPassword, int roleId, MultipartFile image, boolean active) {
		super();
		this.empName = empName;
		this.dob = dob;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.hire_date = hire_date;
		this.salary = salary;
		this.userName = userName;
		this.encryptedPassword = encryptedPassword;
		this.roleId = roleId;
		this.image = image;
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHire_date() {
		return hire_date;
	}

	public void setHire_date(String hire_date) {
		this.hire_date = hire_date;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void setEncryptedPassword(String encryptedPassword) {
		this.encryptedPassword = encryptedPassword;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public EmpForm() {
		super();
	}

}
