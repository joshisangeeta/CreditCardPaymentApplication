package com.cg.creditcardpayment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.exception.TransactionException;
import com.cg.creditcardpayment.model.TransactionModel;
import com.cg.creditcardpayment.service.ITransactionService;

@RestController
@RequestMapping("/home/customer/creditcard/transactions")
@CrossOrigin
public class TransactionRestController {

	@Autowired
	private ITransactionService transactionService;
	/**
	 * 
	 * @return
	 */
	@GetMapping()
	public ResponseEntity<List<TransactionModel>> findAll() {
		return ResponseEntity.ok(transactionService.findAll());
	}
	/**
	 * 
	 * @param transactionId
	 * @return
	 * @throws TransactionException
	 */
	@GetMapping("/{transactionId}")
	public ResponseEntity<TransactionModel> findById(@PathVariable("transactionId") Long transactionId) throws TransactionException{
		ResponseEntity<TransactionModel> response=null;
		if(!(transactionService.existsById(transactionId)) || transactionId==null) {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(transactionService.findById(transactionId),HttpStatus.FOUND);
		}
		return response;
	}
	/**
	 * 
	 * @param transaction
	 * @return
	 * @throws TransactionException
	 */
	@PostMapping()
	public ResponseEntity<TransactionModel> add(@RequestBody TransactionModel transaction) throws TransactionException {
		transaction=transactionService.add(transaction);
		return ResponseEntity.ok(transaction);
	}
	/**
	 * 
	 * @param transaction
	 * @return
	 * @throws TransactionException
	 */
	@PutMapping("/updateTransaction")
	public ResponseEntity<TransactionModel> updateUser(@RequestBody TransactionModel transaction) throws TransactionException{
		transaction =transactionService.save(transaction);
		return ResponseEntity.ok(transaction);
	}
	/**
	 * 
	 * @param cardNumber
	 * @param amount
	 * @param description
	 * @return
	 * @throws CreditCardException
	 */
	@GetMapping("/transact/{cardNumber}/{amount}/{description}")
	public ResponseEntity<TransactionModel> transact(@PathVariable("cardNumber") String cardNumber,@PathVariable("amount") Double amount,@PathVariable("description") String description) throws CreditCardException {
		return ResponseEntity.ok(transactionService.transaction(cardNumber, amount, description));
	}
	/**
	 * 
	 * @param cardNumber
	 * @return
	 * @throws CreditCardException
	 */
	@GetMapping("/history/{cardNumber}")
	public ResponseEntity<List<TransactionModel>> transactionHistory(@PathVariable("cardNumber") String cardNumber) throws CreditCardException {
		return ResponseEntity.ok(transactionService.transactionHistory(cardNumber));
	}
	
	@GetMapping("/history/user/{userId}")
	public ResponseEntity<List<TransactionModel>> transactionHistoryById(@PathVariable("userId") String userId) throws CreditCardException, TransactionException, CustomerException {
		return ResponseEntity.ok(transactionService.transactionHistoryById(userId));
	}
}
