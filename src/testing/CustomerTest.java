package testing;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import app.Customer;
class CustomerTest {

	String testFirstName = "First";
	String testLastName = "Last";
	String testCardNum = "123456789";
	int testPin = 1234;
	List<String> accounts;
	@Test
	void getFirstName() {
		Customer customer = new Customer(testFirstName, testLastName, testPin);
		assertEquals(testFirstName, customer.getFirstName());
	}
	
	@Test
	void getLastName() {
		Customer customer = new Customer(testFirstName, testLastName, testPin);
		assertEquals(testLastName, customer.getLastName());
	}
	
	@Test
	void getPin() {
		Customer customer = new Customer(testFirstName, testLastName, testPin);
		assertEquals(testPin, customer.getPin());
	}
	
	@Test
	void getCardNum() {
		Customer customer = new Customer(testCardNum, testPin);
		assertEquals(testCardNum, customer.getCardNum());
	}

}
