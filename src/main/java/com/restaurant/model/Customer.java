package com.restaurant.model;


import com.restaurant.form.CustomerForm;

public class Customer {


	private String custName;

	private String email;

	private String phone;
	
	private String address;
	
	

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCustName() {
		return custName;
	}

	public void setCustName(String custName) {
		this.custName = custName;
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

	public Customer(String custName, String email, String phone, String address) {
		super();
		this.custName = custName;
		this.email = email;
		this.phone = phone;
		this.address = address;
	}

	public Customer(CustomerForm customerForm) {
		this.custName = customerForm.getCustName();
		this.email = customerForm.getEmail();
		this.phone = customerForm.getPhone();
		this.address = customerForm.getAddress();
	}

	public Customer() {
		super();
	}

}
