package com.cg.creditcardpayment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.cg.creditcardpayment.dao.IPaymentRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.entity.PaymentEntity;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.PaymentException;
import com.cg.creditcardpayment.model.CardName;
import com.cg.creditcardpayment.model.CardType;
import com.cg.creditcardpayment.model.PaymentMethod;
import com.cg.creditcardpayment.model.PaymentModel;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private IPaymentRepository paymentRepo;
	
	@InjectMocks
	private PaymentServiceImpl service;

	@Test
	@DisplayName("PaymentServiceImplTest :: All Payment Details should retrive")
	void testGetAll() {
		CreditCardEntity creditCard1=new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		List<PaymentEntity> testData=Arrays.asList(new PaymentEntity[] {
				new PaymentEntity(1L,PaymentMethod.UPI,LocalDate.now(),LocalTime.now(),6000.0,creditCard1),
				new PaymentEntity(2L,PaymentMethod.UPI,LocalDate.now(),LocalTime.now(),7000.0,creditCard1)
		});
		
		Mockito.when(paymentRepo.findAll()).thenReturn(testData);
		
		List<PaymentModel> expected=Arrays.asList(new PaymentModel[] {
				new PaymentModel(1L,PaymentMethod.UPI,6000.0,LocalDate.now(),LocalTime.now(),creditCard1.getCardNumber()),
				new PaymentModel(2L,PaymentMethod.UPI,7000.0,LocalDate.now(),LocalTime.now(),creditCard1.getCardNumber())
		});
		
		List<PaymentModel> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	
	@Test
	@DisplayName("PaymentServiceImplTest :: View Payment Details By Payment Id ")
	void testGetById () throws PaymentException {
		CreditCardEntity creditCard1=new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		PaymentEntity testdata=new PaymentEntity(1L,PaymentMethod.UPI,LocalDate.now(),LocalTime.now(),6000.0,creditCard1);
		
		PaymentModel expected=new PaymentModel(1L,PaymentMethod.UPI,6000.0,LocalDate.now(),LocalTime.now(),creditCard1.getCardNumber());
		
		
		Mockito.when(paymentRepo.findById(testdata.getPaymentId())).thenReturn(Optional.of(testdata));
		Mockito.when(paymentRepo.existsById(testdata.getPaymentId())).thenReturn(true);
	
		PaymentModel actual=service.findById(testdata.getPaymentId());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("PaymentServiceImplTest :: Return Null When payment doesnt exists")
	void testGetByIdNull() throws PaymentException {		
		
		Mockito.when(paymentRepo.findById(1L)).thenReturn(Optional.empty());
		Mockito.when(paymentRepo.existsById(1L)).thenReturn(true);
		PaymentModel actual = service.findById(1L);
		assertNull(actual);
	}
	
	
	
	@Test
	@DisplayName("PaymentServiceImplTest :: Payment Details should throw Exception When getting pending bill cardNumber null")
	void paymentDetailsShouldDisplayException() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.pendingBills(null);
		});
	}
	
	@Test
	@DisplayName("PaymentServiceImplTest :: Payment Bill should throw Exception When payment details are null")
	void paymentBillUPIShouldDisplayException() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.payBill(null,1L);
		});
	}
	
	@Test
	@DisplayName("PaymentServiceImplTest :: Payment Bill should throw Exception When account details are null")
	void paymentBillAccountShouldDisplayException() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.payBill(null,1L,null);
		});
	}
	
	@Test
	@DisplayName("PaymentServiceImplTest :: Payment History should throw Exception When cardNumber is Null ")
	void paymentHistoryShouldDisplayException() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.paymentHistory(null);
		});
	}
	
	@Test
	@DisplayName("PaymentServiceImplTest :: Payment History should throw Exception When cardNumber is not Exists")
	void paymentHistoryShouldDisplayExceptionCardNotFound() throws CreditCardException {
		assertThrows(Exception.class, () -> {
			service.paymentHistory("46576789939329");
		});
	}
	
}
