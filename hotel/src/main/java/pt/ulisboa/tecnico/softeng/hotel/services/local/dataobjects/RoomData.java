package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

import java.util.ArrayList;
import java.util.List;

public class RoomData {

	private String hotelCode;
	private String type;
	private String number;
	private List<RoomBookingData> bookings = new ArrayList();

	public static enum CopyDepth {
		SHALLOW, BOOKINGS
	}

	;

	public RoomData() {
	}

	public RoomData(Room room, CopyDepth depth) {
		this.hotelCode = room.getHotel().getCode();
		this.type = room.getType().name();
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

	public String getHotelCode() {
		return hotelCode;
	}

	public void setHotelCode(String hotelCode) {
		this.hotelCode = hotelCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
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
