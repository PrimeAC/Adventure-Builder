package pt.ulisboa.tecnico.softeng.broker.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;



public class AdventureConstructorMethodTest {
	private Broker broker;
	private final LocalDate begin = LocalDate.now().plusDays(1);
	private final LocalDate end = LocalDate.now().plusDays(2);
	
	public static final int CODE_SIZE = 4;

	
	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}
	
 	@Test
 	public void success() {

		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK011234567", 300);
 
 
 		Assert.assertEquals(this.broker, adventure.getBroker());
 		Assert.assertEquals(begin, adventure.getBegin());
 		Assert.assertEquals(end, adventure.getEnd());
 		Assert.assertEquals(20, adventure.getAge());
 		Assert.assertEquals("BK011234567", adventure.getIBAN());
 		Assert.assertEquals(300, adventure.getAmount());
 		Assert.assertTrue(this.broker.hasAdventure(adventure));
 
 
 		Assert.assertNull(adventure.getBankPayment());
 		Assert.assertNull(adventure.getActivityBooking());
 		Assert.assertNull(adventure.getRoomBooking());
 	}

	@Test(expected=BrokerException.class)
	public void nullBrokerTest() {
		new Adventure(null, begin, end, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullBeginTest() {
		new Adventure(this.broker, null, end, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullEndTest() {
		new Adventure(this.broker, begin, null, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullIBANTest() {
		new Adventure(this.broker, begin, end, 20, null, 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void endDateBeforeInitialDateTest() {
		// begin > end error
		new Adventure(this.broker, end, begin, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void initialDateBeforeCurrentDateTest() {
		// initial date (begin) must be >= current date
		// begin -> current date -1
		LocalDate begin = LocalDate.now().minusDays(1);
		new Adventure(this.broker, begin, end, 20, "BK011234567", 300);	
	}
	
	@Test
	public void sameInitialAndEndDates() {
		// adventure can have same init and end dates
		new Adventure(this.broker, begin, begin, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void minAgeTest() {
		// age>17
		new Adventure(this.broker, begin, end, 17, "BK011234567", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void maxAgeTest() {
		// age<100
		new Adventure(this.broker, begin, end, 100, "BK011234567", 300);
	}

	@Test(expected=BrokerException.class)
	public void minAmountTest() {
		// amount > 0
		new Adventure(this.broker, begin, end, 20, "BK011234567", 0);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANSizeTest() {
		// code size < 5 fails
		// In account - bank code(4digits) + int(1 digit minimum)
		new Adventure(this.broker, begin, end, 20, "BK01", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANBlankStringTest() {
		// bank IBAN can't have 5 whitespaces only(passes size test)
		new Adventure(this.broker, begin, end, 20, "     ", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void duplicateAdventure() {
		// checks for duplicate adventure (unique IBAN)
		new Adventure(this.broker, begin.plusDays(1), end.plusDays(1), 25, "BK011234567", 350);
		new Adventure(this.broker, begin, end, 20, "BK011234567", 300);
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
