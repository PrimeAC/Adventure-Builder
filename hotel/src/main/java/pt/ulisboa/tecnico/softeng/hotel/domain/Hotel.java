package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkCode(code);
		checkName(name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkCode(String code) {
		if (!isFormatOk(code) || !isLengthOk(code)) throw new HotelException("Invalid code format");
		if (!isUniqueHotel(code)) throw new HotelException("Code in use");
	}

	private void checkName(String name) {
		if (!isFormatOk(name)) throw new HotelException("Invalid name format (null, empty or blank characters)");

	}

	private void checkRoom(Room room) {
		String number = room.getNumber();
		if (!isRoomNumberOk(number) || !isUniqueRoom(number)) throw new HotelException("Invalid room");
	}

	private boolean isFormatOk(String cn) {
		return !(StringUtils.isBlank(cn) || cn.contains("\n") || cn.contains("\t") || cn.contains("\r"));
	}

	private boolean isLengthOk(String code) {
		return (code.length() == Hotel.CODE_SIZE);
	}

	private boolean isUniqueHotel(String code) {
		for (Hotel h : hotels) {
			if (h.getCode().equals(code))
				return false;
		}
		return true;
	}

	private boolean isUniqueRoom(String number) {
		for (Room room : rooms) {
			if (room.getNumber().equals(number))
				return false;
		}
		return true;
	}

	private boolean isRoomNumberOk(String number) {
		// check if non-number character present in room number
		try {
			int n = Integer.parseInt(number);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null) { throw new HotelException("Type Argument null"); }
		else if (arrival == null) { throw new HotelException("Arrival Argument null"); }
		else if (departure == null) { throw new HotelException("Departure Argument null"); }

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	void addRoom(Room room) {
		checkRoom(room);
		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public static String reserveHotel(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		return null;
	}

}
