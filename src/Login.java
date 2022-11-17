import java.io.Serializable;

public class Login implements Serializable {
	private String cardNum;
	private String pin;
	
	public Login(String cardNum, String pin) {
		setCardNum(cardNum);
		setPin(pin);
	}
	
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public String getCardNum() {
		return cardNum;
	}
	
	public String getPin() {
		return pin;
	}
	
}
