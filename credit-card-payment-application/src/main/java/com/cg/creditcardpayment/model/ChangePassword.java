package com.cg.creditcardpayment.model;
/**
* <h1>Change Password</h1>
* The Change Password program implements when the user needs to change his existing password.<br>
* After updating the details are send to User entity with help of Parser
* and perform some Validations.
* <p>
* 
*
* @author  Shambhavi
* @version 1.0
* @since   2021-03-31 
*/
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class ChangePassword {
	
	/**
	 * This a local variable: {@link #userId} userId of the Customer
	 * @HasGetter
	 * @HasSetter
	 */
	@NotNull(message="user id cannot be null")	
	@NotBlank(message="user id cannot be blank")
	@Pattern(regexp="^[A-Za-z][A-Za-z0-9]{3,20}$",message="UserId should be Alphanumeric of min length of 4")
	private String userId;
	
	/**
	 * This a local variable: {@link #currentPassword} current password of the Customer
	 * @HasGetter
	 * @HasSetter
	 */
	@NotNull(message="password cannot be null")	
	@NotBlank(message="password cannot be blank")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,30}$",message="Password should contain atleast one Capital, Lower, Numeric and special charecters with min length of 8")
	private String currentPassword;
	
	/**
	 * This a local variable: {@link #newPassword} new password of the Customer
	 * @HasGetter
	 * @HasSetter
	 */
	@NotNull(message="password cannot be null")	
	@NotBlank(message="password cannot be blank")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,30}$",message="Password should contain atleast one Capital, Lower, Numeric and special charecters with min length of 8")
	private String newPassword;
	
	/**
	 * This a local variable: {@link #confirmPassword} confirm password of the Customer
	 * @HasGetter
	 * @HasSetter
	 */
	@NotNull(message="password cannot be null")	
	@NotBlank(message="password cannot be blank")
	@Pattern(regexp="^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&()])(?=\\S+$).{8,30}$",message="Password should contain atleast one Capital, Lower, Numeric and special charecters with min length of 8")
	private String confirmPassword;
	
	/**
	 * Default Constructor
	 */
	public ChangePassword() {
		
	}
	/**
	 * @param userId
	 * @param currentPassword
	 * @param ChangePassword
	 * @param confirmPassword
	 */
	public ChangePassword(String userId, String currentPassword, String newPassword, String confirmPassword) {
		super();
		this.userId = userId;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmPassword = confirmPassword;
	}
	
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the currentPassword
	 */
	public String getCurrentPassword() {
		return currentPassword;
	}
	/**
	 * @param currentPassword the currentPassword to set
	 */
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	/**
	 * @return the newPassword
	 */
	public String getNewPassword() {
		return newPassword;
	}
	/**
	 * @param newPassword the newPassword to set
	 */
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	/**
	 * @return the confirmPassword
	 */
	public String getConfirmPassword() {
		return confirmPassword;
	}
	/**
	 * @param confirmPassword the confirmPassword to set
	 */
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
	/**
	 * Returns a string representation of the object. In general, the toString method returns a string that "textually represents" this object.<br> 
	 * The result should be a concise but informative representation that is easy for a person to read.
	 * 
	 * @return a string representation of the object.
	 */
	@Override
	public String toString() {
		return String.format("ChangePassword [userId=%s, currentPassword=%s, changePassword=%s, confirmPassword=%s]",
				userId, currentPassword, newPassword, confirmPassword);
	}
	
	

}
