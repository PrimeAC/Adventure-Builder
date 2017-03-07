package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		Assert.assertEquals("Adventure++", provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}
	
	//Test to verify if Arguments of ActivityProvider's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments01() {
	 	ActivityProvider provider = new ActivityProvider(null, "Adventure++");
	}
	
	//Test to verify if Arguments of ActivityProvider's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments02() {
		ActivityProvider provider = new ActivityProvider("", "Adventure++");
	}
	
	//Test to verify if Arguments of ActivityProvider's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments03() {
		ActivityProvider provider = new ActivityProvider("      ", "Adventure++");
	}
	
	//Test to verify if Arguments of ActivityProvider's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments04() {
		ActivityProvider provider = new ActivityProvider("XtremX", null);
	}
	 
	//Test to verify if Arguments of ActivityProvider's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments05() {
		ActivityProvider provider = new ActivityProvider("XtremX", "");
	}
	
	//Test to verify if Arguments of ActivityProvider's Constructor are valid
	@Test (expected = ActivityException.class)
	public void testArguments06() {
		ActivityProvider provider = new ActivityProvider("XtremX", "      ");
	}
	
	// Test to verify if ActivityProvider's name is unique
	@Test (expected = ActivityException.class)
	public void testUnique01() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");
		ActivityProvider provider1 = new ActivityProvider("Xtrem1", "Adventure++");
	}
	
	// Test to verify if ActivityProvider's code is unique
	@Test (expected = ActivityException.class)
	public void testUnique02() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");
		ActivityProvider provider1 = new ActivityProvider("XtremX", "Adventure++1");
	}
	
	// Test to verify if ActivityProvider's code is grader than 6
	@Test (expected = ActivityException.class)
	public void testLength01() {
		ActivityProvider provider = new ActivityProvider("XtremX7", "Adventure++");
	}
	
	// Test to verify if ActivityProvider's name is less than 6
	@Test (expected = ActivityException.class)
	public void testLength02() {
		ActivityProvider provider = new ActivityProvider("Xtre5", "Adventure++");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
