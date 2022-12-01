package testing;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import app.Account;
class AccountTest {

	String testAccNum = "123456789";
	double testBal = 123.45;
	@Test
	public void getAccount() {
		Account testAcc = new Account(testAccNum, testBal);
		
		assertEquals(testAccNum, testAcc.getAccount());
	}
	@Test
	public void getBalance() {
		Account testAcc = new Account(testAccNum, testBal);
		
		assertEquals(testBal, testAcc.getBalance());
	}

}
