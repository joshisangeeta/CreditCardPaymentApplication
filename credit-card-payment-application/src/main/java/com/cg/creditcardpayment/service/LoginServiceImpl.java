package com.cg.creditcardpayment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ILoginRepository;
import com.cg.creditcardpayment.entity.LoginEntity;
import com.cg.creditcardpayment.exception.LoginException;
import com.cg.creditcardpayment.model.ChangePassword;
import com.cg.creditcardpayment.model.SignUp;
import com.cg.creditcardpayment.model.LoginModel;


@Service
public class LoginServiceImpl implements ILoginService {
	
	@Autowired
	private ILoginRepository userRepo;

	@Autowired
	private EMParse parser;
	
	public LoginServiceImpl() {
		
	}
	
	public LoginServiceImpl(ILoginRepository userRepo) {
		super();
		this.userRepo = userRepo;
		this.parser = new EMParse();
	}

	
	public ILoginRepository getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(ILoginRepository userRepo) {
		this.userRepo = userRepo;
	}

	public EMParse getParser() {
		return parser;
	}

	public void setParser(EMParse parser) {
		this.parser = parser;
	}

	@Override
	public LoginModel add(LoginModel user) throws LoginException {
		if(user !=null) {
			if(userRepo.existsById(user.getUserId())) {
				throw new LoginException("User "+user.getUserId()+" is already Exists");
			}
			if(!user.getUserId().matches("^[A-Za-z][A-Za-z0-9]{3,20}$")) {
				throw new LoginException("UserId should be non empty and minimum of length 4");
			}
			if(!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,}$")) {
				throw new LoginException("Password should contain upper case, Lower case, Special charecter, numbers and length minimum 8");
			}
			else {
				user=parser.parse(userRepo.save(parser.parse(user)));
			}
		}
		return user;
	}

	@Override
	public LoginModel save(LoginModel user) throws LoginException {
		LoginModel old=parser.parse(userRepo.findById(user.getUserId()).orElse(null));
		if(old == null) {
			throw new LoginException("No user with Id "+user.getUserId()+" is present");
		}else if(!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,}$")) {
			throw new LoginException("Password should contain upper case, Lower case, Special charecter, numbers and length minimum 8");
		}else {
			user=parser.parse(userRepo.save(parser.parse(user)));
		}
		return user;
	}

	@Override
	public List<LoginModel> findAll() {
		return userRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(String userId) throws LoginException {
		if(userId==null) {
			throw new LoginException("User Id Cannot be Null");
		}else if(!userRepo.existsById(userId)) {
			throw new LoginException("User with user Id "+userId+" Does not exists");
		}
		userRepo.deleteById(userId);
	}

	@Override
	public LoginModel findById(String userId) throws LoginException {
		if(userId==null) {
			throw new LoginException("User Id Cannot be Null");
		}else if(!userRepo.existsById(userId)) {
			throw new LoginException("User with user Id "+userId+" Does not exists");
		}
		return parser.parse(userRepo.findById(userId).orElse(null));
	}

	@Override
	public boolean existsById(String userId) {
		return userRepo.existsById(userId);
	}

	@Override
	public boolean signIn(LoginModel user) throws LoginException {
		if(user==null) {
			throw new LoginException("SignIn details Cannot be Null");
		}
		LoginEntity userDetails=userRepo.findById(user.getUserId()).orElse(null);
		if(userDetails==null) {
			throw new LoginException("User Details doesnot Exists");
		}
		return userDetails.getPassword().equals(user.getPassword());
	}

	@Override
	public boolean signOut(LoginModel user) {
		return true;
	}

	@Override
	public boolean changePassword(ChangePassword changePassword) throws LoginException {
		if(changePassword==null) {
			throw new LoginException("Change details should not be null");
		}
		LoginModel user=parser.parse(userRepo.findById(changePassword.getUserId()).orElse(null));
		if(user==null) {
			throw new LoginException("User details Does not exists");
		}
		boolean isChanged=false;
		if(user.getPassword().equals(changePassword.getCurrentPassword()) && changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
			user.setPassword(changePassword.getConfirmPassword());
			userRepo.save(parser.parse(user));
			isChanged= true;
		}
		return isChanged;
	}

	@Override
	public LoginModel signUp(SignUp signUp) throws LoginException {
		if(signUp==null) {
			throw new LoginException("SignUp details cannot be Null");
		}
		LoginModel user=parser.parse(userRepo.findById(signUp.getUserId()).orElse(null));
		if(user==null) {
			throw new LoginException("SignUp details Does not Exists");
		}
		if(user.getPassword().equals(signUp.getKey()) && signUp.getCreatePassword().equals(signUp.getConfirmPassword())) {
			user.setPassword(signUp.getConfirmPassword());
			user=parser.parse(userRepo.save(parser.parse(user)));
		}
		return user;
	}

}
