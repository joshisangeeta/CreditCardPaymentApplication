package com.cg.creditcardpayment.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.creditcardpayment.exception.AccountException;
import com.cg.creditcardpayment.exception.CreditCardException;
import com.cg.creditcardpayment.exception.CustomerException;
import com.cg.creditcardpayment.exception.PaymentException;
import com.cg.creditcardpayment.exception.StatementException;
import com.cg.creditcardpayment.model.PaymentModel;
import com.cg.creditcardpayment.model.StatementModel;
import com.cg.creditcardpayment.service.IPaymentService;

@RestController
@RequestMapping("/home/customer/creditcard/payments")
@CrossOrigin
public class PaymentRestController {

	@Autowired
	private IPaymentService paymentService;
	/**
	 * 
	 * @return
	 */
	@GetMapping()
	public ResponseEntity<List<PaymentModel>> findAll() {
		return ResponseEntity.ok(paymentService.findAll());
	}
	/**
	 * 
	 * @param paymentId
	 * @return
	 * @throws PaymentException
	 */
	@GetMapping("/{paymentId}")
	public ResponseEntity<PaymentModel> findById(@PathVariable("paymentId") Long paymentId) throws PaymentException{
		return ResponseEntity.ok(paymentService.findById(paymentId));
	}
	/**
	 * 
	 * @param cardNumber
	 * @return
	 * @throws CreditCardException
	 */
	@GetMapping("/pendingBills/{cardNumber}")
	public ResponseEntity<List<StatementModel>> getPendingStatements(@PathVariable("cardNumber") String cardNumber) throws CreditCardException{
		return ResponseEntity.ok(paymentService.pendingBills(cardNumber));
	}
	/**
	 * 
	 * @param pay
	 * @param statementId
	 * @return
	 * @throws PaymentException
	 * @throws CreditCardException
	 * @throws StatementException
	 * not recommended
	 */
	@PostMapping("/pendingBills/payUsingUPI/{statementId}")
	public ResponseEntity<PaymentModel> paybill(@RequestBody PaymentModel pay,@PathVariable("statementId") Long statementId) throws PaymentException, CreditCardException, StatementException{
		return ResponseEntity.ok(paymentService.payBill(pay,statementId));
	}
	/**
	 * 
	 * @param pay
	 * @param cardNumber
	 * @return
	 * @throws PaymentException
	 * @throws CreditCardException
	 * @throws StatementException
	 * recommended
	 */
	@PostMapping("/pay/payUsingUPI/{cardNumber}")
	public ResponseEntity<PaymentModel> payUsingUPI(@RequestBody PaymentModel pay,@PathVariable("cardNumber") String cardNumber) throws PaymentException, CreditCardException, StatementException{
		return ResponseEntity.ok(paymentService.payForCreditCard(pay,cardNumber));
	}
	/**
	 * 
	 * @param pay
	 * @param statementId
	 * @param accountNumber
	 * @return
	 * @throws PaymentException
	 * @throws CreditCardException
	 * @throws StatementException
	 * @throws AccountException
	 */
	@PostMapping("/pendingBills/payUsingAccount/{statementId}/{accountNumber}")
	public ResponseEntity<PaymentModel> paybillUsingAccount(@RequestBody PaymentModel pay,@PathVariable("statementId") Long statementId,@PathVariable("accountNumber") String accountNumber) throws PaymentException, CreditCardException, StatementException, AccountException{
		return ResponseEntity.ok(paymentService.payBill(pay,statementId,accountNumber));
	}
	
	@PostMapping("/pay/payUsingAccount/{cardNumber}/{accountNumber}")
	public ResponseEntity<PaymentModel> payUsingAccount(@RequestBody PaymentModel pay,@PathVariable("cardNumber") String cardNumber,@PathVariable("accountNumber") String accountNumber) throws PaymentException, CreditCardException, StatementException, AccountException{
		return ResponseEntity.ok(paymentService.payForCreditCardAccount(pay,cardNumber,accountNumber));
	}
	/**
	 * 
	 * @param cardNumber
	 * @return
	 * @throws CreditCardException
	 */
	@GetMapping("/paymentHistory/{cardNumber}")
	public ResponseEntity<List<PaymentModel>> paymentHistory(@PathVariable("cardNumber") String cardNumber) throws CreditCardException{
		return ResponseEntity.ok(paymentService.paymentHistory(cardNumber));
	}
	
	/**
	 * 
	 * @param customerId
	 * @return
	 * @throws CreditCardException
	 * @throws CustomerException
	 */
	@GetMapping("/paymentHistory/user/{customerId}")
	public ResponseEntity<List<PaymentModel>> paymentHistoryById(@PathVariable("customerId") String customerId) throws CreditCardException, CustomerException{
		return ResponseEntity.ok(paymentService.paymentHistoryById(customerId));
	}
}
