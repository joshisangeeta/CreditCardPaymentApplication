package com.cg.creditcardpayment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.IAccountRepository;
import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.ICustomerRepository;
import com.cg.creditcardpayment.dao.IPaymentRepository;
import com.cg.creditcardpayment.dao.IStatementRepository;
import com.cg.creditcardpayment.entity.AccountEntity;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.entity.StatementEntity;
import com.cg.creditcardpayment.exception.AccountException;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.exception.PaymentException;
import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.PaymentModel;
import com.cg.creditcardpayment.model.StatementModel;


@Service
public class PaymentServiceImpl implements IPaymentService {
	
	@Autowired
	private IPaymentRepository paymentRepo;
	
	@Autowired
	private ICreditCardRepository creditCardRepo;
	
	@Autowired
	private ICustomerRepository customerRepo;
	
	@Autowired
	private IAccountRepository accountRepo;
	
	@Autowired
	private IStatementRepository statementRepo;

	@Autowired
	private IPaymentService paymentService;
	/**
	 * 
	 */
	@Autowired
	private EMParse parser;
	
	public PaymentServiceImpl() {
		
	}

	/**
	 * 
	 * @param paymentRepo
	 */
	public PaymentServiceImpl(IPaymentRepository paymentRepo) {
		super();
		this.paymentRepo = paymentRepo;
		this.parser = new EMParse();
	}


	/**
	 * @param PaymentModel Object
	 * @return PaymentModel Object
	 */
	@Override
	public PaymentModel add(PaymentModel payment) throws PaymentException {
		if(payment !=null) {
			if(paymentRepo.existsById(payment.getPaymentId())) {
				throw new PaymentException("Payment "+payment.getPaymentId()+" is already Exists");
			}else {
				payment=parser.parse(paymentRepo.save(parser.parse(payment)));
			}
		}
		return payment;
	}

	/**
	 * @param PaymentModel Object
	 * @return PaymentModel Object
	 */
	@Override
	public PaymentModel save(PaymentModel payment) throws PaymentException {
		if(payment==null) {
			throw new PaymentException("Payment can not be Null");
		}
		return parser.parse(paymentRepo.save(parser.parse(payment)));
	}

	/**
	 * @param paymentId Long
	 */
	@Override
	public void deleteById(Long paymentId) throws PaymentException {
		if(paymentId==null) {
			throw new PaymentException("payment Id can not be null");
		}else if(!paymentRepo.existsById(paymentId)) {
			throw new PaymentException("Payment Id "+paymentId+" Does not Exist");
		}
		paymentRepo.deleteById(paymentId);
	}

	/**
	 * @param paymentId Long
	 * @return PaymentModel Object
	 */
	@Override
	public PaymentModel findById(Long paymentId) throws PaymentException {
		if(paymentId==null) {
			throw new PaymentException("payment Id can not be null");
		}else if(!paymentRepo.existsById(paymentId)) {
			throw new PaymentException("Payment Id "+paymentId+" Does not Exist");
		}
		return parser.parse(paymentRepo.findById(paymentId).orElse(null));
	}

	/**
	 * @return List of PaymentModel Object
	 */
	@Override
	public List<PaymentModel> findAll() {
		return paymentRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}


	/**
	 * @return boolean
	 */
	@Override
	public boolean existsById(Long paymentId) {
		return paymentRepo.existsById(paymentId);
	}


	/**
	 * @param cardNumber String
	 * @return List of StatementModel Object
	 */
	@Override
	public List<StatementModel> pendingBills(String cardNumber) throws CreditCardException {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(cardNumber==null) {
			throw new CreditCardException("Card Number can not be null");
		}else if(card==null) {
			throw new CreditCardException("Card does not Exists");
		}		
		Set<StatementEntity> statements =card.getStatement();			
	
		List<StatementModel> pendingStatements=statements.stream().filter(state->state.getDueAmount()>=0.0001).distinct().map(parser::parse).collect(Collectors.toList());
		
		pendingStatements.sort((st1,st2)->st1.getStatementId().compareTo(st2.getStatementId()));
		
		return pendingStatements;
	}


