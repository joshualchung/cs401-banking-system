import java.io.Serializable;

public class TellerLogin implements Serializable{
	private String username;
	private String password;
	
	public TellerLogin(String username, String password) {
		setUsername(username);
		setPassword(password);
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}
