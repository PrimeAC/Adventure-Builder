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
	
	public void mainTest(Broker broker,LocalDate begin,LocalDate end, int age, String IBAN, int amount,String error){
		Adventure adventure;
		try {
			adventure = new Adventure(broker, begin, end, age, IBAN, amount);	
		} catch (BrokerException e) {
			System.out.print("broker exception caught in: "+error);
			//System.out.print(e.getStackTrace());
			return;
		}
		Assert.fail("no exception in: "+error);
	}
	
	@Test
	public void nullBrokerTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(null, begin, end, 20, "BK011234567", 300,"nullBrokerTest");
	}
	
	@Test
	public void nullBeginTest() {
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, null, end, 20, "BK011234567", 300, "nullBeginTest");
	}
	
	@Test
	public void nullEndTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		mainTest(this.broker, begin, null, 20, "BK011234567", 300, "nullEndTest");
	}
	
	@Test
	public void nullAgeTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, begin, end, (Integer) null, "BK011234567", 300, "nullAgeTest");
	}
	
	@Test
	public void nullIBANTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, begin, end, 20, null, 300, "nullIBANTest");
	}
	
	@Test
	public void nullAmountTest() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, begin, end, 20, "BK011234567", (Integer) null, "nullAmountTest");
	}
	
	@Test
	public void endDateBeforeInitialDateTest() {
		// begin > end
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, begin, end, 20, "BK011234567", 300, "endDateBeforeInitialDateTest");
	}
	
	@Test
	public void ageTest() {
		// age <0
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, begin, end, -1, "BK011234567", 300, "ageTest");
	}

	@Test
	public void valueTest() {
		// amount <0
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		mainTest(this.broker, begin, end, 20, "BK011234567", -1, "valueTest");
	}
	
	@Test
	public void IBANSizeTest() {
		// code size <= 4 fails
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure;
		try {
			adventure = new Adventure(this.broker, begin, end, 20, "BK", 300);
		} catch (BrokerException e) {
			System.out.print("broker exception caught in: IBANSizeTest");
			//System.out.print(e.getStackTrace());
			return;
		}
		Assert.fail("no exception in: IBANSizeTest");
	}
	
	@Test
	public void IBANSizeTest2() {
		// code size > 4 pass
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure;
		try {
			adventure = new Adventure(this.broker, begin, end, 20, "BK011", 300);
		} catch (BrokerException e) {
			Assert.fail("exception in: IBANSizeTest2");
			//System.out.print(e.getStackTrace());
			return;
		}
		//System.out.print("no exception in: IBANSizeTest2");
		return;
	}
	
	@Test
	public void IBANWhitespaceStringTest() {
		// code not whitespaces
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure;
		try {
			adventure = new Adventure(this.broker, begin, end, 20, "     ", 300);
		} catch (BrokerException e) {
			System.out.print("no exception in: IBANWhitespaceStringTest");
			//System.out.print(e.getStackTrace());
			return;
		}
		Assert.fail("exception in: IBANWhitespaceStringTest");
	}
	


	@After
	public void tearDown() {
		Broker.brokers.clear();
	}
}
