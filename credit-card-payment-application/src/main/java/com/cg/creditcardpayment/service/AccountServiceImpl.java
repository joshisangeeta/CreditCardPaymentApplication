package com.cg.creditcardpayment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.IAccountRepository;
import com.cg.creditcardpayment.exception.AccountException;
import com.cg.creditcardpayment.model.AccountModel;


@Service
public class AccountServiceImpl implements IAccountService {
	
	@Autowired
	private IAccountRepository accountRepo;

	@Autowired
	private EMParse parser;
	
	public AccountServiceImpl() {
		
	}
	
	/**
	 * @param accountRepo
	 */
	public AccountServiceImpl(IAccountRepository accountRepo) {
		super();
		this.accountRepo = accountRepo;
		this.parser=new EMParse();
	}

	public IAccountRepository getAccountRepo() {
		return accountRepo;
	}

	public void setAccountRepo(IAccountRepository accountRepo) {
		this.accountRepo = accountRepo;
	}

	public EMParse getParser() {
		return parser;
	}

	public void setParser(EMParse parser) {
		this.parser = parser;
	}

	@Override
	public AccountModel add(AccountModel account) throws AccountException {
		if(account !=null) {
			if(accountRepo.existsById(account.getAccountNumber())) {
				throw new AccountException("Account "+account.getAccountNumber()+" is already Exists");
			}else {
				account=parser.parse(accountRepo.save(parser.parse(account)));
			}
		}
		return account;
	}

	@Override
	public AccountModel save(AccountModel account) throws AccountException {
		return parser.parse(accountRepo.save(parser.parse(account)));
	}

	@Override
	public void deleteById(String accountNumber) {
		accountRepo.deleteById(accountNumber);
	}

	@Override
	public AccountModel findById(String accountNumber) {
		System.out.println("accountNumber "+accountNumber);
		return parser.parse(accountRepo.findById(accountNumber).orElse(null));
	}

	@Override
	public List<AccountModel> findAll() {
		return accountRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public boolean existsById(String accountNumber) {
		return accountRepo.existsById(accountNumber);
	}

}
