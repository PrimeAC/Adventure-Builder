package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room {
	public static enum Type {
		SINGLE, DOUBLE
	}

	private final Hotel hotel;
	private final String number;
	private final Type type;
	private final Set<Booking> bookings = new HashSet<>();

	public Room(Hotel hotel, String number, Type type) {
		checkArguments(hotel, number, type);

		this.hotel = hotel;
		this.number = number;
		this.type = type;

		this.hotel.addRoom(this);
	}

	private void checkArguments(Hotel hotel, String number, Type type) {
		if (hotel == null || number == null || number.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (!number.matches("\\d*")) {
			throw new HotelException();
		}
	}

	private void checkDates(LocalDate arrival, LocalDate departure) {
		if (arrival.equals(departure)) {
			throw new HotelException("Reservations must be for at least 1 day (i.e. 24 hours)");
		}

		if (departure.isBefore(arrival)) {
			throw new HotelException();
		}
	}

	Hotel getHotel() {
		return this.hotel;
	}

	String getNumber() {
		return this.number;
	}

	Type getType() {
		return this.type;
	}

	int getNumberOfBookings() {
		return this.bookings.size();
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		checkDates(arrival, departure);

		if (!type.equals(this.type)) {
			return false;
		}

		for (Booking booking : this.bookings) {
			if (booking.conflict(arrival, departure) && booking.getReferenceCancelled() == null) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (!isFree(type, arrival, departure)) {
			throw new HotelException();
		}

		Booking booking = new Booking(this.hotel, arrival, departure);
		this.bookings.add(booking);

		return booking;
	}

	public Set<Booking> getBookings() {
		return this.bookings;
	}
}
