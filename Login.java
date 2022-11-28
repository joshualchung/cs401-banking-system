
import java.io.Serializable;

public class Login implements Serializable {
	private String cardNum;
	private int pin;
	
	public Login(String cardNum, int pin) {
		setCardNum(cardNum);
		setPin(pin);
	}
	
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	
	public void setPin(int pin) {
		this.pin = pin;
	}
	
	public String getCardNum() {
		return cardNum;
	}
	
	public int getPin() {
		return pin;
	}
	
}
