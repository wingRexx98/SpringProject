package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Supply_Contract;

public class ContractMapper implements RowMapper<Supply_Contract> {

	@Override
	public Supply_Contract mapRow(ResultSet rs, int rowNum) throws SQLException {
		Supply_Contract contract = new Supply_Contract();

		contract.setSupplierId(rs.getInt("supplierId"));
		contract.setIngridientId(rs.getInt("ingridientId"));
		contract.setSupplyQuantity(rs.getInt("supplyQuantity"));
		contract.setContractSignDate(String.valueOf(rs.getDate("contractSignDate")));
		contract.setContractExpireDate(String.valueOf(rs.getDate("contractExpireDate")));
		return contract;
	}
}
