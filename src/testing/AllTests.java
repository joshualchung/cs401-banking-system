package testing;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({TransactionTests.class, TellerLoginTests.class, RequestTests.class, ServerTests.class})
public class AllTests {}