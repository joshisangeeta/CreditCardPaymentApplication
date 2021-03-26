package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.model.CreditCardModel;

public interface ICreditCardService {


	boolean existsById(String cardNumber);
	
	CreditCardModel add(CreditCardModel creditCard) throws CreditCardException;
	CreditCardModel save(CreditCardModel creditCard) throws CreditCardException;
	
	void deleteById(String cardNumber);
	
	CreditCardModel findById(String cardNumber);
	
	List<CreditCardModel> findAll();

}
