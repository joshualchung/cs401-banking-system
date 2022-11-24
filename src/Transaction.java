import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable {
	private String account;
	private String target;				// transfer target (only for transfer requests)
	private double amount;
	private RequestType request;		// DEPOSIT, WITHDRAW, TRANSFER
	private Date date;
	
	// deposit/withdraw constructor
	public Transaction(String account, double amount, RequestType request) {
		date = new Date();
		this.account = account;
		this.amount = amount;
		this.request = request;
	}
	
	// transfer transaction constructor
	public Transaction(String account, String target, double amount, RequestType request) {
		date = new Date();
		this.account = account;
		this.target = target;
		this.amount = amount;
		this.request = request;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getAccount() {
		return account;
	}
	
	public String getTarget() {
		return target;
	}
}
