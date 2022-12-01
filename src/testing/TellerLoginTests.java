package testing;
import app.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TellerLoginTests {
	
	TellerLogin testTeller = new TellerLogin();
	
	@Test
	void setterGetterTests() {
		testTeller.setUsername("testingUser");
		testTeller.setPassword("testingPassword");
		assertEquals(testTeller.getUsername(), "testingUser");
		assertEquals(testTeller.getPassword(), "testingPassword");
	}

}
