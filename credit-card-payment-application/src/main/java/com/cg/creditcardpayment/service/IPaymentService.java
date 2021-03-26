package com.cg.creditcardpayment.service;

import java.util.List;

import com.cg.creditcardpayment.exception.PaymentException;
import com.cg.creditcardpayment.model.PaymentModel;
import com.cg.creditcardpayment.model.StatementModel;

public interface IPaymentService {
	
	boolean existsById(Long paymentId);

	PaymentModel add(PaymentModel payment) throws PaymentException;
	PaymentModel save(PaymentModel payment) throws PaymentException;

	void deleteById(Long paymentId);
	
	PaymentModel findById(Long paymentId);
	List<PaymentModel> findAll();
	
	PaymentModel payBill(PaymentModel payment,Long statementId,String accountNumber)throws PaymentException;
	PaymentModel payBill(PaymentModel payment,Long statementId) throws PaymentException;
	List<StatementModel> pendingBills(String cardNumber);
	List<PaymentModel> paymentHistory (String cardNumber);
	
}
