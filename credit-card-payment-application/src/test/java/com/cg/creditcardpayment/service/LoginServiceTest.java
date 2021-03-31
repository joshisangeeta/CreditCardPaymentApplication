package com.cg.creditcardpayment.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

import com.cg.creditcardpayment.dao.ILoginRepository;
import com.cg.creditcardpayment.entity.LoginEntity;
import com.cg.creditcardpayment.exception.LoginException;
import com.cg.creditcardpayment.model.LoginModel;


@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

	@Mock
	private ILoginRepository loginRepo;
	
	@InjectMocks
	private LoginServiceImpl service;
	
	@Test
	@DisplayName("LoginServiceImpl :: All Login Details should be retrieve")
	void testGetAll() {
		
		List<LoginEntity> testData=Arrays.asList(new LoginEntity[] {
				new LoginEntity("U101","Shambhavi@123","USER"),
				new LoginEntity("U102","Sai@1345","USER")
		});
		
		Mockito.when(loginRepo.findAll()).thenReturn(testData);
		
		List<LoginModel> expected=Arrays.asList(new LoginModel[] {
				new LoginModel("U101","Shambhavi@123","USER"),
				new LoginModel("U102","Sai@1345","USER")
		});
		
		List<LoginModel> actual = service.findAll();
		
		assertEquals(expected,actual);

	}
	
	@Test
	@DisplayName("LoginServiceImpl :: Get Login Details when user Id is Given ")
	void testGetById () throws LoginException {
		LoginEntity testdata=new LoginEntity("U101","Shambhavi@123","USER");
		
		LoginModel expected=new LoginModel("U101","Shambhavi@123","USER");
		
		Mockito.when(loginRepo.findById(testdata.getUserId())).thenReturn(Optional.of(testdata));
		Mockito.when(loginRepo.existsById(testdata.getUserId())).thenReturn(true);
		
	
		LoginModel actual=service.findById(testdata.getUserId());
		
		assertEquals(expected,actual);
	}
	
	@Test
	@DisplayName("LoginServiceImpl :: SignIn ")
	void testSignIn () throws LoginException {
		LoginEntity testdata=new LoginEntity("U101","Shambhavi@123","USER");
				
		Mockito.when(loginRepo.findById(testdata.getUserId())).thenReturn(Optional.of(testdata));
		
		boolean signIn=service.signIn(service.getParser().parse(testdata));
		
		assertTrue(signIn);
	}
	@Test
	@DisplayName("LoginServiceImpl :: SignIn Exception because null data as signIn ")
	void testSignInException () throws LoginException {
		assertThrows(LoginException.class, () -> {
			service.signIn(null);
		});
	}
	@Test
	@DisplayName("LoginServiceImpl :: SignUp Exception because null data as signup ")
	void testSignUpException () throws LoginException {
		assertThrows(LoginException.class, () -> {
			service.signUp(null);
		});
	}
	@Test
	@DisplayName("LoginServiceImpl :: Find User Who is not Exists return exception")
	void testFindUserNotExistsException () throws LoginException {
		assertThrows(LoginException.class, () -> {
			service.findById("U101");
		});
	}
	
}
