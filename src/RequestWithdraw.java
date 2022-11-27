@SuppressWarnings("serial")
public class RequestWithdraw extends Request {
	double amount;
	String accountID;
	String description;
	Account account;
	
	public RequestWithdraw(double amount, String accountID, String description) {
		this.type = RequestType.WITHDRAW;
		this.amount = amount;
		this.accountID = accountID;
		this.description = description;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public String getAccountID() {
		return accountID;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Account getAccount() {
		return account;
	}
	
	public void setAccount(Account account) {
		this.account = account;
	}
	
	public String toString() {
		return "[" + date + "] " + type + " - amount:" + amount + ", accountID:" + accountID + ", description:" + description;
	}
}