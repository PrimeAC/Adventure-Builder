package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.Interval;
import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private final LocalDate arrival;
	private final LocalDate departure;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		validateNewDates(arrival, departure);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
	}

	public String getReference() {
		return this.reference;
	}

	LocalDate getArrival() {
		return this.arrival;
	}

	LocalDate getDeparture() {
		return this.departure;
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		validatesDates(arrival, departure);

		Interval thisInterval = new Interval(this.arrival.toDate().getTime(), this.departure.toDate().getTime());
		Interval interval = new Interval(arrival.toDate().getTime(), departure.toDate().getTime());

		return interval.overlaps(thisInterval);
	}

	private void validateNewDates(LocalDate arrival, LocalDate departure) {
		validatesDates(arrival, departure);
		if (arrival.isBefore(LocalDate.now())) {
			throw new HotelException("Arrival is before today");
		}
	}

	private void validatesDates(LocalDate arrival, LocalDate departure) {
		if (arrival.isAfter(departure)) {
			throw new HotelException("Arrival is after departure");
		}
	}

}
