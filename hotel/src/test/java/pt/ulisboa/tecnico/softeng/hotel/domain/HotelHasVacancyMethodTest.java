package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;

	private LocalDate arrival = LocalDate.now();
	private LocalDate departure = arrival.plusDays(4);

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void roomAvailable() {

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals("01", room.getNumber());
	}

	@Test
	public void roomTypeNotAvailable() {

		Room room = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertEquals(null, room);
	}

	@Test
	public void oneRoomNotAvailable() {
		LocalDate booked_arrival = departure.minusDays(2);
		LocalDate booked_departure = departure.plusDays(1);

		this.hotel.reserveHotel(Type.DOUBLE, booked_arrival, booked_departure);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals(null, room);
	}

	@Test
	public void twoRoomsAvailable() {
		new Room(this.hotel, "02", Type.SINGLE);

		LocalDate booked_arrival = departure.plusDays(2);
		LocalDate booked_departure = departure.plusDays(5);

		this.hotel.reserveHotel(Type.DOUBLE, booked_arrival, booked_departure);
		this.hotel.reserveHotel(Type.SINGLE, booked_arrival, booked_departure);

		Room room1 = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);
		Room room2 = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertEquals("01", room1.getNumber());
		Assert.assertEquals("02", room2.getNumber());
	}

	@Test
	public void twoRoomsNotAvailable() {
		new Room(this.hotel, "02", Type.SINGLE);

		LocalDate booked_arrival = departure.minusDays(2);
		LocalDate booked_departure = departure.plusDays(1);

		this.hotel.reserveHotel(Type.DOUBLE, booked_arrival, booked_departure);
		this.hotel.reserveHotel(Type.SINGLE, booked_arrival, booked_departure);

		Room room1 = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);
		Room room2 = this.hotel.hasVacancy(Type.SINGLE, arrival, departure);

		Assert.assertEquals(null, room1);
		Assert.assertEquals(null, room2);
	}

	@Test (expected = HotelException.class)
	public void nullRoomType() {
		this.hotel.hasVacancy(null, arrival, departure);
	}

	@Test (expected = HotelException.class)
	public void nullArrivalDate() {
		this.hotel.hasVacancy(Type.DOUBLE, null, departure);
	}

	@Test (expected = HotelException.class)
	public void nullDepartureDate() {
		this.hotel.hasVacancy(Type.DOUBLE, arrival, null);
	}
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
