package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.AccountException;
import com.cg.creditcardpayment.model.AccountModel;

public interface IAccountService {
	
	boolean existsById(String accountNumber);
	
	AccountModel add(AccountModel account) throws AccountException;
	AccountModel save(AccountModel account) throws AccountException;
	
	void deleteById(String accountNumber);
	
	AccountModel findById(String accountNumber);
	
	List<AccountModel> findAll();
}
