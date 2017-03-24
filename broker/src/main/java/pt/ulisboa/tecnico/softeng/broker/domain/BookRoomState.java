package pt.ulisboa.tecnico.softeng.broker.domain;

import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookRoomState extends AdventureState {
	@Override
	public Adventure.State getState() {
		return Adventure.State.BOOK_ROOM;
	}

	@Override
	public void process(Adventure adventure) {
		try {
			String confirmation = HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			adventure.setRoomConfirmation(confirmation);
			adventure.setState(Adventure.State.CONFIRMED);
		} catch (HotelException rae) {
			adventure.setState(Adventure.State.UNDO);
		} catch (RemoteAccessException rae) {
			incNumOfRemoteErrors();
			if (getNumOfRemoteErrors() == 10) {
				adventure.setState(Adventure.State.UNDO);
			}
		}

	}
}
