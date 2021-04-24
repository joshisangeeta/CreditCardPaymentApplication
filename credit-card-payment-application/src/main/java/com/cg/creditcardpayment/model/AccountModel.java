package com.cg.creditcardpayment.model;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AccountModel {
	
	@NotNull(message="Account name cannot be null")	
	@NotBlank(message="Account name cannot be blank")
	@Pattern(regexp="^[1-9][0-9]{8,17}$",message="Enter a valid account number")
	private String accountNumber;
	
	@NotNull(message="Account name cannot be null")	
	@NotBlank(message="Account name cannot be blank")
	@Pattern(regexp="^[A-Z a-z]{6,30}$",message="Enter the name according bank records")
	private String accountName;
	
	@NotNull(message="balance cannot be null")
	@DecimalMin(value="0.1",message="Account Balance should not be less than 0")
	private Double accountBalance;
	
	@NotNull(message="account type cannot be null")	
	private AccountType accountType;
	
	
	public AccountModel() {
		/* Default Constructor */
	}
	
	public AccountModel(String accountNumber, String accountName, Double accountBalance, AccountType accountType) {
		super();
		this.accountNumber = accountNumber;
		this.accountName = accountName;
		this.accountBalance = accountBalance;
		this.accountType = accountType;
	}
	
	public String getAccountNumber() {
		return accountNumber;
	}
	
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	public String getAccountName() {
		return accountName;
	}
	
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public Double getAccountBalance() {
		return accountBalance;
	}

	public void setAccountBalance(Double accountBalance) {
		this.accountBalance = accountBalance;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountBalance == null) ? 0 : accountBalance.hashCode());
		result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccountModel other = (AccountModel) obj;
		if (accountBalance == null) {
			if (other.accountBalance != null)
				return false;
		} else if (!accountBalance.equals(other.accountBalance))
			return false;
		if (accountName == null) {
			if (other.accountName != null)
				return false;
		} else if (!accountName.equals(other.accountName))
			return false;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("AccountModel [accountNumber=%s, accountName=%s, accountBalance=%s, accountType=%s]",
				accountNumber, accountName, accountBalance, accountType);
	}

	
}
