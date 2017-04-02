package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;


public class ActivityProviderGetActivityReservationDataMethodTest {
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 80;
	private static final int CAPACITY = 25;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private ActivityProvider provider;
	private Activity activity;
	private ActivityOffer offer;
	private Booking booking;
	private ActivityReservationData ard;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", MIN_AGE, MAX_AGE, CAPACITY);
		this.offer = new ActivityOffer(this.activity, this.begin, this.end);
		this.booking = new Booking(this.provider, this.offer);
	}

	@Test
	public void success() {
		this.ard = ActivityProvider.getActivityReservationData(this.booking.getReference());

		Assert.assertEquals(this.begin, this.ard.getBegin());
		Assert.assertEquals(this.end, this.ard.getEnd());
		Assert.assertEquals(this.activity.getCode(), this.ard.getCode());
		Assert.assertEquals(this.activity.getName(), this.ard.getName());
		Assert.assertEquals(null, this.ard.getCancellation());
		Assert.assertEquals(null, this.ard.getCancellationDate());
	}

	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
	}

	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.getActivityReservationData(" ");
	}

	@Test(expected = ActivityException.class)
	public void invalidReference() {
		ActivityProvider.getActivityReservationData("123ASDC");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}
}
