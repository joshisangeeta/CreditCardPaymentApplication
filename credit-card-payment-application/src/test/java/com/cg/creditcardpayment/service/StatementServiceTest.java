package com.cg.creditcardpayment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.creditcardpayment.dao.IStatementRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.entity.StatementEntity;
import com.cg.creditcardpayment.model.CardName;
import com.cg.creditcardpayment.model.CardType;
import com.cg.creditcardpayment.model.StatementModel;

@ExtendWith(MockitoExtension.class)
class StatementServiceTest {

	@Mock
	private IStatementRepository statementRepo;
	
	@InjectMocks
	private StatementServiceImpl service;

	@Test
	@DisplayName("StatementDetails should retrive")
	void testGetAll() {
		
		CreditCardEntity creditCard1=new CreditCardEntity("2568479632140",CardName.VISA,CardType.Gold,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		List<StatementEntity> testData=Arrays.asList(new StatementEntity[] {
				new StatementEntity(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1),
				new StatementEntity(2L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1)
		});
		
		Mockito.when(statementRepo.findAll()).thenReturn(testData);
		
		List<StatementModel> expected=Arrays.asList(new StatementModel[] {
				new StatementModel(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1.getCardNumber()),
				new StatementModel(2L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1.getCardNumber())
		});
		
		List<StatementModel> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	
	@Test
	@DisplayName("get by Id ")
	void testGetById () {
		CreditCardEntity creditCard1=new CreditCardEntity("2568479632140",CardName.VISA,CardType.Gold,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		StatementEntity testdata=new StatementEntity(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1);
		
		StatementModel expected=new StatementModel(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1.getCardNumber());
				
		
		Mockito.when(statementRepo.findById(testdata.getStatementId())).thenReturn(Optional.of(testdata));
	
		StatementModel actual=service.findById(testdata.getStatementId());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("get by id return null")
	void testGetByIdNull() {		
		
		Mockito.when(statementRepo.findById(1L)).thenReturn(Optional.empty());
		
		StatementModel actual = service.findById(1L);
		assertNull(actual);
	}
	
}