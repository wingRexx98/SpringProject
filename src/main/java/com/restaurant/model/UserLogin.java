package com.restaurant.model;

import java.io.Serializable;


public class UserLogin implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int empId;

	private String userName;

	private String encryptedPassword;

	private int roleId;
	
	private boolean active;

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
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

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserLogin(int empId, String userName, String encryptedPassword, int roleId, boolean active) {
		super();
		this.empId = empId;
		this.userName = userName;
		this.encryptedPassword = encryptedPassword;
		this.roleId = roleId;
		this.active = active;
	}

	public UserLogin() {
		super();
	}

}
