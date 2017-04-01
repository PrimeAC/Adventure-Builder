package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.awt.print.Book;

public class HotelCancelBookingTest {
	private final LocalDate arrival = LocalDate.now();
	private final LocalDate departure = arrival.plusDays(5);
	private Hotel hotel;
	private Room room;
	private String roomConfirmation;

	@Before
	public void setUp() {
		this.hotel = new Hotel("CODE567", "HotelTeste");
		this.room = new Room(hotel, "01", Room.Type.SINGLE);
		this.roomConfirmation = hotel.reserveRoom(Room.Type.SINGLE, this.arrival, this.departure);
	}

	@Test
	public void success() {

		Assert.assertEquals(1, hotel.getNumberOfRooms());
		Assert.assertEquals(1, room.getNumberOfBookings());

		for (Booking booking : room.getBookings())
			Assert.assertEquals(this.hotel.cancelBooking(roomConfirmation), booking.getReferenceCancelled());
	}

	@Test(expected = HotelException.class)
	public void testRoomConfirmationNull() {
		this.hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void testBookingReferenceDifferentRoomConfirmation() {
		this.hotel.cancelBooking("Something");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
