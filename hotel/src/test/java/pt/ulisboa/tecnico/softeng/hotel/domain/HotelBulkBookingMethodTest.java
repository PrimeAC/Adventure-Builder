package pt.ulisboa.tecnico.softeng.hotel.domain;


import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.util.Set;

public class HotelBulkBookingMethodTest {
	private final LocalDate arrival = new LocalDate(2016, 12, 19);
	private final LocalDate departure = new LocalDate(2016, 12, 21);

	private Hotel hotel1, hotel2;

	@Before
	public void setUp() {
		hotel1 = new Hotel("XPTO123", "Hilton");
		hotel2 = new Hotel("XPT4567", "Sheraton");

		new Room(this.hotel1, "01", Room.Type.SINGLE);
		new Room(this.hotel1, "02", Room.Type.DOUBLE);
		new Room(this.hotel2, "01", Room.Type.SINGLE);
		new Room(this.hotel2, "02", Room.Type.SINGLE);
		new Room(this.hotel2, "03", Room.Type.DOUBLE);
	}

	@Test(expected = HotelException.class)
	public void zeroNumber() {
		Hotel.bulkBooking(0, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void negativeNumber() {
		Hotel.bulkBooking(-5, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(2, null, departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(2, arrival, null);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {

		LocalDate badArrival = new LocalDate(2016, 12, 21);
		LocalDate badDeparture = new LocalDate(2016, 12, 19);

		Hotel.bulkBooking(2, badArrival, badDeparture);
	}

	@Test(expected = HotelException.class)
	public void departureSameDayArrival() {

		LocalDate badArrival = new LocalDate(2016, 12, 19);
		LocalDate badDeparture = new LocalDate(2016, 12, 19);

		Hotel.bulkBooking(2, badArrival, badDeparture);
	}

	@Test
	public void checkStringArrayLengthSuccess() {
		Set<String> array;

		array = Hotel.bulkBooking(2, arrival, departure);

		Assert.assertEquals(2, array.size());
	}

	@Test(expected = HotelException.class)
	public void checkNotEnoughVacancy() {

		Hotel.reserveRoom(Room.Type.SINGLE, arrival, departure);
		Hotel.reserveRoom(Room.Type.SINGLE, arrival, departure);
		Hotel.bulkBooking(3, arrival, departure);

	}

	@Test(expected = HotelException.class)
	public void checkNoHotelAvailable() {
		Hotel.bulkBooking(4, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void checkNotExistingHotels() {
		Hotel.hotels.clear();
		Hotel.bulkBooking(2, arrival, departure);
	}

	@Test
	public void reserveAllRooms() {
		Set<String> array1;
		Set<String> array2;

		array1 = Hotel.bulkBooking(3, arrival, departure);
		array2 = Hotel.bulkBooking(2, arrival, departure);

		Assert.assertEquals(3, array1.size());
		Assert.assertEquals(2, array2.size());
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();

	}

}
