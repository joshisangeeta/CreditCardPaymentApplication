package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.LoginException;
import com.cg.creditcardpayment.model.ChangePassword;
import com.cg.creditcardpayment.model.SignUp;
import com.cg.creditcardpayment.model.LoginModel;

public interface ILoginService {

	boolean existsById(String userId);
	
	
	LoginModel add(LoginModel user) throws LoginException;	
	LoginModel save(LoginModel user) throws LoginException;
	
	boolean signIn(LoginModel user) throws LoginException;	
	boolean signOut(LoginModel user);
	
	void deleteById(String userId) throws LoginException;
	
	LoginModel findById(String userId) throws LoginException;
	List<LoginModel> findAll();
	
	boolean changePassword(ChangePassword changePassword) throws LoginException;
	
	LoginModel signUp(SignUp signUp) throws LoginException;
	
}
