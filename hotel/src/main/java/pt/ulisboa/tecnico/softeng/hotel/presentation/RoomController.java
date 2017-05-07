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
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;

@Controller
@RequestMapping(value = "/hotels/{hotelCode}/rooms")
public class RoomController {

	private static Logger logger = LoggerFactory.getLogger(RoomController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showRooms(Model model, @PathVariable String hotelCode) {
		logger.info("showRooms: hotelCode:{}", hotelCode);

		HotelData hotelData = HotelInterface.getHotelDataByCode(hotelCode, HotelData.CopyDepth.ROOMS);
		
		if (hotelData == null) {
			model.addAttribute("error", "Error: no such hotel with given code");
			model.addAttribute("hotel", new HotelData());
			model.addAttribute("hotels", HotelInterface.getHotels());
			return "hotels";
		} else {
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", HotelInterface.getRooms(hotelCode));
			model.addAttribute("hotel", hotelData);
			return "rooms";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String roomSubmit(Model model, @ModelAttribute RoomData roomData, @PathVariable String hotelCode) {
		logger.info("roomSubmit hotelCode:{}, number:{}, type:{}", hotelCode, roomData.getNumber(), roomData.getType());

		HotelData hotelData = HotelInterface.getHotelDataByCode(hotelCode, HotelData.CopyDepth.ROOMS);
		roomData.setHotelCode(hotelCode);
		try {
			HotelInterface.createRoom(roomData);
		} catch (HotelException he) {
			model.addAttribute("error", "Error: it was not possible to create the room. possible types: single or double. cannot repeat room number.");
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", HotelInterface.getRooms(hotelCode));
			model.addAttribute("hotel", hotelData);
			return "rooms";
		}
		return "redirect:/hotels/" + hotelCode + "/rooms";
	}
}
