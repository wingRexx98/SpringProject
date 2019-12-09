package com.restaurant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.restaurant.dao.UserLoginDAO;
import com.restaurant.dao.UserRoleDAO;
import com.restaurant.model.UserLogin;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserLoginDAO loginDAO;

	@Autowired
	private UserRoleDAO roleDAO;

	public UserDetails loadUserByUsername(String username) {
		UserLogin account = loginDAO.findAccount(username);
		System.out.println("Account= " + account);

		if (account == null) {
			System.out.println("No account found");
		}

		// EMPLOYEE,MANAGER,..
		List<String> roles = this.roleDAO.getRole(account.getRoleId());

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();

		if (roles != null) {
			for (String r : roles) {
				// ROLE_EMPLOYEE, ROLE_MANAGER
				GrantedAuthority authority = new SimpleGrantedAuthority(r);
				grantList.add(authority);
			}
		}

		UserDetails userDetails = (UserDetails) new User(account.getUserName(), //
				account.getEncryptedPassword(), grantList);

		return userDetails;
	}

}
