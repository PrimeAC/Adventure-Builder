package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class RoomData {

	private Hotel hotel;
	private Room.Type type;
	private String number;
	private List<RoomBookingData> bookings = new ArrayList();

	public static enum CopyDepth {
		SHALLOW, BOOKINGS
	};

	public RoomData() {}
	
	public RoomData(Room room) {
		this.hotel = room.getHotel();
		this.type = room.getType();
		this.number = room.getNumber();
	}

	public RoomData(Room room, CopyDepth depth) {
		this.hotel = room.getHotel();
		this.type = room.getType();
		this.number = room.getNumber();

		switch (depth) {
		case BOOKINGS:
			for (Booking book : room.getBookingSet()) {
				this.bookings.add(new RoomBookingData(room, book));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public Hotel getHotel() {
		return hotel;
	}

	public void setHotel(Hotel hotel) {
		this.hotel = hotel;
	}

	public Room.Type getType() {
		return type;
	}

	public void setType(Room.Type type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public List<RoomBookingData> getBookings() {
		return bookings;
	}

	public void setBookings(List<RoomBookingData> bookings) {
		this.bookings = bookings;
	}

}
