package app;
import java.io.Serializable;
import java.util.List;

public class Customer implements Serializable{
	private String firstName;
	private String lastName;
	private String cardNum;
	private int pin;
	private List<String> accounts;			// [0] checking, [1] saving
	
	public Customer(String firstName, String lastName, String cardNum, int pin, List<String> accounts) {
		setFirst(firstName);
		setLast(lastName);
		setCard(cardNum);
		setPin(pin);
		setAccounts(accounts);
	}
	
	public Customer(String firstName, String lastName, int pin) {
		setFirst(firstName);
		setLast(lastName);
		setPin(pin);
	}
	
	public Customer(String cardNum, int pin) {
		setCard(cardNum);
		setPin(pin);
	}
	
	public void setFirst(String first) {
		firstName = first;
	}
	
	public void setLast(String last) {
		lastName = last;
	}
	
	public void setCard(String card) {
		cardNum = card;
	}
	
	public void setPin(int pin) {
		this.pin = pin;
	}
	
	public void setAccounts(List<String> accounts) { 
		this.accounts = accounts;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public List<String> getAccounts() {
		return accounts;
	}
	
	public String getCardNum() {
		return cardNum;
	}
	
	public int getPin() {
		return pin;
	}
}