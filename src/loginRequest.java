import java.util.ArrayList;

@SuppressWarnings("serial")
public class loginRequest {
	private Login login;
	private Customer user;
	private ArrayList<Account> accounts;
	
	public LoginRequest(Login login) {
		this.type = RequestType.LOGIN;
		this.login = login;
		user = null;
		accounts = new ArrayList<Account>();
	}
	public Login getLogin() {
		return login;
	}
	
	public Customer getUser() {
		return user;
	}
	
	public ArrayList<Account> getAccounts() {
		return accounts;
	}
	
	public void setUser(Customer user) {
		this.user = user;
	}
	
	public void setAccounts(ArrayList<Account> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(Account newAccount) {
		accounts.add(newAccount);
	}

}
