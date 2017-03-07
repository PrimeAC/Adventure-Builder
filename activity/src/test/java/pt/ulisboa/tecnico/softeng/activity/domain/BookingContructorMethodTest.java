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
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
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
		ActivityProvider activityProvider = new ActivityProvider("XtremY", "Motorcycling");
		Activity activity = new Activity(activityProvider, "Trail", 18, 40, 3);

		LocalDate begin = new LocalDate(2017, 03, 1);
		LocalDate end = new LocalDate(2017, 03, 4);

		ActivityOffer activityOffer = new ActivityOffer(activity, begin, end);

		new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
	}

	// Test to verify if Activity's capacity wasn't exceeded
	@Test
	public void testCapacity02() {
		ActivityProvider activityProvider = new ActivityProvider("XtremY", "Motorcycling");
		Activity activity = new Activity(activityProvider, "Trail", 18, 40, 3);

		LocalDate begin = new LocalDate(2017, 03, 1);
		LocalDate end = new LocalDate(2017, 03, 4);

		ActivityOffer activityOffer = new ActivityOffer(activity, begin, end);

		new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
	}

	// Test to verify if Activity's capacity was exceeded
	@Test (expected = ActivityException.class)
	public void testCapacity03() {
		ActivityProvider activityProvider = new ActivityProvider("XtremY", "Motorcycling");
		Activity activity = new Activity(activityProvider, "Trail", 18, 40, 3);

		LocalDate begin = new LocalDate(2017, 03, 1);
		LocalDate end = new LocalDate(2017, 03, 4);

		ActivityOffer activityOffer = new ActivityOffer(activity, begin, end);

		new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
	}

	// Test to verify if Arguments of Booking's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments01() {
		ActivityProvider activityProvider = new ActivityProvider("XtremY", "Motorcycling");
		Activity activity = new Activity(activityProvider, "Trail", 18, 40, 1);

		LocalDate begin1 = new LocalDate(2017, 03, 02);
		LocalDate end1 = new LocalDate(2017, 03, 03);

		ActivityOffer activityOffer = new ActivityOffer(activity, begin1, end1);

		new Booking(null, activityOffer);
	}

	// Test to verify if Arguments of Booking's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments02() {
		ActivityProvider activityProvider = new ActivityProvider("XtremY", "Motorcycling");

		new Booking(activityProvider, null);
	}

	// Test to verify if Arguments of Booking's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments03() {
		new Booking(null, null);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
