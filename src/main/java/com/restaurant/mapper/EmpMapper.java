package com.restaurant.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.restaurant.model.Employee;

public class EmpMapper implements RowMapper<Employee> {

	@Override
	public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
		Employee emp = new Employee();
		emp.setId(rs.getInt("id"));
		emp.setEmpName(rs.getString("empName"));
		emp.setDob(String.valueOf(rs.getDate("dob")));
		emp.setEmail(rs.getString("email"));
		emp.setPhone(rs.getString("phone"));
		emp.setAddress(rs.getString("address"));
		emp.setHire_date(String.valueOf(rs.getDate("hire_date")));
		emp.setSalary(rs.getDouble("salary"));
		emp.setImage(rs.getBytes("image"));
		return emp;
	}

}
