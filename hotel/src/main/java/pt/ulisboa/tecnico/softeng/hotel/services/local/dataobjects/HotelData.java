package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;

public class HotelData {
	public static enum CopyDepth {
		SHALLOW, BOOKINGS, ROOMS
	};

	private String name;
	private String code;

	public HotelData() {
	}

	public HotelData(Hotel hotel, CopyDepth depth) {
		this.name = hotel.getName();
		this.code = hotel.getCode();

		switch (depth) {
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
