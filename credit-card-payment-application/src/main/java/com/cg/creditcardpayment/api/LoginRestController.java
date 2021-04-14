package com.cg.creditcardpayment.api;

import java.util.List;

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

import com.cg.creditcardpayment.exception.LoginException;
import com.cg.creditcardpayment.model.ChangePassword;
import com.cg.creditcardpayment.model.LoginModel;
import com.cg.creditcardpayment.model.SignUp;
import com.cg.creditcardpayment.service.ILoginService;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginRestController {

	@Autowired
	private ILoginService userService;
	
	
	@GetMapping("/allUsers")
	public ResponseEntity<List<LoginModel>> findAll() {
		return ResponseEntity.ok(userService.findAll());
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<LoginModel> findById(@PathVariable("userId")String userId) throws LoginException {
		return new ResponseEntity<>(userService.findById(userId),HttpStatus.OK);
	}
		
	@PostMapping("/signIn")
	public ResponseEntity<String> signIn(@RequestBody @Valid LoginModel user,BindingResult result) throws LoginException{
		ResponseEntity<String> response=null;
		if (result.hasErrors()) {
			throw new LoginException(GlobalExceptionHandler.messageFrom(result));
		}
		if(userService.existsById(user.getUserId())) {
			if(userService.signIn(user)) {
				response=new ResponseEntity<>("Signed In "+user.getUserId(),HttpStatus.ACCEPTED);
			}else {
				response=new ResponseEntity<>("Login Id and password did not match",HttpStatus.UNAUTHORIZED);
			}		
		}else {
			response=new ResponseEntity<>("User with "+user.getUserId()+" Does not exists",HttpStatus.NOT_FOUND);
		}
		return response;
	}
	
	
	@PostMapping("/user")
	public ResponseEntity<String> add(@RequestBody @Valid LoginModel user,BindingResult result) throws LoginException {
		if (result.hasErrors()) {
			throw new LoginException(GlobalExceptionHandler.messageFrom(result));
		}
		userService.add(user);
		return new ResponseEntity<>("User is Added",HttpStatus.CREATED);
	}
	
	@DeleteMapping("/user/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable("userId") String userId) throws LoginException {
		userService.deleteById(userId);
		return new ResponseEntity<>("User is Deleted",HttpStatus.OK);
	}
	
	@PutMapping("/changePassword")
	public ResponseEntity<String> updateUser(@RequestBody @Valid ChangePassword changePassword,BindingResult result ) throws LoginException {
		ResponseEntity<String> response=null;
		if (result.hasErrors()) {
			throw new LoginException(GlobalExceptionHandler.messageFrom(result));
		}
		if(userService.changePassword(changePassword)) {
			response=new ResponseEntity<>("Password Changed Succesfull!",HttpStatus.ACCEPTED);
		}else {
			response=new ResponseEntity<>("Password not Changed",HttpStatus.NOT_ACCEPTABLE);
		}
		return response;
	}
	
	@PutMapping("/signUp")
	public ResponseEntity<String> signUp(@RequestBody @Valid SignUp signUp, BindingResult result ) throws LoginException {
		ResponseEntity<String> response=null;
		if (result.hasErrors()) {
			throw new LoginException(GlobalExceptionHandler.messageFrom(result));
		}
		userService.signUp(signUp);
		response=new ResponseEntity<>("Signed Up Succesfully",HttpStatus.ACCEPTED);
		return response;
	}
	
}
