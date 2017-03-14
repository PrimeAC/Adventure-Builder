package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityOfferHasVacancyMethodTest {
	private ActivityOffer offer;
	private ActivityProvider provider;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 2);

		LocalDate begin = LocalDate.now();
		LocalDate end = begin.plusDays(2);

		this.offer = new ActivityOffer(activity, begin, end);
	}

	//Test to check if the activity still has all vacancies
	@Test
	public void sucessZeroBookings() {
		Assert.assertTrue(this.offer.hasVacancy());
	}
	
	//Test to check if the activity still has 1 vacancy
	@Test
	public void sucessOneBooking() {
		new Booking(this.provider,this.offer);
		Assert.assertTrue(this.offer.hasVacancy());
	}
	
	//Test to check if the activity has no vacancies
	@Test
	public void sucessTwoBookings() {
		new Booking(this.provider,this.offer);
		new Booking(this.provider,this.offer);
		Assert.assertFalse(this.offer.hasVacancy());
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
