import java.io.Serializable;
import java.util.Date;

enum RequestType{
	DEPOSIT, 
	WITHDRAW, 
	TRANSFER, 
	CLOSEACCOUT, 
	OPENACCOUNT,
	CREATEACCOUNT,
	CREATECUSTOMER, 
	GETACCOUNT, 
	GETCUSTOMER, 
	CUSTOMER_LOGIN, 
	TELLER_LOGIN,
	LOGOUT, 
	REMOVECUSTOMER, 
	GETALLCUSTOMERACCOUNTS
}

enum Status{
	PENDING, FAIL, SUCCESS
}

@SuppressWarnings("Serial")
public class Request implements Serializable{
	protected RequestType type;
	protected Date date;
	protected Status status; 
	
	public Request() {
		type = null;
		date = new Date();
		status = Status.PENDING;
	}
	
	public Request(RequestType type) {
		this.type = type;
		date = new Date();
		status = Status.PENDING;
	}
	
	public Request(RequestType type, String description) {
		this.type = type;
		this.date = new Date();
		status = Status.PENDING;
	}
	
	public RequestType getType() {
		return type;
	}
	
	public Date getDate() {
		return date;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public void setStatus(Status newStatus) {
		status = newStatus;
	}
	
	public String toString() {
		return "[" + date + "] " + type + " - " + status;
	}
}