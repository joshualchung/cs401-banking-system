package testing;
import app.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RequestTests {

	Request testRequest = new Request(RequestType.LOGOUT);
	
	
	@Test
	void setterGetterTest() {
		assertEquals(testRequest.getType(), RequestType.LOGOUT);
		assertEquals(testRequest.getStatus(), Status.PENDING);
		testRequest.setStatus(Status.SUCCESS);
		assertEquals(testRequest.getStatus(), Status.SUCCESS);
	}

}
