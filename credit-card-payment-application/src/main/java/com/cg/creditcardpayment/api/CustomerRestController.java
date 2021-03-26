package com.cg.creditcardpayment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.CustomerModel;
import com.cg.creditcardpayment.service.ICustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {

	@Autowired
	private ICustomerService customerService;
	
	@GetMapping("/getAllCustomers")
	public ResponseEntity<List<CustomerModel>> findAll() {
		return ResponseEntity.ok(customerService.findAll());
	}
	
	@GetMapping("/getCustomer/{userId}")
	public ResponseEntity<CustomerModel> findById(@PathVariable("userId") String userId){
		ResponseEntity<CustomerModel> response=null;
		if(!customerService.existsById(userId)){
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(customerService.findById(userId),HttpStatus.FOUND);
		}
		return response;
	}
	
	@PostMapping("/addCustomer")
	public ResponseEntity<String> add(@RequestBody CustomerModel customer) throws CustomerException {
		ResponseEntity<String> response=null;
		if(customer==null) {
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			customer=customerService.add(customer);
			response= new ResponseEntity<>("Customer is Added",HttpStatus.CREATED);
		}
		return response;
	}
	
	@DeleteMapping("/deleteCustomer/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
		ResponseEntity<String> response=null;
		CustomerModel customer=customerService.findById(userId);
		if(customer==null) {
			response = new ResponseEntity<>("Customer is not Exists",HttpStatus.NOT_FOUND);
		}else {
			customerService.deleteById(customer.getUserId());
			customerService.deleteById(userId);
			response=new ResponseEntity<>("Customer is Deleted",HttpStatus.OK);
		}
		return response;
	}
	
	@PutMapping("/updateCustomer")
	public ResponseEntity<CustomerModel> updateUser(@RequestBody CustomerModel user) throws CustomerException{
		
		ResponseEntity<CustomerModel> response=null;
		if(user==null) {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			user =customerService.save(user);
			response =new ResponseEntity<>(user,HttpStatus.OK);
		}
		
		return response;
	}
	
	@PostMapping("/addAccount/{customerId}")
	public ResponseEntity<String> addAccount(@RequestBody AccountModel account,@PathVariable("customerId") String customerId) throws AccountException{
		ResponseEntity<String> response=null;
		if(customerService.addAccount(account, customerId)) {
			response = new ResponseEntity<>("Account is Added",HttpStatus.CREATED);
		}else {
			response= new ResponseEntity<>("Account is not Added",HttpStatus.NOT_ACCEPTABLE);
		}
		return response;
	}
	
	@GetMapping("/getAccounts/{customerId}")
	public ResponseEntity<List<AccountModel>> getAccounts(@PathVariable("customerId") String customerId) throws AccountException{
		ResponseEntity<List<AccountModel>> response=null;
		List<AccountModel> accounts=customerService.getAccounts(customerId);
		if(accounts==null) {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(customerService.getAccounts(customerId),HttpStatus.FOUND);
		}
		return response;
	}
	@PostMapping("/addCreditCard/{customerId}")
	public ResponseEntity<String> addCreditCard(@RequestBody CreditCardModel creditCard,@PathVariable("customerId") String customerId) throws AccountException{
		ResponseEntity<String> response=null;
		if(customerService.addCreditCard(creditCard, customerId)) {
			response = new ResponseEntity<>("Credit Card is Added",HttpStatus.CREATED);
		}else {
			response= new ResponseEntity<>("Credit Card is not Added",HttpStatus.NOT_ACCEPTABLE);
		}
		return response;
	}
	
	@GetMapping("/getCreditCards/{customerId}")
	public ResponseEntity<List<CreditCardModel>> getCreditCards(@PathVariable("customerId") String customerId) throws AccountException{
		ResponseEntity<List<CreditCardModel>> response=null;
		List<CreditCardModel> creditCards=customerService.getCreditCards(customerId);
		if(creditCards==null) {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(customerService.getCreditCards(customerId),HttpStatus.FOUND);
		}
		return response;
	}
}
