package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

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
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String roomConfirmation) {
		// TODO implement
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		// TODO implement
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		// TODO: verify consistency of arguments, return the
		// references for 'number' new bookings, it does not matter if they are
		// single of double. If there aren't enough rooms available it throws a
		// hotel exception
		if(number <= 0) {
			throw new HotelException("Invalid number");
		}

		if(arrival == null || departure == null) {
			throw new HotelException("Arrival/departure dates can't be NULL");
		}

		if(departure.isBefore(arrival)) {
			throw new HotelException("Departure date must be after arrival date");
		}

		int check = 0;
		Hotel hotel = null;

		for(Hotel i : hotels) {
			while(check < number) {
				try {
					if (i.hasVacancy(Room.Type.SINGLE, arrival, departure) != null) {
						check++;
					}
				} catch (HotelException he1) {
					try {
						if (i.hasVacancy(Room.Type.DOUBLE, arrival, departure) != null) {
							check++;
						}
					} catch (HotelException he2) {
						break;
					}
				}
			}
			if(check == number) {
				hotel = i;
				break;
			}
			else {
				check = 0;
			}
		}

		if(check == number) {
			Set<String> result = null;
			for (int i = 0; i < number; i++) {
				try {
					result.add(hotel.reserveRoom(Room.Type.SINGLE, arrival, departure));
				} catch (HotelException he1) {
					try {
						result.add(hotel.reserveRoom(Room.Type.DOUBLE, arrival, departure));
					} catch (HotelException he2){
						throw new HotelException();
					}
				}
			}
			return result;
		}
		throw new HotelException();
	}

}
