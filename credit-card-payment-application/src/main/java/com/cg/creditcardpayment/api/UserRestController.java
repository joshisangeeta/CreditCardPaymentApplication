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

import com.cg.creditcardpayment.exception.UserException;
import com.cg.creditcardpayment.model.ChangePassword;
import com.cg.creditcardpayment.model.CustomerModel;
import com.cg.creditcardpayment.model.UserModel;
import com.cg.creditcardpayment.service.ICustomerService;
import com.cg.creditcardpayment.service.IUserService;

@RestController
@RequestMapping("/users")
public class UserRestController {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private ICustomerService customerService;
	
	@GetMapping("/getAllUsers")
	public ResponseEntity<List<UserModel>> findAll() {
		return ResponseEntity.ok(userService.findAll());
	}
	
	@GetMapping("/getUser/{userId}")
	public ResponseEntity<UserModel> findById(@PathVariable("userId")String userId) throws UserException{
		ResponseEntity<UserModel> response=null;
		if(!(userService.existsById(userId)) || userId==null) {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response=new ResponseEntity<>(userService.findById(userId),HttpStatus.FOUND);
		}
		return response;
	}
		
	@PostMapping("/signIn")
	public ResponseEntity<CustomerModel> signIn(@RequestBody UserModel user) throws UserException{
		ResponseEntity<CustomerModel> response=null;
		if(userService.existsById(user.getUserId())) {
			CustomerModel customer=customerService.findById(user.getUserId());
			if(userService.signIn(user)) {
				response=new ResponseEntity<>(customer,HttpStatus.ACCEPTED);
			}else {
				response=new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
			}		
		}else {
			response=new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	
	@PostMapping("/addUser")
	public ResponseEntity<String> add(@RequestBody UserModel user) throws UserException {
		ResponseEntity<String> response=null;
		if(user==null) {
			response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}else {
			user=userService.add(user);
			response= new ResponseEntity<>("User is Added",HttpStatus.CREATED);
		}
		return response;
	}
	
	@DeleteMapping("/deleteUser/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) {
		ResponseEntity<String> response=null;
		UserModel user=userService.findById(userId);
		if(user==null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			userService.deleteById(userId);
			response = new ResponseEntity<>("User is Deleted",HttpStatus.OK);
		}
		return response;
	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<String> updateUser(@RequestBody ChangePassword changePassword ) throws UserException{
		ResponseEntity<String> response=null;
		UserModel user=userService.findById(changePassword.getUserId());
		if(user!=null) {
			if(userService.changePassword(changePassword)) {
				response=new ResponseEntity<>("Password Changed Succesfull!",HttpStatus.ACCEPTED);
			}else {
				response=new ResponseEntity<>("Password not Changed",HttpStatus.NOT_ACCEPTABLE);
			}
		}else {
			response=new ResponseEntity<>(HttpStatus.NO_CONTENT);	
		}
		return response;
	}
	
}
