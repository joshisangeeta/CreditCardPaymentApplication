package com.cg.creditcardpayment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.ITransactionRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.TransactionException;
import com.cg.creditcardpayment.model.TransactionModel;
import com.cg.creditcardpayment.model.TransactionStatus;

@Service
public class TransactionServiceImpl implements ITransactionService {
	
	
	private static Long transactionId=100L; 
	@Autowired
	private ITransactionRepository transactionRepo;
	
	@Autowired
	private ICreditCardRepository creditCardRepo;

	@Autowired
	private EMParse parser;
	
	public TransactionServiceImpl() {
		
	}
	
	
	/**
	 * @param transactionRepo
	 * @param parser
	 */
	public TransactionServiceImpl(ITransactionRepository transactionRepo) {
		super();
		this.transactionRepo = transactionRepo;
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
		return parser.parse(transactionRepo.save(parser.parse(transaction)));
	}

	@Override
	public List<TransactionModel> findAll() {
		return transactionRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long transactionId) {
		transactionRepo.deleteById(transactionId);
		
	}

	@Override
	public TransactionModel findById(Long transactionId) {
		return parser.parse(transactionRepo.findById(transactionId).orElse(null));
	}

	@Override
	public boolean existsById(Long transactionId) {
		return transactionRepo.existsById(transactionId);
	}


	@Override
	public TransactionModel transaction(String cardNumber,Double amount,String discription) throws CreditCardException {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null || card.getExpiryDate().isBefore(LocalDate.now())) {
			throw new CreditCardException("card "+card.getCardNumber()+" is invalid");
		}else {
			TransactionModel transact=new TransactionModel();
			transact.setTransactionId(transactionId++);
			transact.setCardNumber(cardNumber);
			transact.setTransactionDate(LocalDate.now());
			transact.setTransactionTime(LocalTime.now());
			transact.setDescription(discription);
			System.out.println(cardNumber+" "+amount);
			if(!(amount+card.getUsedLimit()>=card.getCreditLimit())) {
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
		
	}

}
