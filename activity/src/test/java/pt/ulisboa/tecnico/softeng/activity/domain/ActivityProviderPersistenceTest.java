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
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {

		ActivityProvider provider = ActivityProvider.getActivityProviderByCode("XtremX");
		assertEquals("ExtremeAdventure", provider.getName());
	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider provider : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			provider.delete();
		}
	}

}
