package pt.ulisboa.tecnico.softeng.activity.domain;

import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityProviderFindOfferMethodTest {
	private ActivityProvider provider;
	private LocalDate begin;
	private LocalDate end;
	private ActivityOffer offer1_1;
	private ActivityOffer offer1_2;
	private ActivityOffer offer2_1;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");

		//Creating activities
		Activity activity1 = new Activity(this.provider, "Bush Walking", 18, 30, 10);
		Activity activity2 = new Activity(this.provider, "Sky Diving", 30, 50, 15);

		this.begin = LocalDate.now().plusDays(4);
		this.end = begin.plusDays(7);

		//Creating offers for activity1
		this.offer1_1 = new ActivityOffer(activity1, this.begin, this.end);
		this.offer1_2 = new ActivityOffer(activity1, this.end.plusDays(3), this.end.plusDays(3));

		//Creating offers for activity2
		this.offer2_1 = new ActivityOffer(activity2, this.begin, this.end);

	}

	/**
	 * Test to find if there's an {@link ActivityOffer} completely in the
	 * range of the begin and end day minus one day it should not find any
	 */
	@Test
	public void testNoOfferAvailableForEndMinusOne() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.minusDays(1), 18);

		Assert.assertEquals(0, offers.size());
	}

	/**
	 * Test to find if there's an {@link ActivityOffer} completely in the
	 * range of the begin and end day, it should find offer1_1 that matches
	 * those dates and age
	 */
	@Test
	public void testOneOfferAvailable() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end, 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
	}

	/**
	 * Test to find if there's an {@link ActivityOffer} completely in the
	 * range of the begin and end plus one day, it should find offer1_1
	 * that matches those dates and age
	 */
	@Test
	public void testOneOfferAvailableForEndPlusOne() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(1), 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
	}

	/**
	 * Test to find if there's an {@link ActivityOffer} completely in the
	 * range of the begin minus one day and end it should not find any
	 */
	@Test
	public void testOneOfferAvailableForBeginMinusOne() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin.minusDays(1), this.end, 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
	}

	/**
	 * Test to find if there's an {@link ActivityOffer} completely in the
	 * range of the begin plus one day and end it should not find any
	 */
	@Test
	public void testNoOfferAvailableForBeginPlusOne() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin.plusDays(1), this.end, 18);

		Assert.assertEquals(0, offers.size());
	}

	/**
	 * Test to find if there's two {@link ActivityOffer} completely in the
	 * range of the begin of one offer and end day of another, it should
	 * find offer1_1 and offer3_1 that matches those dates and age
	 */
	@Test
	public void testTwoOffersAvailable() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(3), 18);

		Assert.assertEquals(2, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer1_2));
	}

	/**
	 * Test to find if there's an {@link ActivityOffer} where the age is lower
	 * than the maximum and minimum of activity
	 */
	@Test
	public void testAgeLowerThanMaxAndMin() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(3), 29);

		Assert.assertEquals(2, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer1_2));
	}

	/**
	 * Test to find if there's an {@link ActivityOffer} where the age is equal
	 * than the maximum and minimum of activity
	 */
	@Test
	public void testAgeEqualsMaxAndMin() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(3), 30);

		Assert.assertEquals(4, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer1_2));
		Assert.assertTrue(offers.contains(this.offer2_1));

	}

	/**
	 * Test to find if there's an {@link ActivityOffer} where the age is higher
	 * than the maximum and minimum of activity
	 */
	@Test
	public void testAgeHigherThanMaxAndMin() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin.plusDays(1), this.end.plusDays(8), 31);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer2_1));
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
