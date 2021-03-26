package com.cg.creditcardpayment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.TransactionException;
import com.cg.creditcardpayment.model.TransactionModel;
import com.cg.creditcardpayment.service.ITransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionRestController {

	@Autowired
	private ITransactionService transactionService;
	
	@GetMapping("/getAll")
	public ResponseEntity<List<TransactionModel>> findAll() {
		return ResponseEntity.ok(transactionService.findAll());
	}
	
	@GetMapping("/getTransaction/{transactionId}")
	public ResponseEntity<TransactionModel> findById(@PathVariable("transactionId") Long transactionId){
		return ResponseEntity.ok(transactionService.findById(transactionId));
	}
	
	@PostMapping("/addTransaction")
	public ResponseEntity<TransactionModel> add(@RequestBody TransactionModel transaction) throws TransactionException {
		transaction=transactionService.add(transaction);
		System.out.println(transaction.getDescription()+" ");
		return ResponseEntity.ok(transaction);
	}
	
	@PutMapping("/updateTransaction")
	public ResponseEntity<TransactionModel> updateUser(@RequestBody TransactionModel transaction) throws TransactionException{
		transaction =transactionService.save(transaction);
		return ResponseEntity.ok(transaction);
		//new ResponseEntity<>(transaction, HttpStatus.OK);
	}
	
	@GetMapping("/transact/{cardNumber}/{amount}/{description}")
	public ResponseEntity<TransactionModel> transact(@PathVariable("cardNumber") String cardNumber,@PathVariable("amount") Double amount,@PathVariable("description") String description) throws CreditCardException {
		System.out.println("Hello"+cardNumber+" "+amount+" "+description);
		return ResponseEntity.ok(transactionService.transaction(cardNumber, amount, description));
	}
	
}
