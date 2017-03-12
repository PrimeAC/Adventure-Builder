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
		Adventure adventure = new Adventure(null, begin, end, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullBeginTest() {
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, null, end, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullEndTest() {
		LocalDate begin = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, null, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void nullIBANTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, null, 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void endDateBeforeInitialDateTest() {
		// begin > end error
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, end, begin, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void initialDateBeforeCurrentDateTest() {
		// initial date (begin) must be >= current date
		// begin -> current date -1 min
		LocalDate begin = LocalDate.now();
		LocalDate end = new LocalDate(2016, 12, 21);
		begin.minusDays(1);
		Adventure adventure = new Adventure(this.broker, end, begin, 20, "BK011234567", 300);	
	}
	
	@Test(expected=BrokerException.class)
	public void minAgeTest() {
		// age >0
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, end, begin, -1, "BK011234567", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void maxAgeTest() {
		// age <150
		LocalDate begin = new LocalDate(2016, 12, 20);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, end, begin, 150, "BK011234567", 300);
	}

	@Test(expected=BrokerException.class)
	public void valueTest() {
		// amount >0
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, end, begin, 20, "BK011234567", -1);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANSizeTest() {
		// code size < 5 fails
		// In account - bank code(4digits) + int(1 digit -> min)
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK01", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANWhitespaceStringTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK0112 34567", 300);
	}
	
	@Test(expected=BrokerException.class)
	public void IBANNonAlfanumericCharsTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK011234567", 300);
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
