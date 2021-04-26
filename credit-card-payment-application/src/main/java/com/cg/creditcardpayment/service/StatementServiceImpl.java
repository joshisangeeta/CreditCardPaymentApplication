package com.cg.creditcardpayment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.ICustomerRepository;
import com.cg.creditcardpayment.dao.IStatementRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.entity.TransactionEntity;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.StatementModel;


@Service
public class StatementServiceImpl implements IStatementService {
	
	@Autowired
	private IStatementRepository statementRepo;

	@Autowired
	private ICreditCardRepository creditCardRepo;
	
	@Autowired
	private ICustomerRepository customerRepo;
	
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
		if(statement==null) {
			throw new StatementException("Statement cannot be null");
		}
		return parser.parse(statementRepo.save(parser.parse(statement)));
	}

	@Override
	public List<StatementModel> findAll() {
		return statementRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public void deleteById(Long statementId) throws StatementException {
		if(statementId==null) {
			throw new StatementException("Statement Id cannot be null");
		}else if(!statementRepo.existsById(statementId)) {
			throw new StatementException("Statement id "+statementId+" Does not exists");
		}
		statementRepo.deleteById(statementId);
		
	}

	@Override
	public StatementModel findById(Long statementId) throws StatementException {
		if(statementId==null) {
			throw new StatementException("Statement Id cannot be null");
		}else if(!statementRepo.existsById(statementId)) {
			throw new StatementException("Statement id "+statementId+" Does not exists");
		}
		return parser.parse(statementRepo.findById(statementId).orElse(null));
	}

	@Override
	public boolean existsById(Long statementId) throws StatementException {
		if(statementId==null) {
			throw new StatementException("Statement Id cannot be Null");
		}
		return statementRepo.existsById(statementId);
	}

	@Override
	public StatementModel getBilledStatement(String cardNumber) throws CreditCardException, StatementException {
		StatementModel bill=new StatementModel();
		if(cardNumber==null) {
			throw new CreditCardException("Card number cannot be null");
		}
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null) {
			throw new CreditCardException("Credit card "+cardNumber+" Does not Exists");
		}
		if(card.getExpiryDate().isBefore(LocalDate.now())) {
			throw new CreditCardException("Credit Card is Expired, Please request new Credit Card");
		}
		Integer defaultBillDate=24;
		
		bill.setStatementId(0L);
		LocalDate generalBillDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate);
		LocalTime generalBillTime = LocalTime.of(0, 0, 0);
		bill.setBillTime(generalBillTime);
		LocalDate lastBillDate;
		if(LocalDate.now().isAfter(generalBillDate)) {
			lastBillDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate);
			bill.setBillDate(lastBillDate);
		}else if(LocalDate.now().isBefore(generalBillDate)) {
			lastBillDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().minusMonths(1).getMonthValue(), defaultBillDate);
			bill.setBillDate(lastBillDate);
		}else if(LocalDate.now().isEqual(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate))) {
			bill.setBillDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate));
		}
		bill.setCardNumber(cardNumber);
		bill.setDueDate(bill.getBillDate().plusDays(20));
		StatementModel billStatement = null;
		if(!bill.getBillDate().isEqual(LocalDate.now())) {
			if(this.statementHistory(cardNumber).isEmpty()) {
				Set<TransactionEntity> transaction =card.getTransaction();			
				Double usedAmount=transaction.stream().filter(trans->(trans.getTransactionDate().isBefore(generalBillDate) && trans.getTransactionDate().plusMonths(1).isAfter(generalBillDate))).mapToDouble( amo -> amo.getAmount()).sum();
				if(usedAmount!=0.0) {
					Double used=card.getUsedLimit();
					if(used<0) {
						if(usedAmount+used>=0.0) {
							bill.setBillAmount(usedAmount+used);
							bill.setDueAmount(usedAmount+used);
						}else {
							bill.setBillAmount(0.0);
							bill.setDueAmount(0.0);
						}
					}else {
						if(used>=usedAmount) {
							bill.setBillAmount(usedAmount);
							bill.setDueAmount(usedAmount);
						}else {
							bill.setBillAmount(used);
							bill.setDueAmount(used);
						}
					}
					billStatement = parser.parse(statementRepo.save(parser.parse(bill)));
				}else {
					throw new StatementException("No bill statements");
				}
			}else {
				return this.statementHistory(cardNumber).stream().filter(st->st.getBillDate().isEqual(bill.getBillDate())).findFirst().orElse(null);
				
			}
			
		}else {
			Set<TransactionEntity> transaction =card.getTransaction();
			StatementModel billed = this.statementHistory(cardNumber).stream().filter(st->st.getBillDate().isEqual(bill.getBillDate())).findFirst().orElse(null);
			if(billed!=null) {
				return billed;
			}
			Double amount=transaction.stream().filter(trans->trans.getTransactionDate().plusDays(30).isAfter(LocalDate.now())).mapToDouble( amo -> amo.getAmount()).sum();
			Double used=card.getUsedLimit();
			if(used<0) {
				if(amount+used>=0.0) {
					bill.setBillAmount(amount+used);
					bill.setDueAmount(amount+used);
				}else {
					bill.setBillAmount(0.0);
					bill.setDueAmount(0.0);
				}
			}else {
				if(used>=amount) {
					bill.setBillAmount(amount);
					bill.setDueAmount(amount);
				}else {
					bill.setBillAmount(used);
					bill.setDueAmount(used);
				}
			}
			billStatement = parser.parse(statementRepo.save(parser.parse(bill)));
		}
		return billStatement;
	}

	@Override
	public StatementModel getUnBilledStatement(String cardNumber) throws CreditCardException {
		if(cardNumber==null) {
			throw new CreditCardException("Card Number cannot be Null");
		}
		StatementModel unBill=new StatementModel();
		unBill.setStatementId(0L);
		unBill.setCardNumber(cardNumber);
		unBill.setBillTime(LocalTime.now());
		Integer defaultBillDate=24;
		LocalDate generalBillDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate);
		LocalDate lastBillDate = LocalDate.now();
		if(LocalDate.now().isAfter(generalBillDate)) {
			lastBillDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate);
			unBill.setBillDate(lastBillDate);
		}else if(LocalDate.now().isBefore(generalBillDate)) {
			lastBillDate=LocalDate.of(LocalDate.now().getYear(),LocalDate.now().minusMonths(1).getMonthValue(), defaultBillDate);
			unBill.setBillDate(lastBillDate);
		}else if(LocalDate.now().isEqual(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate))) {
			unBill.setBillDate(LocalDate.of(LocalDate.now().getYear(),LocalDate.now().getMonthValue(), defaultBillDate));
		}
		unBill.setBillDate(unBill.getBillDate().plusMonths(1));
		unBill.setDueDate(unBill.getBillDate().plusDays(20));
		CreditCardEntity credit=creditCardRepo.findById(cardNumber).orElse(null);
		
		if(credit==null) {
			throw new CreditCardException("Credit card"+cardNumber+" does not Exists");
		}
		LocalDate bill = lastBillDate;
		Set<TransactionEntity> transaction =credit.getTransaction();	
		Double amount=transaction.stream().filter(trans->(trans.getTransactionDate().isAfter(bill) || trans.getTransactionDate().isEqual(bill))).mapToDouble(amo -> amo.getAmount()).sum();
		
		Double used=credit.getUsedLimit();
		if(used<0) {
			if(amount+used>=0.0) {
				unBill.setBillAmount(amount+used);
				unBill.setDueAmount(amount+used);
			}else {
				unBill.setBillAmount(0.0);
				unBill.setDueAmount(0.0);
			}
		}else {
			if(used<=amount) {
				unBill.setBillAmount(used);
				unBill.setDueAmount(used);
			}else {
				unBill.setBillAmount(amount);
				unBill.setDueAmount(amount);
			}
			
		}

		unBill=parser.parse(statementRepo.save(parser.parse(unBill)));
		statementRepo.deleteById(unBill.getStatementId());
		return unBill;
	}

	@Override
	public List<StatementModel> statementHistory(String cardNumber) throws CreditCardException {
		if(cardNumber==null) {
			throw new CreditCardException("Card Number cannot be Null");
		}
		CreditCardEntity card=creditCardRepo.findById(cardNumber).orElse(null);
		if(card==null) {
			throw new CreditCardException("Credit Card "+cardNumber+" doesnot Exists");
		}
		return card.getStatement().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public List<StatementModel> statementHistoryByUserId(String userId) throws CreditCardException, CustomerException {
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
		
		List<StatementModel> statements= new ArrayList<>();
		
		for(int i=0;i<creditCards.size();i++) {
			statements.addAll(this.statementHistory(creditCards.get(i).getCardNumber()));
		}
		return statements;
	}
	@Override
	public List<StatementModel> getBilledStatementsById(String customerId) throws CreditCardException, CustomerException, StatementException {
		if(customerId==null) {
			throw new CustomerException("UserId cannot be Null");
		}
		CustomerEntity customer=customerRepo.findById(customerId).orElse(null);
		if(customer==null) {
			throw new CustomerException("Customer Does not Exists");
		}else if(customer.getCreditCard().isEmpty()) {
			throw new CreditCardException("No Credit Cards Exists");
		}
		List<CreditCardModel> creditCards = customer.getCreditCard().stream().map(parser::parse).collect(Collectors.toList());
		
		List<StatementModel> statements= new ArrayList<>();
		
		for(int i=0;i<creditCards.size();i++) {
			statements.add(this.getBilledStatement(creditCards.get(i).getCardNumber()));
		}
		if(statements.isEmpty()) {
			throw new StatementException("No statements Exists");
		}
		return statements;
	}

	@Override
	public List<StatementModel> getUnBilledStatementsById(String customerId) throws CreditCardException, CustomerException, StatementException {
		if(customerId==null) {
			throw new CustomerException("Customer Id cannot be Null");
		}
		CustomerEntity customer=customerRepo.findById(customerId).orElse(null);
		if(customer==null) {
			throw new CustomerException("Customer not Exists");
		}else if(customer.getCreditCard().isEmpty()) {
			throw new CreditCardException("Credit Cards not Exists");
		}
		List<CreditCardModel> creditCards = customer.getCreditCard().stream().map(parser::parse).collect(Collectors.toList());
		
		List<StatementModel> statements= new ArrayList<>();
		
		for(int i=0;i<creditCards.size();i++) {
			statements.add(this.getUnBilledStatement(creditCards.get(i).getCardNumber()));
		}
		if(statements.isEmpty()) {
			throw new StatementException("No statements Exists");
		}
		return statements;
	}
	
	
	@Override
	public StatementModel findByBillDate(LocalDate billDate) {
		return parser.parse(statementRepo.findByBillDate(billDate));
	}

	

	

}