	/**
	 * @param PaymentModel Object
	 * @param statementId Long
	 * @param accountNumber String
	 * @return PaymentModel Object
	 * 
	 * @throws PaymentException, CreditCardException, StatementException, AccountException
	 * 
	 */
	@Override
	public PaymentModel payBill(PaymentModel pay, Long statementId,String accountNumber) throws PaymentException, CreditCardException, StatementException, AccountException {
		
		CreditCardEntity card=creditCardRepo.findById(pay.getCardNumber()).orElse(null);
		
		if(card==null) {
			throw new CreditCardException("Credit card does not Exists");
		}
		
		StatementModel statement=card.getStatement().stream().filter(state->state.getStatementId().equals(statementId)).map(parser::parse).findFirst().orElse(null);
		if(statementId==null) {
			throw new StatementException("StatementId can not be Null");
		}else if(statement==null) {
			throw new StatementException("Statement Does not ");
		}
		Long paymentId=0L;
		if(!this.findAll().isEmpty()) {
			 PaymentModel payId = this.findAll().stream().max(Comparator.comparingLong(PaymentModel::getPaymentId)).orElse(null);
			if(payId!=null) {
				paymentId = payId.getPaymentId();
			}
		}
		PaymentModel payment=new PaymentModel();
		payment.setCardNumber(pay.getCardNumber());
		payment.setMethod(pay.getMethod());
		
		if(accountNumber==null) {
			throw new AccountException("account number can not be null");
		}
		AccountEntity acc=accountRepo.findById(accountNumber).orElse(null);
		if(acc==null) {
			throw new AccountException("Account Does not Exists");
		}
		Double accountBalance=acc.getAccountBalance();
		
		Double amount=pay.getAmount();
		
		if(accountBalance-amount<0) {
			throw new AccountException("Balanace Insufficient");
		}else if(accountBalance-amount>=0) {
			accountBalance-=amount;
			acc.setAccountBalance(accountBalance);
			accountRepo.save(acc);
		}
		payment.setAmount(amount);
		card.setUsedLimit(card.getUsedLimit()-amount);
		if(statement.getDueAmount()-amount>=0.0) {
			statement.setDueAmount(statement.getDueAmount()-amount);
		}else {
			statement.setDueAmount(0.0);
		}
		payment.setPaidDate(LocalDate.now());
		payment.setPaidTime(LocalTime.now());
		payment.setPaymentId(paymentId+1);
		statementRepo.save(parser.parse(statement));
		paymentService.add(payment);
		return payment;
	}
	
	@Override
	public PaymentModel payForCreditCardAccount(PaymentModel pay, String cardNumber, String accountNumber) throws PaymentException, CreditCardException, StatementException, AccountException {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null) {
			throw new CreditCardException("Card does not exists");
		}
		Long paymentId=0L;
		if(!this.findAll().isEmpty()) {
			 PaymentModel payId = this.findAll().stream().max(Comparator.comparingLong(PaymentModel::getPaymentId)).orElse(null);
			if(payId!=null) {
				paymentId = payId.getPaymentId();
			}
		}
		if(accountNumber==null) {
			throw new AccountException("account number can not be null");
		}
		AccountEntity acc=accountRepo.findById(accountNumber).orElse(null);
		if(acc==null) {
			throw new AccountException("Account Does not Exists");
		}
		Double accountBalance=acc.getAccountBalance();
		PaymentModel payment=new PaymentModel();
		Double amount=pay.getAmount();
		payment.setAmount(amount);
		if(accountBalance-amount<0) {
			throw new AccountException("Balanace Insufficient");
		}
		if(accountBalance-amount>=0) {
			accountBalance-=amount;
			acc.setAccountBalance(accountBalance);
			accountRepo.save(acc);
		}
		payment.setPaymentId(paymentId+1);
		payment.setCardNumber(cardNumber);
		payment.setMethod(pay.getMethod());
		
