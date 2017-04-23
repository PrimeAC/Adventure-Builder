package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

import static org.junit.Assert.assertEquals;

public class BookingPersistenceTest {
	private static final String HOTEL_CODE = "XPTO123";
	private static final String HOTEL_NAME = "Londres";
	private static final String ROOM_NUMBER = "001";
	private static final Room.Type ROOM_TYPE = Room.Type.DOUBLE;
	private static final LocalDate ARRIVAL = LocalDate.now();
	private static final LocalDate DEPARTURE = ARRIVAL.plusDays(2);

	Booking booking;

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = Atomic.TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel = new Hotel(HOTEL_CODE, HOTEL_NAME);
		Room room = new Room(hotel, ROOM_NUMBER, ROOM_TYPE);
		booking = room.reserve(ROOM_TYPE, ARRIVAL, DEPARTURE);
	}

	@Atomic(mode = Atomic.TxMode.READ)
	public void atomicAssert() {
		Hotel hotel = FenixFramework.getDomainRoot().getHotelSet().iterator().next();
		Room room = hotel.getRoomSet().iterator().next();
		Booking booking = room.getBookingSet().iterator().next();

		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());
		assertEquals(1, room.getBookingSet().size());
		assertEquals(this.booking.getReference(), booking.getReference());
		assertEquals(ARRIVAL, booking.getArrival());
		assertEquals(DEPARTURE, booking.getDeparture());

	}

	@After
	@Atomic(mode = Atomic.TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}
}
