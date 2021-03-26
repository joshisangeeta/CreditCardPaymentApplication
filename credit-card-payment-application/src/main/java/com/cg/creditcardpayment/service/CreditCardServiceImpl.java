package com.cg.creditcardpayment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.model.CreditCardModel;


@Service
public class CreditCardServiceImpl implements ICreditCardService {
	
	@Autowired
	private ICreditCardRepository creditCardRepo;

	@Autowired
	private EMParse parser;
	
	public CreditCardServiceImpl() {
		
	}
	
	/**
	 * @param creditCardRepo
	 * @param parser
	 */
	public CreditCardServiceImpl(ICreditCardRepository creditCardRepo) {
		super();
		this.creditCardRepo = creditCardRepo;
		this.parser = new EMParse();
	}

	public ICreditCardRepository getCreditCardRepo() {
		return creditCardRepo;
	}

	public void setCreditCardRepo(ICreditCardRepository creditCardRepo) {
		this.creditCardRepo = creditCardRepo;
	}

	public EMParse getParser() {
		return parser;
	}

	public void setParser(EMParse parser) {
		this.parser = parser;
	}

	@Override
	public CreditCardModel add(CreditCardModel creditCard) throws CreditCardException {
		if(creditCard !=null) {
			if(creditCardRepo.existsById(creditCard.getCardNumber())) {
				throw new CreditCardException("CreditCard "+creditCard.getCardNumber()+" is already Exists");
			}else {
				creditCard=parser.parse(creditCardRepo.save(parser.parse(creditCard)));
			}
		}
		return creditCard;
	}

	@Override
	public CreditCardModel save(CreditCardModel creditCard) throws CreditCardException {
		// TODO Auto-generated method stub
		return parser.parse(creditCardRepo.save(parser.parse(creditCard)));
	}

	@Override
	public void deleteById(String creditCardId) {
		creditCardRepo.deleteById(creditCardId);
	}

	@Override
	public CreditCardModel findById(String creditCardId) {
		return parser.parse(creditCardRepo.findById(creditCardId).orElse(null));
	}

	@Override
	public List<CreditCardModel> findAll() {
		return creditCardRepo.findAll().stream().map(parser::parse).collect(Collectors.toList());
	}

	@Override
	public boolean existsById(String cardNumber) {

		return creditCardRepo.existsById(cardNumber);
	}

}
