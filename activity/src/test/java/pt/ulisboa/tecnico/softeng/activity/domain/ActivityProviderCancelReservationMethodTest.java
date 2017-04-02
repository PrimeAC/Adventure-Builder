package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;

import java.util.List;

public class ActivityProviderCancelReservationMethodTest {

	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 1;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;
	private Booking booking;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
		this.booking = new Booking(this.provider, this.offer);
	}

	@Test
	public void success() {
		Assert.assertFalse(this.offer.hasVacancy());
		ActivityProvider.cancelReservation(this.booking.getReference());
		Assert.assertTrue(this.offer.hasVacancy());
		Assert.assertEquals(this.booking.getReference() + "cancelled", booking.getCancelledReference());
	}

	@Test(expected = ActivityException.class)
	public void testNullActivityConfirmation() {
		ActivityProvider.cancelReservation(null);
	}

	@Test(expected = ActivityException.class)
	public void testEmptyActivityConfirmation() {
		ActivityProvider.cancelReservation("       ");
	}

	@Test(expected = ActivityException.class)
	public void testUnexistingActivityConfirmation() {
		ActivityProvider.cancelReservation(this.booking.getReference() + "invalidSuffix");
	}

	@Test(expected = ActivityException.class)
	public void testCancelActivityTwice() {
		ActivityProvider.cancelReservation(this.booking.getReference());
		ActivityProvider.cancelReservation(this.booking.getReference());
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
}
