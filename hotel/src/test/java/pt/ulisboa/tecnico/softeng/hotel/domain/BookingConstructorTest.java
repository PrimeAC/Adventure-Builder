package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {

	private LocalDate arrivalGlob = LocalDate.now();
	private LocalDate departureGlob = arrivalGlob.plusDays(2);

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		Booking booking = new Booking(hotel, arrivalGlob, departureGlob);

		Assert.assertTrue(booking.getReference().startsWith(hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrivalGlob, booking.getArrival());
		Assert.assertEquals(departureGlob, booking.getDeparture());
	}

	@Test(expected = HotelException.class)
	public void arrivalAfterDeparture() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate departure = arrivalGlob.minusDays(2);

		new Booking(hotel, arrivalGlob, departure);
	}

	@Test(expected = HotelException.class)
	public void arrivalBeforeToday() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = arrivalGlob.minusDays(2);

		new Booking(hotel, arrival, departureGlob);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
