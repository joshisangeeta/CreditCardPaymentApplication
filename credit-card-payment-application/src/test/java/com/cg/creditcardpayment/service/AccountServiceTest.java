package com.cg.creditcardpayment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.cg.creditcardpayment.dao.IAccountRepository;
import com.cg.creditcardpayment.dao.ICustomerRepository;
import com.cg.creditcardpayment.entity.AccountEntity;
import com.cg.creditcardpayment.entity.CustomerEntity;
import com.cg.creditcardpayment.exception.AccountException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.model.AccountModel;
import com.cg.creditcardpayment.model.AccountType;
import com.cg.creditcardpayment.model.AddressModel;
import com.cg.creditcardpayment.model.CustomerModel;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	@Mock
	private IAccountRepository accountRepo;

	@Mock
	private ICustomerRepository customerRepo;
	
	@InjectMocks
	private AccountServiceImpl service;

	@InjectMocks
	private CustomerServiceImpl customerService;
	
	@Test
	@DisplayName("AccountServiceImpl :: All Account Details should be retrieve")
	void testGetAll() {
		List<AccountEntity> testData=Arrays.asList(new AccountEntity[] {
				new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new AccountEntity("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new AccountEntity("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		
		Mockito.when(accountRepo.findAll()).thenReturn(testData);
		
		List<AccountModel> expected=Arrays.asList(new AccountModel[] {
				new AccountModel("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new AccountModel("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new AccountModel("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		
		List<AccountModel> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Account Details should be Added")
	void testAdd() throws AccountException {
		AccountEntity account1=new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Mockito.when(accountRepo.save(account1)).thenReturn(account1);

		AccountModel expected=new AccountModel("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		AccountModel actual = service.add(service.getParser().parse(account1));
		
		assertEquals(expected,actual);

	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Account Details should Throw Exception if Already Exists")
	void testAddException() throws AccountException {
		AccountEntity account1=new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Mockito.when(accountRepo.save(account1)).thenReturn(account1);

		service.add(service.getParser().parse(account1));
		
		AccountModel account2=new AccountModel("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Mockito.when(accountRepo.existsById(account2.getAccountNumber())).thenReturn(true);
		assertThrows(AccountException.class, () -> {
			service.add(account2);
		});
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Account Details should to be delete by Account Number")
	void testDelete() throws AccountException {
		AccountEntity account1=new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		
		Mockito.when(accountRepo.save(account1)).thenReturn(account1);

		AccountModel added = service.add(service.getParser().parse(account1));
		
		Mockito.doNothing().when(accountRepo).deleteById(added.getAccountNumber());
		Mockito.when(accountRepo.existsById(account1.getAccountNumber())).thenReturn(true);
		service.deleteById(added.getAccountNumber());
		boolean test=service.existsById(added.getAccountNumber());
		
		assertTrue(test);
		
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Account Details should retrieve by using Account Number")
	void testGetById () throws AccountException {
		AccountEntity testdata=new AccountEntity("42356879564","Sai",90000.0,AccountType.SAVINGS);
		
		AccountModel expected=new AccountModel("42356879564","Sai",90000.0,AccountType.SAVINGS);
	
		Mockito.when(accountRepo.findById(testdata.getAccountNumber())).thenReturn(Optional.of(testdata));
	
		AccountModel actual=service.findById(testdata.getAccountNumber());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Account Details should return Null when Account Number not Exists")
	void testGetByIdNull() throws AccountException {		

		Mockito.when(accountRepo.findById("425631257892")).thenReturn(Optional.empty());
		
		AccountModel actual = service.findById("425631257892");
		assertNull(actual);
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: return Exception when Account Details is Added By Customer where Customer Id is Null")
	void testAddByCustomer() throws AccountException, CustomerException {
		AccountModel account=new AccountModel("42356879562","Venkata sai",50000.0,AccountType.SAVINGS);
		assertThrows(CustomerException.class, () -> {
			service.addByCustomer(account,null);
		});
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: return All Accounts of Customer when Customer provide his Id")
	void testFindAllByCustomerId() throws AccountException, CustomerException {
		AddressModel address1=new AddressModel("10-10A","kbr","Jublihills","Hydrabad","Telangana",500055);
		
		CustomerEntity customer1=new CustomerEntity("U107","Venkata","venkatasai1479@gmail.com","7207388240",LocalDate.parse("1999-10-18"),address1);

		List<AccountEntity> testData=Arrays.asList(new AccountEntity[] {
				new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new AccountEntity("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new AccountEntity("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		List<AccountModel> expected=Arrays.asList(new AccountModel[] {
				new AccountModel("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new AccountModel("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new AccountModel("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		Mockito.when(customerRepo.save(customer1)).thenReturn(customer1);
		Mockito.when(customerRepo.findById(customer1.getUserId())).thenReturn(Optional.of(customer1));
		CustomerModel customer = customerService.addCustomer(service.getParser().parse(customer1),customer1.getUserId());
		
		customer1.setAccounts(testData.stream().collect(Collectors.toSet()));
		
		Set<AccountModel> actual=service.findAllByCustomerId(customer.getUserId());
		
		assertEquals(expected.stream().collect(Collectors.toSet()),actual);
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Throw Exception when Customer provide wrong id when he want to get all his accounts")
	void testFindAllByCustomerIdNotExists() throws AccountException, CustomerException {
		assertThrows(CustomerException.class, () -> {
			service.findAllByCustomerId("U500");
		});
	}
	
	@Test
	@DisplayName("AccountServiceImpl :: Throw Exception when try to Delete Account by Customer when Account Number is not Exists")
	void testDeleteAccountByCustomer() throws AccountException, CustomerException {
		AddressModel address1=new AddressModel("10-10A","kbr","Jublihills","Hydrabad","Telangana",500055);
		
		CustomerEntity customer1=new CustomerEntity("U107","Venkata","venkatasai1479@gmail.com","7207388240",LocalDate.parse("1999-10-18"),address1);

		List<AccountEntity> testData=Arrays.asList(new AccountEntity[] {
				new AccountEntity("42356879562","Venkata sai",50000.0,AccountType.SAVINGS),
				new AccountEntity("42356879563","Venkata",40000.0,AccountType.SAVINGS),
				new AccountEntity("42356879564","Sai",90000.0,AccountType.CURRENT)
		});
		Mockito.when(customerRepo.save(customer1)).thenReturn(customer1);
		Mockito.when(customerRepo.findById(customer1.getUserId())).thenReturn(Optional.of(customer1));
		CustomerModel customer = customerService.addCustomer(service.getParser().parse(customer1),customer1.getUserId());
		customer1.setAccounts(testData.stream().collect(Collectors.toSet()));
		
		assertThrows(AccountException.class, () -> {
			service.deleteAccountByCustomer(customer.getUserId(),"42367890987");
		});
	}
	
	
}
