package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConflictMethodTest {
	Booking booking;

	private LocalDate bookingArrival = LocalDate.now();
	private LocalDate bookingDeparture = bookingArrival.plusDays(5);

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		this.booking = new Booking(hotel, bookingArrival, bookingDeparture);
	}

	@Test
	public void noConflictBeforeLimitCase() {
		LocalDate departure = bookingArrival;
		LocalDate arrival = departure.minusDays(3);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflictBefore() {
		LocalDate departure = bookingArrival.minusDays(1);
		LocalDate arrival = departure.minusDays(3);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflictAfterLimitCase() {
		LocalDate arrival = bookingDeparture;
		LocalDate departure = arrival.plusDays(3);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflictAfter() {
		LocalDate arrival = bookingDeparture.plusDays(1);
		LocalDate departure = arrival.plusDays(3);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictDepartureBetween() {
		LocalDate arrival = bookingArrival.minusDays(1);
		LocalDate departure = bookingDeparture.minusDays(1);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictArrivalBetween() {
		LocalDate arrival = bookingArrival.plusDays(1);
		LocalDate departure = bookingDeparture.plusDays(1);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictOldBetweenNew() {
		LocalDate arrival = bookingArrival.minusDays(1);
		LocalDate departure = bookingDeparture.plusDays(1);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictNewBetweenOld() {
		LocalDate arrival = bookingArrival.plusDays(1);
		LocalDate departure = bookingDeparture.minusDays(1);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test
	public void conflictSameDates() {
		LocalDate arrival = bookingArrival;
		LocalDate departure = bookingDeparture;

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@Test(expected = HotelException.class)
	public void exceptionReversedDates() {
		LocalDate arrival = bookingDeparture;
		LocalDate departure = bookingArrival;

		this.booking.conflict(arrival, departure);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
