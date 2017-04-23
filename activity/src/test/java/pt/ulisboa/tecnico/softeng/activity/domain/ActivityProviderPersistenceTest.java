package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.*;


public class ActivityProviderPersistenceTest {

	private static final String ACTIVITY_PROVIDER_CODE1 = "XtremX";
	private static final String ACTIVITY_PROVIDER_CODE2 = "XtremY";
	private static final String ACTIVITY_PROVIDER_CODE3 = "XtremZ";
	private static final String ACTIVITY_PROVIDER_NAME1 = "ExtremeAdventure";
	private static final String ACTIVITY_PROVIDER_NAME2 = "ExtremeAdventure2";
	private static final String ACTIVITY_PROVIDER_NAME3 = "ExtremeAdventure3";



	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		new ActivityProvider(ACTIVITY_PROVIDER_CODE1, ACTIVITY_PROVIDER_NAME1);
		new ActivityProvider(ACTIVITY_PROVIDER_CODE2, ACTIVITY_PROVIDER_NAME2);
		new ActivityProvider(ACTIVITY_PROVIDER_CODE3, ACTIVITY_PROVIDER_NAME3);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {

		ActivityProvider provider1 = ActivityProvider.getActivityProviderByCode(ACTIVITY_PROVIDER_CODE1);
		ActivityProvider provider2 = ActivityProvider.getActivityProviderByCode(ACTIVITY_PROVIDER_CODE2);
		ActivityProvider provider3 = ActivityProvider.getActivityProviderByCode(ACTIVITY_PROVIDER_CODE3);
		ActivityProvider providerNull = ActivityProvider.getActivityProviderByCode("XtremA");

		assertNotNull(provider1);
		assertNotNull(provider2);
		assertNotNull(provider3);
		assertNull(providerNull);

		assertEquals(ACTIVITY_PROVIDER_NAME1, provider1.getName());
		assertEquals(ACTIVITY_PROVIDER_NAME2, provider2.getName());
		assertEquals(ACTIVITY_PROVIDER_NAME3, provider3.getName());

		assertEquals(3, FenixFramework.getDomainRoot().getActivityProviderSet().size());
	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}

}
