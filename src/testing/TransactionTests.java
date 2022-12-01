package testing;
import app.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TransactionTests {
	
	Transaction testTrans = new Transaction("tester", "tester", 15.50, RequestType.DEPOSIT);
	
	@Test
	void getterTest() {
		assertEquals("tester", testTrans.getAccount());
		assertEquals("tester", testTrans.getTarget());
		assertEquals(15.5, testTrans.getAmount());
		assertEquals(RequestType.DEPOSIT, testTrans.getRequest());
	}

}
