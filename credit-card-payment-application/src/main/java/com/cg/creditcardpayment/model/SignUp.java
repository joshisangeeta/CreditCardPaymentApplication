package com.cg.creditcardpayment.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class SignUp {
	@NotNull(message="user id cannot be null")	
	@NotBlank(message="user id cannot be blank")
	@Pattern(regexp="^[A-Za-z][A-Za-z0-9]{3,20}$")
	private String userId;
	
	@NotNull(message="Key cannot be null")	
	@NotBlank(message="Key cannot be blank")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,10}$")
	private String key;
	
	@NotNull(message="Create password cannot be null")	
	@NotBlank(message="Create password cannot be blank")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,30}$")
	private String createPassword;
	
	@NotNull(message="confirm password cannot be null")	
	@NotBlank(message="confirm password cannot be blank")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,30}$")
	private String confirmPassword;
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getCreatePassword() {
		return createPassword;
	}
	public void setCreatePassword(String createPassword) {
		this.createPassword = createPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	
	public SignUp() {
		
	}
	
	public SignUp(String userId, String key, String createPassword, String confirmPassword) {
		super();
		this.userId = userId;
		this.key = key;
		this.createPassword = createPassword;
		this.confirmPassword = confirmPassword;
	}
	@Override
	public String toString() {
		return String.format("SignUp [userId=%s, key=%s, createPassword=%s, confirmPassword=%s]", userId, key,
				createPassword, confirmPassword);
	}
	
	

}