		card.setUsedLimit(card.getUsedLimit()-amount);
		payment.setPaidDate(LocalDate.now());
		payment.setPaidTime(LocalTime.now());
		paymentService.add(payment);
		return payment;
	}


	@Override
	public PaymentModel payBill(PaymentModel pay, Long statementId) throws PaymentException, CreditCardException, StatementException{
		CreditCardEntity card=creditCardRepo.findById(pay.getCardNumber()).orElse(null);
		if(card==null) {
			throw new CreditCardException("Card does not exists");
		}
		StatementModel statement=card.getStatement().stream().filter(state->state.getStatementId().equals(statementId)).map(parser::parse).findFirst().orElse(null);
		if(statement==null) {
			throw new StatementException("Statement Does not exists");
		}
		Long paymentId=0L;
		if(!this.findAll().isEmpty()) {
			 PaymentModel payId = this.findAll().stream().max(Comparator.comparingLong(PaymentModel::getPaymentId)).orElse(null);
			if(payId!=null) {
				paymentId = payId.getPaymentId();
			}
		}
		PaymentModel payment=new PaymentModel();
		payment.setPaymentId(paymentId+1);
		payment.setCardNumber(pay.getCardNumber());
		payment.setMethod(pay.getMethod());
		Double amount=pay.getAmount();
		Double dueAmount=statement.getDueAmount();
		payment.setAmount(amount);
		card.setUsedLimit(card.getUsedLimit()-amount);
		payment.setPaidDate(LocalDate.now());
		payment.setPaidTime(LocalTime.now());
		if(dueAmount-amount>=0.0) {
			statement.setDueAmount(dueAmount-amount);
		}else {
			statement.setDueAmount(0.0);
		}
		statementRepo.save(parser.parse(statement));
		paymentService.add(payment);
		return payment;
	}

	@Override
	public PaymentModel payForCreditCard(PaymentModel pay, String cardNumber) throws PaymentException, CreditCardException, StatementException{
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null) {
			throw new CreditCardException("Credit Card does not exists");
		}
		Long paymentId=0L;
		if(!this.findAll().isEmpty()) {
			 PaymentModel payId = this.findAll().stream().max(Comparator.comparingLong(PaymentModel::getPaymentId)).orElse(null);
			if(payId!=null) {
				paymentId = payId.getPaymentId();
			}
		}
		PaymentModel payment=new PaymentModel();
		payment.setPaymentId(paymentId+1);
		payment.setCardNumber(cardNumber);
		payment.setMethod(pay.getMethod());
		Double amount=pay.getAmount();
		payment.setAmount(amount);
		card.setUsedLimit(card.getUsedLimit()-amount);
		payment.setPaidDate(LocalDate.now());
		payment.setPaidTime(LocalTime.now());
		paymentService.add(payment);
		return payment;
	}

	@Override
	public List<PaymentModel> paymentHistory(String cardNumber) throws CreditCardException {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(cardNumber==null) {
			throw new CreditCardException("Card number can not be null");
		}else if(card==null) {
			throw new CreditCardException("Credit Card does not exists");
		}
		return card.getPayments().stream().map(parser::parse).collect(Collectors.toList());
	}
	
	@Override
	public List<PaymentModel> paymentHistoryById(String userId) throws CustomerException, CreditCardException {
		if(userId==null) {
			throw new CustomerException("UserId cannot be Null");
		}
		CustomerEntity customer=customerRepo.findById(userId).orElse(null);
		if(customer==null) {
			throw new CustomerException("Customer Does not Exists");
		}else if(customer.getCreditCard().isEmpty()) {
			throw new CreditCardException("No Credit Cards Exists");
		}
		List<CreditCardModel> creditCards = customer.getCreditCard().stream().map(parser::parse).collect(Collectors.toList());
		
		List<PaymentModel> payments= new ArrayList<>();
		
		for(int i=0;i<creditCards.size();i++) {
			payments.addAll(this.paymentHistory(creditCards.get(i).getCardNumber()));
		}
		return payments;
	}

	

}
