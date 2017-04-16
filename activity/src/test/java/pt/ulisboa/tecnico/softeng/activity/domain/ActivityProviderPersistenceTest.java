package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertEquals;

public class ActivityProviderPersistenceTest {
	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		new ActivityProvider("XtremX", "ExtremeAdventure");
		new ActivityProvider("XtremY", "ExtremeAdventure2");
		new ActivityProvider("XtremZ", "ExtremeAdventure3");
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {

		ActivityProvider provider = ActivityProvider.getActivityProviderByCode("XtremX");
		assertEquals("ExtremeAdventure", provider.getName());

		provider = ActivityProvider.getActivityProviderByCode("XtremY");
		assertEquals("ExtremeAdventure2", provider.getName());

		provider = ActivityProvider.getActivityProviderByCode("XtremZ");
		assertEquals("ExtremeAdventure3", provider.getName());

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
