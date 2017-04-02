package pt.ulisboa.tecnico.softeng.hotel.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;


public class GetRoomBookingDataMethodTest {
	private Hotel hotel;
	private Room room;
	private Booking booking;
	private String reference;

	private static final String CODE = "Hotel12";
	private static final String NAME = "HotelXPTO";
	private static final String NUMBER = "123";
	private static final Room.Type ROOMTYPE = Room.Type.DOUBLE;
	private static final LocalDate ARRIVAL = LocalDate.now().plusDays(3);
	private static final LocalDate DEPARTURE = LocalDate.now().plusDays(5);

	@Before
	public void setup() {
		this.hotel = new Hotel(CODE, NAME);
		this.room = new Room(hotel, NUMBER, ROOMTYPE);
		this.booking = room.reserve(ROOMTYPE, ARRIVAL, DEPARTURE);
		this.reference = booking.getReference();
	}

	@Test
	public void success() {
		RoomBookingData roomBookingData = Hotel.getRoomBookingData(reference);

		assertEquals(reference, roomBookingData.getReference());
		assertEquals(CODE, roomBookingData.getHotelCode());
		assertEquals(NAME, roomBookingData.getHotelName());
		assertEquals(NUMBER, roomBookingData.getRoomNumber());
		assertEquals(ROOMTYPE, roomBookingData.getRoomType());
		assertEquals(ARRIVAL, roomBookingData.getArrival());
		assertEquals(DEPARTURE, roomBookingData.getDeparture());

		assertNull(roomBookingData.getCancellation());
		assertNull(roomBookingData.getCancellationDate());
	}

	@Test
	public void successWithCancellation() {
		booking.setCancellation(LocalDate.now());

		RoomBookingData roomBookingData = Hotel.getRoomBookingData(reference);

		assertEquals(reference, roomBookingData.getReference());
		assertEquals(booking.getCancellation(), roomBookingData.getCancellation());
		assertEquals(booking.getCancellationDate(), roomBookingData.getCancellationDate());
	}

	@Test (expected = HotelException.class)
	public void invalidReference() {
		Hotel.getRoomBookingData("Hotel12x");
	}

	@Test (expected = HotelException.class)
	public void nullReference() {
		Hotel.getRoomBookingData(null);
	}

	@Test (expected = HotelException.class)
	public void blankReference() {
		Hotel.getRoomBookingData("     ");
	}

	@Test (expected = HotelException.class)
	public void emptyReference() {
		Hotel.getRoomBookingData("");
	}

	@Test (expected = HotelException.class)
	public void noRoomsInHotel() {
		this.hotel = new Hotel("Hotel23", "HotelYPTO");
		Hotel.getRoomBookingData("Hotel231");
	}

	@Test (expected = HotelException.class)
	public void noBookingsInRoom() {
		this.room = new Room(hotel, "234", ROOMTYPE);
		Hotel.getRoomBookingData("Hotel122");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
