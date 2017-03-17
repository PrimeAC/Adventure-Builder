package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomReserveMethodTest {
	Room room;
	private LocalDate arrival;
	private LocalDate departure;
	private Booking booking, booking2;

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(hotel, "01", Type.SINGLE);

		arrival = new LocalDate(2016, 12, 19);
		departure = new LocalDate(2016, 12, 24);
		booking = this.room.reserve(Type.SINGLE, arrival, departure);
	}

	@Test
	public void success() {
		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}

	@Test
	public void noConflictBefore() {
		LocalDate arrival2 = new LocalDate(2016, 12, 5);
		LocalDate departure2 = new LocalDate(2016, 12, 19);
		booking2 = this.room.reserve(Type.SINGLE, arrival2, departure2);

		Assert.assertTrue(booking2.getReference().length() > 0);
		Assert.assertEquals(arrival2, booking2.getArrival());
		Assert.assertEquals(departure2, booking2.getDeparture());
	}

	@Test
	public void noConflictAfter() {
		LocalDate arrival2 = new LocalDate(2016, 12, 24);
		LocalDate departure2 = new LocalDate(2016, 12, 28);
		booking2 = this.room.reserve(Type.SINGLE, arrival2, departure2);

		Assert.assertTrue(booking2.getReference().length() > 0);
		Assert.assertEquals(arrival2, booking2.getArrival());
		Assert.assertEquals(departure2, booking2.getDeparture());
	}

	@Test (expected = HotelException.class)
	public void checkWrongType() {
		LocalDate arrival = new LocalDate(2016, 12, 5);
		LocalDate departure = new LocalDate(2016, 12, 19);
		this.room.reserve(Type.DOUBLE, arrival, departure);
	}

	@Test (expected = HotelException.class)
	public void checkDoubleBookingStart() {
		LocalDate arrival2 = new LocalDate(2016, 12, 20);
		LocalDate departure2 = new LocalDate(2016, 12, 27);
		this.room.reserve(Type.SINGLE, arrival2, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkDoubleBookingMiddle() {
		LocalDate arrival2 = new LocalDate(2016, 12, 20);
		LocalDate departure2 = new LocalDate(2016, 12, 22);
		this.room.reserve(Type.SINGLE, arrival2, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkDoubleBookingEnd() {
		LocalDate arrival2 = new LocalDate(2016, 12, 17);
		LocalDate departure2 = new LocalDate(2016, 12, 20);
		this.room.reserve(Type.SINGLE, arrival2, departure2);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
