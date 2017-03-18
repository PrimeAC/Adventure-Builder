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
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 99, 25);

		Assert.assertTrue(activity.getCode().startsWith(this.provider.getCode()));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals("Bush Walking", activity.getName());
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(99, activity.getMaxAge());
		Assert.assertEquals(25, activity.getCapacity());
		Assert.assertEquals(0, activity.getNumberOfOffers());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());
	}

	// Test to verify if minimum age is under the lowest limit
	@Test (expected = ActivityException.class)
	public void testMinAgeUnder18() {
		new Activity(this.provider, "Bush Walking", 17, 80, 25);
	}

    // Test to verify if minimum age is above the lowest limit
    @Test
	public void testMinAgeAbove18() { new Activity(this.provider, "Bush Walking", 19, 80, 25); }

    // Test to verify if maximum age is under the highest limit
	@Test
	public void testMaxAgeUnder99() {
		new Activity(this.provider, "Bush Walking", 20, 98, 25);
	}

    // Test to verify if maximum age is above the highest limit
	@Test (expected = ActivityException.class)
	public void testMaxAgeAbove99() {
		new Activity(this.provider, "Bush Walking", 20, 100, 25);
	}

    // Test to verify if minimum and maximum ages are switched
	@Test (expected = ActivityException.class)
	public void testMinMaxAgeSwitched() {
		new Activity(this.provider, "Bush Walking", 99, 18, 25);
	}

    // Test to verify if minimum and maximum ages are equal to lowest limit
	@Test
	public void testMinMaxAgeEqual18() {
		new Activity(this.provider, "Bush Walking", 18, 18, 25);
	}

    // Test to verify if minimum and maximum ages are equal to highest limit
	@Test
	public void testMinMaxAgeEqual99() {
		new Activity(this.provider, "Bush Walking", 99, 99, 25);
	}

    // Test to verify if capacity is under the limit
    @Test (expected = ActivityException.class)
    public void testCapacityEqual0() { new Activity(this.provider, "Bush Walking", 18, 80, 0); }

    // Test to verify if capacity is equal to limit
    @Test
    public void testCapacityEqual1() { new Activity(this.provider, "Bush Walking", 18, 80, 1); }

    // Test to verify if capacity is a negative number
    @Test (expected = ActivityException.class)
    public void testCapacityEqualMinus1() { new Activity(this.provider, "Bush Walking", 18, 80, -1); }

    // Test to verify if ActivityProvider is null
    @Test (expected = ActivityException.class)
    public void testProviderNull() { new Activity(null,  "Bush Walking", 18, 80, 25); }

    // Test to verify if name is null
    @Test (expected = ActivityException.class)
    public void testNameNull() { new Activity(this.provider,  null, 18, 80, 25); }

    // Test to verify if name is an empty string
    @Test (expected = ActivityException.class)
    public void testNameEmpty() { new Activity(this.provider,  "", 18, 80, 25); }

    // Test to verify if name is a blank string
    @Test (expected = ActivityException.class)
    public void testNameBlank() { new Activity(this.provider,  " ", 18, 80, 25); }

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
