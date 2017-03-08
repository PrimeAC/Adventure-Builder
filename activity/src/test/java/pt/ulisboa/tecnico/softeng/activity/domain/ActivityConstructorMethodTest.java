package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityConstructorMethodTest {
	private ActivityProvider provider;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
	}

	@Test
	public void success() {
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		Assert.assertTrue(activity.getCode().startsWith(this.provider.getCode()));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals("Bush Walking", activity.getName());
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(80, activity.getMaxAge());
		Assert.assertEquals(25, activity.getCapacity());
		Assert.assertEquals(0, activity.getNumberOfOffers());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());
	}

	// Test to verify if minimum age is under the limit
	@Test (expected = ActivityException.class)
	public void testAge01() {
		new Activity(this.provider, "Bush Walking", 17, 80, 25);
	}

    // Test to verify if both ages are between the limits
    @Test
	public void testAge03() {
		new Activity(this.provider, "Bush Walking", 19, 80, 25);
	}

    // Test to verify if both ages are between the limits
	@Test
	public void testAge04() {
		new Activity(this.provider, "Bush Walking", 20, 98, 25);
	}

    // Test to verify if both ages are between the limits
	@Test
	public void testAge05() {
		new Activity(this.provider, "Bush Walking", 20, 99, 25);
	}

    // Test to verify if maximum age is above the limit
	@Test (expected = ActivityException.class)
	public void testAge06() {
		new Activity(this.provider, "Bush Walking", 20, 100, 25);
	}

    // Test to verify if ages are switched
	@Test (expected = ActivityException.class)
	public void testAge07() {
		new Activity(this.provider, "Bush Walking", 80, 18, 25);
	}

    // Test to verify if both ages are between the limits
	@Test
	public void testAge08() {
		new Activity(this.provider, "Bush Walking", 18, 18, 25);
	}

    // Test to verify if both ages are between the limits
	@Test
	public void testAge09() {
		new Activity(this.provider, "Bush Walking", 99, 99, 25);
	}

    // Test to verify if capacity is under the limit
    @Test (expected = ActivityException.class)
    public void testCapacity01() { new Activity(this.provider, "Bush Walking", 18, 80, 0); }

    // Test to verify if capacity is in the limit
    @Test
    public void testCapacity02() { new Activity(this.provider, "Bush Walking", 18, 80, 1); }

    // Test to verify if capacity is above the limit
    @Test
    public void testCapacity03() { new Activity(this.provider, "Bush Walking", 18, 80, 2); }

    // Test to verify if ActivityProvider is null
    @Test (expected = ActivityException.class)
    public void testArguments01() { new Activity(null,  "Bush Walking", 18, 80, 25); }

    // Test to verify if name is null
    @Test (expected = ActivityException.class)
    public void testArguments02() { new Activity(this.provider,  null, 18, 80, 25); }

    // Test to verify if name is an empty string
    @Test (expected = ActivityException.class)
    public void testArguments03() { new Activity(this.provider,  "", 18, 80, 25); }

    // Test to verify if name is a blank string
    @Test (expected = ActivityException.class)
    public void testArguments04() { new Activity(this.provider,  " ", 18, 80, 25); }

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
