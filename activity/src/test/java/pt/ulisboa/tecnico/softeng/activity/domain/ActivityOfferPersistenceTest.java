package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ActivityOfferPersistenceTest {
	private static final String PROVIDER_CODE = "CODE12";
	private static final String PROVIDER_NAME = "providerNAME";
	private static final String ACTIVITY_NAME = "activityNAME";
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 50;
	private static final int CAPACITY = 30;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	ActivityOffer activityOffer;
	Activity activity;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		ActivityProvider activityProvider = new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);
		activity = new Activity(activityProvider, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		activityOffer = new ActivityOffer(activity, begin, end);
		new ActivityOffer(activity, begin, end);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {

		List<ActivityOffer> activityOffers = new ArrayList<ActivityOffer>(activity.getOffers(begin, end, 40));
		ActivityOffer AO = activityOffers.get(0);

		assertEquals(activityOffer.getBegin(), AO.getBegin());
		assertEquals(activityOffer.getEnd(), AO.getEnd());
		assertEquals(activityOffer.getActivity(), AO.getActivity());
		assertEquals(2, activity.getNumberOfOffers());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider AP : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			AP.delete();
		}
	}

}
