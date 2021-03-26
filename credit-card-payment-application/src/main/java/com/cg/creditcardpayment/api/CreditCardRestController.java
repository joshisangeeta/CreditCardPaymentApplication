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

import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.service.ICreditCardService;

@RestController
@RequestMapping("/creditcards")
public class CreditCardRestController {

	@Autowired
	private ICreditCardService creditCardService;
	
	@GetMapping("/getAllCreditCards")
	public ResponseEntity<List<CreditCardModel>> findAll() {
		return ResponseEntity.ok(creditCardService.findAll());
	}
	
	@GetMapping("/getCreditCard/{cardNumber}")
	public ResponseEntity<CreditCardModel> findById(@PathVariable("cardNumber") String cardNumber){
		ResponseEntity<CreditCardModel> response=null;
		if(!(creditCardService.existsById(cardNumber)) || cardNumber==null) {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(creditCardService.findById(cardNumber),HttpStatus.FOUND);
		}
		return response;
	}
	
	@PostMapping("/addCreditCard")
	public ResponseEntity<String> add(@RequestBody CreditCardModel creditCard) throws CreditCardException {
		
		ResponseEntity<String> response=null;
		if(creditCard==null) {
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			creditCard=creditCardService.add(creditCard);
			response= new ResponseEntity<>("CreditCard is Added",HttpStatus.CREATED);
		}
		return response;
	}
	
	@DeleteMapping("/deleteCreditCard/{cardNumber}")
	public ResponseEntity<Void> deleteUser(@PathVariable("cardNumber") String cardNumber) {
		ResponseEntity<Void> response=null;
		CreditCardModel creditCard=creditCardService.findById(cardNumber);
		if(creditCard==null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			creditCardService.deleteById(cardNumber);
			System.out.println("deleted");
		}
		return response;
	}
	
	@PutMapping("/updateCreditCard")
	public ResponseEntity<CreditCardModel> updateCreditCard(@RequestBody CreditCardModel creditCard) throws CreditCardException{
		
		ResponseEntity<CreditCardModel> response=null;
		if(creditCard==null) {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			creditCard =creditCardService.save(creditCard);
			response =new ResponseEntity<>(creditCard,HttpStatus.OK);
		}
		
		return response;
	}
	
}
