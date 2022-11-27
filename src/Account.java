import java.io.Serializable;

public class Account implements Serializable{
	private String accountNum;
	private double balance;
	// private List<Transaction> to implement later
	
	public Account(String accountNum, double balance) {
		setAccount(accountNum);
		setBalance(balance);
	}
	
	public void setAccount(String account) {
		accountNum = account;
	}
	
	public void setBalance(double balance) {
		this.balance = balance;
	}
	
	public String getAccount() {
		return accountNum;
	}
	
	public double getBalance() {
		return balance;
	}
}