import java.io.Serializable;
import java.time.LocalDateTime;

public class Transaction implements Serializable {
	private String account;
	private String target;				// transfer target (only for transfer requests)
	private double amount;
	private RequestType request;		// DEPOSIT, WITHDRAW, TRANSFER
	private LocalDateTime date;
	
	public Transaction(String account, String target, double amount, RequestType request) {
		date = LocalDateTime.now();
		
		this.account = account;
		this.target = target;
		this.amount = amount;
		this.request = request;
	}
	
	// for loading transactions from file
	public Transaction(String account, String target, double amount, RequestType request, LocalDateTime date) {
		this.date = date;
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
	
	public String getDate() {
		return date.toString();
	}
	
	public RequestType getRequest() {
		return request;
	}
}
