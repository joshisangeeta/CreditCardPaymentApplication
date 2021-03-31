package com.cg.creditcardpayment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.StatementException;
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
	@DisplayName("StatementServiceImplTest :: Statement Details should retrive")
	void testGetAll() {
		
		CreditCardEntity creditCard1=new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		List<StatementEntity> testData=Arrays.asList(new StatementEntity[] {
				new StatementEntity(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1),
				new StatementEntity(2L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1)
		});
		
		Mockito.when(statementRepo.findAll()).thenReturn(testData);
		
		List<StatementModel> expected=Arrays.asList(new StatementModel[] {
				new StatementModel(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1.getCardNumber(),creditCard1.getCustomer().getName()),
				new StatementModel(2L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1.getCardNumber(),creditCard1.getCustomer().getName())
		});
		
		List<StatementModel> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	
	@Test
	@DisplayName("StatementServiceImplTest :: Get Statements by Statement Id ")
	void testGetById () throws StatementException {
		CreditCardEntity creditCard1=new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		StatementEntity testdata=new StatementEntity(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1);
		
		StatementModel expected=new StatementModel(1L,25000.0,25000.0,LocalDate.of(2021, 03, 18),LocalDate.of(2021, 04, 8),creditCard1.getCardNumber(),creditCard1.getCustomer().getName());
				
		
		Mockito.when(statementRepo.findById(testdata.getStatementId())).thenReturn(Optional.of(testdata));
		Mockito.when(statementRepo.existsById(testdata.getStatementId())).thenReturn(true);
		
		StatementModel actual=service.findById(testdata.getStatementId());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("StatementServiceImplTest :: Return Null when statement Id not Exists")
	void testGetByIdNull() throws StatementException {		
		
		Mockito.when(statementRepo.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(statementRepo.existsById(1L)).thenReturn(true);
		
		StatementModel actual = service.findById(1L);
		assertNull(actual);
	}
	
	@Test
	@DisplayName("StatementServiceImplTest :: Get Bill Statement should throw Exception When card number is null")
	void BilledStatementShouldDisplayExceptionNull() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.getBilledStatement(null);
		});
	}
	@Test
	@DisplayName("StatementServiceImplTest :: Get Bill Statement should throw Exception When card number is null")
	void BillStatementShouldDisplayExceptionNotExists() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.getBilledStatement("8738974927492");
		});
	}
	@Test
	@DisplayName("StatementServiceImplTest :: Get UnBill Statement should throw Exception When card number is null")
	void UnBilledStatementShouldDisplayExceptionNull() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.getUnBilledStatement(null);
		});
	}
	@Test
	@DisplayName("StatementServiceImplTest :: Get UnBill Statement should throw Exception When card number is null")
	void UnBillStatementShouldDisplayExceptionNotExists() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.getUnBilledStatement("8738974927492");
		});
	}
	@Test
	@DisplayName("StatementServiceImplTest :: statement History should throw Exception When cardNumber is Null ")
	void statementHistoryShouldDisplayException() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.statementHistory(null);
		});
	}
	
	@Test
	@DisplayName("StatementServiceImplTest :: Statement History should throw Exception When cardNumber is not Exists")
	void StatementHistoryShouldDisplayExceptionCardNotFound() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.statementHistory("46576789939329");
		});
	}
	
}
