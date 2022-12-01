package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import app.Login;
class LoginTest {
	String testCardNum = "123456789";
	int testPin = 1234;

	@Test
	void getCardNum() {
		Login testLogin = new Login(testCardNum, testPin);
		assertEquals(testCardNum, testLogin.getCardNum());
	}
	
	@Test
	void getPin() {
		Login testLogin = new Login(testCardNum, testPin);
		assertEquals(testPin, testLogin.getPin());
	}

}
