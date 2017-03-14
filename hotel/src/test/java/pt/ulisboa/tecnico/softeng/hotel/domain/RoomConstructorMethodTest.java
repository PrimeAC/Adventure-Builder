package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomConstructorMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
	}

	@Test
	public void success() {
		Room room = new Room(this.hotel, "01", Type.DOUBLE);

		Assert.assertEquals(this.hotel, room.getHotel());
		Assert.assertEquals("01", room.getNumber());
		Assert.assertEquals(Type.DOUBLE, room.getType());
		Assert.assertEquals(1, this.hotel.getNumberOfRooms());
	}

	@Test (expected = HotelException.class)
	public void uniqueRoomNumberDifferentType {
		Room room1 = new Room(this.hotel, "01", Type.DOUBLE);
		Room room2 = new Room(this.hotel, "01", Type.SINGLE);
	}

	@Test (expected = HotelException.class)
	public void uniqueRoomNumberSameType {
		Room room1 = new Room(this.hotel, "01", Type.SINGLE);
		Room room2 = new Room(this.hotel, "01", Type.SINGLE);
	}

	// letter in room number
	@Test (expected = HotelException.class)
	public void letterRoomNumber() { Room room = new Room(this.hotel, "a", Type.SINGLE); }

	// special character in room number
	@Test (expected = HotelException.class)
	public void specialCharacterRoomNumber() { Room room = new Room(this.hotel, "\n", Type.SINGLE); }

	// non-ascii character in room number
	@Test (expected = HotelException.class)
	public void nonAsciiRoomNumber() { Room room = new Room(this.hotel, "á", Type.SINGLE); }

	// empty room number
	@Test (expected = HotelException.class)
	public void emptyRoomNumber() { Room room = new Room(this.hotel, "", Type.SINGLE); }

	// null room number
	@Test (expected = HotelException.class)
	public void nullRoomNumber() { Room room = new Room(this.hotel, null, Type.SINGLE); }

	// null hotel input
	@Test (expected = HotelException.class)
	public void nullHotel() { Room room = new Room(null,"01", Type.SINGLE); }

	// not a hotel class in input
	@Test (expected = HotelException.class)
	public void notHotel() { Room room = new Room("not a hotel", "01", Type.SINGLE); }

	// null room type input
	@Test (expected = HotelException.class)
	public void nullRoomType() { Room room = new Room(this.hotel,"01", null); }

	// invalid room type input
	@Test (expected = HotelException.class)
	public void invalidRoomType() { Room room = new Room(this.hotel,"01", "invalid type"); }

	public void tearDown() { Hotel.hotels.clear(); }
}
