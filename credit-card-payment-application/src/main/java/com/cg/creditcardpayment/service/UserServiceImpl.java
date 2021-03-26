package com.cg.creditcardpayment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.IUserRepository;
import com.cg.creditcardpayment.exception.UserException;
import com.cg.creditcardpayment.model.ChangePassword;
import com.cg.creditcardpayment.model.UserModel;


@Service
public class UserServiceImpl implements IUserService {
	
	@Autowired
	private IUserRepository userRepo;

	@Autowired
	private EMParse parser;
	
	public UserServiceImpl() {
		
	}
	
	/**
	 * @param userRepo
	 * @param parser
	 */
	public UserServiceImpl(IUserRepository userRepo) {
		super();
		this.userRepo = userRepo;
		this.parser = new EMParse();
	}

	
	public IUserRepository getUserRepo() {
		return userRepo;
	}

	public void setUserRepo(IUserRepository userRepo) {
		this.userRepo = userRepo;
	}

	public EMParse getParser() {
		return parser;
	}

	public void setParser(EMParse parser) {
		this.parser = parser;
	}

	@Override
	public UserModel add(UserModel user) throws UserException {
		if(user !=null) {
			if(userRepo.existsById(user.getUserId())) {
				throw new UserException("User "+user.getUserId()+" is already Exists");
			}if(!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,}$")) {
				throw new UserException("Password should contain upper case, Lower case, Special charecter, numbers and length minimum 8");
			}
			else {
				user=parser.parse(userRepo.save(parser.parse(user)));
			}
		}
		return user;
	}

	@Override
	public UserModel save(UserModel user) throws UserException {
		UserModel old=parser.parse(userRepo.findById(user.getUserId()).orElse(null));
		if(old == null) {
			throw new UserException("No user with Id "+user.getUserId()+" is present");
		}else if(!user.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&-+=()])(?=\\S+$).{8,}$")) {
			throw new UserException("Password should contain upper case, Lower case, Special charecter, numbers and length minimum 8");
		}else {
			user=parser.parse(userRepo.save(parser.parse(user)));
		}
		return user;
	}

	@Override
	public List<UserModel> findAll() {
		return userRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(String userId) {
		userRepo.deleteById(userId);
		
	}

	@Override
	public UserModel findById(String userId) {
		return parser.parse(userRepo.findById(userId).orElse(null));
	}

	@Override
	public boolean existsById(String userId) {
		return userRepo.existsById(userId);
	}

	@Override
	public boolean signIn(UserModel user) throws UserException {
		return userRepo.findById(user.getUserId()).orElse(null).getPassword().equals(user.getPassword());
	}

	@Override
	public boolean signOut(UserModel user) {
		return true;
	}

	@Override
	public boolean changePassword(ChangePassword changePassword) {
		UserModel user=parser.parse(userRepo.findById(changePassword.getUserId()).orElse(null));
		boolean isChanged=false;
		if(user.getPassword().equals(changePassword.getCurrentPassword())) {
			if(changePassword.getChangePassword().equals(changePassword.getConfirmPassword())) {
				user.setPassword(changePassword.getConfirmPassword());
				userRepo.save(parser.parse(user));
				isChanged= true;
			}
		}
		return isChanged;
	}

}
