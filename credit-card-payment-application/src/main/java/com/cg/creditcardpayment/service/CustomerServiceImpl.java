package com.cg.creditcardpayment.service;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.ICustomerRepository;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.model.AccountModel;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.CustomerModel;


@Service
public class CustomerServiceImpl implements ICustomerService {
	
	@Autowired
	private ICustomerRepository customerRepo;
	
	@Autowired
	private ICreditCardRepository creditCardRepo;
	
	@Autowired
	private EMParse parser;
	
	
	/**
	 * @param customerRepo
	 * @param parser
	 */
	public CustomerServiceImpl(ICustomerRepository customerRepo) {
		super();
		this.customerRepo = customerRepo;
		this.parser = new EMParse();
	}

	
	public ICustomerRepository getCustomerRepo() {
		return customerRepo;
	}


	public void setCustomerRepo(ICustomerRepository customerRepo) {
		this.customerRepo = customerRepo;
	}


	public EMParse getParser() {
		return parser;
	}


	public void setParser(EMParse parser) {
		this.parser = parser;
	}


	@Override
	@Transactional
	public CustomerModel add(CustomerModel customer) throws CustomerException {
		if(customer !=null) {
			if(customerRepo.existsById(customer.getUserId())) {
				throw new CustomerException("Customer "+customer.getUserId()+" is already Exists");
			}else if (customerRepo.existsByContactNo(customer.getContactNo())) {
				throw new CustomerException("Customer with number "+customer.getContactNo()+" is already Exists");
			}else if (customerRepo.existsByEmail(customer.getEmail())) {
				throw new CustomerException("Customer with email "+customer.getEmail()+" is already Exists");
			}else {
				customer=parser.parse(customerRepo.save(parser.parse(customer)));
			}
		}
		return customer;
	}

	@Override
	@Transactional
	public CustomerModel save(CustomerModel customer) throws CustomerException {
		return parser.parse(customerRepo.save(parser.parse(customer)));
	}

	@Override
	@Transactional
	public void deleteById(String userId) {
		System.out.println(userId);
		customerRepo.deleteById(userId);
	}

	@Override
	public CustomerModel findById(String userId) {
		return parser.parse(customerRepo.findById(userId).orElse(null));
	}

	@Override
	public List<CustomerModel> findAll() {
		return customerRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public boolean existsByContactNo(String contactNo) {
		return customerRepo.existsByContactNo(contactNo);
	}

	@Override
	public boolean existsByEmail(String email) {
		return customerRepo.existsByEmail(email);
	}

	@Override
	public boolean existsById(String userId) {
		// TODO Auto-generated method stub
		return customerRepo.existsById(userId);
	}


	@Override
	public boolean addAccount(AccountModel account,String customerId) {
		CustomerEntity customer=customerRepo.findById(customerId).orElse(null);
		boolean isAdded=false;
		if(account!=null) {
			customer.getAccounts().add(parser.parse(account));
			customer.setAccounts(customer.getAccounts());
			customerRepo.save(customer);
			isAdded=true;
		}
		return isAdded;
	}


	@Override
	public List<AccountModel> getAccounts(String customerId) {
		CustomerEntity customer=customerRepo.findById(customerId).orElse(null);
		if(customer ==null) {
			return null;
		}
		return customer.getAccounts().stream().map(parser::parse).collect(Collectors.toList());
	}


	@Override
	public boolean addCreditCard(CreditCardModel creditCard, String customerId) {
		CustomerEntity customer=customerRepo.findById(customerId).orElse(null);
		boolean isAdded=false;
		if(creditCard!=null) {
			creditCardRepo.save(parser.parse(creditCard));
			customer.getCreditCard().add(parser.parse(creditCard));
			customer.setCreditCard(customer.getCreditCard());
			customerRepo.save(customer);
			isAdded=true;
		}
		return isAdded;
	}


	@Override
	public List<CreditCardModel> getCreditCards(String customerId) {
		CustomerEntity customer=customerRepo.findById(customerId).orElse(null);
		if(customer ==null) {
			return null;
		}
		return customer.getCreditCard().stream().map(parser::parse).collect(Collectors.toList());
	}

}
