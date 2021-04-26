package com.cg.creditcardpayment.api;

import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.creditcardpayment.exception.AccountException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.model.AccountModel;
import com.cg.creditcardpayment.service.IAccountService;

@RestController
@RequestMapping("/home/customer/accounts")
@CrossOrigin("http://localhost:3000")
public class AccountRestController {

	@Autowired
	private IAccountService accountService;
	
	
	@GetMapping()
	public ResponseEntity<List<AccountModel>> findAll() {
		return ResponseEntity.ok(accountService.findAll());
	}
	
	@GetMapping("/{accountnumber}")
	public ResponseEntity<AccountModel> findByAccountNumber(@PathVariable("accountnumber") String accountNumber) throws AccountException{
		ResponseEntity<AccountModel> response=null;
		response=new ResponseEntity<>(accountService.findById(accountNumber),HttpStatus.FOUND);
		return response;
	}
	
	@PostMapping()
	@Transactional
	public ResponseEntity<String> addAccount(@RequestBody @Valid AccountModel account, BindingResult result) throws AccountException {
		ResponseEntity<String> response=null;
		if(result.hasErrors()) {
			throw new AccountException(GlobalExceptionHandler.messageFrom(result));
		}
		if(account==null) {
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			accountService.add(account);
			
			response= new ResponseEntity<>("Account is Added",HttpStatus.CREATED);

		}
		return response;
	}
	
	@DeleteMapping("/{accountnumber}")
	@Transactional
	public ResponseEntity<String> deleteAccount(@PathVariable("accountnumber") String accountNumber) throws AccountException {
		ResponseEntity<String> response=null;
		AccountModel account=accountService.findById(accountNumber);
		if(account==null) {
			response = new ResponseEntity<>("Account Doesnot Exists",HttpStatus.NOT_FOUND);
		}else {
			accountService.deleteById(accountNumber);
			response=new ResponseEntity<>("Account Deleted",HttpStatus.OK);
		}
		return response;
	}
	
	@DeleteMapping("/{customerId}/{accountnumber}")
	@Transactional
	public ResponseEntity<String> deleteAccountByCustomer(@PathVariable("customerId") String customerId,@PathVariable("accountnumber") String accountNumber) throws AccountException, CustomerException {
		ResponseEntity<String> response=null;
		AccountModel account=accountService.findById(accountNumber);
		if(account==null) {
			response = new ResponseEntity<>("Account Doesnot Exists",HttpStatus.NOT_FOUND);
		}else {
			accountService.deleteAccountByCustomer(customerId,accountNumber);
			response=new ResponseEntity<>("Account Deleted",HttpStatus.OK);
		}
		return response;
	}
	
	@PutMapping("/update")
	@Transactional
	public ResponseEntity<AccountModel> updateAccount(@RequestBody @Valid AccountModel account,BindingResult result) throws AccountException{
		ResponseEntity<AccountModel> response=null;
		if(result.hasErrors()) {
			throw new AccountException(GlobalExceptionHandler.messageFrom(result));
		}
		if(account==null) {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			account =accountService.save(account);
			response =new ResponseEntity<>(account,HttpStatus.OK);
		}
		
		return response;
	}
	
	@PostMapping("/{customerId}")
	public ResponseEntity<AccountModel> addAccountToCustomer(@RequestBody @Valid AccountModel account,BindingResult result,@PathVariable("customerId") String customerId) throws AccountException, CustomerException {
		ResponseEntity<AccountModel> response=null;
		if(result.hasErrors()) {
			throw new AccountException(GlobalExceptionHandler.messageFrom(result));
		}
		if(account==null) {
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			account=accountService.addByCustomer(account,customerId);
			response= new ResponseEntity<>(account, HttpStatus.CREATED);
		}
		return response;
	}
	
	@GetMapping("/all/{customerId}")
	public ResponseEntity<Set<AccountModel>> getAllAccountOfCustomer(@PathVariable("customerId") String customerId) throws CustomerException{
		ResponseEntity<Set<AccountModel>> response=null;
		if(customerId==null) {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			response=new ResponseEntity<>(accountService.findAllByCustomerId(customerId),HttpStatus.OK);
		}
		return response;
	}
	
}
