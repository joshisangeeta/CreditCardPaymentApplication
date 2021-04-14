package com.cg.creditcardpayment.model;

/**
* <h1>Address Model</h1>
* The Address Model program is an embaddable one such that
* the Customer model can embadded this one to complete the details and send the details to entity with help of Parser
* and perform some Validations.
* <p>
* 
*
* @author  P Venkata Sai Reddy
* @version 1.0
* @since   2021-03-31 
*/

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.cg.creditcardpayment.entity.CustomerEntity;


@Embeddable
public class AddressModel implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * This a local variable: {@link #doorNo} door number of the Customer
	 * @HasGetter
	 * @HasSetter
	 */
	@NotNull(message="Door Number cannot be null")	
	@NotBlank(message="Door Number cannot be blank")
	private String doorNo;
	
	/**
	 * This a local variable: {@link #street} defines the street of the Customer which should not be Null
	* @HasGetter
	* @HasSetter
	*/
	@NotNull(message="street name cannot be null")	
	@NotBlank(message="street name cannot be blank")
	private String street;
	
	/**
	 * This a local variable: {@link #area} defines the area of the Customer which should not be Null
	* @HasGetter
	* @HasSetter
	*/
	@NotNull(message="area name cannot be null")	
	@NotBlank(message="area name cannot be blank")
	private String area;
	
	/**
	 * This a local variable: {@link #city} defines the city of the Customer which should not be Null
	* @HasGetter
	* @HasSetter
	*/
	@NotNull(message="city name cannot be null")	
	@NotBlank(message="city name cannot be blank")
	private String city;
	
	/**
	 * This a local variable: {@link #state} defines the state of the Customer which should not be Null
	* @HasGetter
	* @HasSetter
	*/
	@NotNull(message="state name cannot be null")	
	@NotBlank(message="state cannot be blank")
	private String state;
	
	/**
	 * This a local variable: {@link #pincode} defines the pincode of the Customer which should not be Null
	* @HasGetter
	* @HasSetter
	*/
	@Min(value=100000, message="Pincode should be valid of length 6")
	@Max(value=999999,message="Pincode should be valid of length 6")
	private Integer pincode;
	
	/**
	 * This a local variable: {@link #customers} defines the set of Customer who are comes under same address.<br> It has a relation <strong>one to many</strong> with <strong>CustomerEntity</strong>
	* @HasGetter
	* @HasSetter
	*/
	@OneToMany
	private Set<CustomerEntity> customers;
	
	/**
	 * Default Constructor
	 */
	public AddressModel() {
		/* Default Constructor */
	}

	
	/**
	 * @param doorNo
	 * @param street
	 * @param area
	 * @param city
	 * @param state
	 * @param pincode
	 */
	public AddressModel(String doorNo, String street, String area, String city, String state, Integer pincode) {
		super();
		this.doorNo = doorNo;
		this.street = street;
		this.area = area;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}

	/**
	 * @return the customers
	 */
	public Set<CustomerEntity> getCustomers() {
		return customers;
	}
	
	/**
	 * @param customers the customers to set
	 */
	public void setCustomers(Set<CustomerEntity> customers) {
		this.customers = customers;
	}

	/**
	 * @return the doorNo
	 */
	public String getDoorNo() {
		return doorNo;
	}

	/**
	 * @param doorNo the doorNo to set
	 */
	public void setDoorNo(String doorNo) {
		this.doorNo = doorNo;
	}


	/**
	 * @return the street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * @return the area
	 */
	public String getArea() {
		return area;
	}

	/**
	 * @param area the area to set
	 */
	public void setArea(String area) {
		this.area = area;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * @return the pincode
	 */
	public Integer getPincode() {
		return pincode;
	}

	/**
	 * @param pincode the pincode to set
	 */
	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((doorNo == null) ? 0 : doorNo.hashCode());
		result = prime * result + ((pincode == null) ? 0 : pincode.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((street == null) ? 0 : street.hashCode());
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
		AddressModel other = (AddressModel) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (doorNo == null) {
			if (other.doorNo != null)
				return false;
		} else if (!doorNo.equals(other.doorNo))
			return false;
		if (pincode == null) {
			if (other.pincode != null)
				return false;
		} else if (!pincode.equals(other.pincode))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (street == null) {
			if (other.street != null)
				return false;
		} else if (!street.equals(other.street))
			return false;
		return true;
	}

	/**
	 * Returns a string representation of the object. In general, the toString method returns a string that "textually represents" this object.<br> 
	 * The result should be a concise but informative representation that is easy for a person to read.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return String.format("doorNo=%s, street=%s, area=%s, city=%s, state=%s, pincode=%s", doorNo, street,
				area, city, state, pincode);
	}
	
	
	
	
}
