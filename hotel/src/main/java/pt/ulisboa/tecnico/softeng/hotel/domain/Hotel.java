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
		if (StringUtils.isBlank(code) || code.contains("\n") || code.contains("\t") || code.contains("\r"))
			throw new HotelException("invalid code format (null, empty or blank characters)");

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException("code: incorrect dimension");
		}

		isUnique(code);

	}

	private void isUnique(String code) {
		for (Hotel h : hotels) {
			if (h.getCode().equals(code))
				throw new HotelException("code already in use");
		}
	}

	private void checkName(String name) {
		if (StringUtils.isBlank(name) || name.contains("\n") || name.contains("\t") || name.contains("\r"))
			throw new HotelException("invalid name format (null, empty or blank characters)");

	}



	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
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
