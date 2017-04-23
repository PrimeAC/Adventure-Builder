package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Booking extends Booking_Base {
	private static int counter = 0;

	Booking(Hotel hotel, LocalDate arrival, LocalDate departure) {
		checkArguments(hotel, arrival, departure);

		String reference = hotel.getCode() + Integer.toString(++Booking.counter);
		this.setReference(reference);
		this.setArrival(arrival);
		this.setDeparture(departure);
	}

	private void checkArguments(Hotel hotel, LocalDate arrival, LocalDate departure) {
		if (hotel == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	boolean conflict(LocalDate arrival, LocalDate departure) {
		if (isCancelled()) {
			return false;
		}

		if (arrival.equals(departure)) {
			return true;
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}

		if ((arrival.equals(this.getArrival()) || arrival.isAfter(this.getArrival())) && arrival.isBefore(this.getDeparture())) {
			return true;
		}

		if ((departure.equals(this.getDeparture()) || departure.isBefore(this.getDeparture()))
				&& departure.isAfter(this.getArrival())) {
			return true;
		}

		if ((arrival.isBefore(this.getArrival()) && departure.isAfter(this.getDeparture()))) {
			return true;
		}

		return false;
	}

	public String cancel() {
		String cancellation = this.getReference() + "CANCEL";
		this.setCancellation(cancellation);
		this.setCancellationDate(new LocalDate());
		return cancellation;
	}

	public boolean isCancelled() {
		return this.getCancellation() != null;
	}

	public void delete() {
		this.setRoom(null);
		super.deleteDomainObject();
	}

}
