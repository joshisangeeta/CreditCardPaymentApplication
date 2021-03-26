package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.UserException;
import com.cg.creditcardpayment.model.ChangePassword;
import com.cg.creditcardpayment.model.UserModel;

public interface IUserService {

	boolean existsById(String userId);
	
	
	UserModel add(UserModel user) throws UserException;	
	UserModel save(UserModel user) throws UserException;
	
	boolean signIn(UserModel user) throws UserException;	
	boolean signOut(UserModel user);
	
	void deleteById(String userId);
	
	UserModel findById(String userId);
	List<UserModel> findAll();
	
	boolean changePassword(ChangePassword changePassword);
	
//	UserModel signUp();
	
}
