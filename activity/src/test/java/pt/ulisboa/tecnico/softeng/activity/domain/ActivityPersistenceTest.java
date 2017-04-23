package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ActivityPersistenceTest {

	private static final int CAPACITY = 25;
	private static final int MAX_AGE = 50;
	private static final int MIN_AGE = 25;
	private static final String ACTIVITY_PROVIDER_CODE = "XtremX";
	private static final String ACTIVITY_PROVIDER_NAME = "ExtremeAdventure";
	private static final String ACTIVITY_NAME = "Bush Walking";

	@Test
	public void success() {
		String code = atomicProcess();
		atomicAssert(code);
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public String atomicProcess() {
		ActivityProvider activityProvider = new ActivityProvider(ACTIVITY_PROVIDER_CODE, ACTIVITY_PROVIDER_NAME);
		Activity activity = new Activity(activityProvider, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		new Activity(activityProvider, ACTIVITY_NAME + "2", MIN_AGE, MAX_AGE, CAPACITY);
		return activity.getCode();
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert(String code) {

		Set<ActivityProvider> activityProviderSet = FenixFramework.getDomainRoot().getActivityProviderSet();

		ActivityProvider activityProvider = activityProviderSet.iterator().next();
		assertEquals(2, activityProvider.getActivitySet().size());

		Activity activity = activityProvider.getActivityByCode(code);

		assertNotNull(activity);
		assertEquals(ACTIVITY_PROVIDER_CODE, activity.getActivityProvider().getCode());
		assertEquals(ACTIVITY_PROVIDER_NAME, activity.getActivityProvider().getName());
		assertEquals(ACTIVITY_NAME, activity.getName());
		assertEquals(CAPACITY, activity.getCapacity());
		assertEquals(MAX_AGE, activity.getMaxAge());
		assertEquals(MIN_AGE, activity.getMinAge());
	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}

}
