package com.cg.creditcardpayment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cg.creditcardpayment.dao.ICreditCardRepository;
import com.cg.creditcardpayment.dao.ICustomerRepository;
import com.cg.creditcardpayment.entity.CreditCardEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.model.AddressModel;
import com.cg.creditcardpayment.model.CardName;
import com.cg.creditcardpayment.model.CardType;
import com.cg.creditcardpayment.model.CreditCardModel;
import com.cg.creditcardpayment.model.CustomerModel;


@ExtendWith(MockitoExtension.class)
class CreditCardServiceTest {

	@Mock
	private ICreditCardRepository creditCardRepo;
	
	@InjectMocks
	private CreditCardServiceImpl service;
	
	@Mock
	private ICustomerRepository customerRepo;
	
	@InjectMocks
	private CustomerServiceImpl customerService;

	@Test
	@DisplayName("CreditCardServiceImpl :: All Credit Card Details should be retrieve")
	void testGetAll() {
		
		List<CreditCardEntity> testData=Arrays.asList(new CreditCardEntity[] {
				new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity()),
				new CreditCardEntity("2568479632444",CardName.VISA,CardType.GOLD,LocalDate.parse("2023-05-22"),"SBI",528,10000.0,10000.0,new CustomerEntity())
		});
		
		Mockito.when(creditCardRepo.findAll()).thenReturn(testData);
		
		List<CreditCardModel> expected=Arrays.asList(new CreditCardModel[] {
				new CreditCardModel("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity().getUserId()),
				new CreditCardModel("2568479632444",CardName.VISA,CardType.GOLD,LocalDate.parse("2023-05-22"),"SBI",528,10000.0,10000.0,new CustomerEntity().getUserId())
		});
		
		List<CreditCardModel> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Get Credit Card Details when Card Number is Given ")
	void testGetById () throws CreditCardException {
		CreditCardEntity testdata=new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity());
		
		CreditCardModel expected=new CreditCardModel("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity().getUserId());
		
		Mockito.when(creditCardRepo.findById(testdata.getCardNumber())).thenReturn(Optional.of(testdata));
		Mockito.when(creditCardRepo.existsById(testdata.getCardNumber())).thenReturn(true);
		
	
		CreditCardModel actual=service.findById(testdata.getCardNumber());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Return Null When Adding Credit Card with Null")
	void testAddCreditCardWithNull() throws CreditCardException {
		assertNull(service.add(null));
	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Throw Exception When Try to Delete Credit Card with Null")
	void testDeleteCreditCardWithNull() throws CreditCardException {
		assertThrows(CreditCardException.class, () -> {
			service.deleteById(null);
		});
	}
	
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Return Null for Credit Card Details when Card Number not Exists")
	void testGetByIdNull() throws CreditCardException {		
		
		Mockito.when(creditCardRepo.findById("425631257892")).thenReturn(Optional.empty());
		Mockito.when(creditCardRepo.existsById("425631257892")).thenReturn(true);
		
		CreditCardModel actual = service.findById("425631257892");
		assertNull(actual);
	
	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Throw Exception for Adding Credit Card Details When Null given")
	void testAddCreditCardByCustomerException() throws CreditCardException,CustomerException {
		assertThrows(CustomerException.class, () -> {
			service.addToCustomer(null,"U500");
		});
	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: return All Credit Cards of Customer when Customer provide his Id")
	void testFindAllByCustomerId() throws CustomerException, CreditCardException {
		AddressModel address1=new AddressModel("10-10A","kbr","Jublihills","Hydrabad","Telangana",500055);
		
		CustomerEntity customer1=new CustomerEntity("U107","Venkata","venkatasai1479@gmail.com","7207388240",LocalDate.parse("1999-10-18"),address1);

		List<CreditCardEntity> testData=Arrays.asList(new CreditCardEntity[] {
				new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity()),
				new CreditCardEntity("2568479632444",CardName.VISA,CardType.GOLD,LocalDate.parse("2023-05-22"),"SBI",528,10000.0,10000.0,new CustomerEntity())
		});
		List<CreditCardModel> expected=Arrays.asList(new CreditCardModel[] {
				new CreditCardModel("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity().getUserId()),
				new CreditCardModel("2568479632444",CardName.VISA,CardType.GOLD,LocalDate.parse("2023-05-22"),"SBI",528,10000.0,10000.0,new CustomerEntity().getUserId())
		});
		Mockito.when(customerRepo.save(customer1)).thenReturn(customer1);
		Mockito.when(customerRepo.findById(customer1.getUserId())).thenReturn(Optional.of(customer1));
		CustomerModel customer = customerService.addCustomer(service.getParser().parse(customer1),customer1.getUserId());
		
		customer1.setCreditCard(testData.stream().collect(Collectors.toSet()));
		
		Set<CreditCardModel> actual=service.findByCustomerId(customer.getUserId());
		
		assertEquals(expected.stream().collect(Collectors.toSet()),actual);
	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Throw Exception when Customer provide wrong id when he want to get all his Credit Card Details")
	void testFindAllByCustomerIdNotExists() throws CustomerException {
		assertThrows(CustomerException.class, () -> {
			service.findByCustomerId("U500");
		});
	}
	
	@Test
	@DisplayName("CreditCardServiceImpl :: Throw Exception when try to Delete CreditCard by Customer when Credit Card Number is not Exists")
	void testDeleteCreditCardByCustomer() throws CustomerException {
		AddressModel address1=new AddressModel("10-10A","kbr","Jublihills","Hydrabad","Telangana",500055);
		
		CustomerEntity customer1=new CustomerEntity("U107","Venkata","venkatasai1479@gmail.com","7207388240",LocalDate.parse("1999-10-18"),address1);

		List<CreditCardEntity> testData=Arrays.asList(new CreditCardEntity[] {
				new CreditCardEntity("2568479632140",CardName.VISA,CardType.GOLD,LocalDate.parse("2022-10-18"),"SBI",623,10000.0,10000.0,new CustomerEntity()),
				new CreditCardEntity("2568479632444",CardName.VISA,CardType.GOLD,LocalDate.parse("2023-05-22"),"SBI",528,10000.0,10000.0,new CustomerEntity())
		});
		Mockito.when(customerRepo.save(customer1)).thenReturn(customer1);
		Mockito.when(customerRepo.findById(customer1.getUserId())).thenReturn(Optional.of(customer1));
		CustomerModel customer = customerService.addCustomer(service.getParser().parse(customer1),customer1.getUserId());
		customer1.setCreditCard(testData.stream().collect(Collectors.toSet()));
		
		assertThrows(CreditCardException.class, () -> {
			service.deleteCreditCardOfCustomer(customer.getUserId(),"42362357890987");
		});
	}
	
}
