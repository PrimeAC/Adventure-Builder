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
		this.begin = new LocalDate(2016, 12, 19);
		this.end = new LocalDate(2016, 12, 21);
	}

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

	@Test(expected = ActivityException.class)
	public void testEndBeforeBegin() {
		new ActivityOffer(this.activity, this.end, this.begin);
	}

	@Test(expected = ActivityException.class)
	public void testOverlappedDates01() {
		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, this.begin, this.end);
	}

	@Test(expected = ActivityException.class)
	public void testOverlappedDates02() {
		LocalDate secondBegin = new LocalDate(2016, 12, 17);
		LocalDate secondEnd = new LocalDate(2016, 12, 19);

		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, secondBegin, secondEnd);
	}

	@Test(expected = ActivityException.class)
	public void testOverlappedDates03() {
		LocalDate secondBegin = new LocalDate(2016, 12, 21);
		LocalDate secondEnd = new LocalDate(2016, 12, 23);

		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, secondBegin, secondEnd);
	}

	@Test(expected = ActivityException.class)
	public void testOverlappedDates04() {
		LocalDate secondBegin = new LocalDate(2016, 12, 17);
		LocalDate secondEnd = new LocalDate(2016, 12, 23);

		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, secondBegin, secondEnd);
	}

	@Test(expected = ActivityException.class)
	public void testOverlappedDates05() {
		LocalDate secondBegin = new LocalDate(2016, 12, 17);
		LocalDate secondEnd = new LocalDate(2016, 12, 23);

		new ActivityOffer(this.activity, secondBegin, secondEnd);
		new ActivityOffer(this.activity, this.begin, this.end);
	}

	@Test
	public void testSuccessfulDates01() {
		LocalDate secondBegin = new LocalDate(2016, 12, 16);
		LocalDate secondEnd = new LocalDate(2016, 12, 18);

		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, secondBegin, secondEnd);
	}

	@Test
	public void testSuccessfulDates02() {
		LocalDate secondBegin = new LocalDate(2016, 12, 22);
		LocalDate secondEnd = new LocalDate(2016, 12, 24);

		new ActivityOffer(this.activity, this.begin, this.end);
		new ActivityOffer(this.activity, secondBegin, secondEnd);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
