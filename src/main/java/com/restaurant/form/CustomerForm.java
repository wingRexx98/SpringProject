package com.restaurant.form;

import com.restaurant.model.Customer;

public class CustomerForm {

	private String custName;
	private String email;
	private String phone;
	private String address;

	private boolean valid;

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

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public CustomerForm(String custName, String email, String phone, String address, boolean valid) {
		super();
		this.custName = custName;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.valid = valid;
	}
	
	public CustomerForm(Customer customer) {
		this.custName = customer.getCustName();
		this.email = customer.getEmail();
		this.phone = customer.getPhone();
		this.address = customer.getAddress();
	}

	public CustomerForm() {
		super();
	}

}
