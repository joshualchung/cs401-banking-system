import java.util.List;

public class Customer {
	private String firstName;
	private String lastName;
	private String cardNum;
	private int pin;
	private List<Account> accounts;			// [0] checking, [1] saving
	
	public Customer(String firstName, String lastName, String cardNum, int pin, List<Account> accounts) {
		setFirst(firstName);
		setLast(lastName);
		setCard(cardNum);
		setPin(pin);
		setAccounts(accounts);
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
	
	public void setAccounts(List<Account> accounts) { 
		this.accounts = accounts;
	}
	
	public String getCard() {
		return cardNum;
	}
	
	public int getPin() {
		return pin;
	}
}
