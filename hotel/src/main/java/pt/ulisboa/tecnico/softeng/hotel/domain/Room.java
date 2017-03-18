package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room {
	private static Logger logger = LoggerFactory.getLogger(Room.class);

	public static enum Type {
		SINGLE, DOUBLE
	}

	private final Hotel hotel;
	private final String number;
	private final Type type;
	private final Set<Booking> bookings = new HashSet<>();

	public Room(Hotel hotel, String number, Type type) {
		checkHotel(hotel);
		checkType(type);

		this.hotel = hotel;
		this.number = number;
		this.type = type;

		this.hotel.addRoom(this);
	}

	private void checkHotel(Hotel hotel) {
		if (hotel == null) { throw new HotelException("invalid hotel"); };
	}

	private void checkType(Type type) {
		if (type == null) { throw new HotelException("invalid room type"); };
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

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(this.type)) {
			return false;
		}

		for (Booking booking : this.bookings) {
			if (booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		checkType(type);
		checkDate(arrival, departure);

		if (!isFree(type, arrival, departure)) {
			throw new HotelException("Room not available for selected dates or incorrect type");
		}

		Booking booking = new Booking(this.hotel, arrival, departure);
		this.bookings.add(booking);

		return booking;
	}

	private void checkDate(LocalDate date1, LocalDate date2) {
		if (date1 == null || date2 == null) throw new HotelException("Null date entered");
	}

}
