package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;
import pt.ist.fenixframework.Atomic.TxMode;

public class BookingPersistenceTest {

	private static final String PROVIDER_CODE = "CODE12";
	private static final String PROVIDER_NAME = "providerNAME";
	private static final String ACTIVITY_NAME = "activityNAME";
	private static final int MIN_AGE = 25;
	private static final int MAX_AGE = 50;
	private static final int CAPACITY = 30;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	ActivityOffer activityOffer;
	Booking booking;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		ActivityProvider activityProvider = new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);
		Activity activity = new Activity(activityProvider, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		activityOffer = new ActivityOffer(activity, begin, end);
		booking = new Booking(activityProvider, activityOffer);
		new Booking(activityProvider, activityOffer);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Booking book = ActivityProvider.getBookingByReference(booking.getReference());
		assertEquals(booking.getReference(), book.getReference());
		Assert.assertNull(book.getCancel());
		Assert.assertNull(book.getCancellationDate());
		assertEquals(activityOffer, book.getActivityOffer());
		assertEquals(2, book.getActivityOffer().getNumberOfBookings());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (ActivityProvider AP : FenixFramework.getDomainRoot().getActivityProviderSet()) {
			AP.delete();
		}
	}
}
