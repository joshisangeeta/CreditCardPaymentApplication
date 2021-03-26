package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.model.StatementModel;

public interface IStatementService {

	boolean existsById(Long statementId);
	
	
	StatementModel add(StatementModel statement) throws StatementException;
	StatementModel save(StatementModel statement) throws StatementException;
	
	void deleteById(Long statementId);
	
	StatementModel findById(Long StatementId);
	
	List<StatementModel> findAll();
	

	StatementModel getBilledStatement(String cardNumber);
	StatementModel getUnBilledStatement(String cardNumber);
	
	List<StatementModel> statementHistory(String cardNumber);
}
