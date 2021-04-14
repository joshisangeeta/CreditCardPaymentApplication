package com.cg.creditcardpayment.model;
/**
* <h1>Customer Model</h1>
* The Customer Model program implements an application such that
* the user can provide Customer details and send the details to entity with help of Parser
* and perform some Validations.
* <p>
* 
*
* @author  P Venkata Sai Reddy
* @version 1.0
* @since   2021-03-31 
*/

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;


public class CustomerModel {
	/**
	 * This a local variable: {@link #userId} defines the unique Id for Customer
	 */
	private String userId;
	
	/**
	 * This a local variable: {@link #userName} defines the user name of Customer whcih should not be Null
	* @HasGetter
	* @HasSetter
	*/
	@NotNull(message="customer name cannot be null")	
	@NotBlank(message="customer name cannot be blank")
	@Pattern(regexp="^[A-Za-z ]{4,}$",message="Must be more than 4 Letters and should contain Alphabets")
	private String userName;
	
	/**
	 * This a local variable: {@link #email} defines the user Email of the Customer which should not be Null
	 * @HasGetter
	 * @HasSetter
	 */	
	@NotNull(message="email cannot be null")	
	@NotBlank(message="email cannot be blank")
	@Pattern(regexp="^[A-Za-z0-9]{3,}[@][a-z]{2,}[.][a-z.]{2,}[a-z]$",message="Email should be in valid format ex:(example@gmail.com)")
	@Email(message="Email should be valid")
	private String email;
	
	/**
	 * This a local variable: {@link #contactNo} defines the user mobile number of the Customer which should not be Null
	 * @HasGetter
	 * @HasSetter
	 */	
	@NotNull(message="number cannot be null")	
	@NotBlank(message="number cannot be blank")
	@Pattern(regexp = "[6-9][0-9]{9}",message="Mobile number should be 10 digit with valid number")
	private String contactNo;

	/**
	 * This a local variable: {@link #dob} defines the user Date of Birth of the Customer which should not be Null
	 * @HasGetter
	 * @HasSetter
	 */	
	@PastOrPresent(message="Date of Birth cannot be in future")
	@DateTimeFormat(iso=ISO.DATE)
	@NotNull(message="Date of Birth should not be Null")
	private LocalDate dob;
	
	/**
	 * This a local variable: {@link #address} defines the user Address of the Customer which should not be Null
	 * @HasGetter
	 * @HasSetter
	 */	
	@Valid
	private AddressModel address;

	/**
	 * Default Constructor
	 */
	public CustomerModel() {
		/* Default Constructor*/
	}

	/**
	 * Paramatrized Constructor with parameters
	 * @param userId      the unique Id for Customer
	 * @param userName    the name of the Customer
	 * @param email       the emial of the Customer
	 * @param contactNo   the contact number of Customer 
	 * @param dob		  the date of birth of Customer
	 * @param address     the Address of the Customer
	 */
	public CustomerModel(String userId,
			@NotNull(message = "customer name cannot be null") @NotBlank(message = "customer name cannot be blank") String userName,
			@NotNull(message = "email cannot be null") @NotBlank(message = "email cannot be blank") @Pattern(regexp = "^[A-Za-z0-9]{3,}[@][a-z]{2,}[a-z.]{2,}[a-z]$") String email,
			@NotNull(message = "number cannot be null") @NotBlank(message = "number cannot be blank") @Pattern(regexp = "[6-9][0-9]{9}") String contactNo,
			@NotNull(message = "date of birth cannot be null") @NotBlank(message = "date of birth cannot be blank") @PastOrPresent(message = "Date of Birth cannot be in future") LocalDate dob,
			@NotNull(message = "address cannot be null") @NotBlank(message = "adress cannot be blank") AddressModel address) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.email = email;
		this.contactNo = contactNo;
		this.dob = dob;
		this.address = address;
	}

	/**
	 * @return userId in String  
	 */
	public String getUserId() {
		return userId;
	}
	
	/**
	 * @param userId to set which is unique and string type
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 
	 * @return userName in String 	 
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName to set for the User which is string type
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/** 
	 * @return email in String 	 
	 */
	public String getEmail() {
		return email;
	}
	/**
	 * @param email to set for the User which is string type
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	/** 
	 * @return contactNo in String 	 
	 */
	public String getContactNo() {
		return contactNo;
	}
	/**
	 * @param contactNo to set for the User which is string type
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}
	/** 
	 * @return dob in LocalDate 	 
	 */
	public LocalDate getDob() {
		return dob;
	}
	/**
	 * @param dob to set for the User which is LocalDate type
	 */
	public void setDob(LocalDate dob) {
		this.dob = dob;
	}
	/** 
	 * @return address in object 	 
	 */
	public AddressModel getAddress() {
		return address;
	}
	/**
	 * @param address to set for the User which is an object
	 */
	public void setAddress(AddressModel address) {
		this.address = address;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((contactNo == null) ? 0 : contactNo.hashCode());
		result = prime * result + ((dob == null) ? 0 : dob.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.<br><br>
	 * The <strong>equals</strong> method for class <strong>Object</strong> implements the most discriminating possible equivalence relation on objects; 
	 * that is, for any non-null reference values x and y, this method returns <strong>true</strong> if and only if x and y refer to the same object (<strong>x == y</strong> has the value <strong>true</strong>).
	 * <br><br>Note that it is generally necessary to override the <strong>hashCode</strong> method whenever this method is overridden, 
	 * so as to maintain the general contract for the <strong>hashCode</strong> method,
	 * which states that equal objects must have equal hash codes.
	 * <br>
	 * @param obj the reference object with which to compare.
	 * 
	 * @return true if this object is the same as the obj argument; false otherwise.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerModel other = (CustomerModel) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (contactNo == null) {
			if (other.contactNo != null)
				return false;
		} else if (!contactNo.equals(other.contactNo))
			return false;
		if (dob == null) {
			if (other.dob != null)
				return false;
		} else if (!dob.equals(other.dob))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
	
	/**
	 * Returns a string representation of the object. In general, the toString method returns a string that "textually represents" this object. 
	 * The result should be a concise but informative representation that is easy for a person to read.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return String.format("userId=%s, userName=%s, email=%s, contactNo=%s, dob=%s, address=%s",
				userId, userName, email, contactNo, dob, address);
	}
	
	
}
