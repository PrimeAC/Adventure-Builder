package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomReserveMethodTest {
	Room room;

	LocalDate arrival = LocalDate.now();
	LocalDate departure = arrival.plusDays(4);

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {

		Booking booking = this.room.reserve(Type.SINGLE, arrival, departure);

		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
