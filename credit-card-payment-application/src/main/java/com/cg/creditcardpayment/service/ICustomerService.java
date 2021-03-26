package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.model.AccountModel;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.CustomerModel;

public interface ICustomerService {

	boolean existsByContactNo(String contactNo);
	boolean existsByEmail(String email);
	boolean existsById(String userId);

	CustomerModel add(CustomerModel customer) throws CustomerException;
	CustomerModel save(CustomerModel customer) throws CustomerException;
	
	void deleteById(String customerId);
	
	CustomerModel findById(String customerId);
	
	List<CustomerModel> findAll();


	boolean addAccount(AccountModel account,String customerId);
	List<AccountModel> getAccounts(String customerId);
	
	boolean addCreditCard(CreditCardModel creditCard,String customerId);
	List<CreditCardModel> getCreditCards(String customerId);
	
	

}
