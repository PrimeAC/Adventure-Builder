package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BulkRoomBookingPersistenceTest {

	private static final String BROKER_NAME = "Happy Going";
	private static final String BROKER_CODE = "BK1017";
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";

	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	private Broker broker = new Broker(BROKER_CODE, BROKER_NAME);

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME);

		new Adventure(broker, this.begin, this.end, AGE, IBAN, AMOUNT);

		broker.bulkBooking(4, begin, end);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		List<Broker> brokers = new ArrayList<>(FenixFramework.getDomainRoot().getBrokerSet());
		Broker broker = brokers.get(0);

		List<BulkRoomBooking> bulkBookings = new ArrayList<>(broker.getBulkRoomBookingSet());

		Assert.assertEquals(1, bulkBookings.size());

		BulkRoomBooking booking = bulkBookings.get(0);

		assertEquals(begin, booking.getArrival());
		assertEquals(end, booking.getDeparture());

		List<String> references = new ArrayList<>(booking.getReferences());

		assertEquals(4, references.size());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
	}

}
