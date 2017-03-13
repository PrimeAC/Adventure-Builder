package pt.ulisboa.tecnico.softeng.broker.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;



public class AdventureConstructorMethodTest {
	private Broker broker;

	public static final int CODE_SIZE = 4;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

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
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(null, begin, end, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullBeginTest() {
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, null, end, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullEndTest() {
		LocalDate begin = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, begin, null, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullIBANTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, begin, end, 20, null, 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void endDateBeforeInitialDateTest() {
		// begin > end error
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, end, begin, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void initialDateBeforeCurrentDateTest() {
		// initial date (begin) must be >= current date
		// begin -> current date -1 min
		LocalDate begin = LocalDate.now();
		LocalDate end = new LocalDate(2016, 12, 21);
		begin.minusDays(1);
		 new Adventure(this.broker, end, begin, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void minAgeTest() {
		// age>17
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, end, begin, 17, "BK011234567", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void maxAgeTest() {
		// age<100
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, end, begin, 100, "BK011234567", 300);
	}

	@Test(expected=BrokerException.class)
	public void minAmountTest() {
		// amount > -1
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, end, begin, 20, "BK011234567", -1);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANSizeTest() {
		// code size < 5 fails
		// In account - bank code(4digits) + int(1 digit -> min)
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, begin, end, 20, "BK01", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANBlankStringTest() {
		// bank IBAN can't have 5 whitespaces only(passes size test) or it is blank
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		 new Adventure(this.broker, begin, end, 20, "     ", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void duplicateAdventure() {
		// check if the IBAN code (1st 4 characters) is the same as the bank code
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		new Adventure(this.broker, begin, end, 20, "BK011234567", 300);
		new Adventure(this.broker, begin, end, 20, "BK011234567", 300);
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
