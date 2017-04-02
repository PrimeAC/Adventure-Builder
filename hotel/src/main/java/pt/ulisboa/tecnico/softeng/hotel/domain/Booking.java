package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking {
	private static int counter = 0;

	private final String reference;
	private final LocalDate arrival;
	private final LocalDate departure;
	private String referenceCancelled;
	private String cancellation;
	private LocalDate cancellationDate;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		checkArguments(hotel, arrival, departure);

		this.reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.arrival = arrival;
		this.departure = departure;
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure) {
		if (hotel == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		checkDates(arrival, departure);
	}

	private void checkDates(LocalDate arrival, LocalDate departure) {
		if (arrival.equals(departure)) {
			throw new HotelException("Reservations must be for at least 1 day (i.e. 24 hours)");
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
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

	public void setReferenceCancelled() { this.referenceCancelled = this.reference + "Cancelled"; }

	public String getReferenceCancelled() {	return this.referenceCancelled;	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		checkDates(arrival, departure);

		if ((arrival.equals(this.arrival) || arrival.isAfter(this.arrival)) && arrival.isBefore(this.departure)) {
			return true;
		}

		if ((departure.equals(this.departure) || departure.isBefore(this.departure))
				&& departure.isAfter(this.arrival)) {
			return true;
		}

		if ((arrival.isBefore(this.arrival) && departure.isAfter(this.departure))) {
			return true;
		}

		return false;
	}

	public void setCancellation(LocalDate cancelDate) {
		this.cancellation = "cancelled";
		this.cancellationDate = cancelDate;
	}

	public String getCancellation() {
		return this.cancellation;
	}

	public LocalDate getCancellationDate() {
		return this.cancellationDate;
	}

}
