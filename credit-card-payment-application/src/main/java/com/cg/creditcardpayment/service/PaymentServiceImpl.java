package com.cg.creditcardpayment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.IAccountRepository;
import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.IPaymentRepository;
import com.cg.creditcardpayment.dao.IStatementRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.StatementEntity;
import com.cg.creditcardpayment.exception.PaymentException;
import com.cg.creditcardpayment.model.AccountModel;
import com.cg.creditcardpayment.model.PaymentModel;
import com.cg.creditcardpayment.model.StatementModel;


@Service
public class PaymentServiceImpl implements IPaymentService {
	
	private static Long paymentId=1L;
	
	@Autowired
	private IPaymentRepository paymentRepo;
	
	@Autowired
	private ICreditCardRepository creditCardRepo;
	
	@Autowired
	private IAccountRepository accountRepo;
	
	@Autowired
	private IStatementRepository statementRepo;

	@Autowired
	private IPaymentService paymentService;
	
	@Autowired
	private EMParse parser;
	
	public PaymentServiceImpl() {
		
	}
	
	
	/**
	 * @param paymentRepo
	 * @param parser
	 */
	public PaymentServiceImpl(IPaymentRepository paymentRepo) {
		super();
		this.paymentRepo = paymentRepo;
		this.parser = new EMParse();
	}


	@Override
	public PaymentModel add(PaymentModel payment) throws PaymentException {
		if(payment !=null) {
			if(paymentRepo.existsById(payment.getPaymentId())) {
				throw new PaymentException("Payment "+payment.getPaymentId()+" is already Exists");
			}else {
				System.out.println(payment);
				payment=parser.parse(paymentRepo.save(parser.parse(payment)));
			}
		}
		return payment;
	}

	@Override
	public PaymentModel save(PaymentModel payment) throws PaymentException {
		System.out.println(payment);
		return parser.parse(paymentRepo.save(parser.parse(payment)));
	}

	@Override
	public void deleteById(Long paymentId) {
		paymentRepo.deleteById(paymentId);
	}

	@Override
	public PaymentModel findById(Long paymentId) {
		return parser.parse(paymentRepo.findById(paymentId).orElse(null));
	}

	@Override
	public List<PaymentModel> findAll() {
		return paymentRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}


	@Override
	public boolean existsById(Long paymentId) {
		// TODO Auto-generated method stub
		return paymentRepo.existsById(paymentId);
	}


	@Override
	public List<StatementModel> pendingBills(String cardNumber) {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		
		Set<StatementEntity> statements =card.getStatement();			
	
		List<StatementModel> pendingStatements=statements.stream().filter(state->state.getDueAmount()>=1.0).distinct().map(parser::parse).collect(Collectors.toList());
		
		pendingStatements.sort((st1,st2)->st1.getStatementId().compareTo(st2.getStatementId()));
		
		return pendingStatements;
	}


	@Override
	public PaymentModel payBill(PaymentModel pay, Long statementId,String accountNumber) throws PaymentException {
		CreditCardEntity card=creditCardRepo.findById(pay.getCardNumber()).orElse(null);
		StatementModel statement=card.getStatement().stream().filter(state->state.getStatementId()==statementId).map(parser::parse).findFirst().orElse(null);
		
		PaymentModel payment=new PaymentModel();
		payment.setCardNumber(pay.getCardNumber());
		payment.setMethod(pay.getMethod());
		
		Double accountBalance=accountRepo.findById(accountNumber).orElse(null).getAccountBalance();
		Double amount=pay.getAmount();
		AccountModel account=parser.parse(accountRepo.findById(accountNumber).orElse(null));
		account.setAccountBalance(accountBalance-amount);
		payment.setAmount(amount);
		card.setUsedLimit(card.getUsedLimit()-amount);
		statement.setDueAmount(statement.getDueAmount()-amount);
		accountRepo.save(parser.parse(account));
		payment.setPaidDate(LocalDate.now());
		payment.setPaidTime(LocalTime.now());
		payment.setPaymentId(paymentId++);
		statementRepo.save(parser.parse(statement));
		paymentService.add(payment);
		return payment;
	}


	@Override
	public PaymentModel payBill(PaymentModel pay, Long statementId) throws PaymentException {
		CreditCardEntity card=creditCardRepo.findById(pay.getCardNumber()).orElse(null);
		StatementModel statement=card.getStatement().stream().filter(state->state.getStatementId()==statementId).map(parser::parse).findFirst().orElse(null);
		
		PaymentModel payment=new PaymentModel();
		payment.setPaymentId(paymentId++);
//		payment.setPaymentId(pay.getPaymentId());
		payment.setCardNumber(pay.getCardNumber());
		payment.setMethod(pay.getMethod());
		Double amount=pay.getAmount();
		Double dueAmount=statement.getDueAmount();
		payment.setAmount(amount);
		card.setUsedLimit(card.getUsedLimit()-amount);
		payment.setPaidDate(LocalDate.now());
		payment.setPaidTime(LocalTime.now());
		statement.setDueAmount(dueAmount-amount);
		statementRepo.save(parser.parse(statement));
		paymentService.add(payment);
		return payment;
	}


	@Override
	public List<PaymentModel> paymentHistory(String cardNumber) {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		
		List<PaymentModel> paymentHistory=card.getPayments().stream().map(parser::parse).collect(Collectors.toList());
		return paymentHistory;
	}
	
	

}
