package com.cg.creditcardpayment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.model.StatementModel;
import com.cg.creditcardpayment.service.IStatementService;

@RestController
@RequestMapping("/home/customer/creditcard/statements")
@CrossOrigin
public class StatementRestController {

	@Autowired
	private IStatementService statementService;
	
	@GetMapping()
	public ResponseEntity<List<StatementModel>> findAll() {
		return ResponseEntity.ok(statementService.findAll());
	}
	
	@GetMapping("/{statementId}")
	public ResponseEntity<StatementModel> findById(@PathVariable("statementId") Long statementId) throws StatementException{
		return ResponseEntity.ok(statementService.findById(statementId));
	}
	
	@GetMapping("/generateBill/{cardNumber}")
	public ResponseEntity<StatementModel> getBill(@PathVariable("cardNumber") String cardNumber) throws CreditCardException, StatementException{
		return ResponseEntity.ok(statementService.getBilledStatement(cardNumber));
	}
	
	@GetMapping("/generateUnBilled/{cardNumber}")
	public ResponseEntity<StatementModel> getUnBill(@PathVariable("cardNumber") String cardNumber) throws CreditCardException{
		return ResponseEntity.ok(statementService.getUnBilledStatement(cardNumber));
	}
	@GetMapping("/generateBill/user/{customerId}")
	public ResponseEntity<List<StatementModel>> getBilluser(@PathVariable("customerId") String customerId) throws CreditCardException, CustomerException, StatementException{
		return ResponseEntity.ok(statementService.getBilledStatementsById(customerId));
	}
	
	@GetMapping("/generateUnBilled/user/{customerId}")
	public ResponseEntity<List<StatementModel>> getUnBilluser(@PathVariable("customerId") String customerId) throws CreditCardException, CustomerException, StatementException{
		return ResponseEntity.ok(statementService.getUnBilledStatementsById(customerId));
	}
	@GetMapping("/history/{cardNumber}")
	public ResponseEntity<List<StatementModel>> statementHistory(@PathVariable("cardNumber") String cardNumber) throws CreditCardException {
		return ResponseEntity.ok(statementService.statementHistory(cardNumber));
	}
	@GetMapping("/history/user/{userId}")
	public ResponseEntity<List<StatementModel>> statementHistoryByUserId(@PathVariable("userId") String userId) throws CreditCardException, CustomerException {
		return ResponseEntity.ok(statementService.statementHistoryByUserId(userId));
	}
	
}
