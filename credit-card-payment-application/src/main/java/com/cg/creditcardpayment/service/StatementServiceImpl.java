package com.cg.creditcardpayment.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.IStatementRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.TransactionEntity;
import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.model.StatementModel;


@Service
public class StatementServiceImpl implements IStatementService {
	
	private static Long statementId=101L;
	@Autowired
	private IStatementRepository statementRepo;

	@Autowired
	private ICreditCardRepository creditCardRepo;
	
	@Autowired
	private EMParse parser;
	
	public StatementServiceImpl() {
		
	}
	
	public StatementServiceImpl(IStatementRepository statementRepo) {
		super();
		this.statementRepo = statementRepo;
		this.parser = new EMParse();
	}

	@Override
	public StatementModel add(StatementModel statement) throws StatementException {
		if(statement !=null) {
			if(statementRepo.existsById(statement.getStatementId())) {
				throw new StatementException("Statement "+statement.getStatementId()+" is already Exists");
			}else {
				statement=parser.parse(statementRepo.save(parser.parse(statement)));
			}
		}
		return statement;
	}

	@Override
	public StatementModel save(StatementModel statement) throws StatementException {
		return parser.parse(statementRepo.save(parser.parse(statement)));
	}

	@Override
	public List<StatementModel> findAll() {
		return statementRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long statementId) {
		statementRepo.deleteById(statementId);
		
	}

	@Override
	public StatementModel findById(Long statementId) {
		return parser.parse(statementRepo.findById(statementId).orElse(null));
	}

	@Override
	public boolean existsById(Long statementId) {

		return statementRepo.existsById(statementId);
	}

	@Override
	public StatementModel getBilledStatement(String cardNumber) {
		StatementModel bill=new StatementModel();
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card!=null && card.getCardExpiry().isAfter(LocalDate.now())) {
			bill.setStatementId(statementId++);
			bill.setBillDate(LocalDate.now());
			bill.setCardNumber(cardNumber);
			bill.setDueDate(LocalDate.now().plusDays(20));
			Double amount=0.0;
			
			Set<TransactionEntity> transaction =card.getTransaction();			
			
			amount=transaction.stream().filter(trans->trans.getTransactionDate().plusDays(30).isAfter(LocalDate.now())).mapToDouble(amo -> amo.getAmount()).sum();
			bill.setDueAmount(amount);
			bill.setBillAmount(amount);
			statementRepo.save(parser.parse(bill));
			return bill;
		}
		return null;
	}

	@Override
	public StatementModel getUnBilledStatement(String cardNumber) {
		StatementModel unBill=new StatementModel();
		unBill.setStatementId(statementId);
		unBill.setCardNumber(cardNumber);
		LocalDate lastBillDate=statementRepo.findAll().get(statementRepo.findAll().size()-1).getDueDate();
		unBill.setBillDate(lastBillDate.plusDays(30));
		unBill.setDueDate(unBill.getBillDate().plusDays(20));
		Double amount=0.0;
		Set<TransactionEntity> transaction =creditCardRepo.findById(cardNumber).orElse(null).getTransaction();	
		
		amount=transaction.stream().filter(trans->trans.getTransactionDate().isAfter(lastBillDate.plusDays(1))).mapToDouble(amo -> amo.getAmount()).sum();
		
		unBill.setDueAmount(amount);
		unBill.setBillAmount(amount);
		return unBill;
	}

	@Override
	public List<StatementModel> statementHistory(String cardNumber) {
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		
		List<StatementModel> statementHistory=card.getStatement().stream().map(parser::parse).collect(Collectors.toList());
		return statementHistory;
	}

}
