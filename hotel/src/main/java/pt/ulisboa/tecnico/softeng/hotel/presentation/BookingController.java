package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;

@Controller
@RequestMapping(value = "/hotels/{hotelCode}/rooms/{roomNumber}/bookings")
public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBookings(Model model, @PathVariable String hotelCode, @PathVariable String roomNumber) {
		logger.info("showBookings hotelCode:{}, roomCode:{}", hotelCode, roomNumber);

		HotelData hotelData = HotelInterface.getHotelDataByCode(hotelCode, HotelData.CopyDepth.ROOMS);
		RoomData roomData = HotelInterface.getRoomData(hotelCode, roomNumber, RoomData.CopyDepth.BOOKINGS);

		if (roomData == null) {
			model.addAttribute("error", "Error: it does not exist a room with that number " + roomNumber);
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", HotelInterface.getRooms(hotelCode));
			return "rooms";
		} else {
			model.addAttribute("booking", new RoomBookingData());
			model.addAttribute("room", roomData);
			model.addAttribute("hotel", hotelData);
			return "bookings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitBooking(Model model, @PathVariable String hotelCode, @PathVariable String roomNumber,
	                            @ModelAttribute RoomBookingData roomBookingData) {
		logger.info("submitBooking hotelCode:{}, roomNumber:{}, arrival:{}, departure:{}", hotelCode, roomNumber,
				roomBookingData.getArrival(), roomBookingData.getDeparture());

		roomBookingData.setHotelCode(hotelCode);
		roomBookingData.setRoomNumber(roomNumber);
		try {
			HotelInterface.createBooking(roomBookingData);
		} catch (HotelException be) {
			model.addAttribute("error", "Error: it was not possible to create the booking");
			model.addAttribute("booking", roomBookingData);
			model.addAttribute("room", HotelInterface.getRoomData(hotelCode, roomNumber, RoomData.CopyDepth.BOOKINGS));
			model.addAttribute("hotel", HotelInterface.getHotelDataByCode(hotelCode, HotelData.CopyDepth.ROOMS));
			return "bookings";
		}

		return "redirect:/hotels/" + hotelCode + "/rooms/" + roomNumber + "/bookings";
	}
}
