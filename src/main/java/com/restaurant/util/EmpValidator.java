package com.restaurant.util;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.restaurant.form.EmpForm;

public class EmpValidator implements Validator {

	private EmailValidator emailValidator = EmailValidator.getInstance();

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz == EmpForm.class;
	}

	@Override
	public void validate(Object target, Errors errors) {
		/*
		 * EmpForm form = (EmpForm) target;
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "empName",
		 * "NotEmpty.empForm.userName");
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "role",
		 * "NotEmpty.appUserForm.firstName");
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email",
		 * "NotEmpty.appUserForm.email");
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password",
		 * "NotEmpty.appUserForm.password");
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword",
		 * "NotEmpty.appUserForm.confirmPassword");
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "gender",
		 * "NotEmpty.appUserForm.gender");
		 * ValidationUtils.rejectIfEmptyOrWhitespace(errors, "countryCode",
		 * "NotEmpty.appUserForm.countryCode");
		 * 
		 * if (!this.emailValidator.isValid(appUserForm.getEmail())) { // Email không
		 * hợp lệ. errors.rejectValue("email", "Pattern.appUserForm.email"); } else if
		 * (appUserForm.getUserId() == null) { AppUser dbUser =
		 * appUserDAO.findAppUserByEmail(appUserForm.getEmail()); if (dbUser != null) {
		 * // Email đã được sử dụng bởi tài khoản khác. errors.rejectValue("email",
		 * "Duplicate.appUserForm.email"); } }
		 * 
		 * if (!errors.hasFieldErrors("userName")) { AppUser dbUser =
		 * appUserDAO.findAppUserByUserName(appUserForm.getUserName()); if (dbUser !=
		 * null) { // Tên tài khoản đã bị sử dụng bởi người khác.
		 * errors.rejectValue("userName", "Duplicate.appUserForm.userName"); } }
		 * 
		 * if (!errors.hasErrors()) { if
		 * (!appUserForm.getConfirmPassword().equals(appUserForm.getPassword())) {
		 * errors.rejectValue("confirmPassword", "Match.appUserForm.confirmPassword"); }
		 * }
		 */

	}

}
