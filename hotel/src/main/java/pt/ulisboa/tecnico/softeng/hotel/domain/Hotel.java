package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

import java.util.HashSet;
import java.util.Set;

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
		if (roomConfirmation == null) {
			throw new HotelException("room Confirmation Null");
		}

		for (Hotel hotel : hotels) {
			for (Room room : hotel.rooms) {
				for (Booking booking : room.getBookings()) {
					if (booking.getReference().equals(roomConfirmation) && booking.getReferenceCancelled() == null) {
						booking.setReferenceCancelled();
						return booking.getReferenceCancelled();
					}
				}
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		if (reference != null && reference.trim().length() > 0) {
			RoomBookingData roomBookingData = new RoomBookingData();

			for (Hotel hotel : Hotel.hotels) {
				for (Room room : hotel.rooms) {
					try {
						room.getBooking(reference);
					} catch (HotelException he) {
						continue;
					}
					Booking booking = room.getBooking(reference);
					roomBookingData.setReference(reference);
					roomBookingData.setHotelCode(hotel.getCode());
					roomBookingData.setHotelName(hotel.getName());
					roomBookingData.setRoomNumber(room.getNumber());
					roomBookingData.setRoomType(room.getType());
					roomBookingData.setArrival(booking.getArrival());
					roomBookingData.setDeparture(booking.getDeparture());
					roomBookingData.setCancellation(booking.getReferenceCancelled());
					roomBookingData.setCancellationDate(booking.getCancellationDate());

					return roomBookingData;
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {

		if (number <= 0) {
			throw new HotelException("Invalid number");
		}

		if (arrival == null || departure == null) {
			throw new HotelException("Arrival/departure dates can't be NULL");
		}

		if (departure.isBefore(arrival) || arrival.isEqual(departure)) {
			throw new HotelException("Departure date must be after arrival date");
		}

		int check = 0;
		Hotel hotel = null;

		for (Hotel i : hotels) {
			if (i.getNumberOfRooms() >= number) {
				for (Room j : i.rooms) {
					if (j.isFree(Room.Type.SINGLE, arrival, departure)) {
						check++;
					} else {
						if (j.isFree(Room.Type.DOUBLE, arrival, departure)) {
							check++;
						} else {
							continue;
						}
					}
					if (check == number) {
						hotel = i;
						break;
					}
				}
			}
			if (check == number) {
				break;
			} else {
				check = 0;
			}
		}
		if (hotel != null) {
			Set<String> result = new HashSet<>();
			for (int i = 0; i < number; i++) {
				Room room = hotel.hasVacancy(Room.Type.SINGLE, arrival, departure);
				if (room != null) {
					Booking reserve = room.reserve(Room.Type.SINGLE, arrival, departure);
					result.add(reserve.getReference());
				} else {
					room = hotel.hasVacancy(Room.Type.DOUBLE, arrival, departure);
					if (room != null) {
						Booking reserve = room.reserve(Room.Type.DOUBLE, arrival, departure);
						result.add(reserve.getReference());
					} else {
						throw new HotelException("Unexpected error while reserving!");
					}
				}
			}
			return result;
		}
		throw new HotelException();
	}

}
