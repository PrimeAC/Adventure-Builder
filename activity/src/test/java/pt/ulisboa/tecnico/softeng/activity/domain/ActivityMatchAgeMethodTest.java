package pt.ulisboa.tecnico.softeng.activity.domain;

import java.time.format.DateTimeFormatter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ActivityMatchAgeMethodTest {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	private Activity activity;

	@Before
	public void setUp() {
		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "Bush Walking", 18, 80, 3);
	}

	@Test
	public void successIn() {
		Assert.assertTrue(this.activity.matchAge(50));
	}

    // Test to verify if given ages are equal to minimum and maximum age respectively
	@Test
    public void testAgeLimits() {
	    Assert.assertTrue(this.activity.matchAge(18));
        Assert.assertTrue(this.activity.matchAge(80));
	}

    // Test to verify if given ages are between minimum and maximum ages
    @Test
    public void testInsideLimits() {
        Assert.assertTrue(this.activity.matchAge(19));
        Assert.assertTrue(this.activity.matchAge(79));
    }

    // Test to verify if given ages aren't between minimum and maximum ages
	@Test
    public void testOutsideLimits() {
        Assert.assertFalse(this.activity.matchAge(17));
        Assert.assertFalse(this.activity.matchAge(81));
    }

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
