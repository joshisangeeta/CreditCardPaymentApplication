package com.cg.creditcardpayment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.ICustomerRepository;
import com.cg.creditcardpayment.dao.IStatementRepository;
import com.cg.creditcardpayment.dao.ITransactionRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.entity.StatementEntity;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.exception.TransactionException;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.TransactionModel;
import com.cg.creditcardpayment.model.TransactionStatus;

@Service
public class TransactionServiceImpl implements ITransactionService {
	
	@Autowired
	private ITransactionRepository transactionRepo;
	
	@Autowired
	private ICreditCardRepository creditCardRepo;
	
	@Autowired
	private IStatementRepository statementRepo;
	
	@Autowired
	private ICustomerRepository customerRepo;

	@Autowired
	private EMParse parser;
	
	public TransactionServiceImpl() {
		
	}

	public TransactionServiceImpl(ITransactionRepository transactionRepo,ICreditCardRepository creditCardRepo) {
		super();
		this.transactionRepo = transactionRepo;
		this.creditCardRepo=creditCardRepo;
		this.parser = new EMParse();
	}

	

	public ITransactionRepository getTransactionRepo() {
		return transactionRepo;
	}


	public void setTransactionRepo(ITransactionRepository transactionRepo) {
		this.transactionRepo = transactionRepo;
	}


	public EMParse getParser() {
		return parser;
	}


	public void setParser(EMParse parser) {
		this.parser = parser;
	}


	@Override
	public TransactionModel add(TransactionModel transaction) throws TransactionException {
		if(transaction !=null) {
			if(transactionRepo.existsById(transaction.getTransactionId())) {
				throw new TransactionException("Transaction "+transaction.getTransactionId()+" is already Exists");
			}else {
				
				transaction=parser.parse(transactionRepo.save(parser.parse(transaction)));
			}
		}
		return transaction;
	}

	@Override
	public TransactionModel save(TransactionModel transaction) throws TransactionException {
		if(transaction==null) {
			throw new TransactionException("transaction details cannot be null");
		}
		return parser.parse(transactionRepo.save(parser.parse(transaction)));
	}

	@Override
	public List<TransactionModel> findAll() {
		return transactionRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long transactionId) throws TransactionException {
		if(transactionId==null) {
			throw new TransactionException("transaction Id cannot be Null");
		}else if(!transactionRepo.existsById(transactionId)) {
			throw new TransactionException("Transaction with Transaction Id "+transactionId+" Does not Exists");
		}
		transactionRepo.deleteById(transactionId);
	}

	@Override
	public TransactionModel findById(Long transactionId) throws TransactionException {
		if(transactionId==null) {
			throw new TransactionException("transaction Id cannot be Null");
		}else if(!transactionRepo.existsById(transactionId)) {
			throw new TransactionException("Transaction with Transaction Id "+transactionId+" Does not Exists");
		}
		return parser.parse(transactionRepo.findById(transactionId).orElse(null));
	}

	@Override
	public boolean existsById(Long transactionId) throws TransactionException {
		if(transactionId==null) {
			throw new TransactionException("transaction Id can't be Null");
		}
		return transactionRepo.existsById(transactionId);
	}


	@Override
	public TransactionModel transaction(String cardNumber,Double amount,String discription) throws CreditCardException {
		if(cardNumber==null) {
			throw new CreditCardException("Card Number cannot be Null");
		}
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null) {
			throw new CreditCardException("Card Details should not be Null");
		}
		if(card.getExpiryDate().isBefore(LocalDate.now())) {
			throw new CreditCardException("card "+card.getCardNumber()+" is invalid");
		}
		TransactionModel transact=new TransactionModel();
		transact.setTransactionId(0L);
		transact.setCardNumber(cardNumber);
		transact.setTransactionDate(LocalDate.now());
		transact.setTransactionTime(LocalTime.now());
		transact.setDescription(discription);
		if(amount+card.getUsedLimit()<card.getCreditLimit()) {
			transact.setAmount(amount);
			card.setUsedLimit(amount+card.getUsedLimit());
			transact.setStatus(TransactionStatus.SUCCESSFUL);
		}else {
			transact.setAmount(0.0);
			card.setUsedLimit(card.getUsedLimit());
			transact.setStatus(TransactionStatus.FAILED);
		}
		transact=parser.parse(transactionRepo.save(parser.parse(transact)));
		return transact;
	}
	
	@Override
	public List<TransactionModel> transactionHistory(String cardNumber) throws CreditCardException {
		if(cardNumber==null) {
			throw new CreditCardException("Credit card number should not be Null");
		}
		return transactionRepo.findAll().stream().filter(tran->tran.getCardNumber().equals(cardNumber)).map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public List<TransactionModel> transactionHistoryById(String userId) throws TransactionException, CustomerException, CreditCardException {
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
		
		List<TransactionModel> transactions= new ArrayList<>();
		
		for(int i=0;i<creditCards.size();i++) {
			transactions.addAll(this.transactionHistory(creditCards.get(i).getCardNumber()));
		}
		return transactions;
	}

	@Override
	public List<TransactionModel> transactionHistoryForBill(Long statementId ) throws TransactionException, CreditCardException, StatementException {
		if(statementId==null) {
			throw new StatementException("StatementId should not be Null");
		}
		StatementEntity statement = statementRepo.findById(statementId).orElse(null);
		if(statement==null) {
			throw new StatementException("statement Does not exists");
		}
		CreditCardEntity card=creditCardRepo.findById(statement.getCreditCard().getCardNumber()).orElse(null);
		if(card==null) {
			throw new CreditCardException("Credit card Not Found");
		}
		return transactionRepo.findAll().stream().filter(tran->tran.getCardNumber().equals(card.getCardNumber())).filter(trans->trans.getTransactionDate().isBefore(statement.getBillDate()) && trans.getTransactionDate().plusMonths(1).isAfter(statement.getBillDate())).map(parser::parse).collect(Collectors.toList());

	}
}
