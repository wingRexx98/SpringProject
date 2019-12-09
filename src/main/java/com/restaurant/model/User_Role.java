package com.restaurant.model;

import java.io.Serializable;

public class User_Role implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private String roleName;

	private String descript;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescript() {
		return descript;
	}

	public void setDescript(String descript) {
		this.descript = descript;
	}

	public User_Role(int id, String roleName, String descript) {
		super();
		this.id = id;
		this.roleName = roleName;
		this.descript = descript;
	}

	public User_Role() {
		super();
	}

}
