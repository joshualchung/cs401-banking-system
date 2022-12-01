package testing;
import app.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerTests {
	
	private Server testServer = null;
	
	@BeforeEach
	void init() {
		testServer = new Server(8888);	
	}
	
	@Test
	public void testLoadCustomers() {
		HashMap<String, Customer> testCustomers = testServer.getCustomers();
		assertEquals(testCustomers.get("123456789").getFirstName(), "JOSHUA");
	}
	
	@Test
	public void testLoadAccounts() {
		HashMap<String, Account> testAccounts = testServer.getAccounts();
		assertTrue(testAccounts.containsKey("000000000"));
		assertTrue(testAccounts.containsKey("111111111"));
	}
	
	@Test
	public void testLoadTellers() {
		HashMap<String, TellerLogin> testTellers = testServer.getTellers();
		assertTrue(testTellers.containsKey("testUser"));
	}
	
	@Test
	public void testLoadTransactions() {
		HashMap<String, List<Transaction>> testTransactions = testServer.getTransactions();
		List<Transaction> testList = testTransactions.get("000000000");
		assumeTrue(testList != null);
		assertTrue(testList.size() > 0);
		
	}
}
