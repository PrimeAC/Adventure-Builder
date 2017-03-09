package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.Interval;
import org.joda.time.LocalDate;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private final LocalDate arrival;
	private final LocalDate departure;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
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
		Interval thisInterval = new Interval(this.arrival.toDate().getTime(), this.departure.toDate().getTime());
		Interval interval = new Interval(arrival.toDate().getTime(), departure.toDate().getTime());

		return interval.overlaps(thisInterval);
	}

}
