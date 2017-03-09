package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class BookingContructorMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 40, 3);

		LocalDate begin = LocalDate.now();
		LocalDate end = begin.plusDays(3);
		this.offer = new ActivityOffer(activity, begin, end);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.provider, this.offer);

		Assert.assertTrue(booking.getReference().startsWith(this.provider.getCode()));
		Assert.assertTrue(booking.getReference().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, this.offer.getNumberOfBookings());
	}

	// Test to verify if Activity's capacity wasn't exceeded
	@Test
	public void testCapacity01() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
	}

	// Test to verify if Activity's capacity wasn't exceeded
	@Test
	public void testCapacity02() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
	}

	// Test to verify if Activity's capacity was exceeded
	@Test (expected = ActivityException.class)
	public void testCapacity03() {
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
		new Booking(this.provider, this.offer);
	}

	// Test to verify if Arguments of Booking's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments01() {
		new Booking(null, this.offer);
	}

	// Test to verify if Arguments of Booking's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments02() {
		new Booking(this.provider, null);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
}
