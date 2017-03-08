package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityOfferMatchDateMethodTest {
	private ActivityOffer offer;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		this.offer = new ActivityOffer(activity, begin, end);
	}
	
	//Test to verify if the dates given are the same as the initial offer dates
	@Test
	public void success() {
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 21)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap01(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 18), new LocalDate(2016, 12, 19)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap02(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 18), new LocalDate(2016, 12, 20)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap03(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 18), new LocalDate(2016, 12, 21)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap04(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 18), new LocalDate(2016, 12, 22)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap05(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 20)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap06(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 22)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap07(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 20), new LocalDate(2016, 12, 20)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap08(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 20), new LocalDate(2016, 12, 21)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap09(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 20), new LocalDate(2016, 12, 22)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap10(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 21), new LocalDate(2016, 12, 22)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap11(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 19), new LocalDate(2016, 12, 19)));
	}
	
	//Test to verify if the dates given overlaps the dates of the initial offer
	@Test
	public void testDateOverlap12(){
		Assert.assertTrue(this.offer.matchDate(new LocalDate(2016, 12, 21), new LocalDate(2016, 12, 21)));
	}
	
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
