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
	private ActivityOffer offer2_2;
	private ActivityOffer offer3_1;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");

		//Creating activities
		Activity activity1 = new Activity(this.provider, "Bush Walking", 18, 30, 10);
		Activity activity2 = new Activity(this.provider, "Sky Diving", 30, 50, 15);
		Activity activity3 = new Activity(this.provider, "Swimming with Sharks", 18, 80, 12);

		this.begin = LocalDate.now().plusDays(4);
		this.end = begin.plusDays(7);

		//Creating offers for activity1
		this.offer1_1 = new ActivityOffer(activity1, this.begin, this.end);
		this.offer1_2 = new ActivityOffer(activity1, this.end.plusDays(1), this.end.plusDays(8));

		//Creating offers for activity2
		this.offer2_1 = new ActivityOffer(activity2, this.begin, this.end);
		this.offer2_2 = new ActivityOffer(activity2, this.end.plusDays(1), this.end.plusDays(8));

		//Creating offers for activity3
		this.offer3_1 = new ActivityOffer(activity3, this.begin.plusDays(3), this.end.plusDays(3));
	}

	@Test
	public void success01() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.minusDays(1), 18);

		Assert.assertEquals(0, offers.size());
	}

	@Test
	public void success02() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end, 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
	}

	@Test
	public void success03() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(2), 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
	}

	@Test
	public void success04() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(3), 18);

		Assert.assertEquals(2, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success05() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(7), 18);

		Assert.assertEquals(2, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success06() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 18);

		Assert.assertEquals(3, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer1_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success07() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin.plusDays(1), this.end.plusDays(8), 18);

		Assert.assertEquals(2, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success08() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin.plusDays(3), this.end.plusDays(8), 18);

		Assert.assertEquals(2, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success09() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin.plusDays(4), this.end.plusDays(8), 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_2));
	}

	@Test
	public void success10() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.end.plusDays(1), this.end.plusDays(8), 18);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_2));
	}

	@Test
	public void success11() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.end.plusDays(2), this.end.plusDays(8), 18);

		Assert.assertEquals(0, offers.size());
	}

	@Test
	public void success12() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 29);

		Assert.assertEquals(3, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer1_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success13() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 30);

		Assert.assertEquals(5, offers.size());
		Assert.assertTrue(offers.contains(this.offer1_1));
		Assert.assertTrue(offers.contains(this.offer1_2));
		Assert.assertTrue(offers.contains(this.offer2_1));
		Assert.assertTrue(offers.contains(this.offer2_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success14() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 31);

		Assert.assertEquals(3, offers.size());
		Assert.assertTrue(offers.contains(this.offer2_1));
		Assert.assertTrue(offers.contains(this.offer2_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success15() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 49);

		Assert.assertEquals(3, offers.size());
		Assert.assertTrue(offers.contains(this.offer2_1));
		Assert.assertTrue(offers.contains(this.offer2_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success16() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 50);

		Assert.assertEquals(3, offers.size());
		Assert.assertTrue(offers.contains(this.offer2_1));
		Assert.assertTrue(offers.contains(this.offer2_2));
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success17() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 51);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success18() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 80);

		Assert.assertEquals(1, offers.size());
		Assert.assertTrue(offers.contains(this.offer3_1));
	}

	@Test
	public void success19() {
		Set<ActivityOffer> offers = this.provider.findOffer(this.begin, this.end.plusDays(8), 81);

		Assert.assertEquals(0, offers.size());
	}

    @Test
    public void success20() {
        Set<ActivityOffer> offers = this.provider.findOffer(this.begin.minusDays(4), this.end, 30);

        Assert.assertEquals(2, offers.size());
        Assert.assertTrue(offers.contains(this.offer1_1));
        Assert.assertTrue(offers.contains(this.offer2_1));
    }

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
