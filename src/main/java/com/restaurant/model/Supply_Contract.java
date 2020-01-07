package com.restaurant.model;

public class Supply_Contract {

	private int supplierId;
	private int ingridientId;
	private int supplyQuantity;
	private String contractSignDate;
	private String contractExpireDate;

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public int getIngridientId() {
		return ingridientId;
	}

	public void setIngridientId(int ingridientId) {
		this.ingridientId = ingridientId;
	}

	public int getSupplyQuantity() {
		return supplyQuantity;
	}

	public void setSupplyQuantity(int supplyQuantity) {
		this.supplyQuantity = supplyQuantity;
	}

	public String getContractSignDate() {
		return contractSignDate;
	}

	public void setContractSignDate(String contractSignDate) {
		this.contractSignDate = contractSignDate;
	}

	public String getContractExpireDate() {
		return contractExpireDate;
	}

	public void setContractExpireDate(String contractExpireDate) {
		this.contractExpireDate = contractExpireDate;
	}

	public Supply_Contract(int supplierId, int ingridientId, int supplyQuantity, String contractSignDate,
			String contractExpireDate) {
		super();
		this.supplierId = supplierId;
		this.ingridientId = ingridientId;
		this.supplyQuantity = supplyQuantity;
		this.contractSignDate = contractSignDate;
		this.contractExpireDate = contractExpireDate;
	}

	public Supply_Contract() {
		super();
	}

}
