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

	LocalDate arrival = LocalDate.now();
	LocalDate departure = arrival.plusDays(4);

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(hotel, "01", Type.SINGLE);

		arrival = new LocalDate();
		arrival.now();
		departure = arrival.plusDays(5);
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
		LocalDate arrival2 = arrival.minusDays(5);
		LocalDate departure2 = arrival;
		booking2 = this.room.reserve(Type.SINGLE, arrival2, departure2);

		Assert.assertTrue(booking2.getReference().length() > 0);
		Assert.assertEquals(arrival2, booking2.getArrival());
		Assert.assertEquals(departure2, booking2.getDeparture());
	}

	@Test
	public void noConflictAfter() {
		LocalDate arrival2 = departure;
		LocalDate departure2 = departure.plusDays(5);
		booking2 = this.room.reserve(Type.SINGLE, arrival2, departure2);

		Assert.assertTrue(booking2.getReference().length() > 0);
		Assert.assertEquals(arrival2, booking2.getArrival());
		Assert.assertEquals(departure2, booking2.getDeparture());
	}

	@Test (expected = HotelException.class)
	public void checkWrongType() {
		LocalDate arrival2 = departure.plusDays(5);
		LocalDate departure2 = departure.plusDays(10);
		this.room.reserve(Type.DOUBLE, arrival2, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkNullType() {
		LocalDate arrival2 = departure.plusDays(5);
		LocalDate departure2 = departure.plusDays(10);
		this.room.reserve(null, arrival2, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkNullArrival() {
		LocalDate departure2 = arrival.minusDays(5);
		this.room.reserve(Type.SINGLE, null, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkNullDeparture() {
		LocalDate arrival = departure.plusDays(5);
		this.room.reserve(Type.SINGLE, arrival, null);
	}

	@Test (expected = HotelException.class)
	public void checkDoubleBookingStart() {
		LocalDate arrival2 = arrival.plusDays(2);
		LocalDate departure2 = departure.plusDays(2);
		this.room.reserve(Type.SINGLE, arrival2, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkDoubleBookingMiddle() {
		LocalDate arrival2 = arrival.plusDays(1);
		LocalDate departure2 = departure.minusDays(1);
		this.room.reserve(Type.SINGLE, arrival2, departure2);
	}

	@Test (expected = HotelException.class)
	public void checkDoubleBookingEnd() {
		LocalDate arrival2 = arrival.minusDays(2);
		LocalDate departure2 = departure.minusDays(2);
		this.room.reserve(Type.SINGLE, arrival2, departure2);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
