package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferConstructorMethodTest {
	private Activity activity;
	private LocalDate begin;
	private LocalDate end;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 25);
		this.begin = LocalDate.now();
		this.end = this.begin.plusDays(2);
	}

	/**
	 * Test to check if the {@link ActivityOffer} can start today
	 */
	@Test
	public void success() {
		ActivityOffer offer = new ActivityOffer(this.activity, this.begin, this.end);

		Assert.assertEquals(begin, offer.getBegin());
		Assert.assertEquals(end, offer.getEnd());
		Assert.assertEquals(1, this.activity.getNumberOfOffers());
		Assert.assertEquals(0, offer.getNumberOfBookings());
	}

	@Test(expected = ActivityException.class)
	public void testNullActivity() {
		new ActivityOffer(null, this.begin, this.end);
	}

	@Test(expected = ActivityException.class)
	public void testNullBegin() {
		new ActivityOffer(this.activity, null, this.end);
	}

	@Test(expected = ActivityException.class)
	public void testNullEnd() {
		new ActivityOffer(this.activity, this.begin, null);
	}

	/**
	 * Test to check if the {@link ActivityOffer} end can be before the beginning
	 */
	@Test(expected = ActivityException.class)
	public void testEndBeforeBegin() {
		new ActivityOffer(this.activity, this.end, this.begin);
	}

	/**
	 * Test to check if the {@link ActivityOffer} can start one day before the current date
	 */
	@Test(expected = ActivityException.class)
	public void testBeginBeforeNow() {
		new ActivityOffer(this.activity, this.begin.minusDays(1), this.end);
	}

	/**
	 * Test to check if the two {@link ActivityOffer} can be created on the same dates
	 */
	@Test(expected = ActivityException.class)
	public void testOverlappedDates01() {
		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, this.begin, this.end);
	}

	/**
	 * Test to check if the second {@link ActivityOffer} can end on the beginning of the first one
	 */
	@Test(expected = ActivityException.class)
	public void testOverlappedDates02() {
		new ActivityOffer(this.activity, this.end, this.end.plusDays(2));
		new ActivityOffer(this.activity, this.begin, this.end);
	}

	/**
	 * Test to check if the second {@link ActivityOffer} can start on the end of the first one
	 */
	@Test(expected = ActivityException.class)
	public void testOverlappedDates03() {
		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, this.end, this.end.plusDays(2));
	}

	/**
	 * Test to check if the second {@link ActivityOffer} can start before the beginning and end
	 * after the end of the first one
	 */
	@Test(expected = ActivityException.class)
	public void testOverlappedDates04() {
		new ActivityOffer(this.activity, this.end, this.end.plusDays(2));
		new ActivityOffer(this.activity, this.begin, this.end.plusDays(4));
	}

	/**
	 * Test to check if the second {@link ActivityOffer} can start after the beginning and end
	 * before the end of the first one
	 */
	@Test(expected = ActivityException.class)
	public void testOverlappedDates05() {
		new ActivityOffer(this.activity, this.begin, this.end.plusDays(4));
		new ActivityOffer(this.activity, this.end, this.end.plusDays(2));
	}

	/**
	 * Test to check if the second {@link ActivityOffer} can end one day before the beginning
	 * of the first one
	 */
	@Test
	public void testSuccessfulDates01() {
		new ActivityOffer(this.activity, this.end.plusDays(1), this.end.plusDays(3));
		new ActivityOffer(this.activity, this.begin, this.end);
	}

	/**
	 * Test to check if the second {@link ActivityOffer} can begin one day after the end
	 * of the first one
	 */
	@Test
	public void testSuccessfulDates02() {
		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, this.end.plusDays(1), this.end.plusDays(3));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
